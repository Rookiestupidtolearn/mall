package com.platform.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.platform.dao.GoodsSpecificationDao;
import com.platform.dao.ProductDao;
import com.platform.entity.GoodsPureInterestRateEntity;
import com.platform.entity.GoodsSpecificationEntity;
import com.platform.entity.ProductEntity;
import com.platform.entity.SysUserEntity;
import com.platform.service.GoodsPureInterestRateService;
import com.platform.service.ProductService;
import com.platform.utils.ShiroUtils;
import com.platform.utils.StringUtils;

/**
 * Service实现类
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-30 14:31:21
 */
@Service("productService")
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private GoodsSpecificationDao goodsSpecificationDao;
    @Autowired
	private GoodsPureInterestRateService goodsPureInterestRateService;
    
    @Override
    public ProductEntity queryObject(Integer id) {
        return productDao.queryObject(id);
    }

    @Override
    public List<ProductEntity> queryList(Map<String, Object> map) {
    	/**
    	 *过滤部门
    	 *
    	 */
    	
    	SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
    	
    	Long  deptId  =null;
    	
    	if(sysUserEntity!=null && sysUserEntity.getUserId() != 1){
    		
    		  deptId = sysUserEntity.getDeptId();
    		  if(deptId!=null){
    			  map.put("deptId", deptId);
    		  }
    	}
    	
        List<ProductEntity> list = productDao.queryList(map);

        List<ProductEntity> result = new ArrayList<>();
        //翻译产品规格
        if (null != list && list.size() > 0) {
            for (ProductEntity item : list) {
                String specificationIds = item.getGoodsSpecificationIds();
                String specificationValue = "";
                if (!StringUtils.isNullOrEmpty(specificationIds)) {
                    String[] arr = specificationIds.split("_");

                    for (String goodsSpecificationId : arr) {
                        GoodsSpecificationEntity entity = goodsSpecificationDao.queryObject(goodsSpecificationId);
                        if (null != entity) {
                            specificationValue += entity.getValue() + "；";
                        }
                    }
                }
                item.setSpecificationValue(item.getGoodsName() + " " + specificationValue);
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return productDao.queryTotal(map);
    }

    @Override
    @Transactional
    public int save(ProductEntity product) {
    	SysUserEntity user = ShiroUtils.getUserEntity();
    	//校验此规格是否存在
    	Map paramMap = new HashMap();
    	paramMap.put("goods_specification_ids", product.getGoodsSpecificationIds());
    	int i = productDao.findProductIsHave(paramMap);
    	if(i>0){
    		return -1;
    	}
    	int num = productDao.save(product);
    	//保存商品毛利率表
		GoodsPureInterestRateEntity goodsPureInterestRateEntity = new GoodsPureInterestRateEntity();
		goodsPureInterestRateEntity.setGoodsId(product.getGoodsId());
		goodsPureInterestRateEntity.setProductId(product.getId()); 
		goodsPureInterestRateEntity.setMarketPrice(product.getMarketPrice());
		goodsPureInterestRateEntity.setRetailPrice(product.getRetailPrice());
		goodsPureInterestRateEntity.setCreateUserId(user.getUserId());
		goodsPureInterestRateEntity.setCreateTime(new Date());
		goodsPureInterestRateEntity.setMemo("");
		//计算毛利息
		double PureInterestRate = product.getMarketPrice().subtract(product.getRetailPrice()).divide(product.getRetailPrice(),2,BigDecimal.ROUND_HALF_UP).doubleValue();
		goodsPureInterestRateEntity.setPureInterestRate(PureInterestRate);
		goodsPureInterestRateService.save(goodsPureInterestRateEntity);
    	return num;
    	
    }

    @Override
    public int update(ProductEntity product) {
        if (StringUtils.isNullOrEmpty(product.getGoodsSpecificationIds())){
            product.setGoodsSpecificationIds("");
        }
        //如果产品规格的价格发生变化，则需要更新产品毛利率
        ProductEntity pd = productDao.queryObject(product.getId());
        if(!pd.getMarketPrice().toString().equals(product.getMarketPrice().toString()) || !pd.getRetailPrice().toString().equals(product.getRetailPrice().toBigInteger())){
        	//查出商品规格对应的毛利率
        	HashMap<String, Object> paramMap = new HashMap<>();
        	paramMap.put("goodsId", product.getGoodsId());
        	paramMap.put("productId", product.getId());
        	List<GoodsPureInterestRateEntity> GoodsPureInterestRateList = goodsPureInterestRateService.queryAll(paramMap);
        	if(CollectionUtils.isNotEmpty(GoodsPureInterestRateList)){
        		GoodsPureInterestRateEntity goodsPureInterestRateEntity = GoodsPureInterestRateList.get(0);
        		goodsPureInterestRateEntity.setMarketPrice(product.getMarketPrice());
        		goodsPureInterestRateEntity.setRetailPrice(product.getRetailPrice());
        		double PureInterestRate = product.getMarketPrice().subtract(product.getRetailPrice()).divide(product.getRetailPrice(),2,BigDecimal.ROUND_HALF_UP).doubleValue();
        		goodsPureInterestRateEntity.setPureInterestRate(PureInterestRate);
        		goodsPureInterestRateService.update(goodsPureInterestRateEntity);
        	}else{
        		throw new RuntimeException("【修改商品规格】获取商品毛利率异常");
        	}
        }
        return productDao.update(product);
    }

    @Override
    public int delete(Integer id) {
        return productDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
    	//删除商品规格对应的毛利率
    	goodsPureInterestRateService.delByProductIds(ids);
        return productDao.deleteBatch(ids);
    }
}
