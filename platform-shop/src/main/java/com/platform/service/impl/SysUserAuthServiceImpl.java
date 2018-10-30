package com.platform.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.platform.dao.SysUserAuthDao;
import com.platform.entity.SysUserAuthEntity;
import com.platform.entity.SysUserEntity;
import com.platform.service.SysUserAuthService;
import com.platform.utils.ShiroUtils;

/**
 * 用户信息认证Service实现类
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-10-26 11:03:04
 */
@Service("sysUserAuthService")
public class SysUserAuthServiceImpl implements SysUserAuthService {
    @Autowired
    private SysUserAuthDao sysUserAuthDao;

    @Override
    public SysUserAuthEntity queryObject(Integer id) {
        return sysUserAuthDao.queryObject(id);
    }

    @Override
    public List<SysUserAuthEntity> queryList(Map<String, Object> map) {
        return sysUserAuthDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return sysUserAuthDao.queryTotal(map);
    }

    @Override
    public int save(SysUserAuthEntity sysUserAuth) {
    	
    	
    	   //用户名
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        String username = "";
        Long userId   = null;
        Long deptId = null;
        if (null != sysUserEntity) {
            username = sysUserEntity.getUsername();
            userId = sysUserEntity.getUserId();
            deptId = sysUserEntity.getDeptId();
            
        }
        sysUserAuth.setCreateTime(new Date());
        sysUserAuth.setCreateUserId(userId);
        if(deptId!=null){
        	sysUserAuth.setDeptId(Integer.parseInt(Long.toString(deptId)));
        }
        if(userId!=null){
        	sysUserAuth.setUserId(Integer.parseInt(Long.toString(userId)));
        }
        if(StringUtils.isNotEmpty(username)){
        	sysUserAuth.setUseraccount(username);
        }
        sysUserAuth.setDelflag("0");
        return sysUserAuthDao.save(sysUserAuth);
    }

    @Override
    public int update(SysUserAuthEntity sysUserAuth) {
        return sysUserAuthDao.update(sysUserAuth);
    }

    @Override
    public int delete(Integer id) {
        return sysUserAuthDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return sysUserAuthDao.deleteBatch(ids);
    }

	@Override
	public SysUserAuthEntity queryObjectByUserId(Integer userId) {
		// TODO Auto-generated method stub
		return sysUserAuthDao.queryObjectByUserId(userId);
	}
}
