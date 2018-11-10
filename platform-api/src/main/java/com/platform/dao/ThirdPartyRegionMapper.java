package com.platform.dao;

import java.util.List;

import com.platform.entity.ThirdPartyRegionEntity;

public interface ThirdPartyRegionMapper extends BaseDao<ThirdPartyRegionEntity> {

	int insertBatch(List<ThirdPartyRegionEntity> thirdPartyRegionList);

	List<ThirdPartyRegionEntity> queryAllByType(Integer type);

}
