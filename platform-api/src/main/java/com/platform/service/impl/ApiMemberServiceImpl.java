package com.platform.service.impl;

import com.platform.constant.GradeEnum;
import com.platform.constant.MemberConstants;
import com.platform.constant.MemberConstants.*;
import com.platform.constant.MemberGradeInit;
import com.platform.dao.ApiMemberGrowthInfoDao;
import com.platform.entity.*;
import com.platform.exception.*;
import com.platform.service.*;
import com.platform.util.DateUtil;
import com.platform.util.JudgeMembershipLevelUtil;
import com.platform.utils.GenerateCodeUtil;
import com.platform.utils.StringUtils;
import com.platform.yeepay.service.YeepayOrderBizService;
import com.sun.tools.javac.code.Flags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import com.platform.dao.ApiMemberDao;
import org.springframework.transaction.annotation.Transactional;

/**
 * 会员表Service实现类
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2019-02-19 11:37:51
 */
@Service("memberService")
public class ApiMemberServiceImpl implements ApiMemberService {


    private Logger longger = LoggerFactory.getLogger(MemberGradeServiceImpl.class);

    @Autowired
    private ApiMemberDao memberDao;

    @Autowired
    private YeepayOrderBizService yeepayOrderBizService;

    @Autowired
    private ApiVplusRecordInfoService vplusRecordInfoService;

    @Autowired
    private ApiMemberGrowthInfoDao memberGrowthInfoDao;

    @Autowired
    private QzRechargeRecordApiService qzRechargeRecordApiService;

    @Autowired
    private QzUserAccountApiService qzUserAccountApiService;

    @Autowired
    private ApiUserService apiUserService;

    @Override
    public MemberEntity queryObject(Long id) {
        return memberDao.queryObject(id);
    }

    @Override
    public List<MemberEntity> queryList(Map<String, Object> map) {
        return memberDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return memberDao.queryTotal(map);
    }

    @Override
    public int save(MemberEntity member) {
        return memberDao.save(member);
    }

    @Override
    public int update(MemberEntity member) {
        return memberDao.update(member);
    }

    @Override
    public int delete(Long id) {
        return memberDao.delete(id);
    }

    @Override
    public int deleteBatch(Long[] ids) {
        return memberDao.deleteBatch(ids);
    }

    @Override
    public void updateBatch(List<MemberEntity> list) {
        memberDao.updateBatch(list);
    }

    @Override
    public MemberEntity queryObjectByUserIdOrMobile(Integer userId, String mobile) {
        return memberDao.queryObjectByUserIdOrMobile(userId, mobile);
    }


    /**
     * 根据消费充值额更新消费后会员等级
     *
     * @param monetary     充值金额 单位：元
     * @param percentage   成长值与金额百分比
     * @param memberEntity 会员信息
     * @param growthType   1:支付增加成长值 2:充值增加成长值
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public MemberEntity memberShipUpgrade(BigDecimal monetary, BigDecimal percentage, MemberEntity memberEntity, String growthType) {
        BigDecimal defalutPercentage = new BigDecimal(1);//默认比例为1：1
        if (StringUtils.isNullOrEmpty(memberEntity)) {
            longger.error("会员信息为空");
            throw new MemberNullException("会员信息为空");
        }

        if (StringUtils.isNullOrEmpty(monetary)) {
            longger.error("消费金额为空,会员信息为{}", memberEntity.toString());
            throw new MonetaryNullException("消费金额为空");
        }

        if (!StringUtils.isNotEmpty(growthType)) {
            longger.error("增长值类型为空,会员信息为{}", memberEntity.toString());
            throw new GrowthTypeNullException("增长值类型为空");
        }

        if (!StringUtils.isNullOrEmpty(percentage))
            defalutPercentage = percentage;

        Long currentmemberGrowthValue;//消费后的成长值
        Long membersGrowthValue;//消费成长值
        String level;
        Date now = new Date();
        if (memberEntity.getIsVplus() == 1 && now.before(memberEntity.getVplusEndDate())) {//当用户类型为vplus会员
            //消费成长值*vplus固定权益
            membersGrowthValue = MemberGradeInit.getGradeMap().get(memberEntity.getMemberGrade()).getGrowthValueRatio().longValue() * monetary.multiply(defalutPercentage).setScale(0, BigDecimal.ROUND_HALF_DOWN).longValue();
            currentmemberGrowthValue = memberEntity.getMembersGrowthValue() + membersGrowthValue;
            level = JudgeMembershipLevelUtil.judgeLevel((long) Math.round(currentmemberGrowthValue), GradeEnum.VPLUS);

        } else if (memberEntity.getIsVplus() == 1 && now.after(memberEntity.getVplusEndDate())) {//当用户vplus状态没有及时更新但权益时间到期按vip成长
            memberEntity.setIsVplus(0);//vplus状态设置为0
            membersGrowthValue = monetary.multiply(defalutPercentage).setScale(0, BigDecimal.ROUND_HALF_DOWN).longValue();
            currentmemberGrowthValue = memberEntity.getMembersGrowthValue() + membersGrowthValue;
            level = JudgeMembershipLevelUtil.judgeLevel((long) Math.round(currentmemberGrowthValue), GradeEnum.VIP);
        } else {//vip会员
            membersGrowthValue = monetary.multiply(defalutPercentage).setScale(0, BigDecimal.ROUND_HALF_DOWN).longValue();
            currentmemberGrowthValue = memberEntity.getMembersGrowthValue() + membersGrowthValue;
            level = JudgeMembershipLevelUtil.judgeLevel((long) Math.round(currentmemberGrowthValue), GradeEnum.VIP);
        }

        if (!StringUtils.isNotEmpty(level)) {
            longger.error("会员等级级别为空,会员信息为{}", memberEntity.toString());
            throw new GradeNullException("会员等级级别为空");
        }
        memberEntity.setMemberGrade(level);
        memberEntity.setMembersGrowthValue(currentmemberGrowthValue);
        memberDao.update(memberEntity);
        longger.info("更新会员级别信息success");
        //保存会员成长记录信息
        MemberGrowthInfoEntity memberGrowthInfoEntity = new MemberGrowthInfoEntity();
        memberGrowthInfoEntity.setUserId(memberEntity.getUserId());
        memberGrowthInfoEntity.setType(growthType);
        memberGrowthInfoEntity.setCreateDate(new Date());
        memberGrowthInfoEntity.setAtPresentGrowthValue(currentmemberGrowthValue.toString());
        memberGrowthInfoEntity.setGrowthValue(currentmemberGrowthValue.toString());
        memberGrowthInfoEntity.setIsDelete(FlagType.NO);

        memberGrowthInfoDao.save(memberGrowthInfoEntity);
        longger.info("保存会员成长记录信息success");
        return memberEntity;
    }

    /**
     * 消费支付后会员成长值和会员级别处理
     * @param monetary
     * @param userId
     * @return
     */
    public MemberEntity memberShipUpgrade(BigDecimal monetary, Integer userId) {
        if (StringUtils.isNullOrEmpty(userId)) {
            longger.error("userId为空");
            throw new MemberNullException("userId为空");
        }

        if (StringUtils.isNullOrEmpty(monetary)) {
            longger.error("消费金额为空,userId为{}", userId);
            throw new MonetaryNullException("消费金额为空");
        }

        MemberEntity memberEntity=memberDao.queryObjectByUserIdOrMobile(userId,null);
        if(memberEntity.getMembersGrowthValue()==0){  //是否为第一次消费 第一次消费需修改vip状态成为vip
            memberEntity.setIsVip(FlagType.YES);
        }
        memberEntity= memberShipUpgrade(monetary,null,memberEntity,GrowthType.PAY);
        return memberEntity;

    }

    /**
     * 开通vplus或者续费
     *
     * @param money        金额元
     * @param userId
     * @param validIP
     * @param platFormType
     * @return
     */
    @Override
    public Map<String, Object> openOrRenewVplus(BigDecimal money, Integer userId, String validIP, Integer platFormType) {
        Map<String, Object> resultObj = new HashMap<String, Object>();


        if (StringUtils.isNullOrEmpty(userId)) {
            longger.error("会员id为空");
            throw new MemberNullException("会员id为空");
        }

        if (StringUtils.isNullOrEmpty(money)) {
            longger.error("消费金额为空,会员id信息为{}", userId);
            throw new MonetaryNullException("消费金额为空");
        }
        if (money.intValue() % 5 != 0) {
            longger.error("消费金额不合法，不是5的倍数,会员信息id为{}", userId);
            throw new MoneyIllegalException("消费金额不合法，不是5的倍数");
        }

        MemberEntity memberEntity = memberDao.queryObjectByUserIdOrMobile(userId, null);

        if (StringUtils.isNullOrEmpty(memberEntity)) {
            UserVo userVo = apiUserService.queryObject(userId.longValue());
            if (StringUtils.isNullOrEmpty(userVo)) {
                longger.error("无此用户");
                throw new MemberNullException("无此用户");
            }
            //第一次充值加入到会员表中
            memberEntity = new MemberEntity();
            memberEntity.setUserId(userVo.getUserId().intValue());
            memberEntity.setMobile(userVo.getMobile());
            memberEntity.setIsVplus(FlagType.NO);
            memberEntity.setIsVip(FlagType.YES);
            memberDao.save(memberEntity);
        }
        //创建充值订单
        String tradeNo = GenerateCodeUtil.buildBizNo("VPLUS");
        QzRechargeRecordEntity qzRechargeRecordEntity = new QzRechargeRecordEntity();
        qzRechargeRecordEntity.setShopUserId(userId);
        qzRechargeRecordEntity.setMobile(memberEntity.getMobile());
        qzRechargeRecordEntity.setTradeNo(tradeNo);//平台交易流水号
        qzRechargeRecordEntity.setAmount(money);
        qzRechargeRecordEntity.setRechargeType(RechargePlatFormType.FRONT);//充值类型
        qzRechargeRecordEntity.setState(AuditState.PASS); //前端充值默认审核通过
        qzRechargeRecordEntity.setCreateTime(new Date());

        Boolean result = qzRechargeRecordApiService.saveQzRechargeRecord(qzRechargeRecordEntity);
        resultObj.put("code", "success");
        if (!result) {
            resultObj.put("code", "fail");
            resultObj.put("message", "保存充值记录失败");
            return resultObj;
        }

        //调用易宝充值
        Map<String, Object> yeepayMap = yeepayOrderBizService.yeepayMemberRechargeSubmmit(qzRechargeRecordEntity, validIP);
        if (yeepayMap != null) {
            resultObj.put("payurl", yeepayMap.get("payurl"));
        } else {
            return yeepayMap;
        }
        longger.info("调用易宝支付充值完成并保存充值记录成功，会员信息：{}", memberEntity.toString());
        return resultObj;


    }

    /**
     * 前端充值续费成功处理会员余额 权益
     *
     * @param tradeNo
     * @return
     */
    @Transactional
    public Map<String, Object> rechargeSuccessAndUpdateVplus(String tradeNo) {

        Map<String, Object> resultObject = new HashMap<>();

        if (!StringUtils.isNotEmpty(tradeNo)) {
            longger.error("平台流水号为空");
            throw new YeePayTradeNoNullException("平台流水号为空");
        }
        QzRechargeRecordEntity qzRechargeRecordEntity = qzRechargeRecordApiService.queryObjectByTradeNo(tradeNo);
        if (StringUtils.isNullOrEmpty(qzRechargeRecordEntity)) {
            longger.error("充值记录为空平台流水号为{}", tradeNo);
            throw new MemberNullException("充值记录为空");
        }
        qzRechargeRecordEntity.setMemo("易宝充值成功");
        qzRechargeRecordEntity.setUpdateTime(new Date());

        qzRechargeRecordApiService.update(qzRechargeRecordEntity);//更新充值记录

        resultObject.put("code", "success");
        MemberEntity memberEntity = queryObjectByUserIdOrMobile(qzRechargeRecordEntity.getShopUserId(), null);//查询会员
        if (StringUtils.isNullOrEmpty(memberEntity)) {
            longger.error("平台无此会员用户，用户id为{}", qzRechargeRecordEntity.getShopUserId());
            throw new MemberNullException("平台无此用户");
        }
        BigDecimal money = BigDecimal.ZERO;
        //处理克拉赠送用户账户增加
        boolean isUpdate = qzUserAccountApiService.updateUserAccountAndMoneyRecordByUserId(qzRechargeRecordEntity.getShopUserId(), money,MoneyChangeType.CARAT_CURRENCY , tradeNo);
        if (!isUpdate) {
            longger.error("克拉赠送用户账户增加保存失败，用户id为{}", qzRechargeRecordEntity.getShopUserId());
            resultObject.put("code", "fail");
        }
        //处理会员用户权益记录
        boolean isSave = vplusRecordInfoService.saveVplus(qzRechargeRecordEntity.getShopUserId(), null, qzRechargeRecordEntity.getAmount(), RechargePlatFormType.FRONT);
        if (!isSave) {
            longger.error("处理会员用户权益记录失败，用户id为{}", qzRechargeRecordEntity.getShopUserId());
            resultObject.put("code", "fail");
        }
        //延长会员vplus权益
        boolean isUpdateV = updateMemberVplus(qzRechargeRecordEntity.getShopUserId(), null, qzRechargeRecordEntity.getAmount(), RechargePlatFormType.FRONT);
        if (!isSave) {
            longger.error("延长会员vplus权益失败，用户id为{}", qzRechargeRecordEntity.getShopUserId());
            resultObject.put("code", "fail");
        }
        //增加会员成长值并返回成长值后的
        MemberEntity returnMemberEntity = memberShipUpgrade(money, null, memberEntity, GrowthType.RECHARGE);
        resultObject.put("memberEntity", returnMemberEntity);
        return resultObject;

    }


    /**
     * 第三方充值
     * 增加会员权益资金流水用户账户
     *
     * @param money
     * @param userId
     * @param validIP
     * @return
     */
    public Map<String, Object> thirdRechargeSuccessAndUpdateVplus(BigDecimal money, Integer userId, String mobile, String tradeNo, String validIP) {
        Map<String, Object> resultObject = new HashMap<>();
        if (!StringUtils.isNotEmpty(mobile) && StringUtils.isNullOrEmpty(userId)) {
            longger.error("userId和mobile至少一个不为空");
            throw new MemberNullException("userId和mobile至少一个不为空");
        }
        if (StringUtils.isNullOrEmpty(money)) {
            longger.error("充值金额为空userId={}mobile={}", userId, mobile);
            throw new MonetaryNullException("充值记录为空");
        }
        resultObject.put("code", "success");
        MemberEntity memberEntity = queryObjectByUserIdOrMobile(userId, mobile);//查询会员
        if (StringUtils.isNullOrEmpty(memberEntity)) {
            longger.error("平台无此会员用户，userId={}mobile={}", userId, mobile);
            throw new MemberNullException("平台无此用户");
        }

        //处理克拉赠送用户账户增加
        boolean isUpdate = qzUserAccountApiService.updateUserAccountAndMoneyRecordByUserId(userId, money,MoneyChangeType.CARAT_CURRENCY , tradeNo);
        if (!isUpdate) {
            longger.error("克拉赠送用户账户增加保存失败，用户id为{}", userId);
            resultObject.put("code", "fail");
        }
        //处理会员用户权益记录
        boolean isSave = vplusRecordInfoService.saveVplus(userId, null, money, RechargePlatFormType.THIRD);
        if (!isSave) {
            longger.error("处理会员用户权益记录失败，用户id为{}", userId);
            resultObject.put("code", "fail");
        }
        //延长会员vplus权益
        boolean isUpdateV = updateMemberVplus(userId, null, money, RechargePlatFormType.THIRD);
        if (!isSave) {
            longger.error("延长会员vplus权益失败，用户id为{}", userId);
            resultObject.put("code", "fail");
        }
        //增加会员成长值并返回成长值后的
        MemberEntity returnMemberEntity = memberShipUpgrade(money, null, memberEntity, GrowthType.RECHARGE);
        resultObject.put("memberEntity", returnMemberEntity);
        return resultObject;
    }

    /**
     * 根据手机号或者usreid 更新vplus 权益
     *
     * @param userId
     * @param mobile
     * @param money
     * @param type   平台类型
     * @return
     */
    @Override
    public boolean updateMemberVplus(Integer userId, String mobile, BigDecimal money, Integer type) {
        MemberEntity memberEntity = memberDao.queryObjectByUserIdOrMobile(userId, mobile);//查询会员

        BigDecimal cq;//充值期限
        BigDecimal zq;//赠送期限
        BigDecimal hq;//会员期限
        Date currentDate = new Date();
        Date endDate;

        if (memberEntity.getIsVplus() == FlagType.YES || type == RechargePlatFormType.THIRD) {
            cq = money.divide(new BigDecimal(5));
            zq = (cq.multiply(MemberGradeInit.getGradeMap().get(memberEntity.getMemberGrade()).getGiveDeadlineRatio())).setScale(0, BigDecimal.ROUND_UP);//向上取整 4.1 取 5
            hq = cq.add(zq);
            endDate = DateUtil.plusDay(currentDate, hq.intValue());
            memberEntity.setVplusEndDate(endDate);//设置会员到期时间
        } else if (memberEntity.getIsVplus() == FlagType.NO && memberEntity.getVplusBeginDate() == null) {//第一次开通vplus
            memberEntity.setIsVplus(FlagType.YES);//续费后 开通享有vplus权益
            hq = money.divide(new BigDecimal(5));
            endDate = DateUtil.plusDay(currentDate, hq.intValue());
            memberEntity.setVplusEndDate(endDate);//设置会员到期时间
        } else {//
            memberEntity.setIsVplus(FlagType.YES);//续费后 开通享有vplus权益
            hq = money.divide(new BigDecimal(5));
            endDate = DateUtil.plusDay(currentDate, hq.intValue());
            memberEntity.setVplusEndDate(endDate);//设置到期时间
        }
        return memberDao.update(memberEntity) == 0 ? false : true;
    }

    @Override
    public void autoCheckMemberVplusValided() {

        Map<String,Object> query=new HashMap<>();
        query.put("isVplus",FlagType.YES);
        List<MemberEntity> list = memberDao.queryList(query);//查询vplus 会员
        List<MemberEntity> updateList = new ArrayList<MemberEntity>();

        longger.info("平台共有{}会员", list.size());
        Date now = new Date();
        for (MemberEntity memberEntity : list) {
            if (now.after(memberEntity.getVplusEndDate())) {
                memberEntity.setIsVplus(FlagType.NO);
                String vipGrdae=JudgeMembershipLevelUtil.judgeLevel(memberEntity.getMembersGrowthValue().longValue(),GradeEnum.VIP);//判断vip等级
                memberEntity.setMemberGrade(vipGrdae);
                updateList.add(memberEntity);
            }
        }
        longger.info("更新时间{}>>>>平台共有{}会员vplus权益已经过期", now, updateList.size());
        longger.info("更新时间{}批量更新开始", now);
        memberDao.updateBatch(updateList);
        longger.info("更新时间{}批量更新结束消耗{}秒", now, (new Date().getTime() - now.getTime()) / 1000);

    }


}
