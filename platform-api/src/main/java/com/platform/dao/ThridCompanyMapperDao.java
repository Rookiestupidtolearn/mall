package com.platform.dao;


import com.platform.thirdrechard.entity.ThridCompany;
import com.platform.thirdrechard.entity.ThridCompanyExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ThridCompanyMapperDao {
	
    /**
     * 统计
     * @param example
     * @return
     */
    int countByExample(ThridCompanyExample example);

    /**
     * 删除 主键
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入
     * @param record
     * @return
     */
    int insert(ThridCompany record);

    /**
     * 插入 非空
     * @param record
     * @return
     */
    int insertSelective(ThridCompany record);

    /**
     * 查询集合
     * @param example
     * @return
     */
    List<ThridCompany> selectByExample(ThridCompanyExample oredCriteria);

    /**
     * 查询主键
     * @param id
     * @return
     */
    ThridCompany selectByPrimaryKey(Integer id);

    /**
     * 主键更新 非空
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(ThridCompany record);

    /**
     * 主键更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(ThridCompany record);

    ThridCompany selectByAppId(@Param("appId") String appId);
}