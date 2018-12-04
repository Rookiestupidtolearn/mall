package com.platform.api;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.code.kaptcha.Constants;
import com.platform.annotation.IgnoreAuth;
import com.platform.cache.J2CacheUtils;
import com.platform.entity.SmsLogVo;
import com.platform.entity.SysSmsLogEntity;
import com.platform.service.ApiUserService;
import com.platform.service.SysSmsLogService;
import com.platform.utils.CharUtil;
import com.platform.utils.DateUtils;
import com.platform.utils.R;
import com.platform.utils.RequestUtil;
import com.platform.utils.ShiroUtils;
import com.platform.utils.StringUtils;

import net.oschina.j2cache.CacheProviderHolder;
import net.oschina.j2cache.Level2Cache;

/**
 * 发送短信接口Controller
 *
 * @author liepngjun
 * @email 939961241@qq.com
 * @date 2018-06-05 13:58:47
 */
@RestController
@RequestMapping("api")
public class ApiSmsController {
    @Autowired
    private SysSmsLogService smsLogService;
    @Autowired
    private ApiUserService userService;
    /**
     * 发送短信
     *
     * @param request request
     * @param params 请求参数{mobile：电话号码字符串，中间用英文逗号间隔,content：内容字符串,stime：追加发送时间，可为空，为空为及时发送}
     * @return R
     */
    @IgnoreAuth
    @PostMapping("/sendSms")
    public R sendSms(HttpServletRequest request, @RequestParam Map<String, String> params) {
        SysSmsLogEntity smsLog = new SysSmsLogEntity();
       String validIP = RequestUtil.getIpAddrByRequest(request);
//        if (ResourceUtil.getConfigByName("sms.validIp").indexOf(validIP) < 0) {
//        	 return R.error("非法IP请求！");
//        }
        if (params.get("mobile")== null ||params.get("mobile").equals("") ) {
        	 return R.error("手机号不能为空！");
		}
        //校验手机号格式
       String mobile =  params.get("mobile");
		String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
		if (mobile.length() != 11) {
			 return R.error("手机号错误");
		}
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(mobile);
		boolean isMatch = m.matches();

		if (!isMatch) {
			 return R.error("手机号错误");
		}
        Object va = J2CacheUtils.get(J2CacheUtils.CHECK_CACHE, "DOUBAO:"+mobile);
    	if (va != null) {
    		 return R.error("操作频繁");
		}
    	Level2Cache level2 = CacheProviderHolder.getLevel2Cache(J2CacheUtils.INVALID_CACHE);
        //手机号
        Integer count =(Integer) level2.get("DOUBAO_SMS_COUNT:"+mobile);
        
        if (count!=null) {
        	  if (count>10) {
        		   return R.error("操作频繁，明天再试");
			  }
        	  count +=1;
		 }else {
			 count = 1;
		}
        

    	level2.put("DOUBAO_SMS_COUNT:"+mobile, count,86400l);
     	
     	R result = R.ok().put("count", count);
     	
        //ip地址
        Integer countIP =(Integer) level2.get("DOUBAO_SMS_IP_COUNT:"+validIP);
        
        if (countIP!=null) {
        	  if (countIP>10) {
        		   return R.error("操作频繁，明天再试");
			  }
        	  countIP +=1;
		 }else {
			 countIP = 1;
		}
    
        level2.put("DOUBAO_SMS_IP_COUNT:"+validIP, countIP,86400l);
    	//校验图形验证码
    	if (count >=5) {
            String captcha =  params.get("code");
            if (captcha == null) {
            	return result.put("msg", "请传入图形验证码");
			}
            String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
            if(null == kaptcha){
                return R.error("验证码已失效");
            }

            if (!captcha.equalsIgnoreCase(kaptcha)) {
                return R.error("验证码不正确");
            }
		}
    	
        String sms_code = CharUtil.getRandomNum(4);
        String msgContent = "您的验证码是：" + sms_code + "，请在页面中提交验证码完成验证。";
        smsLog.setMobile(params.get("mobile"));
        smsLog.setContent(msgContent);
        String stime = params.get("stime");
        if (StringUtils.isNotEmpty(stime)) {
            smsLog.setStime(DateUtils.convertStringToDate(stime));
        }

        SysSmsLogEntity sysSmsLogEntity = smsLogService.sendSms(smsLog);
        if (sysSmsLogEntity.getType()!=null && sysSmsLogEntity.getType().equals(("pt"))) {
        	Object codeSmsValue = J2CacheUtils.get(J2CacheUtils.CHECK_CACHE, "DOUBAO:"+mobile);
        	if (codeSmsValue != null) {
        		J2CacheUtils.remove(J2CacheUtils.CHECK_CACHE,"DOUBAO:"+mobile);
    		}
        	//验证码放入缓存
        	  int  num = J2CacheUtils.check(J2CacheUtils.CHECK_CACHE,"DOUBAO:"+mobile);
          	J2CacheUtils.putExire(J2CacheUtils.CHECK_CACHE,"DOUBAO:"+mobile, sms_code, 120l);

        	SmsLogVo  userSms = new SmsLogVo();
        	userSms.setLog_date(System.currentTimeMillis() / 1000);
        	userSms.setPhone(mobile);
        	userSms.setSmsCode(sms_code);
        	userSms.setSms_text(msgContent);
        	userSms.setSend_status(1); //1成功   0失败
              userService.saveSmsCodeLog(userSms);
		}
       result.put("result", "发送成功");
        return  result;
    }
}
