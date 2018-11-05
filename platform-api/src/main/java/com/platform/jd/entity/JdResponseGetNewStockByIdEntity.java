package com.platform.jd.entity;

import java.util.List;

/**
 * 批量获取库存响应实体
 *
 */
public class JdResponseGetNewStockByIdEntity extends JdResponseBaseEntity {
	private String jd_msg;	//京东接口错误信息，只在京东返回错误的情况下存在
	private List<JdResponseGetNewStockByIdInfoEntity> info; //库存结果
	public String getJd_msg() {
		return jd_msg;
	}
	public void setJd_msg(String jd_msg) {
		this.jd_msg = jd_msg;
	}
	public List<JdResponseGetNewStockByIdInfoEntity> getInfo() {
		return info;
	}
	public void setInfo(List<JdResponseGetNewStockByIdInfoEntity> info) {
		this.info = info;
	}
	
	
	
	
}
