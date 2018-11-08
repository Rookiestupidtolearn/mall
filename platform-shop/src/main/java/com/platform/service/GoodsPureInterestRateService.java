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
}
