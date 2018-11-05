package com.platform.jd.entity;

import java.util.List;

/**
 * 获取商品上下架状态响应实体
 *
 */
public class JdResponseSkuStateEntity extends JdResponseBaseEntity {
	
	private String jd_msg; //京东接口错误信息，只在京东返回错误的情况下存在
	
	private List<JdResponseSkuStateInfoEntity> info; //上下架状态

	public String getJd_msg() {
		return jd_msg;
	}

	public void setJd_msg(String jd_msg) {
		this.jd_msg = jd_msg;
	}

	public List<JdResponseSkuStateInfoEntity> getInfo() {
		return info;
	}

	public void setInfo(List<JdResponseSkuStateInfoEntity> info) {
		this.info = info;
	}
	
	
}
