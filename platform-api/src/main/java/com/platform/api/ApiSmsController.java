package com.platform.api;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.code.kaptcha.Producer;
import com.platform.annotation.IgnoreAuth;
import com.platform.cache.J2CacheUtils;
import com.platform.entity.SysSmsLogEntity;
import com.platform.service.ApiSendSMSService;
import com.platform.service.ApiUserService;
import com.platform.service.SysSmsLogService;
import com.platform.utils.R;
import com.platform.utils.RequestUtil;

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
@RequestMapping("/api")
public class ApiSmsController {
	private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SysSmsLogService smsLogService;
    @Autowired
    private ApiUserService userService;
    @Autowired
    private ApiSendSMSService apiSendSMSService;
    @Autowired
    private Producer  producer;
    private final static String  SESSION_SECURITY_CODE= "SESSION_SECURITY_CODE" ; 
    
    @RequestMapping("/image.jpg")
    @IgnoreAuth
    public void captcha(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
    	
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);
    	request.setAttribute(DefaultSubjectContext.SESSION_CREATION_ENABLED, Boolean.TRUE);
       HttpSession session = request.getSession();
       session.setAttribute("imageCode", text);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
    }
    
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
        if (params.get("mobile")== null ||params.get("mobile").equals("") ) {
        	 return R.error("手机号不能为空！");
		}
   
    	//校验图形验证码
		Level2Cache level2 = CacheProviderHolder.getLevel2Cache(J2CacheUtils.INVALID_CACHE);
		Integer count = (Integer) level2.get("DOUBAO_SMS_COUNT:" + params.get("mobile"));
		 Integer countIP = (Integer) level2.get("DOUBAO_SMS_IP_COUNT:" + validIP);
        if (count == null) {
        	count = 0;
		}
        if (countIP == null) {
        	countIP = 0;
		}
		 
        Map<String, Object> result = new HashMap<String, Object>();
        if (count !=null) {
            logger.info("今日手机号"+params.get("mobile")+"已发送"+count+"次");
		}
        if (countIP !=null) {
        	logger.info("今日用手机号:"+params.get("mobile")+">>所在的IP地址"+validIP+"已发送"+countIP+"次");
		}
        
        if (count >=5) {
        	 String imageCode = (String) request.getSession().getAttribute("imageCode");
        	if (StringUtils.isEmpty(params.get("imageCode"))) {
        		result.put("errno", 1);
            	result.put("msg", "请传入图形验证码");
            	result.put("count", count);
            	return result;
			}
        	
            if (StringUtils.isEmpty(imageCode)) {
            	result.put("errno", 1);
            	result.put("msg", "图形验证码已失效");
            	result.put("count", count);
            	return result;
			}

            if (!imageCode.equals(params.get("imageCode").toString())) {
            	result.put("errno", 1);
            	result.put("msg", "图形验证码错误");
            	result.put("count", count);
            	return result;
            }
		}
        
        return  apiSendSMSService.sendSms(params.get("mobile"), validIP, "1",null);
        
    }
}
