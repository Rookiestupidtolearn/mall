package com.platform.youle.entity.result;

import java.util.List;

public class ResultDataOrderTrackEntity {

	//第三方订单号
	private String third_order;
	//快递公司
	private String shipment_name;
	//快递单号
	private String shipment_order;
	//物流状态, receive:揽件,transit:运输中, signed:已签收, refuse:拒收, other:其他
	private String status;
	//最后更新时间, 格式yyyy-MM-dd HH:mm:ss, 例如:2016-06-01 11:12:13
	private String last_modify_time;
	//物流信息
	private List<String> contents;
	//时间
	private String time;
	//物流描述信息
	private String description;
	public String getThird_order() {
		return third_order;
	}
	public void setThird_order(String third_order) {
		this.third_order = third_order;
	}
	public String getShipment_name() {
		return shipment_name;
	}
	public void setShipment_name(String shipment_name) {
		this.shipment_name = shipment_name;
	}
	public String getShipment_order() {
		return shipment_order;
	}
	public void setShipment_order(String shipment_order) {
		this.shipment_order = shipment_order;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLast_modify_time() {
		return last_modify_time;
	}
	public void setLast_modify_time(String last_modify_time) {
		this.last_modify_time = last_modify_time;
	}
	public List<String> getContents() {
		return contents;
	}
	public void setContents(List<String> contents) {
		this.contents = contents;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
