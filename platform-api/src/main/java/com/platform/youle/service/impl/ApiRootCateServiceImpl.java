package com.platform.youle.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.platform.youle.constant.Constants.Urls;
import com.platform.youle.entity.RequestBaseEntity;
import com.platform.youle.entity.RequestChildsEntity;
import com.platform.youle.entity.RequestRootDetailEntity;
import com.platform.youle.entity.ResponseChildsEntity;
import com.platform.youle.entity.ResponseRootCateEntity;
import com.platform.youle.entity.ResponseRootDetailEntity;
import com.platform.youle.service.AbsApiRootCateService;
import com.platform.youle.util.HttpUtil;

public class ApiRootCateServiceImpl extends AbsApiRootCateService{
	
	private static final Logger logger = LoggerFactory.getLogger(ApiRootCateServiceImpl.class);

	@Override
	protected ResponseRootCateEntity rootCate() {
		ResponseRootCateEntity  reponse=null;
		RequestBaseEntity entity = new RequestBaseEntity();
	    initRequestParam(entity);
		try {
			logger.info("[5.1获取一级产品分类]入参："+JSONObject.toJSONString(entity));
			String result = HttpUtil.post(Urls.base_test_url+Urls.rootCate, objectToMap(entity));
			logger.info("[5.1获取一级产品分类]出参："+result);
			reponse = JSON.parseObject(result,ResponseRootCateEntity.class);
		} catch (Exception e) {
			logger.error("[5.1获取一级产品分类]异常：", e);
		}
		return reponse;
	}

	@Override
	protected ResponseChildsEntity childs(Integer parentCate) {
		ResponseChildsEntity  reponse=null;
		RequestChildsEntity entity = new RequestChildsEntity();
	    initRequestParam(entity);
	    entity.setParentCate(parentCate);
		try {
			logger.info("[5.2获取下级产品分类]入参："+JSONObject.toJSONString(entity));
			String result = HttpUtil.post(Urls.base_test_url+Urls.childs, objectToMap(entity));
			logger.info("[5.2获取下级产品分类]出参："+result);
			reponse = JSON.parseObject(result,ResponseChildsEntity.class);
		} catch (Exception e) {
			logger.error("[5.2获取下级产品分类]异常：", e);
		}
		return reponse;
	}

	@Override
	protected ResponseRootDetailEntity detial(Integer cid) {
		ResponseRootDetailEntity  reponse=null;
		RequestRootDetailEntity entity = new RequestRootDetailEntity();
	    initRequestParam(entity);
	    entity.setCid(cid);
		try {
			logger.info("[5.3获取单个分类详情]入参："+JSONObject.toJSONString(entity));
			String result = HttpUtil.post(Urls.base_test_url+Urls.cateDetial, objectToMap(entity));
			logger.info("[5.3获取单个分类详情]出参："+result);
			reponse = JSON.parseObject(result,ResponseRootDetailEntity.class);
		} catch (Exception e) {
			logger.error("[5.3获取单个分类详情]异常：", e);
		}
		return reponse;
	}

}
