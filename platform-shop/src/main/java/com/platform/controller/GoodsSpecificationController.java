package com.platform.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.platform.entity.GoodsSpecificationEntity;
import com.platform.service.GoodsSpecificationService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;

/**
 * 商品对应规格表值表Controller
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-31 11:15:55
 */
@RestController
@RequestMapping("goodsspecification")
public class GoodsSpecificationController {
	@Autowired
	private GoodsSpecificationService goodsSpecificationService;

	/**
	 * 查看列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("goodsspecification:list")
	public R list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);

		List<GoodsSpecificationEntity> goodsSpecificationList = goodsSpecificationService.queryList(query);
		int total = goodsSpecificationService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(goodsSpecificationList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 查看信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("goodsspecification:info")
	public R info(@PathVariable("id") Integer id) {
		GoodsSpecificationEntity goodsSpecification = goodsSpecificationService.queryObject(id);

		return R.ok().put("goodsSpecification", goodsSpecification);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("goodsspecification:save")
	public R save(@RequestBody GoodsSpecificationEntity goodsSpecification) {
		goodsSpecificationService.save(goodsSpecification);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("goodsspecification:update")
	public R update(@RequestBody GoodsSpecificationEntity goodsSpecification) {
		goodsSpecificationService.update(goodsSpecification);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("goodsspecification:delete")
	public R delete(@RequestBody Integer[] ids) {
		goodsSpecificationService.deleteBatch(ids);

		return R.ok();
	}

	/**
	 * 查看所有列表
	 */
	@RequestMapping("/queryAll")
	public R queryAll(@RequestParam Map<String, Object> params) {

		List<GoodsSpecificationEntity> list = goodsSpecificationService.queryList(params);

		return R.ok().put("list", list);
	}

	@RequestMapping("/querySpecificationByGoodId")
	public R querySpecificationByGoodId(Integer goodId) {

		//根据商品ID 获取商品规格
		List<GoodsSpecificationEntity> list = goodsSpecificationService.querySpecificationByGoodId(goodId);
		
		List<Map<String, Object>> specificationList = new ArrayList<>();
		
		List<Map<String,Object>> specificationValueList = new ArrayList<>();
		
		if (CollectionUtils.isNotEmpty(list)) {

			Set<Integer> specificationIds = new HashSet<>();
			for (GoodsSpecificationEntity specification : list) {
				
				Integer specificationId = specification.getSpecificationId();
				Map<String,Object> mapValue = new HashMap<>();
				
				if(specificationId==null){
					continue;
				}
				
				
				String name = specification.getName();
				if (specificationIds.add(specificationId)) {
					Map<String, Object> specificationMap = new HashMap<>();
					specificationMap.put("id", specificationId);
					specificationMap.put("name", name);
					specificationList.add(specificationMap);

					
					
					Boolean flag = false;
					List<Object> values = new ArrayList<>();
					Integer id = null;
					for(GoodsSpecificationEntity goodsSpecificationEntity :list){
						if(goodsSpecificationEntity.getSpecificationId().intValue()==specificationId.intValue()){
							flag = true;
							values.add(goodsSpecificationEntity);
							id =  goodsSpecificationEntity.getSpecificationId();
						}
					}
					if(id!=null&&flag){
						mapValue.put("id", id);
						mapValue.put("name", specification.getName());
						mapValue.put("model12","");
						mapValue.put("values", values);
					}
					
				}
				if(!mapValue.isEmpty()){
					specificationValueList.add(mapValue);
				}
			}
			
		}
		
		R  returnR  = R.ok();
		returnR.put("specificationList",specificationList);
		returnR.put("specificationValueList", specificationValueList);
		returnR.put("list", list);
		return returnR;
	}

}
