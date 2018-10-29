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
import com.platform.entity.CartEntity;
import com.platform.entity.GoodsCouponConfigEntity;
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
    @Autowired
    private CartDao cartDao;
    @Autowired
	private ApiCartService apiCartService;

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
    	
		Map<String, Object> map = new HashMap<>();
//		map.put("id",goodsCouponConfig.getId());
//		List<GoodsCouponConfigEntity> list = queryList(map);
		
		GoodsCouponConfigEntity  goodsCouponConfigEntity = goodsCouponConfigDao.queryObject(goodsCouponConfig.getId());
		
		if (null != goodsCouponConfigEntity) {
			throw new RRException("商品配比已存在！");
		}
		
		if(goodsCouponConfig.getGoodValue()<0||goodsCouponConfig.getGoodValue()>1){
			throw new RRException("商品配比值为大于0且小于等于1");
		}
		
		//新增商品配配比，删除购物车中对应的商品，以及回滚余额、删除优惠券
		List<CartEntity> cartList = cartDao.queryCartListByGoodsId(goodsCouponConfigEntity.getGoodsId());
		if(CollectionUtils.isNotEmpty(cartList)){
			Integer[] CartEntityIds = new Integer[cartList.size()];
			for(int i = 0;i<cartList.size();i++){
				CartEntityIds[i] = cartList.get(i).getId();
			}
			Boolean boo = apiCartService.roolbackAllCartsCoupons(CartEntityIds); //请求退回平台币并删除优惠券
			if(boo){
				//开始清除购物车中的商品信息
				int delNum = cartDao.deleteBatch(CartEntityIds);
				Log.info("【新增商品配比】删除购物车中对应商品id为"+goodsCouponConfigEntity.getGoodsId()+"的商品共"+delNum+"条");
			}else{
				Log.info("【新增商品配比】退回平台币并删除优惠券失败");
			}
		}else{
			Log.info("【新增商品配比】购物车中没有查找到商品id为"+goodsCouponConfigEntity.getGoodsId()+"的商品");
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
    	//新增商品配配比，删除购物车中对应的商品，以及回滚余额、删除优惠券
			List<CartEntity> cartList = cartDao.queryCartListByGoodsId(goodsCouponConfig.getGoodsId());
			if(CollectionUtils.isNotEmpty(cartList)){
				Integer[] CartEntityIds = new Integer[cartList.size()];
				for(int i = 0;i<cartList.size();i++){
					CartEntityIds[i] = cartList.get(i).getId();
				}
				Boolean boo = apiCartService.roolbackAllCartsCoupons(CartEntityIds); //请求退回平台币并删除优惠券
				if(boo){
					//开始清除购物车中的商品信息
					int delNum = cartDao.deleteBatch(CartEntityIds);
					Log.info("【修改商品配比】删除购物车中对应商品id为"+goodsCouponConfig.getGoodsId()+"的商品共"+delNum+"条");
				}else{
					Log.info("【修改商品配比】退回平台币并删除优惠券失败");
				}
			}else{
				Log.info("【修改商品配比】购物车中没有查找到商品id为"+goodsCouponConfig.getGoodsId()+"的商品");
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
}
