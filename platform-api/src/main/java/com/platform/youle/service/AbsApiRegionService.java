package com.platform.youle.service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.platform.youle.entity.RequestBaseEntity;
import com.platform.youle.entity.ResponseBaseEntity;
import com.platform.youle.entity.ResponseRegionEntity;
import com.platform.youle.util.TokenUtil;

public abstract class AbsApiRegionService implements IApiFuncServicein {

	/**
	 *
	 * 初始请求参数
	 * 
	 * @param entity
	 */
	@Override
	public void initRequestParam(RequestBaseEntity entity) {
		entity.setWid(TokenUtil.wid);
		entity.setToken(TokenUtil.token);
		entity.setTimestamp(TokenUtil.currentTime.toString());
	}

	/**
	 * 实体转map
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> objectToMap(RequestBaseEntity entity) throws Exception {
		String str = JSON.toJSONString(entity);
		Map<String, Object> map = (Map<String, Object>) JSON.parse(str);
		return map;
	}

	/**
	 * 获取时间戳
	 * 
	 * @return
	 */
	@Override
	public String getTimestamp() {
		return String.valueOf(Calendar.getInstance().getTimeInMillis());
	}
	
	/**
	 * 4.1获取一级地址(省份)
	 * @return
	 */
	public abstract ResponseBaseEntity<JSONArray> province();
	/**
	 * 4.2获取二地址(城市)
	 * @return
	 */
	public abstract ResponseBaseEntity<JSONArray> city(int province);
	/**
	 * 4.3获取三级地址(县/区)
	 * @return
	 */
	public abstract ResponseBaseEntity<JSONArray> county(int city);
	/**
	 * 4.4获取四级地址(镇/街道)
	 * 
	 * @return
	 */
	public abstract ResponseBaseEntity<JSONArray> town(int county);

}
