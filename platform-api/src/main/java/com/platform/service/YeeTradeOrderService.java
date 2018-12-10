package com.platform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.platform.dao.YeeTradeOrderDao;
import com.platform.entity.YeeTradeOrderEntity;
import com.platform.service.YeeTradeOrderService;

/**
 * 易宝支付订单表Service实现类
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-12-09 13:31:06
 */
@Service
public class YeeTradeOrderService {
    @Autowired
    private YeeTradeOrderDao yeeTradeOrderDao;

  
    public YeeTradeOrderEntity queryObject(Integer id) {
        return yeeTradeOrderDao.queryObject(id);
    }

    
    public List<YeeTradeOrderEntity> queryList(Map<String, Object> map) {
        return yeeTradeOrderDao.queryList(map);
    }

    
    public int queryTotal(Map<String, Object> map) {
        return yeeTradeOrderDao.queryTotal(map);
    }

    
    public int save(YeeTradeOrderEntity yeeTradeOrder) {
        return yeeTradeOrderDao.save(yeeTradeOrder);
    }

    
    public int update(YeeTradeOrderEntity yeeTradeOrder) {
        return yeeTradeOrderDao.update(yeeTradeOrder);
    }

    
    public int delete(Integer id) {
        return yeeTradeOrderDao.delete(id);
    }

    
    public int deleteBatch(Integer[] ids) {
        return yeeTradeOrderDao.deleteBatch(ids);
    }
}
