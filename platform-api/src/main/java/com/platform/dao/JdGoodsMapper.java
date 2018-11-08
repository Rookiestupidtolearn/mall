package com.platform.dao;

import com.platform.youle.entity.JdGoodsVo;

public interface JdGoodsMapper extends BaseDao<JdGoodsVo>{
	 /**
     * 保存商品
     * @param goodsVo
     * @return
     */
    int save(JdGoodsVo goodsVo);
}
