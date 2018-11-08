package com.platform.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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
    @RequestMapping("/save/{normalMatching}/{activityMatching}/{goodsIds}")
    @RequiresPermissions("goodscouponconfig:save")
    public R save(@PathVariable("normalMatching") String normalMatching,@PathVariable("activityMatching") String activityMatching,@PathVariable("goodsIds") Integer[] goodsIds) {
    	if(StringUtils.isBlank(normalMatching) || StringUtils.isBlank(activityMatching) || goodsIds.length <= 0 ){
    		return R.error("配比数据异常");
    	}
    	StringBuffer sb = new StringBuffer();
    	List<GoodsEntity> goodsList = goodsService.queryGoodsList(goodsIds);
    	//先校验商品是否已下架(只有下架了才能新增配比)
    	for(GoodsEntity goodsEntrty : goodsList){
    		if(goodsEntrty.getIsOnSale() == 1){ //是上架状态不能修改、新增、删除 配比
    			sb.append(goodsEntrty.getName()+"、");
    		}
    	}
    	if(StringUtils.isNotBlank(sb.toString())){
    		String result = sb.toString().substring(0,sb.toString().length()-1);
    		return R.error("商品:"+result+"未下架不能新增配比值");
    	}
    	//批量保存商品配比
        String goodsIDS= goodsCouponConfigService.save(normalMatching,activityMatching,goodsIds);
        if(StringUtils.isNotBlank(goodsIDS)){ //存在有配比的商品
        	List<GoodsEntity> goodsEntityList = goodsService.queryGoodsList(goodsIds);
        	//先校验商品是否已下架(只有下架了才能新增配比)
        	for(GoodsEntity goodsEntrty : goodsEntityList){
        		sb.append(goodsEntrty.getName()+"、");
        	}
        	if(StringUtils.isNotBlank(sb.toString())){
        		String result = sb.toString().substring(0,sb.toString().length()-1);
        		return R.error("商品:"+result+"已存在配比值");
        	}
        }
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
    	List<GoodsCouponConfigEntity> li = goodsCouponConfigService.selectGoodsIdsById(ids);
    	if(li.size()>0){
    		for(GoodsCouponConfigEntity gccf : li){
    			//先校验商品是否已下架(只有下架了才能删除配比)
    			boolean boo = goodsIsoldOut(gccf.getGoodsId());
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
