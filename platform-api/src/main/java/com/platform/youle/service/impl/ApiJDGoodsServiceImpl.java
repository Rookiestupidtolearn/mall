package com.platform.youle.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.platform.dao.ApiBrandMapper;
import com.platform.dao.ApiCategoryMapper;
import com.platform.dao.ApiGoodsGalleryMapper;
import com.platform.dao.ApiGoodsMapper;
import com.platform.dao.ApiProductMapper;
import com.platform.dao.JdGoodsImgPathMapper;
import com.platform.dao.JdGoodsMapper;
import com.platform.entity.BrandVo;
import com.platform.entity.GoodsGalleryVo;
import com.platform.entity.GoodsVo;
import com.platform.entity.ProductVo;
import com.platform.youle.constant.Constants.Urls;
import com.platform.youle.entity.RequestBaseEntity;
import com.platform.youle.entity.RequestProductStockEntity;
import com.platform.youle.entity.RequestSkuDetailEntity;
import com.platform.youle.entity.ResponseBaseEntity;
import com.platform.youle.entity.ResponseSkuDetailEntity;
import com.platform.youle.service.AbsApiGoodsService;
import com.platform.youle.service.ApiJDGoodsService;
import com.platform.youle.util.HttpUtil;
import com.platform.youle.util.TokenUtil;

@Service
public class ApiJDGoodsServiceImpl implements ApiJDGoodsService{

	@Autowired
	private AbsApiGoodsService absApiGoodsService;


	@Autowired
	private ApiCategoryMapper apiCategoryMapper;

	@Autowired
	private ApiBrandMapper apiBrandMapper;
	@Autowired
	private ApiGoodsMapper apiGoodsMapper;
	@Autowired
	private ApiGoodsGalleryMapper apiGoodsGalleryMapper;
	@Autowired
	private JdGoodsMapper jdGoodsMapper;
	@Autowired
	private JdGoodsImgPathMapper goodsImagePathMapper;
	@Autowired
	private ApiProductMapper apiProductMapper;
	// 查询库存默认地址
	private String DEFAULT_ADDRESS = "1_72_2799";
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	
	
//	public JSONObject saveGoods() {}
	
	/**
	 * 查询三方返回所有商品id
	 * @return
	 */
	public String queryAllProduects() {
		ResponseBaseEntity response = absApiGoodsService.getAllProductIds();
		String result = response.getRESULT_DATA().toString();
		return result;
	}
	
	
	public Map<String, Object> stock(String productId, Integer num, String address) {
		Map<String, Object> param = new HashMap<>();
		//// 保存商品库存，初次录入商品，查询地区以北京市(1)朝阳区(72)三环以里(2799) 商品数量100
		Integer number = num;
		RequestProductStockEntity entity = new RequestProductStockEntity();
		initRequestParam(entity);
		entity.setPid(productId);
		entity.setNum(number);
		entity.setAddress(address);
		logger.info("[1.4单个查询商品库存]入参：" + JSONObject.toJSONString(entity));
		String result = "";
		try {
			result = HttpUtil.post(Urls.base_test_url + Urls.stock, objectToMap(entity));
			logger.info("[1.4单个查询商品库存" + result);
			if (result == null) {
				param.put("msg", "三方返回数据为空");
				return param;
			}
		} catch (Exception e) {
			logger.error("[1.4单个查询商品库存]异常", e);
		}
		JSONObject resultDate = JSONObject.parseObject(result);
		String results = resultDate.get("RESULT_DATA").toString();
		if (StringUtils.isEmpty(results)) {
			param.put("msg", "三方返回数据为空");
			return param;
		}
		JSONObject resObj = JSONObject.parseObject(results);
		if (resObj.get("stock_status") == null) {
			param.put("msg", "三方返回数据为空");
			return param;
		}

		if (Boolean.parseBoolean(resObj.get("stock_status").toString())) {
			param.put("msg", "三方返回数据成功");
			param.put("num", number);
			return param;
		}
		return stock(productId, Math.round(number / 2), address);
	}
	
	public void initRequestParam(RequestBaseEntity entity) {
		entity.setWid(TokenUtil.wid);
		entity.setTimestamp(TokenUtil.currentTime.toString());
		entity.setToken(TokenUtil.token);
	}

	public Map<String, Object> objectToMap(RequestBaseEntity entity) throws Exception {
		String str = JSON.toJSONString(entity);
		Map<String, Object> map = (Map<String, Object>) JSON.parse(str);
		return map;
	}
	/**
	 * 保存产品信息
	 * 
	 * @param vo
	 */
	public void saveProduct(GoodsVo vo) {
		ProductVo product = new ProductVo();
		product.setGoods_id(vo.getId());
		product.setGoods_name(vo.getName());
		product.setGoods_number(vo.getGoods_number());
		product.setGoods_sn(vo.getGoods_sn());
		product.setGoods_specification_ids("");
		product.setList_pic_url(vo.getList_pic_url());
		product.setMarket_price(vo.getMarket_price());
		product.setRetail_price(vo.getRetail_price());
		apiProductMapper.save(product);
	}
	
	public void updateProduct(GoodsVo vo) {
		ProductVo product = new ProductVo();
		product.setGoods_id(vo.getId());
		product.setGoods_name(vo.getName());
		product.setGoods_number(vo.getGoods_number());
		product.setGoods_sn(vo.getGoods_sn());
		product.setGoods_specification_ids("");
		product.setList_pic_url(vo.getList_pic_url());
		product.setMarket_price(vo.getMarket_price());
		product.setRetail_price(vo.getRetail_price());
		apiProductMapper.update(product);
	}


	@Override
	public JSONObject saveGoods() {
		// TODO Auto-generated method stub
		return null;
	}
}
