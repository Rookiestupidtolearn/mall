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

import com.platform.entity.QzUserAccountEntity;
import com.platform.service.QzUserAccountService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;

/**
 * 用户账户Controller
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-10-13 11:28:14
 */
@RestController
@RequestMapping("qzuseraccount")
public class QzUserAccountController {
    @Autowired
    private QzUserAccountService qzUserAccountService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("qzuseraccount:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<QzUserAccountEntity> qzUserAccountList = qzUserAccountService.queryList(query);
        int total = qzUserAccountService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(qzUserAccountList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("qzuseraccount:info")
    public R info(@PathVariable("id") Long id) {
        QzUserAccountEntity qzUserAccount = qzUserAccountService.queryObject(id);

        return R.ok().put("qzUserAccount", qzUserAccount);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("qzuseraccount:save")
    public R save(@RequestBody QzUserAccountEntity qzUserAccount) {
        qzUserAccountService.save(qzUserAccount);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("qzuseraccount:update")
    public R update(@RequestBody QzUserAccountEntity qzUserAccount) {
        qzUserAccountService.update(qzUserAccount);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("qzuseraccount:delete")
    public R delete(@RequestBody Long[] ids) {
        qzUserAccountService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<QzUserAccountEntity> list = qzUserAccountService.queryList(params);

        return R.ok().put("list", list);
    }
}
