package com.platform.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.platform.dao.ApiCartMapper;
import com.platform.dao.ApiTranInfoRecordMapper;
import com.platform.dao.ApiUserCouponMapper;
import com.platform.dao.GoodsCouponConfigMapper;
import com.platform.dao.QzUserAccountMapper;
import com.platform.entity.ApiTranInfoRecordVo;
import com.platform.entity.CartVo;
import com.platform.entity.GoodsCouponConfigVo;
import com.platform.entity.QzUserAccountVo;
import com.platform.entity.UserCouponVo;

@Component
public class ApiUpdateUserCouponPriceUtils extends ApiBaseAction{
	
    @Autowired
    private GoodsCouponConfigMapper goodsCouponConfigMapper;
    @Autowired
    private  ApiUserCouponMapper apiUserCouponMapper;
    @Autowired
    private  QzUserAccountMapper qzUserAccountMapper;
    @Autowired
    private ApiCartMapper apiCartMapper;
    @Autowired
    private ApiTranInfoRecordMapper apiTranInfoRecordMapper;
    @Autowired
    private PayMatchingUtil payMatchingUtils;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 更新用户优惠券、平台币
     * @param goodsId
     * @param productId
     * @param number
     * @param userId
     * @param type 1:添加购物车 2:减少商品到购物车 3:更新购物车 4:是否选择商品
     * @return
     */
    public synchronized Object updateUserCouponPrice(Integer goodsId,Integer productId,Integer number,Long userId,Integer type){
    	 BigDecimal couponTotalPrice = BigDecimal.ZERO;//优惠券总价值
    	 BigDecimal couponTotalPriceAbstract = BigDecimal.ZERO;//理论优惠券价格
         Map<String,Object> map = new HashMap<>();
         map.put("userId",userId);
         BigDecimal amount = BigDecimal.ZERO;//初始化用户平台币
         logger.info("【更新用户优惠券开始】,用户id" + userId);
         QzUserAccountVo userAmountVo =qzUserAccountMapper.queruUserAccountInfo(userId);//查询用户平台币信息
         
         if(userAmountVo != null){
        	 List<UserCouponVo> userCouponVos = apiUserCouponMapper.queryUserCouponTotalPrice(userId);//查询用户优惠券信息
             List<UserCouponVo> coupons = new ArrayList<>();
             List<CartVo> carts = apiCartMapper.queryUserCarts(userId);	
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
             if(userCouponVo != null && userAmountVo != null){
            	 //购物车发生修改  原有优惠券作废，重新生成优惠券
            	 List<UserCouponVo> userCoupons = apiUserCouponMapper.querySubCoupons(userCouponVo.getId());
            	 if(CollectionUtils.isNotEmpty(userCoupons)){
            		 for(UserCouponVo vo :userCoupons){
            			 vo.setCoupon_status(7);
            			 apiUserCouponMapper.update(vo);
            		 }
            	 }
            	 userCouponVo.setCoupon_status(7);
            	 logger.info("【更新用户优惠券】原有优惠券作废，原有优惠券金额" + userCouponVo.getCoupon_price()+"原用户平台币" + userAmountVo.getAmount());
            	 apiUserCouponMapper.update(userCouponVo);
            	 if(type == 1){
            		 saveTranInfoRecord(userId, "1", "2", userCouponVo.getCoupon_price(), userCouponVo.getCoupon_price(), "【添加购物车】购物车发生修改  原有优惠券作废");
            	 }else if(type == 2){
            		 saveTranInfoRecord(userId, "1", "2", userCouponVo.getCoupon_price(), userCouponVo.getCoupon_price(), "【减少商品到购物车】购物车发生修改  原有优惠券作废");
            	 }else if(type == 3){
            		 saveTranInfoRecord(userId, "1", "2", userCouponVo.getCoupon_price(), userCouponVo.getCoupon_price(), "【更新购物车】购物车发生修改  原有优惠券作废");
            	 }else if(type == 4){
            		 saveTranInfoRecord(userId, "1", "2", userCouponVo.getCoupon_price(), userCouponVo.getCoupon_price(), "【是否选择商品】购物车发生修改  原有优惠券作废");
            	 }
            	 //回滚平台币
            	 userAmountVo.setAmount(userAmountVo.getAmount().add(userCouponVo.getCoupon_price()));
            	 qzUserAccountMapper.updateUserAccount(userAmountVo);
            	
            	 logger.info("更新用户平台币,更新后平台币金额" + userAmountVo.getAmount());
            	 if(type == 1){
            		 saveTranInfoRecord(userId, "2", "1", userCouponVo.getCoupon_price(), userAmountVo.getAmount(), "【添加购物车】原有优惠券作废,原优惠券金额回滚到平台币");
            	 }else if(type == 2){
            		 saveTranInfoRecord(userId, "2", "1", userCouponVo.getCoupon_price(), userAmountVo.getAmount(), "【减少商品到购物车】原有优惠券作废,原优惠券金额回滚到平台币");
            	 }else if(type == 3){
            		 saveTranInfoRecord(userId, "2", "1", userCouponVo.getCoupon_price(), userAmountVo.getAmount(), "【更新购物车】原有优惠券作废,原优惠券金额回滚到平台币");
            	 }else if(type == 4){
            		 saveTranInfoRecord(userId, "2", "1", userCouponVo.getCoupon_price(), userAmountVo.getAmount(), "【是否选择商品】原有优惠券作废,原优惠券金额回滚到平台币");
            	 }
             }
             if(!CollectionUtils.isEmpty(carts)){
            	for(CartVo cart : carts){
            		if(null != cart.getChecked() && 1 == cart.getChecked()){
            			//获取产品配比值
            			GoodsCouponConfigVo goodsCoupon = goodsCouponConfigMapper.getUserCoupons(cart.getGoods_id(),userId);
            			BigDecimal couponlPrice = BigDecimal.ZERO;//优惠券临时总价值
            			//计算该产品优惠券总和
            			if(goodsCoupon != null){
            				BigDecimal payMatching = BigDecimal.ZERO;
                			if(payMatchingUtils.getPayMatching(cart.getProduct_id())!= null){
                				Object value = payMatchingUtils.getPayMatching(cart.getProduct_id()).get(cart.getGoods_id());
                				if(value != null){
                					payMatching = new BigDecimal(value.toString());
                				}
                			}
            				couponlPrice = payMatching.multiply(new BigDecimal(cart.getNumber()));
            			}
            			couponTotalPrice = couponTotalPrice.add(couponlPrice);
            		}
            	}
            	couponTotalPriceAbstract = couponTotalPrice;
             }
             if(userAmountVo != null){
            	 amount = userAmountVo.getAmount();
            	 if(amount.compareTo(couponTotalPrice)<0){
            		 couponTotalPrice = amount;
            	 }
            	 userAmountVo.setAmount(userAmountVo.getAmount().subtract(couponTotalPrice));
            	 qzUserAccountMapper.updateUserAccount(userAmountVo);
            	 logger.info("更新用户平台币,更新后平台币金额" + userAmountVo.getAmount());
            	 if(type == 1){
            		 saveTranInfoRecord(userId, "2", "2", couponTotalPrice, userAmountVo.getAmount(), "【添加购物车】回滚平台币后扣减购物车中生成优惠券金额");
            	 }else if(type == 2){
            		 saveTranInfoRecord(userId, "2", "2", couponTotalPrice, userAmountVo.getAmount(), "【减少商品到购物车】回滚平台币后扣减购物车中生成优惠券金额");
            	 }else if(type == 3){
            		 saveTranInfoRecord(userId, "2", "2", couponTotalPrice, userAmountVo.getAmount(), "【更新购物车】回滚平台币后扣减购物车中生成优惠券金额");
            	 }else if(type == 4){
            		 saveTranInfoRecord(userId, "2", "2", couponTotalPrice, userAmountVo.getAmount(), "【是否选择商品】回滚平台币后扣减购物车中生成优惠券金额");
            	 }
             }
             getUserCouponTotalPrice(userId,couponTotalPrice,type,couponTotalPriceAbstract);
         }
         return this.toResponsObject(0, "执行成功", "");
    }
    
    
    
	public Object getUserCouponTotalPrice(Long userId, BigDecimal couponTotalPrice, Integer type,BigDecimal couponTotalPriceAbstract) {
		List<UserCouponVo> userCouponVos = apiUserCouponMapper.queryUserCouponTotalPrice(userId);// 查询用户优惠券信息
		List<UserCouponVo> coupons = new ArrayList<>();
		if (!CollectionUtils.isEmpty(userCouponVos)) {
			for (int i = 0; i < userCouponVos.size(); i++) {
				if (userCouponVos.get(i).getCoupon_id() == 11) {
					coupons.add(userCouponVos.get(i));
				}
			}
		}
		UserCouponVo userCouponVo = null;
		if (!CollectionUtils.isEmpty(coupons)) {
			userCouponVo = coupons.get(0);
		}
		if (userCouponVo == null) {
			// 获取订单使用的总优惠券
			UserCouponVo userCoupon = new UserCouponVo();
			userCoupon.setCoupon_id(11);
			userCoupon.setCoupon_number("1");
			userCoupon.setUser_id(userId);
			userCoupon.setCoupon_status(1);
			userCoupon.setCoupon_price(couponTotalPrice);
			userCoupon.setAdd_time(new Date());
			userCoupon.setParent_id(0);
			userCoupon.setAbstract_coupon_status(couponTotalPriceAbstract);
			apiUserCouponMapper.save(userCoupon);
			if (null == userCoupon.getId()) {
				return this.toResponsObject(400, "优惠券生成失败", "");
			}
			saveSubUserCoupons(userId, userCoupon.getId());
		}
		if (userCouponVo != null) {
			userCouponVo.setAbstract_coupon_status(couponTotalPriceAbstract);
			userCouponVo.setCoupon_price(userCouponVo.getCoupon_price().add(couponTotalPrice));
			userCouponVo.setCoupon_status(1);
			apiUserCouponMapper.update(userCouponVo);
			if (type == 1) {
				saveTranInfoRecord(userId, "1", "1", couponTotalPrice, userCouponVo.getCoupon_price(),
						"【添加购物车】原有优惠券作废,重新更新新优惠券");
			} else if (type == 2) {
				saveTranInfoRecord(userId, "1", "1", couponTotalPrice, userCouponVo.getCoupon_price(),
						"【减少商品到购物车】原有优惠券作废,重新更新新优惠券");
			} else if (type == 3) {
				saveTranInfoRecord(userId, "1", "1", couponTotalPrice, userCouponVo.getCoupon_price(),
						"【更新购物车】原有优惠券作废,重新更新新优惠券");
			}else if(type == 4){
				saveTranInfoRecord(userId, "1", "1", couponTotalPrice, userCouponVo.getCoupon_price(), 
						"【是否选择商品】原有优惠券作废,重新更新新优惠券");
			}else if(type == 5){
				saveTranInfoRecord(userId, "1", "1", couponTotalPrice, userCouponVo.getCoupon_price(), 
						"【购物车删除商品】原有优惠券作废,重新更新新优惠券");
			}
		}
		return this.toResponsObject(0, "优惠券发送成功", "");
	}
    /**
     * 生成子优惠券
     * @param userId
     * @param parentId
     * @return
     */
    public Object  saveSubUserCoupons(Long userId,Integer parentId){
    	QzUserAccountVo userAmountVo =qzUserAccountMapper.queruUserAccountInfo(userId);//查询用户平台币信息
    	if(userAmountVo != null){
    		List<CartVo> carts = apiCartMapper.queryUserCarts(userId);
    		if (CollectionUtils.isEmpty(carts)) {
    			return this.toResponsObject(0, "子优惠券生成失败,购物车无商品", "");
    		}
    		for (int i = 0;i<carts.size();i++) {
    			if (carts.get(i).getChecked() == null || carts.get(i).getChecked() != 1) {
    				continue;
    			}
    			// 获取产品配比值
    			GoodsCouponConfigVo goodsCoupon = goodsCouponConfigMapper.getUserCoupons(carts.get(i).getGoods_id(), userId);
    			BigDecimal couponlPrice = BigDecimal.ZERO;// 优惠券临时总价值
    			// 计算该产品优惠券总和
    			if (goodsCoupon != null) {
    				BigDecimal payMatching = BigDecimal.ZERO;
    				if (payMatchingUtils.getPayMatching(carts.get(i).getProduct_id()) != null) {
    					Object value = payMatchingUtils.getPayMatching(carts.get(i).getProduct_id()).get(carts.get(i).getGoods_id());
    					if (value != null) {
    						payMatching = new BigDecimal(value.toString());
    					}
    				}
    				couponlPrice = payMatching.multiply(new BigDecimal(carts.get(i).getNumber()));
    			}
     			// 生成子优惠券
    			UserCouponVo subUserCoupon = new UserCouponVo();
    			subUserCoupon.setCoupon_id(11);
    			subUserCoupon.setCoupon_number("1");
    			subUserCoupon.setUser_id(userId);
    			subUserCoupon.setCoupon_status(1);
    			subUserCoupon.setCoupon_price(couponlPrice);
    			subUserCoupon.setAdd_time(new Date());
    			subUserCoupon.setParent_id(parentId);
    			subUserCoupon.setGoods_id(Long.parseLong(carts.get(i).getGoods_id().toString()));
    			apiUserCouponMapper.save(subUserCoupon);
    			if (null == subUserCoupon.getId()) {
    				continue;
    			}
    		}
    	}
		return this.toResponsObject(0, "子优惠券生成成功", "");
    }
    
    
    /**
     * 生成平台币、优惠券流水
     * @param userId
     * @param tranType
     * @param TranFlag
     * @param tranAmount
     * @param currentAmount
     * @param remark
     */
    public void saveTranInfoRecord(Long userId,String tranType,String TranFlag,BigDecimal tranAmount,BigDecimal currentAmount
    		,String remark){
    	 ApiTranInfoRecordVo tranInfo = new ApiTranInfoRecordVo();
    	 tranInfo.setUser_id(userId);
    	 tranInfo.setTran_type(tranType);//1优惠券 2 平台币
    	 tranInfo.setTran_flag(TranFlag);//1收入 2支出
    	 tranInfo.setTran_amount(tranAmount);
    	 tranInfo.setCurrent_amount(currentAmount);
    	 tranInfo.setCreate_time(new Date());
    	 tranInfo.setRemark(remark);
    	 apiTranInfoRecordMapper.save(tranInfo);
    }
    /**
     * 子订单提交失败 回滚平台币
     * @param goodId
     * @return
     */
    public Object rollbackUserAmount(Long[] goodIds,Long userId){
    	//平台币金额
    	QzUserAccountVo userAmountVo =qzUserAccountMapper.queruUserAccountInfo(userId);//查询用户平台币信息
    	if(userAmountVo == null){
    		return this.toResponsObject(0, "没有该用户相关平台币信息,平台币回滚失败", "");
    	}
    	if(goodIds.length <= 0){
    		return this.toResponsObject(0, "订单提交成功,无需回滚平台币", "");
    	}
    	for(int i = 0; i<goodIds.length; i++){
    		List<UserCouponVo> coupons = apiUserCouponMapper.quertUserCouponByGoodId(goodIds[i]);
    		if(CollectionUtils.isEmpty(coupons)){
    			continue;
    		}
    		UserCouponVo userCoupon = apiUserCouponMapper.queryObject(coupons.get(0).getParent_id());
    		if(userCoupon == null){
    			continue;
    		}
    		//购物车优惠券总价
    		BigDecimal couponTotalPrice =userCoupon.getCoupon_price(); 
    		//理论上优惠券价格
    		BigDecimal couponTotalPriceAbstract = userCoupon.getAbstract_coupon_status();
    		BigDecimal couponPrice = BigDecimal.ZERO; 
    		List<UserCouponVo> subUserCoupons = apiUserCouponMapper.querySubUserCoupons(userCoupon.getId());
    		for(UserCouponVo vo : subUserCoupons){
    			if(vo.getId()==coupons.get(0).getId()){
    				continue;
    			}
    			couponPrice = couponPrice.add(vo.getCoupon_price());
    		}
    		//理论上优惠券金额大于子实际优惠券总和
    		if(couponTotalPrice.compareTo(couponTotalPriceAbstract) < 0){
    			userAmountVo.setAmount(userAmountVo.getAmount().add(couponTotalPrice.subtract(couponPrice)));
    			qzUserAccountMapper.update(userAmountVo);
    		}
    		//理论上优惠券金额小于等于子实际优惠券总和
    		if(couponTotalPrice.compareTo(couponTotalPriceAbstract) >= 0){
    			userAmountVo.setAmount(userAmountVo.getAmount().add(userCoupon.getCoupon_price()));
    			qzUserAccountMapper.update(userAmountVo);
    		}
    	}
    	return this.toResponsObject(0, "子订单提交失败,回滚平台币成功", "");
    }
}
