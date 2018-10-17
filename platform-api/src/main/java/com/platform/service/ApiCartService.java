package com.platform.service;

import com.platform.dao.ApiCartMapper;
import com.platform.dao.ApiUserCouponMapper;
import com.platform.dao.GoodsCouponConfigMapper;
import com.platform.dao.QzUserAccountMapper;
import com.platform.entity.CartVo;
import com.platform.entity.GoodsCouponConfigVo;
import com.platform.entity.ProductVo;
import com.platform.entity.QzUserAccountVo;
import com.platform.entity.UserCouponVo;

import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ApiCartService {
    @Autowired
    private ApiCartMapper cartDao;
    @Autowired
    private ApiUserCouponMapper apiUserCouponMapper;
    @Autowired
    private GoodsCouponConfigMapper goodsCouponConfigMapper;
    @Autowired
    private ApiProductService productService;
    @Autowired
    private ApiCartMapper cartMapper;
    @Autowired
    private QzUserAccountMapper qzUserAccountMapper;

    public CartVo queryObject(Integer id) {
        return cartDao.queryObject(id);
    }


    public List<CartVo> queryList(Map<String, Object> map) {
        return cartDao.queryList(map);
    }


    public int queryTotal(Map<String, Object> map) {
        return cartDao.queryTotal(map);
    }


    public void save(CartVo cart) {
        cartDao.save(cart);
        // 更新购物车搭配减价
        // 判断购物车中是否存在此规格商品
        Map cartParam = new HashMap();
        cartParam.put("user_id", cart.getUser_id());
        List<CartVo> cartInfoList = cartDao.queryList(cartParam);
        Map crashParam = new HashMap();
        List<Integer> goods_ids = new ArrayList();
        List<CartVo> cartUpdateList = new ArrayList();
        for (CartVo cartItem : cartInfoList) {
            if (null != cartItem.getChecked() && 1 == cartItem.getChecked()) {
                goods_ids.add(cartItem.getGoods_id());
            }
            if (!cartItem.getRetail_price().equals(cartItem.getRetail_product_price())) {
                cartItem.setRetail_price(cartItem.getRetail_product_price());
                cartUpdateList.add(cartItem);
            }
        }
        crashParam.put("goods_ids", goods_ids);
        for (CartVo cartItem : cartInfoList) {
            // 存在原始的
            if (null != cartItem.getChecked() && 1 == cartItem.getChecked()) {
                for (CartVo cartCrash : cartInfoList) {
                    if (!cartCrash.getId().equals(cartItem.getId())) {
                        cartUpdateList.add(cartItem);
                    }
                }
            }
        }
        if (null != cartUpdateList && cartUpdateList.size() > 0) {
            for (CartVo cartItem : cartUpdateList) {
                cartDao.update(cartItem);
            }
        }
    }

    public void update(CartVo cart) {
        cartDao.update(cart);
    }


    public void delete(Integer id) {
        cartDao.delete(id);
    }


    public void deleteBatch(Integer[] ids) {
        cartDao.deleteBatch(ids);
    }

    public void updateCheck(String[] productIds, Integer isChecked, Long userId) {
        cartDao.updateCheck(productIds, isChecked, userId);

        // 判断购物车中是否存在此规格商品
        Map cartParam = new HashMap();
        cartParam.put("user_id", userId);
        List<CartVo> cartInfoList = cartDao.queryList(cartParam);
        Map crashParam = new HashMap();
        List<Integer> goods_ids = new ArrayList();
        List<CartVo> cartUpdateList = new ArrayList();
        for (CartVo cartItem : cartInfoList) {
            if (null != cartItem.getChecked() && 1 == cartItem.getChecked()) {
                goods_ids.add(cartItem.getGoods_id());
            }
            if (!cartItem.getRetail_price().equals(cartItem.getRetail_product_price())) {
                cartItem.setRetail_price(cartItem.getRetail_product_price());
                cartUpdateList.add(cartItem);
            }
        }
        if (null != goods_ids && goods_ids.size() > 0) {
            crashParam.put("goods_ids", goods_ids);
            for (CartVo cartItem : cartInfoList) {
                // 存在原始的
                if (null != cartItem.getChecked() && 1 == cartItem.getChecked()) {
                    for (CartVo cartCrash : cartInfoList) {
                        if (null != cartItem.getChecked() && 1 == cartItem.getChecked() && !cartCrash.getId().equals(cartItem.getId())) {
                            cartUpdateList.add(cartCrash);
                            break;
                        }
                    }
                }
            }
        }
        if (null != cartUpdateList && cartUpdateList.size() > 0) {
            for (CartVo cartItem : cartUpdateList) {
                cartDao.update(cartItem);
            }
            ProductVo productInfo = productService.queryObject(Integer.parseInt(productIds[0]));
            updateUserCouponPrice(userId, cartUpdateList.get(0).getGoods_id(),Integer.parseInt(productIds[0]),productInfo != null ? productInfo : null, cartUpdateList.size());
        }
    }

    public void deleteByProductIds(String[] productIds) {
        cartDao.deleteByProductIds(productIds);
    }

    public void deleteByUserAndProductIds(Long userId, String[] productIds) {
    	 List<String> products= new ArrayList<>();	
    	 cartDao.deleteByUserAndProductIds(userId, productIds);
    	 Map<String,Object> param = new HashMap<String, Object>();
    	 if(productIds != null && productIds.length > 0){
    		 for(int i = 0;i < productIds.length;i++){
    			 if(!products.contains(productIds[i])) {
    				 products.add(productIds[i]);
    			 }
    		 }
    	 }
    	 
	    if(!CollectionUtils.isEmpty(products)){
	    	for(String product : products){
	    		param.put("product_id", product);
	    		param.put("user_id", userId);
	    		List<CartVo> cart = cartMapper.queryList(param);
	    		updateUserCouponPrice(cart.get(0).getGoods_id(),cart.get(0).getProduct_id(),cart.get(0).getNumber(),userId);
	    	}
	    }
    }

    public void deleteByCart(Long user_id, Integer session_id, Integer checked) {
        cartDao.deleteByCart(user_id, session_id, checked);
    }
    public Object updateUserCouponPrice(Long userId,Integer goodId,Integer productId,ProductVo productInfo,Integer number){
    	BigDecimal couponTotalPrice = BigDecimal.ZERO;
    	Map<String, Object> param = new HashMap<String, Object>();
    	 //删除原有优惠券信息
        param.put("good_id", goodId);
        param.put("user_id", userId);
        apiUserCouponMapper.delete(param);
        //获取优惠券比例
        GoodsCouponConfigVo goodsCoupon = goodsCouponConfigMapper.getUserCoupons((new Long(goodId).intValue()),userId);
        if(goodsCoupon != null){
        	couponTotalPrice = productInfo.getRetail_price().multiply(new BigDecimal(goodsCoupon.getGood_value())).multiply(new BigDecimal(number));
        }
      //新生成优惠券
        UserCouponVo userCoupon = new UserCouponVo();
        userCoupon.setCoupon_id(11);
        userCoupon.setCoupon_number("1");
        userCoupon.setUser_id(userId);
        userCoupon.setCoupon_status(1);
        userCoupon.setCoupon_price(couponTotalPrice);
        userCoupon.setGood_id(productInfo.getGoods_id());
        apiUserCouponMapper.save(userCoupon);
        if(null == userCoupon.getId()){
        	return this.toResponsObject(400, "优惠券生成失败", "");
        }
        return this.toResponsObject(0, "优惠券生成成功", "");
    }
    /**
     * @param requestCode
     * @param msg
     * @param data
     * @return Map<String,Object>
     * @throws
     * @Description:构建统一格式返回对象
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
     * 获取商品优惠券
     * @param goodsId
     * @param productId
     * @param number
     * @param userId
     * @return
     */
    public Object updateUserCouponPrice(Integer goodsId,Integer productId,Integer number,Long userId){
   	 BigDecimal couponTotalPrice = BigDecimal.ZERO;//优惠券总价值
        BigDecimal couponlPrice = BigDecimal.ZERO;//优惠券临时总价值
        // 获取平台币 TODO
        BigDecimal amount = BigDecimal.ZERO;
        QzUserAccountVo userAmountVo =qzUserAccountMapper.queruUserAccountInfo(userId);
        ProductVo productInfo = productService.queryObject(productId);
        if(number != null){
       	 if (null == productInfo || productInfo.getGoods_number() < number) {
       		 return this.toResponsObject(400, "库存不足", "");
       	 }
        }
        if(userAmountVo != null){
        	amount = userAmountVo.getAmount();
        }
        //获取产品配比值
        GoodsCouponConfigVo goodsCoupon = goodsCouponConfigMapper.getUserCoupons(goodsId,userId);
        /**
         * 1.获取用户的平台币
         *   获取产品配比
         *   	判断配比
         *   	  1.1  平台币 等于配比值  直接兑换
         *   	  1.2 平台币 大于配比值   取配置值
         *   	  1.3 平台币 小于配比值   按平台币最小规则兑换 			
         * */    
        if(goodsCoupon != null){
        	couponlPrice = productInfo.getRetail_price().multiply(new BigDecimal(goodsCoupon.getGood_value())).multiply(new BigDecimal(number));
        }
        if(amount.compareTo(couponlPrice) == 0){
        	couponTotalPrice = couponlPrice;
        }
        if(amount.compareTo(couponlPrice)>0){
        	couponTotalPrice = amount;
        }
        if(amount.compareTo(couponlPrice)<0){
        	couponTotalPrice = couponlPrice;
        }
        /**
         * 1.检查当前购物车是否已经生成了优惠券
         * 			1.1  有       更新的优惠券值
         * 			1.2  没有    新增一条
         * */  
        getUserCouponTotalPrice(userId,couponTotalPrice);
        return this.toResponsObject(0, "执行成功", "");
   }
    public Object  getUserCouponTotalPrice(Long userId,BigDecimal couponTotalPrice){
    	UserCouponVo userCouponVo = apiUserCouponMapper.queryUserCouponTotalPrice(userId);
	    if(userCouponVo != null){
     	   userCouponVo.setCoupon_price(userCouponVo.getCoupon_price().subtract(couponTotalPrice));
     	   apiUserCouponMapper.update(userCouponVo);
        }
    	return this.toResponsObject(0, "优惠券发送成功", "");
    }
}
