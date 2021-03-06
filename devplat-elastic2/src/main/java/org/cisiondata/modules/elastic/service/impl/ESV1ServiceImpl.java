package org.cisiondata.modules.elastic.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.cisiondata.modules.abstr.entity.QueryResult;
import org.cisiondata.modules.elastic.client.ESClient;
import org.cisiondata.modules.elastic.service.IESV1Service;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.search.MultiSearchRequestBuilder;
import org.elasticsearch.action.search.MultiSearchResponse.Item;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;

public class ESV1ServiceImpl extends AbstractESServiceV1Impl implements IESV1Service {
	
	@Override
	public List<Map<String, Object>> readDataListByCondition(QueryBuilder query, int size, 
			boolean isHighLight){
		return readDataList(null, new String[0], query, size, isHighLight);
	}

	@Override
	public List<Map<String, Object>> readDataListByCondition(String index, String type, 
			QueryBuilder query, boolean isHighLight){
		return readDataListByCondition(index, new String[]{type}, query, isHighLight);
	}
	
	@Override
	public List<Object> readDataListByCondition(String index, String type, String attribute, 
			List<String> attributeValues, int size){
		Client client = ESClient.getInstance().getClient();
		MultiSearchRequestBuilder msrb = client.prepareMultiSearch();
		for (int i = 0, len = attributeValues.size(); i < len; i++) {
			SearchRequestBuilder srb = client.prepareSearch(index).setTypes(type);
			srb.setQuery(QueryBuilders.termQuery(attribute, attributeValues.get(i)));
			srb.setSearchType(SearchType.QUERY_AND_FETCH);
			srb.setSize(size).setExplain(false);
			msrb.add(srb);
		}
		Item[] items = msrb.execute().actionGet().getResponses();
		List<Object> resultList = new ArrayList<Object>();
		for (int i = 0, len = items.length; i < len; i++) {
			SearchHits searchHits = items[i].getResponse().getHits();
			List<Map<String, Object>> hitResultList = new ArrayList<Map<String, Object>>(); 
			if (searchHits.getTotalHits() == 0) {
				hitResultList.add(new HashMap<String, Object>());
			} else {
				SearchHit[] hits = searchHits.getHits();
				for (int j = 0, jLen = hits.length; j < jLen; j++) {
					hitResultList.add(hits[j].getSource());
				}
			}
			resultList.add(hitResultList);
		}
		return resultList;
	}
	
	@Override
	public List<Map<String, Object>> readDataListByCondition(String index, String[] types, 
			QueryBuilder query, boolean isHighLight){
		return readDataList(index, types, query, 200, isHighLight);
	}
	
	@Override
	public QueryResult<Map<String, Object>> readPaginationDataListByCondition(QueryBuilder query, 
			String scrollId, int size){
		return readPaginationDataListByCondition(null, new String[0], query, scrollId, size, false);
	}
	
	@Override
	public QueryResult<Map<String, Object>> readPaginationDataListByCondition(String index, String type,
			QueryBuilder query, String scrollId, int size, boolean isHighLight){
		return readPaginationDataListByCondition(index, new String[]{type}, query, scrollId, size, isHighLight);
	}
	
	@Override
	public QueryResult<Map<String, Object>> readPaginationDataListByCondition(String index, String type, 
			QueryBuilder query, SearchType searchType, String scrollId, int size, boolean isHighLight){
		return readPaginationDataListByCondition(index, new String[]{type}, query, 
				searchType, scrollId, size, isHighLight);
	}
	
	@Override
	public QueryResult<Map<String, Object>> readPaginationDataListByCondition(String index, String[] types,
			QueryBuilder query, String scrollId, int size, boolean isHighLight){
		return readPaginationDataList(index, types, query, scrollId, size, isHighLight);
	}
	
	@Override
	public QueryResult<Map<String, Object>> readPaginationDataListByCondition(String index, String[] types,
			QueryBuilder query, SearchType searchType, String scrollId, int size, 
				boolean isHighLight){
		return readPaginationDataList(index, types, query, searchType, scrollId, size, isHighLight);
	}
	
	private String[] buildIndices(String index, Set<String> defaultIndices) {
		return (StringUtils.isBlank(index)) ? defaultIndices.toArray(new String[0]) : new String[]{index};
	}
	
	private String[] buildTypes(String[] types, Set<String> defaultTypes) {
		return (null == types || types.length == 0) ? defaultTypes.toArray(new String[0]) : types;
	}
	
	private SearchRequestBuilder buildSearchRequestBuilder(String qindex, String[] qtypes) {
		return ESClient.getInstance().getClient().prepareSearch(
				buildIndices(qindex, indices)).setTypes(buildTypes(qtypes, types));
	}
	
	private void wrapperSearchRequestBuilder(SearchRequestBuilder searchRequestBuilder) {
		searchRequestBuilder.setExplain(false);
        searchRequestBuilder.setHighlighterPreTags("<span style=\"color:red\">");
        searchRequestBuilder.setHighlighterPostTags("</span>");
        for (String attri : all_attributes) {
        	searchRequestBuilder.addHighlightedField(attri);
        }
	}
	
	private boolean indicesExists(String index) {
		if (StringUtils.isBlank(index)) return true;
		IndicesExistsRequest indicesExistsRequest = new IndicesExistsRequest(index);
		IndicesExistsResponse indicesExistsResponse = ESClient.getInstance().getClient()
				.admin().indices().exists(indicesExistsRequest).actionGet();
		return indicesExistsResponse.isExists();
	}
	
	private List<Map<String, Object>> readDataList(String index, String[] types, QueryBuilder query, 
			int size, boolean isHighLight){
		if (!indicesExists(index)) return new ArrayList<Map<String,Object>>();
		SearchRequestBuilder searchRequestBuilder = buildSearchRequestBuilder(index, types);
		searchRequestBuilder.setQuery(query);
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		wrapperSearchRequestBuilder(searchRequestBuilder);
		searchRequestBuilder.setSize(size);
		SearchResponse response = searchRequestBuilder.execute().actionGet();
		SearchHit[] hitArray = response.getHits().getHits();
		return buildResultList(hitArray, false, isHighLight);
	}
	
	private QueryResult<Map<String, Object>> readPaginationDataList(String index, String[] types,
		QueryBuilder queryBuilder, String scrollId, int size, boolean isHighLight){
		return null == queryBuilder ? new QueryResult<Map<String, Object>>() : 
			readPaginationDataList(index, types, queryBuilder, SearchType.DFS_QUERY_THEN_FETCH, 
				scrollId, size, isHighLight);
	}
	
	private QueryResult<Map<String, Object>> readPaginationDataList(String index, String[] types,
			QueryBuilder query, SearchType searchType, String scrollId, int size, boolean isHighLight){
		if (!indicesExists(index)) return new QueryResult<Map<String,Object>>();
		SearchRequestBuilder searchRequestBuilder = buildSearchRequestBuilder(index, types);
		searchRequestBuilder.setQuery(query);
		searchRequestBuilder.setSearchType(searchType);
		wrapperSearchRequestBuilder(searchRequestBuilder);
        searchRequestBuilder.setScroll(TimeValue.timeValueMinutes(3));
        searchRequestBuilder.setSize(size);
		SearchResponse response = searchRequestBuilder.execute().actionGet();
		if (StringUtils.isNotBlank(scrollId)) {
			response = ESClient.getInstance().getClient().prepareSearchScroll(scrollId)
					.setScroll(TimeValue.timeValueMinutes(3)).execute().actionGet();
		}
		SearchHit[] hitArray = response.getHits().getHits();
		QueryResult<Map<String, Object>> qr = new QueryResult<Map<String, Object>>();
		qr.setTotalRowNum(response.getHits().getTotalHits());
		qr.setScrollId(response.getScrollId());
		qr.setResultList(buildResultList(hitArray, true, isHighLight));
		return qr;
	}
	
	private List<Map<String, Object>> buildResultList(SearchHit[] hitArray, 
			boolean isPagination, boolean isHighLight) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		SearchHit hit = null;
		String key = null;
		Map<String, Object> source = null;
		Map<String, Object> replaceSource = null;
		for (int i = 0, len = hitArray.length; i < len; i++) {
			hit = hitArray[i];
			source = hit.getSource();
			if (isHighLight) {
				wrapperHighLight(source, hit.getHighlightFields());
			}
			replaceSource = new HashMap<String, Object>();
			for (Map.Entry<String, Object> entry : source.entrySet()) {
				key = entry.getKey();
				if (filter_attributes.contains(key) || key.endsWith("Alias")) continue;
				replaceSource.put(key, wrapperValue(key, entry.getValue()));
			}
			if (isPagination) {
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("index", hit.getIndex());
				result.put("type", hit.getType());
				result.put("data", replaceSource);
				resultList.add(result);
			} else {
				replaceSource.put("index", hit.getIndex());
				replaceSource.put("type", hit.getType());
				resultList.add(replaceSource);
			}
		}
		return resultList;
	}
	
	private void wrapperHighLight(Map<String, Object> source, Map<String, HighlightField> highLightFields) {
		String entryKey = null;
		Object entryValue = null;
		for (Map.Entry<String, Object> entry : source.entrySet()) {
			entryKey = entry.getKey();
			if (!highLightFields.containsKey(entryKey)) continue;
			Text[] texts = highLightFields.get(entryKey).getFragments();
			StringBuilder highLightText = new StringBuilder(100);
			for (int i = 0, tlen = texts.length; i < tlen; i++) {
				highLightText.append(texts[i]);
			}
			if (highLightText.length() > 0) entryValue = highLightText.toString();
			entry.setValue(entryValue);
		}
	}
	
	protected Object wrapperValue(String key, Object value) {
		return value;
	}
	
}

