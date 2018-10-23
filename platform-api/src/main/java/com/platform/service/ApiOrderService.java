package com.platform.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.platform.cache.J2CacheUtils;
import com.platform.dao.ApiAddressMapper;
import com.platform.dao.ApiCartMapper;
import com.platform.dao.ApiCouponMapper;
import com.platform.dao.ApiOrderGoodsMapper;
import com.platform.dao.ApiOrderMapper;
import com.platform.dao.ApiUserCouponMapper;
import com.platform.dao.GoodsCouponConfigMapper;
import com.platform.dao.QzUserAccountMapper;
import com.platform.entity.AddressVo;
import com.platform.entity.BuyGoodsVo;
import com.platform.entity.CartVo;
import com.platform.entity.OrderGoodsVo;
import com.platform.entity.OrderVo;
import com.platform.entity.ProductVo;
import com.platform.entity.QzUserAccountVo;
import com.platform.entity.UserCouponVo;
import com.platform.entity.UserVo;
import com.platform.util.CommonUtil;


@Service
public class ApiOrderService {
    @Autowired
    private ApiOrderMapper orderDao;
    @Autowired
    private ApiAddressMapper apiAddressMapper;
    @Autowired
    private ApiCartMapper apiCartMapper;
    @Autowired
    private ApiCouponMapper apiCouponMapper;
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


    public Map<String, Object> submit(JSONObject jsonParam, UserVo loginUser) {
        Map<String, Object> resultObj = new HashMap<String, Object>();
//        Integer couponId = jsonParam.getInteger("couponId");
        String type = jsonParam.getString("type");
        String postscript = jsonParam.getString("postscript");
//        AddressVo addressVo = jsonParam.getObject("checkedAddress",AddressVo.class);
        AddressVo addressVo = apiAddressMapper.queryObject(jsonParam.getInteger("addressId"));

        Integer freightPrice = 0;
        
        // * 获取要购买的商品
        List<CartVo> checkedGoodsList = new ArrayList<>();
        BigDecimal goodsTotalPrice;
        
        List<UserCouponVo> userCouponVos = apiUserCouponMapper.queryUserCouponTotalPrice(loginUser.getUserId());//查询用户优惠券信息
        List<UserCouponVo> coupons = new ArrayList<>();
        if(!CollectionUtils.isEmpty(userCouponVos)){
       	 for(int i = 0;i<userCouponVos.size();i++){
       		 if(userCouponVos.get(i).getCoupon_id() == 11){
       			 coupons.add(userCouponVos.get(i));
       		 }
       	 }
        }
      UserCouponVo userCoupon = null;
      if(!CollectionUtils.isEmpty(coupons)){
    	  userCoupon = coupons.get(0);
      }
      
      
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
            //统计商品总价
            goodsTotalPrice = new BigDecimal(0.00);
            for (CartVo cartItem : checkedGoodsList) {
                goodsTotalPrice = goodsTotalPrice.add(cartItem.getRetail_price().multiply(new BigDecimal(cartItem.getNumber())));
            }
        } else {
            BuyGoodsVo goodsVo = (BuyGoodsVo) J2CacheUtils.get(J2CacheUtils.SHOP_CACHE_NAME, "goods" + loginUser.getUserId());
            ProductVo productInfo = productService.queryObject(goodsVo.getProductId());
            //计算订单的费用
            //商品总价
            goodsTotalPrice = productInfo.getRetail_price().multiply(new BigDecimal(goodsVo.getNumber()));
            
            CartVo cartVo = new CartVo();
            BeanUtils.copyProperties(productInfo, cartVo);
            cartVo.setNumber(goodsVo.getNumber());
            cartVo.setProduct_id(goodsVo.getProductId());
            checkedGoodsList.add(cartVo);
        }
        //订单价格计算
        BigDecimal orderTotalPrice = goodsTotalPrice.add(new BigDecimal(freightPrice)); //订单的总价

        BigDecimal actualPrice = orderTotalPrice.subtract(userCoupon.getCoupon_price());  //减去其它支付的金额后，要实际支付的金额
        //
        OrderVo orderInfo = new OrderVo();
        orderInfo.setOrder_sn(CommonUtil.generateOrderNumber());
        orderInfo.setUser_id(loginUser.getUserId());
        //收货地址和运费
        orderInfo.setConsignee(addressVo.getUserName());
        orderInfo.setMobile(addressVo.getTelNumber());
        orderInfo.setCountry(addressVo.getNationalCode());
        orderInfo.setProvince(addressVo.getProvinceName());
        orderInfo.setCity(addressVo.getCityName());
        orderInfo.setDistrict(addressVo.getCountyName());
        orderInfo.setAddress(addressVo.getDetailInfo());
        //
        orderInfo.setFreight_price(freightPrice);
        //留言
        orderInfo.setPostscript(postscript);
        //使用的优惠券
        orderInfo.setCoupon_id(userCoupon.getId());
        orderInfo.setCoupon_price(userCoupon.getCoupon_price());
        orderInfo.setAdd_time(new Date());
        orderInfo.setGoods_price(goodsTotalPrice);
        orderInfo.setOrder_price(orderTotalPrice);
        orderInfo.setActual_price(actualPrice);
        /*
         * 0 订单创建成功等待付款，　101订单已取消，　102订单已删除
         * 201订单已付款，等待发货
         * 300订单已发货， 301用户确认收货
         * 401 没有发货，退款　402 已收货，退款退货
         */
        orderInfo.setOrder_status(0);
        
        //发货状态 商品配送情况;0未发货,1已发货,2已收货,4退货
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

        //开启事务，插入订单信息和订单商品
        apiOrderMapper.save(orderInfo);
        if (null == orderInfo.getId()) {
            resultObj.put("errno", 1);
            resultObj.put("errmsg", "订单提交失败");
            return resultObj;
        }
        //统计商品总价
        List<OrderGoodsVo> orderGoodsData = new ArrayList<OrderGoodsVo>();
        for (CartVo goodsItem : checkedGoodsList) {
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
            orderGoodsData.add(orderGoodsVo);
            apiOrderGoodsMapper.save(orderGoodsVo);
        }

        //清空已购买的商品
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
        	userCoupon.setCoupon_status(4);//支付中
            apiUserCouponMapper.updateUserOrderCoupon(userCoupon);
        }
        return resultObj;
    }
	
	/***
     * 购物车 时效性 
     * 			
     * 		1.支付成功	
     * 		     	优惠券已经使用
     * 
     * 		2.超时未支付 
     * 			   	优惠券失效 逻辑删除
     * 				失效操作：
     * 					回滚平台币
     * 					作废优惠券
     * 					查看是否需要清除购物车
     */
	public JSONObject checkOrderValid(Integer orderId, Integer userId){
		JSONObject resultObj = new JSONObject();
		OrderVo orderVo =  orderDao.queryObject(orderId);
		if(orderVo == null){
        	resultObj.put("couponStatus", "没有查询到相关订单");
        }
		UserCouponVo userCouponVo = apiUserCouponMapper.queryObject(orderVo.getCoupon_id());
		BigDecimal amount = BigDecimal.ZERO;
		QzUserAccountVo userAmountVo =qzUserAccountMapper.queruUserAccountInfo(Long.parseLong(userId.toString()));
        if(userAmountVo != null){
        	amount = userAmountVo.getAmount();
        }
		if(orderVo != null){
			//0未付款;1 付款中;2已付款;4退款
			if(orderVo.getPay_status() == 2){//已付款
				if(userCouponVo != null){
					userCouponVo.setCoupon_status(2);
					apiUserCouponMapper.updateUserCoupon(userCouponVo);
				}
				resultObj.put("couponStatus", "支付成功,优惠券已用");
			}
			//0未支付 1 付款中 2 已付款 4退款
			if(orderVo.getPay_status() == 4){//退款
				if(userCouponVo != null){
					userCouponVo.setCoupon_status(3);//作废
					apiUserCouponMapper.updateUserCoupon(userCouponVo);
				}
				//回滚平台币
				amount = amount.add(userCouponVo.getCoupon_price());
				userAmountVo.setAmount(amount);
				qzUserAccountMapper.updateUserAccount(userAmountVo);
				resultObj.put("couponStatus", "支付失败,优惠券作废");
			}
			if(orderVo.getPay_status() == 0){//未支付
				
			}
		}
		return resultObj;
	}
}
