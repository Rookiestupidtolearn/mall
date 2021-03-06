package com.platform.api;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.dao.ApiOrderGoodsMapper;
import com.platform.dao.ApiOrderMapper;
import com.platform.entity.OrderGoodsVo;
import com.platform.entity.OrderVo;
import com.platform.entity.UserVo;
import com.platform.service.ApiKdniaoService;
import com.platform.service.ApiOrderGoodsService;
import com.platform.service.ApiOrderService;
import com.platform.service.JdOrderService;
import com.platform.util.ApiBaseAction;
import com.platform.util.ApiPageUtils;
import com.platform.util.wechat.WechatRefundApiResult;
import com.platform.util.wechat.WechatUtil;
import com.platform.utils.Query;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 作者: @author Harmon <br>
 * 时间: 2017-08-11 08:32<br>
 * 描述: ApiIndexController <br>
 */
@Api(tags = "订单相关")
@RestController
@RequestMapping("/api/order")
public class ApiOrderController extends ApiBaseAction {
	private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApiOrderService orderService;
    @Autowired
    private ApiOrderGoodsService orderGoodsService;
    @Autowired
    private ApiKdniaoService apiKdniaoService;
    @Autowired
    private ApiOrderMapper apiOrderMapper;
    @Autowired
    private JdOrderService JdOrderService;
    @Autowired
    private ApiOrderGoodsMapper  apiOrderGoodsMapper;
    
    @ApiOperation(value = "订单首页")
    @IgnoreAuth
    @PostMapping("index")
    public Object index() {
        //
        return toResponsSuccess("");
    }

    /**
     * 获取订单列表
     */
    @ApiOperation(value = "获取订单列表")
    @PostMapping("list")
    public Object list(@LoginUser UserVo loginUser,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "size", defaultValue = "10") Integer size) {
        //
        Map params = new HashMap();
        params.put("user_id", loginUser.getUserId());
        params.put("page", page);
        params.put("limit", size);
        params.put("sidx", "id");
        params.put("order", "asc");
        //查询列表数据
        Query query = new Query(params);
        List<OrderVo> orderEntityList = apiOrderMapper.queryList(query);
        int total = orderService.queryTotal(query);
        ApiPageUtils pageUtil = new ApiPageUtils(orderEntityList, total, query.getLimit(), query.getPage());
        //
        for (OrderVo item : orderEntityList) {
            Map orderGoodsParam = new HashMap();
            orderGoodsParam.put("order_id", item.getId());
            //订单的商品
            List<OrderGoodsVo> goodsList = orderGoodsService.queryList(orderGoodsParam);
            Integer goodsCount = 0;
            for (OrderGoodsVo orderGoodsEntity : goodsList) {
                goodsCount += orderGoodsEntity.getNumber();
                item.setGoodsCount(goodsCount);
            }
        }
        return toResponsSuccess(pageUtil);
    }

    /**
     * 获取订单详情
     */
    @ApiOperation(value = "获取订单详情")
    @PostMapping("detail")
    public Object detail(Integer orderId) {
        Map resultObj = new HashMap();
        //
        OrderVo orderInfo = orderService.queryObject(orderId);
        if (null == orderInfo) {
            return toResponsObject(400, "订单不存在", "");
        }
        Map orderGoodsParam = new HashMap();
        orderGoodsParam.put("order_id", orderId);
        //订单的商品
        List<OrderGoodsVo> orderGoods = orderGoodsService.queryList(orderGoodsParam);
        //订单最后支付时间
        if (orderInfo.getOrder_status() == 0) {
            // if (moment().subtract(60, 'minutes') < moment(orderInfo.add_time)) {
//            orderInfo.final_pay_time = moment("001234", "Hmmss").format("mm:ss")
            // } else {
            //     //超过时间不支付，更新订单状态为取消
            // }
        }

        //订单可操作的选择,删除，支付，收货，评论，退换货
        Map handleOption = orderInfo.getHandleOption();
        //
        resultObj.put("orderInfo", orderInfo);
        resultObj.put("orderGoods", orderGoods);
        resultObj.put("handleOption", handleOption);
        if (!StringUtils.isEmpty(orderInfo.getShipping_code()) && !StringUtils.isEmpty(orderInfo.getShipping_no())) {
            // 快递
            List Traces = apiKdniaoService.getOrderTracesByJson(orderInfo.getShipping_code(), orderInfo.getShipping_no());
            resultObj.put("shippingList", Traces);
        }
        return toResponsSuccess(resultObj);
    }

    @ApiOperation(value = "修改订单")
    @PostMapping("updateSuccess")
    public Object updateSuccess(Integer orderId) {
        OrderVo orderInfo = orderService.queryObject(orderId);
        if (orderInfo == null) {
            return toResponsFail("订单不存在");
        } else if (orderInfo.getOrder_status() != 0) {
            return toResponsFail("订单状态不正确orderStatus" + orderInfo.getOrder_status() + "payStatus" + orderInfo.getPay_status());
        }

        orderInfo.setId(orderId);
        orderInfo.setPay_status(1);
        orderInfo.setOrder_status(201);
        orderInfo.setShipping_status(0);
        orderInfo.setPay_time(new Date());
        int num = orderService.update(orderInfo);
        if (num > 0) {
            return toResponsMsgSuccess("修改订单成功");
        } else {
            return toResponsFail("修改订单失败");

        }
    }

    /**
     * 订单提交
     */
    @ApiOperation(value = "订单提交")
    @PostMapping("submit")
    public Object submit(HttpServletRequest request,@LoginUser UserVo loginUser) {
    	
//        String  REDIS_ORDER_LOCK ="orderSubmitLock"+loginUser.getUserId();
        
//    	Object  orderSubmitLock =  J2CacheUtils.get(J2CacheUtils.SHOP_CACHE_NAME,REDIS_ORDER_LOCK);
//    	if(orderSubmitLock!=null){
//    		return toResponsFail("订单处理中，请稍后再试。。。");
//    	}
//    	
//    	J2CacheUtils.putExire(J2CacheUtils.SHOP_CACHE_NAME, REDIS_ORDER_LOCK, REDIS_ORDER_LOCK, 5*60L);
    	synchronized (loginUser.getUserId()) {
    		Map resultObj = null;
    	    try {
            	logger.info("订单提交，用户id:"+loginUser.getUserId());
            	logger.info("订单提交，redis锁:orderSubmitLock"+loginUser.getUserId());
	        	resultObj = orderService.submit(request,getJsonRequest(), loginUser);
	            if (null != resultObj) {
//	                return toResponsObject(MapUtils.getInteger(resultObj, "errno"), MapUtils.getString(resultObj, "errmsg"), resultObj.get("data"));
	            	 return resultObj;
	            }
            } catch (Exception e) {
            	logger.error("订单提交,处理订单失败",e);
            }
		}
    
        return toResponsFail("提交失败");
    }

    /**
     * 取消订单
     */
    @ApiOperation(value = "取消订单")
    @PostMapping("cancelOrder")
    @Transactional
    public Object cancelOrder(Integer orderId) {
        try {
        	JSONObject feedbackJson = super.getJsonRequest();
        	
        	if (feedbackJson.get("orderId") == null) {
    			return toResponsFail("订单orderId不能为空");
    		}
        	orderId = Integer.parseInt(feedbackJson.get("orderId").toString());
            OrderVo orderVo = orderService.queryObject(orderId);
            if (orderVo.getOrder_status() == 300) {
                return toResponsFail("已发货，不能取消");
            } else if (orderVo.getOrder_status() == 301) {
                return toResponsFail("已收货，不能取消");
            }
            if (orderVo.getOrder_status() !=0) {
				return toResponsFail("订单初始完毕，该订单不能取消");
			}
            //取消本系统的订单
            /*
             * 0 订单创建成功等待付款，　101订单已取消，　102订单已删除
             * 201订单已付款，等待发货
             * 300订单已发货， 301用户确认收货
             * 401 没有发货，退款　402 已收货，退款退货
             */
            // 需要退款
             if (orderVo.getPay_status() == 1) {
                WechatRefundApiResult result = WechatUtil.wxRefund(orderVo.getId().toString(),
                        0.01, 0.01);
                if (result.getResult_code().equals("SUCCESS")) {
                    if (orderVo.getOrder_status() == 201) {//已付款等待发货
                        orderVo.setOrder_status(401);//退款
                    } else if (orderVo.getOrder_status() == 300) {//已发货
                        orderVo.setOrder_status(402);//已收货，退款退货
                    }
                    orderVo.setPay_status(4);
                    orderService.update(orderVo);
                    //取消京东订单
                    JdOrderService.cancelByOrderKey(orderVo);
                    //回滚子优惠信息
                    orderService.rollbackDiscount(orderVo,2);
                    return toResponsMsgSuccess("取消成功");
                } else {
                    return toResponsObject(400, "取消成失败", "");
                }
            } else {
                orderVo.setOrder_status(101);
                orderService.update(orderVo);
                //取消京东订单
                JdOrderService.cancelByOrderKey(orderVo);
                //回滚子优惠信息
                orderService.rollbackDiscount(orderVo,2);
                return toResponsSuccess("取消成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("提交失败");
    }

    /**
     * 确认收货
     */
    @ApiOperation(value = "确认收货")
    @PostMapping("confirmOrder")
    public Object confirmOrder(Integer orderId) {
        try {
        	JSONObject feedbackJson = super.getJsonRequest();
        	if (feedbackJson.get("orderId") == null) {
    			return toResponsFail("订单orderId不能为空");
    		}
        	orderId = Integer.parseInt(feedbackJson.get("orderId").toString());
            OrderVo orderVo = orderService.queryObject(orderId);
            orderVo.setOrder_status(9);
            orderVo.setShipping_status(2);
            orderVo.setConfirm_time(new Date());
            orderService.update(orderVo);
            return toResponsSuccess("确认收货成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("提交失败");
    }
    /**
     * 查询物流信息
     * @param orderId
     * @return
     */
    @ApiOperation(value = "查询物流")
    @PostMapping("queryLogistics")
    public JSONObject queryLogistics(Long orderId){
    	JSONObject resultObj = new JSONObject();
    	JSONObject feedbackJson = super.getJsonRequest();
    	
    	orderId = Long.parseLong(feedbackJson.get("orderId").toString());
    	OrderVo order = apiOrderMapper.queryObject(orderId);
    	if(order == null){
    		resultObj.put("code", 500);
    		resultObj.put("msg", "查询订单为空");
    		return resultObj;
    	}
    	JSONObject obj = JdOrderService.queryLogistics(order.getOrder_sn(),orderId);
    	return obj;
    }
    
    
    /**
     * 查询代付款
     * @param loginUser
     * @return
     */
	@ApiOperation(value = "查询各订单")
	@PostMapping("queryUnPayments")
//	@IgnoreAuth
	public Object queryUnPayments(@LoginUser UserVo loginUser,
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size) {
		
		JSONObject feedbackJson = super.getJsonRequest();
		
		String orderStatus = feedbackJson.get("orderStatus").toString();

		String[] state = null;
		Map params = new HashMap();
		params.put("user_id", loginUser == null ? null : loginUser.getUserId());
		params.put("page", page);
		params.put("limit", size);
		if ("all".equals(orderStatus)) {// 全部订单
			state = null;
		} else if ("UnPayments".equals(orderStatus)) {// 待付款
			state   = new String[]{"0"};
		} else if ("success".equals(orderStatus)) {// 已完成
			state   = new String[]{"9"};
		} else if ("delivered".equals(orderStatus)) {// 待收货
			state   = new String[]{"200","300","201"};
		} else if ("cancelFlag".equals(orderStatus)) {// 已取消
			state =new String[]{"101","103"};
		}
		params.put("orderStatus", state);
		params.put("sidx", "id");
		params.put("order", "asc");
		// 查询列表数据
		Query query = new Query(params);
		List<OrderVo> orderEntityList = apiOrderMapper.queryOrderList(query);
		int total = orderService.queryOrderTotal(query);
		ApiPageUtils pageUtil = new ApiPageUtils(orderEntityList, total, query.getLimit(), query.getPage());
		//
		Map orderGoodsParam = new HashMap();
		for (OrderVo item : orderEntityList) {
			orderGoodsParam.put("order_id", item.getId());
			// 订单的商品
			List<OrderGoodsVo> goodsList = orderGoodsService.queryList(orderGoodsParam);
			Integer goodsCount = 0;
			for (OrderGoodsVo orderGoodsEntity : goodsList) {
				goodsCount += orderGoodsEntity.getNumber();
				item.setGoodsCount(goodsCount);
			}
		}
		return toResponsSuccess(pageUtil);
	}
	
    /**
     * 查询物流列表
     * @param orderId
     * @return
     */
    @ApiOperation(value = "查询物流列表")
    @PostMapping("queryOrderLogistics")
    public JSONObject queryOrderLogistics(Long orderId){
    	JSONObject resultObj = new JSONObject();
    	JSONObject feedbackJson = super.getJsonRequest();
    	
    	orderId = Long.parseLong(feedbackJson.get("orderId").toString());
    	OrderVo order = apiOrderMapper.queryObject(orderId);
    	if(order == null){
    		resultObj.put("code", 500);
    		resultObj.put("msg", "查询订单为空");
    		return resultObj;
    	}
    	JSONObject obj = JdOrderService.queryOrderLogistics(order.getOrder_sn(),orderId);
    	return obj;
    }
}