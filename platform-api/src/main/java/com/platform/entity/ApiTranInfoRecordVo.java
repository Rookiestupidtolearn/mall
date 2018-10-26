package com.platform.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ApiTranInfoRecordVo implements Serializable{
	 private static final long serialVersionUID = 1L;
	 //主键id
	 private Long id;
	 //用户id
	 private Long user_id;
	 //交易类型 1：优惠券 2 平台币
	 private String tran_type;
	 //交易方向 1 收入 2支出
	 private String tran_flag;
	 //交易金额
	 private BigDecimal tran_amount;
	 //当前金额
	 private BigDecimal current_amount;
	 //创建时间
	 private Date create_time;
	 //备注
	 private String remark;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getTran_type() {
		return tran_type;
	}

	public void setTran_type(String tran_type) {
		this.tran_type = tran_type;
	}

	public String getTran_flag() {
		return tran_flag;
	}

	public void setTran_flag(String tran_flag) {
		this.tran_flag = tran_flag;
	}

	public BigDecimal getTran_amount() {
		return tran_amount;
	}

	public void setTran_amount(BigDecimal tran_amount) {
		this.tran_amount = tran_amount;
	}

	public BigDecimal getCurrent_amount() {
		return current_amount;
	}

	public void setCurrent_amount(BigDecimal current_amount) {
		this.current_amount = current_amount;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	 
	 
	 
}
