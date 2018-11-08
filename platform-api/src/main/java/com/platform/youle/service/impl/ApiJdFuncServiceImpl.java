package com.platform.youle.service.impl;

import java.util.Calendar;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import com.platform.youle.constant.Constants;
import com.platform.youle.entity.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.platform.youle.constant.Constants.Urls;
import com.platform.youle.service.AbsApiFuncServicein;
import com.platform.youle.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ApiJdFuncServiceImpl extends AbsApiFuncServicein {

    private static final Logger logger = LoggerFactory.getLogger(ApiJdFuncServiceImpl.class);


    @Override
    public  ResponseBaseEntity<?>  getAllProductIds(){
        ResponseBaseEntity<?>  reponse=null;
        RequestBaseEntity entity = new RequestBaseEntity();
        initRequestParam(entity);
        try {
            logger.info("[1.1获取所有商品ID]入参："+JSONObject.toJSONString(entity));
            String result = HttpUtil.post(Urls.base_test_url+Urls.getAllProductIdsUrl, objectToMap(entity));
            logger.info("[1.1获取所有商品ID]出参："+result);
            reponse = JSON.parseObject(result,new TypeReference<ResponseBaseEntity>(){});
        } catch (Exception e) {
            logger.error("[1.1获取所有商品ID]异常",e);
        }
        return reponse;
    }


    @Override
    public ResponseProductEntity getProductIdsByPage(Integer page) {
        ResponseProductEntity  reponse = null;
        RequestProductEntity entity = new RequestProductEntity();
        initRequestParam(entity);
        entity.setTimestamp(getTimestamp());
        try {
            logger.info("[1.2分页获取当前页商品ID, 每页数据100条]入参：" + JSONObject.toJSONString(entity));
            String result = HttpUtil.post(Urls.base_test_url + Urls.getProductIdsByPage, objectToMap(entity));
            logger.info("[1.2分页获取当前页商品ID, 每页数据100条：" + result);
            reponse = JSON.parseObject(result,ResponseProductEntity.class);
        } catch (Exception e) {
            logger.error("[1.2分页获取当前页商品ID, 每页数据100条]异常", e);
        }

        return reponse;
    }

    @Override
    public ResponseSkuDetailEntity getSkuDetail(Long productId) {
        ResponseSkuDetailEntity  reponse=null;
        RequestSkuDetailEntity entity = new RequestSkuDetailEntity();
        initRequestParam(entity);
        entity.setPid(productId);
        try {
            logger.info("[1.3获取单个商品详情]入参："+JSONObject.toJSONString(entity));
            String result = HttpUtil.post(Urls.base_test_url+Urls.detial, objectToMap(entity));
            logger.info("[1.3获取单个商品详情]出参："+result);
            reponse = JSON.parseObject(result,new TypeReference<ResponseSkuDetailEntity>(){});
            return reponse;
        } catch (Exception e) {
            logger.info("[1.3获取单个商品详情]异常：", e);
        }
        return reponse;
    }



    @Override
    public ResponseProductEntity stock(String pid, Integer num, String address) {
        ResponseProductEntity reponse = null;
        RequestProductStockEntity entity = new RequestProductStockEntity();
        initRequestParam(entity);
        entity.setPid(pid);
        entity.setNum(num);
        entity.setAddress(address);
        try {
            logger.info("[1.4单个查询商品库存]入参：" + JSONObject.toJSONString(entity));
            String result = HttpUtil.post(Urls.base_test_url + Urls.stock, objectToMap(entity));
            logger.info("[1.4单个查询商品库存" + result);
            reponse = JSON.parseObject(result, ResponseProductEntity.class);
        } catch (Exception e) {
            logger.error("[1.4单个查询商品库存]异常", e);
        }

        return reponse;
    }

    @Override
    protected ResponseProductStockBatchEntity stockBatch(String pid_nums, String address) {
        ResponseProductStockBatchEntity reponse = null;
        RequestProductStockBatchEntity entity = new RequestProductStockBatchEntity();
        initRequestParam(entity);
        entity.setPid_nums(pid_nums);
        entity.setAddress(address);
        try {
            logger.info("[1.5批量查询商品库存]：" + JSONObject.toJSONString(entity));
            String result = HttpUtil.post(Urls.base_test_url + Urls.stock, objectToMap(entity));
            logger.info("[1.5批量查询商品库存" + result);
            reponse = JSON.parseObject(result, ResponseProductStockBatchEntity.class);
        } catch (Exception e) {
            logger.error("[1.5批量查询商品库存", e);
        }
        return reponse;
    }


	
    @Override
    public ResponseSaleStatusEntity getsaleStatus(Integer pid) {
        ResponseSaleStatusEntity reponse = null;
        RequstSaleStatusEntity entity = new RequstSaleStatusEntity();
        initRequestParam(entity);
        entity.setPid(pid);
        try {
            logger.info("[1.6查询商品可售状态]入参：" + JSONObject.toJSONString(entity));
            String result = HttpUtil.post(Urls.base_test_url + Urls.saleStatus, objectToMap(entity));
            reponse = JSON.parseObject(result, ResponseSaleStatusEntity.class);
        } catch (Exception e) {
            logger.error("[1.6查询商品可售状态]异常", e);
        }
        return reponse;
    }

    @Override
    protected ResponseGetPriceEntity getPrice(Integer pid) {
        ResponseGetPriceEntity reponse = null;
        RequstSaleStatusEntity entity = new RequstSaleStatusEntity();
        initRequestParam(entity);
        entity.setPid(pid);
        try {
            logger.info("[1.7查询商品协议价]入参：" + JSONObject.toJSONString(entity));
            String result = HttpUtil.post(Urls.base_test_url + Urls.getPrice, objectToMap(entity));
            reponse = JSON.parseObject(result, ResponseGetPriceEntity.class);
        } catch (Exception e) {
            logger.error("[1.7查询商品协议价]异常", e);
        }
        return reponse;
    }

    @Override
    protected ResponseBaseEntity<?> batchSaleStatus(String pids) {

        ResponseBaseEntity reponse = null;
        RequsetBatchSaleStatusEntity entity = new RequsetBatchSaleStatusEntity();
        initRequestParam(entity);
        entity.setPids(pids);
        try {
            logger.info("[1.8批量查询商品可售状态]入参：" + JSONObject.toJSONString(entity));
            String result = HttpUtil.post(Urls.base_test_url + Urls.batchSaleStatus, objectToMap(entity));
            reponse = JSON.parseObject(result, ResponseBaseEntity.class);
        } catch (Exception e) {
            logger.error("[1.8批量查询商品可售状态]异常", e);
        }
        return reponse;
    }

    @Override
    protected ResponseBaseEntity<?> batchGetPrice(String pids) {
        ResponseBaseEntity reponse = null;
        RequsetBatchSaleStatusEntity entity = new RequsetBatchSaleStatusEntity();
        initRequestParam(entity);
        entity.setPids(pids);
        try {
            logger.info("[1.9批量查询商品协议价]入参：" + JSONObject.toJSONString(entity));
            String result = HttpUtil.post(Urls.base_test_url + Urls.batchSaleStatus, objectToMap(entity));
            reponse = JSON.parseObject(result, ResponseBaseEntity.class);
        } catch (Exception e) {
            logger.error("[1.9批量查询商品协议价]异常", e);
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
			logger.info("[2.4订单物流信息接口-根据己方订单号获取]入参："+JSONObject.toJSONString(entity));
			String result = HttpUtil.post(Urls.base_test_url+Urls.orderTrack, objectToMap(entity));
			logger.info("[2.4订单物流信息接口-根据己方订单号获取]出参："+result);
			reponse = JSON.parseObject(result,ResponseOrderTrackEntity.class);
		} catch (Exception e) {
			logger.info("[2.4订单物流信息接口-根据己方订单号获取]异常：", e);
		}
		return reponse;
	}


	@Override
	protected ResponseCancelEntity cancel(String thirdOrder) {
		ResponseCancelEntity response = null;
		RequestOrderTrackEntity entity = new RequestOrderTrackEntity();
		initRequestParam(entity);
		entity.setThirdOrder(thirdOrder);
		try{
			logger.info("[2.6取消订单接口-不支持京东及严选产品]入参："+JSONObject.toJSONString(entity));
			String result = HttpUtil.post(Urls.base_test_url+Urls.orderCancel, objectToMap(entity));
			logger.info("[2.6取消订单接口-不支持京东及严选产品]出参："+result);
			response = JSON.parseObject(result,ResponseCancelEntity.class);
		}catch(Exception e ){
			logger.info("[2.6取消订单接口-不支持京东及严选产品]异常",e);
		}
		return response;
	}


	@Override
	protected ResponseBaseEntity cancelByOrderKey(String thirdOrder, String orderKey) {
		ResponseBaseEntity  response=null;
		RequestCancelByOrderKeyEntity entity = new RequestCancelByOrderKeyEntity();
		initRequestParam(entity);
		entity.setThirdOrder(thirdOrder);
		entity.setOrderKey(orderKey);
		try{
			logger.info("[2.7取消订单接口-子订单取消]入参："+JSONObject.toJSONString(entity));
			String result = HttpUtil.post(Urls.base_test_url+Urls.orderCancelByOrderKey, objectToMap(entity));
			logger.info("[2.7取消订单接口-子订单取消]出参："+result);
			response = JSON.parseObject(result,ResponseCancelEntity.class);
		}catch(Exception e ){
			logger.info("[2.7取消订单接口-子订单取消]异常",e);
		}
		return response;
	}


	


}


