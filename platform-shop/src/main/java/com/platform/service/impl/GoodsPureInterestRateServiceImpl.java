package com.platform.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.GoodsPureInterestRateDao;
import com.platform.entity.GoodsPureInterestRateEntity;
import com.platform.service.GoodsPureInterestRateService;
@Service("goodsPureInterestRateService")
public class GoodsPureInterestRateServiceImpl implements GoodsPureInterestRateService {
	
	@Autowired
	private GoodsPureInterestRateDao goodsPureInterestRateDao;
	
	@Override
	public int save(GoodsPureInterestRateEntity goodsPureInterestRateEntity) {
		return goodsPureInterestRateDao.save(goodsPureInterestRateEntity);
	}

	@Override
	public GoodsPureInterestRateEntity queryObject(Integer id) {
		return goodsPureInterestRateDao.queryObject(id);
	}

	@Override
	public List<GoodsPureInterestRateEntity> queryAll(HashMap<String, Object> paramMap) {
		return goodsPureInterestRateDao.queryAll(paramMap);
	}

	@Override
	public int update(GoodsPureInterestRateEntity goodsPureInterestRateEntity) {
		return goodsPureInterestRateDao.update(goodsPureInterestRateEntity);
	}

	@Override
	public int delete(Integer id) {
		return goodsPureInterestRateDao.delete(id);
	}

	@Override
	public Integer[] queryGoodsIdsByPrice(Map<String, Object> paramMap) {
		return goodsPureInterestRateDao.queryGoodsIdsByPrice(paramMap);
	}

	@Override
	public int delBygoodsIds(Integer[] productIds) {
		return goodsPureInterestRateDao.delBygoodsIds(productIds);
	}

	@Override
	public int delByProductIds(Integer[] productIds) {
		return goodsPureInterestRateDao.delByProductIds(productIds);
	}

	@Override
	public List<GoodsPureInterestRateEntity> queryGoodsByPureInterestRate(Map<String, Object> map) {
		return goodsPureInterestRateDao.queryGoodsByPureInterestRate(map);
	}

}
