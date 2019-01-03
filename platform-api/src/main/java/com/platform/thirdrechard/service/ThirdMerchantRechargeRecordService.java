package com.platform.thirdrechard.service;

import com.platform.dao.ThirdMerchantRechargeRecordMapper;
import com.platform.thirdrechard.entity.ThirdMerchantRechargeRecord;
import com.platform.thirdrechard.entity.ThirdMerchantRechargeRecordExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThirdMerchantRechargeRecordService {

	@Autowired
	private ThirdMerchantRechargeRecordMapper thirdMerchantRechargeRecordMapper;
	
	
	/**
	 * 添加 
	 * @param record
	 * @return
	 */
	public boolean saveThirdMerchantRechargeRecord(ThirdMerchantRechargeRecord record){
		int num = thirdMerchantRechargeRecordMapper.insert(record);
		return num == 0 ? false : true ;
	}
	
	/**
	 * 
	 * @param record
	 * @param example
	 * @return
	 */
	public boolean updateThirdMerchantRechargeRecord(ThirdMerchantRechargeRecord record,ThirdMerchantRechargeRecordExample example){
		int num = thirdMerchantRechargeRecordMapper.updateByExampleSelective(record, example);
		return num == 0 ? false : true ; 
	}

	public  boolean updateThirdMerchantRechargeRecord(ThirdMerchantRechargeRecord record){
		int i = thirdMerchantRechargeRecordMapper.updateByPrimaryKey(record);
		return i == 0 ? false : true ;
	}
}
