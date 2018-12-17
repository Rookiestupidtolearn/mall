package com.platform.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.platform.dao.QzRechargeRecordDao;
import com.platform.entity.QzMoneyRecordEntity;
import com.platform.entity.QzRechargeRecordEntity;
import com.platform.entity.QzUserAccountEntity;
import com.platform.entity.SysUserEntity;
import com.platform.entity.UserEntity;
import com.platform.service.QzMoneyRecordService;
import com.platform.service.QzRechargeRecordService;
import com.platform.service.QzUserAccountService;
import com.platform.service.UserService;
import com.platform.utils.GenerateCodeUtil;
import com.platform.utils.R;
import com.platform.utils.ShiroUtils;

/**
 * 用户充值记录Service实现类
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-10-11 17:49:01
 */
@Service("qzRechargeRecordService")
public class QzRechargeRecordServiceImpl implements QzRechargeRecordService {
	
	private static final Logger log = LoggerFactory.getLogger(QzRechargeRecordServiceImpl.class);
	
	@Autowired
	private QzUserAccountService qzUserAccountService;
	@Autowired
	private QzMoneyRecordService qzMoneyRecordService;
	@Autowired
	private QzRechargeRecordDao qzRechargeRecordDao;
	@Autowired
	private UserService userService;

	@Override
	public QzRechargeRecordEntity queryObject(Long id) {
		return qzRechargeRecordDao.queryObject(id);
	}

	@Override
	public List<QzRechargeRecordEntity> queryList(Map<String, Object> map) {
		return qzRechargeRecordDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return qzRechargeRecordDao.queryTotal(map);
	}
	
	@Override
	public List<QzRechargeRecordEntity> queryAuditList(Map<String, Object> map) {
		return qzRechargeRecordDao.queryAuditList(map);
	}

	@Override
	public int queryAuditTotal(Map<String, Object> map) {
		return qzRechargeRecordDao.queryAuditTotal(map);
	}

	@Override
	public int save(QzRechargeRecordEntity qzRechargeRecord) {
		return qzRechargeRecordDao.save(qzRechargeRecord);
	}

	@Override
	public int update(QzRechargeRecordEntity qzRechargeRecord) {
		return qzRechargeRecordDao.update(qzRechargeRecord);
	}

	@Override
	public int delete(Long id) {
		return qzRechargeRecordDao.delete(id);
	}

	@Override
	public int deleteBatch(Long[] ids) {
		return qzRechargeRecordDao.deleteBatch(ids);
	}

	/**
	 * 批量充值申请
	 */
	@Override
	public R rechargeBatch(Map<String, Object> params) {

		if (params.get("mobiles").equals("")) {
			return R.error(400, "手机号不能为空");
		}
		if (params.get("amount").equals("")) {
			return R.error(400, "转账金额不能为空");
		}
		if (params.get("memo").equals("")) {
			return R.error(400, "转账说明不能为空");
		}
		// 用户名
		SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
		SysUserEntity user = new SysUserEntity();
		if (null != sysUserEntity) {
			user = ShiroUtils.getUserEntity();
		}

		String mobile = (String) params.get("mobiles");
		String[] arr = mobile.split(","); // 用,分割
		for (int i = 0; i < arr.length; i++) {
			UserEntity entity = userService.queryEntityByMobile(arr[i]);
			if (entity == null) {
				continue;
			}
			if (entity.getId() != null) {
				// 充值记录流水
				QzRechargeRecordEntity record = new QzRechargeRecordEntity();
				record.setShopUserId(entity.getId());
				record.setState("0");
				record.setOperateId(user.getUserId());
				String amount = (String) params.get("amount");
				record.setAmount(new BigDecimal(amount));
				record.setMemo((String) params.get("memo"));
				record.setTradeNo(GenerateCodeUtil.buildBizNo());
				record.setMobile(arr[i]);
				record.setRechargeType(1);//1后台充值
				log.info("充值记录信息"+JSON.toJSONString(record));
				
				qzRechargeRecordDao.save(record);
				
			}

		}

		return R.ok();
	}

	@Override
	@Transactional
	public R rechargeAudit(Integer id, String state) {
		SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
		SysUserEntity user = new SysUserEntity();
		if (null != sysUserEntity) {
			user = ShiroUtils.getUserEntity();
		}
		QzRechargeRecordEntity qzRechargeRecordEntity = qzRechargeRecordDao.queryObject(id);
		if (qzRechargeRecordEntity == null) {
			log.error("充值记录不存在,充值记录id是"+id);
			return R.error(400, "充值记录不存在");
		}
		if (state.equals("1")) {// 通过

			// 充值资金流水
			QzMoneyRecordEntity moneyRecordEntity = new QzMoneyRecordEntity();
			QzUserAccountEntity account = qzUserAccountService
					.queryEntityByUserId(qzRechargeRecordEntity.getShopUserId());
			moneyRecordEntity.setShopUserId(qzRechargeRecordEntity.getShopUserId());
			moneyRecordEntity.setTranType("1");
			moneyRecordEntity.setTranFlag(1);
			moneyRecordEntity.setTarnAmount(qzRechargeRecordEntity.getAmount());
			if (account == null) {
				moneyRecordEntity.setCurrentAmount(qzRechargeRecordEntity.getAmount());
			} else {
				moneyRecordEntity.setCurrentAmount(account.getAmount().add(qzRechargeRecordEntity.getAmount()));
			}

			moneyRecordEntity.setTradeNo(qzRechargeRecordEntity.getTradeNo());
			log.info("充值资金流水信息"+JSON.toJSONString(moneyRecordEntity));
			qzMoneyRecordService.save(moneyRecordEntity);

			// 操作用户余额
			if (account == null) {
				QzUserAccountEntity accountEntity = new QzUserAccountEntity();
				accountEntity.setShopUserId(qzRechargeRecordEntity.getShopUserId());
				accountEntity.setAmount(qzRechargeRecordEntity.getAmount());
				accountEntity.setLastUpdateTime(new Date());
				log.info("初次创建用户账户余额信息"+JSON.toJSONString(accountEntity));
				qzUserAccountService.save(accountEntity);
			} else {
				account.setLastUpdateTime(new Date());
				account.setAmount(qzRechargeRecordEntity.getAmount().add(account.getAmount()));
				log.info("用户账户余额信息"+JSON.toJSONString(account));
				qzUserAccountService.update(account);
			}

			
		}
		  //审核状态
			qzRechargeRecordEntity.setState(state);
			qzRechargeRecordEntity.setAuditId(user.getUserId());
			log.info("修改充值记录的审核状态为："+state+"，充值记录id是："+qzRechargeRecordEntity.getId());
			qzRechargeRecordDao.update(qzRechargeRecordEntity);
		

		return R.ok();
	}
}
