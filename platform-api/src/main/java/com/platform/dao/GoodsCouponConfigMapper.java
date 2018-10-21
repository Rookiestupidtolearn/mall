package com.platform.dao;

import com.platform.entity.GoodsCouponConfigVo;

public interface GoodsCouponConfigMapper extends BaseDao<GoodsCouponConfigVo>{
	//根据goodid查询优惠券跑配比值
	GoodsCouponConfigVo getUserCoupons(Integer goods_id,Long user_id);
	//立即购买查询商品配比值
	GoodsCouponConfigVo getUserBuyNowCoupons(Integer goods_id); 
}
