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

import com.platform.entity.QzMoneyRecordEntity;
import com.platform.service.QzMoneyRecordService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;

/**
 * 用户资金流水Controller
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-10-13 11:28:14
 */
@RestController
@RequestMapping("qzmoneyrecord")
public class QzMoneyRecordController {
    @Autowired
    private QzMoneyRecordService qzMoneyRecordService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("qzmoneyrecord:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<QzMoneyRecordEntity> qzMoneyRecordList = qzMoneyRecordService.queryList(query);
        int total = qzMoneyRecordService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(qzMoneyRecordList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("qzmoneyrecord:info")
    public R info(@PathVariable("id") Long id) {
        QzMoneyRecordEntity qzMoneyRecord = qzMoneyRecordService.queryObject(id);

        return R.ok().put("qzMoneyRecord", qzMoneyRecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("qzmoneyrecord:save")
    public R save(@RequestBody QzMoneyRecordEntity qzMoneyRecord) {
        qzMoneyRecordService.save(qzMoneyRecord);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("qzmoneyrecord:update")
    public R update(@RequestBody QzMoneyRecordEntity qzMoneyRecord) {
        qzMoneyRecordService.update(qzMoneyRecord);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("qzmoneyrecord:delete")
    public R delete(@RequestBody Long[] ids) {
        qzMoneyRecordService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<QzMoneyRecordEntity> list = qzMoneyRecordService.queryList(params);

        return R.ok().put("list", list);
    }
}
