package com.platform.service.impl;

import com.platform.constant.MemberConstants.*;
import com.platform.constant.MemberGradeInit;
import com.platform.dao.ApiMemberDao;
import com.platform.entity.MemberEntity;
import com.platform.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.platform.dao.ApiVplusRecordInfoDao;
import com.platform.entity.VplusRecordInfoEntity;
import com.platform.service.ApiVplusRecordInfoService;

/**
 * Vplus开通续费记录表Service实现类
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2019-02-21 13:58:03
 */
@Service("vplusRecordInfoService")
public class ApiVplusRecordInfoServiceImpl implements ApiVplusRecordInfoService {
    @Autowired
    private ApiVplusRecordInfoDao vplusRecordInfoDao;

    @Autowired
    private ApiMemberDao memberDao;

    @Override
    public VplusRecordInfoEntity queryObject(Long id) {
        return vplusRecordInfoDao.queryObject(id);
    }

    @Override
    public List<VplusRecordInfoEntity> queryList(Map<String, Object> map) {
        return vplusRecordInfoDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return vplusRecordInfoDao.queryTotal(map);
    }

    @Override
    public int save(VplusRecordInfoEntity vplusRecordInfo) {
        return vplusRecordInfoDao.save(vplusRecordInfo);
    }

    @Override
    public int update(VplusRecordInfoEntity vplusRecordInfo) {
        return vplusRecordInfoDao.update(vplusRecordInfo);
    }

    @Override
    public int delete(Long id) {
        return vplusRecordInfoDao.delete(id);
    }

    @Override
    public int deleteBatch(Long[] ids) {
        return vplusRecordInfoDao.deleteBatch(ids);
    }

    @Override
    public VplusRecordInfoEntity queryVplusRecordInfoByYeepayTradeNo(String yeePayTradeNo) {
        return vplusRecordInfoDao.queryVplusRecordInfoByYeepayTradeNo(yeePayTradeNo);
    }

    /**
     *
     * @param userId
     * @param mobile
     * @param money
     * @param type  平台类型 第三方的话 按vplus权益充值
     * @return
     */
    public boolean saveVplus(Integer userId,String mobile,BigDecimal money,Integer type){
        MemberEntity memberEntity= memberDao.queryObjectByUserIdOrMobile(userId,mobile);//查询会员

        VplusRecordInfoEntity vplusRecordInfoEntity=new VplusRecordInfoEntity();

        //处理充值记录

        BigDecimal cq;//充值期限
        BigDecimal zq;//赠送期限
        BigDecimal hq;//会员期限
        Date currentDate=new Date();
        Date endDate;

        if(memberEntity.getIsVplus()== FlagType.YES || type == RechargePlatFormType.THIRD){
            vplusRecordInfoEntity.setType(OpenOrRenewType.RENEW);//享有vplus权益续费
            cq= money.divide(new BigDecimal(5));
            zq=(cq.multiply(MemberGradeInit.getGradeMap().get(memberEntity.getMemberGrade()).getGiveDeadlineRatio())).setScale(0,BigDecimal.ROUND_UP);//向上取整 4.1 取 5
            hq=cq.add(zq);
            endDate= DateUtil.plusDay(currentDate,hq.intValue());
            vplusRecordInfoEntity.setEndDate(endDate);//设置到期时间
        }else if(memberEntity.getIsVplus()== FlagType.NO && memberEntity.getVplusBeginDate()==null){//第一次开通vplus
            vplusRecordInfoEntity.setOpenDate(currentDate);
            hq=money.divide(new BigDecimal(5));
            endDate=DateUtil.plusDay(currentDate,hq.intValue());
            vplusRecordInfoEntity.setType(OpenOrRenewType.OPEN);
            vplusRecordInfoEntity.setEndDate(endDate);//设置到期时间
        }else{//
            vplusRecordInfoEntity.setType(OpenOrRenewType.RENEW);//不享有vplus权益续费
            hq=money.divide(new BigDecimal(5));
            endDate=DateUtil.plusDay(currentDate,hq.intValue());
            vplusRecordInfoEntity.setEndDate(endDate);//设置到期时间
        }
         return vplusRecordInfoDao.save(vplusRecordInfoEntity)==0?false:true;
    }


}
