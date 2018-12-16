package com.platform.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.platform.annotation.IgnoreAuth;
import com.platform.util.ApiBaseAction;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "充值记录")
@RestController
@RequestMapping("/api/rechargeRecord")
public class ApiRechargeRecordController  extends ApiBaseAction  {

	
//	
//	@ApiOperation(value ="充值提交", response = Map.class)
//	@PostMapping("subbmitRecharge")
//	@IgnoreAuth
//	public Object   rechargeRecordSubbmit(){
//		
//		
//	}
	
}
