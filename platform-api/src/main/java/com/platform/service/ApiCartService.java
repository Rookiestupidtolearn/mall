package com.platform.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.platform.dao.ApiAddressMapper;
import com.platform.dao.ApiCartMapper;
import com.platform.dao.GoodsCouponConfigMapper;
import com.platform.dao.QzUserAccountMapper;
import com.platform.entity.AddressVo;
import com.platform.entity.CartVo;
import com.platform.entity.GoodsCouponConfigVo;
import com.platform.entity.GoodsVo;
import com.platform.entity.QzUserAccountVo;
import com.platform.util.PayMatchingUtil;

import jline.internal.Log;

@Service
public class ApiCartService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private ApiCartMapper cartDao;
	@Autowired
	private ApiCartMapper cartMapper;
	@Autowired
	private ApiCartMapper apiCartMapper;
	@Autowired
	private QzUserAccountMapper apiUserAccountMapper;
    @Autowired
    private PayMatchingUtil payMatchingUtils;
    @Autowired
    private GoodsCouponConfigMapper goodsCouponConfigMapper;
	@Autowired
	private ApiGoodsService apiGoodsService;
	@Autowired
	private JdOrderService jdOrderService;
	@Autowired
	private ApiAddressMapper apiAddressMapper;
	// 查询库存默认地址
	private String DEFAULT_ADDRESS = "1_72_2799";

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

	@Transactional
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
						if (null != cartItem.getChecked() && 1 == cartItem.getChecked()
								&& !cartCrash.getId().equals(cartItem.getId())) {
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
		}
	}

	public void deleteByProductIds(String[] productIds) {
		cartDao.deleteByProductIds(productIds);
	}

	@Transactional
	public void deleteByUserAndProductIds(Long userId, String[] productIds) {
		logger.info("【购物车删除商品开始】用户id" + userId);
		List<String> products = new ArrayList<>();
		Map<String, Object> param = new HashMap<String, Object>();
		if (productIds != null && productIds.length > 0) {
			for (int i = 0; i < productIds.length; i++) {
				if (!products.contains(productIds[i])) {
					products.add(productIds[i]);
				}
			}
		}
		if (!CollectionUtils.isEmpty(products)) {
			for (int i = 0; i < products.size(); i++) {
				param.put("product_id", products.get(i));
				param.put("user_id", userId);
				List<CartVo> cart = cartMapper.getCarts(param);
				if (!CollectionUtils.isEmpty(cart)) {
					cartMapper.delete(cart.get(0).getId());
				}
			}
		}
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
	 * @throws @Description:构建统一格式返回对象
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
	 * @param CartEntityIds
	 * @param type
	 *            --> 0:商城后台操作下架 1:毛利率任务自动下架jd商品 2:获取jd商品任务下架3定时任务扫描购物车商品下架
	 * @return
	 */
	public boolean roolbackAllCartsCoupons(Integer[] CartEntityIds, Integer type) {
		if (CartEntityIds.length < 1) {
			Log.info("【回滚平台币并删除优惠券】购物车id为空");
			return false;
		}
		List<CartVo> carts = apiCartMapper.queryCartsByCartId(CartEntityIds);
		if (!CollectionUtils.isEmpty(carts)) {
			for (CartVo cart : carts) {
				try {
//					rollbackUserCouponPrice(cart.getGoods_id(), cart.getNumber(), cart.getUser_id(), CartEntityIds,
//							type);
				} catch (Exception e) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	/**
	 * 查询用户平台币余额
	 * @param userId
	 * @return
	 */
	public BigDecimal queryUserAccountAmount(Long userId){
		BigDecimal userAccountAmount = BigDecimal.ZERO;
		QzUserAccountVo userAccount = apiUserAccountMapper.queruUserAccountInfo(userId);
		if(userAccount != null){
			userAccountAmount = userAccount.getAmount();
		}
		return userAccountAmount;
	}
	
	/**
	 * 查询购物车所选商品总优惠价格
	 * @param carts
	 * @return
	 */
	public BigDecimal queryUserDisCountAmount(List<CartVo> carts){
		BigDecimal disCountAmount = BigDecimal.ZERO;
		if(CollectionUtils.isEmpty(carts)){
			return disCountAmount;
		}
		for(CartVo cart : carts){
			//获取产品配比值
			GoodsCouponConfigVo goodsCoupon = goodsCouponConfigMapper.getUserCoupons(cart.getGoods_id(),cart.getUser_id());
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
			disCountAmount = disCountAmount.add(couponlPrice);
		}
		return disCountAmount;
	}
	
	/**
	 * 轮询购物车选中商品三方是否下架
	 * @param carts
	 * @return
	 */
	public JSONObject queryJdGoodsStatus(List<CartVo> carts,Integer addressId) {
		JSONObject resultObj = new JSONObject();
		// 三方下架的商品
		List<CartVo> unSaleCarts = new ArrayList<>();
		// 三方无库存的商品
		List<CartVo> unStoreCarts = new ArrayList<>();
		AddressVo addressVo = apiAddressMapper.queryObject(addressId);
		String address = "";
		
		if(addressVo == null){
			address = DEFAULT_ADDRESS;
		}else{
			address = addressVo.getProvince() + "_" + addressVo.getCity() + "_" + addressVo.getCounty();
		}
		if (!CollectionUtils.isEmpty(carts)) {
			
			for (CartVo cart : carts) {
				GoodsVo goods = apiGoodsService.queryObject(cart.getGoods_id());
				if (goods == null) {
					continue;
				}
				// 判断是三方的还是自己的产品
				String source = goods.getSource();
				if (source.equals("JD")) {
					// 检验库存+上下架状态
					String pid = goods.getGoods_sn().substring(2, goods.getGoods_sn().length());
					// 库存
					Map<String, Object> stockMap = jdOrderService.checkStockSingle(pid, cart.getNumber(), address);
					if (!stockMap.get("code").equals("200")) {
						resultObj.put("errno", "100");
						resultObj.put("errmsg", "不可出售");
						Integer[] arr1 = { cart.getGoods_id() };
						apiGoodsService.unSaleBatch(arr1, 3);
						unStoreCarts.add(cart);
						continue;
					}
					// 上下架状态
					Map<String, Object> saleStatusMap = jdOrderService.checkSaleStatusSingle(Integer.parseInt(pid));
					if (!saleStatusMap.get("code").equals("200")) {
						resultObj.put("errno", "100");
						resultObj.put("errmsg", "不可出售");
						Integer[] arr1 = { cart.getGoods_id() };
						apiGoodsService.unSaleBatch(arr1, 3);
						unSaleCarts.add(cart);
						continue;
					}
				}
				if (source.equals("system")) {
					// 校验自己的库存和上下架状态
					if (goods.getGoods_number() < cart.getNumber()) {
						logger.info(
								"【系统商品库存不足无法正常下单】商品id:" + cart.getGoods_id() + "剩余库存：" + goods.getGoods_number());
						unStoreCarts.add(cart);
						continue;
					}
					if (goods.getIs_on_sale() == 0) {
						logger.info("【系统商品已经下架无法正常下单】商品上下架状态为：" + goods.getIs_on_sale());
						unSaleCarts.add(cart);
						continue;
					}

				}

			}
			resultObj.put("unSaleCarts", unSaleCarts);
			resultObj.put("unStoreCarts", unStoreCarts);
		}

		return resultObj;
	}
	
	
}
