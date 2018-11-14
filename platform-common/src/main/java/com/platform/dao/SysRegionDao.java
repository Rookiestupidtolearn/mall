package com.platform.dao;


import java.util.List;
import java.util.Map;

import com.platform.entity.SysRegionEntity;

/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-11-04 11:19:31
 */
public interface SysRegionDao extends BaseDao<SysRegionEntity> {

	List<SysRegionEntity> queryAllByType(int type);

	List<SysRegionEntity> queryDate(Map<String, Object> paramMap);

	int insertBatch(List<SysRegionEntity> sysRegionEntityList);

}
