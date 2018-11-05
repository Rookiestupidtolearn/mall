package com.platform.jd.entity;

/**
 * 商品好评度
 *
 */
public class JdResponseGetCommentSummarysInfoEntity {
	private String skuId;//	商品编号
	private String averageScore; //	商品评分 (5颗星，4颗星)
	private String goodRate; //	好评度
	private String generalRate;//	中评度
	private String poorRate; //	差评度
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public String getAverageScore() {
		return averageScore;
	}
	public void setAverageScore(String averageScore) {
		this.averageScore = averageScore;
	}
	public String getGoodRate() {
		return goodRate;
	}
	public void setGoodRate(String goodRate) {
		this.goodRate = goodRate;
	}
	public String getGeneralRate() {
		return generalRate;
	}
	public void setGeneralRate(String generalRate) {
		this.generalRate = generalRate;
	}
	public String getPoorRate() {
		return poorRate;
	}
	public void setPoorRate(String poorRate) {
		this.poorRate = poorRate;
	}
	
	
}
