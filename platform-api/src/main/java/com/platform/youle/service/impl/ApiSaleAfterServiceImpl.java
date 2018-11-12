package com.platform.youle.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.platform.youle.constant.Constants.Urls;
import com.platform.youle.entity.ReponseSaleBasisEntity;
import com.platform.youle.entity.RequestSaleBasisEntity;
import com.platform.youle.entity.RequestSaleCheckEntity;
import com.platform.youle.entity.RequestSaleDetailEntity;
import com.platform.youle.entity.RequestSaleOrderSubmitEntity;
import com.platform.youle.entity.ResponseSaleCheckEntity;
import com.platform.youle.entity.ResponseSaleDetailEntity;
import com.platform.youle.entity.ResponseSaleOrderSubmitEntity;
import com.platform.youle.entity.ResponseSaleSubmitEntity;
import com.platform.youle.entity.ResquestSaleSubmitEntity;
import com.platform.youle.service.AbsApiSaleAfterService;
import com.platform.youle.util.HttpUtil;

@Service
public class ApiSaleAfterServiceImpl extends AbsApiSaleAfterService {

    private static final Logger logger = LoggerFactory.getLogger(ApiSaleAfterServiceImpl.class);

	@Override
	protected ResponseSaleCheckEntity check(String thirdOrder, Integer productId, String type) {
		ResponseSaleCheckEntity   reponse=null;
		RequestSaleCheckEntity entity = new RequestSaleCheckEntity();
	     initRequestParam(entity);
	     entity.setThirdOrder(thirdOrder);
	     entity.setProductId(productId);
	     entity.setType(type);
	        try {
	            logger.info("[6.1 查询商品是否可以申请售后服务]入参："+JSONObject.toJSONString(entity));
	            String result = HttpUtil.post(Urls.base_test_url+Urls.sale_after_check, objectToMap(entity));
	            logger.info("[6.1 查询商品是否可以申请售后服务]出参："+result);
	            reponse = JSON.parseObject(result,new TypeReference<ResponseSaleCheckEntity>(){});
	        } catch (Exception e) {
	            logger.error("[6.1 查询商品是否可以申请售后服务]异常",e);
	        }
	        return reponse;
	}

	@Override
	protected ResponseSaleSubmitEntity submit(ResquestSaleSubmitEntity entity) {
		ResponseSaleSubmitEntity   reponse=null;
	     initRequestParam(entity);
	        try {
	            logger.info("[6.2 提交订单售后申请]入参："+JSONObject.toJSONString(entity));
	            String result = HttpUtil.post(Urls.base_test_url+Urls.sale_after_submit, objectToMap(entity));
	            logger.info("[6.2 提交订单售后申请]出参："+result);
	            reponse = JSON.parseObject(result,new TypeReference<ResponseSaleSubmitEntity>(){});
	        } catch (Exception e) {
	            logger.error("[6.2 提交订单售后申请]异常",e);
	        }
	        return reponse;
	}

	@Override
	protected ResponseSaleDetailEntity detail(String serviceOrder) {
		ResponseSaleDetailEntity   reponse=null;
		RequestSaleDetailEntity entity = new RequestSaleDetailEntity();
	     initRequestParam(entity);
	     entity.setServiceOrder(serviceOrder);
	        try {
	            logger.info("[6.3 查询售后订单详情]入参："+JSONObject.toJSONString(entity));
	            String result = HttpUtil.post(Urls.base_test_url+Urls.sale_after_detail, objectToMap(entity));
	            logger.info("[6.3 查询售后订单详情]出参："+result);
	            reponse = JSON.parseObject(result,new TypeReference<ResponseSaleDetailEntity>(){});
	        } catch (Exception e) {
	            logger.error("[6.3 查询售后订单详情]异常",e);
	        }
	        return reponse;
	}

	@Override
	protected ResponseSaleOrderSubmitEntity orderSubmit(RequestSaleOrderSubmitEntity entity) {
		ResponseSaleOrderSubmitEntity   reponse=null;
	     initRequestParam(entity);
	        try {
	            logger.info("[6.4 提交京东商品订单售后服务申请]入参："+JSONObject.toJSONString(entity));
	            String result = HttpUtil.post(Urls.base_test_url+Urls.sale_after_jd_submit, objectToMap(entity));
	            logger.info("[6.4 提交京东商品订单售后服务申请]出参："+result);
	            reponse = JSON.parseObject(result,new TypeReference<ResponseSaleOrderSubmitEntity>(){});
	        } catch (Exception e) {
	            logger.error("[6.4 提交京东商品订单售后服务申请]异常",e);
	        }
	        return reponse;
	}

	@Override
	protected ReponseSaleBasisEntity basis(String yzhOrderKey) {
		ReponseSaleBasisEntity   reponse=null;
		RequestSaleBasisEntity entity = new RequestSaleBasisEntity();
	     initRequestParam(entity);
	        try {
	            logger.info("[6.5 获取京东商品订单售后单概要信息]入参："+JSONObject.toJSONString(entity));
	            String result = HttpUtil.post(Urls.base_test_url+Urls.sale_after_basis, objectToMap(entity));
	            logger.info("[6.5 获取京东商品订单售后单概要信息]出参："+result);
	            reponse = JSON.parseObject(result,new TypeReference<ReponseSaleBasisEntity>(){});
	        } catch (Exception e) {
	            logger.error("[6.5 获取京东商品订单售后单概要信息]异常",e);
	        }
	        return reponse;
	}

}
