package com.platform.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.platform.entity.*;
import com.platform.service.*;
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
import com.platform.dao.ApiMoneyRecordMapper;
import com.platform.dao.QzUserAccountMapper;
import com.platform.util.ApiBaseAction;
import com.platform.utils.DateUtils;
import com.platform.yeepay.utils.PaymobileUtils;
import com.platform.youle.util.PropertiesUtil;

import io.swagger.annotations.Api;

@Api(tags = "易宝支付")
@RestController
@RequestMapping("/api/yeepay")
public class ApiYeepayController extends ApiBaseAction {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private YeeTradeOrderService yeeTradeOrderService;
	@Autowired
	private ApiOrderService apiOrderService;
    @Autowired
    private ApiOrderService orderService;
    @Autowired
    private ApiSendSMSService apiSendSMSService;
    @Autowired
    private ApiUserService apiUserService;

	@Autowired
	ApiMemberService apiMemberService;
	@Autowired
	QzRechargeRecordApiService qzRechargeRecordApiService;
	@Autowired
	ApiIntegralService apiIntegralService;
	@IgnoreAuth
	@GetMapping("yeepayOrderFCallback")
	public String yeepayOrderFCallback(String data, String encryptkey) {
		logger.info("易宝支付订单支付页面回调start......");
		
		try {
			if (StringUtils.isEmpty(data) || StringUtils.isEmpty(encryptkey)) {
				logger.error("易宝支付回调参数错误");
				return "ERROR";
			}
			// 解密data
			TreeMap<String, String> dataMap = PaymobileUtils.decrypt(data, encryptkey);
			logger.info("易宝支付订单回调，返回的明文参数：" + dataMap);
			// sign验签
			if (!PaymobileUtils.checkSign(dataMap)) {
				logger.error("易宝支付订单回调 ，sign 验签失败！");
				return "ERROR";
			}
			String yborderid = dataMap.get("yborderid");
			logger.info("易宝支付订单回调，易宝交易流水号" + yborderid);
			
			YeeTradeOrderEntity entity = yeeTradeOrderService.queryObjectByYborderid(yborderid);
			if (entity != null) {
				entity.getYeeOrderNo();
				
			}	
			
			
			
		} catch (Exception e) {
			logger.error("易宝支付订单支付页面回调失败，",e);
			return "ERROR";
		}

		return "SUCCESS";

	}

	@IgnoreAuth
	@PostMapping("yeepayOrderCallback")
	@Transactional
	public String yeepayOrderCallback(String data, String encryptkey) throws IOException {
		logger.info("易宝支付订单支付回调start");
		try {
			if (StringUtils.isEmpty(data) || StringUtils.isEmpty(encryptkey)) {
				logger.error("易宝支付回调参数错误");
				return "ERROR";
			}
			// 解密data
			TreeMap<String, String> dataMap = PaymobileUtils.decrypt(data, encryptkey);
			logger.info("易宝支付订单回调，返回的明文参数：" + dataMap);
			// sign验签
			if (!PaymobileUtils.checkSign(dataMap)) {
				logger.error("易宝支付订单回调 ，sign 验签失败！");
				return "ERROR";
			}
			String yborderid = dataMap.get("yborderid");
			logger.info("易宝支付订单回调，易宝交易流水号" + yborderid);

			YeeTradeOrderEntity entity = yeeTradeOrderService.queryObjectByYborderid(yborderid);
			if (entity != null) {
				if (entity.getPayStatus() == 1) {
					// 无需再次回调
					logger.info("本地订单已经处理成功，无需再处理，易宝交易流水号" + yborderid);
					return "SUCCESS";
				}
				// 校验实际支付的金额和订单的金额是否一致
				BigDecimal pay = new BigDecimal(dataMap.get("amount")).divide(new BigDecimal(100));

				if (pay.compareTo(entity.getAmount()) != 0) {
					logger.info("实际支付给易宝金额跟实际订单金额不一致,易宝交易流水号" + yborderid);
					// 有异常情况。
					// 修改回填信息
					entity.setPayAmount(pay);
					entity.setBankCode(dataMap.get("bankcode"));
					entity.setBank(dataMap.get("bank"));
					entity.setLastno(dataMap.get("lastno"));
					entity.setCardType(dataMap.get("cardtype"));
					entity.setPayStatus(1);// 1成功
					entity.setMsg("exception"); //异常
					entity.setMemo("实际支付给易宝金额跟实际订单金额不一致");
					yeeTradeOrderService.update(entity);
					// 更新本地状态为支付成功
					OrderVo order = apiOrderService.queryObjectByTradeNo(entity.getTradeNo());
					if (order != null) {
						// 更新订单为成功
						order.setPay_status(1);
						order.setOrder_status(200); // 支付成功，待提交京东订单
						order.setOrder_type("404");//支付异常
						apiOrderService.update(order);
						orderService.discountUserAmount(order);//支付成功，扣减平台比
					}

				} else {
					// 修改回填信息
					entity.setPayAmount(pay);
					entity.setBankCode(dataMap.get("bankcode"));
					entity.setBank(dataMap.get("bank"));
					entity.setLastno(dataMap.get("lastno"));
					entity.setCardType(dataMap.get("cardtype"));
					entity.setPayStatus(1);// 1成功
					entity.setMsg("success");
					entity.setMemo("支付成功");
					yeeTradeOrderService.update(entity);
					// 更新本地状态为支付成功
					OrderVo order = apiOrderService.queryObjectByTradeNo(entity.getTradeNo());
					if (order != null) {
						// 更新订单为成功
						order.setPay_status(1);
						order.setOrder_status(200); // 支付成功，待提交京东订单
						order.setOrder_type("1");//正常
						order.setPay_time(new Date());//订单支付时间
						apiOrderService.update(order);
						orderService.discountUserAmount(order);//支付成功，扣减平台比

						apiMemberService.memberShipUpgrade(order.getActual_price(),order.getUser_id().intValue());//支付成功，处理会员成长值
						apiIntegralService.increaseIntegralByUserId(order.getUser_id().intValue(),order.getActual_price(),null);//增加用户积分

						UserVo user = apiUserService.queryObject(order.getUser_id());
						if (order.getCoupon_price() != null && order.getCoupon_price().compareTo(new BigDecimal("0")) > 0) {
							//发送扣减短信
							String  smsTemplet = PropertiesUtil.getValue("doubao.properties","consumeSmsTemplet");
							String msgContent = MessageFormat.format(smsTemplet, user.getMobile(),DateUtils.formatChina(new Date()),order.getCoupon_price());
							apiSendSMSService.sendSms(user.getMobile(), msgContent);
						}
					    //订单支付成功短信
						String  paySuccessSmsTemplet = PropertiesUtil.getValue("doubao.properties","paySuccessSmsTemplet");
						String content = MessageFormat.format(paySuccessSmsTemplet, DateUtils.formatChina(new Date()),order.getOrder_sn());
						apiSendSMSService.sendSms(user.getMobile(), content);
						
					
					}
				}

			}

			// 回写SUCCESS
			logger.info("易宝支付订单支付回调end");
			return "SUCCESS";
		} catch (Exception e) {
			logger.error("易宝支付订单支付回调失败，",e);
			return "ERROR";
		}

	}


	/**
	 * 会员充值易宝回调
	 * @param data
	 * @param encryptkey
	 * @return
	 * @throws IOException
	 */
	@IgnoreAuth
	@PostMapping("yeepayMemberRechargeCallback")
	@Transactional
	public String yeepayMemberRechargeCallback(String data, String encryptkey) throws IOException {
		logger.info("易宝支付订单支付回调start");
		try {
			if (StringUtils.isEmpty(data) || StringUtils.isEmpty(encryptkey)) {
				logger.error("易宝支付回调参数错误");
				return "ERROR";
			}
			// 解密data
			TreeMap<String, String> dataMap = PaymobileUtils.decrypt(data, encryptkey);
			logger.info("易宝支付订单回调，返回的明文参数：" + dataMap);
			// sign验签
			if (!PaymobileUtils.checkSign(dataMap)) {
				logger.error("易宝支付订单回调 ，sign 验签失败！");
				return "ERROR";
			}
			String yborderid = dataMap.get("yborderid");
			logger.info("易宝支付订单回调，易宝交易流水号" + yborderid);

			YeeTradeOrderEntity entity = yeeTradeOrderService.queryObjectByYborderid(yborderid);
			if (entity != null) {
				if (entity.getPayStatus() == 1) {
					// 无需再次回调
					logger.info("本地订单已经处理成功，无需再处理，易宝交易流水号" + yborderid);
					return "SUCCESS";
				}
				// 校验实际支付的金额和订单的金额是否一致
				BigDecimal pay = new BigDecimal(dataMap.get("amount")).divide(new BigDecimal(100));

				if (pay.compareTo(entity.getAmount()) != 0) {
					logger.info("实际支付给易宝金额跟实际订单金额不一致,易宝交易流水号" + yborderid);
					// 有异常情况。
					// 修改回填信息
					entity.setPayAmount(pay);
					entity.setBankCode(dataMap.get("bankcode"));
					entity.setBank(dataMap.get("bank"));
					entity.setLastno(dataMap.get("lastno"));
					entity.setCardType(dataMap.get("cardtype"));
					entity.setPayStatus(1);// 1成功
					entity.setMsg("exception"); //异常
					entity.setMemo("实际支付给易宝金额跟实际订单金额不一致");
					yeeTradeOrderService.update(entity);



				} else {
					// 修改回填信息
					entity.setPayAmount(pay);
					entity.setBankCode(dataMap.get("bankcode"));
					entity.setBank(dataMap.get("bank"));
					entity.setLastno(dataMap.get("lastno"));
					entity.setCardType(dataMap.get("cardtype"));
					entity.setPayStatus(1);// 1成功
					entity.setMsg("success");
					entity.setMemo("支付成功");
					yeeTradeOrderService.update(entity);
					Map<String,Object> result=apiMemberService.rechargeSuccessAndUpdateVplus(entity.getTradeNo());
					if(result.get("code").equals("success")){
						logger.info("用户会员权益增加成功");
//
						//订单支付成功短信
//						String  paySuccessSmsTemplet = PropertiesUtil.getValue("doubao.properties","paySuccessSmsTemplet");
//						String content = MessageFormat.format(paySuccessSmsTemplet, DateUtils.formatChina(new Date()),order.getOrder_sn());
//						apiSendSMSService.sendSms(result.get("mee").toString(), content);
					}else{
						QzRechargeRecordEntity qzRechargeRecordEntity=qzRechargeRecordApiService.queryObjectByTradeNo(entity.getTradeNo());
						qzRechargeRecordEntity.setMemo("会员充值易宝支付成功但是会员用户权益延长失败");
						qzRechargeRecordApiService.update(qzRechargeRecordEntity);
						logger.info("易宝支付成功但是会员用户权益延长失败");
					}

				}

			}

			// 回写SUCCESS
			logger.info("易宝支付订单支付回调end");
			return "SUCCESS";
		} catch (Exception e) {
			logger.error("易宝支付订单支付回调失败，",e);
			return "ERROR";
		}

	}


}
