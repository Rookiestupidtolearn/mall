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
		List<ThirdPartyRegionEntity> thirdPartyRegionList = new ArrayList<>(); //用来批量保存三方接口地址信息
		RequestBaseEntity entity = new RequestBaseEntity();
        initRequestParam(entity);
        try {
        	List<SysRegionEntity> SysRegionList = sysRegionService.queryAllByType(1);
            logger.info("[4.1获取一级地址-省份]入参："+JSONObject.toJSONString(entity));
            String result = HttpUtil.post(Urls.base_test_url+Urls.province, objectToMap(entity));
            logger.info("[4.1获取一级地址-省份] 返回结果："+JSONObject.toJSONString(result));
            response = JSON.parseObject(result,ResponseBaseEntity.class);
            logger.info("[4.1获取一级地址-省份] 返回data："+response.getRESULT_DATA());
            JSONArray resultData = response.getRESULT_DATA();
	        for(int i =0 ;i <resultData.size();i++){
	        	ResponseRegionEntity responseRegionEntity = resultData.getObject(i, ResponseRegionEntity.class);
	        	ThirdPartyRegionEntity thirdPartyRegionEntity = new ThirdPartyRegionEntity();
				thirdPartyRegionEntity.setId(responseRegionEntity.getCode());
				thirdPartyRegionEntity.setParentId(0); //省的父级id固定为0
				thirdPartyRegionEntity.setName(responseRegionEntity.getName());
				thirdPartyRegionEntity.setSysRegionId(0);
				thirdPartyRegionEntity.setType(1); //表示省
				thirdPartyRegionEntity.setThirdType(responseRegionEntity.getType());
				thirdPartyRegionEntity.setThirdParty("jd");
				thirdPartyRegionList.add(thirdPartyRegionEntity);
	        }
        } catch (Exception e) {
            logger.error("[4.1获取一级地址-省份]异常",e);
        }
        
        if(CollectionUtils.isNotEmpty(thirdPartyRegionList)){
        	//执行三方数据入库
    		thirdPartyRegionService.insertBatch(thirdPartyRegionList);
        }
        return response;
}
        
        //省地址数据入库
        /*if(null != response){
        	//查询库中地址信息
        	List<SysRegionEntity> SysRegionList = sysRegionService.queryAllByType(1);
        	 
        	List<ThirdPartyRegionEntity> thirdPartyRegionList = new ArrayList(); //用来批量保存三方接口地址信息
        	List<ResponseRegionEntity> notList = new ArrayList(); //保存本地无三方接口返回省份信息
        	
        	if(CollectionUtils.isNotEmpty(RRl)){
        		for(ResponseRegionEntity responseRegionEntity : RRl){
        			for(SysRegionEntity sysRegionEntity : SysRegionList){
        				if(sysRegionEntity.getName().equals(responseRegionEntity.getName())){
        					//三方地址信息入库
        					ThirdPartyRegionEntity thirdPartyRegionEntity = new ThirdPartyRegionEntity();
        					thirdPartyRegionEntity.setId(responseRegionEntity.getCode());
        					thirdPartyRegionEntity.setParent_id(0); //省的父级id固定为0
        					thirdPartyRegionEntity.setName(responseRegionEntity.getName());
        					thirdPartyRegionEntity.setSysRegionId(sysRegionEntity.getId());
        					thirdPartyRegionEntity.setType(1); //表示省
        					thirdPartyRegionEntity.setThirdParty("优乐礼购");
        					thirdPartyRegionList.add(thirdPartyRegionEntity);
        				}else{
        					logger.error("[4.1获取一级地址-省份]本地无三方接口返回省份："+responseRegionEntity.getName());
        					notList.add(responseRegionEntity);
        					//保存三方省份信息
        					ThirdPartyRegionEntity thirdPartyRegionEntity = new ThirdPartyRegionEntity();
        					thirdPartyRegionEntity.setId(responseRegionEntity.getCode());
        					thirdPartyRegionEntity.setParent_id(0); //省的父级id固定为0
        					thirdPartyRegionEntity.setName(responseRegionEntity.getName());
        					thirdPartyRegionEntity.setSysRegionId(sysRegionEntity.getId());
        					thirdPartyRegionEntity.setType(1); //表示省
        					thirdPartyRegionEntity.setThirdParty("优乐礼购");
        					thirdPartyRegionList.add(thirdPartyRegionEntity);
        				}
        			}
        		}
        	}else{
        		 logger.info("[4.1获取一级地址-省份] 三方数据入库异常：未获取到三方接口地址信息");
        	}
        	if(CollectionUtils.isNotEmpty(thirdPartyRegionList)){
        		//执行三方数据入库
        		thirdPartyRegionService.insertBatch(thirdPartyRegionList);
        		logger.info("[4.1获取一级地址-省份] 三方数据入库成功"+thirdPartyRegionList.size()+"条");
        		//Dao.xml:
	       		<insert id="insertBatch">
		        	    INSERT INTO third_party_region
		        	            (id, parent_id, name,type,third_party,sys_region_id)
		        	    VALUES
		        	    <foreach collection ="list" item="thirdPartyRegion" separator =",">
		        	         (#{thirdPartyRegion.id}, #{thirdPartyRegion.parentId}, #{thirdPartyRegion.name}, #{thirdPartyRegion.type}, #{thirdPartyRegion.thirdParty}, #{thirdPartyRegion.sysRegionId})
		        	    </foreach >
	       	    </insert>
        	}
        	if(CollectionUtils.isNotEmpty(notList)){
        		for(ResponseRegionEntity responseRegionEntity : notList){
        			logger.info("[4.1获取一级地址-省份] 本地无三方返回省份信息"+responseRegionEntity.getName());
        		}
        	}
        }*/
       
	//}

	@Override
	public ResponseBaseEntity<JSONArray> city(int province) {
		ResponseBaseEntity<JSONArray> response =null;
		List<ThirdPartyRegionEntity> thirdPartyRegionList = new ArrayList<>(); //用来批量保存三方接口地址信息
		RequestCityEntity entity = new RequestCityEntity();
        initRequestParam(entity);
        entity.setProvince(province);
        try {
            logger.info("[4.2获取二地址-城市]入参："+JSONObject.toJSONString(entity));
            String result = HttpUtil.post(Urls.base_test_url+Urls.city, objectToMap(entity));
            logger.info("[4.2获取二地址-城市] 返回结果："+JSONObject.toJSONString(result));
            response = JSON.parseObject(result,ResponseBaseEntity.class);
            JSONArray resultData = response.getRESULT_DATA();
	        for(int i =0 ;i <resultData.size();i++){
	        	ResponseRegionEntity responseRegionEntity = resultData.getObject(i, ResponseRegionEntity.class);
	        	ThirdPartyRegionEntity thirdPartyRegionEntity = new ThirdPartyRegionEntity();
				thirdPartyRegionEntity.setId(responseRegionEntity.getCode());
				thirdPartyRegionEntity.setParentId(province); //省的父级id固定为0
				thirdPartyRegionEntity.setName(responseRegionEntity.getName());
				thirdPartyRegionEntity.setSysRegionId(0);
				thirdPartyRegionEntity.setType(2); //表示市
				thirdPartyRegionEntity.setThirdType(responseRegionEntity.getType());
				thirdPartyRegionEntity.setThirdParty("jd");
				thirdPartyRegionList.add(thirdPartyRegionEntity);
	        }
        } catch (Exception e) {
            logger.error("[4.2获取二地址-城市]异常",e);
        }
        if(CollectionUtils.isNotEmpty(thirdPartyRegionList)){
        	//执行三方数据入库
    		thirdPartyRegionService.insertBatch(thirdPartyRegionList);
        }
        
      /*  if(null != response){
        	//查询库中市地址信息
        	//List<SysRegionEntity> SysRegionList = new ArrayList();
        	// SysRegionList = sysRegionService.queryAll(2);
        	
        	List<ThirdPartyRegionEntity> thirdPartyRegionList = new ArrayList(); //用来批量保存三方接口地址信息
        	List<ResponseRegionEntity> notList = new ArrayList(); //保存本地无三方接口返回市信息
        	
        	if(CollectionUtils.isNotEmpty(response.getRESULT_DATA())){
        		for(ResponseRegionEntity responseRegionEntity : response.getRESULT_DATA()){
        			for(SysRegionEntity sysRegionEntity : SysRegionList){
        				if(sysRegionEntity.getName().equals(responseRegionEntity.getName())){
        					//三方地址信息入库
        					ThirdPartyRegionEntity thirdPartyRegionEntity = new ThirdPartyRegionEntity();
        					thirdPartyRegionEntity.setId(responseRegionEntity.getCode());
        					thirdPartyRegionEntity.setParentId(province); //父级省
        					thirdPartyRegionEntity.setName(responseRegionEntity.getName());
        					thirdPartyRegionEntity.setSysRegionId(sysRegionEntity.getId());
        					thirdPartyRegionEntity.setType(2); //表示市
        					thirdPartyRegionEntity.setThirdParty("优乐礼购");
        					thirdPartyRegionList.add(thirdPartyRegionEntity);
        				}else{
        					logger.error("[4.2获取二地址-城市]本地无三方接口返回省份："+responseRegionEntity.getName());
        					notList.add(responseRegionEntity);
        				}
        			}
        		}
        	}else{
       		 logger.info("[4.2获取二地址-城市] 三方数据入库异常：未获取到三方接口地址信息");
        	}
        	if(CollectionUtils.isNotEmpty(thirdPartyRegionList)){
	       		//执行三方数据入库
	       		//ThirdPartyRegionService.insertBatch(thirdPartyRegionList);
	       		
	       		//Dao.xml:
	       		<insert id="insertBatch">
		        	    INSERT INTO third_party_region
		        	            (id, parent_id, name,type,third_party,sys_region_id)
		        	    VALUES
		        	    <foreach collection ="list" item="thirdPartyRegion" separator =",">
		        	         (#{thirdPartyRegion.id}, #{thirdPartyRegion.parentId}, #{thirdPartyRegion.name}, #{thirdPartyRegion.type}, #{thirdPartyRegion.thirdParty}, #{thirdPartyRegion.sysRegionId})
		        	    </foreach >
	       	    </insert>
	       	}
        }*/
        return response;
	}

	@Override
	public ResponseBaseEntity<JSONArray> county(int city) {
		ResponseBaseEntity<JSONArray> response =null;
		RequestCountyEntity entity = new RequestCountyEntity();
		List<ThirdPartyRegionEntity> thirdPartyRegionList = new ArrayList<>(); //用来批量保存三方接口地址信息
        initRequestParam(entity);
        entity.setCity(city);
        try {
            logger.info("[4.3获取三级地址-县/区]入参："+JSONObject.toJSONString(entity));
            String result = HttpUtil.post(Urls.base_test_url+Urls.county, objectToMap(entity));
            logger.info("[4.3获取三级地址-县/区] 返回结果："+JSONObject.toJSONString(result));
            response = JSON.parseObject(result,ResponseBaseEntity.class);
            
            JSONArray resultData = response.getRESULT_DATA();
	        for(int i =0 ;i <resultData.size();i++){
	        	ResponseRegionEntity responseRegionEntity = resultData.getObject(i, ResponseRegionEntity.class);
	        	ThirdPartyRegionEntity thirdPartyRegionEntity = new ThirdPartyRegionEntity();
				thirdPartyRegionEntity.setId(responseRegionEntity.getCode());
				thirdPartyRegionEntity.setParentId(city); //省的父级id固定为0
				thirdPartyRegionEntity.setName(responseRegionEntity.getName());
				thirdPartyRegionEntity.setSysRegionId(0);
				thirdPartyRegionEntity.setType(3); //表示市
				thirdPartyRegionEntity.setThirdType(responseRegionEntity.getType());
				thirdPartyRegionEntity.setThirdParty("jd");
				thirdPartyRegionList.add(thirdPartyRegionEntity);
	        }
        } catch (Exception e) {
            logger.error("[4.3获取三级地址-县/区]异常",e);
        }
        
        if(CollectionUtils.isNotEmpty(thirdPartyRegionList)){
        	//执行三方数据入库
    		thirdPartyRegionService.insertBatch(thirdPartyRegionList);
        }
        
        
        /*if(null != response){
        	//查询库中市地址信息
        	List<SysRegionEntity> SysRegionList = new ArrayList();
        	// SysRegionList = sysRegionService.queryAll(2);
        	
        	List<ThirdPartyRegionEntity> thirdPartyRegionList = new ArrayList(); //用来批量保存三方接口地址信息
        	List<ResponseRegionEntity> notList = new ArrayList(); //保存本地无三方接口返回市信息
        	
        	if(CollectionUtils.isNotEmpty(response.getRESULT_DATA())){
        		for(ResponseRegionEntity responseRegionEntity : response.getRESULT_DATA()){
        			for(SysRegionEntity sysRegionEntity : SysRegionList){
        				if(sysRegionEntity.getName().equals(responseRegionEntity.getName())){
        					//三方地址信息入库
        					ThirdPartyRegionEntity thirdPartyRegionEntity = new ThirdPartyRegionEntity();
        					thirdPartyRegionEntity.setId(responseRegionEntity.getCode());
        					thirdPartyRegionEntity.setParentId(city); // 父级市id 
        					thirdPartyRegionEntity.setName(responseRegionEntity.getName());
        					thirdPartyRegionEntity.setSysRegionId(sysRegionEntity.getId());
        					thirdPartyRegionEntity.setType(3); //表示县/区
        					thirdPartyRegionEntity.setThirdParty("优乐礼购");
        					thirdPartyRegionList.add(thirdPartyRegionEntity);
        				}else{
        					logger.error("[4.3获取三级地址-县/区]本地无三方接口返回省份："+responseRegionEntity.getName());
        					notList.add(responseRegionEntity);
        				}
        			}
        		}
        	}else{
       		 logger.info("[4.3获取三级地址-县/区] 三方数据入库异常：未获取到三方接口地址信息");
        	}
        	if(CollectionUtils.isNotEmpty(thirdPartyRegionList)){
	       		//执行三方数据入库
	       		//ThirdPartyRegionService.insertBatch(thirdPartyRegionList);
	       		
	       		//Dao.xml:
	       		<insert id="insertBatch">
		        	    INSERT INTO third_party_region
		        	            (id, parent_id, name,type,third_party,sys_region_id)
		        	    VALUES
		        	    <foreach collection ="list" item="thirdPartyRegion" separator =",">
		        	         (#{thirdPartyRegion.id}, #{thirdPartyRegion.parentId}, #{thirdPartyRegion.name}, #{thirdPartyRegion.type}, #{thirdPartyRegion.thirdParty}, #{thirdPartyRegion.sysRegionId})
		        	    </foreach >
	       	    </insert>
	       	}
        }*/
        return response;
	}

	@Override
	public ResponseBaseEntity<JSONArray> town(int county) {
		ResponseBaseEntity<JSONArray> response =null;
		RequestTownEntity entity = new RequestTownEntity();
		List<ThirdPartyRegionEntity> thirdPartyRegionList = new ArrayList<>(); //用来批量保存三方接口地址信息
        initRequestParam(entity);
        entity.setCounty(county);
        try {
            logger.info("[4.4获取四级地址-镇/街道]入参："+JSONObject.toJSONString(entity));
            String result = HttpUtil.post(Urls.base_test_url+Urls.town, objectToMap(entity));
            logger.info("[4.4获取四级地址-镇/街道] 返回结果："+JSONObject.toJSONString(result));
            response = JSON.parseObject(result,ResponseBaseEntity.class);
            
            JSONArray resultData = response.getRESULT_DATA();
	        for(int i =0 ;i <resultData.size();i++){
	        	ResponseRegionEntity responseRegionEntity = resultData.getObject(i, ResponseRegionEntity.class);
	        	ThirdPartyRegionEntity thirdPartyRegionEntity = new ThirdPartyRegionEntity();
				thirdPartyRegionEntity.setId(responseRegionEntity.getCode());
				thirdPartyRegionEntity.setParentId(county); //省的父级id固定为0
				thirdPartyRegionEntity.setName(responseRegionEntity.getName());
				thirdPartyRegionEntity.setSysRegionId(0);
				thirdPartyRegionEntity.setType(4); //表示市
				thirdPartyRegionEntity.setThirdType(responseRegionEntity.getType());
				thirdPartyRegionEntity.setThirdParty("jd");
				thirdPartyRegionList.add(thirdPartyRegionEntity);
	        }
        } catch (Exception e) {
            logger.error("[4.4获取四级地址-镇/街道]异常",e);
        }
        if(CollectionUtils.isNotEmpty(thirdPartyRegionList)){
        	//执行三方数据入库
    		thirdPartyRegionService.insertBatch(thirdPartyRegionList);
        }
        
        /*if(null != response){
        	//查询库中市地址信息
        	List<SysRegionEntity> SysRegionList = new ArrayList();
        	// SysRegionList = sysRegionService.queryAll(2);
        	
        	List<ThirdPartyRegionEntity> thirdPartyRegionList = new ArrayList(); //用来批量保存三方接口地址信息
        	List<ResponseRegionEntity> notList = new ArrayList(); //保存本地无三方接口返回市信息
        	
        	if(CollectionUtils.isNotEmpty(response.getRESULT_DATA())){
        		for(ResponseRegionEntity responseRegionEntity : response.getRESULT_DATA()){
        			for(SysRegionEntity sysRegionEntity : SysRegionList){
        				if(sysRegionEntity.getName().equals(responseRegionEntity.getName())){
        					//三方地址信息入库
        					ThirdPartyRegionEntity thirdPartyRegionEntity = new ThirdPartyRegionEntity();
        					thirdPartyRegionEntity.setId(responseRegionEntity.getCode());
        					thirdPartyRegionEntity.setParentId(county); // 父级县/区id 
        					thirdPartyRegionEntity.setName(responseRegionEntity.getName());
        					thirdPartyRegionEntity.setSysRegionId(sysRegionEntity.getId());
        					thirdPartyRegionEntity.setType(4); //表示镇/街道
        					thirdPartyRegionEntity.setThirdParty("优乐礼购");
        					thirdPartyRegionList.add(thirdPartyRegionEntity);
        				}else{
        					logger.error("[4.4获取四级地址-镇/街道]本地无三方接口返回镇/街道："+responseRegionEntity.getName());
        					notList.add(responseRegionEntity);
        				}
        			}
        		}
        	}else{
       		 logger.info("[4.4获取四级地址-镇/街道] 三方数据入库异常：未获取到三方接口地址信息");
        	}
        	if(CollectionUtils.isNotEmpty(thirdPartyRegionList)){
	       		//执行三方数据入库
	       		//ThirdPartyRegionService.insertBatch(thirdPartyRegionList);
	       		
	       		//Dao.xml:
	       		<insert id="insertBatch">
		        	    INSERT INTO third_party_region
		        	            (id, parent_id, name,type,third_party,sys_region_id)
		        	    VALUES
		        	    <foreach collection ="list" item="thirdPartyRegion" separator =",">
		        	         (#{thirdPartyRegion.id}, #{thirdPartyRegion.parentId}, #{thirdPartyRegion.name}, #{thirdPartyRegion.type}, #{thirdPartyRegion.thirdParty}, #{thirdPartyRegion.sysRegionId})
		        	    </foreach >
	       	    </insert>
	       	}
        }*/
        return response;
	}
	
}
