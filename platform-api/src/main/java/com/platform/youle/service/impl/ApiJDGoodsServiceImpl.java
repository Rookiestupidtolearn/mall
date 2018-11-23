package com.platform.youle.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.util.StringUtil;
import com.platform.dao.ApiBrandMapper;
import com.platform.dao.ApiCategoryMapper;
import com.platform.dao.ApiGoodsGalleryMapper;
import com.platform.dao.ApiGoodsMapper;
import com.platform.dao.ApiProductMapper;
import com.platform.dao.JdGoodsImgPathMapper;
import com.platform.dao.JdGoodsMapper;
import com.platform.entity.BrandVo;
import com.platform.entity.CartVo;
import com.platform.entity.CategoryVo;
import com.platform.entity.GoodsGalleryVo;
import com.platform.entity.GoodsPureInterestRateVo;
import com.platform.entity.GoodsVo;
import com.platform.entity.ProductVo;
import com.platform.service.ApiGoodsPureInterestRateService;
import com.platform.service.ApiGoodsService;
import com.platform.youle.constant.Constants.Urls;
import com.platform.youle.entity.RequestBaseEntity;
import com.platform.youle.entity.RequestChildsEntity;
import com.platform.youle.entity.RequestProductStockEntity;
import com.platform.youle.entity.RequestSkuDetailEntity;
import com.platform.youle.entity.ResponseAllProductEntity;
import com.platform.youle.entity.ResponseBaseEntity;
import com.platform.youle.service.AbsApiGoodsService;
import com.platform.youle.service.ApiJDGoodsService;
import com.platform.youle.util.HttpUtil;
import com.platform.youle.util.TokenUtil;

@Service
public class ApiJDGoodsServiceImpl implements ApiJDGoodsService {

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
	@Autowired
	private ApiGoodsPureInterestRateService apiGoodsPureInterestRateService;
	// 查询库存默认地址
	private String DEFAULT_ADDRESS = "1_72_2799";
	@Autowired
	private ApiGoodsService apiGoodsService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 查询三方返回所有商品id
	 * 
	 * @return
	 */
	public ResponseAllProductEntity queryAllProduects() {
		ResponseAllProductEntity reponse = null;
		RequestBaseEntity entity = new RequestBaseEntity();
		initRequestParam(entity);
		try {
			logger.info("[1.1获取所有商品ID]入参：" + JSONObject.toJSONString(entity));
			String result = HttpUtil.post(Urls.base_test_url + Urls.getAllProductIdsUrl, objectToMap(entity));
			logger.info("[1.1获取所有商品ID]出参：" + result);
			reponse = JSON.parseObject(result, new TypeReference<ResponseAllProductEntity>() {
			});
		} catch (Exception e) {
			logger.error("[1.1获取所有商品ID]异常", e);
		}
		return reponse;
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
		if ((!Boolean.parseBoolean(resObj.get("stock_status").toString())) && number == 1) {
			param.put("msg", "三方返回数据成功,没有库存");
			param.put("num", 0);
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
		// 根据商品id查询product信息
		Map<String, Object> paramMap = new HashMap();
		paramMap.put("goods_id", vo.getId());
		List<ProductVo> productList = apiProductMapper.queryList(paramMap);
		if (org.apache.shiro.util.CollectionUtils.isEmpty(productList)) {
			logger.info("【jd商品更新入规格product】查询商品id为" + vo.getId() + "的商品为空");
		}
		for (ProductVo productVo : productList) {
			productVo.setGoods_id(vo.getId());
			productVo.setGoods_name(vo.getName());
			productVo.setGoods_number(vo.getGoods_number());
			productVo.setGoods_sn(vo.getGoods_sn());
			productVo.setGoods_specification_ids("");
			productVo.setList_pic_url(vo.getList_pic_url());
			productVo.setMarket_price(vo.getMarket_price());
			productVo.setRetail_price(vo.getRetail_price());
			apiProductMapper.update(productVo);
			logger.info("【jd商品更新入规格product】商品id:" + vo.getId() + "更新成功");
		}
	}

	/**
	 * 保存商品毛利率
	 * 
	 * @param vo
	 */
	private GoodsPureInterestRateVo saveGoodsPureInterestRate(GoodsVo vo) {
		if (null != vo) {
			GoodsPureInterestRateVo goodsPureInterestRateVo = new GoodsPureInterestRateVo();
			goodsPureInterestRateVo.setGoodsId(vo.getId());
			goodsPureInterestRateVo.setProductId(0); // 无规格id
			goodsPureInterestRateVo.setMarketPrice(vo.getMarket_price());
			goodsPureInterestRateVo.setRetailPrice(vo.getRetail_price());
			goodsPureInterestRateVo.setCreateUserId(0L);
			goodsPureInterestRateVo.setCreateTime(new Date());
			goodsPureInterestRateVo.setMemo("JD");
			// 计算毛利息
			double PureInterestRate = vo.getMarket_price().subtract(vo.getRetail_price())
					.divide(vo.getRetail_price(), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
			goodsPureInterestRateVo.setPureInterestRate(PureInterestRate);
			return goodsPureInterestRateVo;
			// apiGoodsPureInterestRateService.save(goodsPureInterestRateVo);
		} else {
			logger.info("【jd商品入毛利率】商品为空");
			return null;
		}
	}

	/**
	 * 更新产品毛利率
	 * 
	 * @param vo
	 * @return
	 */
	private boolean updateGoodsPureInterestRate(GoodsVo vo) {
		if (null != vo) {
			// 通过商品id查询毛利率中的商品信息
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("goodsId", vo.getId());
			List<GoodsPureInterestRateVo> goodsPureInterestRateList = apiGoodsPureInterestRateService
					.queryAll(paramMap);
			if (CollectionUtils.isEmpty(goodsPureInterestRateList)) {
				logger.info("【jd商品更新修改毛利率】未查找到毛利率表中商品id为" + vo.getId() + "的商品信息");
				return false;
			}
			for (GoodsPureInterestRateVo goodsPureInterestRateVo : goodsPureInterestRateList) {
				goodsPureInterestRateVo.setMarketPrice(vo.getMarket_price());
				goodsPureInterestRateVo.setRetailPrice(vo.getRetail_price());
				// 计算毛利率
				double PureInterestRate = vo.getMarket_price().subtract(vo.getRetail_price())
						.divide(vo.getRetail_price(), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
				goodsPureInterestRateVo.setPureInterestRate(PureInterestRate);
				goodsPureInterestRateVo.setUpdateTime(new Date());
				apiGoodsPureInterestRateService.update(goodsPureInterestRateVo);
				logger.info("【jd商品更新修改毛利率】商品：" + vo.getId() + "更新成功");
			}
			return true;
		} else {
			logger.info("【jd商品更新修改毛利率】商品为空");
			return false;
		}
	}

	@Override
	public JSONObject saveGoods() {
		JSONObject resultObj = new JSONObject();
		ResponseAllProductEntity response = this.queryAllProduects();
		if (!response.isRESPONSE_STATUS()) {
			resultObj.put("status", "false");
			resultObj.put("msg", "1.1获取所有商品ID为空");
			logger.info("[1.1获取所有商品ID为空]");
			return resultObj;
		}
		List array = response.getRESULT_DATA();
		if (CollectionUtils.isEmpty(array)) {
			resultObj.put("status", "false");
			resultObj.put("msg", "1.1获取所有商品ID为空");
			logger.info("[1.1获取所有商品ID为空]");
			return resultObj;
		}
		for (int i = 0; i < array.size(); i++) {
			JSONObject json = JSONObject.parseObject(array.get(i).toString());
			String res = json.get((i + 1) + "").toString();
			String newStr = (String) res.subSequence(1, res.length() - 1);
			String[] attr = newStr.split(",");
			// 查询所有京东商品 查出库中商品是否匹配到三方返回商品id，没有则置为下架状态
			List<GoodsPureInterestRateVo> goodsPureInterestRateVoList = new ArrayList<>(); // 批量保存毛利率
			List<GoodsVo> goodsVos = apiGoodsMapper.queryAllJDGoods();
			if (!CollectionUtils.isEmpty(goodsVos)) {
				List<String> arrayStr = Arrays.asList(attr);
				List<Integer> list = new ArrayList<>();
				for (int j = 0; j < goodsVos.size(); j++) {
					String goodsn = goodsVos.get(j).getGoods_sn();
					if (!arrayStr.contains(goodsn.substring(2, goodsn.length()))) {
						list.add(goodsVos.get(j).getId());
					}
				}
				if (!CollectionUtils.isEmpty(list)) {
					Integer[] ids = new Integer[list.size()];
					for (int t = 0; t < list.size(); t++) {
						ids[t] = list.get(t);
					}
					apiGoodsService.unSaleBatch(ids,2);
				}
			}

			for (int j = 0; j < attr.length; j++) {
				RequestSkuDetailEntity entity = new RequestSkuDetailEntity();
				initRequestParam(entity);
				entity.setPid(Long.parseLong(attr[j].toString()));

				String result = "";
				try {
					result = HttpUtil.post(Urls.base_test_url + Urls.detial, objectToMap(entity));
					if (StringUtils.isEmpty(result)) {
						resultObj.put("status", "false");
						resultObj.put("msg", "[1.3获取单个商品详情]为空");
						logger.info("[1.3获取单个商品详情]为空,productId:" + Long.parseLong(attr[j].toString()));
						continue;
					}
					JSONObject dateObj = JSONObject.parseObject(result);
					String resObj = dateObj.get("RESULT_DATA").toString();
					if (StringUtils.isEmpty(resObj)) {
						resultObj.put("status", "false");
						resultObj.put("msg", "[1.3获取单个商品详情]为空");
						logger.info("[1.3获取单个商品详情]为空,productId:" + Long.parseLong(attr[j].toString()));
						continue;
					}
					GoodsVo vo = new GoodsVo();
					JSONObject resultDate = JSONObject.parseObject(resObj);

					String productDate = resultDate.get("PRODUCT_DATA").toString();
					if (!StringUtils.isEmpty(productDate)) {
						JSONObject productObj = JSONObject.parseObject(productDate);
						String goodsn = "";
						if (productObj.get("productId") != null) {
							goodsn = "JD".concat(productObj.get("productId").toString());
							vo.setGoods_sn(productObj.get("productId") == null ? null : goodsn);
						}
						List<GoodsVo> goods = apiGoodsMapper.queryGoodsBySn(goodsn);
						if (CollectionUtils.isEmpty(goods)) {
							vo.setName(productObj.get("name") == null ? "" : productObj.get("name").toString());
							BrandVo brand = new BrandVo();
							if (productObj.get("brand") != null) {
								List<BrandVo> brands = apiBrandMapper
										.quertBrandByNames(productObj.get("brand").toString());
								if (CollectionUtils.isEmpty(brands)) {
									brand.setName(productObj.get("brand").toString());
									apiBrandMapper.save(brand);
									vo.setBrand_id(brand.getId());
								}
							}

							vo.setIs_delete(0);
							vo.setList_pic_url(productObj.get("thumbnailImage") == null ? ""
									: productObj.get("thumbnailImage").toString());
							vo.setCategory_id(Integer.parseInt(productObj.get("productCate").toString()));
							if (productObj.get("status") != null) {
								if ("undercarriage".equals(productObj.get("status").toString())) {
									vo.setIs_on_sale(0);
								} else {
									vo.setIs_on_sale(2);
								}
							}
							vo.setMarket_price(productObj.get("marketPrice") == null ? BigDecimal.ZERO
									: new BigDecimal(productObj.get("marketPrice").toString()));
							vo.setRetail_price(productObj.get("retailPrice") == null ? BigDecimal.ZERO
									: new BigDecimal(productObj.get("retailPrice").toString()));
							vo.setGoods_brief(
									productObj.get("features") == null ? "" : productObj.get("features").toString());
							vo.setIs_hot(0);
							vo.setGoods_desc(resultDate.get("PRODUCT_DESCRIPTION") == null ? null
									: resultDate.get("PRODUCT_DESCRIPTION").toString());
							vo.setAdd_time(new Date());
							vo.setUpdate_time(new Date());
							vo.setSource("JD");
							Map<String, Object> param = stock(productObj.get("productId").toString(), 100,
									DEFAULT_ADDRESS);
							if (param.get("num") != null) {
								vo.setGoods_number(Integer.parseInt(param.get("num").toString()));
							}
							apiGoodsMapper.save(vo);
							saveProduct(vo);
							GoodsPureInterestRateVo goodsPureInterestRateVo = saveGoodsPureInterestRate(vo); // 保存商品毛利率
							if (null != goodsPureInterestRateVo) {
								goodsPureInterestRateVoList.add(goodsPureInterestRateVo);
							}
						} else {
							GoodsVo good = goods.get(0);
							good.setName(productObj.get("name") == null ? "" : productObj.get("name").toString());
							BrandVo brand = new BrandVo();
							if (productObj.get("brand") != null) {
								List<BrandVo> brands = apiBrandMapper
										.quertBrandByNames(productObj.get("brand").toString());
								if (CollectionUtils.isEmpty(brands)) {
									brand.setName(productObj.get("brand").toString());
									apiBrandMapper.save(brand);
									good.setBrand_id(brand.getId());
								}
							}

							good.setIs_delete(good.getIs_delete());
							good.setList_pic_url(productObj.get("thumbnailImage") == null ? ""
									: productObj.get("thumbnailImage").toString());
							good.setCategory_id(Integer.parseInt(productObj.get("productCate").toString()));
							if (productObj.get("status") != null) {
								Integer[] ids = new Integer[good.getId()];
								if ("undercarriage".equals(productObj.get("status").toString())) {
									apiGoodsService.unSaleBatch(ids,2);
								} else {
									good.setIs_on_sale(good.getIs_on_sale());
								}
							}
							good.setMarket_price(productObj.get("marketPrice") == null ? BigDecimal.ZERO
									: new BigDecimal(productObj.get("marketPrice").toString()));
							good.setRetail_price(productObj.get("retailPrice") == null ? BigDecimal.ZERO
									: new BigDecimal(productObj.get("retailPrice").toString()));
							good.setGoods_brief(
									productObj.get("features") == null ? "" : productObj.get("features").toString());
							good.setIs_hot(good.getIs_hot());

							good.setGoods_desc(resultDate.get("PRODUCT_DESCRIPTION") == null ? null
									: resultDate.get("PRODUCT_DESCRIPTION").toString());
							good.setAdd_time(good.getAdd_time());
							good.setUpdate_time(new Date());
							good.setSource("JD");
							Map<String, Object> param = stock(productObj.get("productId").toString(), 100,
									DEFAULT_ADDRESS);
							if (param.get("num") != null) {
								good.setGoods_number(Integer.parseInt(param.get("num").toString()));
							}
							apiGoodsMapper.update(good);
							updateProduct(good);
							updateGoodsPureInterestRate(good); // 更新毛利率
						}
					}
					if (resultDate.get("PRODUCT_IMAGE") != null) {
						JSONArray imgattr = JSONArray.parseArray(resultDate.get("PRODUCT_IMAGE").toString());
						if (!CollectionUtils.isEmpty(imgattr)) {
							for (int t = 0; t < imgattr.size(); t++) {
								GoodsGalleryVo galleryVo = new GoodsGalleryVo();
								JSONObject imgObj = JSONObject.parseObject(imgattr.get(t).toString());
								galleryVo.setImg_url(imgObj.get("imageUrl").toString());
								galleryVo.setGoods_id(vo.getId());
								galleryVo.setSort_order(Integer.parseInt(imgObj.get("orderSort").toString()));
								apiGoodsGalleryMapper.save(galleryVo);
							}
						}
					}

				} catch (Exception e) {
					logger.error("[商品入库/更新异常]", e);
				}

			}
			if (!CollectionUtils.isEmpty(goodsPureInterestRateVoList)) {
				// 执行批量保存商品毛利率
				apiGoodsPureInterestRateService.saveBatch(goodsPureInterestRateVoList);
			}

		}

		resultObj.put("status", "success");
		return resultObj;

	}

	@Override
	public JSONObject saveCategory() {
		JSONObject resultObj = new JSONObject();
		String result = "";
		RequestBaseEntity entity = new RequestBaseEntity();
		initRequestParam(entity);
		try {
			result = HttpUtil.post(Urls.base_test_url + Urls.rootCate, objectToMap(entity));
			if (StringUtil.isEmpty(result)) {
				resultObj.put("status", "false");
				resultObj.put("msg", "三方返回数据为空");
				return resultObj;
			}
			JSONObject dateObj = JSONObject.parseObject(result);
			String resultDate = dateObj.get("RESULT_DATA").toString();

			if (StringUtil.isEmpty(resultDate)) {
				resultObj.put("status", "false");
				resultObj.put("msg", "三方返回数据为空");
				return resultObj;
			}
			JSONArray dateAttr = JSONArray.parseArray(resultDate);
			if (!CollectionUtils.isEmpty(dateAttr)) {
				for (int i = 0; i < dateAttr.size(); i++) {
					JSONObject obj = JSONObject.parseObject(dateAttr.get(i).toString());
					System.out.println("父分类" + obj);
					CategoryVo category = apiCategoryMapper.queryObject(obj.get("code").toString());
					if (category != null) {
						category.setName(obj.get("name") == null ? "" : obj.get("name").toString());
						category.setIs_show(1);
						category.setParent_id(
								obj.get("parentId") == null ? 0 : Integer.parseInt(obj.get("parentId").toString()));
						category.setLevel(obj.get("level").toString());
						apiCategoryMapper.update(category);
						continue;
					}
					CategoryVo vo = new CategoryVo();
					vo.setId(Integer.parseInt(obj.get("code").toString()));
					vo.setName(obj.get("name") == null ? "" : obj.get("name").toString());
					vo.setIs_show(1);
					vo.setParent_id(obj.get("parentId") == null ? 0 : Integer.parseInt(obj.get("parentId").toString()));
					vo.setLevel(obj.get("level").toString());
					apiCategoryMapper.save(vo);
				}
				resultObj.put("status", "success");
				resultObj.put("msg", "5.1获取一级产品分类保存成功");
			}
			childs();
		} catch (Exception e) {
			resultObj.put("status", "false");
			resultObj.put("msg", "查询一级分类异常");
			logger.error("[5.1获取一级产品分类保存异常]", e);
		}
		return resultObj;

	}

	/**
	 * 保存二级分类
	 * 
	 * @return
	 */
	public JSONObject childs() {
		JSONObject resultObj = new JSONObject();
		List<CategoryVo> categorys = apiCategoryMapper.queryAllParentIdsCategorys();
		if (CollectionUtils.isEmpty(categorys)) {
			resultObj.put("status", "false");
			resultObj.put("msg", "保存二级分类异常");
			return resultObj;
		}
		for (CategoryVo vo : categorys) {
			RequestChildsEntity entity = new RequestChildsEntity();
			initRequestParam(entity);
			entity.setParentCate(vo.getId());
			try {
				String result = HttpUtil.post(Urls.base_test_url + Urls.childs, objectToMap(entity));

				if (StringUtils.isEmpty(result)) {
					resultObj.put("status", "false");
					resultObj.put("msg", "查询二级分类三方返回数据为空");
					logger.info("[查询二级分类三方返回数据为空],父类id" + vo.getId());
					continue;
				}
				JSONObject dateObj = JSONObject.parseObject(result);
				if (dateObj.get("RESULT_DATA") == null) {
					resultObj.put("status", "false");
					resultObj.put("msg", "查询二级分类三方返回数据为空");
					logger.info("[查询二级分类三方返回数据为空],父类id" + vo.getId());
					continue;
				}
				if (dateObj.get("RESULT_DATA") == null) {
					resultObj.put("status", "false");
					resultObj.put("msg", "查询二级分类三方返回数据为空");
					logger.info("[查询二级分类三方返回数据为空],父类id" + vo.getId());
					continue;
				}
				String resultDate = dateObj.get("RESULT_DATA").toString();
				if (StringUtils.isEmpty(resultDate)) {
					resultObj.put("status", "false");
					resultObj.put("msg", "查询二级分类三方返回数据为空");
					logger.info("[查询二级分类三方返回数据为空],父类id" + vo.getId());
					continue;
				}
				JSONArray dateAttr = JSONArray.parseArray(resultDate);
				if (CollectionUtils.isEmpty(dateAttr)) {
					logger.info("[查询二级分类三方返回数据为空],父类id" + vo.getId());
					continue;
				}
				for (int i = 0; i < dateAttr.size(); i++) {
					JSONObject obj = JSONObject.parseObject(dateAttr.get(i).toString());
					System.out.println("子分类" + obj);
					CategoryVo category = apiCategoryMapper.queryObject(Integer.parseInt(obj.get("code").toString()));
					if (category != null) {
						category.setName(obj.get("name").toString());
						category.setIs_show(1);
						category.setParent_id(
								obj.get("parentId") == null ? 0 : Integer.parseInt(obj.get("parentId").toString()));
						category.setLevel(obj.get("level").toString());
						apiCategoryMapper.update(category);
						continue;
					}

					CategoryVo vos = new CategoryVo();
					List<GoodsVo> goods = apiGoodsMapper.quertGoodsByCategory(obj.get("code").toString());
					if (!CollectionUtils.isEmpty(goods)) {
						for (GoodsVo good : goods) {
							if (good.getList_pic_url() != null) {
								vos.setWap_banner_url(good.getList_pic_url());
							}
							break;
						}
					}
					vos.setId(Integer.parseInt(obj.get("code").toString()));
					vos.setName(obj.get("name").toString());
					vos.setIs_show(1);
					vos.setParent_id(
							obj.get("parentId") == null ? 0 : Integer.parseInt(obj.get("parentId").toString()));
					vos.setLevel(obj.get("level").toString());
					apiCategoryMapper.save(vos);
				}
				resultObj.put("status", "success");
				resultObj.put("msg", "[5.2获取下级产品分类]保存成功");
			} catch (Exception e) {
				resultObj.put("status", "false");
				resultObj.put("msg", "[5.2获取下级产品分类]保存异常");
				logger.error("[5.2获取下级产品分类]保存异常", e);
			}
		}
		saveThirdCategory();
		return resultObj;
	}

	public JSONObject saveThirdCategory() {
		JSONObject resultObj = new JSONObject();
		List<CategoryVo> categorys = apiCategoryMapper.queryAllChildsCategorys();
		if (CollectionUtils.isEmpty(categorys)) {
			resultObj.put("status", "false");
			resultObj.put("msg", "保存二级分类异常");
			return resultObj;
		}
		for (CategoryVo vo : categorys) {
			RequestChildsEntity entity = new RequestChildsEntity();
			initRequestParam(entity);
			entity.setParentCate(vo.getId());
			try {
				String result = HttpUtil.post(Urls.base_test_url + Urls.childs, objectToMap(entity));
				if (StringUtils.isEmpty(result)) {
					resultObj.put("status", "false");
					resultObj.put("msg", "查询二级分类三方返回数据为空");
					return resultObj;
				}
				JSONObject dateObj = JSONObject.parseObject(result);
				String resultDate = dateObj.get("RESULT_DATA").toString();
				if (StringUtils.isEmpty(resultDate)) {
					resultObj.put("status", "false");
					resultObj.put("msg", "查询二级分类三方返回数据为空");
					return resultObj;
				}
				JSONArray dateAttr = JSONArray.parseArray(resultDate);
				if (CollectionUtils.isEmpty(dateAttr)) {
					continue;
				}
				for (int i = 0; i < dateAttr.size(); i++) {
					JSONObject obj = JSONObject.parseObject(dateAttr.get(i).toString());
					CategoryVo category = apiCategoryMapper.queryObject(Integer.parseInt(obj.get("code").toString()));
					if (category != null) {
						category.setName(obj.get("name").toString());
						category.setIs_show(1);
						category.setParent_id(vo.getParent_id());
						category.setLevel(vo.getLevel());
						apiCategoryMapper.update(category);
						continue;
					}
					CategoryVo vos = new CategoryVo();
					List<GoodsVo> goods = apiGoodsMapper.quertGoodsByCategory(obj.get("code").toString());
					if (!CollectionUtils.isEmpty(goods)) {
						for (GoodsVo good : goods) {
							if (good.getList_pic_url() != null) {
								vos.setWap_banner_url(good.getList_pic_url());
							}
							break;
						}
					}
					vos.setId(Integer.parseInt(obj.get("code").toString()));
					vos.setName(obj.get("name").toString());
					vos.setIs_show(1);
					vos.setParent_id(vo.getParent_id());
					vos.setLevel(vo.getLevel());
					apiCategoryMapper.save(vos);
				}
				resultObj.put("status", "success");
				resultObj.put("msg", "[5.2获取下级产品分类]保存成功");
			} catch (Exception e) {
				resultObj.put("status", "false");
				resultObj.put("msg", "[5.2获取下级产品分类]保存异常");
				logger.error("[5.2获取下级产品分类]保存异常", e);
			}
		}
		return resultObj;
	}
}
