package com.platform.api;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.platform.annotation.IgnoreAuth;
import com.platform.service.ApiLogisticsService;
import com.platform.util.ApiBaseAction;
import com.platform.youle.entity.ResponseOrderTrackEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "物流管理")
@RestController
@RequestMapping("/api/logistics")
public class ApiLogisticsController extends ApiBaseAction {
   
	
	@Autowired
	private ApiLogisticsService apiLogisticsService;

	@IgnoreAuth
	@ApiOperation(value ="查询物流信息")
	@PostMapping("getLogistics")
	public Object getLogisticsByThirdOrder(String thirdOrder){
		if (StringUtils.isEmpty(thirdOrder)) {
			return toResponsFail("请传入第三方订单号thirdOrder");
		}
		
		  
		ResponseOrderTrackEntity response = apiLogisticsService.orderTrack(thirdOrder);
		
		return response;
	}
	
}
