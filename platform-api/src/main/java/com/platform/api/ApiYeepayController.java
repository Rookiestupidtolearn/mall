package com.platform.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.platform.annotation.IgnoreAuth;
import com.platform.entity.OrderVo;
import com.platform.entity.YeeTradeOrderEntity;
import com.platform.service.ApiOrderService;
import com.platform.service.YeeTradeOrderService;
import com.platform.util.ApiBaseAction;
import com.platform.yeepay.utils.PaymobileUtils;

import io.swagger.annotations.Api;

@Api(tags = "易宝支付")
@RestController
@RequestMapping("/api/yeepay")
public class ApiYeepayController extends ApiBaseAction {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private  YeeTradeOrderService  yeeTradeOrderService;
	@Autowired
	private ApiOrderService apiOrderService ;
	
	
	
	@IgnoreAuth
	@GetMapping("yeepayOrderFCallback")
	public String yeepayOrderFCallback(String data,String encryptkey){
		  logger.info("易宝支付订单支付回调start");
		  if(StringUtils.isEmpty(data) || StringUtils.isEmpty(encryptkey)){
			  logger.error("易宝支付回调参数错误");
			  return "ERROR";
		  }
		  
			//解密data
			TreeMap<String, String>	dataMap	= PaymobileUtils.decrypt(data, encryptkey);
			logger.info("易宝支付订单回调，返回的明文参数：" + dataMap);
			//sign验签
			if(!PaymobileUtils.checkSign(dataMap)) {
				logger.error("易宝支付订单回调 ，sign 验签失败！");
				return "ERROR";
			}
		  
		  return "SUCCESS";
		
	}
	
	@IgnoreAuth
	@PostMapping("yeepayOrderCallback")
	@Transactional
	public String yeepayOrderCallback(String data,String encryptkey) throws IOException{
		  logger.info("易宝支付订单支付回调start");
		  if(StringUtils.isEmpty(data) || StringUtils.isEmpty(encryptkey)){
			  logger.error("易宝支付回调参数错误");
			  return "ERROR";
		  }
		
			//解密data
			TreeMap<String, String>	dataMap	= PaymobileUtils.decrypt(data, encryptkey);
			logger.info("易宝支付订单回调，返回的明文参数：" + dataMap);
			//sign验签
			if(!PaymobileUtils.checkSign(dataMap)) {
				logger.error("易宝支付订单回调 ，sign 验签失败！");
				return "ERROR";
			}
			String  yborderid = dataMap.get("yborderid");
			logger.info("易宝支付订单回调，易宝交易流水号"+yborderid);

			YeeTradeOrderEntity entity = yeeTradeOrderService.queryObjectByYborderid(yborderid);
			if (entity != null) {
				if (entity.getPayStatus()==1) {
					 //无需再次回调
					 logger.info("本地订单已经处理成功，无需再处理，易宝交易流水号"+yborderid);
					  return "SUCCESS";
				}
				//修改回填信息
				entity.setBankCode(dataMap.get("bankcode"));
				entity.setBank(dataMap.get("bank"));
				entity.setLastno(dataMap.get("lastno"));
				entity.setCardType(dataMap.get("cardtype"));
				entity.setPayStatus(1);//1成功
				entity.setMsg("success");
				yeeTradeOrderService.update(entity);
				//更新本地状态为支付成功
				OrderVo order = apiOrderService.queryObjectByTradeNo(entity.getTradeNo());
				if (order != null) {
					  //更新订单为成功
					order.setPay_status(1);
					apiOrderService.update(order);
					//调京东的支付下订单的接口
					
					
				}
				
		
				
				
			}
			
			//回写SUCCESS

			logger.info("易宝支付订单支付回调end");
		  return "SUCCESS";
		
	}
}
