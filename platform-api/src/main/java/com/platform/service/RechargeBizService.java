package com.platform.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.api.response.RechargeResponseEntity;
import com.platform.api.yeepay.EncryptUtil;
import com.platform.dao.QzRechargeRecordDao;
import com.platform.entity.QzRechargeRecordEntity;

/**
 * 用户充值记录Service实现类
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-12-17 13:35:56
 */
@Service("rechargeBizService")
public class RechargeBizService  {
	private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private QzRechargeRecordDao qzRechargeRecordDao;

    
     public  RechargeResponseEntity   recharge(String encrypt) throws Exception{
    	 RechargeResponseEntity responseEntity = new RechargeResponseEntity();
    	 responseEntity.setCode("error");
    	 responseEntity.setMsg("充值失败");
    	 //解密
    	 String  data = EncryptUtil.aesDecrypt(encrypt);
    	 if (StringUtils.isNotEmpty(data)) {
    		 logger.info("充值解密后的传值："+data.toString());
    		 
    		 
    		 
		}
    	 
    	 
    	 
		return responseEntity;
    	
    	
    }
    
    
    public QzRechargeRecordEntity queryObject(Integer id) {
        return qzRechargeRecordDao.queryObject(id);
    }

    
    public List<QzRechargeRecordEntity> queryList(Map<String, Object> map) {
        return qzRechargeRecordDao.queryList(map);
    }

    
    public int queryTotal(Map<String, Object> map) {
        return qzRechargeRecordDao.queryTotal(map);
    }

    
    public int save(QzRechargeRecordEntity qzRechargeRecord) {
        return qzRechargeRecordDao.save(qzRechargeRecord);
    }

    
    public int update(QzRechargeRecordEntity qzRechargeRecord) {
        return qzRechargeRecordDao.update(qzRechargeRecord);
    }

    
    public int delete(Integer id) {
        return qzRechargeRecordDao.delete(id);
    }

    
    public int deleteBatch(Integer[] ids) {
        return qzRechargeRecordDao.deleteBatch(ids);
    }
}
