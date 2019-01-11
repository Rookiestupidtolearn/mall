package com.platform.api;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.LoginUser;
import com.platform.cache.J2CacheUtils;
import com.platform.dao.ApiAddressMapper;
import com.platform.dao.ApiMoneyRecordMapper;
import com.platform.dao.QzUserAccountMapper;
import com.platform.entity.AddressVo;
import com.platform.entity.CartVo;
import com.platform.entity.GoodsVo;
import com.platform.entity.OrderGoodsVo;
import com.platform.entity.OrderVo;
import com.platform.entity.QzMoneyRecordVo;
import com.platform.entity.QzUserAccountVo;
import com.platform.entity.UserVo;
import com.platform.entity.YeeTradeOrderEntity;
import com.platform.service.ApiGoodsService;
import com.platform.service.ApiOrderGoodsService;
import com.platform.service.ApiOrderService;
import com.platform.service.JdOrderService;
import com.platform.service.YeeTradeOrderService;
import com.platform.util.ApiBaseAction;
import com.platform.util.wechat.WechatRefundApiResult;
import com.platform.util.wechat.WechatUtil;
import com.platform.utils.CharUtil;
import com.platform.utils.DateUtils;
import com.platform.utils.MapUtils;
import com.platform.utils.ResourceUtil;
import com.platform.utils.XmlUtil;
import com.sun.tools.classfile.StackMapTable_attribute.append_frame;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 作者: @author Harmon <br>
 * 时间: 2017-08-11 08:32<br>
 * 描述: ApiIndexController <br>
 */
@Api(tags = "商户支付")
@RestController
@RequestMapping("/api/pay")
public class ApiPayController extends ApiBaseAction {
    private Logger logger = Logger.getLogger(getClass());
    @Autowired
    private ApiOrderService orderService;
    @Autowired
    private ApiOrderGoodsService orderGoodsService;
    @Autowired
    private QzUserAccountMapper qzUserAccountMapper;
    @Autowired
    private ApiMoneyRecordMapper apiMoneyRecordMapper;
    @Autowired
    private YeeTradeOrderService yeeTradeOrderService;
	@Autowired
	private JdOrderService jdOrderService;
    @Autowired
    private ApiAddressMapper apiAddressMapper;
	@Autowired
	private ApiOrderGoodsService apiOrderGoodsService;
	
	@Autowired
	private ApiGoodsService apiGoodsService;
    
    /**
     */
    @ApiOperation(value = "跳转")
    @PostMapping("index")
    public Object index() {
        //
        return toResponsSuccess("");
    }

    
    @ApiOperation(value = "去支付订单")
    @PostMapping("toPayOrder")
    public Object toPayOrder(@LoginUser UserVo loginUser, Integer orderId){
    	Map<String, Object>  resultObj = new HashMap<>();
    	JSONObject feedbackJson = super.getJsonRequest();
    	
    	if (feedbackJson.get("orderId") == null) {
			return toResponsFail("订单orderId不能为空");
		}
    	orderId = Integer.parseInt(feedbackJson.get("orderId").toString());
    	  logger.info("去支付订单,订单的id"+orderId);
    	  OrderVo orderInfo = orderService.queryObject(orderId);
    	   if (null == orderInfo) {
               return toResponsObject(400, "订单已取消", "");
           }
    	   if (orderInfo.getPay_status() !=0) {
    		   return toResponsObject(400, "订单已支付，请不要重复操作", "");
		  }
    	   if (StringUtils.isEmpty(orderInfo.getShipping_no()) ) {
    		   return toResponsObject(400, "shipping_no 订单编号不存在", "");
		  }
    	   if (orderInfo.getAddress_id() == null) {
    		   return toResponsObject(400, "Address_id 收货地址不存在", "");
		  }
    	   //判断订单的实效性
    	  int minitCha = DateUtils.getBetweenDateByType(new Date(), orderInfo.getAdd_time(), "second");
    	  if (minitCha >86400 ) { //秒
    		  return toResponsObject(400, "该笔订单已经实效重新下单", "");
		  }
    	  //校验上下架。校验库存
        AddressVo addressVo = apiAddressMapper.queryObject(orderInfo.getAddress_id());
    		// 库存
  		String address = addressVo.getProvince() + "_" + addressVo.getCity() + "_" + addressVo.getCounty();
  		 Map<String, Object> map = new HashMap<>();
  		 map.put("order_id", orderId);
  		 List<OrderGoodsVo> list =   apiOrderGoodsService.queryList(map);
  		
  		List<OrderGoodsVo>  unsells = new ArrayList<>();
		for (OrderGoodsVo orderGoodsVo : list) {
			GoodsVo goods = apiGoodsService.queryObject(orderGoodsVo.getGoods_id());
			if (goods == null) {
				continue;
			}
			// 判断是三方的还是自己的产品
			String source = goods.getSource();
			if (source.equals("JD")) {
				// 检验库存+上下架状态
				String pid = goods.getGoods_sn().substring(2, goods.getGoods_sn().length());
				// 库存
				Map<String, Object> stockMap = jdOrderService.checkStockSingle(pid, orderGoodsVo.getNumber(), address);
				if (!stockMap.get("code").equals("200")) {
					resultObj.put("errno", "1");
					resultObj.put("errmsg", "不可出售");
					unsells.add(orderGoodsVo);
					continue;
				}
				// 上下架状态
				Map<String, Object> saleStatusMap = jdOrderService.checkSaleStatusSingle(Integer.parseInt(pid));
				if (!saleStatusMap.get("code").equals("200")) {
					resultObj.put("errno", "1");
					resultObj.put("errmsg", "不可出售");
					unsells.add(orderGoodsVo);
					continue;
				}
			}
			if (source.equals("system")) {
				// 校验自己的库存和上下架状态
				if (goods.getGoods_number() < orderGoodsVo.getNumber()) {
					logger.info(
							"【系统商品库存不足无法正常下单】商品id:" + orderGoodsVo.getGoods_id() + "剩余库存：" + goods.getGoods_number());
					unsells.add(orderGoodsVo);
					continue;
				}
				if (goods.getIs_on_sale() == 0) {
					logger.info("【系统商品已经下架无法正常下单】商品上下架状态为：" + goods.getIs_on_sale());
					unsells.add(orderGoodsVo);
					continue;
				}

			}

		}
  		
   	 if (!CollectionUtils.isEmpty(unsells)) {
			 resultObj.put("unsells", unsells); 
			 resultObj.put("errno", "1");
			 resultObj.put("errmsg", "不可售");
			 resultObj.put("count", unsells.size()); 
			 return resultObj;
   		 
		}
  		
  		
  		
    	   String payurl = "";
    	   YeeTradeOrderEntity  yeepayOrder  =  yeeTradeOrderService.queryObjectByTradeNo(orderInfo.getShipping_no());
    	   if (yeepayOrder != null && yeepayOrder.getResponseMsg() !=null) {
    		   JSONObject jsonObject = JSONObject.parseObject(yeepayOrder.getResponseMsg());
    		   if (jsonObject !=null) {
    			   payurl = (String) jsonObject.get("payurl");
			   }
		   }
    	   resultObj.put("orderId", orderId);
    	   resultObj.put("payurl", payurl);
    	   resultObj.put("200", "请求成功");
    	   resultObj.put("errno", "0");
    	   return  resultObj;
    	   
    } 
    
    
    /**
     * 获取支付的请求参数
     */
    @ApiOperation(value = "获取支付的请求参数")
    @PostMapping("prepay")
    public Object payPrepay(@LoginUser UserVo loginUser, Integer orderId) {
        //
    	
        OrderVo orderInfo = orderService.queryObject(orderId);

        if (null == orderInfo) {
            return toResponsObject(400, "订单已取消", "");
        }

        if (orderInfo.getPay_status() != 0) {
            return toResponsObject(400, "订单已支付，请不要重复操作", "");
        }

        String nonceStr = CharUtil.getRandomString(32);

        //https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=3
        Map<Object, Object> resultObj = new TreeMap();

        try {
            Map<Object, Object> parame = new TreeMap<Object, Object>();
            parame.put("appid", ResourceUtil.getConfigByName("wx.appId"));
            // 商家账号。
            parame.put("mch_id", ResourceUtil.getConfigByName("wx.mchId"));
            String randomStr = CharUtil.getRandomNum(18).toUpperCase();
            // 随机字符串
            parame.put("nonce_str", randomStr);
            // 商户订单编号
            parame.put("out_trade_no", orderInfo.getOrder_sn());
            Map orderGoodsParam = new HashMap();
            orderGoodsParam.put("order_id", orderId);
            // 商品描述
            parame.put("body", "超市-支付");
            //订单的商品
            List<OrderGoodsVo> orderGoods = orderGoodsService.queryList(orderGoodsParam);
            if (null != orderGoods) {
                String body = "超市-";
                for (OrderGoodsVo goodsVo : orderGoods) {
                    body = body + goodsVo.getGoods_name() + "、";
                }
                if (body.length() > 0) {
                    body = body.substring(0, body.length() - 1);
                }
                // 商品描述
                parame.put("body", body);
            }
            //支付金额
            parame.put("total_fee", orderInfo.getActual_price().multiply(new BigDecimal(100)).intValue());
            // 回调地址
            parame.put("notify_url", ResourceUtil.getConfigByName("wx.notifyUrl"));
            // 交易类型APP
            parame.put("trade_type", ResourceUtil.getConfigByName("wx.tradeType"));
            parame.put("spbill_create_ip", getClientIp());
            parame.put("openid", loginUser.getWeixinOpenid());
            String sign = WechatUtil.arraySign(parame, ResourceUtil.getConfigByName("wx.paySignKey"));
            // 数字签证
            parame.put("sign", sign);

            String xml = MapUtils.convertMap2Xml(parame);
            logger.info("xml:" + xml);
            Map<String, Object> resultUn = XmlUtil.xmlStrToMap(WechatUtil.requestOnce(ResourceUtil.getConfigByName("wx.uniformorder"), xml));
            // 响应报文
            String return_code = MapUtils.getString("return_code", resultUn);
            String return_msg = MapUtils.getString("return_msg", resultUn);
            //
            if (return_code.equalsIgnoreCase("FAIL")) {
                return toResponsFail("支付失败," + return_msg);
            } else if (return_code.equalsIgnoreCase("SUCCESS")) {
                // 返回数据
                String result_code = MapUtils.getString("result_code", resultUn);
                String err_code_des = MapUtils.getString("err_code_des", resultUn);
                if (result_code.equalsIgnoreCase("FAIL")) {
                    return toResponsFail("支付失败," + err_code_des);
                } else if (result_code.equalsIgnoreCase("SUCCESS")) {
                    String prepay_id = MapUtils.getString("prepay_id", resultUn);
                    // 先生成paySign 参考https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=5
                    resultObj.put("appId", ResourceUtil.getConfigByName("wx.appId"));
                    resultObj.put("timeStamp", DateUtils.timeToStr(System.currentTimeMillis() / 1000, DateUtils.DATE_TIME_PATTERN));
                    resultObj.put("nonceStr", nonceStr);
                    resultObj.put("package", "prepay_id=" + prepay_id);
                    resultObj.put("signType", "MD5");
                    String paySign = WechatUtil.arraySign(resultObj, ResourceUtil.getConfigByName("wx.paySignKey"));
                    resultObj.put("paySign", paySign);
                    // 业务处理
                    orderInfo.setPay_id(prepay_id);
                    // 付款中
                    orderInfo.setPay_status(0);
                    orderService.update(orderInfo);
                    saveMoneyRecord(loginUser.getUserId(),0,orderInfo);
                    return toResponsObject(0, "微信统一订单下单成功", resultObj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return toResponsFail("下单失败,error=" + e.getMessage());
        }
        return toResponsFail("下单失败");
    }

    /**
     * 微信查询订单状态
     */
    @ApiOperation(value = "查询订单状态")
    @PostMapping("query")
    public Object orderQuery(@LoginUser UserVo loginUser, Integer orderId) {
        if (orderId == null) {
            return toResponsFail("订单不存在");
        }
        
        Map<Object, Object> parame = new TreeMap<Object, Object>();
        parame.put("appid", ResourceUtil.getConfigByName("wx.appId"));
        // 商家账号。
        parame.put("mch_id", ResourceUtil.getConfigByName("wx.mchId"));
        String randomStr = CharUtil.getRandomNum(18).toUpperCase();
        // 随机字符串
        parame.put("nonce_str", randomStr);
        // 商户订单编号
        parame.put("out_trade_no", orderId);

        String sign = WechatUtil.arraySign(parame, ResourceUtil.getConfigByName("wx.paySignKey"));
        // 数字签证
        parame.put("sign", sign);

        String xml = MapUtils.convertMap2Xml(parame);
        logger.info("xml:" + xml);
        Map<String, Object> resultUn = null;
        try {
            resultUn = XmlUtil.xmlStrToMap(WechatUtil.requestOnce(ResourceUtil.getConfigByName("wx.orderquery"), xml));
        } catch (Exception e) {
            e.printStackTrace();
            return toResponsFail("查询失败,error=" + e.getMessage());
        }
        // 响应报文
        String return_code = MapUtils.getString("return_code", resultUn);
        String return_msg = MapUtils.getString("return_msg", resultUn);

        if (!"SUCCESS".equals(return_code)) {
            return toResponsFail("查询失败,error=" + return_msg);
        }

        String trade_state = MapUtils.getString("trade_state", resultUn);
        if ("SUCCESS".equals(trade_state)) {
            // 更改订单状态
            // 业务处理
            OrderVo orderInfo = new OrderVo();
            orderInfo.setId(orderId);
            orderInfo.setPay_status(1);
            orderInfo.setOrder_status(201);
            orderInfo.setShipping_status(0);
            orderInfo.setPay_time(new Date());
            orderService.update(orderInfo);
            orderService.discountUserAmount(orderInfo);
            return toResponsMsgSuccess("支付成功");
        } else if ("USERPAYING".equals(trade_state)) {
            // 重新查询 正在支付中
            Integer num = (Integer) J2CacheUtils.get(J2CacheUtils.SHOP_CACHE_NAME, "queryRepeatNum" + orderId + "");
            if (num == null) {
                J2CacheUtils.put(J2CacheUtils.SHOP_CACHE_NAME, "queryRepeatNum" + orderId + "", 1);
                this.orderQuery(loginUser, orderId);
            } else if (num <= 3) {
                J2CacheUtils.remove(J2CacheUtils.SHOP_CACHE_NAME, "queryRepeatNum" + orderId);
                this.orderQuery(loginUser, orderId);
            } else {
                return toResponsFail("查询失败,error=" + trade_state);
            }

        } else {
            // 失败
            return toResponsFail("查询失败,error=" + trade_state);
        }

        return toResponsFail("查询失败，未知错误");
    }

    /**
     * 微信订单回调接口
     *
     * @return
     */
    @ApiOperation(value = "微信订单回调接口")
    @RequestMapping(value = "/notify", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public void notify(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            InputStream in = request.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.close();
            in.close();
            //xml数据
            String reponseXml = new String(out.toByteArray(), "utf-8");

            WechatRefundApiResult result = (WechatRefundApiResult) XmlUtil.xmlStrToBean(reponseXml, WechatRefundApiResult.class);
            String result_code = result.getResult_code();
            if (result_code.equalsIgnoreCase("FAIL")) {
                //订单编号
                String out_trade_no = result.getOut_trade_no();
                logger.error("订单" + out_trade_no + "支付失败");
                response.getWriter().write(setXml("SUCCESS", "OK"));
            } else if (result_code.equalsIgnoreCase("SUCCESS")) {
                //订单编号
                String out_trade_no = result.getOut_trade_no();
                logger.error("订单" + out_trade_no + "支付成功");
                // 业务处理
                OrderVo orderInfo = orderService.queryObject(Integer.valueOf(out_trade_no));
                orderInfo.setPay_status(1);
                orderInfo.setOrder_status(201);
                orderInfo.setShipping_status(0);
                orderInfo.setPay_time(new Date());
                orderService.update(orderInfo);
                orderService.discountUserAmount(orderInfo);
                response.getWriter().write(setXml("SUCCESS", "OK"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * 订单退款请求
     */
    @ApiOperation(value = "订单退款请求")
    @PostMapping("refund")
    public Object refund(Integer orderId) {
        //
        OrderVo orderInfo = orderService.queryObject(orderId);

        if (null == orderInfo) {
            return toResponsObject(400, "订单已取消", "");
        }

        if (orderInfo.getOrder_status() == 401 || orderInfo.getOrder_status() == 402) {
            return toResponsObject(400, "订单已退款", "");
        }

//        if (orderInfo.getPay_status() != 2) {
//            return toResponsObject(400, "订单未付款，不能退款", "");
//        }

//        WechatRefundApiResult result = WechatUtil.wxRefund(orderInfo.getId().toString(),
//                orderInfo.getActual_price().doubleValue(), orderInfo.getActual_price().doubleValue());
        WechatRefundApiResult result = WechatUtil.wxRefund(orderInfo.getId().toString(),
                10.01, 10.01);
        if (result.getResult_code().equals("SUCCESS")) {
            if (orderInfo.getOrder_status() == 201) {
                orderInfo.setOrder_status(401);
            } else if (orderInfo.getOrder_status() == 300) {
                orderInfo.setOrder_status(402);
            }
            orderService.update(orderInfo);
            return toResponsObject(400, "成功退款", "");
        } else {
            return toResponsObject(400, "退款失败", "");
        }
    }


    //返回微信服务
    public static String setXml(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
    }

    //模拟微信回调接口
    public static String callbakcXml(String orderNum) {
        return "<xml><appid><![CDATA[wx2421b1c4370ec43b]]></appid><attach><![CDATA[支付测试]]></attach><bank_type><![CDATA[CFT]]></bank_type><fee_type><![CDATA[CNY]]></fee_type> <is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[10000100]]></mch_id><nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str><openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid> <out_trade_no><![CDATA[" + orderNum + "]]></out_trade_no>  <result_code><![CDATA[SUCCESS]]></result_code> <return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign><sub_mch_id><![CDATA[10000100]]></sub_mch_id> <time_end><![CDATA[20140903131540]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id></xml>";
    }
    
    

    public void saveMoneyRecord(Long userId,Integer type,OrderVo order){
		QzUserAccountVo userAmountVo =qzUserAccountMapper.queruUserAccountInfo(Long.parseLong(userId.toString()));
    	if(userAmountVo != null){
    		QzMoneyRecordVo moneyRecord  = new QzMoneyRecordVo();
    		moneyRecord.setShopUserId(userId.intValue());
    		moneyRecord.setTranType("2");//使用克拉币
    		moneyRecord.setTranFlag(type);//0-支出 1-收入
    		moneyRecord.setTarnAmount(order.getCoupon_price());
    		moneyRecord.setCreateTime(new Date());
    		moneyRecord.setTradeNo(order.getOrder_sn());
    		if(userAmountVo != null){
    			moneyRecord.setCurrentAmount(userAmountVo.getAmount());
    		}
    		apiMoneyRecordMapper.save(moneyRecord);
    	}
    }
}