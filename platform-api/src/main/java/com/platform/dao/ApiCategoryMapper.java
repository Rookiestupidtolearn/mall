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
	/**
	 * 查询所有分类商品ids
	 * @return
	 */
	List<Integer> quertOtherIds();
	
	List<CategoryVo> queryAllChildsCategorys();
	/**
	 * 根据name查询分类 && parentId = 0
	 * @return
	 */
	List<CategoryVo> queryCategoryByName(String name);
	/**
	 * 查询所有父类id
	 * @return
	 */
	List<CategoryVo> queryAllParentIdsCategorys();
	/**
	 * 查询所有子类
	 * @param name
	 * @param level
	 * @return
	 */
	List<CategoryVo> queryChildCategory(String name,String level);
	/**
	 * 根据id查询时候有子分类
	 * @param id
	 * @return
	 */
	List<CategoryVo> quertSubCategorys(Integer id);
}
