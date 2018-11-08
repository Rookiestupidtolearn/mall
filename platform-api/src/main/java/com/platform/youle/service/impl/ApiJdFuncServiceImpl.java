package com.platform.youle.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.platform.dao.JdGoodsImgPathMapper;
import com.platform.dao.JdGoodsMapper;
import com.platform.youle.constant.Constants;
import com.platform.youle.constant.Constants.Urls;
import com.platform.youle.entity.GoodsImagePathVo;
import com.platform.youle.entity.JdGoodsVo;
import com.platform.youle.entity.RequestBaseEntity;
import com.platform.youle.entity.RequestProductEntity;
import com.platform.youle.entity.ResponseBaseEntity;
import com.platform.youle.entity.ResponseProductEntity;
import com.platform.youle.entity.ResponseSaleStatusEntity;
import com.platform.youle.entity.ResponseSkuDetailEntity;
import com.platform.youle.service.AbsApiFuncServicein;
import com.platform.youle.util.HttpUtil;
import com.platform.youle.util.TokenUtil;

@Service
public class ApiJdFuncServiceImpl extends AbsApiFuncServicein {

    private static final Logger logger = LoggerFactory.getLogger(ApiJdFuncServiceImpl.class);
    @Autowired
    private JdGoodsMapper jdGoodsMapper;
    @Autowired
    private JdGoodsImgPathMapper jdGoodsImgPathMapper;

	@Override
	public ResponseSkuDetailEntity getSkuDetail(Map<String,Object> params) {
		
		logger.info("[1.3获取单个商品详情]入参：" + params);
		try {
			String result = HttpUtil.post(Constants.Urls.base_test_url.concat(Constants.Urls.detial), params);
			logger.info("[1.3获取单个商品详情]响应参数：" + result);
			ResponseSkuDetailEntity reponse = JSONObject.parseObject(result,new TypeReference<ResponseSkuDetailEntity>(){});
			if(reponse != null){
				if(reponse.isRESPONSE_STATUS()){
					saveGoodInfo(reponse.getRESULT_DATA(),Long.parseLong(params.get("pid").toString()));
				}
			}
			return reponse;
		} catch (Exception e) {
			logger.info("[1.3获取单个商品详情]异常：", e);
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public  ResponseBaseEntity<?>  getAllProductIds(){
		ResponseBaseEntity<?>  reponse=null;
        RequestBaseEntity entity = new RequestBaseEntity();
	    initRequestParam(entity);
        entity.setTimestamp(getTimestamp());
		try {
			  logger.info("[1.1获取所有商品ID]入参："+JSONObject.toJSONString(entity));
			String result = HttpUtil.post(Urls.base_test_url+Urls.getAllProductIdsUrl, objectToMap(entity));
			reponse = JSON.parseObject(result,new TypeReference<ResponseBaseEntity>(){});
		} catch (Exception e) {
			logger.error("[1.1获取所有商品ID]异常",e);
		}
		return reponse;
	}
	
	public JSONObject saveGoodInfo(String response,Long productId){
		JSONObject resObj = new JSONObject();
		try {
			JSONObject dateObj = JSONObject.parseObject(response);
			String resultObj = dateObj.get("RESULT_DATA").toString();
			if(!StringUtils.isEmpty(resultObj)){
				JSONObject resultDate = JSONObject.parseObject(resultObj);
				JdGoodsVo good = new JdGoodsVo();
				
				if(resultDate.getString("PRODUCT_IMAGE") != null){
					JSONArray imgArrays = JSONArray.parseArray(resultDate.getString("PRODUCT_IMAGE").toString());
					if(!CollectionUtils.isEmpty(imgArrays)){
						for(int i = 0;i<imgArrays.size();i++){
							JSONObject obj = JSONObject.parseObject(imgArrays.get(i).toString());
							GoodsImagePathVo imagePath = new GoodsImagePathVo();
							imagePath.setImageUrl(obj.get("imageUrl").toString());
							imagePath.setOrderSort(Integer.parseInt(obj.get("orderSort").toString()));
							imagePath.setCreateTime(new Date());
							imagePath.setUpdateTime(new Date());
							imagePath.setGoodsSn(productId);
							jdGoodsImgPathMapper.save(imagePath);
						}
					}
				}
				
				good.setMobileProductDecription(resultDate.get("MOBILE_PRODUCT_DESCRIPTION") == null ? "" : resultDate.get("MOBILE_PRODUCT_DESCRIPTION").toString());
				good.setProductDecription(resultDate.get("PRODUCT_DESCRIPTION") == null ? "" : resultDate.get("PRODUCT_DESCRIPTION").toString());
				
				if(resultDate.getString("PRODUCT_DATA") != null){
					String productDate = resultDate.getString("PRODUCT_DATA").toString();
					JSONObject productObj = JSONObject.parseObject(productDate);
					good.setGoodsSn(productObj.get("productId") == null ? null : Long.parseLong(productObj.get("productId").toString()));
					good.setName(productObj.get("name") == null ? "" : productObj.get("name").toString());
					good.setType(productObj.get("type") == null ? "" : productObj.get("type").toString());
					good.setThumbnailImage(productObj.get("thumbnailImage") == null ? "" : productObj.get("thumbnailImage").toString());
					good.setBrand(productObj.get("brand") == null ? "" : productObj.get("brand").toString());
					good.setProductCate(productObj.get("productCate") == null ? null : Integer.parseInt(productObj.get("productCate").toString()));
					good.setProductCode(productObj.get("productCode") == null ? "" : productObj.get("productCode").toString());
					good.setStatus(productObj.get("status") == null ? "" : productObj.get("status").toString());
					good.setMarketPrice(productObj.get("marketPrice") == null ? null : new BigDecimal(productObj.get("marketPrice").toString()));
					good.setRetailPrice(productObj.get("retailPrice") == null ? null : new BigDecimal(productObj.get("retailPrice").toString()));
					good.setProductPlace(productObj.get("productPlace") == null ? "" : productObj.get("productPlace").toString());
					good.setFeatures(productObj.get("features") == null ? "" : productObj.get("features").toString());
					if(productObj.get("hot") != null){
						good.setHot("true".equals(productObj.get("hot").toString()) ? 1: 0);
					}
					if(productObj.get("is7ToReturn") != null){
						good.setIs7Toreturn("true".equals(productObj.get("is7ToReturn").toString()) ? 1: 0);
					}
					good.setCreateTime(new Date());
					good.setId(productId);
				}
				jdGoodsMapper.save(good);
			}
			resObj.put("state", "success");
		} catch (Exception e) {
			resObj.put("state", "fail");
			logger.info("[1.3获取单个商品详情,商品入库]异常：", e);
		}
		return resObj;
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






        return null;
    }
}
