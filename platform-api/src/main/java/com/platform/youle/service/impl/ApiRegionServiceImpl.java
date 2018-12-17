package com.platform.youle.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.platform.entity.SysRegionEntity;
import com.platform.entity.ThirdPartyRegionEntity;
import com.platform.service.SysRegionService;
import com.platform.service.ThirdPartyRegionService;
import com.platform.youle.constant.AreaEnum;
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

	@Autowired
	private ThirdPartyRegionService thirdPartyRegionService;
	@Autowired
	private SysRegionService sysRegionService;
	
	@Override
	public ResponseBaseEntity<JSONArray> province() {
		ResponseBaseEntity<JSONArray> response =null;
		List<SysRegionEntity> sysRegionEntityList = new ArrayList<>(); //用来批量保存三方接口地址信息
		RequestBaseEntity entity = new RequestBaseEntity();
        initRequestParam(entity);
        try {
        	List<SysRegionEntity> SysRegionList = sysRegionService.queryAllByType(1);
            logger.info("[4.1获取一级地址-省份]入参："+JSONObject.toJSONString(entity));
            String result = HttpUtil.post(Urls.base_prod_url+Urls.province, objectToMap(entity));
            logger.info("[4.1获取一级地址-省份] 返回结果："+JSONObject.toJSONString(result));
            response = JSON.parseObject(result,ResponseBaseEntity.class);
            logger.info("[4.1获取一级地址-省份] 返回data："+response.getRESULT_DATA());
            JSONArray resultData = response.getRESULT_DATA();
	        for(int i =0 ;i <resultData.size();i++){
	        	ResponseRegionEntity responseRegionEntity = resultData.getObject(i, ResponseRegionEntity.class);
	        	SysRegionEntity sysRegionEntity = new SysRegionEntity(); 
	        	sysRegionEntity.setName(responseRegionEntity.getName());
	        	sysRegionEntity.setType(Integer.parseInt(AreaEnum.PROVINCE.getCode()));
	        	sysRegionEntity.setThirdCode(responseRegionEntity.getCode());
	        	sysRegionEntity.setParentId(0);
	        	sysRegionEntityList.add(sysRegionEntity);
	        }
        } catch (Exception e) {
            logger.error("[4.1获取一级地址-省份]异常",e);
        }
        if(CollectionUtils.isNotEmpty(sysRegionEntityList)){
        	//执行三方数据入库
        	sysRegionService.insertBatch(sysRegionEntityList);
        }
        return response;
	}
        
	@Override
	public ResponseBaseEntity<JSONArray> city(int province) {
		ResponseBaseEntity<JSONArray> response =null;
		List<SysRegionEntity> sysRegionEntityList = new ArrayList<>(); //用来批量保存三方接口地址信息
		RequestCityEntity entity = new RequestCityEntity();
        initRequestParam(entity);
        entity.setProvince(province);
        try {
            logger.info("[4.2获取二地址-城市]入参："+JSONObject.toJSONString(entity));
            String result = HttpUtil.post(Urls.base_prod_url+Urls.city, objectToMap(entity));
            logger.info("[4.2获取二地址-城市] 返回结果："+JSONObject.toJSONString(result));
            response = JSON.parseObject(result,ResponseBaseEntity.class);
            JSONArray resultData = response.getRESULT_DATA();
	        for(int i =0 ;i <resultData.size();i++){
	        	//ResponseRegionEntity responseRegionEntity = resultData.getObject(i, ResponseRegionEntity.class);
	        	ResponseRegionEntity responseRegionEntity = resultData.getObject(i, ResponseRegionEntity.class);
	        	SysRegionEntity sysRegionEntity = new SysRegionEntity(); 
	        	sysRegionEntity.setName(responseRegionEntity.getName());
	        	sysRegionEntity.setType(Integer.parseInt(AreaEnum.CITY.getCode()));
	        	sysRegionEntity.setThirdCode(responseRegionEntity.getCode());
	        	sysRegionEntity.setParentId(province);
	        	sysRegionEntityList.add(sysRegionEntity);
	        }
        } catch (Exception e) {
            logger.error("[4.2获取二地址-城市]异常",e);
        }
        if(CollectionUtils.isNotEmpty(sysRegionEntityList)){
        	//执行三方数据入库
        	sysRegionService.insertBatch(sysRegionEntityList);
        }
        return response;
	}

	@Override
	public ResponseBaseEntity<JSONArray> county(int city) {
		ResponseBaseEntity<JSONArray> response =null;
		RequestCountyEntity entity = new RequestCountyEntity();
		List<SysRegionEntity> sysRegionEntityList = new ArrayList<>(); //用来批量保存三方接口地址信息
        initRequestParam(entity);
        entity.setCity(city);
        try {
            logger.info("[4.3获取三级地址-县/区]入参："+JSONObject.toJSONString(entity));
            String result = HttpUtil.post(Urls.base_prod_url+Urls.county, objectToMap(entity));
            logger.info("[4.3获取三级地址-县/区] 返回结果："+JSONObject.toJSONString(result));
            response = JSON.parseObject(result,ResponseBaseEntity.class);
            
            JSONArray resultData = response.getRESULT_DATA();
	        for(int i =0 ;i <resultData.size();i++){
	        	ResponseRegionEntity responseRegionEntity = resultData.getObject(i, ResponseRegionEntity.class);
	        	SysRegionEntity sysRegionEntity = new SysRegionEntity(); 
	        	sysRegionEntity.setName(responseRegionEntity.getName());
	        	sysRegionEntity.setType(Integer.parseInt(AreaEnum.COUNTY.getCode()));
	        	sysRegionEntity.setThirdCode(responseRegionEntity.getCode());
	        	sysRegionEntity.setParentId(city);
	        	sysRegionEntityList.add(sysRegionEntity);
	        }
        } catch (Exception e) {
            logger.error("[4.3获取三级地址-县/区]异常",e);
        }
        if(CollectionUtils.isNotEmpty(sysRegionEntityList)){
        	//执行三方数据入库
        	sysRegionService.insertBatch(sysRegionEntityList);
        }
        return response;
	}

	@Override
	public ResponseBaseEntity<JSONArray> town(int county) {
		ResponseBaseEntity<JSONArray> response =null;
		RequestTownEntity entity = new RequestTownEntity();
		List<SysRegionEntity> sysRegionEntityList = new ArrayList<>(); //用来批量保存三方接口地址信息
		List<ThirdPartyRegionEntity> thirdPartyRegionList = new ArrayList<>(); //用来批量保存三方接口地址信息
        initRequestParam(entity);
        entity.setCounty(county);
        try {
            logger.info("[4.4获取四级地址-镇/街道]入参："+JSONObject.toJSONString(entity));
            String result = HttpUtil.post(Urls.base_prod_url+Urls.town, objectToMap(entity));
            logger.info("[4.4获取四级地址-镇/街道] 返回结果："+JSONObject.toJSONString(result));
            response = JSON.parseObject(result,ResponseBaseEntity.class);
            
            JSONArray resultData = response.getRESULT_DATA();
	        for(int i =0 ;i <resultData.size();i++){
	        	ResponseRegionEntity responseRegionEntity = resultData.getObject(i, ResponseRegionEntity.class);
	        	SysRegionEntity sysRegionEntity = new SysRegionEntity(); 
	        	sysRegionEntity.setName(responseRegionEntity.getName());
	        	sysRegionEntity.setType(Integer.parseInt(AreaEnum.TOWN.getCode()));
	        	sysRegionEntity.setThirdCode(responseRegionEntity.getCode());
	        	sysRegionEntity.setParentId(county);
	        	sysRegionEntityList.add(sysRegionEntity);
	        	/*ThirdPartyRegionEntity thirdPartyRegionEntity = new ThirdPartyRegionEntity();
				thirdPartyRegionEntity.setThirdCode(responseRegionEntity.getCode());;
				thirdPartyRegionEntity.setParentId(county); 
				thirdPartyRegionEntity.setName(responseRegionEntity.getName());
				thirdPartyRegionEntity.setNideshopCode(0); //关联的本地地区id暂不设置
				thirdPartyRegionEntity.setType(Integer.parseInt(AreaEnum.TOWN.getCode())); //表示市
				thirdPartyRegionEntity.setThirdType(responseRegionEntity.getType());
				thirdPartyRegionEntity.setThirdParty("jd");
				thirdPartyRegionList.add(thirdPartyRegionEntity);*/
	        }
        } catch (Exception e) {
            logger.error("[4.4获取四级地址-镇/街道]异常",e);
        }
        if(CollectionUtils.isNotEmpty(sysRegionEntityList)){
        	//执行三方数据入库
    		//thirdPartyRegionService.insertBatch(thirdPartyRegionList);
    		sysRegionService.insertBatch(sysRegionEntityList);
        }
        return response;
	}
	
}
