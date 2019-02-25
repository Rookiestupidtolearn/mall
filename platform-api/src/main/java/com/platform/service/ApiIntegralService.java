package com.platform.service;

import com.platform.entity.IntegralEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 用户积分表Service接口
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2019-02-19 11:37:52
 */
public interface ApiIntegralService {

    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    IntegralEntity queryObject(Long id);

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    List<IntegralEntity> queryList(Map<String, Object> map);

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
     * @param integral 实体
     * @return 保存条数
     */
    int save(IntegralEntity integral);

    /**
     * 根据主键更新实体
     *
     * @param integral 实体
     * @return 更新条数
     */
    int update(IntegralEntity integral);

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

    /**
     * 根据userid 增加对应的用户积分
     * @param userId
     * @return
     */
    boolean increaseIntegralByUserId(Integer userId, BigDecimal money, BigDecimal percentage );
}
