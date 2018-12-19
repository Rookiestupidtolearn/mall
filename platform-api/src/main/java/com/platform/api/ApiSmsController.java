package com.platform.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.code.kaptcha.Constants;
import com.platform.annotation.IgnoreAuth;
import com.platform.cache.J2CacheUtils;
import com.platform.entity.SysSmsLogEntity;
import com.platform.service.ApiSendSMSService;
import com.platform.service.ApiUserService;
import com.platform.service.SysSmsLogService;
import com.platform.utils.R;
import com.platform.utils.RequestUtil;
import com.platform.utils.ShiroUtils;

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
	private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SysSmsLogService smsLogService;
    @Autowired
    private ApiUserService userService;
    @Autowired
    private ApiSendSMSService apiSendSMSService;
    /**
     * 发送短信
     *
     * @param request request
     * @param params 请求参数{mobile：电话号码字符串，中间用英文逗号间隔,content：内容字符串,stime：追加发送时间，可为空，为空为及时发送}
     * @return R
     */
    @IgnoreAuth
    @PostMapping("/sendSms")
    public Object sendSms(HttpServletRequest request, @RequestParam Map<String, String> params) {
    	logger.info("api/sendSms发送登录短信验证码入参："+params.toString());
    	SysSmsLogEntity smsLog = new SysSmsLogEntity();
       String validIP = RequestUtil.getIpAddrByRequest(request);
//        if (ResourceUtil.getConfigByName("sms.validIp").indexOf(validIP) < 0) {
//        	 return R.error("非法IP请求！");
//        }
        if (params.get("mobile")== null ||params.get("mobile").equals("") ) {
        	 return R.error("手机号不能为空！");
		}
        Integer count = 0 ;
        Integer countIP = 0;
    	//校验图形验证码
		Level2Cache level2 = CacheProviderHolder.getLevel2Cache(J2CacheUtils.INVALID_CACHE);
		 count = (Integer) level2.get("DOUBAO_SMS_COUNT:" + params.get("mobile"));
         countIP = (Integer) level2.get("DOUBAO_SMS_IP_COUNT:" + validIP);
   
        Map<String, Object> result = new HashMap<String, Object>();
        if (count !=null) {
            logger.info("今日手机号"+params.get("mobile")+"已发送"+count+"次");
		}
        if (countIP !=null) {
        	logger.info("今日用手机号:"+params.get("mobile")+">>所在的IP地址"+validIP+"已发送"+countIP+"次");
		}
        
        if (count >=5 || countIP >=5) {
            String captcha =  params.get("code");
            if (captcha == null) {
            	return result.put("msg", "请传入图形验证码");
			}
            String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
            if(null == kaptcha){
                return R.error("图形验证码已失效");
            }

            if (!captcha.equalsIgnoreCase(kaptcha)) {
                return R.error("图形验证码错误");
            }
		}
        
        return  apiSendSMSService.sendSms(params.get("mobile"), validIP, "1",null);
        
    }
}
