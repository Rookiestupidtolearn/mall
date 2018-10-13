package com.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.platform.dao.QzMoneyRecordDao;
import com.platform.entity.QzMoneyRecordEntity;
import com.platform.service.QzMoneyRecordService;

/**
 * 用户资金流水Service实现类
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-10-13 11:28:14
 */
@Service("qzMoneyRecordService")
public class QzMoneyRecordServiceImpl implements QzMoneyRecordService {
    @Autowired
    private QzMoneyRecordDao qzMoneyRecordDao;

    @Override
    public QzMoneyRecordEntity queryObject(Long id) {
        return qzMoneyRecordDao.queryObject(id);
    }

    @Override
    public List<QzMoneyRecordEntity> queryList(Map<String, Object> map) {
        return qzMoneyRecordDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return qzMoneyRecordDao.queryTotal(map);
    }

    @Override
    public int save(QzMoneyRecordEntity qzMoneyRecord) {
        return qzMoneyRecordDao.save(qzMoneyRecord);
    }

    @Override
    public int update(QzMoneyRecordEntity qzMoneyRecord) {
        return qzMoneyRecordDao.update(qzMoneyRecord);
    }

    @Override
    public int delete(Long id) {
        return qzMoneyRecordDao.delete(id);
    }

    @Override
    public int deleteBatch(Long[] ids) {
        return qzMoneyRecordDao.deleteBatch(ids);
    }
}
