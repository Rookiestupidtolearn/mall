package com.platform.jd.entity;

import java.util.List;

/**
 * 商品好评度查询响应实体
 *
 */
public class JdResponseGetCommentSummarysEntity extends JdResponseBaseEntity {
	private String jd_msg; //	京东接口错误信息，只在京东返回错误的情况下存在
	private List<JdResponseGetCommentSummarysInfoEntity> info; // 商品好评度
	public String getJd_msg() {
		return jd_msg;
	}
	public void setJd_msg(String jd_msg) {
		this.jd_msg = jd_msg;
	}
	public List<JdResponseGetCommentSummarysInfoEntity> getInfo() {
		return info;
	}
	public void setInfo(List<JdResponseGetCommentSummarysInfoEntity> info) {
		this.info = info;
	}
	
	
	
}
