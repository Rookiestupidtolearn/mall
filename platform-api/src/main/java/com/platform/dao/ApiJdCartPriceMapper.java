package com.platform.dao;

import java.util.List;
import java.util.Map;

import com.platform.entity.JdCartPriceVo;
import com.platform.entity.TopicCategoryVo;

public interface ApiJdCartPriceMapper  extends BaseDao<TopicCategoryVo>{
	
	int save(JdCartPriceVo jdCartPriceVo);
	/**
	 * 查询商品差异表
	 * @param param
	 * @return
	 */
	List<JdCartPriceVo> queryCartPrices(Map<String,Object> param);
}
