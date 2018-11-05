package com.platform.jd.entity;

/**
 * 查询用户消费记录请求实体
 *
 */
public class JdRequestSelectSpendEntity extends JdRequestBaseEntity {
	private Integer start;	//非必须	开始条数，默认值为0
	private  Integer length; //	非必须	获取条数，默认值为20，最大值100 如：start=0,length=20,会从第一条记录开始，返回最多20条记录。
	private Integer orderway; //	非必须	按时间排序方式，0：倒序排列，1：顺序排列，默认为0
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public Integer getOrderway() {
		return orderway;
	}
	public void setOrderway(Integer orderway) {
		this.orderway = orderway;
	}
	
	
}
