package com.platform.dao;

import java.util.List;
import java.util.Map;

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
	
	
	/**
	 * 根据分类id获取父级分类id
	 * @param categoryIds
	 * @return
	 */
	List<Integer> queryParentIdsByCategoryId(List<Integer> categoryIds);
	
	
	/**
	 * 获取分类集合
	 * @param parentIds
	 */
	List<CategoryVo> queryCategoryList(List<Integer> parentIds);

	/**
	 * 获取商品不为空的分类集合
	 * @param map
	 */
	List<CategoryVo> queryListOfGoosNotNull(Map<String, Object> map);
}
