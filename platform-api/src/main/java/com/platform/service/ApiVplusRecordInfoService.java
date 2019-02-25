package com.platform.service;

import com.platform.entity.VplusRecordInfoEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Vplus开通续费记录表Service接口
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2019-02-21 13:58:03
 */
public interface ApiVplusRecordInfoService {

    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    VplusRecordInfoEntity queryObject(Long id);

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    List<VplusRecordInfoEntity> queryList(Map<String, Object> map);

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
     * @param vplusRecordInfo 实体
     * @return 保存条数
     */
    int save(VplusRecordInfoEntity vplusRecordInfo);

    /**
     * 根据主键更新实体
     *
     * @param vplusRecordInfo 实体
     * @return 更新条数
     */
    int update(VplusRecordInfoEntity vplusRecordInfo);

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
     * 通过平台流水号查找会员充值记录
     * @param yeePayTradeNo
     * @return
     */
    VplusRecordInfoEntity queryVplusRecordInfoByYeepayTradeNo(String yeePayTradeNo);

    /**
     * 通过用户id或者手机号保存权益记录
     * @param userId
     * @param mobile
     * @param type 充值平台类型
     * @return
     */
    boolean saveVplus(Integer userId, String mobile, BigDecimal money,Integer type);
}
