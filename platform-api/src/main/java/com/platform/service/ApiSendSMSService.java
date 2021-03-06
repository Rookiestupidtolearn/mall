package com.platform.service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.platform.cache.J2CacheUtils;
import com.platform.config.SmsType;
import com.platform.entity.SmsConfig;
import com.platform.entity.SmsLogVo;
import com.platform.entity.SysConfigEntity;
import com.platform.entity.SysSmsLogEntity;
import com.platform.util.sms.chuanglan.ChuangLanSmsUtil;
import com.platform.util.sms.chuanglan.SmsSendRequest;
import com.platform.utils.CharUtil;
import com.platform.utils.Constant;
import com.platform.utils.DateUtils;
import com.platform.utils.RRException;
import com.platform.utils.StringUtils;
import com.platform.youle.util.PropertiesUtil;

import net.oschina.j2cache.CacheProviderHolder;
import net.oschina.j2cache.Level2Cache;

@Service
public class ApiSendSMSService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private SysConfigService sysConfigService;
	@Autowired
	private ApiUserService userService;
    @Autowired
    private SysSmsLogService smsLogService;

	/**
	 * 发送短信验证码
	 * 
	 * @param mobile
	 * @param ip
	 * @param smsType
	 *            1-登录 2-绑定手机号
	 * 
	 * @return
	 */
	public Map<String, Object> sendSms(String mobile, String ip, String smsType, Integer userId) {
		if (org.apache.commons.lang.StringUtils.isEmpty(mobile)) {
			return toResponsFalseObject("手机号不能为空");
		}
		// 校验手机号格式
		String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
		if (mobile.length() != 11) {
			return toResponsFalseObject("手机号错误");
		}
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(mobile);
		boolean isMatch = m.matches();

		if (!isMatch) {
			return toResponsFalseObject("手机号错误");
		}
		if (org.apache.commons.lang.StringUtils.isEmpty(ip)) {
			return toResponsFalseObject("请传ip");
		}
		if (org.apache.commons.lang.StringUtils.isEmpty(smsType)) {
			return toResponsFalseObject("请传短信发送的类型！");
		}
		if (smsType.equals("2")) {
			if (userId == null) {
				return toResponsFalseObject("userId不能为空！");
			}
		}

		Date dateStart = new Date();
		String endTime = DateUtils.format(dateStart);
		Date endDate = DateUtils.strToDate(endTime + " 23:59:59");

		int cha = DateUtils.getBetweenDateByType(endDate, dateStart, "second");
		long longCha = Long.parseLong(String.valueOf(cha));

		// 手机号
		Level2Cache level2 = CacheProviderHolder.getLevel2Cache(J2CacheUtils.INVALID_CACHE);
		Integer count = (Integer) level2.get("DOUBAO_SMS_COUNT:" + mobile);

		if (count != null) {
			if (count >= 10) {
				return toResponsFalseObject("操作频繁，明天再试");
			}
			count += 1;
		} else {
			count = 1;
		}

		Integer countIP = (Integer) level2.get("DOUBAO_SMS_IP_COUNT:" + ip);
		if (countIP != null) {
			if (countIP >= 10) {
				level2.put("DOUBAO_SMS_MOBILE_IP:" + mobile + "_" + ip, mobile + "_" + ip, longCha);
				return toResponsFalseObject("操作频繁，明天再试");
			}
			countIP += 1;
		} else {
			countIP = 1;
		}


		// 生成验证码
		String sms_code = CharUtil.getRandomNum(4);
      	String  loginSmsTemplet = PropertiesUtil.getValue("doubao.properties","loginSmsTemplet");
		String msgContent = MessageFormat.format(loginSmsTemplet, sms_code);
		// 发送短信
		String result = "";
		String codeValue = "";
		// 获取云存储配置信息
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
		if (StringUtils.isNullOrEmpty(config.getDomain())) {
			throw new RRException("请先配置短信平台域名URL");
		}
		try {
			/**
			 * 状态,发送编号,无效号码数,成功提交数,黑名单数和消息，无论发送的号码是多少，一个发送请求只返回一个sendid，如果响应的状态不是“0”，则只有状态和消息
			 */
         
			
			// 短信发送的URL 请登录zz.253.com 获取完整的URL接口信息
			String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";

			// 设置您要发送的内容：其中“【】”中括号为运营商签名符号，多签名内容前置添加提交
			String msg = config.getSign() + msgContent;
			// 手机号码

			// 状态报告
			String report = "true";

			SmsSendRequest smsSingleRequest = new SmsSendRequest(config.getUsername(), config.getPassword(), msg, mobile,
					report);

			String requestJson = JSON.toJSONString(smsSingleRequest);
    
			//添加发短信的开关 
			String isSend = "";
			String smsConfig = sysConfigService.queryByKey("sendSms");
			if (smsConfig == null) {
				isSend = "true";
			}
            if (smsConfig != null && smsConfig.equals("true")) {
            	isSend = "true";
			}
			
			if (isSend.equals("true")) {
				result = ChuangLanSmsUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
				Gson gson = new Gson();
				Map<String, Object> map = new HashMap<String, Object>();
				map = gson.fromJson(result, map.getClass());
				codeValue = (String) map.get("code");
			}else {
				codeValue = "0";
			}


		} catch (Exception e) {

		}

		if ("0".equals(codeValue)) {
			
			Object codeSmsValue = J2CacheUtils.get(J2CacheUtils.CHECK_CACHE, "DOUBAO:"+mobile);
        	if (codeSmsValue != null) {
        		J2CacheUtils.remove(J2CacheUtils.CHECK_CACHE,"DOUBAO:"+mobile);
    		}
        	//验证码放入缓存
        	  int  num = J2CacheUtils.check(J2CacheUtils.CHECK_CACHE,"DOUBAO:"+mobile);
          	J2CacheUtils.putExire(J2CacheUtils.CHECK_CACHE,"DOUBAO:"+mobile, sms_code, 120l);

			
			SmsLogVo smsLogVo = new SmsLogVo();
			smsLogVo.setLog_date(System.currentTimeMillis() / 1000);
			if (userId != null) {
				smsLogVo.setUser_id(Long.valueOf(userId.toString()));
			}
			smsLogVo.setPhone(mobile);
			smsLogVo.setSmsCode(sms_code);
			smsLogVo.setSms_text(msgContent);
			smsLogVo.setSend_status(1); // 1成功 0失败
			smsLogVo.setCreateTime(new Date());
			userService.saveSmsCodeLog(smsLogVo);
			logger.info("手机号>>>"+mobile+"验证码是>>>"+sms_code);
			// 手机号和缓存
			 level2.put("DOUBAO_SMS_COUNT:"+mobile, count,longCha);
			// ip缓存
			 if (ip.equals("114.254.149.230") || ip.equals("58.135.84.33") ||  ip.equals("123.113.155.112")) {
				  
			 }else{
					level2.put("DOUBAO_SMS_IP_COUNT:"+ip, countIP,longCha);
					 if (countIP !=null) {
				        	logger.info("今日用手机号:"+mobile+">>所在的IP地址"+ip+"已发送"+countIP+"次");
						}
			 }
			Map<String, Object> re = toResponsScuuessObject("短信发送成功");
			re.put("count", count);
	        if (count !=null) {
	            logger.info("今日手机号"+mobile+"已发送"+count+"次");
			}
			return re;
		} else {
			return toResponsFalseObject("短信发送失败");
		}

	}

	/**
	 * 发送营销短信
	 * @param mobile
	 * @param content
	 * @return
	 */
	public  Map<String, Object>  sendSms(String mobile,String content){
			if (org.apache.commons.lang.StringUtils.isEmpty(mobile)) {
				return toResponsFalseObject("手机号不能为空");
			}
			// 校验手机号格式
			String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
			if (mobile.length() != 11) {
				return toResponsFalseObject("手机号错误");
			}
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(mobile);
			boolean isMatch = m.matches();

			if (!isMatch) {
				return toResponsFalseObject("手机号错误");
			}
			if (org.apache.commons.lang.StringUtils.isEmpty(content)) {
				return toResponsFalseObject("发送的短信内容不能为空！");
			}
			// 获取云存储配置信息
			SmsConfig config = sysConfigService.getConfigObject(Constant.SMS_CONFIG_MARKET_KEY, SmsConfig.class);
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
			if (StringUtils.isNullOrEmpty(config.getDomain())) {
				throw new RRException("请先配置短信平台域名URL");
			}
			
			Map<String, Object> map = new HashMap<String, Object>();
		
				// 设置您要发送的内容：其中“【】”中括号为运营商签名符号，多签名内容前置添加提交
				String msg = config.getSign() + content+" 退订回T";
				
				// 短信发送的URL 请登录zz.253.com 获取完整的URL接口信息
				String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
				// 发送短信
				String result = "";
				String codeValue = "";

				// 状态报告
				String report = "true";

				SmsSendRequest smsSingleRequest = new SmsSendRequest(config.getUsername(), config.getPassword(), msg, mobile,
						report);

				String requestJson = JSON.toJSONString(smsSingleRequest);
	    
				//添加发短信的开关 
				String isSend = "";
				String smsConfig = sysConfigService.queryByKey("sendSms_market");
				if (smsConfig == null) {
					isSend = "true";
				}
	            if (smsConfig != null && smsConfig.equals("true")) {
	            	isSend = "true";
				}
				
				if (isSend.equals("true")) {
					logger.info("请求创蓝的营销短信的参数"+requestJson);
					result = ChuangLanSmsUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
					logger.info("创蓝返回的营销短信的结果"+result);
					Gson gson = new Gson();
			
					map = gson.fromJson(result, map.getClass());
					codeValue = (String) map.get("code");
				}else {
					codeValue = "0";
				}
		
			if ("0".equals(codeValue)) {
								
				SysSmsLogEntity smsVo = new SysSmsLogEntity();
				smsVo.setContent(msg);
				smsVo.setMobile(mobile);
				smsVo.setStime(new Date());
				smsVo.setSign(config.getSign());
				smsVo.setType("pt");
				smsVo.setSendStatus(1);
				smsLogService.save(smsVo);

			} else {
				return toResponsFalseObject("短信发送失败");

			}
		 
		return map;
	}
	

	private Map<String, Object> toResponsFalseObject(String msg) {
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("errno", 1);
		obj.put("msg", msg);
		return obj;
	}

	private Map<String, Object> toResponsScuuessObject(String msg) {
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("errno", 0);
		obj.put("msg", msg);
		return obj;
	}
}
