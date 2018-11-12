package com.platform.service;

import java.util.List;

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
	
	
}
