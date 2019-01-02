package com.platform.thirdrechard.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.util.StringUtil;
import com.platform.annotation.IgnoreAuth;
import com.platform.entity.QzMoneyRecordEntity;
import com.platform.entity.QzRechargeRecordEntity;
import com.platform.entity.QzUserAccountVo;
import com.platform.nideshopuser.entity.NideshopUser;
import com.platform.nideshopuser.service.NideshopuserService;
import com.platform.service.QzMoneyRecordApiService;
import com.platform.service.QzRechargeRecordApiService;
import com.platform.service.QzUserAccountApiService;
import com.platform.thirdrechard.constant.PreRechargeConstants.RechargeStatus;
import com.platform.thirdrechard.constant.PreRechargeConstants.ReturnResult;
import com.platform.thirdrechard.entity.RequestPreRechargeEntity;
import com.platform.thirdrechard.entity.RequestRechargeEntity;
import com.platform.thirdrechard.entity.ThirdMerchantRechargeRecord;
import com.platform.thirdrechard.entity.ThirdMerchantRechargeRecordExample;
import com.platform.thirdrechard.entity.ThirdMerchantRechargeRecordExample.Criteria;
import com.platform.thirdrechard.entity.ThirdPreCompanyRechargeRecord;
import com.platform.thirdrechard.entity.ThridCompany;
import com.platform.thirdrechard.service.PreRechargeRecordService;
import com.platform.thirdrechard.service.ThirdMerchantRechargeRecordService;
import com.platform.thirdrechard.service.ThridCompanyService;
import com.platform.thirdrechard.util.FieldValidation;
import com.platform.thirdrechard.utils.GenerateOrderNoUtil;
import com.platform.thirdrechard.utils.IpUtil;
import com.platform.thirdrechard.utils.JedisUtil;
import com.platform.thirdrechard.utils.MsgDigestUtils;
import com.platform.thirdrechard.utils.RedisUtils;
import com.platform.thirdrechard.utils.ReturnUtil;
import com.platform.yeepay.service.YeepayOrderBizService;
import com.platform.yeepay.utils.HttpClient4Utils;
import com.platform.yeepay.utils.PaymobileUtils;



/**
 * @author 支付controller
 *
 */
@Api(tags = "商户充值")
@Controller
@RequestMapping("/recharge")
public class PreRechargeController {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private PreRechargeRecordService preRechargeRecordService;
	@Autowired
	private ThridCompanyService thridCompanyService;
	@Autowired
	private NideshopuserService nideshopuserService;

	@Autowired
    private YeepayOrderBizService yeepayOrderBizService;
	
	@Autowired
	private ThirdMerchantRechargeRecordService thirdMerchantRechargeRecordService;

	@Autowired
	private QzMoneyRecordApiService qzMoneyRecordApiService;

	@Autowired
	private QzRechargeRecordApiService qzRechargeRecordApiService;

	@Autowired
	private QzUserAccountApiService qzUserAccountApiService;


	/**
	 *生成PRE签名
	 * @param
	 * @param request
	 * @param response
	 * @return
	 */
	@ApiOperation(value = "生成PRE签名")
	@IgnoreAuth
	@PostMapping (value = "preSign")
	@ResponseBody
	public Map<String, Object> preSign(@Valid @RequestBody RequestPreRechargeEntity reRechargeRecord,
									   HttpServletRequest request, HttpServletResponse response) {

		logger.info("【签名】入参:" + reRechargeRecord.toString());

		Map<String, Object> preFieldValidation = FieldValidation.preFieldValidation(reRechargeRecord, "1");
		if(ReturnResult.FAIL_CODE.equals(preFieldValidation.get("code"))){
			return preFieldValidation;
		}
		
		ThridCompany thridCompany = thridCompanyService.getThridCompanyByAppId(reRechargeRecord.getAppId());

		if (null == thridCompany) {
			return ReturnUtil.returnFail("商户无权限");
		}

		if (StringUtil.isEmpty(thridCompany.getPublicKey())) {
			return ReturnUtil.returnFail("商户信息未配置");
		}
		
		Map<String, Object> successMap = ReturnUtil.returnSuccess();
		successMap.put("sign", MsgDigestUtils.sign(reRechargeRecord.regSignVal(),thridCompany.getPrivateKey()));
		return successMap;
	}

	/**
	 *生成re签名
	 * @param
	 * @param request
	 * @param response
	 * @return
	 */
	@ApiOperation(value = "生成re签名")
	@IgnoreAuth
	@PostMapping (value = "reSign")
	@ResponseBody
	public Map<String, Object> reSign(@Valid @RequestBody RequestRechargeEntity requestRecharge, HttpServletRequest request,
									  HttpServletResponse response) {

		logger.info("【支付签名】入参:" + requestRecharge.toString());

		Map<String, Object> reFieldValidation = FieldValidation.reFieldValidation(requestRecharge, "1");
		if(ReturnResult.FAIL_CODE.equals(reFieldValidation.get("code"))){
			return reFieldValidation;
		}
		
		ThridCompany thridCompany = thridCompanyService.getThridCompanyByAppId(requestRecharge.getAppId());

		if (null == thridCompany) {
			return ReturnUtil.returnFail("商户无权限");
		}

		if (StringUtil.isEmpty(thridCompany.getPublicKey())) {
			return ReturnUtil.returnFail("商户信息未配置");
		}

		Map<String, Object> successMap = ReturnUtil.returnSuccess();
		successMap.put("sign", MsgDigestUtils.sign(requestRecharge.regSignVal(),thridCompany.getPrivateKey()));
		return successMap;
	}

	/**
	 *预支付
	 * @param reRechargeRecord
	 * @param request
	 * @param response
	 * @return
	 */
	@ApiOperation(value = "预支付")
    @IgnoreAuth
	@PostMapping (value = "pre_recharge")
	@ResponseBody
	public Map<String, Object> preRecharge(@Valid @RequestBody RequestPreRechargeEntity reRechargeRecord,
			HttpServletRequest request, HttpServletResponse response) {

		logger.info("【预充值】入参:" + reRechargeRecord.toString());

		Map<String, Object> preFieldValidation = FieldValidation.preFieldValidation(reRechargeRecord, "2");
		if(ReturnResult.FAIL_CODE.equals(preFieldValidation.get("code"))){
			return preFieldValidation;
		}
		
		ThridCompany thridCompany = thridCompanyService.getThridCompanyByAppId(reRechargeRecord.getAppId());

		if (null == thridCompany) {
			return ReturnUtil.returnFail("商户无权限");
		}

		if (StringUtil.isEmpty(thridCompany.getPublicKey())) {
			return ReturnUtil.returnFail("商户信息未配置");
		}

        //reRechargeRecord.setSignature(MsgDigestUtils.sign(reRechargeRecord.regSignVal(),thridCompany.getPrivateKey()));

		if (!MsgDigestUtils.verifySign(reRechargeRecord.regSignVal(), reRechargeRecord.getSignature(),
				thridCompany.getPublicKey())) {
			return ReturnUtil.returnFail("验签失败");
		}

		//校验金额，必须为正整数
        try {
           int money = reRechargeRecord.getAmount().intValue();
           if (money <= 0 ) {
               return ReturnUtil.returnFail("金额必须为正整数");
           }
        } catch (Exception e) {
            return ReturnUtil.returnFail("金额必须为正整数");
        }

		Long requestId = System.currentTimeMillis();

		Jedis jedis = JedisUtil.getInstance().getJedis();

		String key = reRechargeRecord.getAppId() + "-" + reRechargeRecord.getOrderNo();
		Boolean locked = RedisUtils.tryGetDistributedLock(jedis,key,Long.toString(requestId), 1000 * 120);

		if (!locked) {
			return ReturnUtil.returnFail("当前订单正在处理中");
		}

		//查询历史是否有支付单子
		ThirdPreCompanyRechargeRecord oldThirdPreCompanyRechargeRecord = preRechargeRecordService.getPreRechargeRecordByThirdOrder(reRechargeRecord.getOrderNo(), reRechargeRecord.getAppId());
		if (null != oldThirdPreCompanyRechargeRecord) {
			if ("0".equals(oldThirdPreCompanyRechargeRecord.getStatus()) || "1".equals(oldThirdPreCompanyRechargeRecord.getStatus())) {
				return ReturnUtil.returnFail("当前订单正在处理中");
			} else if ("2".equals(oldThirdPreCompanyRechargeRecord.getStatus())) {
				return ReturnUtil.returnFail("订单支付成功,请勿重复支付");
			} else {
				return ReturnUtil.returnFail("订单支付失败,请重新支付");
			}
		}

		try {
			String phone = reRechargeRecord.getPhone();
			NideshopUser nideshopUser = nideshopuserService.selectByMobile(phone);
			//不存在创建用户
			if (null == nideshopUser) {
				nideshopUser = new NideshopUser();
                nideshopUser.setUsername("");
				nideshopUser.setMobile(reRechargeRecord.getPhone());
				nideshopUser.setRegisterTime(new Date());
				Boolean isSave = nideshopuserService.saveNideshopUser(nideshopUser);
				if (!isSave) {
					RedisUtils.releaseDistributedLock(jedis, key, Long.toString(requestId));
                    return ReturnUtil.returnFail("开户失败");
				}
			}

			// 创建订单号
			String orderNo = GenerateOrderNoUtil.gen(reRechargeRecord.getChannelNo(),reRechargeRecord.getAppId());
			
			ThirdPreCompanyRechargeRecord thirdPreCompanyRechargeRecord = new ThirdPreCompanyRechargeRecord();
			thirdPreCompanyRechargeRecord.setAppId(reRechargeRecord.getAppId());
			thirdPreCompanyRechargeRecord.setOrderNo(orderNo);
			thirdPreCompanyRechargeRecord.setOrderThird(reRechargeRecord.getOrderNo());
			thirdPreCompanyRechargeRecord.setAmount(reRechargeRecord.getAmount());
			thirdPreCompanyRechargeRecord.setPhone(reRechargeRecord.getPhone());
			thirdPreCompanyRechargeRecord.setChannelPay("yeepay");
			thirdPreCompanyRechargeRecord.setChannelThird(reRechargeRecord.getChannelNo());
			thirdPreCompanyRechargeRecord.setStatus(RechargeStatus.INIT);
			thirdPreCompanyRechargeRecord.setCreateTime(new Date());
            thirdPreCompanyRechargeRecord.setUpdateTime(new Date());

			if (preRechargeRecordService.savePreRechargeRecord(thirdPreCompanyRechargeRecord)) {
				RedisUtils.releaseDistributedLock(jedis, key, Long.toString(requestId));
                return ReturnUtil.returnSuccess();
			} else {
				RedisUtils.releaseDistributedLock(jedis, key, Long.toString(requestId));
                return ReturnUtil.returnFail("充值失败");
			}

		} catch (Exception e) {
			logger.error("【预充值】异常", e);
            RedisUtils.releaseDistributedLock(jedis, key, Long.toString(requestId));
            return ReturnUtil.returnFail("未知异常");
		}

	}

	/**
	 * 支付
	 * @param requestRecharge
	 * @param request
	 * @param response
	 * @return
	 */
	@ApiOperation(value = "支付")
    @IgnoreAuth
	@PostMapping(value = "recharge")
	@ResponseBody
	public Map<String, Object> recharge(@Valid @RequestBody RequestRechargeEntity requestRecharge, HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("充值】入参:" + requestRecharge.toString());
		
		Map<String, Object> reFieldValidation = FieldValidation.reFieldValidation(requestRecharge, "2");
		if(ReturnResult.FAIL_CODE.equals(reFieldValidation.get("code"))){
			return reFieldValidation;
		}
		
		ThridCompany thridCompany = thridCompanyService.getThridCompanyByAppId(requestRecharge.getAppId());

		if (null == thridCompany) {
			return ReturnUtil.returnFail("商户无权限");
		}

		if (StringUtil.isEmpty(thridCompany.getPublicKey())) {
			return ReturnUtil.returnFail("商户信息未配置");
		}

        //requestRecharge.setSignature(MsgDigestUtils.sign(requestRecharge.regSignVal(),thridCompany.getPrivateKey()));

		if (!MsgDigestUtils.verifySign(requestRecharge.regSignVal(), requestRecharge.getSignature(),thridCompany.getPublicKey())) {
			return ReturnUtil.returnFail("验签失败");
		}

        //查询历史是否有预支付单子
        ThirdPreCompanyRechargeRecord oldThirdPreCompanyRechargeRecord = preRechargeRecordService.getPreRechargeRecordByThirdOrder(requestRecharge.getOrderNo(), requestRecharge.getAppId());
        //状态0或3的单子 可以支付
        if (null != oldThirdPreCompanyRechargeRecord) {
            if ( "1".equals(oldThirdPreCompanyRechargeRecord.getStatus())) {
                return ReturnUtil.returnFail("当前订单正在处理中,请勿重复操作");
            } else if ("2".equals(oldThirdPreCompanyRechargeRecord.getStatus())) {
                return ReturnUtil.returnFail("订单已经支付成功,请勿重复支付");
            }
        } else {
            return ReturnUtil.returnFail("订单不存在,请先调用预支付接口");
        }

		Long requestId = System.currentTimeMillis();

		Jedis jedis = JedisUtil.getInstance().getJedis();

		//锁key
		String key = requestRecharge.getAppId() + "-"  + requestRecharge.getOrderNo() +"-pay";
		Boolean locked = RedisUtils.tryGetDistributedLock(jedis, key,Long.toString(requestId), 1000 * 60);

		if (!locked) {
			return ReturnUtil.returnFail("当前订单正在处理中");
		}

        String phone = oldThirdPreCompanyRechargeRecord.getPhone();
        NideshopUser nideshopUser = nideshopuserService.selectByMobile(phone);

        //符合要求，去调用真正的支付渠道生成订单
        Map<String, Object> stringObjectMap = yeepayOrderBizService.yeepayRechargeSubmmit(nideshopUser, oldThirdPreCompanyRechargeRecord, IpUtil.getIpAddr(request));
        Map<String, Object> successMap = ReturnUtil.returnSuccess();
        if (null != stringObjectMap) {
            successMap.put("payUrl", stringObjectMap.get("payurl"));
        } else {
            return ReturnUtil.returnFail("订单支付异常,请重新提交");
        }

        //修改三方支付表
        oldThirdPreCompanyRechargeRecord.setUpdateTime(new Date());
        if ("3".equals(oldThirdPreCompanyRechargeRecord.getStatus())) {
            oldThirdPreCompanyRechargeRecord.setRemark("历史支付失败订单，重新支付");
        }
        oldThirdPreCompanyRechargeRecord.setStatus("1");
        if (preRechargeRecordService.updateByPrimaryKeySelective(oldThirdPreCompanyRechargeRecord)) {
        	return successMap;			
		} else {
			logger.error("支付时，修改商户订单失败，商户号:"+oldThirdPreCompanyRechargeRecord.getAppId()+",系统订单号:"+oldThirdPreCompanyRechargeRecord.getOrderNo()+",商户订单号:"+oldThirdPreCompanyRechargeRecord.getOrderThird());
			return ReturnUtil.returnFail("订单支付异常,请重新提交");
		}
	}

	/**
	 *
	 * @Title: queryOrder
	 * @Description: 订单查询
	 * @param requestRecharge
	 * @param request
	 * @param response
	 * @return   Map<String,Object>
	 *
	 * @throws
	 */
	@ApiOperation(value = "商户订单查询")
    @IgnoreAuth
	@PostMapping(value = "queryOrder")
	@ResponseBody
	public Map<String, Object> queryOrder(@Valid @RequestBody RequestRechargeEntity requestRecharge, HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("订单查询入参:" + requestRecharge.toString());
		
		Map<String, Object> reFieldValidation = FieldValidation.reFieldValidation(requestRecharge, "2");
		if(ReturnResult.FAIL_CODE.equals(reFieldValidation.get("code"))){
			return reFieldValidation;
		}
		
		ThridCompany thridCompany = thridCompanyService.getThridCompanyByAppId(requestRecharge.getAppId());

		
		
		if (null == thridCompany) {
			return ReturnUtil.returnFail("商户无权限");
		}

		if (StringUtil.isEmpty(thridCompany.getPublicKey())) {
			return ReturnUtil.returnFail("商户信息未配置");
		}

		if (!MsgDigestUtils.verifySign(requestRecharge.regSignVal(), requestRecharge.getSignature(),thridCompany.getPublicKey())) {
			return ReturnUtil.returnFail("验签失败");
		}

		ThirdPreCompanyRechargeRecord preRechargeRecordByThirdOrder = preRechargeRecordService.getPreRechargeRecordByThirdOrder(requestRecharge.getOrderNo(), requestRecharge.getAppId());
		
		if (null == preRechargeRecordByThirdOrder) {
			return ReturnUtil.returnFail("订单不存在");
		}else {			
			Map<String, Object> successMap = ReturnUtil.returnSuccess();
			successMap.put("state", preRechargeRecordByThirdOrder.getStatus());
			return successMap;
		}	
	}
	
	//回调通知易宝
    @IgnoreAuth
	@RequestMapping(value = "yeePayRecharge",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String yeePayRecharge(String data, String encryptkey){


		Map<String, Object> yee = yeePayRechargeOperation(data, encryptkey);
		if(ReturnResult.SUCCESS.equals(String.valueOf(yee.get("msg")))){
			return "SUCCESS";
		}
		return "ERROR";
	}
	
	
	private Map<String, Object> yeePayRechargeOperation(String data,String encryptkey){

		logger.info("易宝支付订单支付回调start");
		String appId = "";
		try {
			if(StringUtils.isEmpty(data) || StringUtils.isEmpty(encryptkey)){
				logger.info("易宝回调参数有误！！！");
				Map<String, Object> returnAny  =  ReturnUtil.returnAny(ReturnResult.FAIL, "10001", "易宝回调参数有误！");
				return returnAny;
			}
			
			// 解密data
			TreeMap<String, String> dataMap = PaymobileUtils.decrypt(data, encryptkey);
			logger.info("易宝支付订单回调，返回的明文参数：" + dataMap);
			
			//商户订单号
			String orderid = dataMap.get("orderid");
			logger.info("易宝支付订单，当前订单号："+ orderid);
			ThirdPreCompanyRechargeRecord preRecord = preRechargeRecordService.getPreRechargeRecordByOrderNo(orderid);
			appId = preRecord.getAppId();
			
			// sign验签
			if (!PaymobileUtils.checkSign(dataMap)) {
				logger.error("易宝支付订单回调 ，sign 验签失败！");
				Map<String, Object> returnAny  =  ReturnUtil.returnAny(ReturnResult.FAIL, "10002", "sign 验签失败！");
				//callbackMerchants(dataMap, returnAny,appId);
				return returnAny;
			}
			
			if(null != preRecord){
				if ("2".equals(preRecord.getStatus())) {
					// 无需再次回调
					logger.info("本地订单已经处理成功，无需再处理，商户订单号：" + orderid );
					Map<String, Object> returnAny = ReturnUtil.returnAny(ReturnResult.SUCCESS, "9999", "已经支付成功");
					return returnAny;
				}
				
				//状态是否成功
				if("1".equals(dataMap.get("status"))){
					preRecord.setStatus(RechargeStatus.SUCCESS);
				}else{
					preRecord.setStatus(RechargeStatus.FAIL);
					preRecord.setRemark("充值失败");
					preRecord.setUpdateTime(new Date());
					preRechargeRecordService.updateByPrimaryKeySelective(preRecord);
					Map<String, Object> returnAny = ReturnUtil.returnAny(ReturnResult.FAIL, "10003", "充值失败");
					callbackMerchants(dataMap, returnAny, appId, preRecord);
					return returnAny;
				}
			
				//实际支付与订单金额是否一致
				BigDecimal pay = new BigDecimal(dataMap.get("amount")).divide(new BigDecimal(100));
				if(pay.compareTo(preRecord.getAmount()) != 0){
					logger.info("实际支付给易宝金额跟实际订单金额不一致，商户订单号：" + orderid );
					preRecord.setRemark("实际支付给易宝金额跟实际订单金额不一致");
				}else{
					preRecord.setRemark("支付成功");
				}
				preRecord.setUpdateTime(new Date());
				preRechargeRecordService.updateByPrimaryKeySelective(preRecord);
				shopUserHandle(preRecord);
				shopUserRechargeRecord(preRecord);
				shopUserAccount(preRecord);
			}
			
			logger.info("易宝支付订单支付回调成功，商户订单号：" + orderid );
			Map<String, Object> returnAny = ReturnUtil.returnAny(ReturnResult.SUCCESS, "10000", "支付成功");
			callbackMerchants(dataMap, returnAny, appId, preRecord);
			return returnAny;
		} catch (Exception e) {
			logger.error("易宝支付订单支付回调失败，",e);
			Map<String, Object> returnAny = ReturnUtil.returnAny(ReturnResult.FAIL, "10004", "订单支付回调失败");
			//callbackMerchants(new TreeMap<String, String>(), returnAny, appId);
			return returnAny;
		}
	}
	
	/**
	 * 回调通知商户
	 * @throws Exception 
	 */
	private void callbackMerchants(TreeMap<String, String> dataMap,Map<String, Object> returnAny,String appId, ThirdPreCompanyRechargeRecord preRecord) {
		String orderNo = dataMap.get("orderid");
		try {
			if(StringUtils.isEmpty(appId)){
				logger.info("appId为空");
			}
			
			if(dataMap.containsKey("sign")){
				dataMap.remove("sign");
			}
			
			Set<Entry<String, Object>> entrySet = returnAny.entrySet();
			for (Entry<String, Object> entry : entrySet) {
				dataMap.put(entry.getKey(), String.valueOf(entry.getValue()));
			}
			
			ThridCompany thridCompanyByAppId = thridCompanyService.getThridCompanyByAppId(appId);
			String callBackUrl = thridCompanyByAppId.getCallBackUrl(); //回调地址
			String publicKey = thridCompanyByAppId.getPublicKey();
			//String encrypt = RSA.encrypt(JSON.toJSONString(dataMap), publicKey);
			
			Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("appId", appId);
            paramMap.put("orderNo", preRecord.getOrderThird());
            paramMap.put("amount", String.valueOf(preRecord.getAmount()) );
            paramMap.put("signature", MsgDigestUtils.sign(appId + "|" + preRecord.getOrderThird(), thridCompanyByAppId.getPrivateKey()));
            paramMap.put("state", "10000".equals(returnAny.get("code"))?"2":"3");

			ThirdMerchantRechargeRecord record = new ThirdMerchantRechargeRecord();
			record.setAppId(appId);
			record.setData(JSON.toJSONString(paramMap));
			record.setMark("初始化");
			record.setOrderNo(orderNo);
			record.setSendNum(0);
			record.setStatus("0");//初始化状态
			record.setUpdateTime(new Date());
			record.setCreateTime(new Date());
			thirdMerchantRechargeRecordService.saveThirdMerchantRechargeRecord(record);
			String sendHttpRequest = HttpClient4Utils.sendHttpRequest(callBackUrl, paramMap, "UTF-8", true);
			logger.info("商户号："+appId+"，订单号："+orderNo+"回调结果："+sendHttpRequest);
			
			
			ThirdMerchantRechargeRecordExample example = new ThirdMerchantRechargeRecordExample();
			Criteria createCriteria = example.createCriteria();
			createCriteria.andOrderNoEqualTo(preRecord.getOrderNo());
			if("SUCCESS".equals(sendHttpRequest)){
				record = new ThirdMerchantRechargeRecord();
				record.setMark("回调成功");
				record.setSendNum(1);
				record.setStatus("1");
				record.setUpdateTime(new Date());
				thirdMerchantRechargeRecordService.updateThirdMerchantRechargeRecord(record, example);
			}else if("ERROR".equals(sendHttpRequest)){
				record = new ThirdMerchantRechargeRecord();
				record.setMark("回调失败");
				record.setSendNum(1);
				record.setStatus("2");
				record.setUpdateTime(new Date());
				thirdMerchantRechargeRecordService.updateThirdMerchantRechargeRecord(record, example);
			}else if("failed".equals(sendHttpRequest)){
				record = new ThirdMerchantRechargeRecord();
				record.setMark("接口异常");
				record.setSendNum(1);
				record.setStatus("3");
				record.setUpdateTime(new Date());
				thirdMerchantRechargeRecordService.updateThirdMerchantRechargeRecord(record, example);
			}else{
				record = new ThirdMerchantRechargeRecord();
				record.setMark("接口异常");
				record.setSendNum(1);
				record.setStatus("3");
				record.setUpdateTime(new Date());
				thirdMerchantRechargeRecordService.updateThirdMerchantRechargeRecord(record, example);
			}
		} catch (Exception e) {
			logger.error("商户号："+appId+"，订单号："+orderNo+"回调通知商户出现异常：",e);
		}
		
	}

	/**
	 * 充值成功之后，用户资金流水 数据变动
	 * @param preRecord
	 */
	private void shopUserHandle(ThirdPreCompanyRechargeRecord preRecord){

		try {
			NideshopUser nideshopUser = nideshopuserService.selectByMobile(preRecord.getPhone());
			if (nideshopUser == null) {
				logger.info("手机号："+preRecord.getPhone()+"，没有此用户信息");
				return ;
			}
			Integer id = nideshopUser.getId();//获取用户id

			QzMoneyRecordEntity qzMoneyRecordEntity1 = qzMoneyRecordApiService.queryLastMoneyRecord(id);

			QzMoneyRecordEntity qzMoneyRecordEntity = new QzMoneyRecordEntity();
			qzMoneyRecordEntity = new QzMoneyRecordEntity();
			qzMoneyRecordEntity.setShopUserId(id);
			qzMoneyRecordEntity.setTranType("1");
			qzMoneyRecordEntity.setTranFlag(1);
			qzMoneyRecordEntity.setTarnAmount(preRecord.getAmount());
			qzMoneyRecordEntity.setTradeNo(preRecord.getOrderNo());
			qzMoneyRecordEntity.setRemark("充值成功");
			qzMoneyRecordEntity.setLockAmount(new BigDecimal("0.00"));
			if (qzMoneyRecordEntity1 == null) {
				qzMoneyRecordEntity.setCurrentAmount(preRecord.getAmount());
			}else {
				qzMoneyRecordEntity.setCurrentAmount(qzMoneyRecordEntity1.getCurrentAmount().add(preRecord.getAmount()));
			}
			qzMoneyRecordApiService.saveQzMoneyRecord(qzMoneyRecordEntity);


			logger.info("用户订单号："+preRecord.getOrderNo() + "，用户资金流水信息更新成功");
		}catch (Exception e){
			logger.error("用户订单号："+preRecord.getOrderNo() + "，用户资金流水信息更新失败：",e);
		}

	}


	/**
	 * 用户充值记录
	 * @param preRecord
	 */
	private void shopUserRechargeRecord(ThirdPreCompanyRechargeRecord preRecord){

		try {
			NideshopUser nideshopUser = nideshopuserService.selectByMobile(preRecord.getPhone());

			if (nideshopUser == null) {
				logger.info("手机号："+preRecord.getPhone()+"，没有此用户信息");
				return ;
			}

			Integer id = nideshopUser.getId();//获取用户id

			QzRechargeRecordEntity qzRechargeRecordEntity = new QzRechargeRecordEntity();
			qzRechargeRecordEntity.setShopUserId(id);
			qzRechargeRecordEntity.setMobile(preRecord.getPhone());
			qzRechargeRecordEntity.setState("1");
			qzRechargeRecordEntity.setOperateTime(new Date());
			//qzRechargeRecordEntity.setOperateId("");
			qzRechargeRecordEntity.setAmount(preRecord.getAmount());
			qzRechargeRecordEntity.setMemo("易宝充值成功");
			qzRechargeRecordEntity.setTradeNo(preRecord.getOrderNo());
			qzRechargeRecordEntity.setRechargeType(3);//易宝充值成功标识
			qzRechargeRecordEntity.setUpdateTime(new Date());
			qzRechargeRecordApiService.saveQzRechargeRecord(qzRechargeRecordEntity);
			logger.info("用户订单号："+preRecord.getOrderNo() + "，用户充值记录信息更新成功");
		}catch (Exception e){
			logger.error("用户订单号："+preRecord.getOrderNo() + "，用户充值记录信息更新失败：",e);
		}

	}

	/**
	 * 用户账号
	 * @param preRecord
	 */
	private void  shopUserAccount(ThirdPreCompanyRechargeRecord preRecord){
		try {
			NideshopUser nideshopUser = nideshopuserService.selectByMobile(preRecord.getPhone());

			if (nideshopUser == null) {
				logger.info("手机号："+preRecord.getPhone()+"，没有此用户信息");
				return ;
			}

			Integer id = nideshopUser.getId();//获取用户id
			QzUserAccountVo qzUserAccountVo1 = qzUserAccountApiService.queruUserAccountInfo(id);

			QzUserAccountVo qzUserAccountVo = new QzUserAccountVo();
			if (qzUserAccountVo1 == null) {
				qzUserAccountVo.setAmount(preRecord.getAmount());
				qzUserAccountVo.setLast_update_time(new Date());
				qzUserAccountVo.setLock_amount(new BigDecimal("0.00"));
				qzUserAccountVo.setCreate_time(new Date());
				qzUserAccountVo.setShop_user_id(id);
				qzUserAccountVo.setVersion(1);
				qzUserAccountApiService.saveQzUserAccountVo(qzUserAccountVo);
			}else{
				qzUserAccountVo.setShop_user_id(id);
				qzUserAccountVo.setAmount(qzUserAccountVo1.getAmount().add(preRecord.getAmount()));
				qzUserAccountVo.setLast_update_time(new Date());
				qzUserAccountApiService.updateUserAccount(qzUserAccountVo);
			}
			logger.info("用户订单号："+preRecord.getOrderNo() + "，用户账号信息更新成功");
		}catch (Exception e){
			logger.error("用户订单号："+preRecord.getOrderNo() + "，用户账号信息更新失败：",e);
		}

	}
	


	//回调通知三方
	
	
	//定时任务查询异常


	public static void main(String[] args) throws Exception {
//		Map<String, String> generateKeyPair = RSA.generateKeyPair();
//		String privateKey = generateKeyPair.get("privateKey");
//		String publicKey = generateKeyPair.get("publicKey");
//		String sign = MsgDigestUtils.sign("1|2|3|4|5", privateKey);
//		System.out.println(sign);
//		boolean verifySign = MsgDigestUtils.verifySign("1|2|3|4|5", sign, publicKey);
//		System.out.println(verifySign);
		
		System.out.println(StringUtils.isBlank("ds  "));
	}

	


}
