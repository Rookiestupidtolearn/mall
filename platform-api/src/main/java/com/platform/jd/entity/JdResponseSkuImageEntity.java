package com.platform.jd.entity;

import java.util.List;

/**
 * 获取所有图片信息响应实体
 *
 */
public class JdResponseSkuImageEntity extends JdResponseBaseEntity {
	private String jd_msg; // 京东接口错误信息，只在京东返回错误的情况下存在
	private List<JdResponseSkuImageInfoEntity> info; //商品图片
	public String getJd_msg() {
		return jd_msg;
	}
	public void setJd_msg(String jd_msg) {
		this.jd_msg = jd_msg;
	}
	public List<JdResponseSkuImageInfoEntity> getInfo() {
		return info;
	}
	public void setInfo(List<JdResponseSkuImageInfoEntity> info) {
		this.info = info;
	}
	
	
}
