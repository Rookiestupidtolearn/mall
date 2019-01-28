package com.platform.thirdrechard.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.platform.dao.*;
import com.platform.entity.*;
import com.platform.service.ApiSendSMSService;
import com.platform.thirdrechard.entity.QueryMemberCardRequest;
import com.platform.thirdrechard.entity.QueryMemberCardResponse;
import com.platform.thirdrechard.entity.RechargeResponseEntity;
import com.platform.thirdrechard.util.EncryptUtil;
import com.platform.thirdrechard.util.HttpCommonUtils;
import com.platform.utils.DateUtils;
import com.platform.utils.GenerateCodeUtil;
import com.platform.youle.util.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户充值记录Service实现类
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-12-17 13:35:56
 */
@Service("rechargeBizService")
public class RechargeBizService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ThirdRechargeRecordDao thirdRechargeRecordDao;
    @Autowired
    private ApiUserMapper userDao;
    @Autowired
    private QzRechargeRecordMapper qzRechargeRecordDao;
    @Autowired
    private QzUserAccountMapper qzUserAccountMapper;

    @Autowired
    private QzMoneyRecordMapper qzMoneyRecordDao;

    @Autowired
    private ApiSendSMSService apiSendSMSService;

    /**
     * 充值
     *
     * @param encrypt
     * @return
     * @throws Exception
     */
    public RechargeResponseEntity recharge(String encrypt) throws Exception {
        // 解密
        String data = EncryptUtil.aesDecrypt(encrypt);
        logger.info("充值解密后的密文是》》》》》》》》" + data);
        JSONObject jsonObject = JSONObject.parseObject(data);
        RechargeResponseEntity responseEntity = this.checkOrder(jsonObject);
        if (!responseEntity.getCode().equals("success")) {
            return responseEntity;
        }

//		"tradeAmt": 359,
//        "cardTradeNbr": "4xQhl1k2Ki8jG6NEO726s0U5ELLPj0",
//        "tradeCde": "1",

        // 校验9楼的是否已经充值了
        String thirdTradeNo = jsonObject.getString("thirdTradeNo");
        String platformType = jsonObject.getString("cardType");
        String mobile = jsonObject.getString("mobile");
        String amount = jsonObject.getString("amount");
        logger.info("斗宝商户充值开始thirdTrandeNo=：" + thirdTradeNo + "；手机号=：" + mobile + data);
        QueryMemberCardRequest vo = new QueryMemberCardRequest();
        vo.setCardTradeNbr(thirdTradeNo);
        vo.setCardTradeType(platformType);
        vo.setUserMobile(mobile);
        String str1 = HttpCommonUtils.sendPost(jsonObject.getString("queryCheckUrl"), JSONObject.toJSONString(vo));//向商户充值平台校验数据是否存在
        if (StringUtils.isEmpty(str1)) {
            responseEntity.setCode("1006");
            responseEntity.setMsg("校验url失败");
            return responseEntity;
        }
        if (str1.contains("html")) {
            responseEntity.setCode("1006");
            responseEntity.setMsg("校验url失败");
            logger.error("充值失败>>>>" + responseEntity.toString());
            return responseEntity;
        }
        if (str1.contains("Error")) {
            responseEntity.setCode("1006");
            responseEntity.setMsg("校验url失败");
            logger.error("充值失败>>>>" + responseEntity.toString());
            return responseEntity;
        }
        logger.info("查询9楼的充值情况参数返回结果:" + str1);
        JSONObject json = JSONObject.parseObject(str1);
        if (json.getString("state") == null || json.getString("state").equals("0")) {
            responseEntity.setCode("error");
            responseEntity.setMsg("校验订单原始状态失败");
            logger.error("充值失败>>>>" + responseEntity.toString());
            return responseEntity;
        }
        if (json.getString("data") != null) {
            QueryMemberCardResponse nEntity = JSONObject.parseObject(json.getString("data"), QueryMemberCardResponse.class);

            if (nEntity.getCardTradeNbr() == null) {
                responseEntity.setCode("1003");
                responseEntity.setMsg("订单编号错误");
                logger.error("充值失败>>>>" + responseEntity.toString());
                return responseEntity;
            }
            if (!nEntity.getCardTradeNbr().equals(thirdTradeNo)) {
                responseEntity.setCode("1003");
                responseEntity.setMsg("订单编号错误");
                logger.error("充值失败>>>>" + responseEntity.toString());
                return responseEntity;
            }
            if (nEntity.getTradeAmt() == null) {
                logger.info("金额不能为空。。。。");
                responseEntity.setCode("1002");
                responseEntity.setMsg("金额错误");
                logger.error("充值失败>>>>" + responseEntity.toString());
                return responseEntity;
            }

            if (new BigDecimal(amount).compareTo(new BigDecimal(nEntity.getTradeAmt())) != 0) {
                responseEntity.setCode("1002");
                responseEntity.setMsg("金额错误,不一致");
                logger.error("充值失败>>>>" + responseEntity.toString());
                return responseEntity;
            }
            if (nEntity.getTradeCde() == null) {
                logger.info("TradeCde 码不能为空！");
                responseEntity.setCode("error");
                responseEntity.setMsg("充值失败");
                logger.error("充值失败>>>>" + responseEntity.toString());
                return responseEntity;
            }
            if (!nEntity.getTradeCde().equals("1")) {
                responseEntity.setCode("error");
                responseEntity.setMsg("充值失败");
                logger.error("充值失败>>>>" + responseEntity.toString());
                return responseEntity;
            }

        }

        // 开启同步锁
        RechargeResponseEntity result = new RechargeResponseEntity();
        result.setCode("900");
        result.setCode("正在处理中");
        synchronized (jsonObject.getString("thirdTradeNo")) {
            ThirdRechargeRecordEntity nEntity = JSONObject.parseObject(data, ThirdRechargeRecordEntity.class);
            // 新充值
            result = this.newRecharge(nEntity);

        }

        return result;

    }

    /**
     * 新充值
     *
     * @param entity
     * @return
     */
    @Transactional
    private RechargeResponseEntity newRecharge(ThirdRechargeRecordEntity entity) {
        logger.info("三方用户充值流水入参:" + entity.toString());
        RechargeResponseEntity responseEntity = new RechargeResponseEntity();
        responseEntity.setCode("error");
        responseEntity.setMsg("充值失败");
        String tradeNo = GenerateCodeUtil.buildBizNo();

        Map<String, Object> result = this.rechargeForMobile(entity.getMobile(), entity.getAmount(), tradeNo);
        if (result.get("code").equals(200)) {
            responseEntity.setCode("success");
            responseEntity.setMsg("充值成功");
            // 记录流水
            entity.setState("1");//成功
            entity.setTradeNo(tradeNo);
            entity.setVersion(0);
            thirdRechargeRecordDao.save(entity);
            //发送充值完成的短信

            String loginSmsTemplet = PropertiesUtil.getValue("doubao.properties", "rechargeSmsTemplet");
            String msgContent = MessageFormat.format(loginSmsTemplet, entity.getMobile(), DateUtils.formatChina(new Date()), entity.getAmount());
            apiSendSMSService.sendSms(entity.getMobile(), msgContent);
        }
        logger.info("充值结果>>>>" + responseEntity.toString());
        return responseEntity;

    }

    /**
     * 给用户充值
     *
     * @param mobile
     * @param amount
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> rechargeForMobile(String mobile, BigDecimal amount, String tradeNo) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);// 成功
        if (StringUtils.isEmpty(mobile)) {
            result.put("code", "error");
            return result;
        }
        if (amount == null) {
            result.put("code", "error");
            return result;
        }
        if (StringUtils.isEmpty(tradeNo)) {
            tradeNo = GenerateCodeUtil.buildBizNo();
        }
        UserVo user = userDao.queryByMobile(mobile);

        try {
            if (user == null) {

                logger.info("用户充值>>>>" + mobile + "充值金额：" + amount);
                // 创建新的用户
                UserVo userVo = new UserVo();
                userVo.setMobile(mobile);
                userVo.setUsername("");
                userDao.save(userVo);
                // 创建新的充值流水
                QzRechargeRecordEntity record = new QzRechargeRecordEntity();
                record.setShopUserId(Integer.parseInt(userVo.getUserId().toString()));
                record.setState("1");
                record.setAmount(amount);
                record.setMemo("");
                record.setTradeNo(tradeNo);
                record.setMobile(mobile);
                record.setRechargeType(2);
                logger.info("充值记录信息" + JSON.toJSONString(record));
                qzRechargeRecordDao.save(record);
                // 创建资金流水
                QzMoneyRecordEntity moneyRecordEntity = new QzMoneyRecordEntity();
                moneyRecordEntity.setShopUserId(Integer.parseInt(userVo.getUserId().toString()));
                moneyRecordEntity.setTranType("1");
                moneyRecordEntity.setTranFlag(1);
                moneyRecordEntity.setTarnAmount(amount);
                moneyRecordEntity.setCurrentAmount(amount);
                moneyRecordEntity.setTradeNo(tradeNo);
                moneyRecordEntity.setLockAmount(new BigDecimal("0"));
                logger.info("充值资金流水信息" + JSON.toJSONString(moneyRecordEntity));
                qzMoneyRecordDao.save(moneyRecordEntity);
                // 操作用户余额
                QzUserAccountVo accountEntity = new QzUserAccountVo();
                accountEntity.setShop_user_id(Integer.parseInt(userVo.getUserId().toString()));
                accountEntity.setAmount(amount);
                accountEntity.setLast_update_time(new Date());
                accountEntity.setVersion(0);
                accountEntity.setLock_amount(new BigDecimal(0));
                logger.info("初次创建用户账户余额信息" + JSON.toJSONString(accountEntity));
                qzUserAccountMapper.save(accountEntity);

            } else {
                // 给老用户做充值
                // 创建新的充值流水
                QzRechargeRecordEntity record = new QzRechargeRecordEntity();
                record.setShopUserId(Integer.parseInt(user.getUserId().toString()));
                record.setState("1");
                record.setAmount(amount);
                record.setMemo("");
                record.setTradeNo(tradeNo);
                record.setMobile(mobile);
                record.setRechargeType(2);
                logger.info("充值记录信息" + JSON.toJSONString(record));
                qzRechargeRecordDao.save(record);

                // 充值记录流水
                QzMoneyRecordEntity moneyRecordEntity = new QzMoneyRecordEntity();
                moneyRecordEntity.setShopUserId(Integer.parseInt(user.getUserId().toString()));
                moneyRecordEntity.setTranType("1");
                moneyRecordEntity.setTranFlag(1);
                moneyRecordEntity.setTarnAmount(amount);
                moneyRecordEntity.setLockAmount(new BigDecimal("0"));
                QzUserAccountVo account = qzUserAccountMapper.queruUserAccountInfo(user.getUserId());
                if (account == null) {
                    moneyRecordEntity.setCurrentAmount(amount);
                } else {
                    moneyRecordEntity.setCurrentAmount(account.getAmount().add(amount));
                }

                moneyRecordEntity.setTradeNo(tradeNo);
                logger.info("充值资金流水信息" + JSON.toJSONString(moneyRecordEntity));
                qzMoneyRecordDao.save(moneyRecordEntity);

                // 操作用户余额
                if (account == null) {
                    QzUserAccountVo accountEntity = new QzUserAccountVo();
                    accountEntity.setShop_user_id(Integer.parseInt(user.getUserId().toString()));
                    accountEntity.setAmount(amount);
                    accountEntity.setLast_update_time(new Date());
                    accountEntity.setVersion(0);
                    accountEntity.setLock_amount(new BigDecimal(0));
                    logger.info("初次创建用户账户余额信息" + JSON.toJSONString(accountEntity));
                    qzUserAccountMapper.save(accountEntity);
                } else {
                    account.setLast_update_time(new Date());
                    account.setAmount(amount.add(account.getAmount()));
                    account.setVersion(account.getVersion() + 1);
                    logger.info("用户账户余额信息" + JSON.toJSONString(account));
                    qzUserAccountMapper.update(account);
                }

            }
        } catch (Exception e) {
            logger.error("处理充值失败", e);
            result.put("code", "error");
            throw new RuntimeException("数据插入失败，回滚");
        }
        return result;
    }

    public RechargeResponseEntity checkOrder(JSONObject jsonObject) {
        RechargeResponseEntity responseEntity = new RechargeResponseEntity();
        logger.info("校验充值解密后的值：" + jsonObject.toString());
        // 手机号
        String mobile = jsonObject.getString("mobile");
        if (StringUtils.isEmpty(mobile)) {
            responseEntity.setCode("1001");
            responseEntity.setMsg("手机号错误");
            logger.error(responseEntity.toString());
            return responseEntity;
        }
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (mobile.length() != 11) {
            responseEntity.setCode("1001");
            responseEntity.setMsg("手机号错误");
            logger.error(responseEntity.toString());
            return responseEntity;
        }
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(mobile);
        boolean isMatch = m.matches();

        if (!isMatch) {
            responseEntity.setCode("1001");
            responseEntity.setMsg("手机号错误");
            logger.error(responseEntity.toString());
            return responseEntity;
        }
        // 金额
        String amount = jsonObject.getString("amount");
        if (StringUtils.isEmpty(amount)) {
            responseEntity.setCode("1002");
            responseEntity.setMsg("金额错误");
            logger.error(responseEntity.toString());
            return responseEntity;
        }

        Pattern pattern = Pattern.compile("\\d\\.\\d*|[1-9]\\d*|\\d*\\.\\d*|\\d");
        Matcher match = pattern.matcher(amount);
        if (match.matches() == false) {
            responseEntity.setCode("1002");
            responseEntity.setMsg("金额错误");
            logger.error(responseEntity.toString());
            return responseEntity;
        }

        Double checkAmount = Double.valueOf(amount);
        if (checkAmount <= 0) {
            responseEntity.setCode("1002");
            responseEntity.setMsg("金额应大于0");
            logger.error(responseEntity.toString());
            return responseEntity;
        }

        Double bigAmount = Double.valueOf(amount);
        if (bigAmount > 100000) {
            responseEntity.setCode("1002");
            responseEntity.setMsg("充值金额不能大于10万元");
            logger.error(responseEntity.toString());
            return responseEntity;
        }

        Pattern pattern2 = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
        Matcher match2 = pattern2.matcher(amount);
        if (match2.matches() == false) {
            responseEntity.setCode("1002");
            responseEntity.setMsg("充值金额格式错误");
            logger.error(responseEntity.toString());
            return responseEntity;
        }
        // 订单编号
        String thirdTradeNo = jsonObject.getString("thirdTradeNo");
        if (StringUtils.isEmpty(thirdTradeNo)) {
            responseEntity.setCode("1003");
            responseEntity.setMsg("订单编号错误");
            logger.error(responseEntity.toString());
            return responseEntity;
        }
        if (thirdTradeNo.length() > 50) {
            responseEntity.setCode("1003");
            responseEntity.setMsg("订单编号错误");
            logger.error(responseEntity.toString());
            return responseEntity;
        }

        String rechargeType = jsonObject.getString("platformType");
        if (StringUtils.isEmpty(rechargeType)) {
            responseEntity.setCode("1004");
            responseEntity.setMsg("充值渠错误");
            logger.error(responseEntity.toString());
            return responseEntity;
        }
        String queryCheckUrl = jsonObject.getString("queryCheckUrl");
        if (StringUtils.isEmpty(queryCheckUrl)) {
            responseEntity.setCode("1006");
            responseEntity.setMsg("校验url不能为空");
            logger.error(responseEntity.toString());
            return responseEntity;
        }
        String cardType = jsonObject.getString("cardType");
        if (StringUtils.isEmpty(cardType)) {
            responseEntity.setCode("1007");
            responseEntity.setMsg("校验会员卡类型失败");
            logger.error(responseEntity.toString());
            return responseEntity;
        }

        // 检查订单号是否重复
        ThirdRechargeRecordEntity entity = thirdRechargeRecordDao.queryByThirdTradeNo(thirdTradeNo);
        if (entity != null) {
            responseEntity.setCode("1005");
            responseEntity.setMsg("重复下订单");
            logger.error(responseEntity.toString());
            return responseEntity;
        }
        responseEntity.setCode("success");
        responseEntity.setMsg("校验成功");
        logger.error(responseEntity.toString());
        return responseEntity;
    }

}
