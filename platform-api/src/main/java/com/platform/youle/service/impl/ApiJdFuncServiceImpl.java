package com.platform.youle.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.platform.youle.constant.Constants;
import com.platform.youle.entity.RequestSkuDetailEntity;
import com.platform.youle.entity.ResponseSkuDetailEntity;
import com.platform.youle.service.ApiFuncService;
import com.platform.youle.util.HttpUtil;

@Service
public class ApiJdFuncServiceImpl implements ApiFuncService {
    
	public static String url = "http://www.liguanjia.com/index.php/api";
	
	public static String skuDetailurl = "http://open.fygift.com/api/product/detial.php";
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public ResponseSkuDetailEntity getSkuDetail(RequestSkuDetailEntity entity) {
		String str = JSONObject.toJSONString(entity);//{"pid":1101,"timestamp":"1541654878029","token":"3B9576F327A4F5D252C0CEF7CE6CD55C","wid":"1322"}
		logger.info("【获取单个商品详情请求参数】:" + str);
		try {
			String result = HttpUtil.post(Constants.Urls.base_test_url.concat(Constants.Urls.detial), str);
			logger.info("【获取单个商品详情响应参数】:" + result);
			ResponseSkuDetailEntity reponse = JSONObject.parseObject(result,new TypeReference<ResponseSkuDetailEntity>(){});
			return reponse;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	


}
