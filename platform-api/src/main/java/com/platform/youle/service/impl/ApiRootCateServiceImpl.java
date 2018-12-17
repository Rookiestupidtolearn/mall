package com.platform.youle.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.platform.youle.constant.Constants.Urls;
import com.platform.youle.entity.RequestBaseEntity;
import com.platform.youle.entity.RequestChildsEntity;
import com.platform.youle.entity.RequestRootDetailEntity;
import com.platform.youle.entity.ResponseChildsEntity;
import com.platform.youle.entity.ResponseRootCateEntity;
import com.platform.youle.entity.ResponseRootDetailEntity;
import com.platform.youle.entity.ResponseSkuDetailEntity;
import com.platform.youle.service.AbsApiRootCateService;
import com.platform.youle.util.HttpUtil;


@Service
public class ApiRootCateServiceImpl extends AbsApiRootCateService{
	
	private static final Logger logger = LoggerFactory.getLogger(ApiRootCateServiceImpl.class);

	@Override
	public ResponseRootCateEntity rootCate() {
		ResponseRootCateEntity  reponse=null;
		RequestBaseEntity entity = new RequestBaseEntity();
	    initRequestParam(entity);
		try {
			logger.info("[5.1获取一级产品分类]入参："+JSONObject.toJSONString(entity));
			String result = HttpUtil.post(Urls.base_prod_url+Urls.rootCate, objectToMap(entity));
			logger.info("[5.1获取一级产品分类]出参："+result);
			reponse = JSON.parseObject(result,new TypeReference<ResponseRootCateEntity>(){});
		} catch (Exception e) {
			logger.error("[5.1获取一级产品分类]异常：", e);
		}
		return reponse;
	}

	@Override
	public String childs(Integer parentCate) {
		ResponseChildsEntity  reponse=null;
		RequestChildsEntity entity = new RequestChildsEntity();
	    initRequestParam(entity);
	    entity.setParentCate(parentCate);
	    System.out.println(entity.getTimestamp());
	    String result = "";
		try {
			logger.info("[5.2获取下级产品分类]入参："+JSONObject.toJSONString(entity));
			result = HttpUtil.post(Urls.base_prod_url+Urls.childs, objectToMap(entity));
			logger.info("[5.2获取下级产品分类]出参："+result);
//			reponse = JSON.parseObject(result,ResponseChildsEntity.class);
		} catch (Exception e) {
			logger.error("[5.2获取下级产品分类]异常：", e);
		}
		return result;
	}

	@Override
	public ResponseRootDetailEntity detial(Integer cid) {
		ResponseRootDetailEntity  reponse=null;
		RequestRootDetailEntity entity = new RequestRootDetailEntity();
	    initRequestParam(entity);
	    entity.setCid(cid);
		try {
			logger.info("[5.3获取单个分类详情]入参："+JSONObject.toJSONString(entity));
			String result = HttpUtil.post(Urls.base_prod_url+Urls.cateDetial, objectToMap(entity));
			logger.info("[5.3获取单个分类详情]出参："+result);
			reponse = JSON.parseObject(result,new TypeReference<ResponseRootDetailEntity>(){});
		} catch (Exception e) {
			logger.error("[5.3获取单个分类详情]异常：", e);
		}
		return reponse;
	}

}
