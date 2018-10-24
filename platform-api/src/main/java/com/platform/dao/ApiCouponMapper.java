package com.platform.dao;

import com.platform.entity.CouponVo;

import java.util.List;
import java.util.Map;

/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-11 09:16:46
 */
public interface ApiCouponMapper extends BaseDao<CouponVo> {
    /**
     * 按条件查询用户优惠券
     *
     * @param params
     * @return
     */
    List<CouponVo> queryUserCoupons(Map<String, Object> params);

    /**
     * 按条件查询用户优惠券
     *
     * @param params
     * @return
     */
    CouponVo getUserCoupon(Integer id);

    /**
     * 按类型查询
     *
     * @param params
     * @return
     */
    CouponVo queryMaxUserEnableCoupon(Map<String, Object> params);

    /**
     * sendType = 1或4 的优惠券
     *
     * @param params
     * @return
     */
    List<CouponVo> queryUserCouponList(Map<String, Object> params);

    int updateUserCoupon(CouponVo couponVo);
    /**
     * 优惠券与订单一一对应
     * @param couponVo
     * @param orderId
     * @return
     */
    int updateUserCouponNew (CouponVo couponVo,Integer orderId);
    
    /**
     * 根据userid查询所有优惠券
     * @param params
     * @return
     */
    List<CouponVo> getUserDeductibleCoupon(Map<String, Object> params);
    
    CouponVo getUserDeductCoupon(Long userId);
    
    List<CouponVo> queryUsernewCoupons(Map<String, Object> params);
}
