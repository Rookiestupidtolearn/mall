package com.platform.service;

import com.platform.dao.QzMoneyRecordMapper;
import com.platform.dao.QzUserAccountMapper;
import com.platform.entity.QzMoneyRecordEntity;
import com.platform.entity.QzUserAccountEntity;
import com.platform.entity.QzUserAccountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

@Service("qzUserAccountApiService")
public class QzUserAccountApiService {

    @Autowired
    private QzUserAccountMapper qzUserAccountMapper;

    @Autowired
    private QzMoneyRecordMapper qzMoneyRecordMapper;



    public QzUserAccountVo queruUserAccountInfo(Integer id){
        return qzUserAccountMapper.queruUserAccountInfo(Long.valueOf(id));
    }

    public boolean updateUserAccount(QzUserAccountVo userAmountVo){
        int i = qzUserAccountMapper.updateUserAccount(userAmountVo);
        return i == 0 ? false : true ;
    }

    public boolean saveQzUserAccountVo(QzUserAccountVo qzUserAccountVo){
        int save = qzUserAccountMapper.save(qzUserAccountVo);
        return save == 0 ? false : true ;
    }

    /**
     * 更新用户资金流水和用户余额
     * @param userId
     * @param money
     * @param type 资金变动类型'资金变动类型 1-充值 2-克拉币 3-回滚/扣减冻结克拉币'
     * @return
     */
    public boolean updateUserAccountAndMoneyRecordByUserId(Integer userId, BigDecimal money, String type,String tradeNo){
        QzUserAccountVo qzUserAccountVo=qzUserAccountMapper.queruUserAccountInfo(userId.longValue());
        //添加用户资金流水
        QzMoneyRecordEntity qzMoneyRecordEntity=new QzMoneyRecordEntity();
        qzMoneyRecordEntity.setShopUserId(userId);
        qzMoneyRecordEntity.setCreateTime(new Date());
        qzMoneyRecordEntity.setTranType(type);//资金变动类型
        qzMoneyRecordEntity.setTranFlag(1);

        int ratio=1+new Random().nextInt(11)/100;//生成充值比例系数
        qzMoneyRecordEntity.setTarnAmount(money.multiply(new BigDecimal(ratio)));
        qzMoneyRecordEntity.setCurrentAmount(qzMoneyRecordEntity.getCurrentAmount());
        qzMoneyRecordEntity.setLockAmount(qzMoneyRecordEntity.getLockAmount());
        qzMoneyRecordEntity.setTradeNo(tradeNo);
        int i=qzMoneyRecordMapper.save(qzMoneyRecordEntity);
        if(i==0){
            return false;
        }

        //修改用户余额
        qzUserAccountVo.setAmount(qzUserAccountVo.getAmount().add(money));



        return  qzUserAccountMapper.updateUserAccount(qzUserAccountVo)== 0 ? false : true ;
    }

}
