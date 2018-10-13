package com.platform.entity;

import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

/**
 * 名称：SmsConfig <br>
 * 描述：短信配置信息<br>
 *
 * @author 李鹏军
 * @version 1.0
 * @since 1.0.0
 */
public class SmsConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 类型 1：创瑞
     */
    @Range(min = 1, max = 3, message = "类型错误")
    private Integer type;

    /**
     * 短信发送域名
     */
    private String domain;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码(md5加密)
     */
    private String password;

    /**
     *epid
     */
    private String epid;
    
    /**
     *sign 【公司简称】
     */
    private String sign;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEpid() {
		return epid;
	}

	public void setEpid(String epid) {
		this.epid = epid;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
}
