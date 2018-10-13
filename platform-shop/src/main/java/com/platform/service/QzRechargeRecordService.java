package com.platform.service;

import com.platform.entity.QzRechargeRecordEntity;
import com.platform.utils.R;

import java.util.List;
import java.util.Map;

/**
 * 用户充值记录Service接口
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-10-11 17:49:01
 */
public interface QzRechargeRecordService {

    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    QzRechargeRecordEntity queryObject(Long id);

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    List<QzRechargeRecordEntity> queryList(Map<String, Object> map);

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
     * @param qzRechargeRecord 实体
     * @return 保存条数
     */
    int save(QzRechargeRecordEntity qzRechargeRecord);

    /**
     * 根据主键更新实体
     *
     * @param qzRechargeRecord 实体
     * @return 更新条数
     */
    int update(QzRechargeRecordEntity qzRechargeRecord);

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

	R rechargeBatch(Map<String, Object> map);

	R rechargeAudit(Integer id, String state);

}
