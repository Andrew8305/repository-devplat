package org.cisiondata.modules.identity.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.cisiondata.modules.abstr.entity.QueryResult;
import org.cisiondata.modules.abstr.web.ResultCode;
import org.cisiondata.modules.identity.service.IQQService;
import org.cisiondata.modules.search.service.IESBizService;
import org.cisiondata.utils.exception.BusinessException;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Service;

@Service("qqService")
public class QQServiceImpl implements IQQService {
	
	@Resource(name = "esBizService")
	private IESBizService esBizService = null;
	
	//根据QQ号码得到群详情和群昵称
	@Override
	public List<Map<String, Object>> readQQDatas(String qq) {
		String  regex ="[1-9][0-9]{4,14}";
		Pattern p = Pattern.compile(regex);  
		Matcher m = p.matcher(qq); 
		List<String> listqun = new ArrayList<String>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
		if(m.matches()){
			List<String> listQ = AllList(qq);
			if (listQ.size() > 0) {
				BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
				for (int i = 0; i < listQ.size(); i++) {
					boolQueryBuilder.should(QueryBuilders.termQuery("qunNum", listQ.get(i)));
				}
				listMap = esBizService.readDataListByCondition("qq", "qqqundata", boolQueryBuilder, true);
				for (int i = 0; i < listMap.size(); i++) {
					listqun.add(listMap.get(i).get("QQ群号").toString());
					list.add(listMap.get(i));
				}
				// 开始
				if (listqun.size() > 0) {
					for (int i = 0; i < listqun.size(); i++) {
						BoolQueryBuilder qb = QueryBuilders.boolQuery();
						qb.must(QueryBuilders.termQuery("qunNum", listqun.get(i)))
						.must(QueryBuilders.termQuery("qqNum", qq));
						List<Map<String, Object>> listnick = esBizService.readDataListByCondition(
								"qq", "qqqunrelation", qb, true);
						for (int j = 0; j < listnick.size(); j++) {
							list.get(i).put("nick", listnick.get(j).get("QQ昵称"));
						}
					}
				}
			}else{
				throw new BusinessException(ResultCode.NOT_FOUNT_DATA.getCode(),ResultCode.NOT_FOUNT_DATA.getDesc());
			}
			for (int i = 0,len = list.size(); i < len; i++) {
				list.get(i).remove("更新时间");
				list.get(i).remove("源文件");
				list.get(i).remove("录入时间");
				list.get(i).remove("index");
				list.get(i).remove("type");
				list.get(i).remove("clazz");
			}
		}else{
			throw new BusinessException(ResultCode.PARAM_ERROR);
		}
		if(list.size() == 0){
			throw new BusinessException(ResultCode.NOT_FOUNT_DATA.getCode(),ResultCode.NOT_FOUNT_DATA.getDesc());
		}
		return list;
	}
	//根据QQ群号获取QQ群名称、QQ群通知字段
	public Map<String, Object> readQQqun(String qunNum){
		Map<String, Object> map = new HashMap<String,Object>();
		List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.should(QueryBuilders.termQuery("qunNum", qunNum));
		listMap = esBizService.readDataListByCondition("qq", "qqqundata", boolQueryBuilder, true);
		for (int i = 0; i < listMap.size(); i++) {
			map.put("群名称", listMap.get(i).get("群名称").toString());
			map.put("群通知", listMap.get(i).get("群通知").toString());
		}
		return map;
	}
	// 根据QQ号获取QQ群号
	private List<String> AllList(String qq) {
		List<Map<String, Object>> listMap = esBizService.readDataListByCondition(
				"qq", "qqqunrelation", QueryBuilders.termQuery("qqNum", qq), true);
		List<String> list = new ArrayList<String>();
		for (int i = 0, len = listMap.size(); i < len; i++) {
			list.add(listMap.get(i).get("QQ群号").toString());
		}
		return list;
	}

	@Override
	// 根据QQ群号得到对应的群信息
	public List<Map<String, Object>> readQQqunDatas(String qunNum) {
		String  regex ="[1-9][0-9]{4,14}";
		Pattern p = Pattern.compile(regex);  
		Matcher m = p.matcher(qunNum);
		List<Map<String, Object>> list = esBizService.readDataListByCondition(
				"qq", "qqqunrelation", QueryBuilders.termQuery("qunNum", qunNum), true);
		if(m.matches()){
			Map<String, Object> map = readQQqun(qunNum);
			for (int i = 0; i < list.size(); i++) {
				list.get(i).remove("更新时间");
				list.get(i).remove("源文件");
				list.get(i).remove("录入时间");
				list.get(i).remove("index");
				list.get(i).remove("type");
				for (Map.Entry<String, Object> entry:map.entrySet()) {
					list.get(i).put(entry.getKey(), entry.getValue());
				}
			}
		}else{
			throw new BusinessException(ResultCode.PARAM_ERROR);
		}
		if(list.size() == 0){
			throw new BusinessException(ResultCode.NOT_FOUNT_DATA.getCode(),ResultCode.NOT_FOUNT_DATA.getDesc());
		}
		return list;
	}

	@Override
	// 根据QQ号得到基本信息
	public List<Map<String, Object>> readQQData(String qq) {
		String  regex ="[1-9][0-9]{4,14}";
		Pattern p = Pattern.compile(regex);  
		Matcher m = p.matcher(qq); 
		List<Map<String, Object>> list = esBizService.readDataListByCondition(
				"qq", "qqdata", QueryBuilders.termQuery("qqNum", qq), true);
		if(m.matches()){
			for (int i = 0; i < list.size(); i++) {
				list.get(i).remove("更新时间");
				list.get(i).remove("源文件");
				list.get(i).remove("录入时间");
				list.get(i).remove("index");
				list.get(i).remove("type");
			}
		}else{
			throw new BusinessException(ResultCode.PARAM_ERROR);
		}
		if(list.size() == 0){
			throw new BusinessException(ResultCode.NOT_FOUNT_DATA.getCode(),ResultCode.NOT_FOUNT_DATA.getDesc());
		}
		return list;
	}

	//通过QQ昵称查询
	@Override
	@SuppressWarnings("unchecked")
	public QueryResult<Map<String, Object>> readQQNickData(String nick, String scrollId, int rowNumPerPage) {
		if (! StringUtils.isNotBlank(nick)) {
			throw new BusinessException(ResultCode.PARAM_NULL);
		}
		QueryResult<Map<String, Object>> qr = esBizService.readPaginationDataListByCondition("qq", "qqqunrelation", 
				QueryBuilders.matchPhraseQuery("nick", nick), scrollId, rowNumPerPage, true);
		List<Map<String, Object>> resultList = qr.getResultList();
		for (int i = 0; i < resultList.size(); i++) {
			Map<String, Object> data =(Map<String, Object>) resultList.get(i).get("data");
			String qunNum = (String) data.get("QQ群号");
			//通过qunNum获取群名称 群通知
			List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
			BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
			boolQueryBuilder.should(QueryBuilders.termQuery("qunNum", qunNum));
			listMap = esBizService.readDataListByCondition("qq", "qqqundata", boolQueryBuilder, true);
			for (int j = 0; j < listMap.size(); j++) {
				data.put("群名称", listMap.get(j).get("群名称").toString());
				data.put("群通知", listMap.get(j).get("群通知").toString());
			}
		}
		return qr;
	}
	
	//通过qq号查询qq群信息及群成员信息
	@Override
	public Map<String, Object> readQQAndQunDatas(String qqNum) {
		Map<String, Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> qunBaseList = readQQDatas(qqNum);
		map.put("qunBase", qunBaseList);
		List<String> qunNumList = new ArrayList<String>();
		for(Map<String,Object> qunBaseObj : qunBaseList){
			if(qunBaseObj.containsKey("QQ群号") && !"NA".equals(qunBaseObj.get("QQ群号")) && !"".equals(qunBaseObj.get("QQ群号"))){
				qunNumList.add((String)qunBaseObj.get("QQ群号"));
			}
		}
		List<Object> qunList = esBizService.readDataListByCondition("qq", "qqqunrelation", "qunNum", qunNumList, 200);
		map.put("qun", qunList);
		return map;
	}
}