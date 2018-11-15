package com.platform.dao;

import com.platform.entity.GoodsVo;

import java.util.List;
import java.util.Map;

/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-11 09:16:45
 */
public interface ApiGoodsMapper extends BaseDao<GoodsVo> {

    List<GoodsVo> queryHotGoodsList(Map<String, Object> params);

    List<GoodsVo> queryCatalogProductList(Map<String, Object> params);
    
    int save(GoodsVo vo);
    
    void saveGoods(GoodsVo vo);
    /**
     * 根据商品分类查询商品
     * @param categoryId
     * @return
     */
    List<GoodsVo> quertGoodsByCategory(String categoryId);
    /**
     * 根据goodsn查询商品
     * @param goodsn
     * @return
     */
    List<GoodsVo> queryGoodsBySn(String goodsn);
}
