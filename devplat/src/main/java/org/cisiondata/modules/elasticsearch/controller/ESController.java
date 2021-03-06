package org.cisiondata.modules.elasticsearch.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.cisiondata.modules.abstr.web.ResultCode;
import org.cisiondata.modules.abstr.web.WebResult;
import org.cisiondata.modules.elasticsearch.service.IESBizService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/")
public class ESController {
	
	private Logger LOG = LoggerFactory.getLogger(ESController.class);
	
	@Resource(name = "esBizService")
	private IESBizService esBizService = null;
	
	/** 
	 * 超级查询
	 * @param query
	 * @param scrollId
	 * @param rowNumPerPage
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/search")
	public WebResult readPaginationDataListByCondition(String query, String scrollId, int rowNumPerPage) {
		LOG.info("query:{} scrollId:{} rowNumPerPage:{}", query, scrollId, rowNumPerPage);
		WebResult result = new WebResult();
		try {
			Object data = esBizService.readPaginationDataListByCondition(query, scrollId, rowNumPerPage);
			result.setCode(ResultCode.SUCCESS.getCode());
			result.setData(data);
		} catch (Exception e) {
			result.setCode(ResultCode.FAILURE.getCode());
			result.setFailure(e.getMessage());
			LOG.error(e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * 多字段查询界面
	 * @return
	 */
	@RequestMapping(value="/search/multi")
	public ModelAndView readIndexTypeDatasByMultiFieldsView (){
		return new ModelAndView("es/multi");
	}
	
	/**
	 * 多字段查询
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/search/multi/scroll")
	@ResponseBody
	public WebResult readIndexTypeDatasByMultiFields(@RequestParam Map<String,String> map){
		WebResult result = new WebResult();
		try {
			result.setData(esBizService.readPaginationDataListByMultiCondition(map));
			result.setCode(ResultCode.SUCCESS.getCode());
		} catch (Exception e) {
			result.setCode(ResultCode.FAILURE.getCode());
			result.setFailure(e.getMessage());
			LOG.error(e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * 物流信息查询界面
	 * @return
	 */
	@RequestMapping(value="/logistics/search/view")
	public ModelAndView readFinancialLogisticsDatasView(){
		return new ModelAndView("user/amap");
	}
	
	/**
	 * 物流信息查询(未分页)
	 * @param query
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/logistics/search")
	public WebResult readFinancialLogisticsDatas(String query) {
		LOG.info("query:{}", query);
		WebResult result = new WebResult();
		try {
			Object data = esBizService.readLogisticsDataList(query);
			result.setCode(ResultCode.SUCCESS.getCode());
			result.setData(data);
		} catch (Exception e) {
			result.setCode(ResultCode.FAILURE.getCode());
			result.setFailure(e.getMessage());
			LOG.error(e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * 物流关系查询
	 * @param query
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/logistics/relations/search")
	public WebResult readFinancialLogisticsRelationsDatas(String query) {
		LOG.info("query:{}", query);
		WebResult result = new WebResult();
		try {
			Object data = esBizService.readLogisticsDataList(query);
			result.setCode(ResultCode.SUCCESS.getCode());
			result.setData(data);
		} catch (Exception e) {
			result.setCode(ResultCode.FAILURE.getCode());
			result.setFailure(e.getMessage());
			LOG.error(e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * 标签查询视图
	 * @return
	 */
	@RequestMapping(value ="/labels/search/view", method = RequestMethod.GET)
	public ModelAndView readLabelsAndHitsView(){
		return new ModelAndView("/es/labels");
	}
	
	/**
	 * 标签查询
	 * @param query
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/labels/search")
	public WebResult readLabelsAndHits(String query) {
		LOG.info("query:{}", query);
		WebResult result = new WebResult();
		try {
			Object data = esBizService.readLabelsAndHitsIncludeTypes(query);
			result.setCode(ResultCode.SUCCESS.getCode());
			result.setData(data);
		} catch (Exception e) {
			result.setCode(ResultCode.FAILURE.getCode());
			result.setFailure(e.getMessage());
			LOG.error(e.getMessage(), e);
		}
		return result;
	} 
	
	/**
	 * 指定标签查询
	 * @param query
	 * @param includeTypes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/labels/include/search")
	public WebResult readIncludeLabelsAndHits(String query, String[] includeTypes) {
		LOG.info("query:{} include types:{}", query, includeTypes);
		WebResult result = new WebResult();
		try {
			Object data = esBizService.readLabelsAndHitsIncludeTypes(query, includeTypes);
			result.setCode(ResultCode.SUCCESS.getCode());
			result.setData(data);
		} catch (Exception e) {
			result.setCode(ResultCode.FAILURE.getCode());
			result.setFailure(e.getMessage());
			LOG.error(e.getMessage(), e);
		}
		return result;
	} 
	
	/**
	 * 排除指定标签查询
	 * @param query
	 * @param excludeTypes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/labels/exclude/search")
	public WebResult readExcludeLabelsAndHits(String query, String[] excludeTypes) {
		LOG.info("query:{} exclude types:{}", query, excludeTypes);
		WebResult result = new WebResult();
		try {
			Object data = esBizService.readLabelsAndHitsExcludeTypes(query, excludeTypes);
			result.setCode(ResultCode.SUCCESS.getCode());
			result.setData(data);
		} catch (Exception e) {
			result.setCode(ResultCode.FAILURE.getCode());
			result.setFailure(e.getMessage());
			LOG.error(e.getMessage(), e);
		}
		return result;
	} 
	
	/**
	 * 指定库表查询
	 * @param index
	 * @param type
	 * @param query
	 * @param scrollId
	 * @param rowNumPerPage
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/index/{index}/type/{type}/search")
	public WebResult readLabelPaginationDataList(@PathVariable String index, @PathVariable String type,
			String query, String scrollId, int rowNumPerPage) {
		LOG.info("index:{} type:{} query:{} scrollId:{} rowNumPerPage:{}", index, type,
				query, scrollId, rowNumPerPage);
		WebResult result = new WebResult();
		try {
			Object data = esBizService.readLabelPaginationDataList(index, type, query, scrollId, rowNumPerPage);
			result.setCode(ResultCode.SUCCESS.getCode());
			result.setData(data);
		} catch (Exception e) {
			result.setCode(ResultCode.FAILURE.getCode());
			result.setFailure(e.getMessage());
			LOG.error(e.getMessage(), e);
		}
		return result;
	}
	
}
