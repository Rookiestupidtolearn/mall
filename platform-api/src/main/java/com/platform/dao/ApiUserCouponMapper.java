package com.platform.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.platform.entity.CouponVo;
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
    List<UserCouponVo> queryUserCouponTotalPrice(Long userId);
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
    /**
     * 根据主键id查询用户优惠券
     * @param id
     * @return
     */
    UserCouponVo queryObject(Integer id);
   
    List<CouponVo> queryUsernewCoupons(Map<String, Object> params);
    
    int  updateUserOrderCoupon(UserCouponVo userCouponVo);
    /**
     * 根据父id查询所有子优惠券
     * @param id
     * @return
     */
    List<UserCouponVo> querySubCoupons(Integer id);
    /**
     * 根据goodId查询用户优惠券
     * @param goodId
     * @return
     */
    List<UserCouponVo> quertUserCouponByGoodId(Long goodId);
    /**
     * 根据id查询所有子优惠券
     * @param id
     * @return
     */
    List<UserCouponVo> querySubUserCoupons(Integer id);
    /**
     * 根据orderId查询子优惠券
     * @param orderId
     * @return
     */
    List<UserCouponVo> queryCouponsBuOrderId(Integer orderId);
    
}
