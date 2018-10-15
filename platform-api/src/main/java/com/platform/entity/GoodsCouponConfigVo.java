package com.platform.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class GoodsCouponConfigVo implements Serializable{
	 private static final long serialVersionUID = 1L;
	 //主键
	 private Integer id;
	 //产品id
	 private Integer goods_id;
	 //商品配比值
	 private double good_value;
	 //创建者id
	 private BigDecimal create_user_id;
	 //更新者id
	 private BigDecimal update_user_id;
	 //更新时间 
	 private Date update_time;
	 //创建者id
	 private BigDecimal create_user_dept_id;
	 //删除标识
	 private String del_flag;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}

	public double getGood_value() {
		return good_value;
	}

	public void setGood_value(double good_value) {
		this.good_value = good_value;
	}

	public BigDecimal getCreate_user_id() {
		return create_user_id;
	}

	public void setCreate_user_id(BigDecimal create_user_id) {
		this.create_user_id = create_user_id;
	}

	public BigDecimal getUpdate_user_id() {
		return update_user_id;
	}

	public void setUpdate_user_id(BigDecimal update_user_id) {
		this.update_user_id = update_user_id;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public BigDecimal getCreate_user_dept_id() {
		return create_user_dept_id;
	}

	public void setCreate_user_dept_id(BigDecimal create_user_dept_id) {
		this.create_user_dept_id = create_user_dept_id;
	}

	public String getDel_flag() {
		return del_flag;
	}

	public void setDel_flag(String del_flag) {
		this.del_flag = del_flag;
	}
	 
}
