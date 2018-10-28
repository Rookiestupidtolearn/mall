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

import com.platform.entity.SysUserAuthEntity;
import com.platform.service.SysSmsLogService;
import com.platform.service.SysUserAuthService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;

/**
 * 用户信息认证Controller
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-10-26 11:03:04
 */
@RestController
@RequestMapping("sysuserauth")
public class SysUserAuthController {
    @Autowired
    private SysUserAuthService sysUserAuthService;
    
    @Autowired
    private SysSmsLogService smsLogService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sysuserauth:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<SysUserAuthEntity> sysUserAuthList = sysUserAuthService.queryList(query);
        int total = sysUserAuthService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(sysUserAuthList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sysuserauth:info")
    public R info(@PathVariable("id") Integer id) {
        SysUserAuthEntity sysUserAuth = sysUserAuthService.queryObject(id);

        return R.ok().put("sysUserAuth", sysUserAuth);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sysuserauth:save")
    public R save(@RequestBody SysUserAuthEntity sysUserAuth) {
        sysUserAuthService.save(sysUserAuth);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sysuserauth:update")
    public R update(@RequestBody SysUserAuthEntity sysUserAuth) {
        sysUserAuthService.update(sysUserAuth);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sysuserauth:delete")
    public R delete(@RequestBody Integer[] ids) {
        sysUserAuthService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<SysUserAuthEntity> list = sysUserAuthService.queryList(params);

        return R.ok().put("list", list);
    }
}
