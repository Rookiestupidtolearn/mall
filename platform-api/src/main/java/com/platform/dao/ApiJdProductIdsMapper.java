package com.platform.dao;

import java.util.List;

import com.platform.entity.JdProductIdsVo;

public interface ApiJdProductIdsMapper extends BaseDao<JdProductIdsVo>{
	
	int save(JdProductIdsVo vo);
	/**
	 * 定时任务查询产品id
	 * @return
	 */
	List<JdProductIdsVo> quertProductIds(Integer start);
	
	int updateProductIds();
}
