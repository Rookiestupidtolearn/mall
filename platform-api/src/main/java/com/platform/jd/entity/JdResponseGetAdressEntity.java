package com.platform.jd.entity;

import java.util.List;

/**
 * 获取下级地址列表响应参数
 *
 */
public class JdResponseGetAdressEntity extends JdResponseBaseEntity {
	private List<JdResponseGetAdressInfoEntity> info;

	public List<JdResponseGetAdressInfoEntity> getInfo() {
		return info;
	}

	public void setInfo(List<JdResponseGetAdressInfoEntity> info) {
		this.info = info;
	}
	
	
	
	
}
