package com.platform.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.ApiGoodsPureInterestRateMapper;
import com.platform.entity.GoodsPureInterestRateVo;
@Service
public class ApiGoodsPureInterestRateService {
	
	@Autowired
    private ApiGoodsPureInterestRateMapper apiGoodsPureInterestRateDao;

	//List<GoodsPureInterestRateEntity> queryObjectByIds(Integer[] ids);
	
	public GoodsPureInterestRateVo queryObject(Integer id){
		return apiGoodsPureInterestRateDao.queryObject(id);
	}
	
	public List<GoodsPureInterestRateVo> queryAll(HashMap<String,Object> paramMap){
		return apiGoodsPureInterestRateDao.queryAll(paramMap);
	}
	
	public int save(GoodsPureInterestRateVo goodsPureInterestRateVo){
		return apiGoodsPureInterestRateDao.save(goodsPureInterestRateVo);
	}
	
	public int update(GoodsPureInterestRateVo goodsPureInterestRateVo){
		return apiGoodsPureInterestRateDao.update(goodsPureInterestRateVo);
	}
	
	public int delete(Integer id){
		return apiGoodsPureInterestRateDao.delete(id);
	}

	public Integer[] queryGoodsIdsByPrice(Map<String, Object> paramMap){
		return apiGoodsPureInterestRateDao.queryGoodsIdsByPrice(paramMap);
	}

	public int delBygoodsIds(Integer[] ids){
		return apiGoodsPureInterestRateDao.delBygoodsIds(ids);
	}

	public int delByProductIds(Integer[] ids){
		return apiGoodsPureInterestRateDao.delByProductIds(ids);
	}

	public void saveBatch(List<GoodsPureInterestRateVo> goodsPureInterestRateVoList) {
		apiGoodsPureInterestRateDao.saveBatch(goodsPureInterestRateVoList);
	}
	
	/**
	 * 根据毛利率和商品上架状态查询商品
	 * @param map
	 * @return
	 */
	public List<GoodsPureInterestRateVo> queryGoodsByPureInterestRate(Map<String, Object> map){
		return apiGoodsPureInterestRateDao.queryGoodsByPureInterestRate(map);
	}
}
