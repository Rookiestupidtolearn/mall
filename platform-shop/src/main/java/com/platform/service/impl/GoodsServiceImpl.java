package com.platform.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.platform.annotation.DataFilter;
import com.platform.dao.CartDao;
import com.platform.dao.GoodsAttributeDao;
import com.platform.dao.GoodsDao;
import com.platform.dao.GoodsGalleryDao;
import com.platform.dao.ProductDao;
import com.platform.entity.CartEntity;
import com.platform.entity.GoodsAttributeEntity;
import com.platform.entity.GoodsEntity;
import com.platform.entity.GoodsGalleryEntity;
import com.platform.entity.GoodsPureInterestRateEntity;
import com.platform.entity.ProductEntity;
import com.platform.entity.SysUserEntity;
import com.platform.service.ApiCartService;
import com.platform.service.GoodsPureInterestRateService;
import com.platform.service.GoodsService;
import com.platform.utils.RRException;
import com.platform.utils.ShiroUtils;

import jline.internal.Log;

/**
 * Service实现类
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-21 21:19:49
 */
@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {
	@Autowired
	private GoodsDao goodsDao;
	@Autowired
	private GoodsAttributeDao goodsAttributeDao;
	@Autowired
	private ProductDao productDao;
	@Autowired
	private GoodsGalleryDao goodsGalleryDao;
	@Autowired
	private CartDao cartDao;
	@Autowired
	private ApiCartService apiCartService;
	@Autowired
	private GoodsPureInterestRateService goodsPureInterestRateService;

	@Override
	public GoodsEntity queryObject(Integer id) {
		return goodsDao.queryObject(id);
	}
	/*userAlias = "nideshop_goods.create_user_id",*/
	@Override
	
	@DataFilter( deptAlias = "nideshop_goods.create_user_dept_id")
	public List<GoodsEntity> queryList(Map<String, Object> map) {
		return goodsDao.queryList(map);
	}

	@Override
	@DataFilter(deptAlias = "nideshop_goods.create_user_dept_id")
	public int queryTotal(Map<String, Object> map) {
		return goodsDao.queryTotal(map);
	}

	@Override
	@Transactional
	public int save(GoodsEntity goods) {
		SysUserEntity user = ShiroUtils.getUserEntity();
		Map<String, Object> map = new HashMap<>();
		map.put("name", goods.getName());
		List<GoodsEntity> list = queryList(map);
		if (null != list && list.size() != 0) {
			throw new RRException("商品名称已存在！");
		}
		Integer id = goodsDao.queryMaxId() + 1;
		goods.setId(id);

		// 保存产品信息
		ProductEntity productEntity = new ProductEntity();
		productEntity.setGoodsId(id);
		productEntity.setGoodsSn(goods.getGoodsSn());
		productEntity.setGoodsNumber(goods.getGoodsNumber());
		productEntity.setRetailPrice(goods.getRetailPrice());
		productEntity.setMarketPrice(goods.getMarketPrice());
		productEntity.setGoodsSpecificationIds("");
		productDao.save(productEntity);

		goods.setAddTime(new Date());
		goods.setPrimaryProductId(productEntity.getId());

		// 保存商品详情页面显示的属性
		List<GoodsAttributeEntity> attributeEntityList = goods.getAttributeEntityList();
		if (null != attributeEntityList && attributeEntityList.size() > 0) {
			for (GoodsAttributeEntity item : attributeEntityList) {
				item.setGoodsId(id);
				goodsAttributeDao.save(item);
			}
		}

		// 商品轮播图
		List<GoodsGalleryEntity> galleryEntityList = goods.getGoodsImgList();
		if (null != galleryEntityList && galleryEntityList.size() > 0) {
			for (GoodsGalleryEntity galleryEntity : galleryEntityList) {
				galleryEntity.setGoodsId(id);
				goodsGalleryDao.save(galleryEntity);
			}
		}

//		isOnSale
		if(goods.getIsOnSale()==null){
			goods.setIsOnSale(-1);
		}
		
		//保存商品毛利率表
		GoodsPureInterestRateEntity goodsPureInterestRateEntity = new GoodsPureInterestRateEntity();
		goodsPureInterestRateEntity.setGoodsId(goods.getId());
		goodsPureInterestRateEntity.setProductId(0); //无规格id
		goodsPureInterestRateEntity.setMarketPrice(goods.getMarketPrice());
		goodsPureInterestRateEntity.setRetailPrice(goods.getRetailPrice());
		goodsPureInterestRateEntity.setCreateUserId(user.getUserId());
		goodsPureInterestRateEntity.setCreateTime(new Date());
		goodsPureInterestRateEntity.setMemo("");
		//计算毛利息
		double PureInterestRate = goods.getMarketPrice().subtract(goods.getRetailPrice()).divide(goods.getRetailPrice(),2,BigDecimal.ROUND_HALF_UP).doubleValue();
		goodsPureInterestRateEntity.setPureInterestRate(PureInterestRate);
		goodsPureInterestRateService.save(goodsPureInterestRateEntity);
		
		
		goods.setIsDelete(0);
		goods.setCreateUserDeptId(user.getDeptId());
		goods.setCreateUserId(user.getUserId());
		goods.setUpdateUserId(user.getUserId());
		goods.setUpdateTime(new Date());
		return goodsDao.save(goods);
	}

	@Override
	@Transactional
	public int update(GoodsEntity goods) {
		if(1==goods.getIsOnSale()){
			throw new RRException("此商品已处于上架状态！,不能修改");
		}
		SysUserEntity user = ShiroUtils.getUserEntity();
		List<GoodsAttributeEntity> attributeEntityList = goods.getAttributeEntityList();
		if (null != attributeEntityList && attributeEntityList.size() > 0) {
			for (GoodsAttributeEntity goodsAttributeEntity : attributeEntityList) {
				int result = goodsAttributeDao.updateByGoodsIdAttributeId(goodsAttributeEntity);
				if (result == 0) {
					goodsAttributeDao.save(goodsAttributeEntity);
				}
			}
		}
		goods.setUpdateUserId(user.getUserId());
		goods.setUpdateTime(new Date());
		// 商品轮播图
		// 修改时全删全插
		List<GoodsGalleryEntity> galleryEntityList = goods.getGoodsImgList();
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("goodsId", goods.getId());
		goodsGalleryDao.deleteByGoodsId(map);

		if (null != galleryEntityList && galleryEntityList.size() > 0) {
			for (GoodsGalleryEntity galleryEntity : galleryEntityList) {
				galleryEntity.setGoodsId(goods.getId());
				goodsGalleryDao.save(galleryEntity);
			}
		}
		//校验如果修改了商品价格，则需更新商品毛利率、商品规格表(sku)
		GoodsEntity goodsEntity = goodsDao.queryObject(goods.getId());
		if(!goodsEntity.getMarketPrice().toString().equals(goods.getMarketPrice().toString()) || !goodsEntity.getRetailPrice().toString().equals(goods.getRetailPrice().toBigInteger())){
			//查出商品规格对应的毛利率
        	HashMap<String, Object> paramMap = new HashMap<>();
        	paramMap.put("goodsId", goods.getId());
        	List<GoodsPureInterestRateEntity> GoodsPureInterestRateList = goodsPureInterestRateService.queryAll(paramMap);
        	if(CollectionUtils.isNotEmpty(GoodsPureInterestRateList)){
        		GoodsPureInterestRateEntity goodsPureInterestRateEntity = GoodsPureInterestRateList.get(0);
        		goodsPureInterestRateEntity.setMarketPrice(goods.getMarketPrice());
        		goodsPureInterestRateEntity.setRetailPrice(goods.getRetailPrice());
        		double PureInterestRate = goods.getMarketPrice().subtract(goods.getRetailPrice()).divide(goods.getRetailPrice(),2,BigDecimal.ROUND_HALF_UP).doubleValue();
        		goodsPureInterestRateEntity.setPureInterestRate(PureInterestRate);
        		goodsPureInterestRateService.update(goodsPureInterestRateEntity);
        	}else{
        		throw new RuntimeException("【修改商品价格】获取商品毛利率异常");
        	}
        	
        	//更新商品规格
        	paramMap = new HashMap<>();
        	paramMap.put("goodsSpecificationIds","");
        	paramMap.put("goodsId",goods.getId());
        	List<ProductEntity> productList =  productDao.queryList(paramMap);
        	for(ProductEntity productEntity :productList){
        		productEntity.setMarketPrice(goods.getMarketPrice());
        		productEntity.setRetailPrice(goods.getRetailPrice());
        		productEntity.setGoodsNumber(goods.getGoodsNumber());
        		productDao.update(productEntity);
        	}
		}
		return goodsDao.update(goods);
	}

	@Override
	public int delete(Integer id) {
		SysUserEntity user = ShiroUtils.getUserEntity();
		GoodsEntity goodsEntity = queryObject(id);
		goodsEntity.setIsDelete(1);
		goodsEntity.setIsOnSale(0);
		goodsEntity.setUpdateUserId(user.getUserId());
		goodsEntity.setUpdateTime(new Date());
		return goodsDao.update(goodsEntity);
	}

	@Override
	@Transactional
	public int deleteBatch(Integer[] ids) {
		//删除商品对应的毛利率信息
		goodsPureInterestRateService.delBygoodsIds(ids);
		int result = 0;
		for (Integer id : ids) {
			result += delete(id);
		}
		return result;
	}

	@Override
	@Transactional
	public int back(Integer[] ids) {
		SysUserEntity user = ShiroUtils.getUserEntity();
		int result = 0;
		for (Integer id : ids) {
			GoodsEntity goodsEntity = queryObject(id);
			goodsEntity.setIsDelete(0);
			goodsEntity.setIsOnSale(1);
			goodsEntity.setUpdateUserId(user.getUserId());
			goodsEntity.setUpdateTime(new Date());
			result += goodsDao.update(goodsEntity);
		}
		return result;
	}

	@Override
	public int enSale(Integer id) {
		SysUserEntity user = ShiroUtils.getUserEntity();
		GoodsEntity goodsEntity = queryObject(id);
		if (1 == goodsEntity.getIsOnSale()) {
			throw new RRException("此商品已处于上架状态！");
		}
		goodsEntity.setIsOnSale(1);
		goodsEntity.setUpdateUserId(user.getUserId());
		goodsEntity.setUpdateTime(new Date());
		return goodsDao.update(goodsEntity);
	}

	@Override
	public int unSale(Integer id) {
		SysUserEntity user = ShiroUtils.getUserEntity();
		GoodsEntity goodsEntity = queryObject(id);
		if (0 == goodsEntity.getIsOnSale()) {
			throw new RRException("此商品已处于下架状态！");
		}
		//商品下架,删除购物车中对应的商品信息。并且回滚平台币和删除优惠券
		//查询购物车中对应的商品信息。
		List<CartEntity> checkedCartList = cartDao.queryCartListByGoodsId(goodsEntity.getId(),1); //购物车中是选中状态的商品数据
		List<CartEntity> noCheckedCartList = cartDao.queryCartListByGoodsId(goodsEntity.getId(),0); //购物车中非选中状态的商品数据
		if(CollectionUtils.isNotEmpty(checkedCartList)){
			//遍历集合，当购物车的商品为勾选状态则请求 接口，否则直接下架不给予退回平台币
			Integer[] CartEntityIds = new Integer[checkedCartList.size()];
			for(int i = 0;i<checkedCartList.size();i++){
				CartEntityIds[i] = checkedCartList.get(i).getId();
			}
			Boolean boo = apiCartService.roolbackAllCartsCoupons(CartEntityIds,0); //请求退回平台币并删除优惠券
			if(boo){
				//开始清除购物车中的商品信息
				int delNum = cartDao.deleteBatch(CartEntityIds);
				Log.info("【商品下架】回滚平台币并删除购物车中对应商品id为"+goodsEntity.getId()+"的商品共"+delNum+"条");
			}else{
				Log.info("【商品下架】退回平台币并删除优惠券失败");
			}
		}else{
			Log.info("【商品下架】购物车中没有查找到选中转态商品id为"+goodsEntity.getId()+"的商品");
		}
		
		//非选中商品不回滚平台币，直接清除购物车并下架
		if(CollectionUtils.isNotEmpty(noCheckedCartList)){
			Integer[] CartEntityIds = new Integer[noCheckedCartList.size()];
			for(int i = 0;i<noCheckedCartList.size();i++){
				CartEntityIds[i] = noCheckedCartList.get(i).getId();
			}
			//开始清除购物车中的商品信息
			int delNum = cartDao.deleteBatch(CartEntityIds);
			Log.info("【商品下架】删除购物车中未选中商品-对应商品id为"+goodsEntity.getId()+"的商品共"+delNum+"条");
		}else{
			Log.info("【商品下架】购物车中没有查找到未选中状态商品id为"+goodsEntity.getId()+"的商品");
		}
		goodsEntity.setIsOnSale(0);
		goodsEntity.setUpdateUserId(user.getUserId());
		goodsEntity.setUpdateTime(new Date());
		return goodsDao.update(goodsEntity);
	}

	@Override
	public int applySale(Integer id) {
		
		SysUserEntity user = ShiroUtils.getUserEntity();
		GoodsEntity goodsEntity = queryObject(id);
		if (-1 == goodsEntity.getIsOnSale()) {
			throw new RRException("此商品未申请上架！");
		}
		
		if (2 == goodsEntity.getIsOnSale()) {
			throw new RRException("此商品已处于申请上架！");
		}
		if (1 == goodsEntity.getIsOnSale()) {
			throw new RRException("此商品已处于上架状态！");
		}
		
		goodsEntity.setIsOnSale(2);
		goodsEntity.setUpdateUserId(user.getUserId());
		goodsEntity.setUpdateTime(new Date());
		return goodsDao.update(goodsEntity);
	}
	
	
	@Override
	public int applyUnSale(Integer id) {
		
		SysUserEntity user = ShiroUtils.getUserEntity();
		GoodsEntity goodsEntity = queryObject(id);
		if (0 == goodsEntity.getIsOnSale()) {
			throw new RRException("此商品已处于下架状态！");
		}
		if (3 == goodsEntity.getIsOnSale()) {
			throw new RRException("此商品已处于申请下架状态！");
		}
		if (-1 == goodsEntity.getIsOnSale()) {
			throw new RRException("此商品处于编辑状态！");
		}
		goodsEntity.setIsOnSale(3);
		goodsEntity.setUpdateUserId(user.getUserId());
		goodsEntity.setUpdateTime(new Date());
		return goodsDao.update(goodsEntity);
	}
	@Override
	public List<GoodsEntity> queryGoodsList(Integer[] ids) {
		return goodsDao.queryGoodsList(ids);
	}
	@Override
	public int enSaleBatch(Integer[] ids) {
		SysUserEntity user = ShiroUtils.getUserEntity();
		List<GoodsEntity> goodsList = goodsDao.queryGoodsList(ids);
		if(CollectionUtils.isEmpty(goodsList)){
			return 0;
		}
		for(GoodsEntity goodsEntity : goodsList){
			if(1 != goodsEntity.getIsOnSale()){ //不是上架中的商品处理上架
				/*if(!"".equals(goodsEntity.getGoodsSn()) && goodsEntity.getIsOnSale() == 0){ //jd是下架状态的商品不能上架
					continue;
					//
				}*/
				goodsEntity.setIsOnSale(1);
				goodsEntity.setUpdateTime(new Date());
				goodsEntity.setUpdateUserId(user.getUserId());
				goodsDao.update(goodsEntity);
			}
		}
		return 1;		
	}
	
	@Override
	public int unSaleBatch(Integer[] ids) {
		SysUserEntity user = ShiroUtils.getUserEntity();
		List<GoodsEntity> goodsList = goodsDao.queryGoodsList(ids);
		if(CollectionUtils.isEmpty(goodsList)){
			return 0;
		}
		for(GoodsEntity goodsEntity : goodsList){
			if(1 == goodsEntity.getIsOnSale()){ //不是上架中的商品处理上架
				//商品下架,删除购物车中对应的商品信息。并且回滚平台币和删除优惠券
				//查询购物车中对应的商品信息。
				List<CartEntity> checkedCartList = cartDao.queryCartListByGoodsId(goodsEntity.getId(),1); //购物车中是选中状态的商品数据
				List<CartEntity> noCheckedCartList = cartDao.queryCartListByGoodsId(goodsEntity.getId(),0); //购物车中非选中状态的商品数据
				if(CollectionUtils.isNotEmpty(checkedCartList)){
					//遍历集合，当购物车的商品为勾选状态则请求 接口，否则直接下架不给予退回平台币
					Integer[] CartEntityIds = new Integer[checkedCartList.size()];
					for(int i = 0;i<checkedCartList.size();i++){
						CartEntityIds[i] = checkedCartList.get(i).getId();
					}
					Boolean boo = apiCartService.roolbackAllCartsCoupons(CartEntityIds,0); //请求退回平台币并删除优惠券
					if(boo){
						//开始清除购物车中的商品信息
						int delNum = cartDao.deleteBatch(CartEntityIds);
						Log.info("【批量商品下架】回滚平台币并删除购物车中对应商品id为"+goodsEntity.getId()+"的商品共"+delNum+"条");
					}else{
						Log.info("【批量商品下架】退回平台币并删除优惠券失败");
					}
				}else{
					Log.info("【批量商品下架】购物车中没有查找到选中状态商品id为"+goodsEntity.getId()+"的商品");
				}
				
				//非选中商品不回滚平台币，直接清除购物车并下架
				if(CollectionUtils.isNotEmpty(noCheckedCartList)){
					Integer[] CartEntityIds = new Integer[noCheckedCartList.size()];
					for(int i = 0;i<noCheckedCartList.size();i++){
						CartEntityIds[i] = noCheckedCartList.get(i).getId();
					}
					//开始清除购物车中的商品信息
					int delNum = cartDao.deleteBatch(CartEntityIds);
					Log.info("【批量商品下架】删除购物车中未选中商品-对应商品id为"+goodsEntity.getId()+"的商品共"+delNum+"条");
				}else{
					Log.info("【批量商品下架】购物车中没有查找到未选中状态商品id为"+goodsEntity.getId()+"的商品");
				}
				
				goodsEntity.setIsOnSale(0);
				goodsEntity.setUpdateTime(new Date());
				goodsEntity.setUpdateUserId(user.getUserId());
				goodsDao.update(goodsEntity);
			}
		}
		return 1;
	}
	
	@Override
	public int applyUnSaleBatch(Integer[] ids) {
		SysUserEntity user = ShiroUtils.getUserEntity();
		List<GoodsEntity> goodsList = goodsDao.queryGoodsList(ids);
		if(CollectionUtils.isEmpty(goodsList)){
			return 0;
		}
		for(GoodsEntity goodsEntity : goodsList){
			if(0 != goodsEntity.getIsOnSale() && 3 != goodsEntity.getIsOnSale() && -1 != goodsEntity.getIsOnSale()){
				goodsEntity.setIsOnSale(3);
				goodsEntity.setUpdateUserId(user.getUserId());
				goodsEntity.setUpdateTime(new Date());
				goodsDao.update(goodsEntity);
			}
		}
		return 1;
	}
	
	@Override
	public int applySaleBatch(Integer[] ids) {
		SysUserEntity user = ShiroUtils.getUserEntity();
		List<GoodsEntity> goodsList = goodsDao.queryGoodsList(ids);
		if(CollectionUtils.isEmpty(goodsList)){
			return 0;
		}
		for(GoodsEntity goodsEntity : goodsList){
			if(1 != goodsEntity.getIsOnSale() && 2 != goodsEntity.getIsOnSale() && -1 != goodsEntity.getIsOnSale()){
				goodsEntity.setIsOnSale(2);
				goodsEntity.setUpdateUserId(user.getUserId());	
				goodsEntity.setUpdateTime(new Date());
				goodsDao.update(goodsEntity);
			}
		}
		return 1;
	}
}
