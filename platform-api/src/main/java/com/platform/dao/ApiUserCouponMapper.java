package com.platform.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.platform.entity.UserCouponVo;

/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-11 09:16:47
 */
public interface ApiUserCouponMapper extends BaseDao<UserCouponVo> {
    UserCouponVo queryByCouponNumber(@Param("coupon_number") String coupon_number);
    /**
     * 生成优惠券
     * @param userCouponVo
     * @return
     */
    int save(UserCouponVo userCouponVo);
    
    int updateUserCoupon(UserCouponVo userCouponVo);
    /*
     * 根据userid/good查询优惠券
     */
    List<UserCouponVo> queryUserCoupons(Map<String, Object> params);
    /**
     * 根据userid查询用户优惠券
     * @param userId
     * @return
     */
    UserCouponVo queryUserCouponTotalPrice(Long userId);
    /**
     * 根据userid删除用户优惠券
     * @param userId
     * @return
     */
    int deleteUserCouponPrice(Map<String, Object> params);
    /**
     * 根据userid查询用户使用的优惠券
     * @param userId
     * @return
     */
    UserCouponVo queryUserUsedCouponTotalPrice (Long userId);
}
