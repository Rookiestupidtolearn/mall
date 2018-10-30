package com.platform.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.platform.entity.SysSmsLogEntity;
import com.platform.entity.SysUserAuthEntity;
import com.platform.entity.SysUserEntity;
import com.platform.service.SysSmsLogService;
import com.platform.service.SysUserAuthService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import com.platform.utils.ShiroUtils;

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
   /* @RequiresPermissions("sysuserauth:save")*/
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
    
    /**
     * 查看所有列表
     */
    @RequestMapping("/getChecCode")
    public R getChecCode(@RequestParam Map<String, Object> params) {
    	String mobile  = (String) params.get("mobile");
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        String username = "";
        Long userId = null;
        if (null != sysUserEntity) {
            username = sysUserEntity.getUsername();
            userId   = sysUserEntity.getUserId();
        }
    	Random  random = new Random();
    	int checkCode = random.nextInt(899999);
    	checkCode+=100000;
    	SysSmsLogEntity sys = new SysSmsLogEntity();
        sys.setUserId(userId);
        sys.setUserName(username);
        sys.setMobile(mobile);
    	sys.setContent("验证码:"+checkCode);
    	sys.setStime(new Date());
    	sys.setType("cc");
    	smsLogService.sendSms(sys);
        return R.ok().put("checkcode", checkCode);
   
    }
    
    
    /**
     * 获取登录人信息
     */
    @RequestMapping("/getLoginUserInfo")
    public R getLoginUserInfo() {
        SysUserAuthEntity sysUserAuth =  new SysUserAuthEntity();
        String username = "";
        Long userId = null;
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        Long deptId = null;
        if (null != sysUserEntity) {
            username = sysUserEntity.getUsername();
            userId   = sysUserEntity.getUserId();
            deptId  =   sysUserEntity.getDeptId();
        }
        if(userId!=null){
        	sysUserAuth.setUserId(Integer.parseInt(Long.toString(userId)));
        }
        sysUserAuth.setUseraccount(username);
        if(deptId!=null){
        	sysUserAuth.setDeptId(Integer.parseInt(Long.toString(deptId)));	
        }
        sysUserAuth.setDelflag("0");
        return R.ok().put("sysUserAuth", sysUserAuth);
    }

    
    /**
     * 查看信息
     */
    @RequestMapping("/queryObjectByUserId")
    public R queryObjectByUserId() {
    	  Long userId = null;
    	  SysUserAuthEntity sysUserAuth =null;
    	  SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
          if (null != sysUserEntity) {
              userId   = sysUserEntity.getUserId();
          }
    	
	        if(userId!=null){
	             sysUserAuth = sysUserAuthService.queryObjectByUserId(Integer.parseInt(Long.toString(userId)));
	        }
	     R returR  =   R.ok();
	     returR.put("sysUserAuth",sysUserAuth);
	     
	     if(sysUserAuth!=null){
	    	 returR.put("userId",sysUserAuth.getUserId());	 
	     }else{
	    	 returR.put("userId",null);	 
	     }
	     return returR;
    }
    
}
