package com.platform.youle.service;

import java.util.Calendar;
import java.util.Map;

import com.alibaba.fastjson.JSON;
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
		entity.setTimestamp(String.valueOf(Calendar.getInstance().getTimeInMillis()));
		entity.setToken(TokenUtil.token);
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
	protected abstract ResponseBaseEntity<ResponseRegionEntity> province();
	/**
	 * 4.2获取二地址(城市)
	 * @return
	 */
	protected abstract ResponseBaseEntity<ResponseRegionEntity> city(int province);
	/**
	 * 4.3获取三级地址(县/区)
	 * @return
	 */
	protected abstract ResponseBaseEntity<ResponseRegionEntity> county(int city);
	/**
	 * 4.4获取四级地址(镇/街道)
	 * @return
	 */
	protected abstract ResponseBaseEntity<ResponseRegionEntity> town(int county);

}
