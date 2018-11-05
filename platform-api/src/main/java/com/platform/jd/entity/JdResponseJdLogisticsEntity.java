package com.platform.jd.entity;

import java.util.List;

/**
 * 查询京东子订单物流信息响应实体
 *
 */
public class JdResponseJdLogisticsEntity extends JdResponseBaseEntity {
	
	private List<JdResponseOrderTrackInfoOrderTrackEntity> info; //订单配送信息

	public List<JdResponseOrderTrackInfoOrderTrackEntity> getInfo() {
		return info;
	}

	public void setInfo(List<JdResponseOrderTrackInfoOrderTrackEntity> info) {
		this.info = info;
	}
	
	
}
