package org.cisiondata.modules.elasticsearch.controller;

import org.cisiondata.modules.abstr.web.ResultCode;
import org.cisiondata.modules.abstr.web.WebResult;
import org.cisiondata.modules.elasticsearch.entity.ESMetadata;
import org.cisiondata.modules.elasticsearch.service.IESMetadataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ESMetadataController {
	
	private Logger LOG = LoggerFactory.getLogger(ESMetadataController.class);
	
	@Autowired
	private IESMetadataService esMetadataService = null;
	
	@ResponseBody
	@RequestMapping(value = "/indices/types/metadatas")
	public WebResult readIndicesTypesMetadatas() {
		WebResult result = new WebResult();
		try {
			Object data = esMetadataService.readMetadatas();
			result.setCode(ResultCode.SUCCESS.getCode());
			result.setData(data);
		} catch (Exception e) {
			result.setCode(ResultCode.FAILURE.getCode());
			result.setFailure(e.getMessage());
			LOG.error(e.getMessage(), e);
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/indices")
	public WebResult readIndices() {
		WebResult result = new WebResult();
		try {
			Object data = esMetadataService.readIndices();
			result.setCode(ResultCode.SUCCESS.getCode());
			result.setData(data);
		} catch (Exception e) {
			result.setCode(ResultCode.FAILURE.getCode());
			result.setFailure(e.getMessage());
			LOG.error(e.getMessage(), e);
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/{index}/types")
	public WebResult readIndexTypes(@PathVariable String index) {
		WebResult result = new WebResult();
		try {
			Object data = esMetadataService.readIndexTypes(index);
			result.setCode(ResultCode.SUCCESS.getCode());
			result.setData(data);
		} catch (Exception e) {
			result.setCode(ResultCode.FAILURE.getCode());
			result.setFailure(e.getMessage());
			LOG.error(e.getMessage(), e);
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/indices/types/all")
	public WebResult readIndiciesTypesMetadatas() {
		WebResult result = new WebResult();
		try {
			Object data = esMetadataService.readIndicesTypes();
			result.setCode(ResultCode.SUCCESS.getCode());
			result.setData(data);
		} catch (Exception e) {
			result.setCode(ResultCode.FAILURE.getCode());
			result.setFailure(e.getMessage());
			LOG.error(e.getMessage(), e);
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/{index}/{type}/attributes")
	public WebResult readIndexTypeAttributes(@PathVariable String index, @PathVariable String type) {
		WebResult result = new WebResult();
		try {
			Object data = esMetadataService.readIndexTypeAttributes(index, type);
			result.setCode(ResultCode.SUCCESS.getCode());
			result.setData(data);
		} catch (Exception e) {
			result.setCode(ResultCode.FAILURE.getCode());
			result.setFailure(e.getMessage());
			LOG.error(e.getMessage(), e);
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/indices/types/metadatas/cleanup")
	public WebResult updateMetadatasNull() {
		WebResult result = new WebResult();
		try {
			esMetadataService.updateMetadatasNull();
			result.setCode(ResultCode.SUCCESS.getCode());
			result.setData(null);
		} catch (Exception e) {
			result.setCode(ResultCode.FAILURE.getCode());
			result.setFailure(e.getMessage());
			LOG.error(e.getMessage(), e);
		}
		return result;
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/statistics/show")
	public WebResult statisticsIndexType(){
		WebResult result = new WebResult();
		try {
			Object data = esMetadataService.readESStatisticsDatas();
			result.setCode(ResultCode.SUCCESS.getCode());
			result.setData(data);
			
		} catch (Exception e) {
			result.setCode(ResultCode.FAILURE.getCode());
			result.setFailure(e.getMessage());
			LOG.error(e.getMessage(), e);
		}
		return result;
	}
	
	@RequestMapping(value="statistics", method = RequestMethod.GET)
	public ModelAndView statisticsIndexTypeView(){
		return new ModelAndView("/es/statistics");
	}
		
	//根据type查询
	@ResponseBody
	@RequestMapping(value = "/dataType")
	public WebResult findTypes(String type){
		 WebResult result = new WebResult();
			try{
				Object data = esMetadataService.findType(type);
				result.setCode(ResultCode.SUCCESS.getCode());
				result.setData(data);
			}catch  (Exception e){
				result.setCode(ResultCode.FAILURE.getCode());
				result.setFailure(e.getMessage());
				LOG.error(e.getMessage(), e);
			}
		return result;
	}
	
	//根据id删除
	@ResponseBody
	@RequestMapping(value = "/dataTypeId")
	public WebResult deleteType(int id){
		 WebResult result = new WebResult();
			try{
				int data = esMetadataService.deleteId(id);
				result.setCode(ResultCode.SUCCESS.getCode());
				result.setData(data);
			}catch  (Exception e){
				result.setCode(ResultCode.FAILURE.getCode());
				result.setFailure(e.getMessage());
				LOG.error(e.getMessage(), e);
			}
		return result;
	}
	
	//添加
	@ResponseBody
	@RequestMapping(value = "/saveData")
	public WebResult savedata(String indexs,String type,String attribute_en, String attribute_ch){
		 WebResult result = new WebResult();
			try{
				ESMetadata metadata = new ESMetadata();
				metadata.setIndexs(indexs);
				metadata.setType(type);
				metadata.setAttribute_en(attribute_en);
				metadata.setAttribute_ch(attribute_ch);
				esMetadataService.save(metadata);
				result.setCode(ResultCode.SUCCESS.getCode());
			}catch  (Exception e){
				result.setCode(ResultCode.FAILURE.getCode());
				result.setFailure(e.getMessage());
				LOG.error(e.getMessage(), e);
			}
		return result;
	}
	
	//修改
	@ResponseBody
	@RequestMapping(value = "/updateDate")
	public WebResult updateId(Long id,String indexs,String type,String attribute_en, String attribute_ch){
		 WebResult result = new WebResult();
			try{
				ESMetadata metadata = new ESMetadata();
				metadata.setId(id);
				metadata.setIndexs(indexs);
				metadata.setType(type);
				metadata.setAttribute_en(attribute_en);
				metadata.setAttribute_ch(attribute_ch);
				int data = esMetadataService.updateId(metadata);
				result.setCode(ResultCode.SUCCESS.getCode());
				result.setData(data);
			}catch  (Exception e){
				result.setCode(ResultCode.FAILURE.getCode());
				result.setFailure(e.getMessage());
				LOG.error(e.getMessage(), e);
			}
		return result;
	}
}
