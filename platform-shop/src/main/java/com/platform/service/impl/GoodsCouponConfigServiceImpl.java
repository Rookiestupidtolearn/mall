package com.platform.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.GoodsCouponConfigDao;
import com.platform.dao.GoodsDao;
import com.platform.entity.GoodsCouponConfigEntity;
import com.platform.entity.GoodsEntity;
import com.platform.entity.SysUserEntity;
import com.platform.service.GoodsCouponConfigService;
import com.platform.service.GoodsService;
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
    @Autowired
    private GoodsDao goodDao;
    @Autowired
    private GoodsService goodService;
    
    
    private Logger log = Logger.getLogger(GoodsCouponConfigServiceImpl.class);

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
		if(goodsCouponConfig.getNormalMatching()<0||goodsCouponConfig.getNormalMatching()>1){
			throw new RRException("正常配比值应为大于0且小于等于1");
		}
		if(goodsCouponConfig.getActivityMatching()<0||goodsCouponConfig.getActivityMatching()>1){
			throw new RRException("活动配比值应为大于0且小于等于1");
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
    	
    	if(goodsCouponConfig.getNormalMatching()<0||goodsCouponConfig.getNormalMatching()>1){
			throw new RRException("正常配比值应为大于0且小于等于1");
		}
		if(goodsCouponConfig.getActivityMatching()<0||goodsCouponConfig.getActivityMatching()>1){
			throw new RRException("活动配比值应为大于0且小于等于1");
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
	public List<GoodsCouponConfigEntity> selectGoodsIdsByGoodsId(Integer[] ids) {
		return goodsCouponConfigDao.selectGoodsIdsByGoodsId(ids);
	}

	@Override
	public Integer[] save(String normalMatching, String activityMatching, Integer[] goodsIds) {
		try{
			SysUserEntity user = ShiroUtils.getUserEntity();
			//校验商品是否存在规格参数
			for(int i =0;i<goodsIds.length;i++){
				//重置配比需先下架商品
				GoodsEntity goodsEntity = null;
				GoodsEntity goods = goodDao.queryObject(goodsIds[i]);
				if(null == goods){
					log.info("【商品重置/新增 配比】为获取到商品id:"+goodsIds[i]+"的商品");
				}
				if(goods.getIsOnSale() == 1 ||goods.getIsOnSale() == 3){
					goodsEntity = goods;
					goodService.unSaleBatch(new Integer[]{goodsEntity.getId()});
				}
				List<GoodsCouponConfigEntity> goodsCouponConfigEntityList = goodsCouponConfigDao.selectGoodsIdsByGoodsId(new Integer[]{goodsIds[i]});
				if(CollectionUtils.isEmpty(goodsCouponConfigEntityList)){
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
				}else{
					for(GoodsCouponConfigEntity goodsCouponConfigEntity : goodsCouponConfigEntityList){
						goodsCouponConfigEntity.setNormalMatching(Double.parseDouble(normalMatching));
						goodsCouponConfigEntity.setActivityMatching(Double.parseDouble(activityMatching));
						goodsCouponConfigEntity.setUpdateTime(new Date());
						goodsCouponConfigEntity.setUpdateUserId(user.getUserId());
						goodsCouponConfigDao.update(goodsCouponConfigEntity);
					}
				}
				//设置完规格，将操作下架的商品更新为申请上架的状态
				if(null != goodsEntity){
					goodsEntity.setIsOnSale(2);
					goodDao.update(goodsEntity);
				}
			}
			return new Integer[0]; //返回空表示成功
		}catch(Exception e){
			e.printStackTrace();
		}
		return new Integer[0];
	}
}
