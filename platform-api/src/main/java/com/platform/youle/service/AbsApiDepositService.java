package com.platform.youle.service;

import java.util.Calendar;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.platform.youle.entity.RequestBaseEntity;
import com.platform.youle.entity.ResponseDepositEntity;
import com.platform.youle.util.TokenUtil;

public abstract class AbsApiDepositService implements IApiFuncServicein{

		 /**
	    *
	    * 初始请求参数
	    * @param entity
	    */
		@Override
	   public void initRequestParam(RequestBaseEntity  entity){
	       entity.setWid(TokenUtil.wid);
	       entity.setTimestamp(TokenUtil.currentTime.toString());
	       entity.setToken(TokenUtil.token);
	   }
	
	   /**
	    * 实体转map
	    * @param entity
	    * @return
	    * @throws Exception
	    */
		@Override
	   public Map<String,Object>  objectToMap(RequestBaseEntity entity) throws Exception{
	           String str = JSON.toJSONString(entity);
	           Map<String,Object> map = (Map<String,Object>) JSON.parse(str);
	           return map;
	   }
	
		/**
	     * 获取时间戳
	     * @return
	     */
			@Override
		 public String getTimestamp (){
			  return String.valueOf(Calendar.getInstance().getTimeInMillis());
		 }
	
		/**
		 * 3.1查询预存款余额
		 * @return
		 */
		public abstract ResponseDepositEntity remain();
	
}
