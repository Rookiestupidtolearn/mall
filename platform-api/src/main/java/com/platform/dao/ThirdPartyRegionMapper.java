package com.platform.dao;

import java.util.List;
import java.util.Map;

import com.platform.entity.ThirdPartyRegionEntity;

public interface ThirdPartyRegionMapper extends BaseDao<ThirdPartyRegionEntity> {

	int insertBatch(List<ThirdPartyRegionEntity> thirdPartyRegionList);

	List<ThirdPartyRegionEntity> queryAllByType(Integer type);
	
	/**
	 * 根据渠道获取所有的省份
	 * @param channel
	 * @return
	 */
	List<ThirdPartyRegionEntity> queryPrinvinceByChannel(String  channel);

	int updateBatch(List<ThirdPartyRegionEntity> updateList);

	List<ThirdPartyRegionEntity> queryDate(Map<String, Object> paramMap);
}
