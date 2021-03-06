package com.platform.youle.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.platform.youle.constant.Constants.Urls;
import com.platform.youle.entity.RequestBaseEntity;
import com.platform.youle.entity.ResponseDepositEntity;
import com.platform.youle.entity.ResponseRootDetailEntity;
import com.platform.youle.service.AbsApiDepositService;
import com.platform.youle.util.HttpUtil;


@Service
public  class ApiDepositServiceImpl extends AbsApiDepositService{

	private static final Logger logger = LoggerFactory.getLogger(ApiDepositServiceImpl.class);
	
	
	@Override
	public ResponseDepositEntity remain() {
		ResponseDepositEntity  reponse=null;
		RequestBaseEntity entity = new RequestBaseEntity();
	    initRequestParam(entity);
		try {
			logger.info("[3.1查询预存款余额]入参："+JSONObject.toJSONString(entity));
			String result = HttpUtil.post(Urls.base_prod_url+Urls.remain, objectToMap(entity));
			logger.info("[3.1查询预存款余额]出参："+result);
			reponse = JSON.parseObject(result,new TypeReference<ResponseDepositEntity>(){});
		} catch (Exception e) {
			logger.error("[3.1查询预存款余额]异常：", e);
		}
		return reponse;
	}

}
