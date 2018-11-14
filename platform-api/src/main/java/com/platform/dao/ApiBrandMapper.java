package com.platform.dao;

import java.util.List;

import com.platform.entity.BrandVo;

/**
 * 
 * 
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-11 09:14:25
 */
public interface ApiBrandMapper extends BaseDao<BrandVo> {
	
	/*
	 * 根据品牌商名称查询
	 */
	List<BrandVo> quertBrandByNames(String brandName);
}
