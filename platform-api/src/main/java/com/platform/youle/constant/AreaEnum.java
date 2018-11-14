package com.platform.youle.constant;

/**
 * 区域枚举
 * @author liutao
 *
 */
public enum AreaEnum {
	
	CHINA("中国","0"),
	PROVINCE("省","1"),
	CITY("市","2"),
	COUNTY("区","3"),
	TOWN("乡/镇","4");
	
	private  String name;
	private String code;

	private AreaEnum(String name,String code){
		this.name=name;
		this.code=code;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}
	
}
