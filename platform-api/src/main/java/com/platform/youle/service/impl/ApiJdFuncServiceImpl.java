package com.platform.youle.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.platform.youle.constant.Constants;
import com.platform.youle.entity.GoodsImagePathVo;
import com.platform.youle.entity.JdGoodsVo;
import com.platform.youle.entity.RequestBaseEntity;
import com.platform.youle.entity.RequestProductEntity;
import com.platform.youle.entity.ResponseBaseEntity;
import com.platform.youle.entity.ResponseProductEntity;
import com.platform.youle.entity.ResponseSkuDetailEntity;
import com.platform.youle.service.ApiFuncService;
import com.platform.youle.util.HttpUtil;
import com.platform.youle.util.TokenUtil;

@Service
public class ApiJdFuncServiceImpl implements ApiFuncService {
  
	private Logger logger = LoggerFactory.getLogger(getClass());

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
	public  ResponseBaseEntity  getAllProductIds(){
	  
	    RequestBaseEntity entity = new RequestBaseEntity();
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
			ResponseBaseEntity reponse = JSON.parseObject(result,new TypeReference<ResponseBaseEntity>(){});
			return reponse;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	    
		return null;
	}

	public static void main(String[] args) {
		String str = " { "
				 +" 	'RESPONSE_STATUS': 'true', "
				 +" 	'RESULT_DATA': { "
				 +" 		'PRODUCT_DATA': { "
				 +" 			'productId': 1101, "
				 +" 			'name': 'API接口测试产品', "
				 +" 			'type': 'system', "
				 +" 			'thumbnailImage': 'http://img.fygift.com/thumbnail/2016/6/4447263056278594047.jpg', "
				 +" 			'brand': '九阳', "
				 +" 			'productCate': 896, "
				 +" 			'productCode': 'GALAXY29', "
				 +" 			'status': 'selling', "
				 +" 			'marketPrice': 2427.0, "
				 +" 			'retailPrice': 29.0, "
				 +" 			'productPlace': '深圳', "
				 +" 			'features': '这是一个测试产品, 勿拍', "
				 +" 			'hot': false, "
				 +" 			'createTime': '2016-03-18 13:41:28', "
				 +" 			'is7ToReturn': false "
				 +" 		}, "
				 +" 		'PRODUCT_IMAGE': [{ "
				 +" 				'imageUrl': 'http://img.fygift.com//2016/6/4447263056278594047.jpg', "
				 +" 				'orderSort': 0 "
				 +" 			}, "
				 +" 			{ "
				 +" 				'imageUrl': 'http://img.fygift.com//2016/6/845661839931297772.jpg', "
				 +" 				'orderSort': 1 "
				 +" 			} "
				 +" 		], "
				 +" 		'PRODUCT_DESCRIPTION': '产品名称：空气净化器</br >产品型号：RSD-JC66R</br>外箱尺寸 540*278*680mm <p > < img src = ’http: //img.fygift.com/attach/2014/10/8624759654857092274.jpg’ style=’height:634px; width:725px’/></p> ', "
				 +" 		'MOBILE_PRODUCT_DESCRIPTION': '' "
				 +" 	} "
				 +" } ";
		JSONObject dateObj = JSONObject.parseObject(str);
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
			}
			
		}

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


}
