package com.platform.jd.entity;

import java.util.List;

public class JdResponseGetPriceEntity extends JdResponseBaseEntity {
	
	private String jd_msg; //	京东接口错误信息，只在京东返回错误的情况下存在
	
	private List<JdResponseGetPriceInfoEntity> info; // 商品价格列表

	public String getJd_msg() {
		return jd_msg;
	}

	public void setJd_msg(String jd_msg) {
		this.jd_msg = jd_msg;
	}

	public List<JdResponseGetPriceInfoEntity> getInfo() {
		return info;
	}

	public void setInfo(List<JdResponseGetPriceInfoEntity> info) {
		this.info = info;
	}
	
	
}
