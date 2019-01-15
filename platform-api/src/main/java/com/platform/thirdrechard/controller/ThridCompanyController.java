package com.platform.thirdrechard.controller;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.platform.annotation.IgnoreAuth;
import com.platform.dao.ThirdMerchantRechargeRecordMapper;
import com.platform.service.ThridCompanyBackService;
import com.platform.thirdrechard.constant.PreRechargeConstants.ReturnResult;
import com.platform.thirdrechard.entity.ThridCompany;
import com.platform.thirdrechard.service.ThridCompanyService;
import com.platform.thirdrechard.utils.ReturnUtil;
import com.platform.utils.R;
@Api(tags = "三方商户")
@RestController
@RequestMapping("/thridCompany")
public class ThridCompanyController {

    @Autowired
    private ThridCompanyBackService thridCompanyBackService;

    @Autowired
    ThirdMerchantRechargeRecordMapper thirdMerchantRechargeRecordMapper;
    
    @Autowired
    private ThridCompanyService thirdCompanyService;


    @PostMapping("addMerchant")
    @ApiOperation(value = "添加商户")
    public Map<String, Object> addMerchant(@Valid @RequestBody ThridCompany thridCompany,HttpServletRequest request, HttpServletResponse response){
    	
    	String appid = thridCompany.getAppid();
    	if(StringUtils.isEmpty(appid)){
    		return ReturnUtil.returnAny(ReturnResult.FAIL, ReturnResult.FAIL_CODE, "appid不能为空！");
    	}
    	
    	if(appid.contains(" ") || appid.contains(" ") || appid.contains("	")){
    		return ReturnUtil.returnAny(ReturnResult.FAIL, ReturnResult.FAIL_CODE, "appid不能存在空格！");
    	}
    	
    	String name = thridCompany.getName();
    	
    	if(StringUtils.isBlank(name)){
    		return ReturnUtil.returnAny(ReturnResult.FAIL, ReturnResult.FAIL_CODE, "name不能为空，或者首尾存在空格");
    	}
    	
    	ThridCompany thridCompanyByAppId = thirdCompanyService.getThridCompanyByAppId(appid);
    	if(thridCompanyByAppId != null){
    		return ReturnUtil.returnAny(ReturnResult.FAIL, ReturnResult.FAIL_CODE, "此商户号已经存在");
    	}
    	
    	if(thirdCompanyService.add(thridCompany)){
    		return ReturnUtil.returnSuccess();
    	}
    	return ReturnUtil.returnAny(ReturnResult.FAIL, ReturnResult.FAIL_CODE, "添加失败");
    }
    
    @IgnoreAuth
    @ApiOperation(value = "生成秘钥")
    @PutMapping("/{appId}/keyPair")
    public R genKeyPair(@PathVariable(value = "appId")String appId){
        return thridCompanyBackService.genKeyPair(appId);
    }


    @IgnoreAuth
    @ApiOperation(value = "查询")
    @GetMapping("/keyPair")
    public R getKeyPair(@RequestParam String appId){
        return thridCompanyBackService.getKeyPair(appId);
    }

    @ApiOperation(value = "修改回调地址")
    @PutMapping("/{appId}/callBackUrl")
    public R updateCallBackUrl(@PathVariable(value = "appId")String appId,@RequestBody String callBackUrl ){

        return thridCompanyBackService.updateCallBackUrl(appId,callBackUrl);
    }



}
