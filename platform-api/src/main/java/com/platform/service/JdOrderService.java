package com.platform.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.platform.dao.JdOrderMapper;
import com.platform.entity.AddressVo;
import com.platform.entity.JdOrderVo;
import com.platform.entity.OrderVo;
import com.platform.youle.entity.RequestOrderSubmitEntity;
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

@Service
public class JdOrderService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	   @Autowired
	   private JdOrderMapper jdOrderMapper;
	    @Autowired
	    private AbsApiOrderService  apiOrderService;
	    @Autowired
	    private AbsApiGoodsService apiGoodsService;
	    
	 /**
	  * 创建订单 
	  * @param address
	  * @param info
	  * @param jdOrderVo
	  * @return
	  */
	public  Map<String, Object> jdOrderSubbmit(AddressVo address, OrderVo  info,JdOrderVo jdOrderVo){
		   Map<String, Object> resultObj = new HashMap<String, Object>();
		  //创建第三方订单开始校验数据
		   if (info.getAddress_id() == null) {
			     resultObj.put("errno", 1);
	             resultObj.put("errmsg", "收货人地址不能为空");
	            return resultObj;
		  }
		  //校验是否重复下单
		   JdOrderVo entityJD =   jdOrderMapper.queryByThirdOrder(info.getOrder_sn());
		   if (entityJD !=null) {
			     resultObj.put("errno", 1);
	             resultObj.put("errmsg", "订单重复，不能重新下单");
	            return resultObj;
		  }
		   jdOrderVo.setShopUserId(Integer.parseInt(info.getUser_id().toString()));
		   jdOrderVo.setOrderStatus(0);
		   jdOrderVo.setThirdOrder(info.getOrder_sn());
		   jdOrderVo.setReceiverName(info.getConsignee());
		   jdOrderVo.setProvince(address.getProvince());
		   jdOrderVo.setCity(address.getCity());
		   jdOrderVo.setCounty(address.getCounty());
		   jdOrderVo.setTown(address.getTown());
		   jdOrderVo.setAddress(info.getAddress());
		   jdOrderVo.setMobile(info.getMobile());
		
           jdOrderMapper.save(jdOrderVo);
           //处理订单
         RequestOrderSubmitEntity entity = new RequestOrderSubmitEntity();
			entity.setThirdOrder(jdOrderVo.getThirdOrder());
			entity.setPid_nums(jdOrderVo.getPidNums());
			entity.setReceiverName(jdOrderVo.getReceiverName());
			entity.setMobile(jdOrderVo.getMobile());
			entity.setAddress(jdOrderVo.getAddress());
			entity.setProvince(Integer.parseInt(jdOrderVo.getProvince()));
			entity.setCity(Integer.parseInt(jdOrderVo.getCity()));
			entity.setCounty(Integer.parseInt(jdOrderVo.getCounty()));
			entity.setTown(Integer.parseInt(jdOrderVo.getTown()));
        	JdOrderVo  jdOrder = jdOrderMapper.queryByThirdOrder(jdOrderVo.getThirdOrder());

       	ResponseOrderSubmitEntity response  = apiOrderService.submit(entity);
         if (response.getRESPONSE_STATUS().equals("false")) {
        	  resultObj.put("errno", 1);
              resultObj.put("errmsg", response.getERROR_MESSAGE());  
              jdOrder.setOrderStatus(5);
		}else {
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
     
        //订单处理完的操作
       
         jdOrder.setResponseStatus(response.getRESPONSE_STATUS());   
         jdOrder.setErrorMessage(response.getERROR_MESSAGE());
         jdOrder.setErrorCode(response.getERROR_CODE());
        
         jdOrderMapper.update(jdOrder);
		return resultObj;
	}
	
	/**
	 * 取消订单
	 * @param shopOder
	 * @return
	 */
	public Map<String, Object> cancelByOrderKey(OrderVo shopOder){
		Map<String, Object> map =  new HashMap<>();
		
		JdOrderVo jdOrderVo =	jdOrderMapper.queryByThirdOrder(shopOder.getOrder_sn());
		ResponseBaseEntity  response = null;
		if (jdOrderVo == null) {
			logger.info("不存在京东的订单，订单编号："+shopOder.getOrder_sn());
		}else {
			Gson gson = new Gson();
			response = apiOrderService.cancelByOrderKey(jdOrderVo.getThirdOrder(),jdOrderVo.getOrderKey());
			jdOrderVo.setResponseStatus(response.getRESPONSE_STATUS());
			jdOrderVo.setResultData(gson.toJson(response));
			if (response.getRESPONSE_STATUS().equals("true")) {
			 	 //处理取消成功的逻辑
				jdOrderVo.setOrderStatus(1);//订单已经取消
			}else {
			jdOrderVo.setErrorCode(response.getERROR_CODE());
			jdOrderVo.setErrorMessage(response.getERROR_MESSAGE());
		     
			}
		   jdOrderMapper.update(jdOrderVo);			
			
		}
		
		return map;
		
	}
	/**
	 * 校验单个库存 
	 * @param pid
	 * @param num
	 * @param address
	 * @return
	 */
	public Map<String, Object> checkStockSingle(String pid,Integer num,String address){
		Map<String, Object> map =  new HashMap<>();
		map.put("code", "200");
		map.put("msg", "有库存");
		ResponseProductEntity response = apiGoodsService.stock(pid,num,address);
		if (response.getRESPONSE_STATUS().equals("false")) {
			map.put("code", "500");
			map.put("msg", "查询库存失败");
			return map;
		}
		List<ResultstockBatchEntity> list = response.getRESULT_DATA();
		for(ResultstockBatchEntity entity : list){
			 if ( !entity.getStock_status()) {
					map.put("code", "500");
					map.put("msg", "库存不足");
					return map;
			}
		}
		 return map;
	}
	
	/**
	 * 查询上下架状态
	 * @param pid
	 * @return
	 */
	public Map<String, Object> checkSaleStatusSingle(Integer pid){
		Map<String, Object> map =  new HashMap<>();
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
	 * @param pid_nums
	 * @param address
	 * @return
	 */
	public  Map<String, Object> stockBatch(String pid_nums,String address){
		Map<String, Object> map =  new HashMap<>();
		map.put("code", "200");
		map.put("msg", "有库存");
		ResponseProductStockBatchEntity response =  apiGoodsService.stockBatch(pid_nums, address);
		if (response.getRESPONSE_STATUS().equals("false")) {
			map.put("code", "500");
			map.put("msg", response.getERROR_MESSAGE());
			return map;
		}
       
		List<ResultstockBatchEntity> list = response.getRESULT_DATA();
		for(ResultstockBatchEntity entity : list){
			 if ( !entity.getStock_status()) {
					map.put("code", "500");
					map.put("msg", "库存不足");
					return map;
			}
		}
		
		 return map;
	}
	
	/**
	 * 批量校验是上下架状态
	 * @param pids
	 * @return
	 */
	public Map<String, Object>  checkBatchSaleStatus(String pids){
		Map<String, Object> map =  new HashMap<>();
		map.put("code", "200");
		map.put("msg", "可出售");
		ResponseBatchSaleEntity  response = apiGoodsService.batchSaleStatus(pids);
		if (response.getRESPONSE_STATUS().equals("false")) {
			map.put("code", "500");
			map.put("msg", response.getERROR_MESSAGE());
			return map;
		}
		List<ResulGoodsSaleEntity> list = response.getRESULT_DATA();
		for (ResulGoodsSaleEntity entity: list) {
			 if ( !entity.getStatus()) {
					map.put("code", "500");
					map.put("msg", "库存不足");
					return map;
			}
		}
		
		
		 return map;
	}
 	
}
