package com.platform.youle.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.platform.dao.ApiCartMapper;
import com.platform.dao.ApiGoodsMapper;
import com.platform.dao.ApiJdCartPriceMapper;
import com.platform.dao.ApiProductMapper;
import com.platform.dao.ApiTranInfoRecordMapper;
import com.platform.dao.ApiUserCouponMapper;
import com.platform.dao.GoodsCouponConfigMapper;
import com.platform.dao.QzUserAccountMapper;
import com.platform.entity.ApiTranInfoRecordVo;
import com.platform.entity.CartVo;
import com.platform.entity.GoodsCouponConfigVo;
import com.platform.entity.GoodsPureInterestRateVo;
import com.platform.entity.GoodsVo;
import com.platform.entity.JdCartPriceVo;
import com.platform.entity.ProductVo;
import com.platform.entity.QzUserAccountVo;
import com.platform.entity.UserCouponVo;
import com.platform.service.ApiGoodsPureInterestRateService;
import com.platform.util.PayMatchingUtil;
import com.platform.youle.constant.Constants.Urls;
import com.platform.youle.entity.RequestBaseEntity;
import com.platform.youle.entity.RequestSkuDetailEntity;
import com.platform.youle.service.AbsApiCartPriceService;
import com.platform.youle.util.HttpUtil;
import com.platform.youle.util.TokenUtil;

@Service
public class AbsApiCartPriceServiceImpl implements AbsApiCartPriceService {

	@Autowired
	private ApiCartMapper apiCartMapper;
	@Autowired
	private ApiGoodsMapper apiGoodsMapper;
	@Autowired
	private ApiProductMapper apiProductMapper;
	@Autowired
	private ApiJdCartPriceMapper apiJdCartPriceMapper;
	@Autowired
	private ApiUserCouponMapper apiUserCouponMapper;
	@Autowired
	private QzUserAccountMapper qzUserAccountMapper;
	@Autowired
	private GoodsCouponConfigMapper goodsCouponConfigMapper;
	@Autowired
	private PayMatchingUtil payMatchingUtils;
	@Autowired
	private ApiTranInfoRecordMapper apiTranInfoRecordMapper;
	@Autowired
	private ApiGoodsPureInterestRateService goodsPureInterestRateService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public JSONObject quertAllJDCartPrices() {
		JSONObject resultObj = new JSONObject();
		String result = "";
		List<CartVo> carts = apiCartMapper.quertAllCarts();
		if (CollectionUtils.isEmpty(carts)) {
			resultObj.put("state", "fail");
			resultObj.put("msg", "购物车无商品");
			return resultObj;
		}
		for (CartVo cart : carts) {
			Long userId = cart.getUser_id();
			Integer goodsId = cart.getGoods_id();
			String goodsn = cart.getGoods_sn();
			String jdProductId = goodsn.substring(2, goodsn.length());
			RequestSkuDetailEntity entity = new RequestSkuDetailEntity();
			initRequestParam(entity);
			entity.setPid(Long.parseLong(jdProductId));
			try {
				result = HttpUtil.post(Urls.base_test_url + Urls.detial, objectToMap(entity));
				if (StringUtils.isEmpty(result)) {
					resultObj.put("status", "false");
					resultObj.put("msg", "[1.3获取单个商品详情]为空");
					logger.info("[1.3获取单个商品详情]为空,productId:" + Long.parseLong(jdProductId));
					continue;
				}
				JSONObject dateObj = JSONObject.parseObject(result);
				String resObj = dateObj.get("RESULT_DATA").toString();
				if (StringUtils.isEmpty(resObj)) {
					resultObj.put("status", "false");
					resultObj.put("msg", "[1.3获取单个商品详情]为空");
					logger.info("[定时查询购物车商品]为空,productId:" + Long.parseLong(jdProductId));
					continue;
				}
				JSONObject resultDate = JSONObject.parseObject(resObj);
				String productDate = resultDate.get("PRODUCT_DATA").toString();
				if (!StringUtils.isEmpty(productDate)) {
					JSONObject productObj = JSONObject.parseObject(productDate);
					if (productObj.get("marketPrice") == null) {
						resultObj.put("status", "false");
						resultObj.put("msg", "三方市场价为空");
						logger.info("[定时查询购物车商品]为空,productId:" + Long.parseLong(jdProductId));
						continue;
					}
					if (cart.getMarket_price().compareTo(new BigDecimal(productObj.get("marketPrice").toString())) != 0) {
						GoodsVo good = apiGoodsMapper.queryObject(goodsId);
						Map<String, Object> paramMap = new HashMap<>();
						if (good != null) {
							good.setMarket_price(new BigDecimal(productObj.get("marketPrice").toString()));
							good.setUpdate_time(new Date());
							apiGoodsMapper.update(good);
							paramMap.put("goods_id", goodsId);
							//更新product表
							List<ProductVo> productList = apiProductMapper.queryList(paramMap);
							if (!CollectionUtils.isEmpty(productList)) {
								for (ProductVo product : productList) {
									product.setMarket_price(new BigDecimal(productObj.get("marketPrice").toString()));
									apiProductMapper.update(product);
								}
							}
							//更新毛利率表
							 List<GoodsPureInterestRateVo> rateList = goodsPureInterestRateService.queryAll((HashMap)paramMap.put("goodsId", goodsId));
							if(!CollectionUtils.isEmpty(rateList)){
								for(GoodsPureInterestRateVo goodsPureInterestRateVo : rateList){
									goodsPureInterestRateVo.setMarketPrice(new BigDecimal(productObj.get("marketPrice").toString()));
									//重新计算毛利率
									// 计算毛利息
									double PureInterestRate = goodsPureInterestRateVo.getMarketPrice().subtract(goodsPureInterestRateVo.getRetailPrice())
											.divide(goodsPureInterestRateVo.getRetailPrice(), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
									goodsPureInterestRateVo.setPureInterestRate(PureInterestRate);
									goodsPureInterestRateService.update(goodsPureInterestRateVo);
								}
							}
						}
						Map<String,Object> param = new HashMap<>();
						param.put("goodsn",goodsn);
						param.put("marketPrice",new BigDecimal(productObj.get("marketPrice").toString()));
						param.put("rawMarketPrice",cart.getMarket_price());
						List<JdCartPriceVo> cartPrices = apiJdCartPriceMapper.queryCartPrices(param);
						if(CollectionUtils.isEmpty(cartPrices)){
							JdCartPriceVo cartPrice = new JdCartPriceVo(); 
							cartPrice.setGoods_sn(goodsn);
							cartPrice.setUser_id(userId);
							cartPrice.setRaw_market_price(cart.getMarket_price());
							cartPrice.setRaw_retail_price(cart.getRetail_price());
							cartPrice.setMarket_price(new BigDecimal(productObj.get("marketPrice").toString()));
							cartPrice.setRetail_price(productObj.get("retailPrice") == null ? BigDecimal.ZERO
									: new BigDecimal(productObj.get("retailPrice").toString()));
							cartPrice.setCreate_time(new Date());
							apiJdCartPriceMapper.save(cartPrice);
							updateUserCouponPrice(goodsId, cart.getNumber(), userId);
						}
						cart.setMarket_price(new BigDecimal(productObj.get("marketPrice").toString()));
						apiCartMapper.update(cart);
					}
				}
			} catch (Exception e) {
				logger.error("[定时查询购物车商品]异常", e);
			}
		}
		return null;
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

	public synchronized Object updateUserCouponPrice(Integer goodsId, Integer number, Long userId) {
		BigDecimal couponTotalPrice = BigDecimal.ZERO;// 优惠券总价值

		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		BigDecimal amount = BigDecimal.ZERO;// 初始化用户平台币
		logger.info("【更新用户优惠券开始】,用户id" + userId);
		QzUserAccountVo userAmountVo = qzUserAccountMapper.queruUserAccountInfo(userId);// 查询用户平台币信息

		if (userAmountVo != null) {
			List<UserCouponVo> userCouponVos = apiUserCouponMapper.queryUserCouponTotalPrice(userId);// 查询用户优惠券信息
			List<UserCouponVo> coupons = new ArrayList<>();
			List<CartVo> carts = apiCartMapper.queryUserCarts(userId);
			if (!CollectionUtils.isEmpty(userCouponVos)) {
				for (int i = 0; i < userCouponVos.size(); i++) {
					if (userCouponVos.get(i).getCoupon_id() == 11) {
						coupons.add(userCouponVos.get(i));
					}
				}
			}
			UserCouponVo userCouponVo = null;
			if (!CollectionUtils.isEmpty(coupons)) {
				userCouponVo = coupons.get(0);
			}
			if (userCouponVo != null && userAmountVo != null) {
				// 购物车发生修改 原有优惠券作废，重新生成优惠券
				userCouponVo.setCoupon_status(7);
				logger.info("【更新用户优惠券】原有优惠券作废，原有优惠券金额" + userCouponVo.getCoupon_price() + "原用户平台币"
						+ userAmountVo.getAmount());
				apiUserCouponMapper.update(userCouponVo);

				saveTranInfoRecord(userId, "1", "2", userCouponVo.getCoupon_price(), userCouponVo.getCoupon_price(),
						"购物车发生修改  原有优惠券作废");
				// 回滚平台币
				userAmountVo.setAmount(userAmountVo.getAmount().add(userCouponVo.getCoupon_price()));
				qzUserAccountMapper.updateUserAccount(userAmountVo);

				logger.info("更新用户平台币,更新后平台币金额" + userAmountVo.getAmount());
				saveTranInfoRecord(userId, "2", "1", userCouponVo.getCoupon_price(), userAmountVo.getAmount(),
						"原有优惠券作废,原优惠券金额回滚到平台币");
			}
			if (!CollectionUtils.isEmpty(carts)) {
				for (CartVo cart : carts) {
					if (null != cart.getChecked() && 1 == cart.getChecked()) {
						// 获取产品配比值
						GoodsCouponConfigVo goodsCoupon = goodsCouponConfigMapper.getUserCoupons(cart.getGoods_id(),
								userId);
						BigDecimal couponlPrice = BigDecimal.ZERO;// 优惠券临时总价值
						// 计算该产品优惠券总和
						if (goodsCoupon != null) {
							BigDecimal payMatching = BigDecimal.ZERO;
							if (payMatchingUtils.getPayMatching(cart.getProduct_id()) != null) {
								Object value = payMatchingUtils.getPayMatching(cart.getProduct_id())
										.get(cart.getGoods_id());
								if (value != null) {
									payMatching = new BigDecimal(value.toString());
								}
							}
							couponlPrice = payMatching.multiply(new BigDecimal(cart.getNumber()));
						}
						couponTotalPrice = couponTotalPrice.add(couponlPrice);
					}
				}
			}
			if (userAmountVo != null) {
				amount = userAmountVo.getAmount();
				if (amount.compareTo(couponTotalPrice) < 0) {
					couponTotalPrice = amount;
				}
				userAmountVo.setAmount(userAmountVo.getAmount().subtract(couponTotalPrice));
				qzUserAccountMapper.updateUserAccount(userAmountVo);
				logger.info("更新用户平台币,更新后平台币金额" + userAmountVo.getAmount());
				saveTranInfoRecord(userId, "2", "2", couponTotalPrice, userAmountVo.getAmount(), "回滚平台币后扣减购物车中生成优惠券金额");
			}
			getUserCouponTotalPrice(userId, couponTotalPrice);
		}
		return this.toResponsObject(0, "执行成功", "");
	}

	/**
	 * 生成平台币、优惠券流水
	 * 
	 * @param userId
	 * @param tranType
	 * @param TranFlag
	 * @param tranAmount
	 * @param currentAmount
	 * @param remark
	 */
	public void saveTranInfoRecord(Long userId, String tranType, String TranFlag, BigDecimal tranAmount,
			BigDecimal currentAmount, String remark) {
		ApiTranInfoRecordVo tranInfo = new ApiTranInfoRecordVo();
		tranInfo.setUser_id(userId);
		tranInfo.setTran_type(tranType);// 1优惠券 2 平台币
		tranInfo.setTran_flag(TranFlag);// 1收入 2支出
		tranInfo.setTran_amount(tranAmount);
		tranInfo.setCurrent_amount(currentAmount);
		tranInfo.setCreate_time(new Date());
		tranInfo.setRemark(remark);
		apiTranInfoRecordMapper.save(tranInfo);
	}

	public Object getUserCouponTotalPrice(Long userId, BigDecimal couponTotalPrice) {
		List<UserCouponVo> userCouponVos = apiUserCouponMapper.queryUserCouponTotalPrice(userId);// 查询用户优惠券信息
		List<UserCouponVo> coupons = new ArrayList<>();
		if (!CollectionUtils.isEmpty(userCouponVos)) {
			for (int i = 0; i < userCouponVos.size(); i++) {
				if (userCouponVos.get(i).getCoupon_id() == 11) {
					coupons.add(userCouponVos.get(i));
				}
			}
		}
		UserCouponVo userCouponVo = null;
		if (!CollectionUtils.isEmpty(coupons)) {
			userCouponVo = coupons.get(0);
		}
		if (userCouponVo == null) {
			// 获取订单使用的优惠券
			UserCouponVo userCoupon = new UserCouponVo();
			userCoupon.setCoupon_id(11);
			userCoupon.setCoupon_number("1");
			userCoupon.setUser_id(userId);
			userCoupon.setCoupon_status(1);
			userCoupon.setCoupon_price(couponTotalPrice);
			userCoupon.setAdd_time(new Date());
			apiUserCouponMapper.save(userCoupon);
			if (null == userCoupon.getId()) {
				return this.toResponsObject(400, "优惠券生成失败", "");
			}
		}
		if (userCouponVo != null) {
			userCouponVo.setCoupon_price(userCouponVo.getCoupon_price().add(couponTotalPrice));
			userCouponVo.setCoupon_status(1);
			apiUserCouponMapper.update(userCouponVo);

			saveTranInfoRecord(userId, "1", "1", couponTotalPrice, userCouponVo.getCoupon_price(), "原有优惠券作废,重新更新新优惠券");
		}
		return this.toResponsObject(0, "优惠券发送成功", "");
	}

	/**
	 * @param requestCode
	 * @param msg
	 * @param data
	 * @return Map<String,Object>
	 * @throws @Description:构建统一格式返回对象
	 * @date 2016年9月2日
	 * @author zhuliyun
	 */
	public Map<String, Object> toResponsObject(int requestCode, String msg, Object data) {
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("errno", requestCode);
		obj.put("errmsg", msg);
		if (data != null)
			obj.put("data", data);
		return obj;
	}
}
