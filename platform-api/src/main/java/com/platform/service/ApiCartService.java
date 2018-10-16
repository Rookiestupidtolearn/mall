package com.platform.service;

import com.platform.dao.ApiCartMapper;
import com.platform.dao.ApiUserCouponMapper;
import com.platform.dao.GoodsCouponConfigMapper;
import com.platform.entity.CartVo;
import com.platform.entity.GoodsCouponConfigVo;
import com.platform.entity.ProductVo;
import com.platform.entity.UserCouponVo;

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
        cartDao.deleteByUserAndProductIds(userId, productIds);
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
        GoodsCouponConfigVo goodsCoupon = goodsCouponConfigMapper.getUserCoupon((new Long(goodId).intValue()));
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

}
