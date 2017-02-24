package org.cisiondata.modules.datainterface.service.impl;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.cisiondata.modules.datainterface.URLUtil;
import org.cisiondata.modules.datainterface.service.IWLService;
import org.cisiondata.utils.http.HttpUtils;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Service("wlService")
public class WLServiceImpl implements IWLService {
	public static final String URL = "http://api.wolongdata.com:8888/portrait/active/get/";
	public static final String app_key = "3926502285";
	public static final String app_secret = "g4hyxjqrkcmlqe9g1meb74qlo2ix4j95";

	@SuppressWarnings("unchecked")
	public Map<String, Object> readData(String phone) throws NoSuchAlgorithmException {
		HashMap<String, String> data = new HashMap<String, String>();
		long date = System.currentTimeMillis() / 1000L;
		data.put("enc_m", phone);
		data.put("t", String.valueOf(date));
		data.put("app_key", app_key);
		//测试数据
//		String data1 = "{\"msg\": \"\u6210\u529f\", \"code\": 1200, \"data\": {\"info_list\": [{\"m_abnormal_cnt\": \"0\", \"basic_city\": \"\u56db\u5ddd \u6210\u90fd\", \"basic_gender\": \"\u7537\", \"m_std_month_price\": \"1293.28\", \"tag\": {\"\u662f\u5426\u6bcd\u5a74\u4eba\u7fa4\": 1, \"\u662f\u5426\u6469\u6258\u8f66\u5468\u8fb9\u4ea7\u54c1\u7231\u597d\u4eba\u7fa4\": 1, \"\u662f\u5426\u6c7d\u8f66\u5468\u8fb9\u4ea7\u54c1\u7231\u597d\u4eba\u7fa4\": 1, \"\u662f\u5426\u5c45\u5bb6\u504f\u597d\u4eba\u7fa4\": 1, \"\u662f\u5426\u65b0\u5bb6\u88c5\u4eba\u7fa4\": 1}, \"consume_year\": \"6\", \"consume_auth\": \"1\", \"favor_cat\": {\"\u7f51\u7edc\u76f8\u5173\": \"12.96%\", \"DIY\u7535\u8111\": \"21.37%\", \"\u7535\u8111\u5468\u8fb9\": \"17.51%\", \"\u6c7d\u8f66\u7528\u54c1\": \"15.88%\", \"\u5b58\u50a8\u8bbe\u5907\": \"23.74%\"}, \"m_b_cnt_ratio\": \"26.67%\", \"m_general_ratio\": \"4.26\", \"m_bfifty_cnt_ratio\": \"51.11%\", \"m_bfifty_price_ratio\": \"3.47%\", \"basic_age\": \"\", \"favor_comp_avg\": [{\"category\": \"\u5b58\u50a8\u8bbe\u5907\", \"comp_avg\": \"4500.19%\"}, {\"category\": \"DIY\u7535\u8111\", \"comp_avg\": \"68.32%\"}, {\"category\": \"\u7535\u8111\u5468\u8fb9\", \"comp_avg\": \"-52.13%\"}, {\"category\": \"\u6c7d\u8f66\u7528\u54c1\", \"comp_avg\": \"-12.1%\"}, {\"category\": \"\u7f51\u7edc\u76f8\u5173\", \"comp_avg\": \"-71.42%\"}], \"m_brand_price_ratio\": \"83.73%\", \"consume_sumlevel\": \"5\", \"favor_feedrate\": \"37.93%\", \"m_std_month_cnt\": \"1.92\", \"consume_act\": \"37.85\", \"favor_high_brand_per\": \"6.67%\", \"m_price_ratio_list\": [\"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"1.54%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.45%\", \"0.0%\", \"0.0%\", \"0.0%\", \"12.96%\", \"21.37%\", \"0.63%\", \"0.0%\", \"0.0%\", \"0.21%\", \"0.14%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"23.74%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.13%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.21%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"2.37%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"1.42%\", \"0.16%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.27%\", \"0.0%\", \"0.0%\", \"0.0%\", \"15.88%\", \"0.65%\", \"0.0%\", \"0.14%\", \"0.0%\", \"0.23%\", \"0.0%\", \"17.51%\"], \"m_b_price_ratio\": \"25.97%\", \"m_cnt_ratio_list\": [\"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"6.67%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"2.22%\", \"0.0%\", \"0.0%\", \"0.0%\", \"8.89%\", \"2.22%\", \"2.22%\", \"0.0%\", \"0.0%\", \"8.89%\", \"4.44%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"2.22%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"2.22%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"2.22%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"4.44%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"8.89%\", \"2.22%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"2.22%\", \"0.0%\", \"0.0%\", \"0.0%\", \"13.33%\", \"6.67%\", \"0.0%\", \"4.44%\", \"0.0%\", \"2.22%\", \"0.0%\", \"13.33%\"], \"favor_brand\": {\"\u4e30\u7530\u4f17\u6cf0\u5317\u6c7d\u5927\u4f17\u5b9d\u9a8f\u65af\u67ef\u8fbe\u65e5\u4ea7\u672c\u7530\u6807\u81f4\u73b0\u4ee3\u798f\u7279\u8d77\u4e9a\u96ea\u4f5b\u5170\u96ea\u94c1\u9f99\u9a6c\u81ea\u8fbe\": \"11.27%\", \"\u6234\u5c14\": \"7.12%\", \"INTEL\": \"21.37%\", \"\u5176\u4ed6\": \"16.27%\", \"\u897f\u90e8\u6570\u636e\": \"23.74%\"}, \"m_brand_cnt_ratio\": \"82.22%\"}]}}";
		//正式数据
		String data1 = HttpUtils.sendGet(URL, URLUtil.querystring(app_secret, data));

//		String data1 = "";
//		int status = (int) RedisClusterUtils.getInstance().get("WlPhone");
//		SwitchStatus switchStatus = SwitchStatus.getStatus(status);
//		switch (switchStatus) {
//		case DEMO:
//			data1 = "{\"msg\": \"\u6210\u529f\", \"code\": 1200, \"data\": {\"info_list\": [{\"m_abnormal_cnt\": \"0\", \"basic_city\": \"\u56db\u5ddd \u6210\u90fd\", \"basic_gender\": \"\u7537\", \"m_std_month_price\": \"1293.28\", \"tag\": {\"\u662f\u5426\u6bcd\u5a74\u4eba\u7fa4\": 1, \"\u662f\u5426\u6469\u6258\u8f66\u5468\u8fb9\u4ea7\u54c1\u7231\u597d\u4eba\u7fa4\": 1, \"\u662f\u5426\u6c7d\u8f66\u5468\u8fb9\u4ea7\u54c1\u7231\u597d\u4eba\u7fa4\": 1, \"\u662f\u5426\u5c45\u5bb6\u504f\u597d\u4eba\u7fa4\": 1, \"\u662f\u5426\u65b0\u5bb6\u88c5\u4eba\u7fa4\": 1}, \"consume_year\": \"6\", \"consume_auth\": \"1\", \"favor_cat\": {\"\u7f51\u7edc\u76f8\u5173\": \"12.96%\", \"DIY\u7535\u8111\": \"21.37%\", \"\u7535\u8111\u5468\u8fb9\": \"17.51%\", \"\u6c7d\u8f66\u7528\u54c1\": \"15.88%\", \"\u5b58\u50a8\u8bbe\u5907\": \"23.74%\"}, \"m_b_cnt_ratio\": \"26.67%\", \"m_general_ratio\": \"4.26\", \"m_bfifty_cnt_ratio\": \"51.11%\", \"m_bfifty_price_ratio\": \"3.47%\", \"basic_age\": \"\", \"favor_comp_avg\": [{\"category\": \"\u5b58\u50a8\u8bbe\u5907\", \"comp_avg\": \"4500.19%\"}, {\"category\": \"DIY\u7535\u8111\", \"comp_avg\": \"68.32%\"}, {\"category\": \"\u7535\u8111\u5468\u8fb9\", \"comp_avg\": \"-52.13%\"}, {\"category\": \"\u6c7d\u8f66\u7528\u54c1\", \"comp_avg\": \"-12.1%\"}, {\"category\": \"\u7f51\u7edc\u76f8\u5173\", \"comp_avg\": \"-71.42%\"}], \"m_brand_price_ratio\": \"83.73%\", \"consume_sumlevel\": \"5\", \"favor_feedrate\": \"37.93%\", \"m_std_month_cnt\": \"1.92\", \"consume_act\": \"37.85\", \"favor_high_brand_per\": \"6.67%\", \"m_price_ratio_list\": [\"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"1.54%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.45%\", \"0.0%\", \"0.0%\", \"0.0%\", \"12.96%\", \"21.37%\", \"0.63%\", \"0.0%\", \"0.0%\", \"0.21%\", \"0.14%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"23.74%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.13%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.21%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"2.37%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"1.42%\", \"0.16%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.27%\", \"0.0%\", \"0.0%\", \"0.0%\", \"15.88%\", \"0.65%\", \"0.0%\", \"0.14%\", \"0.0%\", \"0.23%\", \"0.0%\", \"17.51%\"], \"m_b_price_ratio\": \"25.97%\", \"m_cnt_ratio_list\": [\"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"6.67%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"2.22%\", \"0.0%\", \"0.0%\", \"0.0%\", \"8.89%\", \"2.22%\", \"2.22%\", \"0.0%\", \"0.0%\", \"8.89%\", \"4.44%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"2.22%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"2.22%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"2.22%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"4.44%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"8.89%\", \"2.22%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"0.0%\", \"2.22%\", \"0.0%\", \"0.0%\", \"0.0%\", \"13.33%\", \"6.67%\", \"0.0%\", \"4.44%\", \"0.0%\", \"2.22%\", \"0.0%\", \"13.33%\"], \"favor_brand\": {\"\u4e30\u7530\u4f17\u6cf0\u5317\u6c7d\u5927\u4f17\u5b9d\u9a8f\u65af\u67ef\u8fbe\u65e5\u4ea7\u672c\u7530\u6807\u81f4\u73b0\u4ee3\u798f\u7279\u8d77\u4e9a\u96ea\u4f5b\u5170\u96ea\u94c1\u9f99\u9a6c\u81ea\u8fbe\": \"11.27%\", \"\u6234\u5c14\": \"7.12%\", \"INTEL\": \"21.37%\", \"\u5176\u4ed6\": \"16.27%\", \"\u897f\u90e8\u6570\u636e\": \"23.74%\"}, \"m_brand_cnt_ratio\": \"82.22%\"}]}}";
//			break;
//		case NORMAL:
//			data1 = HttpUtils.sendGet(URL, URLUtil.querystring(app_secret, data));
//			break;
//		}
		//存储状态码
		Map<String, Object> mapcode = new HashMap<String,Object>();
		Map<String,Map<String,Object>> mapResult = new HashMap<String,Map<String,Object>>();
		Gson gson = new Gson();
		mapcode = gson.fromJson(data1, Map.class);
		mapResult = gson.fromJson(data1, Map.class);
		int code=(int)(Double.parseDouble(mapcode.get("code").toString()));
		Map<String,Object> map = new HashMap<String,Object>();
		if(code == 1200){
			map.put("resultCode", code);
			map.put("result", mapResult.get("data").get("info_list"));
		}
		return map;
	}

}
