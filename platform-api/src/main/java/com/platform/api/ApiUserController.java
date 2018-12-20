package com.platform.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.LoginUser;
import com.platform.dao.ApiOrderGoodsMapper;
import com.platform.dao.ApiOrderMapper;
import com.platform.entity.OrderGoodsVo;
import com.platform.entity.OrderVo;
import com.platform.entity.QzMoneyRecordEntity;
import com.platform.entity.QzUserAccountEntity;
import com.platform.entity.SmsLogVo;
import com.platform.entity.UserVo;
import com.platform.service.ApiSendSMSService;
import com.platform.service.ApiUserCouponService;
import com.platform.service.ApiUserService;
import com.platform.service.SysConfigService;
import com.platform.util.ApiBaseAction;
import com.platform.util.IdcardUtils;
import com.platform.utils.RequestUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 作者: @author Harmon <br>
 * 时间: 2017-08-11 08:32<br>
 * 描述: ApiIndexController <br>
 */
@Api(tags = "会员验证")
@RestController
@RequestMapping("/api/user")
public class ApiUserController extends ApiBaseAction {
    @Autowired
    private ApiUserService userService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private ApiUserCouponService apiUserCouponService;
    @Autowired
    private ApiOrderMapper apiOrderMapper;
    @Autowired
    private ApiOrderGoodsMapper  apiOrderGoodsMapper;
    @Autowired
    private ApiSendSMSService apiSendSMSService;

    /**
     * 发送短信
     */
    @ApiOperation(value = "发送短信")
    @PostMapping("smscode")
    public Object smscode(HttpServletRequest request,@LoginUser UserVo loginUser,String code) {
        JSONObject jsonParams = getJsonRequest();
        String phone = jsonParams.getString("phone");
        // 一分钟之内不能重复发送短信
        SmsLogVo smsLogVo = userService.querySmsCodeByUserId(loginUser.getUserId());
        if (smsLogVo != null) {
			 if (smsLogVo.getLog_date() != null) {
			        if (null != smsLogVo && (System.currentTimeMillis() / 1000 - smsLogVo.getLog_date()) < 1 * 60) {
			            return toResponsFail("一分钟内重复发送短信");
			        }
			}
		}
        //ip地址
     	String validIP = RequestUtil.getIpAddrByRequest(request);
        
        Map paramMap = new HashMap();
        paramMap.put("mobile", phone);
        //校验该手机号是否已被绑定
        List<UserVo> userLi = userService.queryUserInfo(paramMap);
        if(CollectionUtils.isNotEmpty(userLi)){
        	return toResponsFail("该手机号已被绑定");
        }
         
      return  apiSendSMSService.sendSms(phone, validIP, "2",Integer.parseInt(loginUser.getUserId().toString()));
        
    }

    /**
     * 获取当前会员等级
     *
     * @param loginUser
     * @return
     */
    @ApiOperation(value = "获取当前会员等级")
    @PostMapping("getUserLevel")
    public Object getUserLevel(@LoginUser UserVo loginUser) {
        String userLevel = userService.getUserLevel(loginUser);
        return toResponsSuccess(userLevel);
    }

    /**
     * 绑定手机
     */
    @ApiOperation(value = "绑定手机")
    @PostMapping("bindMobile")
    public Object bindMobile(@LoginUser UserVo loginUser) {
        JSONObject jsonParams = getJsonRequest();
        SmsLogVo smsLogVo = userService.querySmsCodeByUserId(loginUser.getUserId());

        String mobile_code = jsonParams.getString("mobile_code");
        String mobile = jsonParams.getString("mobile");
        
        
        Map paramMap = new HashMap();
        paramMap.put("mobile", mobile);
        //校验该手机号是否已被绑定
        List<UserVo> userLi = userService.queryUserInfo(paramMap);
        if(CollectionUtils.isNotEmpty(userLi)){
        	return toResponsFail("该手机号已被绑定");
        }
        if (!mobile_code.equals(smsLogVo.getSmsCode())) {
            return toResponsFail("验证码错误");
        }
        UserVo userVo = userService.queryObject(loginUser.getUserId());
        userVo.setMobile(mobile);
        userService.update(userVo);
        return toResponsSuccess("手机绑定成功");
    }

    
    /**
     * 查询用户账户
     */
    @ApiOperation(value = "查询用户账户")
    @PostMapping("userAccount")
    public Object userAccount(@LoginUser UserVo loginUser) {
    	Map<String, Object> obj = new HashMap<String, Object>();
       try{
    	   QzUserAccountEntity qzUserAccount = userService.queryUserAccount(loginUser.getUserId().intValue());
    	   obj.put("code", 1);
    	   JSONObject unPayment = queryUnPayments(loginUser);
           String unPaymentNum = unPayment.getString("num");
           JSONObject cancelOrder = queryCancelFlag(loginUser);
           String cancelOrderNum = cancelOrder.getString("num");
           JSONObject delivered = queryDelivered(loginUser);
           String deliveredNum = delivered.getString("num");
           JSONObject successOrder = querySuccessOrder(loginUser);
           String successOrderNum = successOrder.getString("num");
           obj.put("unPaymentNum", unPaymentNum);//待付款个数
           obj.put("cancelOrderNum", cancelOrderNum);//取消订单订单
           obj.put("deliveredNum", deliveredNum);//待收货个数
           obj.put("successOrderNum", successOrderNum);//订单成功个数
    	   if(qzUserAccount == null){
        	   //未查询到用户账户
               obj.put("data", "0.00");
               return obj;
           }
          
           obj.put("data", qzUserAccount.getAmount().toString());
           return obj;
       }catch(Exception e){
    	   e.printStackTrace();
       }
       obj.put("code", 0);
       obj.put("data","查询出错");
       return null;
    }
    
    /**
     * 查询用户资金流水
     */
    @ApiOperation(value = "查询用户资金流水")
    @PostMapping("userAccountDetail")
    public Object userAccountDetail(@LoginUser UserVo loginUser) {
    	Map<String, Object> obj = new HashMap<String, Object>();
        try{
            List<QzMoneyRecordEntity> qzMoneyRecordEntity = userService.queryuserAccountDetail(loginUser.getUserId().intValue());
            if(CollectionUtils.isNotEmpty(qzMoneyRecordEntity)){
            	obj.put("code", 1);
                obj.put("data", qzMoneyRecordEntity);
                return obj;
            }
            obj.put("code", 1);
            obj.put("data", "");
            return obj;
        }catch(Exception e){
        	e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 提供登录的用户信息,供小程序调用使用
     */
    @ApiOperation(value = "获取登录用户信息")
    @PostMapping("userInfo")
    public Object userInfo(@LoginUser UserVo loginUser) {
    	Map<String, Object> obj = new HashMap<String, Object>();
    	if(loginUser == null){
    		obj.put("data","查询用户信息异常");
    		return obj;
    	}else{
    		UserVo userInfo = loginUser;
    		userInfo.setPassword("");
    		UserVo vo = userService.queryObject(loginUser.getUserId());
    		userInfo.setIdcard(vo.getIdcard());
    		obj.put("data",userInfo);
    		return obj;
    	}
    }
    
    @ApiOperation(value = "绑定用户的身份证信息")
    @PostMapping("bind_user_idcard")
    public Object bindUserIdcard(@LoginUser UserVo loginUser) {
    	JSONObject jsonParams = getJsonRequest();
    	Map<String, Object> obj = new HashMap<String, Object>();
    	if(loginUser == null){
    		obj.put("data","查询用户信息异常");
    		obj.put("code","500");
    		return obj;
    	}else{
    		//校验姓名
    		  String username = jsonParams.getString("username");
    	      String idcard = jsonParams.getString("idcard");
    		if (org.apache.commons.lang.StringUtils.isEmpty(username)) {
    			obj.put("data","姓名不能为空");
    			obj.put("code","500");
        		return obj;
			}
    		//校验身份证号
    		if (org.apache.commons.lang.StringUtils.isEmpty(idcard)) {
    			obj.put("data","身份证号不能为空");
    			obj.put("code","500");
        		return obj;
			}
    		if (!IdcardUtils.cardCodeVerify(idcard)) {
    			obj.put("data","身份证号不正确");
    			obj.put("code","500");
        		return obj;
			}
    		
    		UserVo vo = userService.queryObject(loginUser.getUserId());
    		vo.setIdcard(idcard);
    		vo.setUsername(username);
    		userService.update(vo);
    		
    		obj.put("code", 200);
    		obj.put("msg", "操作成功");
    		return obj;
    	}
    }
   
    /**
     * 查询代付款
     * @param loginUser
     * @return
     */
    public JSONObject queryUnPayments(@LoginUser UserVo loginUser){
    	JSONObject obj = new JSONObject();
    	Integer num = 0;
    	if(loginUser == null){
    		obj.put("data","查询用户信息异常");
    		obj.put("code","500");
    		return obj;
    	}
    	Long userId = loginUser.getUserId();
    	List<OrderVo> orders = apiOrderMapper.queryOrders(userId);
    	
    	if(!CollectionUtils.isEmpty(orders)){
    		num = orders.size();
    	}
    	obj.put("num", num);
    	obj.put("data", "查询订单个数成功");
    	obj.put("code","200");
    	return obj;
    }
    
    /**
     * 查询订单取消个数
     * @param loginUser
     * @return
     */
    public JSONObject queryCancelFlag(@LoginUser UserVo loginUser){
    	JSONObject obj = new JSONObject();
    	Integer num = 0;
    	if(loginUser == null){
    		obj.put("data","查询用户信息异常");
    		obj.put("code","500");
    		return obj;
    	}
    	Long userId = loginUser.getUserId();
    	List<OrderVo> orders = apiOrderMapper.queryCancelFlag(userId);
    	if(!CollectionUtils.isEmpty(orders)){
    		num = orders.size();
    	}
    	obj.put("num", num);
    	obj.put("data", "查询待收货个数成功");
    	obj.put("code","200");
    	return obj;
    }
    
    /**
     * 查询代收货
     * @param loginUser
     * @return
     */
    public JSONObject queryDelivered(@LoginUser UserVo loginUser){
    	JSONObject obj = new JSONObject();
    	Integer num = 0;
    	if(loginUser == null){
    		obj.put("data","查询用户信息异常");
    		obj.put("code","500");
    		return obj;
    	}
    	Long userId = loginUser.getUserId();
    	List<OrderVo> orders = apiOrderMapper.queryDelivered(userId);
    	if(!CollectionUtils.isEmpty(orders)){
    		for(OrderVo order : orders){
    			Integer orderId = order.getId();
    			List<OrderGoodsVo> orderGoods = apiOrderGoodsMapper.queryOrderDeliveredGoods(orderId);
    			if(!CollectionUtils.isEmpty(orderGoods)){
    				num += orderGoods.size();
    			}
    		}
    	}
    	obj.put("num", num);
    	obj.put("data", "查询待收货个数成功");
    	obj.put("code","200");
    	return obj;
    }
    
    /**
     * 查询已完成
     * @param loginUser
     * @return
     */
    public JSONObject querySuccessOrder(@LoginUser UserVo loginUser){
    	JSONObject obj = new JSONObject();
    	Integer num = 0;
    	if(loginUser == null){
    		obj.put("data","查询用户信息异常");
    		obj.put("code","500");
    		return obj;
    	}
    	Long userId = loginUser.getUserId();
    	List<OrderVo> orders = apiOrderMapper.querySuccessOrder(userId);
    	if(!CollectionUtils.isEmpty(orders)){
    		num = orders.size();
    	}
    	obj.put("num", num);
    	obj.put("data", "查询已完成个数成功");
    	obj.put("code","200");
    	return obj;
    }
    
    
}