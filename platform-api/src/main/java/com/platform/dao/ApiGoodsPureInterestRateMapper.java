package com.platform.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.platform.entity.GoodsPureInterestRateVo;

public interface ApiGoodsPureInterestRateMapper extends BaseDao<GoodsPureInterestRateVo> {

	List<GoodsPureInterestRateVo> queryAll(HashMap<String, Object> paramMap);

	Integer[] queryGoodsIdsByPrice(Map<String, Object> paramMap);

	int delBygoodsIds(Integer[] ids);

	int delByProductIds(Integer[] ids);

}
