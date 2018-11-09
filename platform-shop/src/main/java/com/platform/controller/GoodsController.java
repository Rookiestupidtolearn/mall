package com.platform.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.util.StringUtil;
import com.platform.entity.GoodsEntity;
import com.platform.service.GoodsPureInterestRateService;
import com.platform.service.GoodsService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;

/**
 * Controller
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-21 21:19:49
 */
@RestController
@RequestMapping("goods")
public class GoodsController {
	
    @Autowired
    private GoodsService goodsService;
    
    @Autowired
	private GoodsPureInterestRateService goodsPureInterestRateService;
  
    

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("goods:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        query.put("isDelete", 0);
        List<GoodsEntity> goodsList = goodsService.queryList(query);
        //查询毛利率表
        String status = (String)params.get("status");
        String name = (String)params.get("name");
        String min_retail_price = (String)params.get("min_retail_price");
        String max_retail_price = (String)params.get("max_retail_price");
        String min_pure_interest_rate = (String) params.get("min_pure_interest_rate");
        String max_pure_interest_rate = (String) params.get("max_pure_interest_rate");
        if(StringUtil.isNotEmpty(min_retail_price) || StringUtil.isNotEmpty(max_retail_price) || StringUtil.isNotEmpty(min_pure_interest_rate) || StringUtil.isNotEmpty(max_pure_interest_rate)){
        	Map<String,Object> paramMap = new HashMap<String,Object>();
        	if(StringUtil.isNotEmpty(status) || StringUtil.isNotEmpty(name)){
        		Integer[] goodsIdArr = new Integer[goodsList.size()];
        		for(int i =0;i<goodsList.size();i++){
        			goodsIdArr[i] = goodsList.get(i).getId();
        		}
        		paramMap.put("goodsIds", goodsIdArr);
        	}
        	paramMap.put("min_retail_price",StringUtil.isEmpty(min_retail_price) ? "":min_retail_price);
        	paramMap.put("max_retail_price", StringUtil.isEmpty(max_retail_price) ? "":max_retail_price);
        	paramMap.put("min_pure_interest_rate", StringUtil.isEmpty(min_pure_interest_rate) ? "":min_pure_interest_rate);
        	paramMap.put("max_pure_interest_rate", StringUtil.isEmpty(max_pure_interest_rate) ? "":max_pure_interest_rate);
        	Integer[] goodsIds = goodsPureInterestRateService.queryGoodsIdsByPrice(paramMap);
        	if(goodsIds.length == 0){
        		params.put("goodss", new Integer[]{0} );
        	}else{
        		params.put("goodss", goodsIds);
        	}
        	
        	query = new Query(params);
        	query.put("isDelete", 0);
        	goodsList = goodsService.queryList(query);
        }
        
        int total = goodsService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(goodsList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("goods:info")
    public R info(@PathVariable("id") Integer id) {
        GoodsEntity goods = goodsService.queryObject(id);

 
        return R.ok().put("goods", goods);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("goods:save")
    public R save(@RequestBody GoodsEntity goods) {
    	
        goodsService.save(goods);
        
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("goods:update")
    public R update(@RequestBody GoodsEntity goods) {
        goodsService.update(goods);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("goods:delete")
    public R delete(@RequestBody Integer[] ids) {
        goodsService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        params.put("isDelete", 0);
        List<GoodsEntity> list = goodsService.queryList(params);

        return R.ok().put("list", list);
    }


    /**
     * 商品回收站
     *
     * @param params
     * @return
     */
    @RequestMapping("/historyList")
    public R historyList(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        query.put("isDelete", 1);
        List<GoodsEntity> goodsList = goodsService.queryList(query);
        int total = goodsService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(goodsList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 商品从回收站恢复
     */
    @RequestMapping("/back")
    @RequiresPermissions("goods:back")
    public R back(@RequestBody Integer[] ids) {
        goodsService.back(ids);

        return R.ok();
    }

    /**
     * 总计
     */
    @RequestMapping("/queryTotal")
    public R queryTotal(@RequestParam Map<String, Object> params) {

        params.put("isDelete", 0);
        int sum = goodsService.queryTotal(params);
        return R.ok().put("goodsSum", sum);
    }

    /**
     * 上架
     */
    @RequestMapping("/enSale")
    public R enSale(@RequestBody Integer id) {
        goodsService.enSale(id);

        return R.ok();
    }

    /**
     * 下架
     */
    @RequestMapping("/unSale")
    public R unSale(@RequestBody Integer id) {
        goodsService.unSale(id);

        return R.ok();
    }
    
    /**
     * 申请上架
     */
    @RequestMapping("/applyEnSale")
    public R applySale(@RequestBody Integer id) {
        goodsService.applySale(id);
        return R.ok();
    }
    
    @RequestMapping("/applyUnSale")
    public R applyUnSale(@RequestBody Integer id) {
        goodsService.applyUnSale(id);
        return R.ok();
    }
    
    
    
}
