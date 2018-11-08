package com.platform.youle.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.platform.youle.constant.Constants;
import com.platform.youle.entity.GoodsImagePathVo;
import com.platform.youle.entity.JdGoodsVo;
import com.platform.youle.entity.ResponseSkuDetailEntity;
import com.platform.youle.service.ApiFuncService;
import com.platform.youle.util.HttpUtil;

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
//		JSONObject obj = JSONObject.parseObject(str);
//		String date = obj.get("RESULT_DATA").toString();
//		if(StringUtils){
//			
//		}
//		System.out.println(JSONObject.parseObject().get("PRODUCT_DESCRIPTION"));
//		JdGoodsVo good = new JdGoodsVo();
//		good.setMobileProductDecription(obj.get("RESULT_DATA"));
//		good.setProductDecription(obj.get("RESULT_DATA").toString());
//		GoodsImagePathVo imagePath = new GoodsImagePathVo();

	}
}
