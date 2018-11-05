package com.platform.jd.entity;

/**
 * 获取安全码响应实体
 *
 */
public class GetTokenSafeCodeResponseEntity extends JdResponseBaseEntity {
	
	private String safecode; //安全加密码
	
	public GetTokenSafeCodeResponseEntity() {
		super();
	}

	public GetTokenSafeCodeResponseEntity(String safecode) {
		this.safecode = safecode;
	}

	public String getSafecode() {
		return safecode;
	}

	public void setSafecode(String safecode) {
		this.safecode = safecode;
	}

}
