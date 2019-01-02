package com.platform.dao;

import com.platform.thirdrechard.entity.ThirdMerchantRechargeRecord;
import com.platform.thirdrechard.entity.ThirdMerchantRechargeRecordExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ThirdMerchantRechargeRecordMapper {

    int countByExample(ThirdMerchantRechargeRecordExample example);

    int deleteByExample(ThirdMerchantRechargeRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ThirdMerchantRechargeRecord record);

    int insertSelective(ThirdMerchantRechargeRecord record);

    List<ThirdMerchantRechargeRecord> selectByExample(ThirdMerchantRechargeRecordExample example);

    ThirdMerchantRechargeRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ThirdMerchantRechargeRecord record, @Param("example") ThirdMerchantRechargeRecordExample example);

    int updateByExample(@Param("record") ThirdMerchantRechargeRecord record, @Param("example") ThirdMerchantRechargeRecordExample example);

    int updateByPrimaryKeySelective(ThirdMerchantRechargeRecord record);

    int updateByPrimaryKey(ThirdMerchantRechargeRecord record);
}