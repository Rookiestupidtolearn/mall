package com.platform.dao;

import com.platform.entity.JdOrderVo;

/**
 * 京东订单表Dao
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-11-12 15:02:37
 */
public interface JdOrderMapper extends BaseDao<JdOrderVo> {
   
	JdOrderVo queryByThirdOrder(String thirdOrder);
	
}
