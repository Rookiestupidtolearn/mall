package com.platform.service;

import com.platform.dao.ApiCartMapper;
import com.platform.dao.ApiTranInfoRecordMapper;
import com.platform.dao.ApiUserCouponMapper;
import com.platform.dao.GoodsCouponConfigMapper;
import com.platform.dao.QzUserAccountMapper;
import com.platform.entity.ApiTranInfoRecordVo;
import com.platform.entity.CartVo;
import com.platform.entity.GoodsCouponConfigVo;
import com.platform.entity.ProductVo;
import com.platform.entity.QzUserAccountVo;
import com.platform.entity.UserCouponVo;

import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ApiCartService {
	
    
	private Logger logger = LoggerFactory.getLogger(getClass());
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
    @Autowired
    private ApiCartMapper apiCartMapper;
    @Autowired
    private ApiTranInfoRecordMapper apiTranInfoRecordMapper;

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
        if (null != goods_ids && goods_ids.size() > 1) {
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
        if (null != goods_ids && goods_ids.size() == 1) {
            for (CartVo cartItem : cartInfoList) {
                // 存在原始的
                if (null != cartItem.getChecked() && 1 == cartItem.getChecked()) {
                    if (null != cartItem.getChecked() && 1 == cartItem.getChecked()) {
                        cartUpdateList.add(cartItem);
                    }
                }
            }
        }
        if (null != cartUpdateList && cartUpdateList.size() > 0) {
            for (CartVo cartItem : cartUpdateList) {
                cartDao.update(cartItem);
            }
            updateUserCouponPrice(cartUpdateList.get(0).getGoods_id(),Integer.parseInt(productIds[0]),cartUpdateList.get(0).getNumber(),cartUpdateList.get(0).getUser_id());
        }else{
        	updateUserCouponPrice(userId);
        }
    }

    public void deleteByProductIds(String[] productIds) {
        cartDao.deleteByProductIds(productIds);
    }

    public void deleteByUserAndProductIds(Long userId, String[] productIds) {
    	logger.info("【购物车删除商品开始】用户id" + userId);
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
    	 QzUserAccountVo userAmountVo =qzUserAccountMapper.queruUserAccountInfo(userId);//查询用户平台币信息
    	 BigDecimal couponTotalPrice = BigDecimal.ZERO;//优惠券总价值
       
         BigDecimal amount = BigDecimal.ZERO;//初始化用户平台币
    	 List<String> products= new ArrayList<>();	
    	 Map<String,Object> param = new HashMap<String, Object>();
    	 if(productIds != null && productIds.length > 0){
    		 for(int i = 0;i < productIds.length;i++){
    			 if(!products.contains(productIds[i])) {
    				 products.add(productIds[i]);
    			 }
    		 }
    	 }
    	 if(!CollectionUtils.isEmpty(products)){
    		 for(int i = 0;i<products.size();i++){
    			param.put("product_id", products.get(i));
 	    		param.put("user_id", userId);
 	    		List<CartVo> cart = cartMapper.getCarts(param);
 	    		if(!CollectionUtils.isEmpty(cart)){
 	    			cartMapper.delete(cart.get(0).getId());
 	    		}
    		 }
    		 if(userCouponVo != null){
    			 //购物车发生修改  原有优惠券作废，重新生成优惠券
    			 userCouponVo.setCoupon_status(7);
    			 logger.info("【更新用户优惠券】原有优惠券作废，原有优惠券金额" + userCouponVo.getCoupon_price()+"原用户平台币" + userAmountVo.getAmount());
    			 apiUserCouponMapper.update(userCouponVo);
    			 
            	 saveTranInfoRecord(userId, "1", "2", userCouponVo.getCoupon_price(), userCouponVo.getCoupon_price(), "购物车发生修改  原有优惠券作废");//回滚平台币
    			 userAmountVo.setAmount(userAmountVo.getAmount().add(userCouponVo.getCoupon_price()));
    			 qzUserAccountMapper.updateUserAccount(userAmountVo);
   
            	 saveTranInfoRecord(userId, "2", "1", userCouponVo.getCoupon_price(), userAmountVo.getAmount(), "原有优惠券作废,原优惠券金额回滚到平台币");
    			 logger.info("更新用户平台币,更新后平台币金额" + userAmountVo.getAmount() );
    		 }
    	 }
         
         //查询新购物车信息
    	 List<CartVo> carts = apiCartMapper.queryUserCarts(userId);	
    	 if(!CollectionUtils.isEmpty(carts)){
         	for(CartVo cart : carts){
         		if(null != cart.getChecked() && 1 == cart.getChecked()){
         			ProductVo productInfo = productService.queryObject(cart.getProduct_id());
         			//获取产品配比值
         			GoodsCouponConfigVo goodsCoupon = goodsCouponConfigMapper.getUserCoupons(cart.getGoods_id(),userId);
         			BigDecimal couponlPrice = BigDecimal.ZERO;//优惠券临时总价值
         			//计算该产品优惠券总和
         			if(goodsCoupon != null){
         				couponlPrice = productInfo.getMarket_price().multiply(new BigDecimal(goodsCoupon.getGood_value())).multiply(new BigDecimal(cart.getNumber()));
         			}
         			couponTotalPrice = couponTotalPrice.add(couponlPrice);
         		}
         	}
          }
          amount = userAmountVo.getAmount();//获取用户平台币
          if(amount.compareTo(couponTotalPrice)<0){
           	couponTotalPrice = amount;
          }
          userAmountVo.setAmount(userAmountVo.getAmount().subtract(couponTotalPrice));
          qzUserAccountMapper.updateUserAccount(userAmountVo);

     	  saveTranInfoRecord(userId, "2", "2", couponTotalPrice, userAmountVo.getAmount(), "回滚平台币后扣减购物车中生成优惠券金额");

          getUserCouponTotalPrice(userId,couponTotalPrice);
	      cartDao.deleteByUserAndProductIds(userId, productIds);
    }

    public void deleteByCart(Long user_id, Integer session_id, Integer checked) {
        cartDao.deleteByCart(user_id, session_id, checked);
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
    public synchronized Object updateUserCouponPrice(Integer goodsId,Integer productId,Integer number,Long userId){

   	 BigDecimal couponTotalPrice = BigDecimal.ZERO;//优惠券总价值
       
        Map<String,Object> map = new HashMap<>();
        map.put("userId",userId);
        BigDecimal amount = BigDecimal.ZERO;//初始化用户平台币
        logger.info("【更新用户优惠券开始】,用户id" + userId);
        QzUserAccountVo userAmountVo =qzUserAccountMapper.queruUserAccountInfo(userId);//查询用户平台币信息
       
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
        if(userCouponVo != null){
       	 //购物车发生修改  原有优惠券作废，重新生成优惠券
       	 userCouponVo.setCoupon_status(7);
       	 logger.info("【更新用户优惠券】原有优惠券作废，原有优惠券金额" + userCouponVo.getCoupon_price()+"原用户平台币" + userAmountVo.getAmount());
       	 apiUserCouponMapper.update(userCouponVo);
       	 saveTranInfoRecord(userId, "1", "2", userCouponVo.getCoupon_price(), userCouponVo.getCoupon_price(), "购物车发生修改  原有优惠券作废");
       	 //回滚平台币
       	 userAmountVo.setAmount(userAmountVo.getAmount().add(userCouponVo.getCoupon_price()));
       	 qzUserAccountMapper.updateUserAccount(userAmountVo);
       	 logger.info("更新用户平台币,更新后平台币金额" + userAmountVo.getAmount() );
       	saveTranInfoRecord(userId, "2", "1", userCouponVo.getCoupon_price(), userAmountVo.getAmount(), "原有优惠券作废,原优惠券金额回滚到平台币");
        }
        if(!CollectionUtils.isEmpty(carts)){
       	for(CartVo cart : carts){
       		if(null != cart.getChecked() && 1 == cart.getChecked()){
       			//获取产品配比值
       			GoodsCouponConfigVo goodsCoupon = goodsCouponConfigMapper.getUserCoupons(cart.getGoods_id(),userId);
       			ProductVo productInfo = productService.queryObject(cart.getProduct_id());
       			//计算该产品优惠券总和
       			BigDecimal couponlPrice = BigDecimal.ZERO;//优惠券临时总价值
       			if(goodsCoupon != null){
       				couponlPrice = productInfo.getMarket_price().multiply(new BigDecimal(goodsCoupon.getGood_value())).multiply(new BigDecimal(cart.getNumber()));
       			}
       			couponTotalPrice = couponTotalPrice.add(couponlPrice);
       		}
       	}
        }
        amount = userAmountVo.getAmount();//获取用户平台币
        if(amount.compareTo(couponTotalPrice)<0){
         	couponTotalPrice = amount;
        }


        /**
         * 1.检查当前购物车是否已经生成了优惠券
         * 			1.1  有       更新的优惠券值
         * 			1.2  没有    新增一条
         * */  
        userAmountVo.setAmount(userAmountVo.getAmount().subtract(couponTotalPrice));
        qzUserAccountMapper.updateUserAccount(userAmountVo);
        saveTranInfoRecord(userId, "2", "2", couponTotalPrice, userAmountVo.getAmount(), "回滚平台币后扣减购物车中生成优惠券金额");
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
		     	saveTranInfoRecord(userId, "1", "1", couponTotalPrice, userCouponVo.getCoupon_price(), "原有优惠券作废,重新更新新优惠券");
		     }
			 return this.toResponsObject(0, "优惠券发送成功", "");
	  	
	    }
    
    
    public synchronized Object updateUserCouponPrice(Long userId){

      	   BigDecimal couponTotalPrice = BigDecimal.ZERO;//优惠券总价值
           Map<String,Object> map = new HashMap<>();
           map.put("userId",userId);
           BigDecimal amount = BigDecimal.ZERO;//初始化用户平台币
           QzUserAccountVo userAmountVo =qzUserAccountMapper.queruUserAccountInfo(userId);//查询用户平台币信息
          
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
           if(userCouponVo != null){
          	 //购物车发生修改  原有优惠券作废，重新生成优惠券
          	 userCouponVo.setCoupon_status(7);
          	 apiUserCouponMapper.update(userCouponVo);
          	saveTranInfoRecord(userId, "1", "2", userCouponVo.getCoupon_price(), userCouponVo.getCoupon_price(), "购物车发生修改  原有优惠券作废");
          	 //回滚平台币
          	 userAmountVo.setAmount(userAmountVo.getAmount().add(userCouponVo.getCoupon_price()));
          	 qzUserAccountMapper.updateUserAccount(userAmountVo);
          	saveTranInfoRecord(userId, "2", "1", userCouponVo.getCoupon_price(), userAmountVo.getAmount(), "原有优惠券作废,原优惠券金额回滚到平台币");
           }
           if(!CollectionUtils.isEmpty(carts)){
          	for(CartVo cart : carts){
          		if(null != cart.getChecked() && 1 == cart.getChecked()){
          			//获取产品配比值
          			GoodsCouponConfigVo goodsCoupon = goodsCouponConfigMapper.getUserCoupons(cart.getGoods_id(),userId);
          			ProductVo productInfo = productService.queryObject(cart.getProduct_id());
          			BigDecimal couponlPrice = BigDecimal.ZERO;//优惠券临时总价值
          			//计算该产品优惠券总和
          			if(goodsCoupon != null){
          				couponlPrice = productInfo.getMarket_price().multiply(new BigDecimal(goodsCoupon.getGood_value())).multiply(new BigDecimal(cart.getNumber()));
          			}
          			couponTotalPrice = couponTotalPrice.add(couponlPrice);
          		}
          	}
           }
           
           amount = userAmountVo.getAmount();//获取用户平台币
           if(amount.compareTo(couponTotalPrice)<0){
            	couponTotalPrice = amount;
           }
           userAmountVo.setAmount(userAmountVo.getAmount().add(couponTotalPrice));
           qzUserAccountMapper.updateUserAccount(userAmountVo);
           saveTranInfoRecord(userId, "2", "2", couponTotalPrice, userAmountVo.getAmount(), "回滚平台币后扣减购物车中生成优惠券金额");
           getUserCouponTotalPrice(userId,couponTotalPrice);
           return this.toResponsObject(0, "执行成功", "");
       }
    
    
    
    public boolean roolbackAllCartsCoupons(List<CartVo> carts){
    	if(!CollectionUtils.isEmpty(carts)){
    		for(CartVo cart :carts){
    			try {
    				QzUserAccountVo userAmountVo =qzUserAccountMapper.queruUserAccountInfo(cart.getUser_id());//查询用户平台币信息
    				List<UserCouponVo> userCouponVos = apiUserCouponMapper.queryUserCouponTotalPrice(cart.getUser_id());//查询用户优惠券信息
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
    				
    				if(userCouponVo != null){
    					//获取产品配比值
    					GoodsCouponConfigVo goodsCoupon = goodsCouponConfigMapper.getUserCoupons(cart.getGoods_id(),cart.getUser_id());
    					ProductVo productInfo = productService.queryObject(cart.getProduct_id());
    					BigDecimal couponlPrice = BigDecimal.ZERO;//优惠券临时总价值
    					//计算该产品优惠券总和
    					if(goodsCoupon != null){
    						couponlPrice = productInfo.getMarket_price().multiply(new BigDecimal(goodsCoupon.getGood_value())).multiply(new BigDecimal(cart.getNumber()));
    					}
    					//购物车发生修改  原有优惠券作废，重新生成优惠券
    					userCouponVo.setCoupon_status(1);
    					userCouponVo.setCoupon_price(userCouponVo.getCoupon_price().subtract(couponlPrice));
    					apiUserCouponMapper.update(userCouponVo);
    					saveTranInfoRecord(cart.getUser_id(), "1", "2", couponlPrice, userCouponVo.getCoupon_price(), "商品下架，清空选中购物车商品，优惠券做减");
    					//回滚平台币
    					userAmountVo.setAmount(userAmountVo.getAmount().add(couponlPrice));
    					qzUserAccountMapper.updateUserAccount(userAmountVo);
    					saveTranInfoRecord(cart.getUser_id(), "2", "1", couponlPrice, userAmountVo.getAmount(), "原有优惠券作废,原优惠券金额回滚到平台币");
    				}
				} catch (Exception e) {
					return false;
				}
    		}
    		return true;
    	}
    	return true;
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
  
}
