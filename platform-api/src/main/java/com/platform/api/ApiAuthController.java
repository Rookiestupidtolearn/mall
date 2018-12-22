package com.platform.api;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.IgnoreAuth;
import com.platform.cache.J2CacheUtils;
import com.platform.entity.FullUserInfo;
import com.platform.entity.UserInfo;
import com.platform.entity.UserVo;
import com.platform.service.ApiUserService;
import com.platform.service.TokenService;
import com.platform.util.ApiBaseAction;
import com.platform.util.ApiUserUtils;
import com.platform.util.CommonUtil;
import com.platform.utils.R;
import com.platform.validator.Assert;
import com.qiniu.util.StringUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.oschina.j2cache.CacheProviderHolder;
import net.oschina.j2cache.Level2Cache;

/**
 * API登录授权
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-03-23 15:31
 */
@Api(tags = "API登录授权接口")
@RestController
@RequestMapping("/api/auth")
public class ApiAuthController extends ApiBaseAction {
    private Logger logger = Logger.getLogger(getClass());
    @Autowired
    private ApiUserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 登录
     */
    @IgnoreAuth
    @PostMapping("login")
    @ApiOperation(value = "登录接口")
    public R login(String mobile, String password) {
        Assert.isBlank(mobile, "手机号不能为空");
        Assert.isBlank(password, "密码不能为空");

        //用户登录
        long userId = userService.login(mobile, password);

        //生成token
        Map<String, Object> map = tokenService.createToken(userId);

        return R.ok(map);
    }

    /**
     * 登录
     */
    @ApiOperation(value = "登录")
    @IgnoreAuth
    @PostMapping("login_by_weixin")
    public Object loginByWeixin() {
        JSONObject jsonParam = this.getJsonRequest();
        FullUserInfo fullUserInfo = null;
        String code = "";
        if (!StringUtils.isNullOrEmpty(jsonParam.getString("code"))) {
            code = jsonParam.getString("code");
        }
        if (null != jsonParam.get("userInfo")) {
            fullUserInfo = jsonParam.getObject("userInfo", FullUserInfo.class);
        }

        Map<String, Object> resultObj = new HashMap<String, Object>();
        //
        UserInfo userInfo = fullUserInfo.getUserInfo();

        //获取openid
        String requestUrl = ApiUserUtils.getWebAccess(code);//通过自定义工具类组合出小程序需要的登录凭证 code
        logger.info("》》》组合token为：" + requestUrl);
        String res = restTemplate.getForObject(requestUrl, String.class);
        JSONObject sessionData = JSON.parseObject(res);

        if (null == sessionData || StringUtils.isNullOrEmpty(sessionData.getString("openid"))) {
            return toResponsFail("登录失败");
        }
        //验证用户信息完整性
        String sha1 = CommonUtil.getSha1(fullUserInfo.getRawData() + sessionData.getString("session_key"));
        if (!fullUserInfo.getSignature().equals(sha1)) {
            return toResponsFail("登录失败");
        }
        Date nowTime = new Date();
        UserVo userVo = userService.queryByOpenId(sessionData.getString("openid"));
        if (null == userVo) {
            userVo = new UserVo();
            userVo.setUsername("");
            userVo.setPassword(sessionData.getString("openid"));
            userVo.setRegisterTime(nowTime);
            userVo.setRegisterIp(this.getClientIp());
            userVo.setLastLoginIp(userVo.getRegisterIp());
            userVo.setLastLoginTime(userVo.getRegisterTime());
            userVo.setWeixinOpenid(sessionData.getString("openid"));
            userVo.setAvatar(userInfo.getAvatarUrl());
            userVo.setGender(userInfo.getGender()); // //性别 0：未知、1：男、2：女
            userVo.setNickname(userInfo.getNickName());
            userService.save(userVo);
        } else {
            userVo.setLastLoginIp(this.getClientIp());
            userVo.setLastLoginTime(nowTime);
            userService.update(userVo);
        }

        Map<String, Object> tokenMap = tokenService.createToken(userVo.getUserId());
        String token = MapUtils.getString(tokenMap, "token");

        if (null == userInfo || StringUtils.isNullOrEmpty(token)) {
            return toResponsFail("登录失败");
        }

        resultObj.put("token", token);
        resultObj.put("userInfo", userInfo);
        resultObj.put("userId", userVo.getUserId());
        return toResponsSuccess(resultObj);
    }
    

    /**
     * 根据手机号登录
     */
    @ApiOperation(value = "根据手机号登录")
    @IgnoreAuth
    @PostMapping("login_by_mobile")
    public Object loginByMobile( @RequestParam Map<String, String> params ,HttpServletRequest requset) {
    	requset.setAttribute(DefaultSubjectContext.SESSION_CREATION_ENABLED, Boolean.TRUE);
    	//校验手机号格式
    	if (params.get("mobile").equals("")) {
       	 return R.error("手机号不能为空！");
		}
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
    	//校验验证码
		if (params.get("code")==null) {
	       	 return R.error("验证码不能为空！");
	    }
		
		//TODO
		
		//校验验证码的有效性
		Level2Cache level2 = CacheProviderHolder.getLevel2Cache(J2CacheUtils.INVALID_CACHE);
		Integer count = (Integer) level2.get("DOUBAO_SMS_COUNT:" + mobile);
		 String imageCode = (String) requset.getSession().getAttribute("imageCode");
		if(count !=null &&count>4){
			String  checkcode = params.get("imageCode");
			if(StringUtils.isNullOrEmpty(checkcode)){
			  	 return R.error("图形验证码不能为空！");
			}
		
			if (org.apache.commons.lang.StringUtils.isEmpty(imageCode)) {
				return R.error("图形验证码失效！");
			}

		}
	    if (!org.apache.commons.lang.StringUtils.isEmpty(imageCode)) {
			requset.removeAttribute("imageCode");
	   }		
		UserVo userVo = userService.queryByMobile(mobile);
       if (userVo== null) {
		 //新注册
    	   userVo = new UserVo();
           userVo.setUsername("");
           userVo.setRegisterTime(new Date());
           userVo.setRegisterIp((this.getClientIp()));
           userVo.setRegisterIp(userVo.getRegisterIp());
           userVo.setLastLoginTime(userVo.getRegisterTime());
           userVo.setMobile(mobile);
           userService.save(userVo);
    	   
	   }else {
		    userVo.setLastLoginIp(this.getClientIp());
            userVo.setLastLoginTime(new Date());
            userService.update(userVo);
	}
       J2CacheUtils.remove(J2CacheUtils.CHECK_CACHE, "DOUBAO:"+mobile);
      
        Map<String, Object> tokenMap = tokenService.createToken(userVo.getUserId());
        String token = MapUtils.getString(tokenMap, "token");
        if (null == userVo || StringUtils.isNullOrEmpty(token)) {
            return toResponsFail("登录失败");
        }
        request.setAttribute("LOGIN_USER_KEY", token);
        
        Map<String, Object> resultObj = new HashMap<String, Object>();

        resultObj.put("token", token);
        resultObj.put("userInfo", userVo);
        resultObj.put("userId", userVo.getUserId());
        return toResponsSuccess(resultObj);
    }
}
