package com.platform.youle.service;

import java.util.Calendar;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.platform.youle.entity.ReponseOrderDetailEntity;
import com.platform.youle.entity.RequestBaseEntity;
import com.platform.youle.entity.RequestOrderSubmitEntity;
import com.platform.youle.entity.ResponseBaseEntity;
import com.platform.youle.entity.ResponseCancelEntity;
import com.platform.youle.entity.ResponseOrderSubmitEntity;
import com.platform.youle.entity.ResponseOrderTrackEntity;
import com.platform.youle.entity.ResponseSystemOrderTrackEntity;
import com.platform.youle.util.MD5util;
import com.platform.youle.util.PropertiesUtil;
import com.platform.youle.util.TokenUtil;

public abstract class AbsApiOrderService implements IApiFuncServicein{
	
	    /**
	     *
	     * 初始请求参数
	     * @param entity
	     */
		@Override
	    public void initRequestParam(RequestBaseEntity  entity){
			  Long currentTime = Calendar.getInstance().getTimeInMillis();
		      entity.setWid(PropertiesUtil.getValue("youle.properties","wid"));
		      entity.setTimestamp(currentTime.toString());
		      String token =getToken(currentTime);
		      entity.setToken(token);
	    }
		private  String getToken(Long currentTime){
			String token = ""; 
	     StringBuffer  tokenStr = new StringBuffer("");
	     tokenStr.append(PropertiesUtil.getValue("youle.properties","wid"));
	     tokenStr.append(PropertiesUtil.getValue("youle.properties","accessToken"));
	     tokenStr.append(currentTime);
	     token = MD5util.encodeByMD5(tokenStr.toString()).toUpperCase();
			return token;
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
	     *2.1创建订单接口
	     * @param entity
	     * @return
	     */
		public abstract ResponseOrderSubmitEntity submit(RequestOrderSubmitEntity entity);
	    /**
	     * 2.2查询订单详情接口
	     * @param orderKey
	     * @return
	     */
		public abstract ReponseOrderDetailEntity detail(String orderKey);
	    /**
	     * 2.3订单反查询接口, 用于确认订单是否创建成功
	     * @param thirdOrder
	     * @return
	     */
		public abstract ResponseBaseEntity<?> thirdOrder(String thirdOrder);
	    /**
	     * 2.4订单物流信息接口-根据己方订单号获取
	     * @param thirdOrder
	     * @return
	     */
		public abstract ResponseOrderTrackEntity orderTrack(String thirdOrder);

	    /**
	     * 2.5订单物流信息接口-根据我方订单号获取
	     * @param thirdOrder
	     * @return
	     */
		public abstract ResponseSystemOrderTrackEntity systemOrderTrack(String orderKey);
	    	
	    /**
	     * 2.6取消订单接口（不支持京东及严选产品）
	     * @param cancel
	     */
		public abstract ResponseCancelEntity cancel(String thirdOrder);
	    
	    /**
	     * 2.7取消订单接口（子订单取消）
	     * @param cancelByOrderKey
	     * @return
	     */
		public abstract ResponseBaseEntity cancelByOrderKey(String thirdOrder,String orderKey);
}
