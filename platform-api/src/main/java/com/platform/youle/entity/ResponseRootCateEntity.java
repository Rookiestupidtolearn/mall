package com.platform.youle.entity;

import java.util.Map;

public class ResponseRootCateEntity extends  ResponseBaseEntity<Map<String,String>>{
	
	private Integer code;//分类id
	
	private String name;//分类名称
	
	private Integer level;//层级
	
	private boolean status;//状态(true:启用, false:禁用)

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
}
