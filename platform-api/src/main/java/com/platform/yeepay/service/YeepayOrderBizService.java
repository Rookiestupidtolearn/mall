package com.platform.yeepay.service;

import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.entity.OrderVo;
import com.platform.entity.YeeTradeOrderEntity;
import com.platform.service.YeeTradeOrderService;
import com.platform.utils.GenerateCodeUtil;
import com.platform.yeepay.utils.PaymobileUtils;

@Service
public class YeepayOrderBizService {
   
	@Autowired
	private YeeTradeOrderService yeeTradeOrderService;
	
	public Map<String, Object> yeepayOrderSubmmit(OrderVo orderInfo) {
           //创建易宝订单实体
		     YeeTradeOrderEntity entity = new YeeTradeOrderEntity();
		     entity.setYeeOrderNo(GenerateCodeUtil.buildBizNo("YB"));
             entity.setTradeNo(orderInfo.getShipping_no());
             entity.setUserId(Integer.valueOf(orderInfo.getUser_id()+"") );
             entity.setPayStatus(0); //初始
             entity.setPayType(0);
             entity.setAmount(orderInfo.getActual_price());
		//orderexpdate, currency, version是非必传参数
//		if(request.getParameter("orderexpdate") == null) {
//			//如果该参数不填写，设置为24小时
//			orderexpdate	= 24*60;
//		} else {
//			orderexpdate	= ConvertUtils.objectToInt(request.getParameter("orderexpdate"));
//		}
//		if(request.getParameter("currency") == null) {
//			currency	= 156;
//		} else {
//			currency	= ConvertUtils.objectToInt(request.getParameter("currency"));
//		}
//		if(request.getParameter("version") == null) {
//			version		= 0;
//		} else {
//			version		= ConvertUtils.objectToInt(request.getParameter("version"));
//		}

		//使用TreeMap
		TreeMap<String, Object> treeMap	= new TreeMap<String, Object>();
		treeMap.put("orderid", 			entity.getYeeOrderNo());
		treeMap.put("productcatalog", 	"20");//行业类别
		treeMap.put("productname", 		"一键支付-测试");
		treeMap.put("identityid", 		entity.getUserId().toString());  //用户id
		treeMap.put("userip", 			"192.168.0.1");
		//treeMap.put("paytool", 			"2");
	//	treeMap.put("directpaytype", 	"1");
		treeMap.put("terminalid", 		"44-45-53-54-00-00");
		treeMap.put("terminaltype", 	3);
//		treeMap.put("userua", 			userua);
		treeMap.put("transtime", 		Calendar.getInstance().getTimeInMillis() / 1000 );//带秒的时间戳
		int amount = 1;
		treeMap.put("amount",1);//以分为单位
		treeMap.put("identitytype", 	2); //2 代表用户ID
		
		treeMap.put("productdesc", 		"测试下");
////		treeMap.put("fcallbackurl", 	fcallbackurl);
		treeMap.put("callbackurl", 		"http://localhost:8090/InstantPay-paymobile/jsp/41payApiRequest.jsp");
//		treeMap.put("paytypes", 		paytypes);
		treeMap.put("currency", 		156);
//		treeMap.put("orderexpdate", 	orderexpdate);
	//	treeMap.put("version", 			"1");
//		treeMap.put("cardno", 			cardno);
//		treeMap.put("idcardtype", 		idcardtype);
//		treeMap.put("idcard", 			idcard);
//		treeMap.put("owner", 			owner);
		entity.setRequestParam(treeMap.toString());
		yeeTradeOrderService.save(entity);
		
		System.out.println(treeMap);
		
		//第一步 生成AESkey及encryptkey
		String AESKey		= PaymobileUtils.buildAESKey();
		String encryptkey	= PaymobileUtils.buildEncyptkey(AESKey);

		//第二步 生成data
		String data			= PaymobileUtils.buildData(treeMap, AESKey);

		//第三步 获取商户编号及请求地址，并组装支付链接
		String merchantaccount	= PaymobileUtils.getMerchantaccount();
		String url				= PaymobileUtils.getRequestUrl(PaymobileUtils.PAYAPI_NAME);
		TreeMap<String, String> responseMap	= PaymobileUtils.httpPost(url, merchantaccount, data, encryptkey);

		System.out.println("url=" + url);
		System.out.println("merchantaccount=" + merchantaccount);
		System.out.println("data=" + data);
		System.out.println("encryptkey=" + encryptkey);
		
		//第四步 判断请求是否成功
		if(responseMap.containsKey("error_code")) {
			System.out.println(responseMap);
			return null;
		}
		
		//第五步 请求成功，则获取data、encryptkey，并将其解密
		String data_response						= responseMap.get("data");
		String encryptkey_response					= responseMap.get("encryptkey");
		TreeMap<String, String> responseDataMap	= PaymobileUtils.decrypt(data_response, encryptkey_response);

		//第六步 sign验签
		if(!PaymobileUtils.checkSign(responseDataMap)) {
			System.out.println("sign 验签失败！");
			return null;
		}

		//第七步 判断请求是否成功
		if(responseDataMap.containsKey("error_code")) {
			System.out.println(responseDataMap);
			return null;
		}
      
		return null ;

	}

	public String formatStr(String text) {
		return (text == null) ? "" : text.trim();
	}
}
