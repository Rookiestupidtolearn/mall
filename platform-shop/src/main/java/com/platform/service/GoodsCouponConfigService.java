package com.platform.service;

import com.platform.entity.GoodsCouponConfigEntity;

import java.util.List;
import java.util.Map;

/**
 * 产品-平台币配置表
Service接口
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-10-12 11:42:14
 */
public interface GoodsCouponConfigService {

    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    GoodsCouponConfigEntity queryObject(Integer id);

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    List<GoodsCouponConfigEntity> queryList(Map<String, Object> map);

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
     * @param goodsCouponConfig 实体
     * @return 保存条数
     */
    int save(GoodsCouponConfigEntity goodsCouponConfig);
    
    /**
     * 保存配比
     * */
    Integer[] save(String normalMatching,String activityMatching,Integer[] goodsIds);

    /**
     * 根据主键更新实体
     *
     * @param goodsCouponConfig 实体
     * @return 更新条数
     */
    int update(GoodsCouponConfigEntity goodsCouponConfig);

    /**
     * 根据主键删除
     *
     * @param id
     * @return 删除条数
     */
    int delete(Integer id);

    /**
     * 根据主键批量删除
     *
     * @param ids
     * @return 删除条数
     */
    int deleteBatch(Integer[] ids);

	/**
	 *	根据ids查询对应的商品id
	 * @param ids
	 * @return
	 */
	List<GoodsCouponConfigEntity> selectGoodsIdsById(Integer[] ids);
}
