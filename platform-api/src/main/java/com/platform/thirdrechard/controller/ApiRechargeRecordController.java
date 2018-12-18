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
public class ApiRechargeRecordController  extends ApiBaseAction  {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private RechargeBizService rechargeBizService;
	
	@ApiOperation(value ="充值提交", response = Map.class)
	@PostMapping("subbmitRecharge")
	@IgnoreAuth
	public Object   rechargeRecordSubbmit(String encrypt){
		String a= "ihnAfyBK8SR1Lcfe04ekgJKWDTUuwwbGffxvPsNNHmt63zZJ/+4DnOvYcom5zGVGD4ujJ5BE0UafSgYpjrLkWZqFZyQQbR1KY+MWHgfqI7o=";
		RechargeResponseEntity responseEntity = new RechargeResponseEntity();
		 if (StringUtils.isEmpty(encrypt)) {
			 responseEntity.setCode("1000");
			 responseEntity.setMsg("解析密文失败");
			 return responseEntity;
		}
		logger.info("充值密文："+encrypt);
         try {
        	 responseEntity  =  rechargeBizService.recharge(a);
			
			logger.info("充值成功");
			return responseEntity;
		} catch (Exception e) {
		  logger.error("充值失败",e);
		  responseEntity.setCode("1000");
			 responseEntity.setMsg("解析密文失败");
			 return  responseEntity;
		}
		
	}
	
}
