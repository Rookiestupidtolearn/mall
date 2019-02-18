package com.platform.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.platform.entity.CategoryVo;
import com.platform.service.CategoryService;
import org.apache.commons.collections.CollectionUtils;
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

    @Autowired
    private CategoryService categoryService;


    /**
     * 查看列表
     */
  /*  @RequestMapping("/list")
    @RequiresPermissions("goods:list")
    public R list(@RequestParam Map<String, Object> params) {//{_search=false, nd=1545018244178, limit=10, page=1, sidx=null, order=asc, _=1545018243857, offset=0, isDelete=0}
        //查询列表数据
        Query query = new Query(params);
        query.put("isDelete", 0);
//        query.remove("page");
//        query.remove("limit");
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
        	query.put("page",Integer.parseInt(params.get("page").toString()));
        	query.put("limit",Integer.parseInt(params.get("limit").toString()));
        	goodsList = goodsService.queryList(query);
        }
        
        int total = goodsService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(goodsList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }*/


    //商品查询
    @RequestMapping("/list")
    @RequiresPermissions("goods:list")
    public R queryGoodsList(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);

        String three_category_id = (String) params.get("three_category_id");
        String two_category_id = (String) params.get("two_category_id");
        String one_category_id = (String) params.get("one_category_id");

        if ((StringUtil.isNotEmpty(one_category_id) && !StringUtil.isNotEmpty(two_category_id))) {
            Integer[] integers = new Integer[]{Integer.parseInt(one_category_id)};
            Integer[] categoryids1 = categoryService.queryCategoryListByParentId(integers);//查询一级分类
            Integer[] categoryids = categoryService.queryCategoryListByParentId(categoryids1);// 查询二级分类
            if (categoryids == null || categoryids.length == 0) {
                query.put("categoryids", categoryids1);
            } else {
                query.put("categoryids", categoryids);
            }
        } else if (((StringUtil.isNotEmpty(one_category_id) && StringUtil.isNotEmpty(two_category_id)))) {
            Integer[] integers = new Integer[]{Integer.parseInt(two_category_id)};
            Integer[] categoryids = categoryService.queryCategoryListByParentId(integers);
            if (categoryids.length > 0) {
                query.put("categoryids", categoryids);
            }
        }
        if ((StringUtil.isNotEmpty(three_category_id))) {
            query.put("categoryids", "");
            query.put("category_id", three_category_id);
        }


        query.put("isDelete", 0);
        String min_retail_price = (String) params.get("min_retail_price");
        String max_retail_price = (String) params.get("max_retail_price");
        String min_pure_interest_rate = (String) params.get("min_pure_interest_rate");
        String max_pure_interest_rate = (String) params.get("max_pure_interest_rate");
        List<GoodsEntity> goodsList = null;
        if (StringUtil.isNotEmpty(min_retail_price) || StringUtil.isNotEmpty(max_retail_price) || StringUtil.isNotEmpty(min_pure_interest_rate) || StringUtil.isNotEmpty(max_pure_interest_rate)) {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("min_retail_price", StringUtil.isEmpty(min_retail_price) ? "" : min_retail_price);
            paramMap.put("max_retail_price", StringUtil.isEmpty(max_retail_price) ? "" : max_retail_price);
            paramMap.put("min_pure_interest_rate", StringUtil.isEmpty(min_pure_interest_rate) ? "" : min_pure_interest_rate);
            paramMap.put("max_pure_interest_rate", StringUtil.isEmpty(max_pure_interest_rate) ? "" : max_pure_interest_rate);
            //查询 商品
            Integer[] goodsIds = goodsPureInterestRateService.queryGoodsIdsByPrice(paramMap);
            if (goodsIds.length > 0) {
                query.put("goodss", goodsIds);
            }
            goodsList = goodsService.queryList(query);
        } else {
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
    public R enSale(@RequestBody Integer[] ids) {
        int i = goodsService.enSaleBatch(ids);
        //goodsService.enSale(id);
        if (i == 0) {
            return R.error("未查询到对应的商品信息");
        }
        return R.ok();
    }

    /**
     * 下架
     */
    @RequestMapping("/unSale")
    public R unSale(@RequestBody Integer[] ids) {
        int i = goodsService.unSaleBatch(ids);
        if (i == 0) {
            return R.error("未查询到对应的商品信息");
        }
        return R.ok();
    }


    /**
     * 申请上架校验
     */
    @RequestMapping("/applyEnSaleVerify")
    public R applySaleVerify(@RequestBody Integer[] ids) {
        List<GoodsEntity> goodsList = goodsService.queryGoodsList(ids);
        if (CollectionUtils.isEmpty(goodsList)) {
            return R.error("未查询到对应的商品信息");
        }
        for (GoodsEntity goodsEntity : goodsList) {
            if (1 == goodsEntity.getIsOnSale() || 3 == goodsEntity.getIsOnSale()) {
                return R.error("包含已上架商品,不能更新为申请上架状态");
            }
        }
        return R.ok();
    }

    /**
     * 申请上架
     */
    @RequestMapping("/applyEnSale")
    public R applySale(@RequestBody Integer[] ids) {
    	/*List<GoodsEntity> goodsList = goodsService.queryGoodsList(ids);
		if(CollectionUtils.isEmpty(goodsList)){
			return R.error("未查询到对应的商品信息");
		}
		for(GoodsEntity goodsEntity : goodsList){
			if(1 == goodsEntity.getIsOnSale() || 3 == goodsEntity.getIsOnSale()){
				return R.error("包含已上架商品,不能更新为申请上架状态");
			}
		}*/
        int i = goodsService.applySaleBatch(ids);
        if (i == 0) {
            return R.error("未查询到对应的商品信息");
        }
        return R.ok();
    }


    /**
     * 申请下架校验
     *
     * @param ids
     * @return
     */
    @RequestMapping("/applyUnSaleVerify")
    public R applyUnSaleVerify(@RequestBody Integer[] ids) {
        List<GoodsEntity> goodsList = goodsService.queryGoodsList(ids);
        if (CollectionUtils.isEmpty(goodsList)) {
            return R.error("未查询到对应的商品信息");
        }
        for (GoodsEntity goodsEntity : goodsList) {
            if (0 == goodsEntity.getIsOnSale() || 2 == goodsEntity.getIsOnSale()) {
                return R.error("包含已下架商品,不能更新为申请下架状态");
            }
        }
        return R.ok();
    }


    /**
     * 申请下架
     *
     * @param ids
     * @return
     */
    @RequestMapping("/applyUnSale")
    public R applyUnSale(@RequestBody Integer[] ids) {
    	/*List<GoodsEntity> goodsList = goodsService.queryGoodsList(ids);
		if(CollectionUtils.isEmpty(goodsList)){
			return R.error("未查询到对应的商品信息");
		}
		for(GoodsEntity goodsEntity : goodsList){
			if(0 == goodsEntity.getIsOnSale() || 2 == goodsEntity.getIsOnSale()){
				return R.error("包含已下架商品,不能更新为申请下架状态");
			}
		}*/
        int i = goodsService.applyUnSaleBatch(ids);
        if (i == 0) {
            return R.error("未查询到对应的商品信息");
        }
        return R.ok();
    }


}
