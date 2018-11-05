package com.platform.jd.entity;

import java.util.List;

/**
 * 查询配送信息响应实体
 *
 */
public class JdResponseOrderTrackEntity extends JdResponseBaseEntity {
	
	private String jd_msg; //	京东提示信息
	
	private List<JdResponseOrderTrackInfoEntity> info; // 订单配送信息

	public String getJd_msg() {
		return jd_msg;
	}

	public void setJd_msg(String jd_msg) {
		this.jd_msg = jd_msg;
	}

	public List<JdResponseOrderTrackInfoEntity> getInfo() {
		return info;
	}

	public void setInfo(List<JdResponseOrderTrackInfoEntity> info) {
		this.info = info;
	}
	
	
}
