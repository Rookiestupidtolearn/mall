package com.platform.youle.service;

import java.util.Calendar;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.platform.youle.entity.ReponseSaleBasisEntity;
import com.platform.youle.entity.RequestBaseEntity;
import com.platform.youle.entity.RequestSaleOrderSubmitEntity;
import com.platform.youle.entity.ResponseSaleCheckEntity;
import com.platform.youle.entity.ResponseSaleDetailEntity;
import com.platform.youle.entity.ResponseSaleOrderSubmitEntity;
import com.platform.youle.entity.ResponseSaleSubmitEntity;
import com.platform.youle.entity.ResquestSaleSubmitEntity;
import com.platform.youle.util.TokenUtil;

@Service
public abstract class AbsApiSaleAfterService implements IApiFuncServicein {

    /**
    *
    * 初始请求参数
    * @param entity
    */
	@Override
   public void initRequestParam(RequestBaseEntity  entity){
       entity.setWid(TokenUtil.wid);
       entity.setTimestamp(String.valueOf(Calendar.getInstance().getTimeInMillis()));
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
 * 	6.1 查询商品是否可以申请售后服务
 * @param thirdOrder
 * @param productId
 * @param type
 * @return
 */
protected abstract ResponseSaleCheckEntity check(String thirdOrder,Integer  productId,String type);

/**
 * 6.2 提交订单售后申请
 * @param entity
 * @return
 */
protected abstract ResponseSaleSubmitEntity submit(ResquestSaleSubmitEntity entity);

/**
 * 6.3 查询售后订单详情
 * @param serviceOrder
 * @return
 */
protected abstract ResponseSaleDetailEntity detail(String serviceOrder);

/**
 * 6.4 提交京东商品订单售后服务申请
 * @param entity
 * @return
 */
protected abstract ResponseSaleOrderSubmitEntity orderSubmit(RequestSaleOrderSubmitEntity entity);

/**
 * 6.5 获取京东商品订单售后单概要信息
 * @param yzhOrderKey
 * @return
 */
protected abstract ReponseSaleBasisEntity basis(String yzhOrderKey);
}
