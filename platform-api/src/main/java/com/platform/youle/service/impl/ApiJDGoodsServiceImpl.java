package com.platform.youle.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.util.StringUtil;
import com.platform.dao.*;
import com.platform.entity.*;
import com.platform.service.ApiGoodsPureInterestRateService;
import com.platform.service.ApiGoodsService;
import com.platform.youle.constant.Constants.Urls;
import com.platform.youle.entity.*;
import com.platform.youle.service.AbsApiGoodsService;
import com.platform.youle.service.ApiJDGoodsService;
import com.platform.youle.util.HttpUtil;
import com.platform.youle.util.MD5util;
import com.platform.youle.util.PropertiesUtil;
import org.apache.commons.collections.CollectionUtils;
import org.jgroups.util.ConcurrentLinkedBlockingQueue2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	private ApiProductMapper apiProductMapper;
	@Autowired
	private ApiGoodsPureInterestRateService apiGoodsPureInterestRateService;
	@Autowired
	private ApiJdProductIdsMapper apiJdProductIdsMapper;

	// 查询库存默认地址
	private String DEFAULT_ADDRESS = "1_72_2799";
	@Autowired
	private ApiGoodsService apiGoodsService;
	@Resource(name = "transactionManager")
	private DataSourceTransactionManager txManager;

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
			String result = HttpUtil.post(Urls.base_prod_url + Urls.getAllProductIdsUrl, objectToMap(entity));
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
		String result = "";
		try {
			result = HttpUtil.post(Urls.base_prod_url + Urls.stock, objectToMap(entity));
			if (result == null) {
				param.put("msg", "三方返回数据为空");
				return param;
			}
		} catch (Exception e) {
			logger.error("[1.4单个查询商品库存]异常", e);
		}
		JSONObject resultDate = JSONObject.parseObject(result);
		if (resultDate.get("RESULT_DATA") == null) {
			param.put("msg", "三方返回数据为空");
			return param;
		}
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
		} else {
			param.put("msg", "三方返回数据成功,没有库存");
			param.put("num", 0);
			return param;
		}
		// if ((!Boolean.parseBoolean(resObj.get("stock_status").toString())) &&
		// number == 1) {
		// param.put("msg", "三方返回数据成功,没有库存");
		// param.put("num", 0);
		// return param;
		// }
		// return stock(productId, Math.round(number / 2), address);
	}

	/**
	 * 初始请求参数
	 *
	 * @param entity
	 */
	public void initRequestParam(RequestBaseEntity entity) {
		Long currentTime = Calendar.getInstance().getTimeInMillis();
		entity.setWid(PropertiesUtil.getValue("youle.properties", "wid"));
		entity.setTimestamp(currentTime.toString());
		String token = getToken(currentTime);
		entity.setToken(token);
	}

	private String getToken(Long currentTime) {
		String token = "";
		StringBuffer tokenStr = new StringBuffer("");
		tokenStr.append(PropertiesUtil.getValue("youle.properties", "wid"));
		tokenStr.append(PropertiesUtil.getValue("youle.properties", "accessToken"));
		tokenStr.append(currentTime);
		token = MD5util.encodeByMD5(tokenStr.toString()).toUpperCase();
		return token;
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

	/**
	 * 保存更新第三方京东的商品v1 采用
	 * */
	@Override
	public JSONObject saveGoods() {
		JSONObject resultObj = new JSONObject();
		ResponseAllProductEntity response = this.queryAllProduects();//查询所有第三方商品id
		if (!response.isRESPONSE_STATUS()) {
			resultObj.put("status", "false");
			resultObj.put("msg", "1.1获取所有商品异常");
			logger.info("[1.1获取所有商品异常]");
			return resultObj;
		}
		logger.info("[1.1获取所有商品]产品总数量" + response.getTOTAL_AMOUNT());
		List<?> array = response.getRESULT_DATA();
		if (CollectionUtils.isEmpty(array)) {
			resultObj.put("status", "false");
			resultObj.put("msg", "1.1获取所有商品ID为空");
			logger.info("[1.1获取所有商品ID为空]");
			return resultObj;
		}
		List<String> productIds = null;;//查询所有本地京东商品并下架第三方没有的商品id
		JSONObject json = JSONObject.parseObject(array.get(0).toString());
		productIds = getJdProductIds(json);
		segmentExecute(productIds,resultObj);// 开启多线程分段任务执行
		logger.info("京东商品更新完成");
		return resultObj;

	}

	/**
	 * 多线程分段任务执行更新京东商品
	 * @param productIds
	 * @param resultObj
	 */
	public void segmentExecute(List<String> productIds,JSONObject resultObj){

		try {
			int productIdsListSize=productIds.size();
			int count=1500;//每个线程执行数
			int runSize=productIdsListSize/count;//开启线程数
			BlockingQueue<Runnable> queue = new ConcurrentLinkedBlockingQueue2(runSize);
			final ExecutorService fixedThreadPool=new ThreadPoolExecutor(runSize,runSize,
					1, TimeUnit.HOURS,
					queue);//创建线程池
//			fixedThreadPool = Executors.newFixedThreadPool(runSize);
			CountDownLatch countDownLatch=new CountDownLatch(runSize);
			logger.info("开始多线程执行更新京东商品"+"-----共有"+productIdsListSize+" ---开启线程个数"+runSize);
			for (int i = 0;i < runSize;i++) {
				List<String> newList=null;//把list分段
				if((i+1)==runSize){
					int startIndex = (i*count);
					int endIndex = productIdsListSize;
					newList =productIds.subList(startIndex,endIndex);
				}else{
					int startIndex = i*count;
					int endIndex = (i+1)*count;
					newList =productIds.subList(startIndex,endIndex);
				}
				fixedThreadPool.execute(new SaveJDGoodsJob(newList,resultObj,countDownLatch));//执行任务
			}
			fixedThreadPool.shutdown();
			countDownLatch.await();//阻塞等到所有任务都执行完成后进入主线程

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@Override
	public JSONObject saveCategory() {
		JSONObject resultObj = new JSONObject();
		String result = "";
		RequestBaseEntity entity = new RequestBaseEntity();
		initRequestParam(entity);
		try {
			result = HttpUtil.post(Urls.base_prod_url + Urls.rootCate, objectToMap(entity));
			if (StringUtil.isEmpty(result)) {
				resultObj.put("status", "false");
				resultObj.put("msg", "三方返回数据为空");
				return resultObj;
			}
			JSONObject dateObj = JSONObject.parseObject(result);
			if (dateObj.get("RESULT_DATA") == null) {
				resultObj.put("status", "false");
				resultObj.put("msg", "三方返回数据为空");
				return resultObj;
			}
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
					CategoryVo category = apiCategoryMapper.queryObject(obj.get("code").toString());
					if (category != null) {
//						category.setName(obj.get("name") == null ? "" : obj.get("name").toString());
//						category.setIs_show(1);
//						category.setParent_id(
//								obj.get("parentId") == null ? 0 : Integer.parseInt(obj.get("parentId").toString()));
//						category.setLevel(obj.get("level").toString());
//						apiCategoryMapper.update(category);
//						continue;
						apiCategoryMapper.delete(Integer.parseInt(obj.get("code").toString()));
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
				String result = HttpUtil.post(Urls.base_prod_url + Urls.childs, objectToMap(entity));

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
					CategoryVo category = apiCategoryMapper.queryObject(Integer.parseInt(obj.get("code").toString()));
					if (category != null) {
//						category.setName(obj.get("name").toString());
//						category.setIs_show(1);
//						category.setParent_id(
//								obj.get("parentId") == null ? 0 : Integer.parseInt(obj.get("parentId").toString()));
//						category.setLevel(obj.get("level").toString());
//						apiCategoryMapper.update(category);
//						continue;
						apiCategoryMapper.delete(Integer.parseInt(obj.get("code").toString()));
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
				String result = HttpUtil.post(Urls.base_prod_url + Urls.childs, objectToMap(entity));
				if (StringUtils.isEmpty(result)) {
					resultObj.put("status", "false");
					resultObj.put("msg", "查询三级分类三方返回数据为空");
					return resultObj;
				}
				JSONObject dateObj = JSONObject.parseObject(result);
				String resultDate = dateObj.get("RESULT_DATA").toString();
				if (StringUtils.isEmpty(resultDate)) {
					resultObj.put("status", "false");
					resultObj.put("msg", "查询三级分类三方返回数据为空");
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
//						category.setName(obj.get("name").toString());
//						category.setIs_show(1);
//						category.setParent_id(
//								obj.get("parentId") == null ? 0 : Integer.parseInt(obj.get("parentId").toString()));
//						category.setLevel(obj.get("level").toString());
//						apiCategoryMapper.update(category);
//						continue;
						apiCategoryMapper.delete(Integer.parseInt(obj.get("code").toString()));
					}
					CategoryVo vos = new CategoryVo();
					List<GoodsVo> goods = apiGoodsMapper.quertGoodsByCategory(obj.get("code").toString());
					if(obj.get("parentId") != null){
						CategoryVo parentVo = apiCategoryMapper.queryObject(Integer.parseInt(obj.get("parentId").toString()));
						if (!CollectionUtils.isEmpty(goods)) {
							for (GoodsVo good : goods) {
								if (good.getList_pic_url() != null) {
									parentVo.setWap_banner_url(good.getList_pic_url());
									vos.setWap_banner_url(good.getList_pic_url());
									apiCategoryMapper.update(parentVo);
								}
								break;
							}
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

	/**
	 * 1.获取三方所有产品id 2.校验本地与三方产品id，如果本地有三方没有则本地产品下架
	 *
	 * @param json
	 * @return
	 */
	public List<String> getJdProductIds(JSONObject json) {
		List<String> ids = new ArrayList<>();//第三方商品id集合
		for (int i = 0; i < json.size(); i++) {
			String res = json.get((i + 1) + "").toString();
			String newStr = (String) res.subSequence(1, res.length() - 1);
			String[] attr = newStr.split(",");
			List<String> arrayStr = Arrays.asList(attr);
			ids.addAll(arrayStr);
		}
		//List<GoodsVo> goodsVos = apiGoodsMapper.queryAllJDGoods();
		List<GoodsVo> goodsVos = apiGoodsMapper.queryAllJDGoodsID();
		if (!CollectionUtils.isEmpty(goodsVos)) {
			List<Integer> list = new ArrayList<>();
//			List<GoodsVo> reduce1 = goodsVos.stream().filter(item-> !ids.contains(item.getGoods_sn().substring(2, item.getGoods_sn().length()))).collect(toList());
			for (int j = 0; j < goodsVos.size(); j++) {
				String goodsn = goodsVos.get(j).getGoods_sn();
				if (!ids.contains(goodsn.substring(2, goodsn.length()))) {
					list.add(goodsVos.get(j).getId());
				}
			}
			if (!CollectionUtils.isEmpty(list)) { //不为空时批量更新

				Integer[] goodIds = list.toArray(new Integer[0]);
				int count = 1000;
				int runSize = goodIds.length / count;

				final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(runSize);
				CountDownLatch countDownLatch = new CountDownLatch(runSize);
				logger.info("开启多个线程执行批量更新----共有"+goodIds.length+"个商品---开启线程数"+runSize);
				try {
					for (int i = 0; i < runSize; i++) { //把list分段
						Integer[] newgoodIds = null;
						if ((i + 1) == runSize) {
							int startIndex = (i * count);
							int endIndex = goodIds.length;
							newgoodIds = Arrays.copyOfRange(goodIds, startIndex, endIndex);
						} else {
							int startIndex = i * count;
							int endIndex = (i + 1) * count;
							newgoodIds = Arrays.copyOfRange(goodIds, startIndex, endIndex);
						}
						fixedThreadPool.execute(new UpdateBathJDGoods(countDownLatch, newgoodIds));//执行任务

					}

					fixedThreadPool.shutdown();
					countDownLatch.await();

					logger.info("批量操作执行下架商品完成");
				} catch (InterruptedException e) {
					logger.error("批量操作执行下架商品出现异常", e);
				}
			}
		}


		return ids;

	}

	/**
	 * 查分多个list
	 *
	 * @param source
	 * @param n
	 * @return
	 */
	public static <T> List<List<T>> averageAssign(List<T> source, int n) {
		List<List<T>> result = new ArrayList<List<T>>();
		int remaider = source.size() % n; // (先计算出余数)
		int number = source.size() / n; // 然后是商
		int offset = 0;// 偏移量
		for (int i = 0; i < n; i++) {
			List<T> value = null;
			if (remaider > 0) {
				value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
				remaider--;
				offset++;
			} else {
				value = source.subList(i * number + offset, (i + 1) * number + offset);
			}
			result.add(value);
		}
		return result;
	}

	@Override
	public JSONObject saveJdProductIds() {
		JSONObject resultObj = new JSONObject();
		ResponseAllProductEntity response = this.queryAllProduects();
		if (!response.isRESPONSE_STATUS()) {
			resultObj.put("status", "false");
			resultObj.put("msg", "1.1获取所有商品异常");
			logger.info("[1.1获取所有商品异常]");
			return resultObj;
		}
		logger.info("[1.1获取所有商品]产品总数量" + response.getTOTAL_AMOUNT());
		List<?> array = response.getRESULT_DATA();
		if (CollectionUtils.isEmpty(array)) {
			resultObj.put("status", "false");
			resultObj.put("msg", "1.1获取所有商品ID为空");
			logger.info("[1.1获取所有商品ID为空]");
			return resultObj;
		}
		JSONObject json = JSONObject.parseObject(array.get(0).toString());
		List<String> productIds = getJdProductIds(json);

		JSONObject resObjs = saveJdProductIds(productIds);
		if (!Boolean.parseBoolean(resObjs.get("state").toString())) {
			resultObj.put("status", "false");
			resultObj.put("msg", "保存三方productIds异常");
			logger.info("[保存三方productIds异常]");
			return resultObj;
		}
		resultObj.put("status", "true");
		resultObj.put("msg", "保存三方productIds成功");
		logger.info("[保存三方productIds成功]");
		return null;
	}

	public JSONObject saveJdProductIds(List<String> ids) {
		JSONObject resultObj = new JSONObject();
		JdProductIdsVo vo = new JdProductIdsVo();
		try {
			for (String id : ids) {
				JdProductIdsVo productVo = apiJdProductIdsMapper.queryObject(id);
				if (productVo != null) {
					continue;
				}
				vo.setProductId(Integer.parseInt(id));
				vo.setDeleteFlag("0");
				vo.setCreateTime(new Date());
				vo.setUpdateTime(new Date());
				apiJdProductIdsMapper.save(vo);
			}
			resultObj.put("state", true);
		} catch (Exception e) {
			logger.error("[保存三方产品id异常]", e);
			resultObj.put("state", false);
		}
		return resultObj;
	}

	@Override
	public JSONObject checkJdStore() {
		JSONObject param = new JSONObject();
		//// 保存商品库存，初次录入商品，查询地区以北京市(1)朝阳区(72)三环以里(2799) 商品数量100
		List<GoodsVo> goods = apiGoodsMapper.queryGoods();
		if (CollectionUtils.isEmpty(goods)) {
			param.put("msg", "本地申请上架商品为空");
			return param;
		}
		for (GoodsVo vo : goods) {
			Map<String, Object> rssult = autoStock(vo.getProduct_id().toString(), 100, DEFAULT_ADDRESS);
			GoodsVo good = apiGoodsMapper.queryObject(vo.getId());
			good.setGoods_number(rssult.get("num") == null ? 0 : Integer.parseInt(rssult.get("num").toString()));
			apiGoodsMapper.update(good);
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("goods_id", vo.getId());
			List<ProductVo> productList = apiProductMapper.queryList(paramMap);
			if (!CollectionUtils.isEmpty(productList)) {
				for (ProductVo product : productList) {
					product.setGoods_number(
							rssult.get("num") == null ? 0 : Integer.parseInt(rssult.get("num").toString()));
					apiProductMapper.update(product);
				}
			}
		}
		param.put("msg", "更新本地库存成功");
		return param;
	}

	public Map<String, Object> autoStock(String productId, Integer num, String address) {
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
			result = HttpUtil.post(Urls.base_prod_url + Urls.stock, objectToMap(entity));
			if (result == null) {
				param.put("msg", "三方返回数据为空");
				return param;
			}
		} catch (Exception e) {
			logger.error("[1.4单个查询商品库存]异常", e);
		}
		JSONObject resultDate = JSONObject.parseObject(result);
		if (resultDate.get("RESULT_DATA") == null) {
			param.put("msg", "三方返回数据为空");
			return param;
		}
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
		return autoStock(productId, Math.round(number / 2), address);
	}

	public String getGoodsImg(String imgs) {
		StringBuffer goodsImg = new StringBuffer();
		Pattern p_image;
		Matcher m_image;
		String regEx_img = "(i?)<img.*? src=\"?(.*?\\.(jpg|gif|bmp|bnp|png))\".*? (>|/>|</img>)";
		p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
		m_image = p_image.matcher(imgs);
		while (m_image.find()) {
			goodsImg.append(m_image.group());
		}
		if (StringUtils.isEmpty(goodsImg)) {
			return "";
		}
		return goodsImg.toString();
	}
	class UpdateBathJDGoods implements Runnable{
		public UpdateBathJDGoods(CountDownLatch await, Integer[] goodIds) {
			this.await = await;
			this.goodIds = goodIds;
		}

		CountDownLatch await=null;
		Integer[] goodIds=null;
		@Override
		public void run() {
			apiGoodsService.unSaleBatch(goodIds, 2);
			await.countDown();
		}
	}

	/**
	 * 定义更新保存京东商品执行任务类
	 */
	class SaveJDGoodsJob implements Runnable{ // 定义执行任务
		public SaveJDGoodsJob(List<String> productIds, JSONObject resultObj,CountDownLatch countDownLatch) {
			this.productIds = productIds;
			this.resultObj = resultObj;
			this.countDownLatch=countDownLatch;
		}
		List<String> productIds=null;
		JSONObject resultObj =null;
		CountDownLatch countDownLatch=null;
		@Override
		public void run() {
			int idSize =productIds.size();
			logger.info("Thead"+Thread.currentThread().getName()+"保存京东商品执行开始共有"+idSize+"个");
			try {
				for(int index=0;index<idSize;index++){
					logger.info("Thead"+Thread.currentThread().getName()+"执行保存京东商品第"+index+"个开始");
					// 查询所有京东商品 查出库中商品是否匹配到三方返回商品id，没有则置为下架状态
					List<GoodsPureInterestRateVo> goodsPureInterestRateVoList = new ArrayList<>(); // 批量保存毛利率
//					DefaultTransactionDefinition def = new DefaultTransactionDefinition();
//					def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事务等级，
//					TransactionStatus txStatus = txManager.getTransaction(def);// 获得事务状态
					RequestSkuDetailEntity entity = new RequestSkuDetailEntity();
					initRequestParam(entity);
					entity.setPid(Long.parseLong(productIds.get(index).toString()));
					String result = "";
					try {
						result = HttpUtil.post(Urls.base_prod_url + Urls.detial, objectToMap(entity));
						if (StringUtils.isEmpty(result)) {
							resultObj.put("status", "false");
							resultObj.put("msg", "[1.3获取单个商品详情]result为空");
							logger.info("[1.3获取单个商品详情]result为空,productId:"
									+ Long.parseLong(productIds.get(index).toString()));
							continue;
						}
						JSONObject dateObj = JSONObject.parseObject(result);
						if (dateObj.get("RESULT_DATA") == null) {
							resultObj.put("status", "false");
							resultObj.put("msg", "[1.3获取单个商品详情]RESULT_DATA为空" + dateObj);
							logger.info("[1.3获取单个商品详情]RESULT_DATA为空,productId:" + productIds.get(index));
							continue;
						}
						String resObj = dateObj.get("RESULT_DATA").toString();
						if (StringUtils.isEmpty(resObj)) {
							resultObj.put("status", "false");
							resultObj.put("msg", "[1.3获取单个商品详情]为空");
							logger.info("[1.3获取单个商品详情]为空,productId:" + productIds.get(index));
							continue;
						}
						GoodsVo vo = new GoodsVo();
						JSONObject resultDate = JSONObject.parseObject(resObj);
						if (resultDate.get("PRODUCT_DATA") == null) {
							resultObj.put("status", "false");
							resultObj.put("msg", "[1.3获取单个商品详情]PRODUCT_DATA为空");
							logger.info("[1.3获取单个商品详情]PRODUCT_DATA为空,productId:" + productIds.get(index));
							continue;
						}
						String productDate = resultDate.get("PRODUCT_DATA").toString();
						if (!StringUtils.isEmpty(productDate)) {
							JSONObject productObj = JSONObject.parseObject(productDate);
							String goodsn = "";//?不加JD?
							if (productObj.get("productId") != null) {
								goodsn = "JD".concat(productObj.get("productId").toString());
								vo.setGoods_sn(productObj.get("productId") == null ? null : goodsn);
							}
							List<GoodsVo> goods = apiGoodsMapper.queryGoodsBySn(goodsn);//根据第三方id查询本地库中的商品信息
							if (CollectionUtils.isEmpty(goods)) { //本地库中不存在此商品的情况下添加
								vo.setName(productObj.get("name") == null ? "" : productObj.get("name").toString());
								BrandVo brand = new BrandVo();
								if (productObj.get("status") != null) {
									if ("undercarriage".equals(productObj.get("status").toString())) {
										vo.setIs_on_sale(0);
									} else {
										vo.setIs_on_sale(2);
									}
								}
								if (productObj.get("brand") != null) {
									List<BrandVo> brands = apiBrandMapper
											.quertBrandByNames(productObj.get("brand").toString());
									if (CollectionUtils.isEmpty(brands)) {  //判断品牌为空在品牌表里添加
										brand.setName(productObj.get("brand").toString());
										apiBrandMapper.save(brand);
										vo.setBrand_id(brand.getId());//把这个新商品设置本地库里的品牌id
									}
								}
								vo.setIs_delete(0);
								vo.setList_pic_url(productObj.get("thumbnailImage") == null ? ""
										: productObj.get("thumbnailImage").toString());
								vo.setCategory_id(Integer.parseInt(productObj.get("productCate").toString()));

								vo.setMarket_price(productObj.get("marketPrice") == null ? BigDecimal.ZERO
										: new BigDecimal(productObj.get("marketPrice").toString()));
								vo.setRetail_price(productObj.get("retailPrice") == null ? BigDecimal.ZERO
										: new BigDecimal(productObj.get("retailPrice").toString()));
								vo.setGoods_brief(productObj.get("features") == null ? ""
										: productObj.get("features").toString());
								vo.setIs_hot(0);
								vo.setGoods_desc(resultDate.get("PRODUCT_DESCRIPTION") == null ? null
										: getGoodsImg(resultDate.get("PRODUCT_DESCRIPTION").toString()));
								vo.setAdd_time(new Date());
								vo.setUpdate_time(new Date());
								vo.setSource(productObj.get("type") == null ? "" :productObj.get("type").toString());
								Map<String, Object> param = stock(productObj.get("productId").toString(), 100,
										DEFAULT_ADDRESS);
								if (param.get("num") != null) {
									vo.setGoods_number(Integer.parseInt(param.get("num").toString()));
								}
								apiGoodsMapper.save(vo);//保存商品信息
								saveProduct(vo);//保存产品信息
								if (resultDate.get("PRODUCT_IMAGE") != null) {
									JSONArray imgattr = JSONArray
											.parseArray(resultDate.get("PRODUCT_IMAGE").toString());
									if (!CollectionUtils.isEmpty(imgattr)) {
										apiGoodsGalleryMapper.deleteByGoodId(vo.getId());
										for (int m = 0; m < imgattr.size(); m++) {
											GoodsGalleryVo galleryVo = new GoodsGalleryVo();
											JSONObject imgObj = JSONObject.parseObject(imgattr.get(m).toString());
											galleryVo.setImg_url(imgObj.get("imageUrl").toString());
											galleryVo.setGoods_id(vo.getId());
											galleryVo.setSort_order(
													Integer.parseInt(imgObj.get("orderSort").toString()));
											apiGoodsGalleryMapper.save(galleryVo);
										}
									}
								}
								GoodsPureInterestRateVo goodsPureInterestRateVo = saveGoodsPureInterestRate(vo); // 保存商品毛利率
								if (null != goodsPureInterestRateVo) {
									goodsPureInterestRateVoList.add(goodsPureInterestRateVo);
								}
							} else {   //下面是本地库存在商品不为空的情况
								GoodsVo good = goods.get(0);
								if (productObj.get("status") != null) {
									if ("undercarriage".equals(productObj.get("status").toString())) { //第三方已下架
										Integer[] jdids = { good.getId() };
										apiGoodsService.unSaleBatch(jdids, 2);
									} else {
										good.setIs_on_sale(good.getIs_on_sale());
									}
								}
								good.setName(
										productObj.get("name") == null ? "" : productObj.get("name").toString());
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

								good.setMarket_price(productObj.get("marketPrice") == null ? BigDecimal.ZERO
										: new BigDecimal(productObj.get("marketPrice").toString()));
								good.setRetail_price(productObj.get("retailPrice") == null ? BigDecimal.ZERO
										: new BigDecimal(productObj.get("retailPrice").toString()));
								good.setGoods_brief(productObj.get("features") == null ? ""
										: productObj.get("features").toString());
								good.setIs_hot(good.getIs_hot());

								vo.setGoods_desc(resultDate.get("PRODUCT_DESCRIPTION") == null ? null
										: getGoodsImg(resultDate.get("PRODUCT_DESCRIPTION").toString()));
								good.setAdd_time(good.getAdd_time());
								good.setUpdate_time(new Date());
								good.setSource(productObj.get("type") == null ? "" :productObj.get("type").toString());
								Map<String, Object> param = stock(productObj.get("productId").toString(), 100,
										DEFAULT_ADDRESS);
								if (param.get("num") != null) {
									good.setGoods_number(Integer.parseInt(param.get("num").toString()));
								}
								apiGoodsMapper.update(good);
								updateProduct(good);
								if (resultDate.get("PRODUCT_IMAGE") != null) {
									JSONArray imgattr = JSONArray
											.parseArray(resultDate.get("PRODUCT_IMAGE").toString());
									if (!CollectionUtils.isEmpty(imgattr)) { //存在图片更新商品图片
										apiGoodsGalleryMapper.deleteByGoodId(good.getId());
										for (int m = 0; m < imgattr.size(); m++) {
											GoodsGalleryVo galleryVo = new GoodsGalleryVo();
											JSONObject imgObj = JSONObject.parseObject(imgattr.get(m).toString());
											galleryVo.setImg_url(imgObj.get("imageUrl").toString());
											galleryVo.setGoods_id(good.getId());
											galleryVo.setSort_order(
													Integer.parseInt(imgObj.get("orderSort").toString()));
											apiGoodsGalleryMapper.save(galleryVo);
										}
									}
								}
								updateGoodsPureInterestRate(good); // 更新毛利率

							}
						}
					} catch (Exception e) {
						logger.error("[商品入库/更新异常]产品id：" + productIds.get(index), e);
					}
					if (!CollectionUtils.isEmpty(goodsPureInterestRateVoList)) {
						// 执行批量保存商品毛利率
						apiGoodsPureInterestRateService.saveBatch(goodsPureInterestRateVoList);
					}
					logger.info("Thead"+Thread.currentThread().getName()+"执行完成保存京东商品第"+index+"个结束");
					System.out.println("================================第" + index+ "執行=============================================");
//					txManager.commit(txStatus);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			logger.info("Thead"+Thread.currentThread().getName()+"保存京东商品执行结束");
			countDownLatch.countDown();//当前线程执行完成门闩减一
		}
	}
}
