package com.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.platform.dao.GoodsSpecificationDao;
import com.platform.entity.GoodsSpecificationEntity;
import com.platform.entity.SysUserEntity;
import com.platform.service.GoodsSpecificationService;
import com.platform.utils.ShiroUtils;

/**
 * 商品对应规格表值表Service实现类
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-31 11:15:55
 */
@Service("goodsSpecificationService")
public class GoodsSpecificationServiceImpl implements GoodsSpecificationService {
    @Autowired
    private GoodsSpecificationDao goodsSpecificationDao;

    @Override
    public GoodsSpecificationEntity queryObject(Integer id) {
        return goodsSpecificationDao.queryObject(id);
    }

    @Override
    public List<GoodsSpecificationEntity> queryList(Map<String, Object> map) {
    	
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
    	
   
    	  
    	  
        return goodsSpecificationDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return goodsSpecificationDao.queryTotal(map);
    }

    @Override
    public int save(GoodsSpecificationEntity goodsSpecification) {
        return goodsSpecificationDao.save(goodsSpecification);
    }

    @Override
    public int update(GoodsSpecificationEntity goodsSpecification) {
        return goodsSpecificationDao.update(goodsSpecification);
    }

    @Override
    public int delete(Integer id) {
        return goodsSpecificationDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[]ids) {
        return goodsSpecificationDao.deleteBatch(ids);
    }

	@Override
	public List<GoodsSpecificationEntity> querySpecificationByGoodId(Integer goodId) {
		
		return goodsSpecificationDao.querySpecificationByGoodId(goodId);
	}

	@Override
	public List<GoodsSpecificationEntity> findgoodsSpecification(String[] goodsSpecificationIdsArray) {

		return goodsSpecificationDao.findgoodsSpecification(goodsSpecificationIdsArray);
	}
}
