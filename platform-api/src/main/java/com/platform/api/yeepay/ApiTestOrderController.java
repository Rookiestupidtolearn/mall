package com.platform.api.yeepay;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import com.platform.annotation.IgnoreAuth;
import com.platform.dao.ApiOrderGoodsMapper;
import com.platform.dao.ApiOrderMapper;
import com.platform.dao.GoodsCouponConfigMapper;
import com.platform.dao.JdOrderMapper;
import com.platform.entity.GoodsCouponConfigVo;
import com.platform.entity.JdOrderVo;
import com.platform.entity.OrderGoodsVo;
import com.platform.entity.OrderVo;
import com.platform.util.ApiBaseAction;
import com.platform.utils.R;

import io.swagger.annotations.Api;
import net.sf.json.JSONObject;

@Api(tags = "测试接口")
@RestController
@RequestMapping("/test/order")
public class ApiTestOrderController extends ApiBaseAction {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private JdOrderMapper jdOrderMapper;
	
	@Autowired
	private ApiOrderMapper apiOrderMapper;
	
	@Autowired
	private ApiOrderGoodsMapper apiOrderGoodsMapper;
	
    @Autowired
    private GoodsCouponConfigMapper goodsCouponConfigMapper;
	
	@IgnoreAuth
	@PostMapping("/recordingPrice")
	public Object recordingPrice(String third_order){
		logger.info("补录订单价格开始。。");
		
		if (StringUtils.isEmpty(third_order)) {
			return R.error("订单单号不能为空");
		}
		
		logger.info("订单号为："+third_order);
		// 处理业务
		JdOrderVo jdOrderVo = jdOrderMapper.queryByThirdOrder(third_order);
		if (jdOrderVo == null) {
			return R.error("没有查询到京东的订单");
		}
		OrderVo order = apiOrderMapper.queryOrderByOrderSn(third_order);
		List<OrderGoodsVo> orderGoods = apiOrderGoodsMapper.queryOrderLogisticGoods(order.getId());
		BigDecimal  retail_price = BigDecimal.ZERO ;  //结算总价
		Integer  goods_total_num = 0; // 商品总数量
		if (CollectionUtils.isNotEmpty(orderGoods)) {
			for (OrderGoodsVo  goodsItem : orderGoods) {
			    BigDecimal 	coupon_price  =  BigDecimal.ZERO;
			     Map<String, Object> map = new HashMap<String, Object>();
			     Integer couponNum = 0;
			     map.put("order_id", order.getId());
			     map.put("goods_id", goodsItem.getGoods_id());
				 List<GoodsCouponConfigVo> configVos = goodsCouponConfigMapper.getCouponList(map);
	            if (!CollectionUtils.isEmpty(configVos)) {
	            	coupon_price = configVos.get(0).getCoupon_price();
	            	couponNum = configVos.size();
				}
				
				//结算总价
				retail_price = goodsItem.getRetail_price().multiply(new BigDecimal(goodsItem.getNumber())).add(retail_price);
				//商品的总数量
				goods_total_num += goodsItem.getNumber();
				//商品总价格
				goodsItem.setGoods_total_price(goodsItem.getMarket_price().multiply(new BigDecimal(goodsItem.getNumber())));
			   //单品克拉金额
				goodsItem.setCaller_price(coupon_price);
//				caller_total_price  使用的克拉总金额、
				goodsItem.setCaller_total_price(coupon_price.multiply(new BigDecimal(couponNum)));
				if (goodsItem.getNumber()-couponNum ==0) {
					goodsItem.setActual_price(new BigDecimal("0"));//未发生克拉抵扣
				}else {
					goodsItem.setActual_price(goodsItem.getMarket_price());//未发生克拉抵扣
				}
				goodsItem.setCaller_num(couponNum);
				goodsItem.setActual_caller_price(goodsItem.getMarket_price().subtract(coupon_price));
				logger.info("修改子订单的数据是"+JSON.toJSONString(goodsItem));
				apiOrderGoodsMapper.update(goodsItem);
				
			} 
			
		}
		order.setPay_id("0");
		order.setPay_name("易宝支付");
		order.setRetail_price(retail_price);
		order.setGoods_total_num(goods_total_num);
		apiOrderMapper.update(order);
		logger.info("补录订单价格处理完毕。。");
		
		return R.ok();
		
	}
}
