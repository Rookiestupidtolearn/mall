package com.platform.youle.entity;

import java.util.List;
import java.util.Map;

public class ResponseSkuDetailEntity extends ResponseBaseEntity<Map<String,String>>{
	
	//产品数据
	private List<String> PRODUCT_DATA;
	//商品ID, 唯一标识
	private Integer productId;
	//商品名称
	private String name;
	//产品来源(system: 系统, provider:供应商, jindong：京东，wangyi:网易严选)
	private String type;
	//商品缩略图， 主要用于商品列表展示
	private String thumbnailImage;
	//品牌名称
	private String brand;
	//商品分类
	private Integer productCate;
	//商品型号
	private String productCode;
	//商品状态 (selling: 上架销售中, undercarriage: 下架)
	private String status;
	//市场价
	private double marketPrice;
	//协议价格(系统结算价格)
	private double retailPrice;
	//商品产地
	private String productPlace;
	//商品描述信息（简要描述）
	private String features;
	//是否为热销商品
	private boolean hot;
	//商品创建时间, 格式yyyy-MM-dd HH:mm:ss
	private String createTime;
	//是否支持7天无理由退货
	private boolean is7ToReturn;
	//商品图片
	private List<String> PRODUCT_IMAGE;
	//图片路径
	private String imageUrl;
	//图片顺序
	private Integer orderSort;
	//商品详情信息（图文说明信息， 包含HTML代码） 
	private String PRODUCT_DESCRIPTION;
	//移动端商品详情信息（图文说明信息， 包含HTML代码）
	private String MOBILE_PRODUCT_DESCRIPTION;
	
	public List<String> getPRODUCT_DATA() {
		return PRODUCT_DATA;
	}
	public void setPRODUCT_DATA(List<String> pRODUCT_DATA) {
		PRODUCT_DATA = pRODUCT_DATA;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getThumbnailImage() {
		return thumbnailImage;
	}
	public void setThumbnailImage(String thumbnailImage) {
		this.thumbnailImage = thumbnailImage;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public Integer getProductCate() {
		return productCate;
	}
	public void setProductCate(Integer productCate) {
		this.productCate = productCate;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public double getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(double marketPrice) {
		this.marketPrice = marketPrice;
	}
	public double getRetailPrice() {
		return retailPrice;
	}
	public void setRetailPrice(double retailPrice) {
		this.retailPrice = retailPrice;
	}
	public String getProductPlace() {
		return productPlace;
	}
	public void setProductPlace(String productPlace) {
		this.productPlace = productPlace;
	}
	public String getFeatures() {
		return features;
	}
	public void setFeatures(String features) {
		this.features = features;
	}
	public boolean isHot() {
		return hot;
	}
	public void setHot(boolean hot) {
		this.hot = hot;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public boolean isIs7ToReturn() {
		return is7ToReturn;
	}
	public void setIs7ToReturn(boolean is7ToReturn) {
		this.is7ToReturn = is7ToReturn;
	}
	public List<String> getPRODUCT_IMAGE() {
		return PRODUCT_IMAGE;
	}
	public void setPRODUCT_IMAGE(List<String> pRODUCT_IMAGE) {
		PRODUCT_IMAGE = pRODUCT_IMAGE;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Integer getOrderSort() {
		return orderSort;
	}
	public void setOrderSort(Integer orderSort) {
		this.orderSort = orderSort;
	}
	public String getPRODUCT_DESCRIPTION() {
		return PRODUCT_DESCRIPTION;
	}
	public void setPRODUCT_DESCRIPTION(String pRODUCT_DESCRIPTION) {
		PRODUCT_DESCRIPTION = pRODUCT_DESCRIPTION;
	}
	public String getMOBILE_PRODUCT_DESCRIPTION() {
		return MOBILE_PRODUCT_DESCRIPTION;
	}
	public void setMOBILE_PRODUCT_DESCRIPTION(String mOBILE_PRODUCT_DESCRIPTION) {
		MOBILE_PRODUCT_DESCRIPTION = mOBILE_PRODUCT_DESCRIPTION;
	}
	
	
	
}
