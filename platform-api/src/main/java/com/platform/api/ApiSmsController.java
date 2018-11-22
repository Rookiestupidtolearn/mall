package com.platform.api;

import com.platform.annotation.IgnoreAuth;
import com.platform.entity.SmsLogVo;
import com.platform.entity.SysSmsLogEntity;
import com.platform.service.ApiUserService;
import com.platform.service.SysSmsLogService;
import com.platform.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
//        String validIP = RequestUtil.getIpAddrByRequest(request);
//        if (ResourceUtil.getConfigByName("sms.validIp").indexOf(validIP) < 0) {
//        	 return R.error("非法IP请求！");
//        }
        if (params.get("mobile").equals("")) {
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
        	SmsLogVo smsLogVo = new SmsLogVo();
               smsLogVo.setLog_date(System.currentTimeMillis() / 1000);
               smsLogVo.setPhone(mobile);
               smsLogVo.setSms_code(sms_code);
               smsLogVo.setSms_text(msgContent);
               smsLogVo.setSend_status(1); //1成功   0失败
               userService.saveSmsCodeLog(smsLogVo);
		}
        
        return R.ok().put("result", sysSmsLogEntity);
    }
}
