package com.platform.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.GoodsCouponConfigDao;
import com.platform.entity.GoodsCouponConfigEntity;
import com.platform.entity.SysUserEntity;
import com.platform.service.GoodsCouponConfigService;
import com.platform.utils.RRException;
import com.platform.utils.ShiroUtils;

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
		/*if(goodsCouponConfig.getGoodValue()<0||goodsCouponConfig.getGoodValue()>1){
			throw new RRException("商品配比值为大于0且小于等于1");
		}*/
    	goodsCouponConfig.setDelFlag("0");
    	goodsCouponConfig.setCreateUserDeptId(user.getDeptId());
    	goodsCouponConfig.setCreateUserId(user.getUserId());
    	goodsCouponConfig.setUpdateUserId(user.getUserId());
    	goodsCouponConfig.setUpdateTime(new Date());
    	
        return goodsCouponConfigDao.save(goodsCouponConfig);
    }

    @Override
    public int update(GoodsCouponConfigEntity goodsCouponConfig) {
    	
    	/*if(goodsCouponConfig.getGoodValue()<0||goodsCouponConfig.getGoodValue()>1){
			throw new RRException("商品配比值为大于0且小于等于1");
		}*/
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
	public List<GoodsCouponConfigEntity> selectGoodsIdsById(Integer[] ids) {
		return goodsCouponConfigDao.selectGoodsIdsById(ids);
	}

	@Override
	public Integer[] save(String normalMatching, String activityMatching, Integer[] goodsIds) {
		try{
			SysUserEntity user = ShiroUtils.getUserEntity();
			//校验商品是否存在规格参数
			List<GoodsCouponConfigEntity>  goodsCouponConfigEntity = goodsCouponConfigDao.selectGoodsIdsById(goodsIds);
			if (CollectionUtils.isNotEmpty(goodsCouponConfigEntity)) {
				Integer[] goodsIDS = new Integer[goodsCouponConfigEntity.size()];
				for(int i=0;i<goodsCouponConfigEntity.size();i++){
					goodsIDS[i] = goodsCouponConfigEntity.get(i).getGoodsId();
				}
				if(goodsIDS.length>0){ //存在有配比的商品
					return goodsIDS;
		        }
			}
			//保存配比信息
			for(int i =0;i<goodsIds.length;i++){
				GoodsCouponConfigEntity gccf = new GoodsCouponConfigEntity();
				gccf.setGoodsId(goodsIds[i]);
				gccf.setNormalMatching(Double.parseDouble(normalMatching));
				gccf.setActivityMatching(Double.parseDouble(activityMatching));
				gccf.setDelFlag("0");
				gccf.setCreateUserDeptId(user.getDeptId());
				gccf.setCreateUserId(user.getUserId());
				gccf.setUpdateUserId(user.getUserId());
				gccf.setUpdateTime(new Date());
				goodsCouponConfigDao.save(gccf);
			}
			return new Integer[0]; //返回空表示成功
		}catch(Exception e){
			e.printStackTrace();
		}
		return new Integer[0];
	}
}
