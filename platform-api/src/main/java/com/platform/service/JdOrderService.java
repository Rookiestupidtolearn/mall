package com.platform.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.JdOrderMapper;
import com.platform.entity.JdOrderVo;
import com.platform.entity.OrderVo;
import com.platform.youle.entity.RequestOrderSubmitEntity;
import com.platform.youle.entity.ResponseOrderSubmitEntity;
import com.platform.youle.service.AbsApiOrderService;

@Service
public class JdOrderService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private JdOrderMapper jdOrderMapper;
	  @Autowired
	    private AbsApiOrderService  apiOrderService;
	
	public  Map<String, Object> jdOrderSubbmit(OrderVo  info,JdOrderVo jdOrderVo){
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
		   jdOrderVo.setProvince("5");
		   jdOrderVo.setCity("239");
		   jdOrderVo.setCounty("48379");
//		   jdOrderVo.setTown(town);
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
//       	entity.setPid_nums("81392_1");
       	ResponseOrderSubmitEntity response  = apiOrderService.submit(entity);
         if (response.getRESPONSE_STATUS().equals("false")) {
        	  resultObj.put("errno", 1);
              resultObj.put("errmsg", response.getERROR_MESSAGE());  
              return resultObj;
		}
        //订单处理成功后的操作
         JdOrderVo  entityNew = jdOrderMapper.queryByThirdOrder(jdOrderVo.getThirdOrder());
         entityNew.setResponseStatus(response.getRESPONSE_STATUS());   
         entityNew.setErrorMessage(response.getERROR_MESSAGE());
         entityNew.setErrorCode(response.getERROR_CODE());
         
         jdOrderMapper.update(entityNew);
         
           resultObj.put("errno", 0);
           resultObj.put("errmsg", "创建第三方订单成功");  
		return resultObj;
	}
	

}
