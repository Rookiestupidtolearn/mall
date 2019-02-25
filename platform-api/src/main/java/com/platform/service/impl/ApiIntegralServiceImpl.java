package com.platform.service.impl;

import com.platform.constant.MemberConstants;
import com.platform.dao.ApiIntegralInfoDao;
import com.platform.entity.IntegralInfoEntity;
import com.platform.exception.MemberNullException;
import com.platform.exception.MonetaryNullException;
import com.platform.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.platform.dao.ApiIntegralDao;
import com.platform.entity.IntegralEntity;
import com.platform.service.ApiIntegralService;

/**
 * 用户积分表Service实现类
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2019-02-19 11:37:52
 */
@Service("integralService")
public class ApiIntegralServiceImpl implements ApiIntegralService {
    private Logger longger = LoggerFactory.getLogger(MemberGradeServiceImpl.class);

    @Autowired
    private ApiIntegralDao integralDao;

    @Autowired
    private ApiIntegralInfoDao integralInfoDao;

    @Override
    public IntegralEntity queryObject(Long id) {
        return integralDao.queryObject(id);
    }

    @Override
    public List<IntegralEntity> queryList(Map<String, Object> map) {
        return integralDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return integralDao.queryTotal(map);
    }

    @Override
    public int save(IntegralEntity integral) {
        return integralDao.save(integral);
    }

    @Override
    public int update(IntegralEntity integral) {
        return integralDao.update(integral);
    }

    @Override
    public int delete(Long id) {
        return integralDao.delete(id);
    }

    @Override
    public int deleteBatch(Long[] ids) {
        return integralDao.deleteBatch(ids);
    }


    @Override
    public boolean increaseIntegralByUserId(Integer userId, BigDecimal money, BigDecimal percentage ) {
        BigDecimal defaultPercentage=new BigDecimal(10);

        if (StringUtils.isNullOrEmpty(userId)) {
            longger.error("userId为空");
            throw new MemberNullException("userId为空");
        }
        if (StringUtils.isNullOrEmpty(money)) {
            longger.error("消费金额为空,userId为{}", userId);
            throw new MonetaryNullException("消费金额为空");
        }
        if(StringUtils.isNullOrEmpty(percentage))
            defaultPercentage=percentage;

        BigDecimal pendingIntegral=money.multiply(defaultPercentage);
         IntegralEntity integralEntity=integralDao.queryObjectByUserId(userId);
         Date currentDate=new Date();
        if(StringUtils.isNullOrEmpty(integralEntity)){//用户第一次增加积分需新建
            integralEntity=new IntegralEntity();
            integralEntity.setCreateDate(currentDate);
            integralEntity.setUserId(userId);
            integralEntity.setIntegral(0);
            integralDao.save(integralEntity);
        }
        Integer newIntegral=pendingIntegral.intValue()+integralEntity.getIntegral();
        //增加流失数据 在用户积分记录表
        IntegralInfoEntity integralInfoEntity=new IntegralInfoEntity();
        integralInfoEntity.setUserId(userId);
        integralInfoEntity.setCreateDate(currentDate);
        integralInfoEntity.setType("1");//购物支付
        integralInfoEntity.setAtPresentIntegral(pendingIntegral.intValue());
        integralInfoEntity.setIntegral(newIntegral);//
        integralInfoEntity.setAccountingType(MemberConstants.PayMentType.INCOME);
        integralInfoDao.save(integralInfoEntity);
        //用户积分总额增加
        integralEntity.setIntegral(newIntegral);

        return integralDao.update(integralEntity)==0?false:true;
    }
}
