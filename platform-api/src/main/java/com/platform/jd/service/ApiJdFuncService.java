package com.platform.jd.service;

import java.util.List;

import com.platform.jd.entity.JdRequestGetNewStockByIdSkuNumsEntity;
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

public interface ApiJdFuncService {
  
	/**
	 * 授权服务
	 * @param func
	 * @param username
	 * @param password
	 * @param api_name
	 * @param api_secret
	 * @return
	 */
	public JdResponseGetTokenSafeCodeEntity getTokenSafeCode(String func,
			String username,
			String password,
			String api_name,
			String api_secret
     );
	
	/**
	 *  获取TOKEN（token获取后，两小时内有效）
	 * @param func
	 * @param username
	 * @param password
	 * @param api_name
	 * @param api_secret
	 * @param safecode
	 * @return
	 */
	public JdResponseGetApiTokenEntity getApiToken(
			String func,
			String username,
			String password,
			String api_name,
			String api_secret,
			String safecode
			);
	/**
	 * 2.1 分页获取所有sku
	 * @param func
	 * @param token
	 * @param page
	 * @return
	 */
		public JdResponseGetSkuEntity getSku(
				String func,
				String token,
				String page
         ) ;
		
		/**
		 * 2.2 获取商品详细信息接口
		 * @param func
		 * @param token
		 * @param sku
		 * @return
		 */
		public JdResponseSkuDetailEntity  skuDetail(
				String func,
				String token,
				String sku
        );
		
		/**
		 * 2.3 获取商品上下架状态接口
		 * @param func
		 * @param token
		 * @param sku
		 * @return
		 */
		public JdResponseSkuStateEntity skuState(
				String func,
				String token,
				String sku
      ) ; 
		/**
		 * 2.4 获取所有图片信息
		 * @param func
		 * @param token
		 * @param sku
		 * @return
		 */
		public JdResponseSkuImageEntity skuImage(
				String func,
				String token,
				String sku
		);
		
		/**
		 * 2.5 商品好评度查询
		 * @param func
		 * @param token
		 * @param sku
		 * @return
		 */
		public JdResponseGetCommentSummarysEntity getCommentSummarys(
				String func,
				String token,
				String sku
				);
		/**
		 * 3.1 获取下级地址列表
		 * @param func
		 * @param token
		 * @param parent_id
		 * @return
		 */
		public JdResponseGetAdressEntity getAdress (
				String func,
				String  token,
				String parent_id

		);
		
		/**
		 * 4.1 批量查询协议价价格
		 * @param func
		 * @param token
		 * @param sku
		 * @return
		 */
		public JdResponseGetPriceEntity getPrice(
				String func,
				String token,
				String sku
		);
		
		/**
		 * 5.1 批量获取库存接口
		 * @param func
		 * @param token
		 * @param area
		 * @param skuNums
		 * @return
		 */
		public JdResponseGetNewStockByIdEntity getNewStockById(
				String func,
				String token,
				String area,
				List<JdRequestGetNewStockByIdSkuNumsEntity> skuNums
		);
		
		/**
		 * 5.2 商品购买区域限制查询
		 * @param func
		 * @param token
		 * @param area
		 * @param skuIds
		 * @return
		 */
		public  JdResponseCheckAreaLimitEntity checkAreaLimit(
				String func,
				String token,
				String area,
				String skuIds
		);
		
		/**
		 * 6.1 获取 礼管家平台订单号（一次有效，5分钟内有效）
		 * @param func
		 * @param token
		 * @param thirdsn
		 * @return
		 */
		public JdResponseGetOrderSnEntity getOrderSn(
				String	func,
				String token,
				String thirdsn
		);
		
		/**
		 * 6.2 预存款下单接口
		 * @param func
		 * @param token
		 * @param thirdsn
		 * @param ordersn
		 * @param sku
		 * @param order_amount
		 * @param name
		 * @param mobile
		 * @param province
		 * @param city
		 * @param county
		 * @param town
		 * @param address
		 * @return
		 */
		public JdResponseOrderSubmitEntity orderSubmit (
				String func,
				String token,
				String thirdsn,
				String ordersn,
				List<JdRequestGetNewStockByIdSkuNumsEntity> sku,
				String order_amount,
				String name,
				String mobile,
				String province,
				String city,
				String county,
				String town,
				String address
		);
		
		/**
		 * 6.3 查询用户充值记录接口
		 * @param func
		 * @param token
		 * @param start
		 * @param length
		 * @param orderway
		 * @return
		 */
		public JdResponseSelectRechargeEntity selectRecharge(
				String func,
				String token,
				String start,
				String length,
				String orderway
		);
		
		/**
		 * 6.4 查询用户消费记录接口
		 * @param func
		 * @param token
		 * @param start
		 * @param length
		 * @param orderway
		 * @return
		 */
		public JdResponseSelectSpendEntity selectSpend(
				String func,
				String token,
				String start,
				String length,
				String orderway
		);
		
		/**
		 * 6.5 确认订单接口
		 * @param func
		 * @param token
		 * @param ordersn
		 * @return
		 */
		public JdResponseConfirmOrderEntity confirmOrder (
				String func,
				String token,
				String ordersn
		);
		
		/**
		 * 6.6 查询订单信息接口
		 * @param func
		 * @param token
		 * @param ordersn
		 * @return
		 */
		public JdResponseSelectOrderEntity selectOrder (
				String func,
				String token,
				String ordersn
				);
		
		/**
		 * 6.7 查询配送信息接口
		 * @param func
		 * @param token
		 * @param ordersn
		 * @return
		 */
		public JdResponseOrderTrackEntity orderTrack(
				String func,
				String token,
				String ordersn
		);
		
		/**
		 * 6.8 查询京东子订单订单编号
		 * @param func
		 * @param token
		 * @param ordersn
		 * @return
		 */
		public JdResponseGetOrderSonEntity getOrderSon(
				String func,
				String token,
				String ordersn
		);
		
		/**
		 * 6.9 查询京东子订单物流信息
		 * @param func
		 * @param token
		 * @param jd_order_id
		 * @return
		 */
		public JdResponseJdLogisticsEntity jdLogistics (
				String func,
				String token,
				String jd_order_id
		);
		
}
