package com.platform.dao;

import com.platform.youle.entity.GoodsImagePathVo;
import com.platform.youle.entity.JdGoodsVo;

public interface JdGoodsImgPathMapper extends BaseDao<GoodsImagePathVo>{
	 /**
     * 保存图片地址
     * @param imgPathVo
     * @return
     */
    int save(GoodsImagePathVo imgPathVo);
}
