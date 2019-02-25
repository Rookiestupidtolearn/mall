package com.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.platform.dao.ApiIntegralInfoDao;
import com.platform.entity.IntegralInfoEntity;
import com.platform.service.ApiIntegralInfoService;

/**
 * 会员积分记录表Service实现类
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2019-02-19 11:37:52
 */
@Service("integralInfoService")
public class ApiIntegralInfoServiceImpl implements ApiIntegralInfoService {
    @Autowired
    private ApiIntegralInfoDao integralInfoDao;

    @Override
    public IntegralInfoEntity queryObject(Long id) {
        return integralInfoDao.queryObject(id);
    }

    @Override
    public List<IntegralInfoEntity> queryList(Map<String, Object> map) {
        return integralInfoDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return integralInfoDao.queryTotal(map);
    }

    @Override
    public int save(IntegralInfoEntity integralInfo) {
        return integralInfoDao.save(integralInfo);
    }

    @Override
    public int update(IntegralInfoEntity integralInfo) {
        return integralInfoDao.update(integralInfo);
    }

    @Override
    public int delete(Long id) {
        return integralInfoDao.delete(id);
    }

    @Override
    public int deleteBatch(Long[] ids) {
        return integralInfoDao.deleteBatch(ids);
    }
}
