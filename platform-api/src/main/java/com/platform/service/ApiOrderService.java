package com.platform.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.platform.dao.ApiMoneyRecordMapper;
import com.platform.dao.ApiOrderGoodsMapper;
import com.platform.dao.ApiOrderLogMapper;
import com.platform.dao.ApiOrderMapper;
import com.platform.dao.ApiUserCouponMapper;
import com.platform.dao.GoodsCouponConfigMapper;
import com.platform.dao.QzUserAccountMapper;
import com.platform.entity.AddressVo;
import com.platform.entity.BuyGoodsVo;
import com.platform.entity.CartVo;
import com.platform.entity.GoodsCouponConfigVo;
import com.platform.entity.GoodsVo;
import com.platform.entity.JdOrderVo;
import com.platform.entity.OrderGoodsVo;
import com.platform.entity.OrderLogVo;
import com.platform.entity.OrderVo;
import com.platform.entity.ProductVo;
import com.platform.entity.QzMoneyRecordVo;
import com.platform.entity.QzUserAccountVo;
import com.platform.entity.UserCouponVo;
import com.platform.entity.UserVo;
import com.platform.util.PayMatchingUtil;
import com.platform.utils.GenerateCodeUtil;
import com.platform.utils.RequestUtil;
import com.platform.yeepay.service.YeepayOrderBizService;

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
	private QzUserAccountMapper qzUserAccountMapper;
	@Autowired
	private JdOrderService jdOrderService;
	@Autowired
	private ApiGoodsService apiGoodsService;
    @Autowired
    private PayMatchingUtil payMatchingUtils;
    @Autowired
    private GoodsCouponConfigMapper goodsCouponConfigMapper;
    @Autowired
    private ApiUserCouponMapper apiUserCouponMapper;
    @Autowired
    private ApiMoneyRecordMapper apiMoneyRecordMapper;
    @Autowired
    private ApiOrderLogMapper apiOrderLogMapper;
    
   @Autowired
   private YeepayOrderBizService yeepayOrderBizService;
    
	public OrderVo queryObjectByTradeNo(String tradeNo) {
		OrderVo vo = null;
		Map<String, Object> map = new HashMap<>();
		   map.put("shippingNo", tradeNo);
	       List<OrderVo> list =  this.queryList(map);
	       if (!CollectionUtils.isEmpty(list)) {
	    	    vo = list.get(0);
		  }
		
		return  vo;
	}
   
	public OrderVo queryObject(Integer id) {
		return orderDao.queryObject(id);
	}

	public List<OrderVo> queryList(Map<String, Object> map) {
		return orderDao.queryList(map);
	}

	public int queryTotal(Map<String, Object> map) {
		return orderDao.queryTotal(map);
	}
	/**
	 *  查询待付款/代发货/待收货订单总数量
	 * @param map
	 * @return
	 */
	public int queryOrderTotal(Map<String, Object> map) {
		return orderDao.queryOrderTotal(map);
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

	
	
	/**
	 * 平台币金额小于总优惠金额，优惠金额最大化
	 * @param carts
	 * @param userAccount
	 * @return
	 */
	public BigDecimal goodsMaxDiscount(List<CartVo> carts,BigDecimal userAccount,Integer orderId){
		BigDecimal maxDiscount = BigDecimal.ZERO;
		List<BigDecimal> amounts = new ArrayList<>();
		List<Map<String,Object>> param = new ArrayList<>();
		if(CollectionUtils.isEmpty(carts)){
			return maxDiscount;
		}
		for(CartVo cart : carts){
			//获取产品配比值
			GoodsCouponConfigVo goodsCoupon = goodsCouponConfigMapper.getUserCoupons(cart.getGoods_id(),cart.getUser_id());
			//计算该产品优惠券总和
			if(goodsCoupon != null){
				BigDecimal payMatching = BigDecimal.ZERO;
    			if(payMatchingUtils.getPayMatching(cart.getProduct_id())!= null){
    				Object value = payMatchingUtils.getPayMatching(cart.getProduct_id()).get(cart.getGoods_id());
    				if(value != null){
    					payMatching = new BigDecimal(value.toString());
    				}
    			}
    			for(int j = 0;j<cart.getNumber();j++){
    				Map<String,Object> map = new HashMap<>();
    				map.put(payMatching.toString(), cart.getGoods_id());
    				param.add(map);
    				amounts.add(payMatching);
    			}
			}
		}
		Collections.sort(amounts);
		for(int i = amounts.size()-1;i>= 0;i--){
			userAccount = userAccount.subtract(amounts.get(i));
			if(userAccount.compareTo(BigDecimal.ZERO) > 0){
				for(Map<String,Object> map : param){
					String keyValue = "";
					for(String key1 : map.keySet()){
						keyValue = key1;
					}
					if(amounts.get(i).compareTo(new BigDecimal(keyValue)) == 0){
						UserCouponVo coupon = new UserCouponVo();
						coupon.setOrder_id(orderId);
						coupon.setCoupon_number("1");
						coupon.setCoupon_status(1);//未用
						coupon.setAdd_time(new Date());
						coupon.setGoods_id(Long.parseLong(map.get(keyValue).toString()));
						coupon.setCoupon_id(11);
						coupon.setUser_id(carts.get(0).getUser_id());
						coupon.setCoupon_price(amounts.get(i));
						apiUserCouponMapper.save(coupon);
						param.remove(map);
						break;
					}
				}
				maxDiscount = maxDiscount.add(amounts.get(i));
				continue;
			}
			if(userAccount.compareTo(BigDecimal.ZERO) == 0){
				
				for(Map<String,Object> map : param){
					String keyValue = "";
					for(String key1 : map.keySet()){
						keyValue = key1;
					}
					if(amounts.get(i).compareTo(new BigDecimal(keyValue)) == 0){
						UserCouponVo coupon = new UserCouponVo();
						coupon.setOrder_id(orderId);
						coupon.setCoupon_number("1");
						coupon.setCoupon_status(1);//未用
						coupon.setAdd_time(new Date());
						coupon.setGoods_id(Long.parseLong(map.get(keyValue).toString()));
						coupon.setCoupon_id(11);
						coupon.setUser_id(carts.get(0).getUser_id());
						coupon.setCoupon_price(amounts.get(i));
						apiUserCouponMapper.save(coupon);
						param.remove(map);
						break;
					}
				}
				maxDiscount = maxDiscount.add(amounts.get(i));
				break;
			}
			if(userAccount.compareTo(BigDecimal.ZERO) < 0){
				userAccount = userAccount.add(amounts.get(i));
				continue;
			}
		}
		return maxDiscount;
	}
	
	@Transactional
	public Map<String, Object> submit(HttpServletRequest request,JSONObject jsonParam, UserVo loginUser) {
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
		// 用户的收货地址编码
		String address = addressVo.getProvince() + "_" + addressVo.getCity() + "_" + addressVo.getCounty();
		// * 获取要购买的商品
		List<CartVo> checkedGoodsList = new ArrayList<>();
		// 统计商品总价
		BigDecimal goodsTotalPrice = BigDecimal.ZERO;
		List<CartVo> orderGoodsList = new ArrayList<>();
		//实际优惠券扣减价格
		BigDecimal couponAmount = BigDecimal.ZERO;
		// 订单商品优惠券价格
		BigDecimal discountAmount = BigDecimal.ZERO;
		Map<String, String> soureMap = new HashMap<>();
		if (type.equals("cart")) {
			logger.info("[加入购物车]开始创建订单");
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

			// 下订单的集合

			for (CartVo cartItem : checkedGoodsList) {
				// 校验自己的渠道产品的上下架状态+库存
				GoodsVo goods = apiGoodsService.queryObject(cartItem.getGoods_id());
				if (goods == null) {
					continue;
				}
				// 判断是三方的还是自己的产品
				String source = goods.getSource();
				if (source.equals("JD") || source.equals("jindong")) {
					logger.info("[三方产品],开始校验三方上下架及库存状态");
					// 检验库存+上下架状态
					String pid = goods.getGoods_sn().substring(2, goods.getGoods_sn().length());
					// 库存
					Map<String, Object> stockMap = jdOrderService.checkStockSingle(pid, cartItem.getNumber(), address);
					if (!stockMap.get("code").equals("200")) {
						logger.info("[三方产品],三方产品已下架");
						resultObj.put("errno", "100");
						resultObj.put("errmsg", "不可出售");
						Integer[] arr1 = { cartItem.getGoods_id() };
						apiGoodsService.unSaleBatch(arr1, 3);
						continue;
					}
//					 上下架状态
					Map<String, Object> saleStatusMap = jdOrderService.checkSaleStatusSingle(Integer.parseInt(pid));
					if (!saleStatusMap.get("code").equals("200")) {
						logger.info("[三方产品],三方产品无库存");
						resultObj.put("errno", "100");
						resultObj.put("errmsg", "不可出售");
						Integer[] arr1 = { cartItem.getGoods_id() };
						apiGoodsService.unSaleBatch(arr1, 3);
						continue;
					}

				}
				if (source.equals("system")) {
					logger.info("[本地产品],校验本地库存");
					// 校验自己的库存和上下架状态
					if (goods.getGoods_number() < cartItem.getNumber()) {
						logger.info(
								"【系统商品库存不足无法正常下单】商品id:" + cartItem.getGoods_id() + "剩余库存：" + goods.getGoods_number());
						continue;
					}
					if (goods.getIs_on_sale() == 0) {
						logger.info("【系统商品已经下架无法正常下单】商品上下架状态为：" + goods.getIs_on_sale());
						continue;
					}

				}

				goodsTotalPrice = goodsTotalPrice
						.add(cartItem.getMarket_price().multiply(new BigDecimal(cartItem.getNumber())));
				orderGoodsList.add(cartItem);
				soureMap.put(cartItem.getGoods_id().toString(), source);
			}
		}
		if (type.equals("buy")) {
			logger.info("[立即购买]开始创建订单");
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
		
		// 订单的总价 商品价格+运费价格
		BigDecimal orderTotalPrice = goodsTotalPrice.add(freightPrice);
		BigDecimal ShippingFee = BigDecimal.ZERO; //计算快递费用

		
		OrderVo orderInfo = new OrderVo();
		orderInfo.setShipping_no(GenerateCodeUtil.buildBizNo());
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

		// 减去其它支付的金额后，要实际支付的金额
		BigDecimal actualPrice = orderTotalPrice.subtract(couponAmount);
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
		orderInfo.setShipping_fee(new BigDecimal(0));  //计算快递费用
		if (orderTotalPrice.compareTo(new BigDecimal("49")) <=0) { //(0-49]  8元运费
			orderInfo.setShipping_fee(new BigDecimal("8"));
		}else if (orderTotalPrice.compareTo(new BigDecimal("99")) <=0) { //(49-99]  6元运费
			orderInfo.setShipping_fee(new BigDecimal("6"));
		}
		
		orderInfo.setIntegral(0);
		orderInfo.setIntegral_money(new BigDecimal(0));
		if (type.equals("cart")) {
			orderInfo.setOrder_type("0");
		} else {
			orderInfo.setOrder_type("0");
		}
		orderInfo.setCoupon_price(couponAmount);

		
		// 开启事务，插入订单信息和订单商品
		apiOrderMapper.save(orderInfo);
		//查询可以抵扣金额
		discountAmount = queryUserDisCountAmount(orderGoodsList,orderInfo);
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
		 */

		String pidNums = "";
		// 统计商品总价
		List<OrderGoodsVo> orderGoodsData = new ArrayList<OrderGoodsVo>();// 目前没用
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
			orderGoodsVo.setChannel(soureMap.get(goodsItem.getGoods_id() + ""));
			orderGoodsVo.setGoodStatus(0);
			orderGoodsData.add(orderGoodsVo);
			apiOrderGoodsMapper.save(orderGoodsVo);
			pidNums += orderGoodsVo.getGoods_sn().substring(2, orderGoodsVo.getGoods_sn().length()) + "_"
					+ orderGoodsVo.getNumber() + ",";
		}
		if (StringUtils.isNotEmpty(pidNums)) {
			pidNums = pidNums.substring(0, pidNums.length() - 1);
		}
		OrderVo order = apiOrderMapper.queryObject(orderInfo.getId());
		order.setActual_price(orderTotalPrice.subtract(couponAmount));
		order.setCoupon_price(discountAmount);
		order.setPid_num(pidNums);
		
		BigDecimal actual_price =  order.getActual_price().subtract(discountAmount);
		actual_price = actual_price.add(orderInfo.getShipping_fee());
		order.setActual_price(actual_price);
		apiOrderMapper.update(order);
		// 清空已购买的商品
		apiCartMapper.deleteByCart(loginUser.getUserId(), 1, 1);
		resultObj.put("errno", 0);
		resultObj.put("errmsg", "订单提交成功");
		//
		Map<String, OrderVo> orderInfoMap = new HashMap<String, OrderVo>();
		orderInfoMap.put("orderInfo", orderInfo);
		//
		resultObj.put("data", orderInfoMap);
		//创建易宝支付订单
		String validIP = RequestUtil.getIpAddrByRequest(request);
		Map<String, Object> yeepayMap = yeepayOrderBizService.yeepayOrderSubmmit(order,validIP);
		if (yeepayMap != null) {
			resultObj.put("payurl", yeepayMap.get("payurl"));
		}
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
				// 如果当前日期减掉订单创建时间大于一天则回滚平台币
				if (System.currentTimeMillis() - order.getAdd_time().getTime() > 24 * 60 * 60 * 1000) {
					logger.info("【定时查询订单有效性】订单标号:" + order.getId() + "" + "订单创建时间:" + order.getAdd_time());
					order.setOrder_status(103);// 订单失效
					apiOrderMapper.update(order);
					rollbackDiscount(order,1);
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
	 * 查询购物车所选商品总优惠价格
	 * @param carts
	 * @return
	 */
	public BigDecimal queryUserDisCountAmount(List<CartVo> carts,OrderVo order) {
		BigDecimal disCountAmount = BigDecimal.ZERO;
		BigDecimal couponAmount = BigDecimal.ZERO;
		// 用户平台币账户
		QzUserAccountVo account = qzUserAccountMapper.queruUserAccountInfo(carts.get(0).getUser_id());
		if (account != null) {
			if (CollectionUtils.isEmpty(carts)) {
				return disCountAmount;
			}
			for (CartVo cart : carts) {
				// 获取产品配比值
				GoodsCouponConfigVo goodsCoupon = goodsCouponConfigMapper.getUserCoupons(cart.getGoods_id(),
						cart.getUser_id());
				BigDecimal couponlPrice = BigDecimal.ZERO;// 优惠券临时总价值
				// 计算该产品优惠券总和
				if (goodsCoupon != null) {
					BigDecimal payMatching = BigDecimal.ZERO;
					if (payMatchingUtils.getPayMatching(cart.getProduct_id()) != null) {
						Object value = payMatchingUtils.getPayMatching(cart.getProduct_id()).get(cart.getGoods_id());
						if (value != null) {
							payMatching = new BigDecimal(value.toString());
						}
					}
					couponlPrice = payMatching.multiply(new BigDecimal(cart.getNumber()));
				}
				disCountAmount = disCountAmount.add(couponlPrice);
			}

			// 用户余额 
			BigDecimal userAccount = account.getAmount();
			// 平台币余额>=优惠金额
			if (userAccount.compareTo(disCountAmount) == 0 || userAccount.compareTo(disCountAmount) > 0) {
				couponAmount = disCountAmount;
				account.setAmount(userAccount.subtract(couponAmount));
				if(account.getLock_amount() == null){
					account.setLock_amount(couponAmount);// 冻结金额
				}else{
					account.setLock_amount(account.getLock_amount().add(couponAmount));// 冻结金额
				}
				for(CartVo cart : carts){
					// 获取产品配比值
					GoodsCouponConfigVo goodsCoupon = goodsCouponConfigMapper.getUserCoupons(cart.getGoods_id(),
							cart.getUser_id());
					UserCouponVo coupon = new UserCouponVo();
					if (goodsCoupon != null) {
						BigDecimal payMatching = BigDecimal.ZERO;
						if (payMatchingUtils.getPayMatching(cart.getProduct_id()) != null) {
							Object value = payMatchingUtils.getPayMatching(cart.getProduct_id()).get(cart.getGoods_id());
							if (value != null) {
								payMatching = new BigDecimal(value.toString());
							}
						}
						coupon.setCoupon_price(payMatching);
						coupon.setOrder_id(order.getId());
						coupon.setCoupon_number("1");
						coupon.setCoupon_status(1);//未用
						coupon.setAdd_time(new Date());
						coupon.setGoods_id(Long.parseLong(cart.getGoods_id().toString()));
						coupon.setCoupon_id(11);
						coupon.setUser_id(cart.getUser_id());
						apiUserCouponMapper.save(coupon);
					}
				}
				
			}
			// 平台币余额<优惠金额
			if (userAccount.compareTo(disCountAmount) < 0) {
				couponAmount = goodsMaxDiscount(carts, userAccount,order.getId());
				account.setAmount(userAccount.subtract(couponAmount));
				if(account.getLock_amount() == null){
					account.setLock_amount(couponAmount);// 冻结金额
				}else{
					account.setLock_amount(account.getLock_amount().add(couponAmount));// 冻结金额
				}
			}
			account.setLast_update_time(new Date());
			qzUserAccountMapper.update(account);
			saveMoneyRecord(carts.get(0).getUser_id(),"3", 0, order,"创建订单，生成商品优惠冻结金额",couponAmount);
		}
		return couponAmount;
	}

	
	/**
	 * 订单失效/取消订单，子商品优惠冻结金额返回到用户平台币中
	 * @param order
	 * @return
	 */
	@Transactional
	public Object rollbackDiscount(OrderVo order,Integer type){
		QzUserAccountVo userAmountVo = qzUserAccountMapper.queruUserAccountInfo(order.getUser_id());
		if(userAmountVo != null){
			userAmountVo.setAmount(userAmountVo.getAmount().add(order.getCoupon_price()));
			if(userAmountVo.getLock_amount() == null){
				userAmountVo.setLock_amount(new BigDecimal("0").subtract(order.getCoupon_price()));
			}else{
				userAmountVo.setLock_amount(userAmountVo.getLock_amount().subtract(order.getCoupon_price()));
			}
			userAmountVo.setLast_update_time(new Date());
			qzUserAccountMapper.update(userAmountVo);
			if(type == 1){
				saveMoneyRecord(order.getUser_id(), "3",1, order, "订单失效，子商品优惠冻结金额返回到用户平台币中", order.getCoupon_price());
			}
			if(type == 2){
				saveMoneyRecord(order.getUser_id(), "3",1, order, "取消订单，子商品优惠冻结金额返回到用户平台币中", order.getCoupon_price());
			}
			List<UserCouponVo> coupons = apiUserCouponMapper.queryCouponsBuOrderId(order.getId());
			if(!CollectionUtils.isEmpty(coupons)){
				for(UserCouponVo userCoupon : coupons){
					userCoupon.setCoupon_status(3);//订单作废，子优惠券作废
					apiUserCouponMapper.update(userCoupon);
				}
			}
		}
		return this.toResponsObject(0, "执行成功", "");
	}
	/**
	 * 支付成功，扣减平台币
	 * @param order
	 * @return
	 */
	public Object discountUserAmount(OrderVo order){
		QzUserAccountVo userAmountVo = qzUserAccountMapper.queruUserAccountInfo(order.getUser_id());
		if(userAmountVo != null){
			if(userAmountVo.getLock_amount() == null){
				userAmountVo.setLock_amount(new BigDecimal("0").subtract(order.getCoupon_price()));
			}else{
				userAmountVo.setLock_amount(userAmountVo.getLock_amount().subtract(order.getCoupon_price()));
			}
			userAmountVo.setLast_update_time(new Date());
			qzUserAccountMapper.update(userAmountVo);
			saveMoneyRecord(order.getUser_id(), "2",0, order, "支付成功，扣减商品优化金额", order.getCoupon_price());
			List<UserCouponVo> coupons = apiUserCouponMapper.queryCouponsBuOrderId(order.getId());
			if(!CollectionUtils.isEmpty(coupons)){
				for(UserCouponVo userCoupon : coupons){
					userCoupon.setCoupon_status(2);//支付成功，优惠状态不置为已用
					userCoupon.setUsed_time(new Date());
					apiUserCouponMapper.update(userCoupon);
				}
			}
		}
		return this.toResponsObject(0, "执行成功", "");
	}
	/**
	 * 保存平台币流水
	 * @param userId
	 * @param type
	 * @param order
	 * @param remark
	 * @param tranAmount
	 */
	public void saveMoneyRecord(Long userId,String tranType,Integer type,OrderVo order,String remark,BigDecimal tranAmount){
		QzUserAccountVo userAmountVo =qzUserAccountMapper.queruUserAccountInfo(Long.parseLong(userId.toString()));
    	if(userAmountVo != null){
    		QzMoneyRecordVo moneyRecord  = new QzMoneyRecordVo();
    		moneyRecord.setShopUserId(userId.intValue());
    		moneyRecord.setTranType(tranType);//1-充值 2-克拉币 3-回滚/扣减冻结克拉币',
    		moneyRecord.setTranFlag(type);//0-支出 1-收入
    		moneyRecord.setTarnAmount(tranAmount);
    		moneyRecord.setCreateTime(new Date());
    		moneyRecord.setTradeNo(order.getOrder_sn());
    		moneyRecord.setRemark(remark);
    		if(userAmountVo != null){
    			moneyRecord.setLockAmount(userAmountVo.getLock_amount() == null ? new BigDecimal("0") : userAmountVo.getLock_amount());
    			moneyRecord.setCurrentAmount(userAmountVo.getAmount());
    		}
    		apiMoneyRecordMapper.save(moneyRecord);
    	}
    }
	public void saveOrderLog(OrderVo order,JdOrderVo jdOrder,String remark){
		OrderLogVo log = new OrderLogVo();
		if(order != null){
			log.setOrderSn(order.getOrder_sn());
			log.setOrderState(order.getOrder_status());
		}
		if(jdOrder != null){
			log.setThirdOrderState(jdOrder.getOrderStatus());
			log.setOrderKey(jdOrder.getOrderKey());
		}
		log.setRemark(remark);
		log.setCreateTime(new Date());
		log.setUpdateTime(new Date());
		apiOrderLogMapper.save(log);
	}
	
	public static void main(String[] args) {
      BigDecimal a  = new BigDecimal("42");
      BigDecimal  C = new BigDecimal("49");
	  System.out.println(a.compareTo(C));
	}
}
