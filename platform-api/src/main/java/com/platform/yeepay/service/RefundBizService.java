package com.platform.yeepay.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.platform.utils.GenerateCodeUtil;
import com.platform.yeepay.utils.PaymobileUtils;


/**
 * 退款接口 
 * @author: yingjie.wang    
 * @since : 2015-10-10 14:33
 */
@Service
public class RefundBizService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	



	public  Map<String, Object>  directRefund(String origyborderid,BigDecimal refundAmount) throws ServletException, IOException {
		Map<String, Object> resultObj = new HashMap<String, Object>();
		
		String orderid = GenerateCodeUtil.buildBizNo("RD");
		 int amount = refundAmount.multiply(new BigDecimal(100)).intValue();//实际下单的金额
		//使用TreeMap
		TreeMap<String, Object> treeMap	= new TreeMap<String, Object>();
		treeMap.put("orderid", 		orderid);
		treeMap.put("origyborderid",origyborderid);
		treeMap.put("amount", 		amount);
		treeMap.put("currency", 	156);
		treeMap.put("cause", 		"测试退款");

		//第一步 生成AESkey及encryptkey
		String AESKey		= PaymobileUtils.buildAESKey();
		String encryptkey	= PaymobileUtils.buildEncyptkey(AESKey);

		//第二步 生成data
		String data			= PaymobileUtils.buildData(treeMap, AESKey);

		//第三步 http请求，退款接口的请求方式为POST
		String merchantaccount				= PaymobileUtils.getMerchantaccount();
		String url							= PaymobileUtils.getRequestUrl(PaymobileUtils.REFUNDAPI_NAME);
		TreeMap<String, String> responseMap	= PaymobileUtils.httpPost(url, merchantaccount, data, encryptkey);

		//第四步 判断请求是否成功
		if(responseMap.containsKey("error_code")) {
			logger.info("易宝操作退款失败"+responseMap.toString());
			return resultObj;
		}

		//第五步 请求成功，则获取data、encryptkey，并将其解密
		String data_response						= responseMap.get("data");
		String encryptkey_response					= responseMap.get("encryptkey");
		TreeMap<String, String> responseDataMap	= PaymobileUtils.decrypt(data_response, encryptkey_response);

		logger.info("请求返回的明文参数：" + responseDataMap);

		//第六步 sign验签
		if(!PaymobileUtils.checkSign(responseDataMap)) {
			logger.info("易宝操作退款,sign 验签失败！"+responseDataMap.toString());
			return resultObj;
		}

		//第七步 判断请求是否成功
		if(responseDataMap.containsKey("error_code")) {
			logger.info("易宝操作退款失败，"+responseDataMap.toString());
			return resultObj;
		}

		//第八步 进行业务处理
      
		return resultObj ;
		
	}


}
