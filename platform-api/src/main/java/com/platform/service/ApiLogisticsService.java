package com.platform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.youle.entity.ResponseOrderTrackEntity;
import com.platform.youle.service.AbsApiOrderService;

@Service
public class ApiLogisticsService {

	@Autowired
	private AbsApiOrderService absApiOrderService;
	
	
	public ResponseOrderTrackEntity orderTrack(String thirdOrder){
		ResponseOrderTrackEntity e = absApiOrderService.orderTrack(thirdOrder);
		
		return e;
	}
}
