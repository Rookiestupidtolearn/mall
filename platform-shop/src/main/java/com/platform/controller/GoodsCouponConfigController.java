package com.platform.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.platform.entity.GoodsCouponConfigEntity;
import com.platform.entity.GoodsEntity;
import com.platform.service.GoodsCouponConfigService;
import com.platform.service.GoodsService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;

/**
 * 产品-平台币配置表
Controller
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-10-12 11:42:14
 */
@RestController
@RequestMapping("goodscouponconfig")
public class GoodsCouponConfigController {
    @Autowired
    private GoodsCouponConfigService goodsCouponConfigService;
    @Autowired
    private GoodsService goodsService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("goodscouponconfig:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<GoodsCouponConfigEntity> goodsCouponConfigList = goodsCouponConfigService.queryList(query);
        
        
        int total = goodsCouponConfigService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(goodsCouponConfigList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("goodscouponconfig:info")
    public R info(@PathVariable("id") Integer id) {
        GoodsCouponConfigEntity goodsCouponConfig = goodsCouponConfigService.queryObject(id);

        return R.ok().put("goodsCouponConfig", goodsCouponConfig);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("goodscouponconfig:save")
    public R save(@RequestBody GoodsCouponConfigEntity goodsCouponConfig) {
    	//先校验商品是否已下架(只有下架了才能新增配比)
    	boolean boo = goodsIsoldOut(goodsCouponConfig.getGoodsId());
    	if(!boo){
    		return R.error("商品未下架不能新增配比值");
    	}
        goodsCouponConfigService.save(goodsCouponConfig);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("goodscouponconfig:update")
    public R update(@RequestBody GoodsCouponConfigEntity goodsCouponConfig) {
    	//先校验商品是否已下架(只有下架了才能修改配比)
    	boolean boo = goodsIsoldOut(goodsCouponConfig.getGoodsId());
    	if(!boo){
    		return R.error("商品未下架不能修改配比值");
    	}
        goodsCouponConfigService.update(goodsCouponConfig);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("goodscouponconfig:delete")
    public R delete(@RequestBody Integer[] ids) {
    	//判断要删除的配比值对应的商品是否下架
    	Integer[] goodsIds = goodsCouponConfigService.selectGoodsIdsById(ids);
    	if(goodsIds.length>0){
    		for(int i =0;i<goodsIds.length;i++){
    			//先校验商品是否已下架(只有下架了才能删除配比)
    			boolean boo = goodsIsoldOut(goodsIds[i]);
    	    	if(!boo){
    	    		return R.error("有未下架商品不能删除配比值");
    	    	}
    		}
    	}else{
    		return R.error("未查询到配比值对应的商品");
    	}
        goodsCouponConfigService.deleteBatch(ids);
        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<GoodsCouponConfigEntity> list = goodsCouponConfigService.queryList(params);

        return R.ok().put("list", list);
    }
    
    public boolean goodsIsoldOut(Integer goodsId){
    	boolean boo = true;
    	GoodsEntity  goodsEntrty = goodsService.queryObject(goodsId);
    	if(goodsEntrty != null){
    		if(goodsEntrty.getIsOnSale() == 1){ //是上架状态不能修改、新增、删除 配比
    			return boo = false;
    		}
    	}else{
    		//没有找到商品信息，不能修改、新增、删除 配比
    		return boo=false;
    	}
    	return boo;
    } 
    
}
