package com.platform.thirdrechard.controller;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.platform.annotation.IgnoreAuth;
import com.platform.thirdrechard.entity.RechargeResponseEntity;
import com.platform.thirdrechard.service.RechargeBizService;
import com.platform.util.ApiBaseAction;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "充值记录")
@RestController
@RequestMapping("/api/rechargeRecord")
public class ApiRechargeRecordController extends ApiBaseAction {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RechargeBizService rechargeBizService;

    @ApiOperation(value = "充值提交", response = Map.class)
    @PostMapping("subbmitRecharge")
    @IgnoreAuth
    public Object rechargeRecordSubbmit(String encrypt) {
        RechargeResponseEntity responseEntity = new RechargeResponseEntity();

        if (StringUtils.isEmpty(encrypt)) {
            responseEntity.setCode("1000");
            responseEntity.setMsg("解析密文失败");
            logger.info(responseEntity.toString());
            return responseEntity;
        }
        logger.info("充值开始》》》充值密文：" + encrypt);
        try {
            responseEntity = rechargeBizService.recharge(encrypt);

            logger.info("充值处理完毕。。。。" + responseEntity.toString());
            return responseEntity;
        } catch (Exception e) {
            logger.error("充值失败", e);
            responseEntity.setCode("error");
            responseEntity.setMsg("充值失败");
            return responseEntity;
        }

    }

}
