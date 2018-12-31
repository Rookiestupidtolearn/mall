package com.platform.dao;



import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.platform.nideshopuser.entity.NideshopUser;
import com.platform.nideshopuser.entity.NideshopUserExample;

public interface NideshopUserMapper {
	
    /**
     * 统计
     * @param example
     * @return
     */
    int countByExample(NideshopUserExample example);

    /**
     * 删除
     * @param example
     * @return
     */
    int deleteByExample(NideshopUserExample example);

    /**
     * 主键删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入
     * @param record
     * @return
     */
    int insert(NideshopUser record);

    /**
     * 非空插入
     * @param record
     * @return
     */
    int insertSelective(NideshopUser record);

    /**
     * 获取用户列表
     * @param example
     * @return
     */
    List<NideshopUser> selectByExample(NideshopUserExample example);

    /**
     * 主键查询
     * @param id
     * @return
     */
    NideshopUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") NideshopUser record, @Param("example") NideshopUserExample example);

    int updateByExample(@Param("record") NideshopUser record, @Param("example") NideshopUserExample example);

    int updateByPrimaryKeySelective(NideshopUser record);

    int updateByPrimaryKey(NideshopUser record);
}