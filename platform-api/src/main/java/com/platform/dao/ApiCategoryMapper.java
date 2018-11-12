package com.platform.dao;

import java.util.List;

import com.platform.entity.CategoryVo;

/**
 * 
 * 
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-11 09:14:25
 */
public interface ApiCategoryMapper extends BaseDao<CategoryVo> {
	
	/**
	 * 查询所有分类
	 * @return
	 */
	List<CategoryVo> queryAllCategorys();
	
	
	
}
