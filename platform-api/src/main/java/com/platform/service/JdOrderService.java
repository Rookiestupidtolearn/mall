package com.platform.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.JdOrderMapper;
import com.platform.entity.AddressVo;
import com.platform.entity.JdOrderVo;
import com.platform.entity.OrderVo;
import com.platform.youle.entity.RequestOrderSubmitEntity;
import com.platform.youle.entity.ResponseOrderSubmitEntity;
import com.platform.youle.entity.result.ResponseResultEntity;
import com.platform.youle.service.AbsApiOrderService;

@Service
public class JdOrderService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private JdOrderMapper jdOrderMapper;
	  @Autowired
	    private AbsApiOrderService  apiOrderService;
	
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
//       	entity.setPid_nums("81392458_1");
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
	

}
