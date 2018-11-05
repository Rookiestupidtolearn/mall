package com.platform.jd.entity;

/**
 * 获取夏季地址列表请求接口
 *
 */
public class JdRequestGetAdressEntity extends JdRequestBaseEntity {
	
	private Integer parent_id;// 上级地址  如果为0，则是获取省

	public Integer getParent_id() {
		return parent_id;
	}

	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
	}
	
	
}
