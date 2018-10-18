package com.platform.service.impl;

import com.platform.dao.UserDao;
import com.platform.entity.QzRechargeRecordEntity;
import com.platform.entity.UserEntity;
import com.platform.service.UserService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-16 15:02:28
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public UserEntity queryObject(Integer id) {
        return userDao.queryObject(id);
    }

    @Override
    public List<UserEntity> queryList(Map<String, Object> map) {
        return userDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return userDao.queryTotal(map);
    }

    @Override
    public int save(UserEntity user) {
        user.setRegisterTime(new Date());
        return userDao.save(user);
    }

    @Override
    public int update(UserEntity user) {
        return userDao.update(user);
    }

    @Override
    public int delete(Integer id) {
        return userDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return userDao.deleteBatch(ids);
    }
    
    @Override
    public UserEntity queryEntityByMobile(String mobile){
    	UserEntity entity = new UserEntity();
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("mobile", mobile);
    	List<UserEntity> entities =	queryList(map);
    	if (!CollectionUtils.isEmpty(entities)) {
    		entity = entities.get(0);
    		return  entity;
		}
		return null;
    }
}
