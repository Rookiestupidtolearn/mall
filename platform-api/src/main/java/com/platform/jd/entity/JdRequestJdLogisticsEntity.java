package com.platform.jd.entity;

/**
 * 查询京东子订单物流信息请求实体
 *
 */
public class JdRequestJdLogisticsEntity extends JdRequestBaseEntity {
	private String jd_order_id; //	必须	京东子订单编号

	public String getJd_order_id() {
		return jd_order_id;
	}

	public void setJd_order_id(String jd_order_id) {
		this.jd_order_id = jd_order_id;
	}
	
	
}
