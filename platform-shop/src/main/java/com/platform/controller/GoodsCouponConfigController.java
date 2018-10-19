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
import com.platform.service.GoodsCouponConfigService;
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
        goodsCouponConfigService.save(goodsCouponConfig);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("goodscouponconfig:update")
    public R update(@RequestBody GoodsCouponConfigEntity goodsCouponConfig) {
        goodsCouponConfigService.update(goodsCouponConfig);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("goodscouponconfig:delete")
    public R delete(@RequestBody Integer[] ids) {
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
}
