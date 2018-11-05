package com.platform.jd.entity;

import java.util.List;

/**
 * 查询京东子订单订单编号响应实体
 *
 */
public class JdResponseGetOrderSonEntity extends JdResponseBaseEntity {
	
	private List<JdResponseGetOrderSonInfoEntity> info; //订单配送信息

	public List<JdResponseGetOrderSonInfoEntity> getInfo() {
		return info;
	}

	public void setInfo(List<JdResponseGetOrderSonInfoEntity> info) {
		this.info = info;
	}
	
	
}
