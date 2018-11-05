package com.platform.jd.entity;

import java.util.List;

/**
 * 商品购买区域响应实体
 *
 */
public class JdResponseCheckAreaLimitEntity extends JdResponseBaseEntity {
	 private String jd_msg; //	京东接口错误信息，只在京东返回错误的情况下存在 
	 private List<JdResponseCheckAreaLimitInfoEntity> info; // 是否受限结果
	public String getJd_msg() {
		return jd_msg;
	}
	public void setJd_msg(String jd_msg) {
		this.jd_msg = jd_msg;
	}
	public List<JdResponseCheckAreaLimitInfoEntity> getInfo() {
		return info;
	}
	public void setInfo(List<JdResponseCheckAreaLimitInfoEntity> info) {
		this.info = info;
	}
	 
	 
}
