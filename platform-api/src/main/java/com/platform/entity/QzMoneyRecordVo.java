package com.platform.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class QzMoneyRecordVo implements Serializable{
	 private static final long serialVersionUID = 1L;

	    //主键
	    private Integer id;
	    //会员id
	    private Integer shopUserId;
	    //资金变动类型 1-充值2 -优惠券
	    private String tranType;
	    //金额变动标志 0-支出 1-收入
	    private Integer tranFlag;
	    //变动金额
	    private BigDecimal tarnAmount;
	    //当前余额
	    private BigDecimal currentAmount;
	    //创建时间
	    private Date createTime;
	    //交易流水号(关联各资金订单)
	    private String tradeNo;
	    //备注
	    private String remark;
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public Integer getShopUserId() {
			return shopUserId;
		}
		public void setShopUserId(Integer shopUserId) {
			this.shopUserId = shopUserId;
		}
		public String getTranType() {
			return tranType;
		}
		public void setTranType(String tranType) {
			this.tranType = tranType;
		}
		public Integer getTranFlag() {
			return tranFlag;
		}
		public void setTranFlag(Integer tranFlag) {
			this.tranFlag = tranFlag;
		}
		public BigDecimal getTarnAmount() {
			return tarnAmount;
		}
		public void setTarnAmount(BigDecimal tarnAmount) {
			this.tarnAmount = tarnAmount;
		}
		public BigDecimal getCurrentAmount() {
			return currentAmount;
		}
		public void setCurrentAmount(BigDecimal currentAmount) {
			this.currentAmount = currentAmount;
		}
		public Date getCreateTime() {
			return createTime;
		}
		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}
		public String getTradeNo() {
			return tradeNo;
		}
		public void setTradeNo(String tradeNo) {
			this.tradeNo = tradeNo;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		
}
