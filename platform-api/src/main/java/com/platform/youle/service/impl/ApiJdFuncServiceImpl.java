package com.platform.youle.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.platform.youle.entity.RequestBaseEntity;
import com.platform.youle.entity.ResponseBaseEntity;
import com.platform.youle.service.ApiFuncService;
import com.platform.youle.util.HttpUtil;

@Service
public class ApiJdFuncServiceImpl implements ApiFuncService {
    
	public static String url = "http://www.liguanjia.com/index.php/api";
	
	public static String skuDetailurl = "http://open.fygift.com/api/product/detial.php";
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public ResponseBaseEntity getSkuDetail(RequestBaseEntity entity, Long pid) {
		String str = JSONObject.toJSONString(entity);
		logger.info("【获取单个商品详情请求参数】:" + str);
		try {
			String result = HttpUtil.post(skuDetailurl, str);
			logger.info("【获取单个商品详情响应参数】:" + result);
			ResponseBaseEntity reponse = JSONObject.parseObject(result,new TypeReference<ResponseBaseEntity>(){});
			return reponse;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	


}
