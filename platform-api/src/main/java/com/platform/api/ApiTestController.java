package com.platform.api;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.entity.ThirdPartyRegionEntity;
import com.platform.entity.UserVo;
import com.platform.service.ApiUserService;
import com.platform.service.ThirdPartyRegionService;
import com.platform.utils.R;
import com.platform.youle.entity.ResponseBaseEntity;
import com.platform.youle.service.AbsApiGoodsService;
import com.platform.youle.service.AbsApiRegionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * API测试接口
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-03-23 15:47
 */
@Api(tags = "测试接口")
@RestController
@RequestMapping("/api/test")
public class ApiTestController {

    @Autowired
    private ApiUserService userService;
    @Autowired
    private ThirdPartyRegionService thirdPartyRegionService;
    
    @Autowired
    private AbsApiGoodsService absApiGoodsService;
    
  /*  @Autowired
    private AbsApiOrderService  orderService;*/
    @Autowired
    private AbsApiRegionService  absApiRegionService;
    
    /**
     * 获取用户信息
     */
    @PostMapping("userInfo")
    public R userInfo(@LoginUser UserVo user) {
        return R.ok().put("user", user);
    }

    /**
     * 忽略Token验证测试
     */
    @IgnoreAuth
    @PostMapping("notToken")
    public R notToken() {
        return R.ok().put("msg", "无需token也能访问。。。");
    }


    /**
     * 测试
     */
    @IgnoreAuth
    @PostMapping(value = "propter")
    public Object test() {
    	
        return R.ok().put("propter", "可以了");
    }
    
    /**
     * 根据手机号查询用户信息接口测试方法
     *
     * @param mobile
     * @return
     */
    @IgnoreAuth
    @PostMapping("userListByMobile")
    public R userList(String mobile) {
        UserVo userEntity = userService.queryByMobile(mobile);
        return R.ok().put("dto", userEntity);
    }
    

    @IgnoreAuth
    @ApiOperation(value = "获取所有商品ID")
    @PostMapping("getAllProductIds")
    public Object detail() {
    	
    	ResponseBaseEntity<?> response = absApiGoodsService.getAllProductIds();
		return response;
	}
    
/*    @IgnoreAuth
    @ApiOperation(value = "创建订单接口")
    @PostMapping("orderSubmit")
    public Object  orderSubmit(){
    	
    	RequestOrderSubmitEntity entity = new RequestOrderSubmitEntity();
    	
    	
    	ResponseOrderSubmitEntity response  = orderService.submit(entity);
    	
    	
    	return response;
    }
    */
    @IgnoreAuth
    @ApiOperation(value = "创建订单接口")
    @PostMapping("response")
    public Object response(){
    	// absApiRegionService.province(); // 省
    	 List<ThirdPartyRegionEntity> thirdPartyRegionList = thirdPartyRegionService.queryAllByType(3); //查询全部的省 province: 省1, city(市2), county(区/县3),  town(乡/镇4
    	 if(CollectionUtils.isNotEmpty(thirdPartyRegionList)){
    		 for(ThirdPartyRegionEntity thirdPartyRegionEntity : thirdPartyRegionList){
    			 //absApiRegionService.city(thirdPartyRegionEntity.getId());  //市
    			 //absApiRegionService.county(thirdPartyRegionEntity.getId()); //区、县
    			 //absApiRegionService.town(thirdPartyRegionEntity.getId());//镇
    		 }
    	 }
    	return 	null;
    }
    
    
    
    
}
