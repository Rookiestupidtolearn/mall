package com.platform.jd.entity;

/**
 * 获取商品详细信息接口响应实体
 *
 */
public class SkuDetailResponseEntiey extends JdResponseBaseEntity {
	
	private String jd_msg;//	京东接口错误信息，只在京东返回错误的情况下存在

	public String getJd_msg() {
		return jd_msg;
	}

	public void setJd_msg(String jd_msg) {
		this.jd_msg = jd_msg;
	}
	
	
}
