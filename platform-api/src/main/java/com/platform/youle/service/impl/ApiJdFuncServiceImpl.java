package com.platform.youle.service.impl;

import java.util.Calendar;
import java.util.Map;

import com.platform.youle.entity.*;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.platform.youle.service.ApiFuncService;
import com.platform.youle.util.HttpUtil;
import com.platform.youle.util.TokenUtil;

@Service
public class ApiJdFuncServiceImpl implements ApiFuncService {
    
	public static String url = "http://www.liguanjia.com/index.php/api";
	
	@Override
	public  ResponseBaseEntity  getAllProductIds(){
	  
	    RequestBaseEntity entity = new RequestBaseEntity();
	    Long timestamp = Calendar.getInstance().getTimeInMillis() ;
		entity.setTimestamp(timestamp.toString());
		entity.setToken(TokenUtil.token);
		entity.setWid(TokenUtil.wid);
	   
		String str = JSON.toJSONString(entity);
		System.out.println("请求参数:"+str);
		try {
			  Map<String,Object> map1 = (Map<String,Object>) JSON.parse(str);
			String result = HttpUtil.post("http://open.fygift.com/api/product/getAllProductIds.php", map1);
			System.out.println("结果:"+result);
			ResponseBaseEntity reponse = JSON.parseObject(result,new TypeReference<ResponseBaseEntity>(){});
			return reponse;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	    
		return null;
	}

	@Override
	public ResponseProductEntity getProductIdsByPage(Integer page) {
		
		RequestProductEntity entity = new RequestProductEntity();
		  Long timestamp = Calendar.getInstance().getTimeInMillis() ;
			entity.setTimestamp(timestamp.toString());
			entity.setToken(TokenUtil.token);
			entity.setWid(TokenUtil.wid);
		    entity.setPage(page);
			String str = JSON.toJSONString(entity);
			System.out.println("请求参数:"+str);
			try {
				  Map<String,Object> map1 = (Map<String,Object>) JSON.parse(str);
				String result = HttpUtil.post("http://open.fygift.com/api/product/getAllProductIds.php", map1);
				System.out.println("结果:"+result);
				ResponseProductEntity reponse = JSON.parseObject(result,new TypeReference<ResponseProductEntity>(){});
				return reponse;
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		return null;
	}

	@Override
	public ResponseProductEntity stock(String pid, String num, String address) {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public ResponseSaleStatusEntity getsaleStatus(Integer pid) {






        return null;
    }
}
