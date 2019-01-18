package com.platform.thirdrechard.controller;


import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.IgnoreAuth;
import com.platform.entity.UserVo;
import com.platform.service.ApiUserService;
import com.platform.thirdrechard.util.EncryptUtil;
import com.platform.utils.R;
import org.apache.commons.lang.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Api(tags = "用户业务 ")
@RestController
@RequestMapping("/api/thrid/user")
public class ThridUserController {
    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private ApiUserService apiUserService;

    @IgnoreAuth
    @ApiOperation(value="通过手机号校验用户",response = Map.class)
    @PostMapping("checkUser")
    public R checkUser(String encrypt ){
        try {
            logger.info("第三方传过来的数据是》》》》》》》》" + encrypt);
        if (StringUtils.isEmpty(encrypt)) {
            return R.error(1000,"解析密文失败");
        }
        String data = EncryptUtil.aesDecrypt(encrypt);
        logger.info("充值解密后的密文是》》》》》》》》" + data);
        JSONObject jsonObject = JSONObject.parseObject(data);
        String mobile=jsonObject.getString("mobile");
        logger.info("第三方传入的手机号=="+mobile);
        Map<String, Object> map =new HashMap<>();

            if(!checkMobile(mobile)){//校验手机格式
                logger.info("第三方传入的手机号错误，手机号为=="+mobile);
                return R.error(1001,"手机号错误");
            };
            Map paramMap=new HashMap();
            paramMap.put("mobile",mobile);
            UserVo user=apiUserService.thridQueryUserInfo(paramMap);
            if (null==user||user.equals("")){
                UserVo userVo=new UserVo();
                userVo.setMobile(mobile);
                userVo.setUsername("第三方用户");
                userVo.setRegisterTime(new Date());
                userVo =apiUserService.saveFromThrid(userVo);
                map.put("code","success");
                map.put("msg","成功");
                map.put("userId",userVo.getUserId());
                logger.info("成功创建第三方用户，手机号为=="+mobile+"用户id为"+userVo.getUserId());
                return R.ok(map);
            }else {
                map.put("code","success");
                map.put("msg","成功");
                map.put("userId",user.getUserId());
                logger.info("用户已存在成功返回用户，手机号为=="+mobile+"用户id为"+user.getUserId());
                return R.ok(map);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return R.error(1002,"密文错误");
    }

    /**
     * 校验手机号规则
     * @param mobile
     * @return
     */
    public boolean checkMobile(String mobile){
        if (StringUtils.isEmpty(mobile)) {
            return false;
        }
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (mobile.length() != 11) {
            return false;
        }
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(mobile);
        boolean isMatch = m.matches();
        if (!isMatch) {
            return false;
        }

        return true;

    }


}
