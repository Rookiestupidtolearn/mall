package com.platform.api;

import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.LoginUser;
import com.platform.cache.J2CacheUtils;
import com.platform.dao.ApiCartMapper;
import com.platform.dao.ApiUserCouponMapper;
import com.platform.dao.GoodsCouponConfigMapper;
import com.platform.dao.QzUserAccountMapper;
import com.platform.entity.BuyGoodsVo;
import com.platform.entity.CartVo;
import com.platform.entity.GoodsCouponConfigVo;
import com.platform.entity.ProductVo;
import com.platform.entity.QzUserAccountVo;
import com.platform.entity.UserCouponVo;
import com.platform.entity.UserVo;
import com.platform.service.ApiProductService;
import com.platform.util.ApiBaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "商品购买")
@RestController
@RequestMapping("/api/buy")
public class ApiBuyController extends ApiBaseAction {
	@Autowired
    private GoodsCouponConfigMapper goodsCouponConfigMapper;
    @Autowired
    private  ApiUserCouponMapper apiUserCouponMapper;
    @Autowired
    private  QzUserAccountMapper qzUserAccountMapper;
    @Autowired
    private ApiCartMapper apiCartMapper;
    @Autowired
    private ApiProductService productService;
    
    
    @ApiOperation(value = "商品添加")
    @PostMapping("/add")
    public Object addBuy(@LoginUser UserVo loginUser) {
        JSONObject jsonParam = getJsonRequest();
        Integer goodsId = jsonParam.getInteger("goodsId");
        Integer productId = jsonParam.getInteger("productId");
        Integer number = jsonParam.getInteger("number");
        BuyGoodsVo goodsVo = new BuyGoodsVo();
        goodsVo.setGoodsId(goodsId);
        goodsVo.setProductId(productId);
        goodsVo.setNumber(number);
        J2CacheUtils.put(J2CacheUtils.SHOP_CACHE_NAME, "goods" + loginUser.getUserId() + "", goodsVo);
        updateUserCouponPrice(goodsId, productId, number, loginUser.getUserId());
        return toResponsMsgSuccess("添加成功");
    }
    
    
    @ApiOperation(value = "取消商品")
    @PostMapping("/cancelAddBuyCoupons")
    public Object updateBuyUserCouponPrice(@LoginUser UserVo loginUser){
    	JSONObject jsonParam = getJsonRequest();
    	BuyGoodsVo goodsVO = (BuyGoodsVo) J2CacheUtils.get(J2CacheUtils.SHOP_CACHE_NAME, "goods" + loginUser.getUserId() + "");
    	if(goodsVO != null){
    		if("true".equals(jsonParam.getString("isBuy"))){
    			updateBuyUserCouponPrice(goodsVO.getGoodsId(), goodsVO.getProductId(), goodsVO.getNumber(), loginUser.getUserId());
    		}
    	}
    	return this.toResponsObject(0, "执行成功", "");
    }
    
    public Object updateUserCouponPrice(Integer goodsId,Integer productId,Integer number,Long userId){
    	BigDecimal couponTotalPrice = BigDecimal.ZERO;//立即购买优惠券总价值
    	BigDecimal couponCartTotalPrice = BigDecimal.ZERO;//购物车优惠券总价值
        BigDecimal couponlPrice = BigDecimal.ZERO;//优惠券临时总价值
        Map<String,Object> map = new HashMap<>();
        map.put("userId",userId);
        BigDecimal amount = BigDecimal.ZERO;//初始化用户平台币
        QzUserAccountVo userAmountVo =qzUserAccountMapper.queruUserAccountInfo(userId);//查询用户平台币信息
       
        List<UserCouponVo> userCouponVos = apiUserCouponMapper.queryUserCouponTotalPrice(userId);//查询用户优惠券信息
        List<CartVo> carts = apiCartMapper.queryUserCarts(userId);	
        List<UserCouponVo> coupons = new ArrayList<>();
        if(!CollectionUtils.isEmpty(userCouponVos)){
       	 for(int i = 0;i<userCouponVos.size();i++){
       		 if(userCouponVos.get(i).getCoupon_id() == 11){
       			 coupons.add(userCouponVos.get(i));
       		 }
       	 }
        }
        UserCouponVo userCouponVo = null;
        if(!CollectionUtils.isEmpty(coupons)){
        	userCouponVo = coupons.get(0);
        }
        
        
        if(!CollectionUtils.isEmpty(carts)){
          	for(CartVo cart : carts){
          		if(null != cart.getChecked() && 1 == cart.getChecked()){
          			//获取产品配比值
          			GoodsCouponConfigVo goodsCoupon = goodsCouponConfigMapper.getUserCoupons(cart.getGoods_id(),userId);
          			ProductVo productInfo = productService.queryObject(cart.getProduct_id());
          			BigDecimal couponlPrice1 = BigDecimal.ZERO;//优惠券临时总价值
          			//计算该产品优惠券总和
          			if(goodsCoupon != null){
          				couponlPrice1 = productInfo.getRetail_price().multiply(new BigDecimal(goodsCoupon.getGood_value())).multiply(new BigDecimal(cart.getNumber()));
          			}
          			couponCartTotalPrice = couponCartTotalPrice.add(couponlPrice1);
          		}
          	}
           }
        
        
        if(userCouponVo != null){
       	 //购物车发生修改  原有优惠券临时作废，重新生成优惠券
       	 userCouponVo.setCoupon_status(7);
       	 apiUserCouponMapper.update(userCouponVo);
       	 //回滚平台币
       	 userAmountVo.setAmount(userAmountVo.getAmount().add(userCouponVo.getCoupon_price()).subtract(couponCartTotalPrice));
       	 qzUserAccountMapper.updateUserAccount(userAmountVo);
        }
        
       //获取产品配比值
       GoodsCouponConfigVo goodsCoupon = goodsCouponConfigMapper.getUserBuyNowCoupons(goodsId);
       ProductVo productInfo = productService.queryObject(productId);
       //计算该产品优惠券总和
       if(goodsCoupon != null){
       	couponlPrice = productInfo.getRetail_price().multiply(new BigDecimal(goodsCoupon.getGood_value())).multiply(new BigDecimal(number));
       }
       couponTotalPrice = couponTotalPrice.add(couponlPrice);
        amount = userAmountVo.getAmount();//获取用户平台币
        if(amount.compareTo(couponTotalPrice)<0){
         	couponTotalPrice = amount;
        }
        userAmountVo.setAmount(userAmountVo.getAmount().subtract(couponTotalPrice));
        qzUserAccountMapper.updateUserAccount(userAmountVo);

        getUserCouponTotalPrice(userId,couponTotalPrice);
        return this.toResponsObject(0, "执行成功", "");
   }
    
    public Object  getUserCouponTotalPrice(Long userId,BigDecimal couponTotalPrice){
  	  List<UserCouponVo> userCouponVos = apiUserCouponMapper.queryUserCouponTotalPrice(userId);//查询用户优惠券信息
        List<UserCouponVo> coupons = new ArrayList<>();
        if(!CollectionUtils.isEmpty(userCouponVos)){
       	 for(int i = 0;i<userCouponVos.size();i++){
       		 if(userCouponVos.get(i).getCoupon_id() == 11){
       			 coupons.add(userCouponVos.get(i));
       		 }
       	 }
        }
      UserCouponVo userCouponVo = null;
      if(!CollectionUtils.isEmpty(coupons)){
      	userCouponVo = coupons.get(0);
      }
  	if(userCouponVo == null){
  		//获取订单使用的优惠券
  		UserCouponVo userCoupon = new UserCouponVo();
  		userCoupon.setCoupon_id(11);
  		userCoupon.setCoupon_number("1");
  		userCoupon.setUser_id(userId);
  		userCoupon.setCoupon_status(1);
  		userCoupon.setCoupon_price(couponTotalPrice);
  		userCoupon.setAdd_time(new Date());
  		apiUserCouponMapper.save(userCoupon);
  		if(null == userCoupon.getId()){
  			return this.toResponsObject(400, "优惠券生成失败", "");
  		}
  	}
		 if(userCouponVo != null){
	     	userCouponVo.setCoupon_price(userCouponVo.getCoupon_price().add(couponTotalPrice));
	     	userCouponVo.setCoupon_status(1);
	     	apiUserCouponMapper.update(userCouponVo);
	     }
  	return this.toResponsObject(0, "优惠券发送成功", "");
  }
   
    
    public Object updateBuyUserCouponPrice(Integer goodsId,Integer productId,Integer number,Long userId){
    	BigDecimal couponTotalPrice = BigDecimal.ZERO;//立即购买优惠券总价值
    	BigDecimal couponCartTotalPrice = BigDecimal.ZERO;//购物车优惠券总价值
        BigDecimal couponlPrice = BigDecimal.ZERO;//优惠券临时总价值
        Map<String,Object> map = new HashMap<>();
        map.put("userId",userId);
        BigDecimal amount = BigDecimal.ZERO;//初始化用户平台币
        QzUserAccountVo userAmountVo =qzUserAccountMapper.queruUserAccountInfo(userId);//查询用户平台币信息
       
        List<UserCouponVo> userCouponVos = apiUserCouponMapper.queryUserCouponTotalPrice(userId);//查询用户优惠券信息
        List<CartVo> carts = apiCartMapper.queryUserCarts(userId);	
        List<UserCouponVo> coupons = new ArrayList<>();
        if(!CollectionUtils.isEmpty(userCouponVos)){
       	 for(int i = 0;i<userCouponVos.size();i++){
       		 if(userCouponVos.get(i).getCoupon_id() == 11){
       			 coupons.add(userCouponVos.get(i));
       		 }
       	 }
        }
        UserCouponVo userCouponVo = null;
        if(!CollectionUtils.isEmpty(coupons)){
        	userCouponVo = coupons.get(0);
        }
        if(!CollectionUtils.isEmpty(carts)){
          	for(CartVo cart : carts){
          		if(null != cart.getChecked() && 1 == cart.getChecked()){
          			//获取产品配比值
          			GoodsCouponConfigVo goodsCoupon = goodsCouponConfigMapper.getUserCoupons(cart.getGoods_id(),userId);
          			ProductVo productInfo = productService.queryObject(cart.getProduct_id());
          			BigDecimal couponlPrice1 = BigDecimal.ZERO;//优惠券临时总价值
          			//计算该产品优惠券总和
          			if(goodsCoupon != null){
          				couponlPrice1 = productInfo.getRetail_price().multiply(new BigDecimal(goodsCoupon.getGood_value())).multiply(new BigDecimal(cart.getNumber()));
          			}
          			couponCartTotalPrice = couponCartTotalPrice.add(couponlPrice1);
          		}
          	}
           }
        
        
        if(userCouponVo != null){
       	 //购物车发生修改  原有优惠券临时作废，重新生成优惠券
       	 userCouponVo.setCoupon_status(7);
       	 apiUserCouponMapper.update(userCouponVo);
       	 //回滚平台币
       	 userAmountVo.setAmount(userAmountVo.getAmount().add(userCouponVo.getCoupon_price()));
       	 qzUserAccountMapper.updateUserAccount(userAmountVo);
        }
        getUserCouponTotalPrice(userId,couponCartTotalPrice);
        return this.toResponsObject(0, "执行成功", "");
   }
}
