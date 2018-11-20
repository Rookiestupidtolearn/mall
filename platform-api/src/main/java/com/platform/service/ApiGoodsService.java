package com.platform.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.ApiCartMapper;
import com.platform.dao.ApiGoodsMapper;
import com.platform.entity.CartVo;
import com.platform.entity.GoodsVo;

import jline.internal.Log;


@Service
public class ApiGoodsService {
    @Autowired
    private ApiGoodsMapper goodsDao;
    @Autowired
	private ApiCartMapper cartDao;
	@Autowired
	private ApiCartService apiCartService;


    public GoodsVo queryObject(Integer id) {
        return goodsDao.queryObject(id);
    }


    public List<GoodsVo> queryList(Map<String, Object> map) {
        return goodsDao.queryList(map);
    }


    public int queryTotal(Map<String, Object> map) {
        return goodsDao.queryTotal(map);
    }


    public void save(GoodsVo goods) {
        goodsDao.save(goods);
    }


    public void update(GoodsVo goods) {
        goodsDao.update(goods);
    }


    public void delete(Integer id) {
        goodsDao.delete(id);
    }


    public void deleteBatch(Integer[] ids) {
        goodsDao.deleteBatch(ids);
    }

    public List<GoodsVo> queryHotGoodsList(Map<String, Object> map) {
        return goodsDao.queryHotGoodsList(map);
    }

    public List<GoodsVo> queryCatalogProductList(Map<String, Object> map) {
        return goodsDao.queryCatalogProductList(map);
    }
    
    /**
     * 批量商品下架
     * @param ids
     * @return
     */
    public int unSaleBatch(Integer[] ids) {
		List<GoodsVo> goodsList = goodsDao.queryGoodsList(ids);
		if(CollectionUtils.isEmpty(goodsList)){
			return 0;
		}
		for(GoodsVo goodsVo : goodsList){
			if(1 == goodsVo.getIs_on_sale()){ //不是上架中的商品处理上架
				//商品下架,删除购物车中对应的商品信息。并且回滚平台币和删除优惠券
				//查询购物车中对应的商品信息。
				List<CartVo> checkedCartList = cartDao.queryCartListByGoodsId(goodsVo.getId(),1); //购物车中是选中状态的商品数据
				List<CartVo> noCheckedCartList = cartDao.queryCartListByGoodsId(goodsVo.getId(),0); //购物车中非选中状态的商品数据
				if(CollectionUtils.isNotEmpty(checkedCartList)){
					//遍历集合，当购物车的商品为勾选状态则请求 接口，否则直接下架不给予退回平台币
					Integer[] CartEntityIds = new Integer[checkedCartList.size()];
					for(int i = 0;i<checkedCartList.size();i++){
						CartEntityIds[i] = checkedCartList.get(i).getId();
					}
					Boolean boo = apiCartService.roolbackAllCartsCoupons(CartEntityIds); //请求退回平台币并删除优惠券
					if(boo){
						//开始清除购物车中的商品信息
						int delNum = cartDao.deleteBatch(CartEntityIds);
						Log.info("【批量商品下架】回滚平台币并删除购物车中对应商品id为"+goodsVo.getId()+"的商品共"+delNum+"条");
					}else{
						Log.info("【批量商品下架】退回平台币并删除优惠券失败");
					}
				}else{
					Log.info("【批量商品下架】购物车中没有查找到选中状态商品id为"+goodsVo.getId()+"的商品");
				}
				
				//非选中商品不回滚平台币，直接清除购物车并下架
				if(CollectionUtils.isNotEmpty(noCheckedCartList)){
					Integer[] CartEntityIds = new Integer[noCheckedCartList.size()];
					for(int i = 0;i<noCheckedCartList.size();i++){
						CartEntityIds[i] = noCheckedCartList.get(i).getId();
					}
					//开始清除购物车中的商品信息
					int delNum = cartDao.deleteBatch(CartEntityIds);
					Log.info("【批量商品下架】删除购物车中未选中商品-对应商品id为"+goodsVo.getId()+"的商品共"+delNum+"条");
				}else{
					Log.info("【批量商品下架】购物车中没有查找到未选中状态商品id为"+goodsVo.getId()+"的商品");
				}
				goodsVo.setIs_on_sale(0);
				goodsVo.setUpdate_time(new Date());
				goodsDao.update(goodsVo);
			}
		}
		return 1;
	}
    
    /**
     * 批量申请上架
     * @param ids
     * @return
     */
    public int applySaleBatch(Integer[] ids) {
		List<GoodsVo> goodsList = goodsDao.queryGoodsList(ids);
		if(CollectionUtils.isEmpty(goodsList)){
			return 0;
		}
		for(GoodsVo goodsVo : goodsList){
			if(1 != goodsVo.getIs_on_sale() && 2 != goodsVo.getIs_on_sale() && -1 != goodsVo.getIs_on_sale()){
				goodsVo.setIs_on_sale(2);
				goodsVo.setUpdate_time(new Date());
				goodsDao.update(goodsVo);
			}
		}
		return 1;
	}
}
