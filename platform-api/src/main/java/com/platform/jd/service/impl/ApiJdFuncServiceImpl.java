package com.platform.jd.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.platform.jd.entity.JdRequestCheckAreaLimitEntity;
import com.platform.jd.entity.JdRequestConfirmOrderEntity;
import com.platform.jd.entity.JdRequestGetAdressEntity;
import com.platform.jd.entity.JdRequestGetApiTokenEntity;
import com.platform.jd.entity.JdRequestGetCommentSummarysEntity;
import com.platform.jd.entity.JdRequestGetNewStockByIdEntity;
import com.platform.jd.entity.JdRequestGetNewStockByIdSkuNumsEntity;
import com.platform.jd.entity.JdRequestGetOrderSnEntity;
import com.platform.jd.entity.JdRequestGetOrderSonEntity;
import com.platform.jd.entity.JdRequestGetPriceEntity;
import com.platform.jd.entity.JdRequestGetSkuEntity;
import com.platform.jd.entity.JdRequestGetTokenSafeCodeEntity;
import com.platform.jd.entity.JdRequestJdLogisticsEntity;
import com.platform.jd.entity.JdRequestOrderSubmitEntity;
import com.platform.jd.entity.JdRequestOrderTrackEntity;
import com.platform.jd.entity.JdRequestSelectOrderEntity;
import com.platform.jd.entity.JdRequestSelectRechargeEntity;
import com.platform.jd.entity.JdRequestSelectSpendEntity;
import com.platform.jd.entity.JdRequestSkuDetailEntity;
import com.platform.jd.entity.JdRequestSkuImageEntity;
import com.platform.jd.entity.JdRequestSkuStateEntity;
import com.platform.jd.entity.JdResponseCheckAreaLimitEntity;
import com.platform.jd.entity.JdResponseConfirmOrderEntity;
import com.platform.jd.entity.JdResponseGetAdressEntity;
import com.platform.jd.entity.JdResponseGetApiTokenEntity;
import com.platform.jd.entity.JdResponseGetCommentSummarysEntity;
import com.platform.jd.entity.JdResponseGetNewStockByIdEntity;
import com.platform.jd.entity.JdResponseGetOrderSnEntity;
import com.platform.jd.entity.JdResponseGetOrderSonEntity;
import com.platform.jd.entity.JdResponseGetPriceEntity;
import com.platform.jd.entity.JdResponseGetSkuEntity;
import com.platform.jd.entity.JdResponseGetTokenSafeCodeEntity;
import com.platform.jd.entity.JdResponseJdLogisticsEntity;
import com.platform.jd.entity.JdResponseOrderSubmitEntity;
import com.platform.jd.entity.JdResponseOrderTrackEntity;
import com.platform.jd.entity.JdResponseSelectOrderEntity;
import com.platform.jd.entity.JdResponseSelectRechargeEntity;
import com.platform.jd.entity.JdResponseSelectSpendEntity;
import com.platform.jd.entity.JdResponseSkuDetailEntity;
import com.platform.jd.entity.JdResponseSkuImageEntity;
import com.platform.jd.entity.JdResponseSkuStateEntity;
import com.platform.jd.service.ApiJdFuncService;
import com.platform.jd.util.HttpUtil;

public class ApiJdFuncServiceImpl implements ApiJdFuncService {
    
	public static String url = "http://www.liguanjia.com/index.php/api";
	
	@Override
	public JdResponseGetTokenSafeCodeEntity getTokenSafeCode(String func, String username, String password,
			String api_name, String api_secret) {
		
		JdRequestGetTokenSafeCodeEntity entity = new JdRequestGetTokenSafeCodeEntity();
		entity.setFunc(func);
	    entity.setUsername(username);
	    entity.setPassword(password);
	    entity.setApi_name(api_name);
	    entity.setApi_secret(api_secret);
		String str = JSON.toJSONString(entity);
		System.out.println("请求参数:"+str);
		try {
			String result = HttpUtil.post(url, str);
			System.out.println("结果:"+result);
			JdResponseGetTokenSafeCodeEntity reponse = JSON.parseObject(result,new TypeReference<JdResponseGetTokenSafeCodeEntity>(){});
			return reponse;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public JdResponseGetApiTokenEntity getApiToken(String func, String username, String password, String api_name,
			String api_secret, String safecode) {
		JdRequestGetApiTokenEntity entity = new JdRequestGetApiTokenEntity();
		entity.setFunc(func);
	    entity.setUsername(username);
	    entity.setPassword(password);
	    entity.setApi_name(api_name);
	    entity.setApi_secret(api_secret);
	    entity.setSafecode(safecode);
		String str = JSON.toJSONString(entity);
		System.out.println("请求参数:"+str);
		try {
			String result = HttpUtil.post(url, str);
			System.out.println("结果:"+result);
			JdResponseGetApiTokenEntity reponse = JSON.parseObject(result,new TypeReference<JdResponseGetApiTokenEntity>(){});
			return reponse;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public JdResponseGetSkuEntity getSku(String func, String token, String page) {
		JdRequestGetSkuEntity entity = new JdRequestGetSkuEntity();
		entity.setFunc(func);
	    entity.setToken(token);
	    entity.setPage(Integer.parseInt(page));
		String str = JSON.toJSONString(entity);
		System.out.println("请求参数:"+str);
		try {
			String result = HttpUtil.post(url, str);
			System.out.println("结果:"+result);
			JdResponseGetSkuEntity reponse = JSON.parseObject(result,new TypeReference<JdResponseGetSkuEntity>(){});
			return reponse;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public JdResponseSkuDetailEntity skuDetail(String func, String token, String sku) {
		JdRequestSkuDetailEntity entity = new JdRequestSkuDetailEntity();
		entity.setFunc(func);
	    entity.setToken(token);
	    entity.setSku(sku);
		String str = JSON.toJSONString(entity);
		System.out.println("请求参数:"+str);
		try {
			String result = HttpUtil.post(url, str);
			System.out.println("结果:"+result);
			JdResponseSkuDetailEntity reponse = JSON.parseObject(result,new TypeReference<JdResponseSkuDetailEntity>(){});
			return reponse;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public JdResponseSkuStateEntity skuState(String func, String token, String sku) {
		JdRequestSkuStateEntity entity = new JdRequestSkuStateEntity();
		entity.setFunc(func);
	    entity.setToken(token);
	    entity.setSku(sku);
		String str = JSON.toJSONString(entity);
		System.out.println("请求参数:"+str);
		try {
			String result = HttpUtil.post(url, str);
			System.out.println("结果:"+result);
			JdResponseSkuStateEntity reponse = JSON.parseObject(result,new TypeReference<JdResponseSkuStateEntity>(){});
			return reponse;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public JdResponseSkuImageEntity skuImage(String func, String token, String sku) {
		JdRequestSkuImageEntity entity = new JdRequestSkuImageEntity();
		entity.setFunc(func);
	    entity.setToken(token);
	    entity.setSku(sku);
		String str = JSON.toJSONString(entity);
		System.out.println("请求参数:"+str);
		try {
			String result = HttpUtil.post(url, str);
			System.out.println("结果:"+result);
			JdResponseSkuImageEntity reponse = JSON.parseObject(result,new TypeReference<JdResponseSkuImageEntity>(){});
			return reponse;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public JdResponseGetCommentSummarysEntity getCommentSummarys(String func, String token, String sku) {
		JdRequestGetCommentSummarysEntity entity = new JdRequestGetCommentSummarysEntity();
		entity.setFunc(func);
	    entity.setToken(token);
	    entity.setSku(sku);
		String str = JSON.toJSONString(entity);
		System.out.println("请求参数:"+str);
		try {
			String result = HttpUtil.post(url, str);
			System.out.println("结果:"+result);
			JdResponseGetCommentSummarysEntity reponse = JSON.parseObject(result,new TypeReference<JdResponseGetCommentSummarysEntity>(){});
			return reponse;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public JdResponseGetAdressEntity getAdress(String func, String token, String parent_id) {
		JdRequestGetAdressEntity entity = new JdRequestGetAdressEntity();
		entity.setFunc(func);
	    entity.setToken(token);
	    entity.setParent_id(Integer.parseInt(parent_id));
		String str = JSON.toJSONString(entity);
		System.out.println("请求参数:"+str);
		try {
			String result = HttpUtil.post(url, str);
			System.out.println("结果:"+result);
			JdResponseGetAdressEntity reponse = JSON.parseObject(result,new TypeReference<JdResponseGetAdressEntity>(){});
			return reponse;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public JdResponseGetPriceEntity getPrice(String func, String token, String sku) {
		JdRequestGetPriceEntity entity = new JdRequestGetPriceEntity();
		entity.setFunc(func);
	    entity.setToken(token);
	    entity.setSku(sku);
		String str = JSON.toJSONString(entity);
		System.out.println("请求参数:"+str);
		try {
			String result = HttpUtil.post(url, str);
			System.out.println("结果:"+result);
			JdResponseGetPriceEntity reponse = JSON.parseObject(result,new TypeReference<JdResponseGetPriceEntity>(){});
			return reponse;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public JdResponseGetNewStockByIdEntity getNewStockById(String func, String token, String area, List<JdRequestGetNewStockByIdSkuNumsEntity> skuNums) {
		JdRequestGetNewStockByIdEntity entity = new JdRequestGetNewStockByIdEntity();
		entity.setFunc(func);
	    entity.setToken(token);
	    entity.setArea(area);
	    entity.setSkuNums(skuNums);
		String str = JSON.toJSONString(entity);
		System.out.println("请求参数:"+str);
		try {
			String result = HttpUtil.post(url, str);
			System.out.println("结果:"+result);
			JdResponseGetNewStockByIdEntity reponse = JSON.parseObject(result,new TypeReference<JdResponseGetNewStockByIdEntity>(){});
			return reponse;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public JdResponseCheckAreaLimitEntity checkAreaLimit(String func, String token, String area, String skuIds) {
		JdRequestCheckAreaLimitEntity entity = new JdRequestCheckAreaLimitEntity();
		entity.setFunc(func);
	    entity.setToken(token);
	    entity.setArea(area);
	    entity.setSkuIds(skuIds);
		String str = JSON.toJSONString(entity);
		System.out.println("请求参数:"+str);
		try {
			String result = HttpUtil.post(url, str);
			System.out.println("结果:"+result);
			JdResponseCheckAreaLimitEntity reponse = JSON.parseObject(result,new TypeReference<JdResponseCheckAreaLimitEntity>(){});
			return reponse;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public JdResponseGetOrderSnEntity getOrderSn(String func, String token, String thirdsn) {
		JdRequestGetOrderSnEntity entity = new JdRequestGetOrderSnEntity();
		entity.setFunc(func);
	    entity.setToken(token);
	    entity.setThirdsn(thirdsn);
		String str = JSON.toJSONString(entity);
		System.out.println("请求参数:"+str);
		try {
			String result = HttpUtil.post(url, str);
			System.out.println("结果:"+result);
			JdResponseGetOrderSnEntity reponse = JSON.parseObject(result,new TypeReference<JdResponseGetOrderSnEntity>(){});
			return reponse;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public JdResponseOrderSubmitEntity orderSubmit(String func, String token, String thirdsn, String ordersn,
			List<JdRequestGetNewStockByIdSkuNumsEntity> sku, String order_amount, String name, String mobile, String province, String city, String county,
			String town, String address) {
		JdRequestOrderSubmitEntity entity = new JdRequestOrderSubmitEntity();
		entity.setFunc(func);
	    entity.setToken(token);
	    entity.setThirdsn(thirdsn);
	    entity.setOrdersn(ordersn);
	    entity.setSku(sku);
	    entity.setOrder_amount(new BigDecimal(order_amount));
	    entity.setName(name);
	    entity.setMobile(mobile);
	    entity.setProvince(province);
	    entity.setCity(city);
	    entity.setCounty(county);
	    entity.setTown(town);
	    entity.setAddress(address);
	    
		String str = JSON.toJSONString(entity);
		System.out.println("请求参数:"+str);
		try {
			String result = HttpUtil.post(url, str);
			System.out.println("结果:"+result);
			JdResponseOrderSubmitEntity reponse = JSON.parseObject(result,new TypeReference<JdResponseOrderSubmitEntity>(){});
			return reponse;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public JdResponseSelectRechargeEntity selectRecharge(String func, String token, String start, String length,
			String orderway) {
		JdRequestSelectRechargeEntity entity = new JdRequestSelectRechargeEntity();
		entity.setFunc(func);
	    entity.setToken(token);
	    entity.setStart(Integer.parseInt(start));
	    entity.setLength(Integer.parseInt(length));
		String str = JSON.toJSONString(entity);
		System.out.println("请求参数:"+str);
		try {
			String result = HttpUtil.post(url, str);
			System.out.println("结果:"+result);
			JdResponseSelectRechargeEntity reponse = JSON.parseObject(result,new TypeReference<JdResponseSelectRechargeEntity>(){});
			return reponse;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public JdResponseSelectSpendEntity selectSpend(String func, String token, String start, String length,
			String orderway) {
		JdRequestSelectSpendEntity entity = new JdRequestSelectSpendEntity();
		entity.setFunc(func);
	    entity.setToken(token);
	    entity.setStart(Integer.parseInt(start));
	    entity.setLength(Integer.parseInt(length));
		String str = JSON.toJSONString(entity);
		System.out.println("请求参数:"+str);
		try {
			String result = HttpUtil.post(url, str);
			System.out.println("结果:"+result);
			JdResponseSelectSpendEntity reponse = JSON.parseObject(result,new TypeReference<JdResponseSelectSpendEntity>(){});
			return reponse;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public JdResponseConfirmOrderEntity confirmOrder(String func, String token, String ordersn) {
		JdRequestConfirmOrderEntity entity = new JdRequestConfirmOrderEntity();
		entity.setFunc(func);
	    entity.setToken(token);
	   entity.setOrdersn(ordersn);
		String str = JSON.toJSONString(entity);
		System.out.println("请求参数:"+str);
		try {
			String result = HttpUtil.post(url, str);
			System.out.println("结果:"+result);
			JdResponseConfirmOrderEntity reponse = JSON.parseObject(result,new TypeReference<JdResponseConfirmOrderEntity>(){});
			return reponse;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public JdResponseSelectOrderEntity selectOrder(String func, String token, String ordersn) {
		JdRequestSelectOrderEntity entity = new JdRequestSelectOrderEntity();
		entity.setFunc(func);
	    entity.setToken(token);
	   entity.setOrdersn(ordersn);
		String str = JSON.toJSONString(entity);
		System.out.println("请求参数:"+str);
		try {
			String result = HttpUtil.post(url, str);
			System.out.println("结果:"+result);
			JdResponseSelectOrderEntity reponse = JSON.parseObject(result,new TypeReference<JdResponseSelectOrderEntity>(){});
			return reponse;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public JdResponseOrderTrackEntity orderTrack(String func, String token, String ordersn) {
		JdRequestOrderTrackEntity entity = new JdRequestOrderTrackEntity();
		entity.setFunc(func);
	    entity.setToken(token);
	   entity.setOrdersn(ordersn);
		String str = JSON.toJSONString(entity);
		System.out.println("请求参数:"+str);
		try {
			String result = HttpUtil.post(url, str);
			System.out.println("结果:"+result);
			JdResponseOrderTrackEntity reponse = JSON.parseObject(result,new TypeReference<JdResponseOrderTrackEntity>(){});
			return reponse;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public JdResponseGetOrderSonEntity getOrderSon(String func, String token, String ordersn) {
		JdRequestGetOrderSonEntity entity = new JdRequestGetOrderSonEntity();
		entity.setFunc(func);
	    entity.setToken(token);
	   entity.setOrdersn(ordersn);
		String str = JSON.toJSONString(entity);
		System.out.println("请求参数:"+str);
		try {
			String result = HttpUtil.post(url, str);
			System.out.println("结果:"+result);
			JdResponseGetOrderSonEntity reponse = JSON.parseObject(result,new TypeReference<JdResponseGetOrderSonEntity>(){});
			return reponse;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public JdResponseJdLogisticsEntity jdLogistics(String func, String token, String jd_order_id) {
		JdRequestJdLogisticsEntity entity = new JdRequestJdLogisticsEntity();
		entity.setFunc(func);
	    entity.setToken(token);
	   entity.setJd_order_id(jd_order_id);
		String str = JSON.toJSONString(entity);
		System.out.println("请求参数:"+str);
		try {
			String result = HttpUtil.post(url, str);
			System.out.println("结果:"+result);
			JdResponseJdLogisticsEntity reponse = JSON.parseObject(result,new TypeReference<JdResponseJdLogisticsEntity>(){});
			return reponse;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}


}
