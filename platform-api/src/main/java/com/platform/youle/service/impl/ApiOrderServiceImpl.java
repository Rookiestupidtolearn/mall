package com.platform.youle.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.platform.youle.constant.Constants.Urls;
import com.platform.youle.entity.ReponseOrderDetailEntity;
import com.platform.youle.entity.RequestOrderDetailEntity;
import com.platform.youle.entity.RequestOrderSubmitEntity;
import com.platform.youle.entity.RequestOrderTrackEntity;
import com.platform.youle.entity.RequestThirdOrderEntity;
import com.platform.youle.entity.ResponseBaseEntity;
import com.platform.youle.entity.ResponseOrderSubmitEntity;
import com.platform.youle.entity.ResponseOrderTrackEntity;
import com.platform.youle.entity.ResponseSystemOrderTrackEntity;
import com.platform.youle.service.AbsApiOrderService;
import com.platform.youle.util.HttpUtil;

/**
 * 订单服务
 * @author Administrator
 *
 */
public class ApiOrderServiceImpl  extends AbsApiOrderService{

    	private static final Logger logger = LoggerFactory.getLogger(ApiOrderServiceImpl.class);
		@Override
		protected ResponseOrderSubmitEntity submit(RequestOrderSubmitEntity entity) {
			ResponseOrderSubmitEntity reponse =null;
	        initRequestParam(entity);
	        try {
	            logger.info("[2.1创建订单接口]入参："+JSONObject.toJSONString(entity));
	            String result = HttpUtil.post(Urls.base_test_url+Urls.submit, objectToMap(entity));
	            logger.info("[2.1创建订单接口] 返回结果："+JSONObject.toJSONString(entity));
	            reponse = JSON.parseObject(result,ResponseOrderSubmitEntity.class);
	        } catch (Exception e) {
	            logger.error("[2.1创建订单接口]异常",e);
	        }
	        return reponse;
		}

		@Override
		protected ReponseOrderDetailEntity detail(String orderKey) {
			ReponseOrderDetailEntity reponse =null;
			 RequestOrderDetailEntity entity = new RequestOrderDetailEntity(); 
	        initRequestParam(entity);
	        entity.setOrderKey(orderKey);
	        try {
	            logger.info("[2.2查询订单详情接口]入参："+JSONObject.toJSONString(entity));
	            String result = HttpUtil.post(Urls.base_test_url+Urls.detail, objectToMap(entity));
	            logger.info("[2.2查询订单详情接口] 返回结果："+JSONObject.toJSONString(entity));
	            reponse = JSON.parseObject(result,ReponseOrderDetailEntity.class);
	        } catch (Exception e) {
	            logger.error("[2.2查询订单详情接口]异常",e);
	        }
	        return reponse;
		}
		@Override
		protected ResponseBaseEntity<?> thirdOrder(String thirdOrder) {
			ResponseBaseEntity  reponse=null;
			RequestThirdOrderEntity entity = new RequestThirdOrderEntity();
		    initRequestParam(entity);
		    entity.setThirdOrder(thirdOrder);
			try {
				logger.info("[2.3订单反查询接口, 用于确认订单是否创建成功]入参："+JSONObject.toJSONString(entity));
				String result = HttpUtil.post(Urls.base_test_url+Urls.thirdOrder, objectToMap(entity));
				logger.info("[2.3订单反查询接口, 用于确认订单是否创建成功]出参："+result);
				reponse = JSON.parseObject(result,ResponseBaseEntity.class);
			} catch (Exception e) {
				logger.error("[2.3订单反查询接口, 用于确认订单是否创建成功]异常：", e);
			}
			return reponse;
		}

		@Override
		protected ResponseOrderTrackEntity orderTrack(String thirdOrder) {
			ResponseOrderTrackEntity  reponse=null;
			RequestOrderTrackEntity entity = new RequestOrderTrackEntity();
		    initRequestParam(entity);
		    entity.setThirdOrder(thirdOrder);
			try {
				logger.info("2.4订单物流信息接口-根据己方订单号获取]入参："+JSONObject.toJSONString(entity));
				String result = HttpUtil.post(Urls.base_test_url+Urls.orderTrack, objectToMap(entity));
				logger.info("[2.4订单物流信息接口-根据己方订单号获取]出参："+result);
				reponse = JSON.parseObject(result,ResponseOrderTrackEntity.class);
			} catch (Exception e) {
				logger.error("[2.4订单物流信息接口-根据己方订单号获取]异常：", e);
			}
			return reponse;
		}

	    @Override
	    protected ResponseSystemOrderTrackEntity systemOrderTrack(String orderKey) {
	        ResponseSystemOrderTrackEntity   reponse=null;
	        RequestOrderTrackEntity  entity = new RequestOrderTrackEntity();
	        initRequestParam(entity);
	        entity.setThirdOrder(orderKey);

	        try {
	            logger.info("2.5订单物流信息接口]入参："+JSONObject.toJSONString(entity));
	            String result = HttpUtil.post(Urls.base_test_url+Urls.systemOrderTrack, objectToMap(entity));
	            logger.info("[2.5订单物流信息接口]出参："+result);
	            reponse = JSON.parseObject(result,ResponseSystemOrderTrackEntity.class);
	        } catch (Exception e) {
	            logger.error("[2.5订单物流信息接口]异常：", e);
	        }
	        return reponse;
	    }

}
