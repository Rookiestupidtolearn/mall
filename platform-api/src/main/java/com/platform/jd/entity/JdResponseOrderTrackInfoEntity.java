package com.platform.jd.entity;

import java.util.List;

/**
 * 查询配送信息响应实体  -- 订单信息
 *
 */
public class JdResponseOrderTrackInfoEntity {
	
	private String ordersn; //礼管家平台订单号
	private List<JdResponseOrderTrackInfoOrderTrackEntity> orderTrack; //订单信息
						/*[{"content":"配送内容","msgTime":"操作时间","operator":"操作人"},
						{"content":"您的订单已经进入京东杭州1号库准备出库","msgTime":"2015-10-20 15:12:25","operator":"系统"}]*/
	public String getOrdersn() {
		return ordersn;
	}
	public void setOrdersn(String ordersn) {
		this.ordersn = ordersn;
	}
	public List<JdResponseOrderTrackInfoOrderTrackEntity> getOrderTrack() {
		return orderTrack;
	}
	public void setOrderTrack(List<JdResponseOrderTrackInfoOrderTrackEntity> orderTrack) {
		this.orderTrack = orderTrack;
	}
	
	
}
