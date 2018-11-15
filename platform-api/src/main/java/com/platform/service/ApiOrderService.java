package com.platform.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.platform.cache.J2CacheUtils;
import com.platform.dao.ApiAddressMapper;
import com.platform.dao.ApiCartMapper;
import com.platform.dao.ApiOrderGoodsMapper;
import com.platform.dao.ApiOrderMapper;
import com.platform.dao.ApiTranInfoRecordMapper;
import com.platform.dao.ApiUserCouponMapper;
import com.platform.dao.QzUserAccountMapper;
import com.platform.entity.AddressVo;
import com.platform.entity.ApiTranInfoRecordVo;
import com.platform.entity.BuyGoodsVo;
import com.platform.entity.CartVo;
import com.platform.entity.GoodsVo;
import com.platform.entity.JdOrderVo;
import com.platform.entity.OrderGoodsVo;
import com.platform.entity.OrderVo;
import com.platform.entity.ProductVo;
import com.platform.entity.QzUserAccountVo;
import com.platform.entity.UserCouponVo;
import com.platform.entity.UserVo;
import com.platform.utils.GenerateCodeUtil;

@Service
public class ApiOrderService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ApiOrderMapper orderDao;
	@Autowired
	private ApiAddressMapper apiAddressMapper;
	@Autowired
	private ApiCartMapper apiCartMapper;
	@Autowired
	private ApiOrderMapper apiOrderMapper;
	@Autowired
	private ApiOrderGoodsMapper apiOrderGoodsMapper;
	@Autowired
	private ApiProductService productService;
	@Autowired
	private ApiUserCouponMapper apiUserCouponMapper;
	@Autowired
	private QzUserAccountMapper qzUserAccountMapper;
	@Autowired
	private ApiTranInfoRecordMapper apiTranInfoRecordMapper;

	@Autowired
	private JdOrderService jdOrderService;
	@Autowired
	private ApiGoodsService apiGoodsService;

	public OrderVo queryObject(Integer id) {
		return orderDao.queryObject(id);
	}

	public List<OrderVo> queryList(Map<String, Object> map) {
		return orderDao.queryList(map);
	}

	public int queryTotal(Map<String, Object> map) {
		return orderDao.queryTotal(map);
	}

	public void save(OrderVo order) {
		orderDao.save(order);
	}

	public int update(OrderVo order) {
		return orderDao.update(order);
	}

	public void delete(Integer id) {
		orderDao.delete(id);
	}

	public void deleteBatch(Integer[] ids) {
		orderDao.deleteBatch(ids);
	}

	@Transactional
	public Map<String, Object> submit(JSONObject jsonParam, UserVo loginUser) {
		// TODO
		Map<String, Object> resultObj = new HashMap<String, Object>();
		// 1 cart 购物车 2 buy 立即购买
		String type = jsonParam.getString("type");
		String postscript = jsonParam.getString("postscript");
		AddressVo addressVo = apiAddressMapper.queryObject(jsonParam.getInteger("addressId"));
		BigDecimal freightPrice = BigDecimal.ZERO;
		if (addressVo == null) {
			resultObj.put("errno", 400);
			resultObj.put("errmsg", "用户地址为空，不能下单!");
			return resultObj;
		}
		//用户的收货地址编码
		String address = addressVo.getProvince() + "_" + addressVo.getCity() + "_" + addressVo.getCounty();
		// * 获取要购买的商品
		List<CartVo> checkedGoodsList = new ArrayList<>();
		// 统计商品总价
		BigDecimal goodsTotalPrice = BigDecimal.ZERO;
		List<CartVo> orderGoodsList = new ArrayList<>();
		Map<String, String> soureMap = new HashMap<>();
		if (type.equals("cart")) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("user_id", loginUser.getUserId());
			param.put("session_id", 1);
			param.put("checked", 1);
			checkedGoodsList = apiCartMapper.queryList(param);

			if (null == checkedGoodsList) {
				resultObj.put("errno", 400);
				resultObj.put("errmsg", "请选择商品");
				return resultObj;
			}
			
			//下订单的集合
		
			for (CartVo cartItem : checkedGoodsList) {
				//校验自己的渠道产品的上下架状态+库存
				GoodsVo goods = apiGoodsService.queryObject(cartItem.getGoods_id());
				if (goods == null) {
					continue;
				}
				//判断是三方的还是自己的产品
				 String source = goods.getSource();
				if (source.equals("JD")) {
					  //检验库存+上下架状态
					  String pid =  goods.getGoods_sn().substring(2, goods.getGoods_sn().length());
					  //库存
					  Map<String, Object>  stockMap =   jdOrderService.checkStockSingle(pid, cartItem.getNumber(), address);
						if (!stockMap.get("code").equals("200")) {
							resultObj.put("errno", "100");
							resultObj.put("errmsg", "不可出售");
							continue;
						}
						//上下架状态
						 Map<String, Object>  saleStatusMap =   jdOrderService.checkSaleStatusSingle(Integer.parseInt(pid));
						if (!saleStatusMap.get("code").equals("200")) {
							resultObj.put("errno", "100");
							resultObj.put("errmsg", "不可出售");
							continue;
						}
				}
				if (source.equals("system")) {
					//校验自己的库存和上下架状态
					if (goods.getGoods_number()<cartItem.getNumber()) {
						logger.info("【系统商品库存不足无法正常下单】商品id:"+cartItem.getGoods_id()+"剩余库存："+goods.getGoods_number());
						continue;
					}
				    if (goods.getIs_on_sale()==0) {
				    	logger.info("【系统商品已经下架无法正常下单】商品上下架状态为："+goods.getIs_on_sale());
						continue;
					}
					
				}
				
				goodsTotalPrice = goodsTotalPrice.add(cartItem.getMarket_price().multiply(new BigDecimal(cartItem.getNumber())));
				orderGoodsList.add(cartItem);
				soureMap.put(cartItem.getGoods_id().toString(), source);
			}
		} 
		
		if (type.equals("buy")) {
			BuyGoodsVo goodsVo = (BuyGoodsVo) J2CacheUtils.get(J2CacheUtils.SHOP_CACHE_NAME,
					"goods" + loginUser.getUserId());
			if (goodsVo != null) {
				ProductVo productInfo = productService.queryObject(goodsVo.getProductId());
				// 计算订单的费用
				// 商品总价
				goodsTotalPrice = productInfo.getMarket_price().multiply(new BigDecimal(goodsVo.getNumber()));
				CartVo cartVo = new CartVo();
				BeanUtils.copyProperties(productInfo, cartVo);
				cartVo.setNumber(goodsVo.getNumber());
				cartVo.setProduct_id(goodsVo.getProductId());
				orderGoodsList.add(cartVo);
				soureMap.put(goodsVo.getGoodsId().toString(), "JD");
			}
		}
        if (CollectionUtils.isEmpty(orderGoodsList)) {
    		resultObj.put("errno", 1);
			resultObj.put("errmsg", "没可下单数据");
			return resultObj;
	}
		
		// 查询用户优惠券信息 直接过去 平台币抵扣券
		List<UserCouponVo> userCouponVos = apiUserCouponMapper.queryUserCouponTotalPrice(loginUser.getUserId());

		List<UserCouponVo> coupons = new ArrayList<>();
		if (!CollectionUtils.isEmpty(userCouponVos)) {
			for (UserCouponVo vo : userCouponVos) {
				if (vo.getCoupon_id() == 11) {
					coupons.add(vo);
				}
			}
		}
		// 判断数据是否异常 异常抛出

		if (CollectionUtils.isNotEmpty(coupons) && coupons.size() > 1) {
			resultObj.put("errno", 1);
			resultObj.put("errmsg", "抵扣券数据异常大于1");
			return resultObj;
		}

		UserCouponVo userCoupon = null;

		if (CollectionUtils.isNotEmpty(coupons)) {
			userCoupon = coupons.get(0);
		}

		// 订单的总价 商品价格+运费价格
		BigDecimal orderTotalPrice = goodsTotalPrice.add(freightPrice);

		OrderVo orderInfo = new OrderVo();
		orderInfo.setOrder_sn(GenerateCodeUtil.buildJDBizNo());
		orderInfo.setUser_id(loginUser.getUserId());
		// 收货地址和运费
		orderInfo.setAddress_id(jsonParam.getInteger("addressId"));
		orderInfo.setConsignee(addressVo.getUserName());
		orderInfo.setMobile(addressVo.getTelNumber());
		orderInfo.setCountry(addressVo.getNationalCode());
		orderInfo.setProvince(addressVo.getProvinceName());
		orderInfo.setCity(addressVo.getCityName());
		orderInfo.setDistrict(addressVo.getCountyName());
		orderInfo.setAddress(addressVo.getDetailInfo());
		orderInfo.setFreight_price(freightPrice);
		// 留言
		orderInfo.setPostscript(postscript);
		// 使用的优惠券
		BigDecimal couponPrice = BigDecimal.ZERO;
		if (userCoupon != null) {
			couponPrice = userCoupon.getCoupon_price();
			orderInfo.setCoupon_id(userCoupon.getId());
			orderInfo.setCoupon_price(couponPrice);
		}
		// 减去其它支付的金额后，要实际支付的金额
		BigDecimal actualPrice = orderTotalPrice.subtract(couponPrice);
		orderInfo.setAdd_time(new Date());
		orderInfo.setGoods_price(goodsTotalPrice);
		orderInfo.setOrder_price(orderTotalPrice);
		orderInfo.setActual_price(actualPrice);
		/*
		 * 0 订单创建成功等待付款， 101订单已取消， 102订单已删除 201订单已付款，等待发货 300订单已发货， 301用户确认收货
		 * 401 没有发货，退款 402 已收货，退款退货
		 */
		orderInfo.setOrder_status(0);
		orderInfo.setShipping_status(0);
		orderInfo.setPay_status(0);
		orderInfo.setShipping_id(0);
		orderInfo.setShipping_fee(new BigDecimal(0));
		orderInfo.setIntegral(0);
		orderInfo.setIntegral_money(new BigDecimal(0));
		if (type.equals("cart")) {
			orderInfo.setOrder_type("1");
		} else {
			orderInfo.setOrder_type("4");
		}

		// 开启事务，插入订单信息和订单商品
		apiOrderMapper.save(orderInfo);
		if (null == orderInfo.getId()) {
			resultObj.put("errno", 1);
			resultObj.put("errmsg", "订单提交失败");
			return resultObj;
		}

		/**
		 * 订单问题 1.拆分渠道 1.1 渠道增加 1.2 状态
		 *
		 * 2.定时任务轮训订单状态
		 *
		 *
		 */

		String pidNums = "";
		// 统计商品总价
		List<OrderGoodsVo> orderGoodsData = new ArrayList<OrderGoodsVo>();//目前没用
		for (CartVo goodsItem : orderGoodsList) {
			OrderGoodsVo orderGoodsVo = new OrderGoodsVo();
			orderGoodsVo.setOrder_id(orderInfo.getId());
			orderGoodsVo.setGoods_id(goodsItem.getGoods_id());
			orderGoodsVo.setGoods_sn(goodsItem.getGoods_sn());
			orderGoodsVo.setProduct_id(goodsItem.getProduct_id());
			orderGoodsVo.setGoods_name(goodsItem.getGoods_name());
			orderGoodsVo.setList_pic_url(goodsItem.getList_pic_url());
			orderGoodsVo.setMarket_price(goodsItem.getMarket_price());
			orderGoodsVo.setRetail_price(goodsItem.getRetail_price());
			orderGoodsVo.setNumber(goodsItem.getNumber());
			orderGoodsVo.setGoods_specifition_name_value(goodsItem.getGoods_specifition_name_value());
			orderGoodsVo.setGoods_specifition_ids(goodsItem.getGoods_specifition_ids());
			orderGoodsVo.setChannel(soureMap.get(goodsItem.getGoods_id()+""));
			orderGoodsVo.setGoodStatus(0);
			orderGoodsData.add(orderGoodsVo);
			apiOrderGoodsMapper.save(orderGoodsVo);
			pidNums += orderGoodsVo.getGoods_sn().substring(2, orderGoodsVo.getGoods_sn().length()) + "_"
					+ orderGoodsVo.getNumber() + ",";
		}
		if (StringUtils.isNotEmpty(pidNums)) {
			pidNums = pidNums.substring(0, pidNums.length() - 1);
		}

		// 清空已购买的商品
		apiCartMapper.deleteByCart(loginUser.getUserId(), 1, 1);
		resultObj.put("errno", 0);
		resultObj.put("errmsg", "订单提交成功");
		//
		Map<String, OrderVo> orderInfoMap = new HashMap<String, OrderVo>();
		orderInfoMap.put("orderInfo", orderInfo);
		//
		resultObj.put("data", orderInfoMap);
		// 优惠券标记已用
		if (userCoupon != null && userCoupon.getCoupon_status() == 1) {
			userCoupon.setCoupon_status(4);// 支付中
			apiUserCouponMapper.updateUserOrderCoupon(userCoupon);
		}
		// 创建第三方订单
		JdOrderVo jdOrderVo = new JdOrderVo();
		jdOrderVo.setPidNums(pidNums);
		Map<String, Object> result = jdOrderService.jdOrderSubbmit(addressVo, orderInfo, jdOrderVo);
		resultObj.put("errno", result.get("errno"));
		resultObj.put("errmsg", result.get("errmsg"));

		//
		return resultObj;
	}

	/***
	 * 购物车 时效性
	 *
	 * 1.支付成功 优惠券已经使用
	 *
	 * 2.超时未支付 优惠券失效 逻辑删除 失效操作： 回滚平台币 作废优惠券 查看是否需要清除购物车
	 */
	public Object checkOrderValid() {
		List<OrderVo> orderVos = apiOrderMapper.checkOrderValid();
		if (!CollectionUtils.isEmpty(orderVos)) {
			for (OrderVo order : orderVos) {
				UserCouponVo userCouponVo = apiUserCouponMapper.queryObject(order.getCoupon_id());
				BigDecimal amount = BigDecimal.ZERO;
				QzUserAccountVo userAmountVo = qzUserAccountMapper.queruUserAccountInfo(order.getUser_id());
				if (userAmountVo != null) {
					amount = userAmountVo.getAmount();
				}
				// 如果当前日期减掉订单创建时间大于一天则回滚平台币
				if (System.currentTimeMillis() - order.getAdd_time().getTime() > 24 * 60 * 60 * 1000) {
					logger.info("【定时查询订单有效性】订单标号:" + order.getId() + "" + "订单创建时间:" + order.getAdd_time());
					if (userCouponVo != null) {
						userCouponVo.setCoupon_status(3);// 作废
						apiUserCouponMapper.update(userCouponVo);
						saveTranInfoRecord(order.getUser_id(), "1", "2", userCouponVo.getCoupon_price(),
								userCouponVo.getCoupon_price(), "订单失效，原优惠券作废");
						amount = amount.add(userCouponVo.getCoupon_price());
					}
					if (userAmountVo != null && userCouponVo != null) {
						userAmountVo.setAmount(amount);
						qzUserAccountMapper.updateUserAccount(userAmountVo);
						saveTranInfoRecord(order.getUser_id(), "2", "1", userCouponVo.getCoupon_price(),
								userAmountVo.getAmount(), "订单失效，原优惠券金额回滚平台币中");
					}
					order.setOrder_status(103);// 订单失效
					apiOrderMapper.update(order);
				}
			}
			return this.toResponsObject(0, "执行成功", "");
		}
		if (CollectionUtils.isEmpty(orderVos)) {
			return this.toResponsObject(0, "没有失效订单", "");
		}
		return null;
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
}
