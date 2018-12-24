package com.platform.dao;

import com.platform.entity.QzUserAccountVo;

public interface QzUserAccountMapper extends BaseDao<QzUserAccountVo>{
	/**
	 * 根据userid查询用户余额
	 * @param userId
	 * @return
	 */
	QzUserAccountVo queruUserAccountInfo(Long shop_user_id);
	/**
	 * 更新用户平台币
	 * @param userCouponVo
	 * @return
	 */
	int updateUserAccount(QzUserAccountVo userAmountVo);
}
