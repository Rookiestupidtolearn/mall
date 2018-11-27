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
import com.platform.util.ApiUpdateUserCouponPriceUtils;
import com.platform.util.PayMatchingUtil;

import jline.internal.Log;

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
	private ApiCartMapper cartMapper;
	@Autowired
	private QzUserAccountMapper qzUserAccountMapper;
	@Autowired
	private ApiCartMapper apiCartMapper;
	@Autowired
	private PayMatchingUtil payMatchingUtils;
	@Autowired
	private ApiUpdateUserCouponPriceUtils apiUpdateUserCouponPriceUtils;

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
			apiUpdateUserCouponPriceUtils.updateUserCouponPrice(cartUpdateList.get(0).getGoods_id(),
					Integer.parseInt(productIds[0]), cartUpdateList.get(0).getNumber(),
					cartUpdateList.get(0).getUser_id(), 4);
		} else {
			updateUserCouponPrice(userId);
		}
	}

	public void deleteByProductIds(String[] productIds) {
		cartDao.deleteByProductIds(productIds);
	}

	@Transactional
	public void deleteByUserAndProductIds(Long userId, String[] productIds) {
		logger.info("【购物车删除商品开始】用户id" + userId);
		BigDecimal couponTotalPriceAbstract = BigDecimal.ZERO;//理论优惠券价格
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
		QzUserAccountVo userAmountVo = qzUserAccountMapper.queruUserAccountInfo(userId);// 查询用户平台币信息
		BigDecimal couponTotalPrice = BigDecimal.ZERO;// 优惠券总价值

		BigDecimal amount = BigDecimal.ZERO;// 初始化用户平台币
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
			if (userAmountVo != null) {
				if (userCouponVo != null) {
					 List<UserCouponVo> userCoupons = apiUserCouponMapper.querySubCoupons(userCouponVo.getId());
	            	 if(!CollectionUtils.isEmpty(userCoupons)){
	            		 for(UserCouponVo vo :userCoupons){
	            			 vo.setCoupon_status(7);
	            			 apiUserCouponMapper.update(vo);
	            		 }
	            	 }
					// 购物车发生修改 原有优惠券作废，重新生成优惠券
					userCouponVo.setCoupon_status(7);
					logger.info("【更新用户优惠券】原有优惠券作废，原有优惠券金额" + userCouponVo.getCoupon_price() + "原用户平台币"
							+ userAmountVo.getAmount());
					apiUserCouponMapper.update(userCouponVo);

					apiUpdateUserCouponPriceUtils.saveTranInfoRecord(userId, "1", "2", userCouponVo.getCoupon_price(),
							userCouponVo.getCoupon_price(), "【购物车删除商品】原有优惠券作废");// 回滚平台币
					userAmountVo.setAmount(userAmountVo.getAmount().add(userCouponVo.getCoupon_price()));
					qzUserAccountMapper.updateUserAccount(userAmountVo);

					apiUpdateUserCouponPriceUtils.saveTranInfoRecord(userId, "2", "1", userCouponVo.getCoupon_price(),
							userAmountVo.getAmount(), "【购物车删除商品】原有优惠券作废,原优惠券金额回滚到平台币");
					logger.info("更新用户平台币,更新后平台币金额" + userAmountVo.getAmount());
				}
			}
		}
		if (userAmountVo != null) {
			// 查询新购物车信息
			List<CartVo> carts = apiCartMapper.queryUserCarts(userId);
			if (!CollectionUtils.isEmpty(carts)) {
				for (CartVo cart : carts) {
					if (null != cart.getChecked() && 1 == cart.getChecked()) {
						// 获取产品配比值
						GoodsCouponConfigVo goodsCoupon = goodsCouponConfigMapper.getUserCoupons(cart.getGoods_id(),
								userId);
						BigDecimal couponlPrice = BigDecimal.ZERO;// 优惠券临时总价值
						// 计算该产品优惠券总和
						if (goodsCoupon != null) {
							BigDecimal payMatching = BigDecimal.ZERO;
							if (payMatchingUtils.getPayMatching(cart.getProduct_id()) != null) {
								Object value = payMatchingUtils.getPayMatching(cart.getProduct_id())
										.get(cart.getGoods_id());
								if (value != null) {
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
			amount = userAmountVo.getAmount();// 获取用户平台币
			if (amount.compareTo(couponTotalPrice) < 0) {
				couponTotalPrice = amount;
			}
			userAmountVo.setAmount(userAmountVo.getAmount().subtract(couponTotalPrice));
			qzUserAccountMapper.updateUserAccount(userAmountVo);

			apiUpdateUserCouponPriceUtils.saveTranInfoRecord(userId, "2", "2", couponTotalPrice,
					userAmountVo.getAmount(), "【购物车删除商品】回滚平台币后扣减购物车中生成优惠券金额");

			apiUpdateUserCouponPriceUtils.getUserCouponTotalPrice(userId, couponTotalPrice, 5,couponTotalPriceAbstract);

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

	public synchronized Object updateUserCouponPrice(Long userId) {
		BigDecimal couponTotalPriceAbstract = BigDecimal.ZERO;//理论优惠券价格
		BigDecimal couponTotalPrice = BigDecimal.ZERO;// 优惠券总价值
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		BigDecimal amount = BigDecimal.ZERO;// 初始化用户平台币
		QzUserAccountVo userAmountVo = qzUserAccountMapper.queruUserAccountInfo(userId);// 查询用户平台币信息
		if (userAmountVo != null) {
			List<UserCouponVo> userCouponVos = apiUserCouponMapper.queryUserCouponTotalPrice(userId);// 查询用户优惠券信息
			List<UserCouponVo> coupons = new ArrayList<>();
			List<CartVo> carts = apiCartMapper.queryUserCarts(userId);
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
			if (userCouponVo != null) {
				List<UserCouponVo> userCoupons = apiUserCouponMapper.querySubCoupons(userCouponVo.getId());
           	 	if(!CollectionUtils.isEmpty(userCoupons)){
           		 for(UserCouponVo vo :userCoupons){
           			 vo.setCoupon_status(7);
           			 apiUserCouponMapper.update(vo);
           		 }
           	 }
				// 购物车发生修改 原有优惠券作废，重新生成优惠券
				userCouponVo.setCoupon_status(7);
				apiUserCouponMapper.update(userCouponVo);
				apiUpdateUserCouponPriceUtils.saveTranInfoRecord(userId, "1", "2", userCouponVo.getCoupon_price(),
						userCouponVo.getCoupon_price(), "【是否选择商品】购物车发生修改  原有优惠券作废");
				// 回滚平台币
				userAmountVo.setAmount(userAmountVo.getAmount().add(userCouponVo.getCoupon_price()));
				qzUserAccountMapper.updateUserAccount(userAmountVo);
				apiUpdateUserCouponPriceUtils.saveTranInfoRecord(userId, "2", "1", userCouponVo.getCoupon_price(),
						userAmountVo.getAmount(), "【是否选择商品】原有优惠券作废,原优惠券金额回滚到平台币");
			}
			if (!CollectionUtils.isEmpty(carts)) {
				for (CartVo cart : carts) {
					if (null != cart.getChecked() && 1 == cart.getChecked()) {
						// 获取产品配比值
						GoodsCouponConfigVo goodsCoupon = goodsCouponConfigMapper.getUserCoupons(cart.getGoods_id(),
								userId);
						BigDecimal couponlPrice = BigDecimal.ZERO;// 优惠券临时总价值
						// 计算该产品优惠券总和
						if (goodsCoupon != null) {
							BigDecimal payMatching = BigDecimal.ZERO;
							if (payMatchingUtils.getPayMatching(cart.getProduct_id()) != null) {
								Object value = payMatchingUtils.getPayMatching(cart.getProduct_id())
										.get(cart.getGoods_id());
								if (value != null) {
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

			amount = userAmountVo.getAmount();// 获取用户平台币
			if (amount.compareTo(couponTotalPrice) < 0) {
				couponTotalPrice = amount;
			}
			userAmountVo.setAmount(userAmountVo.getAmount().add(couponTotalPrice));
			qzUserAccountMapper.updateUserAccount(userAmountVo);
			apiUpdateUserCouponPriceUtils.saveTranInfoRecord(userId, "2", "2", couponTotalPrice,
					userAmountVo.getAmount(), "【是否选择商品】回滚平台币后扣减购物车中生成优惠券金额");
			apiUpdateUserCouponPriceUtils.getUserCouponTotalPrice(userId, couponTotalPrice, 4,couponTotalPriceAbstract);
		}
		return this.toResponsObject(0, "执行成功", "");
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
					rollbackUserCouponPrice(cart.getGoods_id(), cart.getNumber(), cart.getUser_id(), CartEntityIds,
							type);
				} catch (Exception e) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public synchronized Object rollbackUserCouponPrice(Integer goodsId, Integer number, Long userId,
			Integer[] CartEntityIds, Integer type) {
		BigDecimal couponTotalPrice = BigDecimal.ZERO;// 优惠券总价值
		BigDecimal couponTotalPriceAbstract = BigDecimal.ZERO;//理论优惠券价格
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		BigDecimal amount = BigDecimal.ZERO;// 初始化用户平台币
		logger.info("【更新用户优惠券开始】,用户id" + userId);
		QzUserAccountVo userAmountVo = qzUserAccountMapper.queruUserAccountInfo(userId);// 查询用户平台币信息

		if (userAmountVo != null) {
			List<UserCouponVo> userCouponVos = apiUserCouponMapper.queryUserCouponTotalPrice(userId);// 查询用户优惠券信息
			List<UserCouponVo> coupons = new ArrayList<>();
			List<CartVo> carts = apiCartMapper.queryRollbackUserCarts(CartEntityIds, userId);
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
			if (userCouponVo != null && userAmountVo != null) {
				List<UserCouponVo> userCoupons = apiUserCouponMapper.querySubCoupons(userCouponVo.getId());
           	 if(!CollectionUtils.isEmpty(userCoupons)){
           		 for(UserCouponVo vo :userCoupons){
           			 vo.setCoupon_status(7);
           			 apiUserCouponMapper.update(vo);
           		 }
           	 }
				// 购物车发生修改 原有优惠券作废，重新生成优惠券
				userCouponVo.setCoupon_status(7);
				apiUserCouponMapper.update(userCouponVo);
				if (type == 0) {
					apiUpdateUserCouponPriceUtils.saveTranInfoRecord(userId, "1", "2", userCouponVo.getCoupon_price(),
							userCouponVo.getCoupon_price(), "【商城后台操作下架】三方商品下架,本地商品下架并清购物车,原有优惠券作废");
				} else if (type == 1) {
					apiUpdateUserCouponPriceUtils.saveTranInfoRecord(userId, "1", "2", userCouponVo.getCoupon_price(),
							userCouponVo.getCoupon_price(), "【毛利率任务自动下架jd商品】三方商品下架,本地商品下架并清购物车,原有优惠券作废");
				} else if (type == 2) {
					apiUpdateUserCouponPriceUtils.saveTranInfoRecord(userId, "1", "2", userCouponVo.getCoupon_price(),
							userCouponVo.getCoupon_price(), "【商品入库定时任务】三方商品下架,本地商品下架并清购物车,原有优惠券作废");
				} else if (type == 3) {
					apiUpdateUserCouponPriceUtils.saveTranInfoRecord(userId, "1", "2", userCouponVo.getCoupon_price(),
							userCouponVo.getCoupon_price(), "【定时任务扫描购物车】三方商品下架,本地商品下架并清购物车,原有优惠券作废");
				}
				// 回滚平台币
				userAmountVo.setAmount(userAmountVo.getAmount().add(userCouponVo.getCoupon_price()));
				qzUserAccountMapper.updateUserAccount(userAmountVo);

				logger.info("更新用户平台币,更新后平台币金额" + userAmountVo.getAmount());
				if (type == 0) {
					apiUpdateUserCouponPriceUtils.saveTranInfoRecord(userId, "2", "1", userCouponVo.getCoupon_price(),
							userAmountVo.getAmount(), "【商城后台操作下架】三方商品下架,本地商品下架并清购物车,原有优惠券作废,优惠券金额回滚到平台币");
				} else if (type == 1) {
					apiUpdateUserCouponPriceUtils.saveTranInfoRecord(userId, "2", "1", userCouponVo.getCoupon_price(),
							userAmountVo.getAmount(), "【毛利率任务自动下架jd商品】三方商品下架,本地商品下架并清购物车,原有优惠券作废,优惠券金额回滚到平台币");
				} else if (type == 2) {
					apiUpdateUserCouponPriceUtils.saveTranInfoRecord(userId, "2", "1", userCouponVo.getCoupon_price(),
							userAmountVo.getAmount(), "【商品入库定时任务】三方商品下架,本地商品下架并清购物车,原有优惠券作废,优惠券金额回滚到平台币");
				} else if (type == 3) {
					apiUpdateUserCouponPriceUtils.saveTranInfoRecord(userId, "2", "1", userCouponVo.getCoupon_price(),
							userAmountVo.getAmount(), "【定时任务扫描购物车】三方商品下架,本地商品下架并清购物车,原有优惠券作废,优惠券金额回滚到平台币");
				}
			}
			if (!CollectionUtils.isEmpty(carts)) {
				for (CartVo cart : carts) {
					if (null != cart.getChecked() && 1 == cart.getChecked()) {
						// 获取产品配比值
						GoodsCouponConfigVo goodsCoupon = goodsCouponConfigMapper.getUserCoupons(cart.getGoods_id(),
								userId);
						BigDecimal couponlPrice = BigDecimal.ZERO;// 优惠券临时总价值
						// 计算该产品优惠券总和
						if (goodsCoupon != null) {
							BigDecimal payMatching = BigDecimal.ZERO;
							if (payMatchingUtils.getPayMatching(cart.getProduct_id()) != null) {
								Object value = payMatchingUtils.getPayMatching(cart.getProduct_id())
										.get(cart.getGoods_id());
								if (value != null) {
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
			if (userAmountVo != null) {
				amount = userAmountVo.getAmount();
				if (amount.compareTo(couponTotalPrice) < 0) {
					couponTotalPrice = amount;
				}
				userAmountVo.setAmount(userAmountVo.getAmount().subtract(couponTotalPrice));
				qzUserAccountMapper.updateUserAccount(userAmountVo);
				logger.info("更新用户平台币,更新后平台币金额" + userAmountVo.getAmount());
				if (type == 0) {
					apiUpdateUserCouponPriceUtils.saveTranInfoRecord(userId, "1", "2", userCouponVo.getCoupon_price(),
							userCouponVo.getCoupon_price(), "【商城后台操作下架】三方商品下架,本地商品下架并清购物车,原有优惠券作废");
				} else if (type == 1) {
					apiUpdateUserCouponPriceUtils.saveTranInfoRecord(userId, "1", "2", userCouponVo.getCoupon_price(),
							userCouponVo.getCoupon_price(), "【毛利率任务自动下架jd商品】三方商品下架,本地商品下架并清购物车,原有优惠券作废");
				} else if (type == 2) {
					apiUpdateUserCouponPriceUtils.saveTranInfoRecord(userId, "1", "2", userCouponVo.getCoupon_price(),
							userCouponVo.getCoupon_price(), "【商品入库定时任务】三方商品下架,本地商品下架并清购物车,原有优惠券作废");
				} else if (type == 3) {
					apiUpdateUserCouponPriceUtils.saveTranInfoRecord(userId, "1", "2", userCouponVo.getCoupon_price(),
							userCouponVo.getCoupon_price(), "【定时任务扫描购物车】三方商品下架,本地商品下架并清购物车,原有优惠券作废");
				}
			}
			getUserCouponTotalPrice(userId, couponTotalPrice, type,couponTotalPriceAbstract);
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
			userCoupon.setAbstract_coupon_status(couponTotalPriceAbstract);
			userCoupon.setAdd_time(new Date());
			userCoupon.setParent_id(0);
			apiUserCouponMapper.save(userCoupon);
			if (null == userCoupon.getId()) {
				return this.toResponsObject(400, "优惠券生成失败", "");
			}
			apiUpdateUserCouponPriceUtils.saveSubUserCoupons(userId, userCoupon.getId());
		}
		if (userCouponVo != null) {
			userCouponVo.setAbstract_coupon_status(couponTotalPriceAbstract);
			userCouponVo.setCoupon_price(userCouponVo.getCoupon_price().add(couponTotalPrice));
			userCouponVo.setCoupon_status(1);
			apiUserCouponMapper.update(userCouponVo);
			if (type == 0) {
				apiUpdateUserCouponPriceUtils.saveTranInfoRecord(userId, "1", "2", userCouponVo.getCoupon_price(),
						userCouponVo.getCoupon_price(), "【商城后台操作下架】三方商品下架,本地商品下架并清购物车,原有优惠券作废");
			} else if (type == 1) {
				apiUpdateUserCouponPriceUtils.saveTranInfoRecord(userId, "1", "2", userCouponVo.getCoupon_price(),
						userCouponVo.getCoupon_price(), "【毛利率任务自动下架jd商品】三方商品下架,本地商品下架并清购物车,原有优惠券作废");
			} else if (type == 2) {
				apiUpdateUserCouponPriceUtils.saveTranInfoRecord(userId, "1", "2", userCouponVo.getCoupon_price(),
						userCouponVo.getCoupon_price(), "【商品入库定时任务】三方商品下架,本地商品下架并清购物车,原有优惠券作废");
			} else if (type == 3) {
				apiUpdateUserCouponPriceUtils.saveTranInfoRecord(userId, "1", "2", userCouponVo.getCoupon_price(),
						userCouponVo.getCoupon_price(), "【定时任务扫描购物车】三方商品下架,本地商品下架并清购物车,原有优惠券作废");
			}
		}
		return this.toResponsObject(0, "优惠券发送成功", "");
	}
}
