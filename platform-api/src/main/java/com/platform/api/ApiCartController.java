package com.platform.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.cache.J2CacheUtils;
import com.platform.dao.GoodsCouponConfigMapper;
import com.platform.entity.AddressVo;
import com.platform.entity.BuyGoodsVo;
import com.platform.entity.CartVo;
import com.platform.entity.CouponInfoVo;
import com.platform.entity.CouponVo;
import com.platform.entity.GoodsCouponConfigVo;
import com.platform.entity.GoodsSpecificationVo;
import com.platform.entity.GoodsVo;
import com.platform.entity.ProductVo;
import com.platform.entity.UserVo;
import com.platform.service.ApiAddressService;
import com.platform.service.ApiCartService;
import com.platform.service.ApiCouponService;
import com.platform.service.ApiGoodsService;
import com.platform.service.ApiGoodsSpecificationService;
import com.platform.service.ApiOrderService;
import com.platform.service.ApiProductService;
import com.platform.service.ApiUserService;
import com.platform.util.ApiBaseAction;
import com.platform.util.PayMatchingUtil;
import com.qiniu.util.StringUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.oschina.j2cache.CacheProviderHolder;
import net.oschina.j2cache.Level2Cache;

/**
 * 作者: @author Harmon <br>
 * 时间: 2017-08-11 08:32<br>
 * 描述: ApiIndexController <br>
 */
@Api(tags = "购物车")
@RestController
@RequestMapping("/api/cart")
public class ApiCartController extends ApiBaseAction {
    @Autowired
    private ApiCartService cartService;
    @Autowired
    private ApiGoodsService goodsService;
    @Autowired
    private ApiProductService productService;
    @Autowired
    private ApiGoodsSpecificationService goodsSpecificationService;
    @Autowired
    private ApiAddressService addressService;
    @Autowired
    private ApiCouponService apiCouponService;
    @Autowired
    private PayMatchingUtil payMatchingUtils;
    @Autowired
    private GoodsCouponConfigMapper goodsCouponConfigMapper;
    
    @Autowired
    private ApiUserService userService;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
    /**
     * 获取购物车中的数据
     */
    @ApiOperation(value = "获取购物车中的数据")
    @PostMapping("getCart")
    public Object getCart(@LoginUser UserVo loginUser) {
        Map<String, Object> resultObj = new HashMap();
        //查询列表数据
        Map param = new HashMap();
        param.put("user_id", loginUser.getUserId());
        List<CartVo> cartList = cartService.queryList(param);
        //获取购物车统计信息
        Integer goodsCount = 0;
        BigDecimal goodsAmount = new BigDecimal(0.00);
        Integer checkedGoodsCount = 0;
        BigDecimal checkedGoodsAmount = new BigDecimal(0.00);
        for (CartVo cartItem : cartList) {
            goodsCount += cartItem.getNumber();
            goodsAmount = goodsAmount.add(cartItem.getMarket_price().multiply(new BigDecimal(cartItem.getNumber())));
            if (null != cartItem.getChecked() && 1 == cartItem.getChecked()) {
                checkedGoodsCount += cartItem.getNumber();
                checkedGoodsAmount = checkedGoodsAmount.add(cartItem.getMarket_price().multiply(new BigDecimal(cartItem.getNumber())));
            }
            cartItem.setGood_url("/pages/goods/goods?id=" + cartItem.getGoods_id());
        }
        // 获取优惠信息提示
        Map couponParam = new HashMap();
        couponParam.put("enabled", true);
        Integer[] send_types = new Integer[]{0, 7};
        couponParam.put("send_types", send_types);
        List<CouponInfoVo> couponInfoList = new ArrayList();
        List<CouponVo> couponVos = apiCouponService.queryList(couponParam);
        if (null != couponVos && couponVos.size() > 0) {
            CouponInfoVo fullCutVo = new CouponInfoVo();
            BigDecimal fullCutDec = new BigDecimal(0);
            BigDecimal minAmount = new BigDecimal(100000);
            for (CouponVo couponVo : couponVos) {
                BigDecimal difDec = couponVo.getMin_goods_amount().subtract(checkedGoodsAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
                if (couponVo.getSend_type() == 0 && difDec.doubleValue() > 0.0
                        && minAmount.compareTo(couponVo.getMin_goods_amount()) > 0) {
                    fullCutDec = couponVo.getType_money();
                    minAmount = couponVo.getMin_goods_amount();
                    fullCutVo.setType(1);
                    fullCutVo.setMsg(couponVo.getName() + "，还差" + difDec + "元");
                } else if (couponVo.getSend_type() == 0 && difDec.doubleValue() < 0.0 && fullCutDec.compareTo(couponVo.getType_money()) < 0) {
                    fullCutDec = couponVo.getType_money();
                    fullCutVo.setType(0);
                    fullCutVo.setMsg("可使用满减券" + couponVo.getName());
                }
                if (couponVo.getSend_type() == 7 && difDec.doubleValue() > 0.0) {
                    CouponInfoVo cpVo = new CouponInfoVo();
                    cpVo.setMsg("满￥" + couponVo.getMin_amount() + "元免配送费，还差" + difDec + "元");
                    cpVo.setType(1);
                    couponInfoList.add(cpVo);
                } else if (couponVo.getSend_type() == 7) {
                    CouponInfoVo cpVo = new CouponInfoVo();
                    cpVo.setMsg("满￥" + couponVo.getMin_amount() + "元免配送费");
                    couponInfoList.add(cpVo);
                }
            }
            if (!StringUtils.isNullOrEmpty(fullCutVo.getMsg())) {
                couponInfoList.add(fullCutVo);
            }
        }
        resultObj.put("couponInfoList", couponInfoList);
        resultObj.put("cartList", cartList);
        //
        Map<String, Object> cartTotal = new HashMap();
        cartTotal.put("goodsCount", goodsCount);
        cartTotal.put("goodsAmount", goodsAmount);
        cartTotal.put("checkedGoodsCount", checkedGoodsCount);
        cartTotal.put("checkedGoodsAmount", checkedGoodsAmount);
        //
        resultObj.put("cartTotal", cartTotal);
        return resultObj;
    }

    /**
     * 获取购物车信息，所有对购物车的增删改操作，都要重新返回购物车的信息
     */
    @ApiOperation(value = "获取购物车信息")
    @PostMapping("index")
    public Object index(@LoginUser UserVo loginUser) {
        return toResponsSuccess(getCart(loginUser));
    }

    private String[] getSpecificationIdsArray(String ids) {
        String[] idsArray = null;
        if (org.apache.commons.lang.StringUtils.isNotEmpty(ids)) {
            String[] tempArray = ids.split("_");
            if (null != tempArray && tempArray.length > 0) {
                idsArray = tempArray;
            }
        }
        return idsArray;
    }


    /**
     * 添加商品到购物车
     */
    @ApiOperation(value = "添加商品到购物车")
    @PostMapping("add")
    @Transactional
    public Object add(@LoginUser UserVo loginUser) {
    	if(null == loginUser){
    		return this.toResponsObject(400, "请先登录", "");
    	}
    	
    	//防止重复提交
    	Level2Cache level2 = CacheProviderHolder.getLevel2Cache(J2CacheUtils.INVALID_CACHE);
    	if(null !=level2.get("USER_ADDCART:" + loginUser.getUserId())){
    		return this.toResponsObject(400, "添加成功,已在购物车中等待亲~", "");
    	}
    	level2.put("USER_ADDCART:" + loginUser.getUserId(), loginUser.getUserId(), 1L);

    	JSONObject jsonParam = getJsonRequest();
        Integer goodsId = jsonParam.getInteger("goodsId");
        Integer productId = jsonParam.getInteger("productId");
        Integer number = jsonParam.getInteger("number");

        //判断商品是否可以购买
        GoodsVo goodsInfo = goodsService.queryObject(goodsId);
        if (null == goodsInfo || goodsInfo.getIs_delete() == 1 || goodsInfo.getIs_on_sale() != 1) {
            return this.toResponsObject(400, "商品已下架", "");
        }
        
        //校验毛利率是否大于0,如果否   则不能添加到购物车
        double PureInterestRate = goodsInfo.getMarket_price().subtract(goodsInfo.getRetail_price())
				.divide(goodsInfo.getRetail_price(), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
        if(PureInterestRate <= 0d){
        	return this.toResponsObject(400, "商品已下架,不能添加购物车", "");
        }
        
        //取得规格的信息,判断规格库存
        ProductVo productInfo = productService.queryObject(productId);
        if (null == productInfo || (productInfo.getGoods_number() == null ? 0 : productInfo.getGoods_number()) < number) {
            return this.toResponsObject(400, "库存不足", "");
        }

        //判断购物车中是否存在此规格商品
        
        
//       String  locked   = Long.toString(loginUser.getUserId())+goodsId;
        
        synchronized (this) {
            Map cartParam = new HashMap();
            cartParam.put("goods_id", goodsId);
            cartParam.put("product_id", productId);
            cartParam.put("user_id", loginUser.getUserId());
            List<CartVo> cartInfoList = cartService.queryList(cartParam);
            CartVo cartInfo = null != cartInfoList && cartInfoList.size() > 0 ? cartInfoList.get(0) : null;
			
            if (null == cartInfo) {
                //添加操作
                //添加规格名和值
                String[] goodsSepcifitionValue = null;
                if (null != productInfo.getGoods_specification_ids() && productInfo.getGoods_specification_ids().length() > 0) {
                    Map specificationParam = new HashMap();
                    String[] idsArray = getSpecificationIdsArray(productInfo.getGoods_specification_ids());
                    specificationParam.put("ids", idsArray);
                    specificationParam.put("goods_id", goodsId);
                    List<GoodsSpecificationVo> specificationEntities = goodsSpecificationService.queryList(specificationParam);
                    goodsSepcifitionValue = new String[specificationEntities.size()];
                    for (int i = 0; i < specificationEntities.size(); i++) {
                        goodsSepcifitionValue[i] = specificationEntities.get(i).getValue();
                    }
                }
                cartInfo = new CartVo();
                cartInfo.setGoods_id(goodsId);
                cartInfo.setProduct_id(productId);
                cartInfo.setGoods_sn(productInfo.getGoods_sn());
                cartInfo.setGoods_name(goodsInfo.getName());
                cartInfo.setList_pic_url(goodsInfo.getList_pic_url());
                cartInfo.setNumber(number);
                cartInfo.setSession_id("1");
                cartInfo.setUser_id(loginUser.getUserId());
                cartInfo.setRetail_price(productInfo.getRetail_price());
                cartInfo.setMarket_price(productInfo.getMarket_price());
                if (null != goodsSepcifitionValue) {
                    cartInfo.setGoods_specifition_name_value(StringUtils.join(goodsSepcifitionValue, ";"));
                }
                cartInfo.setGoods_specifition_ids(productInfo.getGoods_specification_ids());
                cartInfo.setChecked(1);
                cartService.save(cartInfo);
            } else {
                //如果已经存在购物车中，则数量增加
                if ((productInfo.getGoods_number() == null ? 0 : productInfo.getGoods_number()) < (number + cartInfo.getNumber())) {
                    return this.toResponsObject(400, "库存不足", "");
                }
                cartInfo.setNumber(cartInfo.getNumber() + number);
                cartService.update(cartInfo);
            }
		}
        
        return toResponsSuccess(getCart(loginUser));
    }
 
    
    
    /**
     * 减少商品到购物车
     */
    @ApiOperation(value = "减少商品到购物车")
    @PostMapping("minus")
    @Transactional
    public Object minus(@LoginUser UserVo loginUser) {
        JSONObject jsonParam = getJsonRequest();
        Integer goodsId = jsonParam.getInteger("goodsId");
        Integer productId = jsonParam.getInteger("productId");
        Integer number = jsonParam.getInteger("number");
        //判断购物车中是否存在此规格商品
        Map cartParam = new HashMap();
        cartParam.put("goods_id", goodsId);
        cartParam.put("product_id", productId);
        cartParam.put("user_id", loginUser.getUserId());
        List<CartVo> cartInfoList = cartService.queryList(cartParam);
        
        Map<String, Object> param = new HashMap<>();
        CartVo cartInfo = null != cartInfoList && cartInfoList.size() > 0 ? cartInfoList.get(0) : null;
        int cart_num = 0;
        if (null != cartInfo) {
            if (cartInfo.getNumber() > number) {
            	Integer num = cartInfo.getNumber() - number;
                cartInfo.setNumber(num);
                cartService.update(cartInfo);
                cart_num = cartInfo.getNumber();
            } else if (cartInfo.getNumber() == 1) {
                cartService.delete(cartInfo.getId());
                param.put("user_id", loginUser.getUserId());
                cart_num = 0;
            }
        }
        return toResponsSuccess(cart_num);
    }

    /**
     * 更新指定的购物车信息
     */
    @ApiOperation(value = "更新指定的购物车信息")
    @PostMapping("update")
    @Transactional
    public Object update(@LoginUser UserVo loginUser) {
        JSONObject jsonParam = getJsonRequest();
        Integer goodsId = jsonParam.getInteger("goodsId");
        Integer productId = jsonParam.getInteger("productId");
        Integer number = jsonParam.getInteger("number");
        Integer id = jsonParam.getInteger("id");
        Map<String,Object> param = new HashMap<>();
        //取得规格的信息,判断规格库存
        ProductVo productInfo = productService.queryObject(productId);
        if (null == productInfo || (productInfo.getGoods_number() == null ? 0 : productInfo.getGoods_number()) < number) {
            return this.toResponsObject(400, "库存不足", "");
        }
        
        CartVo cartInfo = cartService.queryObject(id);
        //只是更新number
        if (cartInfo.getProduct_id().equals(productId)) {
            cartInfo.setNumber(number);
            cartService.update(cartInfo);
            param.put("user_id", loginUser.getUserId());
            return toResponsSuccess(getCart(loginUser));
        }
        Map cartParam = new HashMap();
        cartParam.put("goodsId", goodsId);
        cartParam.put("productId", productId);
        List<CartVo> cartInfoList = cartService.queryList(cartParam);
        CartVo newcartInfo = null != cartInfoList && cartInfoList.size() > 0 ? cartInfoList.get(0) : null;
        if (null == newcartInfo) {
            //添加操作
            //添加规格名和值
            String[] goodsSepcifitionValue = null;
            if (null != productInfo.getGoods_specification_ids()) {
                Map specificationParam = new HashMap();
                specificationParam.put("ids", productInfo.getGoods_specification_ids());
                specificationParam.put("goodsId", goodsId);
                List<GoodsSpecificationVo> specificationEntities = goodsSpecificationService.queryList(specificationParam);
                goodsSepcifitionValue = new String[specificationEntities.size()];
                for (int i = 0; i < specificationEntities.size(); i++) {
                    goodsSepcifitionValue[i] = specificationEntities.get(i).getValue();
                }
            }
            cartInfo.setProduct_id(productId);
            cartInfo.setGoods_sn(productInfo.getGoods_sn());
            cartInfo.setNumber(number);
            cartInfo.setRetail_price(productInfo.getRetail_price());
            cartInfo.setMarket_price(productInfo.getMarket_price());
            if (null != goodsSepcifitionValue) {
                cartInfo.setGoods_specifition_name_value(StringUtils.join(goodsSepcifitionValue, ";"));
            }
            cartInfo.setGoods_specifition_ids(productInfo.getGoods_specification_ids());
            cartService.update(cartInfo);
        } else {
            //合并购物车已有的product信息，删除已有的数据
            Integer newNumber = number + newcartInfo.getNumber();
            if (null == productInfo || (productInfo.getGoods_number() == null ? 0 : productInfo.getGoods_number()) < newNumber) {
                return this.toResponsObject(400, "库存不足", "");
            }
            cartService.delete(newcartInfo.getId());
            param.put("user_id", loginUser.getUserId());
            //添加规格名和值
            String[] goodsSepcifitionValue = null;
            if (null != productInfo.getGoods_specification_ids()) {
                Map specificationParam = new HashMap();
                specificationParam.put("ids", productInfo.getGoods_specification_ids());
                specificationParam.put("goodsId", goodsId);
                List<GoodsSpecificationVo> specificationEntities = goodsSpecificationService.queryList(specificationParam);
                goodsSepcifitionValue = new String[specificationEntities.size()];
                for (int i = 0; i < specificationEntities.size(); i++) {
                    goodsSepcifitionValue[i] = specificationEntities.get(i).getValue();
                }
            }
            cartInfo.setProduct_id(productId);
            cartInfo.setGoods_sn(productInfo.getGoods_sn());
            cartInfo.setNumber(number);
            cartInfo.setRetail_price(productInfo.getRetail_price());
            cartInfo.setMarket_price(productInfo.getMarket_price());
            if (null != goodsSepcifitionValue) {
                cartInfo.setGoods_specifition_name_value(StringUtils.join(goodsSepcifitionValue, ";"));
            }
            cartInfo.setGoods_specifition_ids(productInfo.getGoods_specification_ids());
            cartService.update(cartInfo);
        }
        return toResponsSuccess(getCart(loginUser));
    }
    /**
     * 是否选择商品，如果已经选择，则取消选择，批量操作
     */
    @ApiOperation(value = "是否选择商品")
    @PostMapping("checked")
    public Object checked(@LoginUser UserVo loginUser) {
        JSONObject jsonParam = getJsonRequest();
        String productIds = jsonParam.getString("productIds");
        Integer isChecked = jsonParam.getInteger("isChecked");
        if (StringUtils.isNullOrEmpty(productIds)) {
            return this.toResponsFail("删除出错");
        }
        String[] productIdArray = productIds.split(",");
        cartService.updateCheck(productIdArray, isChecked, loginUser.getUserId());
        return toResponsSuccess(getCart(loginUser));
    }

    //删除选中的购物车商品，批量删除
    @ApiOperation(value = "删除商品")
    @PostMapping("delete")
    public Object delete(@LoginUser UserVo loginUser) {
        Long userId = loginUser.getUserId();

        JSONObject jsonObject = getJsonRequest();
        String productIds = jsonObject.getString("productIds");

        if (StringUtils.isNullOrEmpty(productIds)) {
            return toResponsFail("删除出错");
        }
        String[] productIdsArray = productIds.split(",");
        cartService.deleteByUserAndProductIds(userId, productIdsArray);
        return toResponsSuccess(getCart(loginUser));
    }

    //  获取购物车商品的总件件数
    @ApiOperation(value = "获取购物车商品的总件件数")
    @IgnoreAuth
    @PostMapping("goodscount")
    public Object goodscount(@LoginUser UserVo loginUser) {
    	Map<String, Object> resultObj = new HashMap();
    	Long userId = getUserId();
    	if(null == userId){ //用户未登录，默认展示购物车数量为0
    		resultObj.put("cartList", null);
    		resultObj.put("cartTotal", 0);
    		 return toResponsSuccess(resultObj);
    	}
    	
        /*if (null == loginUser || null == loginUser.getUserId()) {
            return toResponsFail("未登录");
        }*/
    	loginUser = userService.queryObject(userId);
    	if(null == loginUser){
    		throw new RuntimeException("【商品详情页查询用户购物车数量】未查询带用户id为"+userId+"的用户");
    	}
        //查询列表数据
        Map param = new HashMap();
        param.put("user_id", loginUser.getUserId());
        List<CartVo> cartList = cartService.queryList(param);
        //获取购物车统计信息
        Integer goodsCount = 0;
        for (CartVo cartItem : cartList) {
            goodsCount += cartItem.getNumber();
        }
        resultObj.put("cartList", cartList);
        //
        Map<String, Object> cartTotal = new HashMap();
        cartTotal.put("goodsCount", goodsCount);
        //
        resultObj.put("cartTotal", cartTotal);
        return toResponsSuccess(resultObj);
    }

    /**
     * 订单提交前的检验和填写相关订单信息
     */
    @ApiOperation(value = "订单提交前的检验和填写相关订单信息")
    @PostMapping("checkout")
    public Object checkout(@LoginUser UserVo loginUser, Integer addressId,Integer couponId, @RequestParam(defaultValue = "cart") String type) {
        Map<String, Object> resultObj = new HashMap();
        //根据收货地址计算运费
        
        JSONObject jsonParam = getJsonRequest();
        
        if(!StringUtils.isNullOrEmpty(jsonParam.getString("type"))){
        	type = jsonParam.getString("type");
        }
        BigDecimal freightPrice = new BigDecimal(0.00);
        //默认收货地址
        if(!StringUtils.isNullOrEmpty(jsonParam.getString("addressId"))){
        	addressId = Integer.parseInt(jsonParam.getString("addressId"));
        }
        AddressVo addVo = addressService.queryObject(addressId);
        Map<String,Object> map = new HashMap<>();
        if(null == addVo){
        	resultObj.put("checkedAddress", new AddressVo());
        }else{
        	 resultObj.put("checkedAddress", addVo);
        }
        // * 获取要购买的商品和总价
        ArrayList checkedGoodsList = new ArrayList();
        BigDecimal goodsTotalPrice = BigDecimal.ZERO;
        if (type.equals("cart")) {
            Map<String, Object> cartData = (Map<String, Object>) this.getCart(loginUser);

            for (CartVo cartEntity : (List<CartVo>) cartData.get("cartList")) {
                if (cartEntity.getChecked() == 1) {
                    checkedGoodsList.add(cartEntity);
                }
            }
            goodsTotalPrice = (BigDecimal) ((HashMap) cartData.get("cartTotal")).get("checkedGoodsAmount");
        } else { // 是直接购买的
            BuyGoodsVo goodsVO = (BuyGoodsVo) J2CacheUtils.get(J2CacheUtils.SHOP_CACHE_NAME, "goods" + loginUser.getUserId() + "");
            if(goodsVO != null){
            	ProductVo productInfo = productService.queryObject(goodsVO.getProductId());
            	//计算订单的费用
            	//商品总价
            	goodsTotalPrice = productInfo.getMarket_price().multiply(new BigDecimal(goodsVO.getNumber()));
            	
            	CartVo cartVo = new CartVo();
            	cartVo.setGoods_name(productInfo.getGoods_name());
            	cartVo.setNumber(goodsVO.getNumber());
            	cartVo.setRetail_price(productInfo.getRetail_price());
            	cartVo.setMarket_price(productInfo.getMarket_price());
            	cartVo.setList_pic_url(productInfo.getList_pic_url());
            	checkedGoodsList.add(cartVo);
            }
        }

        //获取可用的优惠券信息
        BigDecimal couponPrice = new BigDecimal(0.00);
        
        map.put("user_id", loginUser.getUserId());
        map.put("coupon_status", 1);
        List<CouponVo> couponVos = apiCouponService.queryUserCoupons(map);
        if(!CollectionUtils.isEmpty(couponVos)){
        	for(CouponVo coupon : couponVos){
        		if("平台抵扣券".equals(coupon.getName())){
        			 couponPrice = couponPrice.add(coupon.getCoupon_price());
        		}
        	}
        }
        BigDecimal couponAmount = BigDecimal.ZERO;
        //平台币总额
        BigDecimal userAmount = cartService.queryUserAccountAmount(loginUser.getUserId()); 
        //购物车勾选商品 总额
        if(userAmount != null){
        	BigDecimal disCountAmount = cartService.queryUserDisCountAmount(checkedGoodsList);
        	if(userAmount.compareTo(disCountAmount) > 0 || userAmount.compareTo(disCountAmount) == 0){
        		resultObj.put("ampleAmountFlag", true);
        		couponAmount = disCountAmount;
        	}
        	if(userAmount.compareTo(disCountAmount) < 0){
        		BigDecimal amount = goodsMaxDiscount(checkedGoodsList, userAmount);
        		BigDecimal usableAmount = disCountAmount.subtract(amount);//还可支付平台币金额
        		BigDecimal residueAmount = userAmount.subtract(amount);//剩余平台币金额
        		resultObj.put("usableAmount", usableAmount);
        		resultObj.put("residueAmount", residueAmount);
        		resultObj.put("ampleAmountFlag", false);
        		couponAmount = amount;
        	}
        }
        //计算快递费
    	if (goodsTotalPrice.compareTo(new BigDecimal("49")) <=0) { //(0-49]  8元运费
    		freightPrice = new BigDecimal("8");
		}else if (goodsTotalPrice.compareTo(new BigDecimal("99")) <=0) { //(49-99]  6元运费
			freightPrice = new BigDecimal("6");
		}
    	
        //订单的总价
        BigDecimal orderTotalPrice = goodsTotalPrice.add(freightPrice);
        BigDecimal actualPrice = orderTotalPrice.subtract(couponPrice).subtract(couponAmount);  //减去其它支付的金额后，要实际支付的金额
        
        //校验商品是否有库存,是否下架
        JSONObject resObj = cartService.queryJdGoodsStatus(checkedGoodsList,addressId);
        
        resultObj.put("unSaleCarts", resObj.get("unSaleCarts"));//下架商品
        resultObj.put("unStoreCarts", resObj.get("unStoreCarts"));//无库存商品
        resultObj.put("disCountAmount", couponAmount);//优惠金额
        resultObj.put("userAmount", userAmount);
        resultObj.put("freightPrice", freightPrice);  //配送费
        resultObj.put("couponPrice", couponPrice);
        resultObj.put("checkedGoodsList", checkedGoodsList);
        resultObj.put("goodsTotalPrice", goodsTotalPrice);
        resultObj.put("orderTotalPrice", orderTotalPrice);
        resultObj.put("actualPrice", actualPrice);
        return toResponsSuccess(resultObj);
    }

    /**
     * 选择优惠券列表
     */
    @ApiOperation(value = "选择优惠券列表")
    @PostMapping("checkedCouponList")
    public Object checkedCouponList(@LoginUser UserVo loginUser) {
        //
        Map param = new HashMap();
        param.put("user_id", loginUser.getUserId());
        List<CouponVo> couponVos = apiCouponService.queryUserCouponList(param);
        if (null != couponVos && couponVos.size() > 0) {
            // 获取要购买的商品
            Map<String, Object> cartData = (Map<String, Object>) this.getCart(loginUser);
            List<CartVo> checkedGoodsList = new ArrayList();
            List<Integer> checkedGoodsIds = new ArrayList();
            for (CartVo cartEntity : (List<CartVo>) cartData.get("cartList")) {
                if (cartEntity.getChecked() == 1) {
                    checkedGoodsList.add(cartEntity);
                    checkedGoodsIds.add(cartEntity.getId());
                }
            }
            // 计算订单的费用
            BigDecimal goodsTotalPrice = (BigDecimal) ((HashMap) cartData.get("cartTotal")).get("checkedGoodsAmount");  //商品总价
            // 如果没有用户优惠券直接返回新用户优惠券
            for (CouponVo couponVo : couponVos) {
                if (couponVo.getMin_amount().compareTo(goodsTotalPrice) <= 0) {
                    couponVo.setEnabled(1);
                }
            }
            }
        return toResponsSuccess(couponVos);
    }
    /**
	 * 平台币金额小于总优惠金额，优惠金额最大化
	 * @param carts
	 * @param userAccount
	 * @return
	 */
	public BigDecimal goodsMaxDiscount(List<CartVo> carts,BigDecimal userAccount){
		BigDecimal maxDiscount = BigDecimal.ZERO;
		List<BigDecimal> amounts = new ArrayList<>();
		if(CollectionUtils.isEmpty(carts)){
			return maxDiscount;
		}
		for(CartVo cart : carts){
			//获取产品配比值
			GoodsCouponConfigVo goodsCoupon = goodsCouponConfigMapper.getUserCoupons(cart.getGoods_id(),cart.getUser_id());
			//计算该产品优惠券总和
			if(goodsCoupon != null){
				BigDecimal payMatching = BigDecimal.ZERO;
    			if(payMatchingUtils.getPayMatching(cart.getProduct_id())!= null){
    				Object value = payMatchingUtils.getPayMatching(cart.getProduct_id()).get(cart.getGoods_id());
    				if(value != null){
    					payMatching = new BigDecimal(value.toString());
    				}
    			}
    			for(int j = 0;j<cart.getNumber();j++){
    				amounts.add(payMatching);
    			}
			}
			
		}
		Collections.sort(amounts);
		for(int i = amounts.size()-1;i>= 0;i--){
			userAccount = userAccount.subtract(amounts.get(i));
			if(userAccount.compareTo(BigDecimal.ZERO) > 0){
				maxDiscount = maxDiscount.add(amounts.get(i));
				continue;
			}
			if(userAccount.compareTo(BigDecimal.ZERO) == 0){
				maxDiscount = maxDiscount.add(amounts.get(i));
				break;
			}
			if(userAccount.compareTo(BigDecimal.ZERO) < 0){
				userAccount = userAccount.add(amounts.get(i));
				continue;
			}
		}
		return maxDiscount;
	}
    
}
