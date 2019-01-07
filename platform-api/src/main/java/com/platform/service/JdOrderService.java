package com.platform.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.platform.dao.ApiGoodsMapper;
import com.platform.dao.ApiOrderGoodsMapper;
import com.platform.dao.JdOrderMapper;
import com.platform.entity.AddressVo;
import com.platform.entity.GoodsVo;
import com.platform.entity.JdOrderVo;
import com.platform.entity.OrderGoodsVo;
import com.platform.entity.OrderLogisticVo;
import com.platform.entity.OrderVo;
import com.platform.utils.DateUtils;
import com.platform.youle.constant.Constants.Urls;
import com.platform.youle.entity.RequestBaseEntity;
import com.platform.youle.entity.RequestOrderSubmitEntity;
import com.platform.youle.entity.RequestOrderTrackEntity;
import com.platform.youle.entity.ResponseBaseEntity;
import com.platform.youle.entity.ResponseBatchSaleEntity;
import com.platform.youle.entity.ResponseOrderSubmitEntity;
import com.platform.youle.entity.ResponseProductEntity;
import com.platform.youle.entity.ResponseProductStockBatchEntity;
import com.platform.youle.entity.ResponseSaleStatusEntity;
import com.platform.youle.entity.result.ResponseResultEntity;
import com.platform.youle.entity.result.ResulGoodsSaleEntity;
import com.platform.youle.entity.result.ResultstockBatchEntity;
import com.platform.youle.service.AbsApiGoodsService;
import com.platform.youle.service.AbsApiOrderService;
import com.platform.youle.util.HttpUtil;
import com.platform.youle.util.MD5util;
import com.platform.youle.util.PropertiesUtil;

@Service
public class JdOrderService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private JdOrderMapper jdOrderMapper;
	@Autowired
	private AbsApiOrderService apiOrderService;
	@Autowired
	private AbsApiGoodsService apiGoodsService;
	@Autowired
	private ApiOrderService orderService;
	@Autowired
	private ApiAddressService  apiAddressService;
	@Autowired
	private ApiGoodsMapper apiGoodsMapper;
	@Autowired
	private ApiOrderGoodsMapper apiOrderGoodsMapper;
	@Autowired
	private ApiOrderGoodsService apiOrderGoodsService;
	
	@Autowired
	private ApiSendSMSService apiSendSMSService;
	
	@Transactional
	public String  jdOrderCreate(OrderVo info){
		 AddressVo addressVo = apiAddressService.queryObject(info.getAddress_id());
		if (addressVo ==null) {
			logger.info("用户的收货地址不能为空，不能下单，用户id"+info.getUser_id());
			return "ERROR";
		}
		if (StringUtils.isEmpty(info.getPid_num()) ) {
			logger.info("订单的商品数量不能为空");
			return "ERROR";
		}
		JdOrderVo jdOrderVo  = new JdOrderVo();
		jdOrderVo.setPidNums(info.getPid_num());
		
	  this.jdOrderSubbmit(addressVo, info, jdOrderVo);
	 
		
		 return "OK";
	}

	/**
	 * 创建订单
	 * 
	 * @param address
	 * @param info
	 * @param jdOrderVo
	 * @return
	 */
	@Transactional
	public Map<String, Object> jdOrderSubbmit(AddressVo addressVo, OrderVo info, JdOrderVo jdOrderVo) {
		Map<String, Object> resultObj = new HashMap<String, Object>();
		// 创建第三方订单开始校验数据
		if (info.getAddress_id() == null) {
			resultObj.put("errno", 1);
			resultObj.put("errmsg", "收货人地址不能为空");
			return resultObj;
		}
		// 校验是否重复下单
		JdOrderVo entityJD = jdOrderMapper.queryByThirdOrder(info.getOrder_sn());
		if (entityJD != null) {
			resultObj.put("errno", 1);
			resultObj.put("errmsg", "订单重复，不能重新下单");
			return resultObj;
		}
		String address = addressVo.getProvince() + "_" + addressVo.getCity() + "_" + addressVo.getCounty();
		// 批量查库存
		Map<String, Object> stockMap = this.stockBatch(info.getPid_num(), address);
		if (!stockMap.get("code").equals("200")) {
			resultObj.put("errno", "100");
			resultObj.put("errmsg", "不可出售");
			return resultObj;
		}
//		 上下架状态
		String pids = "";
		 String pidNums = info.getPid_num();
		 String pidNum[] = pidNums.split(",");
		 for (int i = 0; i < pidNum.length; i++) {
			    String pidNumSingle = pidNum[i].split("_")[0]; //11_11
			    pids+= pidNumSingle+",";
		}
		 
		 if (StringUtils.isNotEmpty(pids)) {
			 pids = pids.substring(0, pids.length() - 1);
		}
		 
		Map<String, Object> saleStatusMap = this.checkBatchSaleStatus(pids);
		if (!saleStatusMap.get("code").equals("200")) {
			resultObj.put("errno", "100");
			resultObj.put("errmsg", "不可出售");
			return resultObj;
		}
		
		jdOrderVo.setShopUserId(Integer.parseInt(info.getUser_id().toString()));
		jdOrderVo.setOrderStatus(0);
		jdOrderVo.setThirdOrder(info.getOrder_sn());
		jdOrderVo.setReceiverName(info.getConsignee());
		jdOrderVo.setProvince(addressVo.getProvince());
		jdOrderVo.setCity(addressVo.getCity());
		jdOrderVo.setCounty(addressVo.getCounty());
		jdOrderVo.setTown(addressVo.getTown());
		jdOrderVo.setAddress(info.getAddress());
		jdOrderVo.setMobile(info.getMobile());

		jdOrderMapper.save(jdOrderVo);
		// 处理订单
		RequestOrderSubmitEntity entity = new RequestOrderSubmitEntity();
		entity.setThirdOrder(jdOrderVo.getThirdOrder());
		entity.setPid_nums(jdOrderVo.getPidNums());
		entity.setReceiverName(jdOrderVo.getReceiverName());
		entity.setMobile(jdOrderVo.getMobile());
		entity.setAddress(jdOrderVo.getAddress());
		entity.setProvince(jdOrderVo.getProvince());
		entity.setCity(jdOrderVo.getCity());
		entity.setCounty(jdOrderVo.getCounty());
		entity.setTown(jdOrderVo.getTown());
		JdOrderVo jdOrder = jdOrderMapper.queryByThirdOrder(jdOrderVo.getThirdOrder());
	
		 info.setOrder_status(201);
		  orderService.update(info);
		  Map<String, Object> paMap =  new HashMap<>();
		  paMap.put("order_id", info.getId());
		  List<OrderGoodsVo> orGoodsVos = apiOrderGoodsService.queryList(paMap);
		  if (!CollectionUtils.isEmpty(orGoodsVos)) {
			  for (OrderGoodsVo orderGoodsVo :orGoodsVos) {
				  orderGoodsVo.setGoodStatus(300); //300等待收货
				  apiOrderGoodsService.update(orderGoodsVo);
			}
			  
		  }
			//订单已提交给三方
		ResponseOrderSubmitEntity response = apiOrderService.submit(entity);
		if (response.getRESPONSE_STATUS().equals("false")) {
			resultObj.put("errno", 1);
			resultObj.put("errmsg", response.getERROR_MESSAGE());
			jdOrder.setOrderStatus(5);
		} else {
			ResponseResultEntity resultEntity = response.getRESULT_DATA();
			jdOrder.setOrderKey(resultEntity.getOrder_key());
			jdOrder.setOrderProductPrice(resultEntity.getOrder_product_price());
			jdOrder.setOrderShipmentFee(resultEntity.getOrder_product_price());
			jdOrder.setOrderTotalPrice(resultEntity.getOrder_total_price());
			jdOrder.setOrderSplit(resultEntity.getOrder_split());
			resultObj.put("errno", 0);
			resultObj.put("errmsg", "创建第三方订单成功");
			jdOrder.setOrderStatus(9);
		}

		// 订单处理完的操作
		jdOrder.setResponseStatus(response.getRESPONSE_STATUS());
		jdOrder.setErrorMessage(response.getERROR_MESSAGE());
		jdOrder.setErrorCode(response.getERROR_CODE());
		jdOrderMapper.update(jdOrder);
		
		//发货提醒
		String  smsTemplet = PropertiesUtil.getValue("doubao.properties","sendGoodsSmsTemplet");
		String content = MessageFormat.format(smsTemplet,jdOrder.getThirdOrder());
		apiSendSMSService.sendSms(jdOrder.getMobile(), content);
		
		
		return resultObj;
	}

	/**
	 * 取消订单
	 * 
	 * @param shopOder
	 * @return
	 */
	public Map<String, Object> cancelByOrderKey(OrderVo shopOder) {
		Map<String, Object> map = new HashMap<>();

		JdOrderVo jdOrderVo = jdOrderMapper.queryByThirdOrder(shopOder.getOrder_sn());
		ResponseBaseEntity response = null;
		if (jdOrderVo == null) {
			logger.info("不存在京东的订单，订单编号：" + shopOder.getOrder_sn());
		} else {
			Gson gson = new Gson();
			response = apiOrderService.cancelByOrderKey(jdOrderVo.getThirdOrder(), jdOrderVo.getOrderKey());
			jdOrderVo.setResponseStatus(response.getRESPONSE_STATUS());
			jdOrderVo.setResultData(gson.toJson(response));
			if (response.getRESPONSE_STATUS().equals("true")) {
				// 处理取消成功的逻辑
				jdOrderVo.setOrderStatus(1);// 订单已经取消
			} else {
				jdOrderVo.setErrorCode(response.getERROR_CODE());
				jdOrderVo.setErrorMessage(response.getERROR_MESSAGE());

			}
			jdOrderMapper.update(jdOrderVo);

		}

		return map;

	}

	/**
	 * 校验单个库存
	 * 
	 * @param pid
	 * @param num
	 * @param address
	 * @return
	 */
	public Map<String, Object> checkStockSingle(String pid, Integer num, String address) {
		Map<String, Object> map = new HashMap<>();
		map.put("code", "200");
		map.put("msg", "有库存");
		ResponseProductEntity response = apiGoodsService.stock(pid, num, address);
		if (response.getRESPONSE_STATUS().equals("false")) {
			map.put("code", "500");
			map.put("msg", "查询库存失败");
			return map;
		}
		List<ResultstockBatchEntity> list = response.getRESULT_DATA();
		for (ResultstockBatchEntity entity : list) {
			if (!entity.getStock_status()) {
				map.put("code", "500");
				map.put("msg", "库存不足");
				return map;
			}
		}
		return map;
	}

	/**
	 * 查询上下架状态
	 * 
	 * @param pid
	 * @return
	 */
	public Map<String, Object> checkSaleStatusSingle(Integer pid) {
		Map<String, Object> map = new HashMap<>();
		map.put("code", "200");
		map.put("msg", "可售");
		ResponseSaleStatusEntity response = apiGoodsService.getsaleStatus(pid);
		if (response.getRESPONSE_STATUS().equals("false")) {
			map.put("code", "500");
			map.put("msg", "查询上下架状态");
			return map;
		}
		if (response.getRESULT_DATA().get("status").equals(false)) {
			map.put("code", "500");
			map.put("msg", "商品已经下架");
			return map;
		}
		return map;
	}

	/**
	 * 校验批量三方的库存
	 * 
	 * @param pid_nums
	 * @param address
	 * @return
	 */
	public Map<String, Object> stockBatch(String pid_nums, String address) {
		Map<String, Object> map = new HashMap<>();
		map.put("code", "200");
		map.put("msg", "有库存");
		ResponseProductStockBatchEntity response = apiGoodsService.stockBatch(pid_nums, address);
		if (response.getRESPONSE_STATUS().equals("false")) {
			map.put("code", "500");
			map.put("msg", response.getERROR_MESSAGE());
			return map;
		}

		List<ResultstockBatchEntity> list = response.getRESULT_DATA();
		for (ResultstockBatchEntity entity : list) {
			if (!entity.getStock_status()) {
				map.put("code", "500");
				map.put("msg", "库存不足");
				return map;
			}
		}

		return map;
	}

	/**
	 * 批量校验是上下架状态
	 * 
	 * @param pids
	 * @return
	 */
	public Map<String, Object> checkBatchSaleStatus(String pids) {
		Map<String, Object> map = new HashMap<>();
		map.put("code", "200");
		map.put("msg", "可出售");
		ResponseBatchSaleEntity response = apiGoodsService.batchSaleStatus(pids);
		if (response.getRESPONSE_STATUS().equals("false")) {
			map.put("code", "500");
			map.put("msg", response.getERROR_MESSAGE());
			return map;
		}
		List<ResulGoodsSaleEntity> list = response.getRESULT_DATA();
		for (ResulGoodsSaleEntity entity : list) {
			if (!entity.getStatus()) {
				map.put("code", "500");
				map.put("msg", "已下架");
				return map;
			}
		}

		return map;
	}

	public JSONObject queryLogistics(String orderSn,Long orderId) {
		logger.info("【查询三方物流信息开始】,本地订单号:"  + orderSn);
		JSONObject resultObj = new JSONObject();
		JdOrderVo jdOrder = jdOrderMapper.queryByThirdOrder(orderSn);
		if (jdOrder == null) {
			resultObj.put("code", 500);
			resultObj.put("msg", "三方订单数据为空");
			return resultObj;
		}
		Pattern pattern = Pattern
				.compile("[0-9]{1,2}[月][0-9]{1,2}[日][0-9]{1,2}[:][0-9]{1,2}[-][0-9]{1,2}[:][0-9]{1,2}");
		String arrivalTime = "";
		
		List<Map<String,Object>> logistics = new ArrayList<>();
		String orderKey = jdOrder.getOrderKey();
		RequestOrderTrackEntity entity = new RequestOrderTrackEntity();
		initRequestParam(entity);
		entity.setOrderKey(orderKey);
		
		logger.info("2.5订单物流信息接口]入参："+JSONObject.toJSONString(entity));
        String result = "";
		try {
			result = HttpUtil.post(Urls.base_prod_url+Urls.systemOrderTrack, objectToMap(entity));
			logger.info("[2.5订单物流信息接口]出参："+result);
		} catch (Exception e) {
			logger.error("【查询三方物流信息异常】,本地订单号:"  + orderSn,e);
		}
 
		JSONObject obj = JSONObject.parseObject(result);
		if(!Boolean.parseBoolean(obj.get("RESPONSE_STATUS").toString())){
			logger.info("【查询三方物流信息RESPONSE_STATUS为false】");
			resultObj.put("code", 500);
			resultObj.put("msg", "三方订单数据为空");
			return resultObj;
		}
		if(obj.get("RESULT_DATA") == null){
			logger.info("【查询三方物流信息】RESULT_DATA为空");
			resultObj.put("code", 500);
			resultObj.put("msg", "三方订单数据RESULT_DATA为空");
			return resultObj;
		}
		JSONObject resultData = JSONObject.parseObject(obj.get("RESULT_DATA").toString());
		resultObj.put("order_key", resultData.get("third_order") == null ? "" : resultData.get("third_order"));//三方订单
		resultObj.put("shipment_name", resultData.get("shipment_name") == null ? "" : resultData.get("shipment_name"));//快递公司
		resultObj.put("shipment_order", resultData.get("shipment_order") == null ? "" : resultData.get("shipment_order"));//快递单号
		resultObj.put("status", resultData.get("status") == null ? "" : resultData.get("status"));//物流状态, receive:揽件,transit:运输中, signed:已签收, refuse:拒收, other:其他
		resultObj.put("last_modify_time", resultData.get("last_modify_time") == null ? "" : resultData.get("last_modify_time"));//快递公司
		if(resultData.get("contents") == null){
			logger.info("【查询三方物流信息】没有相关物流信息");
			resultObj.put("code", 500);
			resultObj.put("msg", "三方订单数据没有相关物流信息");
			return resultObj;
		}
		JSONArray array = JSONArray.parseArray(resultData.get("contents").toString());
		for(int i = 0;i<array.size();i++){
			JSONObject objs = JSONObject.parseObject(array.getString(i));
			Map<String,Object> map = new HashMap<>();
			map.put("time", objs.get("time"));
			map.put("description", objs.get("description"));
			logistics.add(map);
			if(i == 2){
				Matcher matcher = pattern.matcher(objs.get("description").toString());
				String dateStr = "";
				if (matcher.find()) {
					dateStr = matcher.group(0);
				}
				arrivalTime = dateStr.toString();
			}
		}
		List<Map<String,Object>> goodList = queryOrderGoods(orderId);
		resultObj.put("goods", goodList);//物流提供商品名称/商品url
		resultObj.put("arrivalTime", arrivalTime);
		resultObj.put("logistics", logistics);
		return resultObj;
	}

	/**
	 * 初始请求参数
	 * 
	 * @param entity
	 */
	public void initRequestParam(RequestBaseEntity entity) {
		Long currentTime = Calendar.getInstance().getTimeInMillis();
		entity.setWid(PropertiesUtil.getValue("youle.properties", "wid"));
		entity.setTimestamp(currentTime.toString());
		String token = getToken(currentTime);
		entity.setToken(token);
	}

	private String getToken(Long currentTime) {
		String token = "";
		StringBuffer tokenStr = new StringBuffer("");
		tokenStr.append(PropertiesUtil.getValue("youle.properties", "wid"));
		tokenStr.append(PropertiesUtil.getValue("youle.properties", "accessToken"));
		tokenStr.append(currentTime);
		token = MD5util.encodeByMD5(tokenStr.toString()).toUpperCase();
		return token;
	}
	  /**
     * 实体转map
     * @param entity
     * @return
     * @throws Exception
     */
    public Map<String,Object>  objectToMap(RequestBaseEntity entity) throws Exception{
            String str = JSON.toJSONString(entity);
            Map<String,Object> map = (Map<String,Object>) JSON.parse(str);
            return map;
    }

    /**
     * 查看物流提供商品名称和url
     * @param orderId
     * @return
     */
    public List<Map<String,Object>> queryOrderGoods(Long orderId){
    	List<Map<String,Object>> goodLists = new ArrayList<>();
    	List<OrderGoodsVo> orderGoods = apiOrderGoodsMapper.queryOrderLogisticGoods(Integer.parseInt(orderId.toString()));
    	if(!CollectionUtils.isEmpty(orderGoods)){
    		for(OrderGoodsVo order : orderGoods){
    			GoodsVo good = apiGoodsMapper.queryObject(order.getGoods_id());
    			Map<String,Object> map = new HashMap<>();
    			map.put("goodsName", good.getName());
    			map.put("img", good.getList_pic_url());
    			goodLists.add(map);
    		}
    	}
    	return goodLists;
    }
    
    
	public JSONObject queryOrderLogistics(String orderSn,Long orderId) {
		logger.info("【查询三方物流信息开始】,本地订单号:"  + orderSn);
		List<OrderLogisticVo> vos = new ArrayList<OrderLogisticVo>();
		OrderLogisticVo logistic = new OrderLogisticVo();
		JSONObject resultObj = new JSONObject();
		JdOrderVo jdOrder = jdOrderMapper.queryByThirdOrder(orderSn);
		if (jdOrder == null) {
			resultObj.put("code", 500);
			resultObj.put("msg", "三方订单数据为空");
			return resultObj;
		}
		Pattern pattern = Pattern
				.compile("[0-9]{1,2}[月][0-9]{1,2}[日][0-9]{1,2}[:][0-9]{1,2}[-][0-9]{1,2}[:][0-9]{1,2}");
		String arrivalTime = "";
		
		String orderKey = jdOrder.getOrderKey();
		RequestOrderTrackEntity entity = new RequestOrderTrackEntity();
		initRequestParam(entity);
		entity.setOrderKey(orderKey);
		
		logger.info("2.5订单物流信息接口]入参："+JSONObject.toJSONString(entity));
        String result = "";
		try {
			result = HttpUtil.post(Urls.base_prod_url+Urls.systemOrderTrack, objectToMap(entity));
			logger.info("[2.5订单物流信息接口]出参："+result);
		} catch (Exception e) {
			logger.error("【查询三方物流信息异常】,本地订单号:"  + orderSn,e);
		}
 
		JSONObject obj = JSONObject.parseObject(result);
		if(!Boolean.parseBoolean(obj.get("RESPONSE_STATUS").toString())){
			logger.info("【查询三方物流信息RESPONSE_STATUS为false】");
			resultObj.put("code", 500);
			resultObj.put("msg", "三方订单数据为空");
			return resultObj;
		}
		if(obj.get("RESULT_DATA") == null){
			logger.info("【查询三方物流信息】RESULT_DATA为空");
			resultObj.put("code", 500);
			resultObj.put("msg", "三方订单数据RESULT_DATA为空");
			return resultObj;
		}
		JSONObject resultData = JSONObject.parseObject(obj.get("RESULT_DATA").toString());
		resultObj.put("status", resultData.get("status") == null ? "" : resultData.get("status"));//物流状态, receive:揽件,transit:运输中, signed:已签收, refuse:拒收, other:其他
		if(resultData.get("contents") == null){
			logger.info("【查询三方物流信息】没有相关物流信息");
			resultObj.put("code", 500);
			resultObj.put("msg", "三方订单数据没有相关物流信息");
			return resultObj;
		}
		JSONArray array = JSONArray.parseArray(resultData.get("contents").toString());
		for(int i = 0;i<array.size();i++){
			JSONObject objs = JSONObject.parseObject(array.getString(i));
			if(i == 2){
				Matcher matcher = pattern.matcher(objs.get("description").toString());
				String dateStr = "";
				if (matcher.find()) {
					dateStr = matcher.group(0);
				}
				arrivalTime = dateStr.toString();
			}
		}
		List<Map<String,Object>> goodList = queryOrderGoods(orderId);
		logistic.setArrivalTime(arrivalTime);
		logistic.setGoodName(goodList.get(0) == null ? "" : goodList.get(0).get("goodsName").toString());
		logistic.setGoodUrl(goodList.get(0) == null ? "" : goodList.get(0).get("img").toString());
		logistic.setOrderId(orderId.toString());
		logistic.setShipmentName(resultData.get("shipment_name") == null ? "" : resultData.get("shipment_name").toString());
		logistic.setShipmentOrder(resultData.get("shipment_order") == null ? "" : resultData.get("shipment_order").toString());
		vos.add(logistic);
		resultObj.put("orderLogistics", vos);
		return resultObj;
	}
    
    
}
