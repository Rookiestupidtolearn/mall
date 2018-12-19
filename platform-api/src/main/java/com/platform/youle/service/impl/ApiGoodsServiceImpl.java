package com.platform.youle.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.platform.youle.constant.Constants.Urls;
import com.platform.youle.entity.RequestBaseEntity;
import com.platform.youle.entity.RequestProductEntity;
import com.platform.youle.entity.RequestProductStockBatchEntity;
import com.platform.youle.entity.RequestProductStockEntity;
import com.platform.youle.entity.RequestSkuDetailEntity;
import com.platform.youle.entity.RequsetBatchSaleStatusEntity;
import com.platform.youle.entity.RequstSaleStatusEntity;
import com.platform.youle.entity.ResponseBaseEntity;
import com.platform.youle.entity.ResponseBatchSaleEntity;
import com.platform.youle.entity.ResponseGetPriceEntity;
import com.platform.youle.entity.ResponseProductEntity;
import com.platform.youle.entity.ResponseProductStockBatchEntity;
import com.platform.youle.entity.ResponseSaleStatusEntity;
import com.platform.youle.entity.ResponseSkuDetailEntity;
import com.platform.youle.service.AbsApiGoodsService;
import com.platform.youle.util.HttpUtil;

/**
 * 
 * 
 * 商品服务
 * @author Administrator
 *
 */
@Service
public class ApiGoodsServiceImpl extends AbsApiGoodsService {

    private static final Logger logger = LoggerFactory.getLogger(ApiGoodsServiceImpl.class);


    @Override
    public  ResponseBaseEntity<?>  getAllProductIds(){
        ResponseBaseEntity<?>  reponse=null;
        RequestBaseEntity entity = new RequestBaseEntity();
        initRequestParam(entity);
        try {
            logger.info("[1.1获取所有商品ID]入参："+JSONObject.toJSONString(entity));
            String result = HttpUtil.post(Urls.base_prod_url+Urls.getAllProductIdsUrl, objectToMap(entity));
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
            String result = HttpUtil.post(Urls.base_prod_url + Urls.getProductIdsByPage, objectToMap(entity));
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
            String result = HttpUtil.post(Urls.base_prod_url+Urls.detial, objectToMap(entity));
            logger.info("[1.3获取单个商品详情]出参："+result);
            reponse = JSON.parseObject(result,new TypeReference<ResponseSkuDetailEntity>(){});
        } catch (Exception e) {
            logger.error("[1.3获取单个商品详情]异常：", e);
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
            String result = HttpUtil.post(Urls.base_prod_url+ Urls.stock, objectToMap(entity));
            logger.info("[1.4单个查询商品库存" + result);
            reponse = JSON.parseObject(result, ResponseProductEntity.class);
        } catch (Exception e) {
            logger.error("[1.4单个查询商品库存]异常", e);
        }

        return reponse;
    }

    @Override
    public ResponseProductStockBatchEntity stockBatch(String pid_nums, String address) {
        ResponseProductStockBatchEntity reponse = null;
        RequestProductStockBatchEntity entity = new RequestProductStockBatchEntity();
        initRequestParam(entity);
        entity.setPid_nums(pid_nums);
        entity.setAddress(address);
        try {
            logger.info("[1.5批量查询商品库存]：" + JSONObject.toJSONString(entity));
            String result = HttpUtil.post(Urls.base_prod_url + Urls.stockBatch, objectToMap(entity));
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
            String result = HttpUtil.post(Urls.base_prod_url+ Urls.saleStatus, objectToMap(entity));
            logger.info("[1.6查询商品可售状态]出参：" +result);
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
            String result = HttpUtil.post(Urls.base_prod_url + Urls.getPrice, objectToMap(entity));
            reponse = JSON.parseObject(result, ResponseGetPriceEntity.class);
        } catch (Exception e) {
            logger.error("[1.7查询商品协议价]异常", e);
        }
        return reponse;
    }

    @Override
    public ResponseBatchSaleEntity batchSaleStatus(String pids) {

    	ResponseBatchSaleEntity reponse = null;
        RequsetBatchSaleStatusEntity entity = new RequsetBatchSaleStatusEntity();
        initRequestParam(entity);
        entity.setPids(pids);
        try {
            logger.info("[1.8批量查询商品可售状态]入参：" + JSONObject.toJSONString(entity));
            String result = HttpUtil.post(Urls.base_prod_url + Urls.batchSaleStatus, objectToMap(entity));
            logger.info("[1.8批量查询商品可售状态] 返回结果：" +result);
            reponse = JSON.parseObject(result, ResponseBatchSaleEntity.class);
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
            String result = HttpUtil.post(Urls.base_prod_url + Urls.batchSaleStatus, objectToMap(entity));
            reponse = JSON.parseObject(result, ResponseBaseEntity.class);
        } catch (Exception e) {
            logger.error("[1.9批量查询商品协议价]异常", e);
        }
        return reponse;
    }
    
}
