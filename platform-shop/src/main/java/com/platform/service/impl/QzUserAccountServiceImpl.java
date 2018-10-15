package com.platform.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.platform.dao.QzUserAccountDao;
import com.platform.entity.QzUserAccountEntity;
import com.platform.service.QzUserAccountService;

/**
 * 用户账户Service实现类
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-10-13 11:28:14
 */
@Service("qzUserAccountService")
public class QzUserAccountServiceImpl implements QzUserAccountService {
    @Autowired
    private QzUserAccountDao qzUserAccountDao;

    @Override
    public QzUserAccountEntity queryObject(Long id) {
        return qzUserAccountDao.queryObject(id);
    }

    @Override
    public List<QzUserAccountEntity> queryList(Map<String, Object> map) {
        return qzUserAccountDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return qzUserAccountDao.queryTotal(map);
    }

    @Override
    public int save(QzUserAccountEntity qzUserAccount) {
        return qzUserAccountDao.save(qzUserAccount);
    }

    @Override
    public int update(QzUserAccountEntity qzUserAccount) {
        return qzUserAccountDao.update(qzUserAccount);
    }

    @Override
    public int delete(Long id) {
        return qzUserAccountDao.delete(id);
    }

    @Override
    public int deleteBatch(Long[] ids) {
        return qzUserAccountDao.deleteBatch(ids);
    }
    
    @Override
    public QzUserAccountEntity queryEntityByUserId(Integer userId) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("shopUserId", userId);
    	 List<QzUserAccountEntity> list = qzUserAccountDao.queryList(map);
    	 if (!CollectionUtils.isEmpty(list)) {
    		 return list.get(0);
		}
        return null;
    }
    
}
