package com.platform.dao;


import com.platform.thirdrechard.entity.ThirdPreCompanyRechargeRecord;
import com.platform.thirdrechard.entity.ThirdPreCompanyRechargeRecordExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 三方公司充值操作
 * @author zct
 *
 */
public interface ThirdPreCompanyRechargeRecordMapper {
	
	
    /**
     * 统计总额
     * @param example
     * @return
     */
    int countByExample(ThirdPreCompanyRechargeRecordExample example);

    /**
     * 删除
     * @param example
     * @return
     */
    int deleteByExample(ThirdPreCompanyRechargeRecordExample example);

    /**
     * 主键删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入操作
     * @param record
     * @return
     */
    int insert(ThirdPreCompanyRechargeRecord record);

    /**
     * 插入非空
     * @param record
     * @return
     */
    int insertSelective(ThirdPreCompanyRechargeRecord record);

    /**
     * 获取集合
     * @param example
     * @return
     */
    List<ThirdPreCompanyRechargeRecord> selectByExample(ThirdPreCompanyRechargeRecordExample example);

    /**
     *主键获取实体
     * @param id
     * @return
     */
    ThirdPreCompanyRechargeRecord selectByPrimaryKey(Integer id);
    

    /**
     * 更新
     * @param record
     * @param example
     * @return
     */
    int updateByExampleSelective(@Param("record") ThirdPreCompanyRechargeRecord record, @Param("example") ThirdPreCompanyRechargeRecordExample example);

    /**
     * 更新
     * @param record
     * @param example
     * @return
     */
    int updateByExample(@Param("record") ThirdPreCompanyRechargeRecord record, @Param("example") ThirdPreCompanyRechargeRecordExample example);

    /**
     * 主键非空更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(ThirdPreCompanyRechargeRecord record);

    /**
     * 主键更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(ThirdPreCompanyRechargeRecord record);
}