package com.platform.youle.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.platform.youle.constant.Constants.Urls;
import com.platform.youle.entity.RequestBaseEntity;
import com.platform.youle.entity.RequestCityEntity;
import com.platform.youle.entity.RequestCountyEntity;
import com.platform.youle.entity.RequestTownEntity;
import com.platform.youle.entity.ResponseBaseEntity;
import com.platform.youle.entity.ResponseRegionEntity;
import com.platform.youle.service.AbsApiRegionService;
import com.platform.youle.util.HttpUtil;

/**
 * 系统地址服务
 *
 */
@Service
public class ApiRegionServiceImpl extends AbsApiRegionService{
	
	private static final Logger logger = LoggerFactory.getLogger(ApiRegionServiceImpl.class);

	@Override
	public ResponseBaseEntity<ResponseRegionEntity> province() {
		ResponseBaseEntity<ResponseRegionEntity> response =null;
		RequestBaseEntity entity = new RequestBaseEntity();
        initRequestParam(entity);
        try {
            logger.info("[4.1获取一级地址-省份]入参："+JSONObject.toJSONString(entity));
            String result = HttpUtil.post(Urls.base_test_url+Urls.province, objectToMap(entity));
            logger.info("[4.1获取一级地址-省份] 返回结果："+JSONObject.toJSONString(result));
            response = JSON.parseObject(result,ResponseBaseEntity.class);
        } catch (Exception e) {
            logger.error("[4.1获取一级地址-省份]异常",e);
        }
        //地址数据入库
        
        return response;
	}

	@Override
	public ResponseBaseEntity<ResponseRegionEntity> city(int province) {
		ResponseBaseEntity<ResponseRegionEntity> response =null;
		RequestCityEntity entity = new RequestCityEntity();
        initRequestParam(entity);
        entity.setProvince(province);
        try {
            logger.info("[4.2获取二地址-城市]入参："+JSONObject.toJSONString(entity));
            String result = HttpUtil.post(Urls.base_test_url+Urls.city, objectToMap(entity));
            logger.info("[4.2获取二地址-城市] 返回结果："+JSONObject.toJSONString(result));
            response = JSON.parseObject(result,ResponseBaseEntity.class);
        } catch (Exception e) {
            logger.error("[4.2获取二地址-城市]异常",e);
        }
        return response;
	}

	@Override
	public ResponseBaseEntity<ResponseRegionEntity> county(int city) {
		ResponseBaseEntity<ResponseRegionEntity> response =null;
		RequestCountyEntity entity = new RequestCountyEntity();
        initRequestParam(entity);
        entity.setCity(city);
        try {
            logger.info("[4.3获取三级地址-县/区]入参："+JSONObject.toJSONString(entity));
            String result = HttpUtil.post(Urls.base_test_url+Urls.county, objectToMap(entity));
            logger.info("[4.3获取三级地址-县/区] 返回结果："+JSONObject.toJSONString(result));
            response = JSON.parseObject(result,ResponseBaseEntity.class);
        } catch (Exception e) {
            logger.error("[4.3获取三级地址-县/区]异常",e);
        }
        return response;
	}

	@Override
	public ResponseBaseEntity<ResponseRegionEntity> town(int county) {
		ResponseBaseEntity<ResponseRegionEntity> response =null;
		RequestTownEntity entity = new RequestTownEntity();
        initRequestParam(entity);
        entity.setCounty(county);
        try {
            logger.info("[4.4获取四级地址-镇/街道]入参："+JSONObject.toJSONString(entity));
            String result = HttpUtil.post(Urls.base_test_url+Urls.town, objectToMap(entity));
            logger.info("[4.4获取四级地址-镇/街道] 返回结果："+JSONObject.toJSONString(result));
            response = JSON.parseObject(result,ResponseBaseEntity.class);
        } catch (Exception e) {
            logger.error("[4.4获取四级地址-镇/街道]异常",e);
        }
        return response;
	}
	
}
