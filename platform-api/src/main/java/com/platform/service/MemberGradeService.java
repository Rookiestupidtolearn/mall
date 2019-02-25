package com.platform.service;

import com.platform.entity.MemberGradeEntity;

import java.util.List;
import java.util.Map;

/**
 * 会员等级表Service接口
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2019-02-20 10:12:40
 */
public interface MemberGradeService {

    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    MemberGradeEntity queryObject(Long id);

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    List<MemberGradeEntity> queryList(Map<String, Object> map);

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
     * @param memberGrade 实体
     * @return 保存条数
     */
    int save(MemberGradeEntity memberGrade);

    /**
     * 根据主键更新实体
     *
     * @param memberGrade 实体
     * @return 更新条数
     */
    int update(MemberGradeEntity memberGrade);

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
