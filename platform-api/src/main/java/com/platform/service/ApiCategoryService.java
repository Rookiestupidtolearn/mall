package com.platform.service;

import com.platform.dao.ApiCategoryMapper;
import com.platform.entity.CategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class ApiCategoryService {
	@Autowired
	private ApiCategoryMapper categoryDao;
	
	
	public CategoryVo queryObject(Integer id){
		return categoryDao.queryObject(id);
	}


	public List<CategoryVo> queryList(Map<String, Object> map){
		return categoryDao.queryList(map);
	}

	public List<Integer> queryListOfGoodsNotNull(Map<String, Object> map){
		return categoryDao.queryListOfGoodsNotNull(map);
	}
	public List<Integer> queryListOfGoodsNotNullAndSortByGoodsNum(Map<String, Object> map){
		return categoryDao.queryListOfGoodsNotNullAndSortByGoodsNum(map);
	}
	

	public int queryTotal(Map<String, Object> map){
		return categoryDao.queryTotal(map);
	}
	
	
	public void save(CategoryVo category){
		categoryDao.save(category);
	}
	
	
	public void update(CategoryVo category){
		categoryDao.update(category);
	}
	
	
	public void delete(Integer id){
		categoryDao.delete(id);
	}
	
	
	public void deleteBatch(Integer[] ids){
		categoryDao.deleteBatch(ids);
	}
	
	public List<Integer> quertOtherIds(){
		return categoryDao.quertOtherIds();
	}
	
	public List<CategoryVo> quertSubCategorys(Integer id){
		return categoryDao.quertSubCategorys(id);
	}
}
