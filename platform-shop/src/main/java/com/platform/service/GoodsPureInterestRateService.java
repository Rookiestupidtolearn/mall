package com.platform.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.platform.entity.GoodsPureInterestRateEntity;

public interface GoodsPureInterestRateService {
	
	//List<GoodsPureInterestRateEntity> queryObjectByIds(Integer[] ids);
	
	GoodsPureInterestRateEntity queryObject(Integer id);
	
	List<GoodsPureInterestRateEntity> queryAll(HashMap<String,Object> paramMap);
	
	int save(GoodsPureInterestRateEntity goodsPureInterestRateEntity);
	
	int update(GoodsPureInterestRateEntity goodsPureInterestRateEntity);
	
	int delete(Integer id);

	Integer[] queryGoodsIdsByPrice(Map<String, Object> paramMap);

	int delBygoodsIds(Integer[] ids);

	int delByProductIds(Integer[] ids);

	/**
	 * 根据毛利率和商品上架状态查询商品
	 * @param map
	 * @return
	 */
	List<GoodsPureInterestRateEntity> queryGoodsByPureInterestRate(Map<String, Object> map);
}
