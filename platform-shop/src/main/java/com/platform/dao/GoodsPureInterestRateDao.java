package com.platform.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.platform.entity.GoodsPureInterestRateEntity;

public interface GoodsPureInterestRateDao extends BaseDao<GoodsPureInterestRateEntity> {

	List<GoodsPureInterestRateEntity> queryAll(HashMap<String, Object> paramMap);

	Integer[] queryGoodsIdsByPrice(Map<String, Object> paramMap);

	int delBygoodsIds(Integer[] goodsIds);

	int delByProductIds(Integer[] productIds);

	List<GoodsPureInterestRateEntity> queryGoodsByPureInterestRate(Map<String, Object> map);

}
