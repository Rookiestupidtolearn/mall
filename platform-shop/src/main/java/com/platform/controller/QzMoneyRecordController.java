package com.platform.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.platform.entity.QzMoneyRecordEntity;
import com.platform.entity.UserEntity;
import com.platform.service.QzMoneyRecordService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import com.platform.utils.excel.ExcelExport;

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

    /**
     * 导出会员
     */
    @RequestMapping("/export")
    @RequiresPermissions("qzmoneyrecord:export")
    public R export(@RequestParam Map<String, Object> params, HttpServletResponse response) {

    	  //查询列表数据
    	System.out.println("111");
        Query query = new Query(params);

        List<QzMoneyRecordEntity> qzMoneyRecordList = qzMoneyRecordService.queryList(query);

        ExcelExport ee = new ExcelExport("平台流水列表");

        String[] header = new String[]{"会员名称", "会员id","手机号","身份证件号","资金变动类型","金额变动标志","变动金额","冻结金额","当前余额","创建时间","交易流水号"};

        List<Map<String, Object>> list = new ArrayList<>();

        if (qzMoneyRecordList != null && qzMoneyRecordList.size() != 0) {
            for (QzMoneyRecordEntity record : qzMoneyRecordList) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("USERNAME", record.getShopUserName());
                map.put("USERID", record.getShopUserId());
                map.put("PHONE", record.getUserPhone());
                map.put("IDCARD", record.getCardId());
                map.put("TRANtYPE", "1".equals(record.getTranType()) ? "平台充值" : "消费");
                map.put("TRANFLAG", record.getTranFlag() == 0 ? "支出" :"收入");
                map.put("TRANAMOUNT", record.getTarnAmount());
                map.put("LOCKAMOUNT", record.getLockAmount());
                map.put("CURRENTAMOUNT", record.getCurrentAmount());
                map.put("CREATEDATE", record.getCreateTime());
                map.put("TRANNO", record.getTradeNo());
                list.add(map);
            }
        }
        ee.addSheetByMap("流水", list, header);
        ee.export(response);
        return R.ok();
    }
}
