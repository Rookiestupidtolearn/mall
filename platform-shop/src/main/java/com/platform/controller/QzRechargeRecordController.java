package com.platform.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.platform.entity.QzRechargeRecordEntity;
import com.platform.entity.SysUserEntity;
import com.platform.entity.UserEntity;
import com.platform.service.QzRechargeRecordService;
import com.platform.service.SysUserService;
import com.platform.service.UserService;
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
	private static final Logger log = LoggerFactory.getLogger(QzRechargeRecordController.class);
    @Autowired
    private QzRechargeRecordService qzRechargeRecordService;
  
    @Autowired
    private UserService userService;
    
    @Autowired
    private SysUserService sysUserService;
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
        
		//封装返回数据
         List<QzRechargeRecordEntity> result= qzRechargeRecordList.stream().map(x -> {
        	QzRechargeRecordEntity reponse = new QzRechargeRecordEntity();
        	 BeanUtils.copyProperties(x, reponse);
        	 UserEntity uEntity = userService.queryObject(x.getShopUserId());
        	 reponse.setShopUserName(uEntity.getUsername());
        	 reponse.setUserPhone(uEntity.getMobile());
        	 return reponse;
        }).collect(Collectors.toList());
        
        PageUtils pageUtil = new PageUtils(result, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    
    /**
     * 查看审核列表
     */
    @RequestMapping("/auditList")
    @RequiresPermissions("qzrechargerecord:list")
    public R auditList(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<QzRechargeRecordEntity> qzRechargeRecordList = qzRechargeRecordService.queryAuditList(query);
        int total = qzRechargeRecordService.queryAuditTotal(query);

    	//封装返回数据
        List<QzRechargeRecordEntity> result= qzRechargeRecordList.stream().map(x -> {
       	QzRechargeRecordEntity reponse = new QzRechargeRecordEntity();
       	 BeanUtils.copyProperties(x, reponse);
       	 UserEntity uEntity = userService.queryObject(x.getShopUserId());
       	 reponse.setShopUserName(uEntity.getUsername());
       	 reponse.setUserPhone(uEntity.getMobile());
       	 
       	SysUserEntity sysUserEntity = sysUserService.queryObject(x.getAuditId());
       	reponse.setOperate(sysUserEntity.getUsername());
       	 return reponse;
       }).collect(Collectors.toList());
       
       PageUtils pageUtil = new PageUtils(result, total, query.getLimit(), query.getPage());
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
        //校验手机号是否合法
        String mobiles = (String) params.get("mobiles");
        String mobile[] = mobiles.split(",");
        for (int i = 0; i < mobile.length; i++) {
            String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
            if (mobile[i].length() != 11) {
            	return R.error(400,"手机号长度错误，应该是11位!");
            } 
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(mobile[i]);
                boolean isMatch = m.matches();
         
                if (!isMatch) {
                 	return R.error(400,"请填入正确的手机号!");
                }
            
                UserEntity entity = userService.queryEntityByMobile(mobile[i]);
    			if (entity == null) {
    				return R.error(400,"手机号【"+mobile[i]+"】不是会员!");
    			}
    			
    			Map<String, Object>  map = new HashMap<>();
    			map.put("mobile", mobile[i]);
    			List<UserEntity> uEntities = userService.queryList(map);
    			if (uEntities.size() >1) {
    				return R.error(400,"手机号【"+mobile[i]+"】不能绑定两个会员!");
				}
    			
            }
        
        if (params.get("amount").equals("") ) {
        	return R.error(400,"转账金额不能为空");
		}
        String amount = (String) params.get("amount");
        
        Pattern pattern=Pattern.compile("\\d\\.\\d*|[1-9]\\d*|\\d*\\.\\d*|\\d"); 
        Matcher match=pattern.matcher(amount); 
        if(match.matches()==false){ 
        	return R.error(400,"转账金额不合法，请检查");
        }
        
        Double checkAmount = Double.valueOf(amount);
        if (checkAmount <=0) {
        	return R.error(400,"转账金额应大于0元");
		}
        
        Double bigAmount = Double.valueOf(amount);
        if (bigAmount > 90000000) {
        	return R.error(400,"转账金额不能大于9千万元");
		}
        
        Pattern pattern2=Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
        Matcher match2=pattern2.matcher(amount); 
        if(match2.matches()==false){ 
        	return R.error(400,"转账金额小数点后保留2位");
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
    	log.info("充值审核通过，申请的id是："+ids);
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
    	log.info("充值审核失败，申请的id是："+ids);
        for (int i = 0; i < ids.length; i++) {
    	  qzRechargeRecordService.rechargeAudit(ids[i], "2");
	   }
        
        return R.ok();
    }

}
