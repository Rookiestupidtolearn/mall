package com.platform.entity;

/**
 * 三方地址信息实体
 *
 */
public class ThirdPartyRegionEntity {
	private Integer id;
	private Integer parentId; //父级id
	private  String name; //地址名
	private Integer type ; //类型
	private String thirdType;
	private String thirdParty; //对接的第三方
	private Integer sysRegionId; //系统地址(sys_region表)id
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getThirdParty() {
		return thirdParty;
	}
	public void setThirdParty(String thirdParty) {
		this.thirdParty = thirdParty;
	}
	public Integer getSysRegionId() {
		return sysRegionId;
	}
	public void setSysRegionId(Integer sysRegionId) {
		this.sysRegionId = sysRegionId;
	}
	public String getThirdType() {
		return thirdType;
	}
	public void setThirdType(String thirdType) {
		this.thirdType = thirdType;
	}
	
	
	
}
