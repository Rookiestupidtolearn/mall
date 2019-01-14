package com.platform.service.impl;

import com.platform.dao.ThridCompanyMapperDao;
import com.platform.service.ThridCompanyBackService;
import com.platform.thirdrechard.entity.ThridCompany;
import com.platform.thirdrechard.service.ThridCompanyService;
import com.platform.utils.R;
import com.platform.yeepay.utils.RSA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("thridCompanyBackService")
public class ThridCompanyBackServiceImpl implements ThridCompanyBackService {

    private Logger logger = LoggerFactory.getLogger(ThridCompanyBackServiceImpl.class);

    @Autowired
    ThridCompanyService thridCompanyService;

    @Autowired
    ThridCompanyMapperDao thridCompanyMapper;

    @Override
    public R genKeyPair(String appId) {
        ThridCompany thridCompany =
//                thridCompanyMapper.selectByAppId(appId);
//
                thridCompanyService.getThridCompanyByAppId(appId);
        if (null == thridCompany){
            logger.info("没有 appId 为 {} 的 商户 ",appId );
            return R.error("没有该用户");
        }
        Map<String, String> keyPair = new HashMap<>();
        try {
            keyPair = RSA.generateKeyPair();
        } catch (Exception e) {
            logger.error("用户{}生成秘钥失败,原因 ： ",appId,e );
            return R.error("生成秘钥失败");
        }
        String publicKey = keyPair.get("publicKey");
        String privateKey = keyPair.get("privateKey");
        thridCompany.setPrivateKey(privateKey);
        thridCompany.setPublicKey(publicKey);
        thridCompany.setUpdateDate(new Date());
        int res = thridCompanyMapper.updateByPrimaryKeySelective(thridCompany);
        if (res<1){
            return R.error("数据操作失败");
        }
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("publicKey", publicKey);
        reMap.put("privateKey", privateKey);
        return R.ok(reMap);
    }

    @Override
    public R updateCallBackUrl(String appId, String callBackUrl) {
        ThridCompany thridCompany = thridCompanyService.getThridCompanyByAppId(appId);
        if (null == thridCompany){
            logger.info("没有 appId 为 {} 的 商户 ",appId );
            return R.error("没有该用户");
        }
        thridCompany.setCallBackUrl(callBackUrl);
        thridCompany.setUpdateDate(new Date());
        int res = thridCompanyMapper.updateByPrimaryKeySelective(thridCompany);
        if (res<1){
            return R.error("appid:"+ appId + "数据操作失败");
        }
        return R.ok();
    }


    @Override
    public R getKeyPair(String appId) {
        ThridCompany thridCompany = thridCompanyService.getThridCompanyByAppId(appId);
        if (null == thridCompany){
            logger.info("没有 appId 为 {} 的 商户 ",appId );
            return R.error("没有该用户");
        }
        String publicKey = thridCompany.getPublicKey();
        String privateKey = thridCompany.getPublicKey();
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("publicKey", publicKey);
        reMap.put("privateKey", privateKey);
        return R.ok(reMap);
    }
}
