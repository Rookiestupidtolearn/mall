package com.platform.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.platform.dao.ApiProductMapper;
import com.platform.entity.ProductVo;
import com.platform.utils.SpringContextUtils;


/**
 * 计算支付配比
 *
 */
@Component
public class PayMatchingUtil {

	 @Autowired
	 public ApiProductMapper apiProductMapper;
	
	/**
	 * 获取商品支付配比
	 * @param goodsId
	 * @return
	 */
	public  Map<Object,Object> getPayMatching(Integer productId){
		Map<Object,Object> goodsPayMatching = new HashMap<>();
		if(null == productId || 0 >= productId){
			return goodsPayMatching;
		}
		//根据productId查询查询商品信息
		ProductVo productVo  = apiProductMapper.queryGoodsMatchingInfo(productId);
		if(null == productVo){
			return goodsPayMatching;
		}
		BigDecimal marketPrice = productVo.getMarket_price(); // 指导价 M
		BigDecimal retailPrice = productVo.getRetail_price(); // 结算价 N
		BigDecimal activityMatching = new BigDecimal(productVo.getActivity_matching()); //活动配比 H
		BigDecimal normalMatching =  new BigDecimal(productVo.getNormal_matching()); //正常配比 Z
		
		//计算支付配比 : 公式 S={M*Z-(Z-H)*N}/M*100%
		BigDecimal PayMatching =marketPrice.multiply(normalMatching).subtract(normalMatching.subtract(activityMatching).multiply(retailPrice)).divide(marketPrice,4, BigDecimal.ROUND_DOWN);
		//计算抵扣金额
		BigDecimal Money = marketPrice.multiply(PayMatching).setScale(2, BigDecimal.ROUND_HALF_UP);
		goodsPayMatching.put(productVo.getGoods_id(), Money);
		return goodsPayMatching;
	}
}
