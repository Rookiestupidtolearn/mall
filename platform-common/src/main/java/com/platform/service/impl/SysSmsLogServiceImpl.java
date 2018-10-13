package com.platform.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.SysSmsLogDao;
import com.platform.entity.SmsConfig;
import com.platform.entity.SysSmsLogEntity;
import com.platform.service.SysConfigService;
import com.platform.service.SysSmsLogService;
import com.platform.utils.Constant;
import com.platform.utils.DateUtils;
import com.platform.utils.IdUtil;
import com.platform.utils.RRException;
import com.platform.utils.ShiroUtils;
import com.platform.utils.SmsUtil;
import com.platform.utils.StringUtils;

@Service("smsLogService")
public class SysSmsLogServiceImpl implements SysSmsLogService {
    @Autowired
    private SysSmsLogDao smsLogDao;
    @Autowired
    private SysConfigService sysConfigService;

    @Override
    public SysSmsLogEntity queryObject(String id) {
        return smsLogDao.queryObject(id);
    }

    @Override
    public List<SysSmsLogEntity> queryList(Map<String, Object> map) {
        return smsLogDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return smsLogDao.queryTotal(map);
    }

    @Override
    public int save(SysSmsLogEntity smsLog) {
        smsLog.setId(IdUtil.createIdbyUUID());
        return smsLogDao.save(smsLog);
    }

    @Override
    public int update(SysSmsLogEntity smsLog) {
        return smsLogDao.update(smsLog);
    }

    @Override
    public int delete(String id) {
        return smsLogDao.delete(id);
    }

    @Override
    public int deleteBatch(String[] ids) {
        return smsLogDao.deleteBatch(ids);
    }

    @Override
    public SysSmsLogEntity sendSms(SysSmsLogEntity smsLog) {
        String result = "";
        //获取云存储配置信息
        SmsConfig config = sysConfigService.getConfigObject(Constant.SMS_CONFIG_KEY, SmsConfig.class);
        if (StringUtils.isNullOrEmpty(config)) {
            throw new RRException("请先配置短信平台信息");
        }
        if (StringUtils.isNullOrEmpty(config.getUsername())) {
            throw new RRException("请先配置短信平台用户名");
        }
        if (StringUtils.isNullOrEmpty(config.getPassword())) {
            throw new RRException("请先配置短信平台密钥");
        }
        if (StringUtils.isNullOrEmpty(config.getEpid())) {
            throw new RRException("请先配置短信平台epid");
        }
        if (StringUtils.isNullOrEmpty(config.getSign())) {
        	throw new RRException("请先配置短信平台签名");
        }
        try {
            /**
             * 状态,发送编号,无效号码数,成功提交数,黑名单数和消息，无论发送的号码是多少，一个发送请求只返回一个sendid，如果响应的状态不是“0”，则只有状态和消息
             result："00"="发送成功";
					"1"="参数不完整，请检查所带的参数名是否都正确";
					"2"="鉴权失败，一般是用户名密码不对";
					"3"="号码数量超出50条";
					"4"="发送失败";
					"5"="余额不足";
					"6"="发送内容含屏蔽词";
					"7"="短信内容超出350个字";
					"72"="内容被审核员屏蔽";
					"8:OverLimit!"="号码列表中没有合法的手机号码或手机号为黑名单或验证码类每小时超过限制条数";
					"9"="夜间管理，不允许一次提交超过20个号码";
					"10"="{txt}不应当出现在提交数据中，请修改[模板类账号]（适用于模板类帐户）";
					"11"="模板匹配成功[模板类必审、免审账号]（适用于模板类帐户）";
					"12"="未匹配到合适模板，已提交至审核员审核[模板类必审账号]（适用于模板类帐户）";
					"13"="未匹配到合适模板，无法下发，请修改[模板类免审账号]（适用于模板类帐户）";
					"14"="该账户没有模板[模板类账号]（适用于模板类帐户）";
					"15"="操作失败[模板类账号]（适用于模板类帐户）";
					"02"="手机号码为黑名单";
					"81"="手机号码错误，请检查手机号是否正确";
             */
            result = SmsUtil.crSendSms(config.getUsername(), config.getPassword(), smsLog.getMobile(),config.getSign()+smsLog.getContent(), config.getEpid(),
                    DateUtils.format(smsLog.getStime(), "yyyy-MM-dd HH:mm:ss"), smsLog.getExtno());
        } catch (Exception e) {

        }
        String arr[] = result.split(",");
        //发送成功
        if ("00".equals(arr[0])) {
        	//保存短信发送日志
        	smsLog.setContent(config.getSign()+smsLog.getContent());
        	smsLog.setMobile(smsLog.getMobile());
            smsLog.setType("pt");
            smsLog.setReturnMsg(arr[0]);
            smsLog.setSign(config.getSign());
            smsLog.setSendStatus(1); //发送成功
        } else {
            //发送失败
            smsLog.setReturnMsg(arr[0]);
        }
        try {
            smsLog.setUserId(ShiroUtils.getUserId());
        } catch (Exception e) {
            //外部发送短信
            smsLog.setUserId(1L);
        }
        if (null == smsLog.getStime()) {
            smsLog.setStime(new Date());
        }
        //保存发送记录
        save(smsLog);
        return smsLog;
    }
}
