package com.platform.service;

import com.platform.entity.MemberGrowthInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 会员成长值记录表Service接口
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2019-02-19 11:37:52
 */
public interface ApiMemberGrowthInfoService {

    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    MemberGrowthInfoEntity queryObject(Long id);

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    List<MemberGrowthInfoEntity> queryList(Map<String, Object> map);

    /**
     * 分页统计总数
     *
     * @param map 参数
     * @return 总数
     */
    int queryTotal(Map<String, Object> map);

    /**
     * 保存实体
     *
     * @param memberGrowthInfo 实体
     * @return 保存条数
     */
    int save(MemberGrowthInfoEntity memberGrowthInfo);

    /**
     * 根据主键更新实体
     *
     * @param memberGrowthInfo 实体
     * @return 更新条数
     */
    int update(MemberGrowthInfoEntity memberGrowthInfo);

    /**
     * 根据主键删除
     *
     * @param id
     * @return 删除条数
     */
    int delete(Long id);

    /**
     * 根据主键批量删除
     *
     * @param ids
     * @return 删除条数
     */
    int deleteBatch(Long[] ids);
}
