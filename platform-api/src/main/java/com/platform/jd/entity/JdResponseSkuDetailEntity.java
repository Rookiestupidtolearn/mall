package com.platform.jd.entity;

import java.util.List;

/**
 * 获取商品详细信息接口响应实体
 *
 */
public class JdResponseSkuDetailEntity extends JdResponseBaseEntity {
	
	private String jd_msg;//	京东接口错误信息，只在京东返回错误的情况下存在
	
	private List<JdResponseSkuDetailInfoEntity> info; //商品详细信息

	public String getJd_msg() {
		return jd_msg;
	}

	public void setJd_msg(String jd_msg) {
		this.jd_msg = jd_msg;
	}

	public List<JdResponseSkuDetailInfoEntity> getInfo() {
		return info;
	}

	public void setInfo(List<JdResponseSkuDetailInfoEntity> info) {
		this.info = info;
	}
	
	
}
