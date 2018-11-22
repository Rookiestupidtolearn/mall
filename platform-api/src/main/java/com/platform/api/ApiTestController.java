package com.platform.api;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.dao.ApiBrandMapper;
import com.platform.dao.ApiCategoryMapper;
import com.platform.dao.ApiGoodsGalleryMapper;
import com.platform.dao.ApiGoodsMapper;
import com.platform.dao.ApiProductMapper;
import com.platform.dao.JdGoodsImgPathMapper;
import com.platform.dao.JdGoodsMapper;
import com.platform.entity.BrandVo;
import com.platform.entity.CategoryVo;
import com.platform.entity.GoodsGalleryVo;
import com.platform.entity.GoodsPureInterestRateVo;
import com.platform.entity.GoodsVo;
import com.platform.entity.ProductVo;
import com.platform.entity.ThirdPartyRegionEntity;
import com.platform.entity.UserVo;
import com.platform.service.ApiGoodsPureInterestRateService;
import com.platform.service.ApiUserService;
import com.platform.service.ThirdPartyRegionService;
import com.platform.util.ApiBaseAction;
import com.platform.utils.GenerateCodeUtil;
import com.platform.utils.R;
import com.platform.youle.constant.Constants.Urls;
import com.platform.youle.entity.GoodsImagePathVo;
import com.platform.youle.entity.JdGoodsVo;
import com.platform.youle.entity.ReponseOrderDetailEntity;
import com.platform.youle.entity.RequestBaseEntity;
import com.platform.youle.entity.RequestChildsEntity;
import com.platform.youle.entity.RequestOrderSubmitEntity;
import com.platform.youle.entity.RequestProductStockEntity;
import com.platform.youle.entity.RequestSkuDetailEntity;
import com.platform.youle.entity.ResponseAllProductEntity;
import com.platform.youle.entity.ResponseBaseEntity;
import com.platform.youle.entity.ResponseCancelEntity;
import com.platform.youle.entity.ResponseOrderSubmitEntity;
import com.platform.youle.entity.ResponseOrderTrackEntity;
import com.platform.youle.entity.ResponseSystemOrderTrackEntity;
import com.platform.youle.service.AbsApiGoodsService;
import com.platform.youle.service.AbsApiOrderService;
import com.platform.youle.service.AbsApiRegionService;
import com.platform.youle.service.ApiJDGoodsService;
import com.platform.youle.util.HttpUtil;
import com.platform.youle.util.TokenUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * API测试接口
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-03-23 15:47
 */
@Api(tags = "测试接口")
@RestController
@RequestMapping("/api/test")
public class ApiTestController extends ApiBaseAction {

	@Autowired
	private ApiUserService userService;
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
	private ThirdPartyRegionService thirdPartyRegionService;

	@Autowired
	private AbsApiGoodsService absApiGoodsService;

	@Autowired
	private AbsApiOrderService orderService;
	@Autowired
	private AbsApiRegionService absApiRegionService;
	@Autowired
	private ApiGoodsPureInterestRateService apiGoodsPureInterestRateService;
	@Autowired
	private ApiJDGoodsService apiJDGoodsService;

	// 查询库存默认地址
	private String DEFAULT_ADDRESS = "1_72_2799";

	/**
	 * 获取用户信息
	 */
	@PostMapping("userInfo")
	public R userInfo(@LoginUser UserVo user) {
		return R.ok().put("user", user);
	}

	/**
	 * 忽略Token验证测试
	 */
	@IgnoreAuth
	@PostMapping("notToken")
	public R notToken() {
		return R.ok().put("msg", "无需token也能访问。。。");
	}

	/**
	 * 测试
	 */
	@IgnoreAuth
	@PostMapping(value = "propter")
	public Object test() {

		return R.ok().put("propter", "可以了");
	}
    
  
   

	/**
	 * 根据手机号查询用户信息接口测试方法
	 *
	 * @param mobile
	 * @return
	 */
	@IgnoreAuth
	@PostMapping("userListByMobile")
	public R userList(String mobile) {
		UserVo userEntity = userService.queryByMobile(mobile);
		return R.ok().put("dto", userEntity);
	}

	/**
	 * 5.1大类 入库
	 * 
	 * @return
	 */
	@ApiOperation(value = "保存一级分类")
	@PostMapping("rootCate")
	public JSONObject rootCate() {
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
					CategoryVo vo = new CategoryVo();
					vo.setId(Integer.parseInt(obj.get("code").toString()));
					vo.setName(obj.get("name").toString());
					vo.setIs_show(1);
					vo.setParent_id(obj.get("parentId") == null ? 0 : Integer.parseInt(obj.get("parentId").toString()));
					vo.setLevel(obj.get("level").toString());
					apiCategoryMapper.save(vo);
				}
				resultObj.put("status", "success");
				resultObj.put("msg", "5.1获取一级产品分类保存成功");
			}
		} catch (Exception e) {
			resultObj.put("status", "false");
			resultObj.put("msg", "查询一级分类异常");
			logger.error("[5.1获取一级产品分类保存异常]", e);
		}
		return resultObj;
	}

	/**
	 * 二级分流入库
	 * 
	 * @return
	 */
	@ApiOperation(value = "保存二级分类")
	@PostMapping("childs")
	public JSONObject childs() {
		JSONObject resultObj = new JSONObject();
		List<CategoryVo> categorys = apiCategoryMapper.queryAllCategorys();
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
		return resultObj;
	}

	/*
	 * 保存商品
	 */
	@ApiOperation(value = "查询所有产品id")
	@PostMapping("queryAllProduects")
	public String queryAllProduects() {
		String resultObj = "";
		RequestBaseEntity entity = new RequestBaseEntity();
		initRequestParam(entity);
		try {
			String result = HttpUtil.post(Urls.base_test_url + Urls.getAllProductIdsUrl, objectToMap(entity));
			if (!StringUtil.isEmpty(result)) {
				JSONObject dateObj = JSONObject.parseObject(result);
				resultObj = dateObj.get("RESULT_DATA").toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultObj;
	}

	@ApiOperation(value = "查询所有产品id")
	@PostMapping("saveGoods")
	@Transactional
	public JSONObject saveGoods() {
		JSONObject resultObj = new JSONObject();
		String porducts = this.queryAllProduects();
		// JSONArray proAttr = JSONArray.parseArray(porducts);
		// if(!CollectionUtils.isEmpty(proAttr)){
		// for(int i = 0;i<proAttr.size();i++){
		if (StringUtils.isEmpty(porducts)) {
			resultObj.put("status", "false");
			resultObj.put("msg", "1.1获取所有商品ID为空");
			logger.info("[1.1获取所有商品ID为空]");
			return resultObj;
		}
		List<GoodsPureInterestRateVo> goodsPureInterestRateVoList = new ArrayList<>(); // 批量保存毛利率
		// JSONArray proAttr = JSONArray.parseArray(porducts);
		// if(!CollectionUtils.isEmpty(proAttr)){
		// for(int i = 0;i<proAttr.size();i++){
		if (StringUtils.isEmpty(porducts)) {
			resultObj.put("status", "false");
			resultObj.put("msg", "1.1获取所有商品ID为空");
			logger.info("[1.1获取所有商品ID为空]");
			return resultObj;
		}
		JSONObject obj = JSONObject.parseObject(porducts);
		String res = obj.get("1").toString();
		String newStr = (String) res.subSequence(1, res.length() - 1);
		String[] attr = newStr.split(",");
		for (int j = 0; j < attr.length; j++) {
			RequestSkuDetailEntity entity = new RequestSkuDetailEntity();
			initRequestParam(entity);
			entity.setPid(Long.parseLong(attr[j].toString()));
			try {
				String result = HttpUtil.post(Urls.base_test_url + Urls.detial, objectToMap(entity));
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
					vo.setGoods_sn(productObj.get("productId") == null ? null
							: "JD".concat(productObj.get("productId").toString()));
					vo.setName(productObj.get("name") == null ? "" : productObj.get("name").toString());
					BrandVo brand = new BrandVo();
					if (productObj.get("brand") != null) {
						List<BrandVo> brands = apiBrandMapper.quertBrandByNames(productObj.get("brand").toString());
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
					vo.setGoods_brief(productObj.get("features") == null ? "" : productObj.get("features").toString());
					if (productObj.get("hot") != null) {
						if ("true".equals(productObj.get("hot").toString())) {
							vo.setIs_hot(1);
						}
						if ("false".equals(productObj.get("hot").toString())) {
							vo.setIs_hot(0);
						}
					}
					vo.setGoods_desc(resultDate.get("PRODUCT_DESCRIPTION") == null ? null
							: resultDate.get("PRODUCT_DESCRIPTION").toString());
					vo.setAdd_time(new Date());
					vo.setSource("JD");
					// Map<String,Object> param =
					// stock(productObj.get("productId").toString(), 100,
					// DEFAULT_ADDRESS);
					// if(param.get("num") != null){
					// vo.setGoods_number(Integer.parseInt(param.get("num").toString()));
					// }
					vo.setGoods_number(100);
					apiGoodsMapper.save(vo);
					saveProduct(vo);
					GoodsPureInterestRateVo goodsPureInterestRateVo = saveGoodsPureInterestRate(vo); // 保存商品毛利率
					if (null != goodsPureInterestRateVo) {
						goodsPureInterestRateVoList.add(goodsPureInterestRateVo);
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
				resultObj.put("status", "false");
				resultObj.put("msg", "[1.3获取单个商品详情]保存数据为空");
				logger.error("导入商品异常，商品productid:" + attr[j]);
				continue;
			}
		}
		if (!CollectionUtils.isEmpty(goodsPureInterestRateVoList)) {
			// 执行批量保存商品毛利率
			apiGoodsPureInterestRateService.saveBatch(goodsPureInterestRateVoList);
		}
		resultObj.put("status", "success");
		return resultObj;
		// }
		// }
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

	@ApiOperation(value = "查询所有产品id")
	@PostMapping("saveJdGoods")
	@Transactional
	public Object saveJdGoods() {
		JSONObject resultObj = new JSONObject();
		String porducts = this.queryAllProduects();
		if (StringUtils.isEmpty(porducts)) {
			resultObj.put("status", "false");
			resultObj.put("msg", "1.1获取所有商品ID为空");
			logger.info("[1.1获取所有商品ID为空]");
			return resultObj;
		}
		JSONObject obj = JSONObject.parseObject(porducts);
		String res = obj.get("1").toString();
		String newStr = (String) res.subSequence(1, res.length() - 1);
		String[] attr = newStr.split(",");
		for (int j = 0; j < attr.length; j++) {
			RequestSkuDetailEntity entity = new RequestSkuDetailEntity();
			initRequestParam(entity);
			entity.setPid(Long.parseLong(attr[j].toString()));
			try {
				String result = HttpUtil.post(Urls.base_test_url + Urls.detial, objectToMap(entity));
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
				JSONObject resultDate = JSONObject.parseObject(resObj);
				JdGoodsVo good = new JdGoodsVo();

				good.setMobileProductDecription(resultDate.get("MOBILE_PRODUCT_DESCRIPTION") == null ? ""
						: resultDate.get("MOBILE_PRODUCT_DESCRIPTION").toString());
				good.setProductDecription(resultDate.get("PRODUCT_DESCRIPTION") == null ? ""
						: resultDate.get("PRODUCT_DESCRIPTION").toString());
				if (resultDate.getString("PRODUCT_DATA") != null) {
					String productDate = resultDate.getString("PRODUCT_DATA").toString();
					JSONObject productObj = JSONObject.parseObject(productDate);
					good.setProductId(productObj.get("productId") == null ? null
							: Long.parseLong(productObj.get("productId").toString()));
					good.setName(productObj.get("name") == null ? "" : productObj.get("name").toString());
					good.setType(productObj.get("type") == null ? "" : productObj.get("type").toString());
					good.setThumbnailImage(productObj.get("thumbnailImage") == null ? ""
							: productObj.get("thumbnailImage").toString());
					good.setBrand(productObj.get("brand") == null ? "" : productObj.get("brand").toString());
					good.setProductCate(productObj.get("productCate") == null ? null
							: Integer.parseInt(productObj.get("productCate").toString()));
					good.setProductCode(
							productObj.get("productCode") == null ? "" : productObj.get("productCode").toString());
					good.setStatus(productObj.get("status") == null ? "" : productObj.get("status").toString());
					good.setMarketPrice(productObj.get("marketPrice") == null ? null
							: new BigDecimal(productObj.get("marketPrice").toString()));
					good.setRetailPrice(productObj.get("retailPrice") == null ? null
							: new BigDecimal(productObj.get("retailPrice").toString()));
					good.setProductPlace(
							productObj.get("productPlace") == null ? "" : productObj.get("productPlace").toString());
					good.setFeatures(productObj.get("features") == null ? "" : productObj.get("features").toString());
					if (productObj.get("hot") != null) {
						good.setHot("true".equals(productObj.get("hot").toString()) ? 1 : 0);
					}
					if (productObj.get("is7ToReturn") != null) {
						good.setIs7Toreturn("true".equals(productObj.get("is7ToReturn").toString()) ? 1 : 0);
					}
					good.setCreateTime(new Date());
					good.setId(Long.parseLong(attr[j]));
				}
				jdGoodsMapper.save(good);
				if (resultDate.getString("PRODUCT_IMAGE") != null) {
					JSONArray imgArrays = JSONArray.parseArray(resultDate.getString("PRODUCT_IMAGE").toString());
					if (!CollectionUtils.isEmpty(imgArrays)) {
						for (int i = 0; i < imgArrays.size(); i++) {
							JSONObject obj2 = JSONObject.parseObject(imgArrays.get(i).toString());
							GoodsImagePathVo imagePath = new GoodsImagePathVo();
							imagePath.setImageUrl(obj2.get("imageUrl").toString());
							imagePath.setOrderSort(Integer.parseInt(obj2.get("orderSort").toString()));
							imagePath.setCreateTime(new Date());
							imagePath.setUpdateTime(new Date());
							imagePath.setGoodsId(good.getId());
							goodsImagePathMapper.save(imagePath);
						}
					}
				}

			} catch (Exception e) {
				resultObj.put("status", "false");
				resultObj.put("msg", "[1.3获取单个商品详情]保存数据为空");
				logger.error("导入商品异常，商品productid:" + attr[j], e);
				continue;
			}
		}
		resultObj.put("status", "success");
		return resultObj;
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
		String result = "";
		try {
			result = HttpUtil.post(Urls.base_test_url + Urls.stock, objectToMap(entity));
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

	@IgnoreAuth
	@ApiOperation(value = "2.1创建订单接口")
	@PostMapping("orderSubmit")
	public Object orderSubmit() {

		RequestOrderSubmitEntity entity = new RequestOrderSubmitEntity();
		entity.setThirdOrder(GenerateCodeUtil.buildJDBizNo());
		entity.setPid_nums("81392_1");
		entity.setReceiverName("冯老师");
		entity.setMobile("13391506299");
		entity.setAddress("承德双桥露露集团");
		entity.setProvince("5");
		entity.setCity("239");
		entity.setCounty("48379");
		ResponseOrderSubmitEntity response = orderService.submit(entity);

		return response;
	}

	@IgnoreAuth
	@ApiOperation(value = "2.2查询订单详情接口")
	@PostMapping("orderDetail")
	public Object orderDetail() {

		ReponseOrderDetailEntity response = orderService.detail("3409468966");

		return response;
	}

	@IgnoreAuth
	@ApiOperation(value = "4、三方地址接口")
	@PostMapping("response")
	public Object response() {
		// absApiRegionService.province(); // 省
		List<ThirdPartyRegionEntity> thirdPartyRegionList = thirdPartyRegionService.queryAllByType(3); // 查询全部的省
																										// province:
																										// 省1,
																										// city(市2),
																										// county(区/县3),
																										// town(乡/镇4
		if (!CollectionUtils.isEmpty(thirdPartyRegionList)) {
			for (ThirdPartyRegionEntity thirdPartyRegionEntity : thirdPartyRegionList) {
				// absApiRegionService.city(thirdPartyRegionEntity.getThirdCode());
				// //市
				// absApiRegionService.county(thirdPartyRegionEntity.getThirdCode());
				// //区、县
				absApiRegionService.town(thirdPartyRegionEntity.getThirdCode());// 镇
			}
		}
		return null;
	}

	@ApiOperation(value = "2.3订单反查询接口, 用于确认订单是否创建成功")
	@PostMapping("thirdOrder")
	public Object thirdOrder() {

		ResponseBaseEntity response = orderService.thirdOrder("jd201811091752324423052");

		return response;
	}

	@IgnoreAuth
	@ApiOperation(value = "2.4订单物流信息接口-根据己方订单号获取")
	@PostMapping("orderTrack")
	public Object orderTrack() {

		ResponseOrderTrackEntity response = orderService.orderTrack("jd201811091752324423052");

		return response;
	}

	@IgnoreAuth
	@ApiOperation(value = "2.5订单物流信息接口-根据我方订单号获取")
	@PostMapping("systemOrderTrack")
	public Object systemOrderTrack() {

		ResponseSystemOrderTrackEntity response = orderService.systemOrderTrack("3409468966");

		return response;
	}

	@IgnoreAuth
	@ApiOperation(value = "2.6 取消订单接口（不支持京东及严选产品）")
	@PostMapping("orderCancel")
	public Object orderCancel() {

		ResponseCancelEntity response = orderService.cancel("jd201811091752324423052");

		return response;
	}

	@IgnoreAuth
	@ApiOperation(value = "2.7取消订单接口（子订单取消）")
	@PostMapping("cancelByOrderKey")
	public Object cancelByOrderKey() {

		ResponseBaseEntity response = orderService.cancelByOrderKey("jd201811091752324423052", "3409468966");

		return response;
	}
	
	@IgnoreAuth
	@ApiOperation(value = "2.7取消订单接口（子订单取消）")
	@PostMapping("apiSaveGoods")
	public Object apiSaveGoods() {
		JSONObject obj = apiJDGoodsService.saveCategory();
		return obj;
	}
	
	
	@IgnoreAuth
	@ApiOperation(value = "1.3")
	@PostMapping("queryAllProducts")
	public Object queryAllProducts(String productId) {
		String resultObj = "";
		ResponseAllProductEntity response = null;
		RequestBaseEntity entity = new RequestBaseEntity();
		initRequestParam(entity);
		try {
			String result = HttpUtil.post(Urls.base_test_url + Urls.getAllProductIdsUrl, objectToMap(entity));
//			if (!StringUtil.isEmpty(result)) {
//				JSONObject dateObj = JSONObject.parseObject(result);
//				resultObj = dateObj.get("RESULT_DATA").toString();
//			}
			response = JSON.parseObject(result,new TypeReference<ResponseAllProductEntity>(){});
			if(response.isRESPONSE_STATUS()){
				List array = response.getRESULT_DATA();
				if(!CollectionUtils.isEmpty(array)){
					for(int i = 0;i<array.size();i++){
						JSONObject json = JSONObject.parseObject(array.get(i).toString());
						
						System.out.println(json.get((i+1)+""));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultObj;
	}
	
}