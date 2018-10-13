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

import com.platform.entity.QzRechargeRecordEntity;
import com.platform.service.QzRechargeRecordService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;

/**
 * 用户充值记录Controller
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-10-11 17:49:01
 */
@RestController
@RequestMapping("qzrechargerecord")
public class QzRechargeRecordController {
    @Autowired
    private QzRechargeRecordService qzRechargeRecordService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("qzrechargerecord:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<QzRechargeRecordEntity> qzRechargeRecordList = qzRechargeRecordService.queryList(query);
        int total = qzRechargeRecordService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(qzRechargeRecordList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("qzrechargerecord:info")
    public R info(@PathVariable("id") Long id) {
        QzRechargeRecordEntity qzRechargeRecord = qzRechargeRecordService.queryObject(id);

        return R.ok().put("qzRechargeRecord", qzRechargeRecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("qzrechargerecord:save")
    public R save(@RequestBody QzRechargeRecordEntity qzRechargeRecord) {
        qzRechargeRecordService.save(qzRechargeRecord);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("qzrechargerecord:update")
    public R update(@RequestBody QzRechargeRecordEntity qzRechargeRecord) {
        qzRechargeRecordService.update(qzRechargeRecord);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("qzrechargerecord:delete")
    public R delete(@RequestBody Long[] ids) {
        qzRechargeRecordService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<QzRechargeRecordEntity> list = qzRechargeRecordService.queryList(params);

        return R.ok().put("list", list);
    }
    
    /**
     * 充值
     */
    @RequestMapping("/recharge")
    public R recharge(@RequestParam Map<String, Object> params) {
 
        if (params.get("mobiles").equals("") ) {
        	return R.error(400,"手机号不能为空");
		}
        if (params.get("amount").equals("") ) {
        	return R.error(400,"转账金额不能为空");
		}
        if (params.get("memo").equals("") ) {
        	return R.error(400,"转账说明不能为空");
		}
        qzRechargeRecordService.rechargeBatch(params);
        
        
        return R.ok();
    }
    
    /**
     * 充值审核通过
     */
    @RequestMapping("/rechargeAuditPass")
    public R rechargeAuditPass(@RequestBody Integer[] ids) {
        for (int i = 0; i < ids.length; i++) {
    	  qzRechargeRecordService.rechargeAudit(ids[i], "1");
	   }
    	
    	
        return R.ok();
    }
    
    /**
     * 充值审核失败
     */
    @RequestMapping("/rechargeAuditFall")
    public R rechargeAuditFall(@RequestBody Integer[] ids) {
        for (int i = 0; i < ids.length; i++) {
    	  qzRechargeRecordService.rechargeAudit(ids[i], "2");
	   }
        
        return R.ok();
    }
    
}
