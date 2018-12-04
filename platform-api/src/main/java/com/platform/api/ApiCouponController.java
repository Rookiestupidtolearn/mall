package com.platform.api;

import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.LoginUser;
import com.platform.cache.J2CacheUtils;
import com.platform.entity.*;
import com.platform.service.*;
import com.platform.util.ApiBaseAction;
import com.platform.utils.CharUtil;
import com.platform.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * API优惠券管理
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-03-23 15:31
 */
@Api(tags = "优惠券")
@RestController
@RequestMapping("/api/coupon")
public class ApiCouponController extends ApiBaseAction {
    @Autowired
    private ApiUserService apiUserService;
    @Autowired
    private ApiCouponService apiCouponService;
    @Autowired
    private ApiUserCouponService apiUserCouponService;
    @Autowired
    private ApiProductService apiProductService;
    @Autowired
    private ApiCartService apiCartService;

    /**
     * 获取优惠券列表
     */
    @ApiOperation(value = "获取优惠券列表")
    @PostMapping("/list")
    public Object list(@LoginUser UserVo loginUser) {
        Map param = new HashMap();
        BigDecimal goodsTotalPrice = BigDecimal.ZERO;
        param.put("user_id", loginUser.getUserId());
        List<CouponVo> coupons = new ArrayList<>();
        List<CouponVo> couponVos = apiCouponService.queryUsernewCoupons(param);
        if(!CollectionUtils.isEmpty(couponVos)){
        	for(CouponVo coupon : couponVos){
        		if(coupon.getCoupon_price().compareTo(BigDecimal.ZERO) > 0){
        			coupons.add(coupon);
        			continue;
        		}
        	}
        }
        List<CartVo> cartList = apiCartService.queryList(param);
        //获取购物车统计信息
        if(!CollectionUtils.isEmpty(cartList)){
        	for (CartVo cartItem : cartList) {
        		if (null != cartItem.getChecked() && 1 == cartItem.getChecked()) {
        			goodsTotalPrice = goodsTotalPrice.add(cartItem.getMarket_price().multiply(new BigDecimal(cartItem.getNumber())));
        		}
        	}
        }
        List<CouponVo> useCoupons = new ArrayList<>();
        List<CouponVo> notUseCoupons = new ArrayList<>();
        if(!CollectionUtils.isEmpty(coupons)){
        	for (CouponVo couponVo : coupons) {
        		if(couponVo.getCoupon_status() == 4){//订单未支付 优惠券使用中
    				couponVo.setEnabled(1);
        			notUseCoupons.add(couponVo);
        			continue;
    			}
        		if (goodsTotalPrice.compareTo(couponVo.getCoupon_price()) >= 0 && "平台抵扣券".equals(couponVo.getName())) { // 可用优惠券
        			if(couponVo.getCoupon_status() == 1){//可用
        				couponVo.setEnabled(1);
        				useCoupons.add(couponVo);
        				continue;
        			}
        			if(couponVo.getCoupon_status() == 2){//已用
        				couponVo.setEnabled(0);
            			notUseCoupons.add(couponVo);
            			continue;
        			}
        		} else {
    				couponVo.setEnabled(0);
    				notUseCoupons.add(couponVo);
    				continue;
        		}
        	}
        }
        useCoupons.addAll(notUseCoupons);
        
        return toResponsSuccess(coupons);
    }

    /**
     * 根据商品获取可用优惠券列表
     */
    @ApiOperation(value = "根据商品获取可用优惠券列表")
    @PostMapping("/listByGoods")
    public Object listByGoods(@RequestParam(defaultValue = "cart") String type, @LoginUser UserVo loginUser) {
        BigDecimal goodsTotalPrice = new BigDecimal(0.00);
        if (type.equals("cart")) {
            Map param = new HashMap();
            param.put("user_id", loginUser.getUserId());
            List<CartVo> cartList = apiCartService.queryList(param);
            //获取购物车统计信息
            for (CartVo cartItem : cartList) {
                if (null != cartItem.getChecked() && 1 == cartItem.getChecked()) {
                    goodsTotalPrice = goodsTotalPrice.add(cartItem.getMarket_price().multiply(new BigDecimal(cartItem.getNumber())));
                }
            }
        } else { // 是直接购买的
            BuyGoodsVo goodsVo = (BuyGoodsVo) J2CacheUtils.get(J2CacheUtils.SHOP_CACHE_NAME, "goods" + loginUser.getUserId() + "");
            if(goodsVo != null){
            	ProductVo productInfo = apiProductService.queryObject(goodsVo.getProductId());
            	//商品总价
            	goodsTotalPrice = productInfo.getMarket_price().multiply(new BigDecimal(goodsVo.getNumber()));
            }
        }

        // 获取可用优惠券
        Map param = new HashMap();
        param.put("user_id", loginUser.getUserId());
        param.put("coupon_status", 1);
        List<CouponVo> usecouponVos = new ArrayList<>();
        List<CouponVo> couponVos = apiCouponService.queryUserCoupons(param);
        if(!CollectionUtils.isEmpty(couponVos)){
        	for(CouponVo coupon : couponVos){
        		if(coupon.getCoupon_price().compareTo(BigDecimal.ZERO) > 0){
        			usecouponVos.add(coupon);
        			continue;
        		}
        	}
        }
        
        List<CouponVo> useCoupons = new ArrayList<>();
        List<CouponVo> notUseCoupons = new ArrayList<>();
        if(!CollectionUtils.isEmpty(usecouponVos)){
        	for (CouponVo couponVo : usecouponVos) {
        		if (goodsTotalPrice.compareTo(couponVo.getCoupon_price()) >= 0 && "平台抵扣券".equals(couponVo.getName())) { // 可用优惠券
        			couponVo.setEnabled(1);
        			useCoupons.add(couponVo);
        		} else {
        			couponVo.setEnabled(0);
        			notUseCoupons.add(couponVo);
        		}
        	}
        }
        useCoupons.addAll(notUseCoupons);
        return toResponsSuccess(useCoupons);
    }

    /**
     * 兑换优惠券
     */
    @ApiOperation(value = "领券优惠券")
    @PostMapping("exchange")
    public Object exchange(@LoginUser UserVo loginUser) {
        JSONObject jsonParam = getJsonRequest();
        String coupon_number = jsonParam.getString("coupon_number");
        if (StringUtils.isNullOrEmpty(coupon_number)) {
            return toResponsFail("当前优惠码无效");
        }
        //
        Map param = new HashMap();
        param.put("coupon_number", coupon_number);
        List<UserCouponVo> couponVos = apiUserCouponService.queryList(param);
        UserCouponVo userCouponVo = null;
        if (null == couponVos || couponVos.size() == 0) {
            return toResponsFail("当前优惠码无效");
        }
        userCouponVo = couponVos.get(0);
        if (null != userCouponVo.getUser_id() && !userCouponVo.getUser_id().equals(0L)) {
            return toResponsFail("当前优惠码已经兑换");
        }
        CouponVo couponVo = apiCouponService.queryObject(userCouponVo.getCoupon_id());
        if (null == couponVo || null == couponVo.getUse_end_date() || couponVo.getUse_end_date().before(new Date())) {
            return toResponsFail("当前优惠码已经过期");
        }
        userCouponVo.setUser_id(loginUser.getUserId());
        userCouponVo.setAdd_time(new Date());
        apiUserCouponService.update(userCouponVo);
        return toResponsSuccess(userCouponVo);
    }

    /**
     * 　　填写手机号码，领券
     */
    @ApiOperation(value = "领券优惠券")
    @PostMapping("newuser")
    public Object newuser(@LoginUser UserVo loginUser) {
        JSONObject jsonParam = getJsonRequest();
        //
        String phone = jsonParam.getString("phone");
        String smscode = jsonParam.getString("smscode");
        // 校验短信码
        SmsLogVo smsLogVo = apiUserService.querySmsCodeByUserId(loginUser.getUserId());
        if (null != smsLogVo && !smsLogVo.getSmsCode().equals(smscode)) {
            return toResponsFail("短信校验失败");
        }
        // 更新手机号码
        if (!StringUtils.isNullOrEmpty(phone)) {
            if (phone.equals(loginUser.getMobile())) {
                loginUser.setMobile(phone);
                apiUserService.update(loginUser);
            }
        }
        // 判断是否是新用户
        if (!StringUtils.isNullOrEmpty(loginUser.getMobile())) {
            return toResponsFail("当前优惠券只能新用户领取");
        }
        // 是否领取过了
        Map params = new HashMap();
        params.put("user_id", loginUser.getUserId());
        params.put("send_type", 4);
        List<CouponVo> couponVos = apiCouponService.queryUserCoupons(params);
        if (null != couponVos && couponVos.size() > 0) {
            return toResponsFail("已经领取过，不能重复领取");
        }
        // 领取
        Map couponParam = new HashMap();
        couponParam.put("send_type", 4);
        CouponVo newCouponConfig = apiCouponService.queryMaxUserEnableCoupon(couponParam);
        if (null != newCouponConfig) {
            UserCouponVo userCouponVo = new UserCouponVo();
            userCouponVo.setAdd_time(new Date());
            userCouponVo.setCoupon_id(newCouponConfig.getId());
            userCouponVo.setCoupon_number(CharUtil.getRandomString(12));
            userCouponVo.setUser_id(loginUser.getUserId());
            apiUserCouponService.save(userCouponVo);
            return toResponsSuccess(userCouponVo);
        } else {
            return toResponsFail("领取失败");
        }
    }

    /**
     * 　　转发领取红包
     */
    @ApiOperation(value = "转发领取红包")
    @PostMapping("transActivit")
    public Object transActivit(@LoginUser UserVo loginUser, String sourceKey, Long referrer) {
        JSONObject jsonParam = getJsonRequest();
        // 是否领取过了
        Map params = new HashMap();
        params.put("user_id", loginUser.getUserId());
        params.put("send_type", 2);
        params.put("source_key", sourceKey);
        List<CouponVo> couponVos = apiCouponService.queryUserCoupons(params);
        if (null != couponVos && couponVos.size() > 0) {
            return toResponsObject(2, "已经领取过", couponVos);
        }
        // 领取
        Map couponParam = new HashMap();
        couponParam.put("send_type", 2);
        CouponVo newCouponConfig = apiCouponService.queryMaxUserEnableCoupon(couponParam);
        if (null != newCouponConfig) {
            UserCouponVo userCouponVo = new UserCouponVo();
            userCouponVo.setAdd_time(new Date());
            userCouponVo.setCoupon_id(newCouponConfig.getId());
            userCouponVo.setCoupon_number(CharUtil.getRandomString(12));
            userCouponVo.setUser_id(loginUser.getUserId());
            userCouponVo.setSource_key(sourceKey);
            userCouponVo.setReferrer(referrer);
            apiUserCouponService.save(userCouponVo);
            //
            List<UserCouponVo> userCouponVos = new ArrayList();
            userCouponVos.add(userCouponVo);
            //
            params = new HashMap();
            params.put("user_id", loginUser.getUserId());
            params.put("send_type", 2);
            params.put("source_key", sourceKey);
            couponVos = apiCouponService.queryUserCoupons(params);
            return toResponsSuccess(couponVos);
        } else {
            return toResponsFail("领取失败");
        }
    }
}
