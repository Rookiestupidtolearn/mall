package com.platform.youle.service.impl;

import java.util.Calendar;
import java.util.Map;

import com.platform.youle.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.platform.youle.constant.Constants;
import com.platform.youle.constant.Constants.Urls;
import com.platform.youle.service.AbsApiFuncServicein;
import com.platform.youle.util.HttpUtil;
import com.platform.youle.util.TokenUtil;

@Service
public class ApiJdFuncServiceImpl extends AbsApiFuncServicein {

    private static final Logger logger = LoggerFactory.getLogger(ApiJdFuncServiceImpl.class);

	@Override
	public ResponseSkuDetailEntity getSkuDetail(Map<String,Object> params) {
		logger.info("【获取单个商品详情请求参数】:" + params);
		try {
			String result = HttpUtil.post(Constants.Urls.base_test_url.concat(Constants.Urls.detial), params);
			logger.info("【获取单个商品详情响应参数】:" + result);
			ResponseSkuDetailEntity reponse = JSONObject.parseObject(result,new TypeReference<ResponseSkuDetailEntity>(){});
			if(reponse != null){
				if(reponse.isRESPONSE_STATUS()){
					JdGoodsVo good = new JdGoodsVo();
					GoodsImagePathVo imagePath = new GoodsImagePathVo();
					
					
					
				}
			}
			return reponse;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public  ResponseBaseEntity<?>  getAllProductIds(){
		ResponseBaseEntity<?>  reponse=null;
        RequestBaseEntity entity = new RequestBaseEntity();
	    initRequestParam(entity);
		try {
		    logger.info("[1.1获取所有商品ID]入参："+JSONObject.toJSONString(entity));
			String result = HttpUtil.post(Urls.base_test_url+Urls.getAllProductIdsUrl, objectToMap(entity));
			reponse = JSON.parseObject(result,new TypeReference<ResponseBaseEntity>(){});
		} catch (Exception e) {
			logger.error("[1.1获取所有商品ID]异常",e);
		}
		return reponse;
	}


	@Override
	public ResponseProductEntity getProductIdsByPage(Integer page) {
		
		RequestProductEntity entity = new RequestProductEntity();
		  Long timestamp = Calendar.getInstance().getTimeInMillis() ;
			entity.setTimestamp(timestamp.toString());
			entity.setToken(TokenUtil.token);
			entity.setWid(TokenUtil.wid);
		    entity.setPage(page);
			String str = JSON.toJSONString(entity);
			System.out.println("请求参数:"+str);
			try {
				  Map<String,Object> map1 = (Map<String,Object>) JSON.parse(str);
				String result = HttpUtil.post("http://open.fygift.com/api/product/getAllProductIds.php", map1);
				System.out.println("结果:"+result);
				ResponseProductEntity reponse = JSON.parseObject(result,new TypeReference<ResponseProductEntity>(){});
				return reponse;
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		return null;
	}

	@Override
	public ResponseProductEntity stock(String pid, String num, String address) {
		RequestProductEntity entity = new RequestProductEntity();
		  Long timestamp = Calendar.getInstance().getTimeInMillis() ;
			entity.setTimestamp(timestamp.toString());
			entity.setToken(TokenUtil.token);
			entity.setWid(TokenUtil.wid);
		
			String str = JSON.toJSONString(entity);
			System.out.println("请求参数:"+str);
			try {
				  Map<String,Object> map1 = (Map<String,Object>) JSON.parse(str);
				String result = HttpUtil.post("http://open.fygift.com/api/product/getAllProductIds.php", map1);
				System.out.println("结果:"+result);
				ResponseProductEntity reponse = JSON.parseObject(result,new TypeReference<ResponseProductEntity>(){});
				return reponse;
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		return null;
	}

    @Override
    public ResponseSaleStatusEntity getsaleStatus(Integer pid) {
        ResponseSaleStatusEntity reponse = null;
        RequstSaleStatusEntity entity = new RequstSaleStatusEntity();
        initRequestParam(entity);
        entity.setPid(pid);
        try {
            logger.info("[1.6查询商品可售状态]入参："+JSONObject.toJSONString(entity));
            String result = HttpUtil.post(Urls.base_test_url+Urls.saleStatus, objectToMap(entity));
            reponse = JSON.parseObject(result,ResponseSaleStatusEntity.class);
        } catch (Exception e) {
            logger.error("[1.6查询商品可售状态]异常",e);
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
            logger.info("[1.7查询商品协议价]入参："+JSONObject.toJSONString(entity));
            String result = HttpUtil.post(Urls.base_test_url+Urls.getPrice, objectToMap(entity));
            reponse = JSON.parseObject(result,ResponseGetPriceEntity.class);
        } catch (Exception e) {
            logger.error("[1.7查询商品协议价]异常",e);
        }
        return reponse;
    }

    @Override
    protected ResponseBaseEntity<?> batchSaleStatus(String pids) {

        ResponseBaseEntity reponse =null;
        RequsetBatchSaleStatusEntity entity = new RequsetBatchSaleStatusEntity();
        initRequestParam(entity);
        entity.setPids(pids);
        try {
            logger.info("[1.8批量查询商品可售状态]入参："+JSONObject.toJSONString(entity));
            String result = HttpUtil.post(Urls.base_test_url+Urls.batchSaleStatus, objectToMap(entity));
            reponse = JSON.parseObject(result,ResponseBaseEntity.class);
        } catch (Exception e) {
            logger.error("[1.8批量查询商品可售状态]异常",e);
        }
        return reponse;
    }

    @Override
    protected ResponseBaseEntity<?> batchGetPrice(String pids) {
        ResponseBaseEntity reponse =null;
        RequsetBatchSaleStatusEntity entity = new RequsetBatchSaleStatusEntity();
        initRequestParam(entity);
        entity.setPids(pids);
        try {
            logger.info("[1.9批量查询商品协议价]入参："+JSONObject.toJSONString(entity));
            String result = HttpUtil.post(Urls.base_test_url+Urls.batchSaleStatus, objectToMap(entity));
            reponse = JSON.parseObject(result,ResponseBaseEntity.class);
        } catch (Exception e) {
            logger.error("[1.9批量查询商品协议价]异常",e);
        }
        return reponse;
    }
}


