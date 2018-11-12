package com.platform.youle.service;

import java.util.Map;

import com.platform.youle.entity.RequestBaseEntity;

public interface IApiFuncServicein {
   
    /**
     *
     * 初始请求参数
     * @param entity
     */
     void initRequestParam(RequestBaseEntity  entity);

    /**
     * 实体转map
     * @param entity
     * @return
     * @throws Exception
     */
     Map<String,Object>  objectToMap(RequestBaseEntity entity) throws Exception;

		
    /**
     * 获取时间戳
     * @return
     */
     String getTimestamp ();
}
