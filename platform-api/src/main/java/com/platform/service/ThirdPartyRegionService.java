package com.platform.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.ThirdPartyRegionMapper;
import com.platform.entity.ThirdPartyRegionEntity;
@Service
public class ThirdPartyRegionService {
	
	@Autowired
	private ThirdPartyRegionMapper thirdPartyRegionDao;
	
	/**
	 * 批量新增
	 * @param thirdPartyRegionList
	 * @return
	 */
	public int insertBatch(List<ThirdPartyRegionEntity> thirdPartyRegionList){
		return thirdPartyRegionDao.insertBatch(thirdPartyRegionList);
	}

	/**
	 * 根据类型查询地址信息
	 * @param string
	 * @return
	 */
	public List<ThirdPartyRegionEntity> queryAllByType(Integer type) {
		return thirdPartyRegionDao.queryAllByType(type);
	}
	
	/**
	 * 根据渠道获取所有的省份
	 * @param channel
	 * @return
	 */
	public List<ThirdPartyRegionEntity> queryPrinvinceByChannel(String  channel,String partType) {
		
		return thirdPartyRegionDao.queryPrinvinceByChannel(channel);
	}

	/**
	 * 批量绑定本地地址
	 * @param updateList
	 */
	public int updateBatch(List<ThirdPartyRegionEntity> updateList) {
		return thirdPartyRegionDao.updateBatch(updateList);
	}

	/**
	 * 根据条件查询地址信息
	 * @param paramMap
	 * @return
	 */
	public List<ThirdPartyRegionEntity> queryDate(Map<String, Object> paramMap) {
		return thirdPartyRegionDao.queryDate(paramMap);
	}
	
	
}
