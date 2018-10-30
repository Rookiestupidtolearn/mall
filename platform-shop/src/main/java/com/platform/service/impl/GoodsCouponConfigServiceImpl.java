package com.platform.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.CartDao;
import com.platform.dao.GoodsCouponConfigDao;
import com.platform.dao.GoodsDao;
import com.platform.entity.CartEntity;
import com.platform.entity.GoodsCouponConfigEntity;
import com.platform.entity.GoodsEntity;
import com.platform.entity.SysUserEntity;
import com.platform.service.ApiCartService;
import com.platform.service.GoodsCouponConfigService;
import com.platform.utils.RRException;
import com.platform.utils.ShiroUtils;

import jline.internal.Log;

/**
 * 产品-平台币配置表
Service实现类
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-10-12 11:42:14
 */
@Service("goodsCouponConfigService")
public class GoodsCouponConfigServiceImpl implements GoodsCouponConfigService {
    @Autowired
    private GoodsCouponConfigDao goodsCouponConfigDao;

    @Override
    public GoodsCouponConfigEntity queryObject(Integer id) {
        return goodsCouponConfigDao.queryObject(id);
    }

    @Override
    public List<GoodsCouponConfigEntity> queryList(Map<String, Object> map) {
        return goodsCouponConfigDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return goodsCouponConfigDao.queryTotal(map);
    }

    @Override
    	
    public int save(GoodsCouponConfigEntity goodsCouponConfig) {
    	SysUserEntity user = ShiroUtils.getUserEntity();
//		map.put("id",goodsCouponConfig.getId());
//		List<GoodsCouponConfigEntity> list = queryList(map);
		GoodsCouponConfigEntity  goodsCouponConfigEntity = goodsCouponConfigDao.queryObject(goodsCouponConfig.getId());
		if (null != goodsCouponConfigEntity) {
			throw new RRException("商品配比已存在！");
		}
		if(goodsCouponConfig.getGoodValue()<0||goodsCouponConfig.getGoodValue()>1){
			throw new RRException("商品配比值为大于0且小于等于1");
		}
    	goodsCouponConfig.setDelFlag("0");
    	goodsCouponConfig.setCreateUserDeptId(user.getDeptId());
    	goodsCouponConfig.setCreateUserId(user.getUserId());
    	goodsCouponConfig.setUpdateUserId(user.getUserId());
    	goodsCouponConfig.setUpdateTime(new Date());
    	
        return goodsCouponConfigDao.save(goodsCouponConfig);
    }

    @Override
    public int update(GoodsCouponConfigEntity goodsCouponConfig) {
    	
    	if(goodsCouponConfig.getGoodValue()<0||goodsCouponConfig.getGoodValue()>1){
			throw new RRException("商品配比值为大于0且小于等于1");
		}
        return goodsCouponConfigDao.update(goodsCouponConfig);
    }

    @Override
    public int delete(Integer id) {
        return goodsCouponConfigDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return goodsCouponConfigDao.deleteBatch(ids);
    }

	@Override
	public Integer[] selectGoodsIdsById(Integer[] ids) {
		return goodsCouponConfigDao.selectGoodsIdsById(ids);
	}
}
