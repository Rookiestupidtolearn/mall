package com.platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.platform.entity.GoodsPureInterestRateEntity;
import com.platform.service.GoodsPureInterestRateService;
import com.platform.utils.R;

/**
 * 商品毛利率
 *
 */
@RestController
@RequestMapping("goodsPureInterestRate")
public class GoodsPureInterestRateController {
	
	@Autowired
	private GoodsPureInterestRateService goodsPureInterestRateService;
	
	@RequestMapping("/all")
	public R queryAll(){
		goodsPureInterestRateService.save(new GoodsPureInterestRateEntity());
		return R.ok();
	}
	
}
