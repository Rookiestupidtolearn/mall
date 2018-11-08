package com.platform.youle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 实体
 * 表名 jd_shop_goods
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-11-08 14:31:35
 */
public class JdGoodsVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键id
    private Long id;
    //商品id 唯一标识
    private Long goodsSn;
    //商品名称
    private String name;
    //产品来源(system: 系统, provider:供应商, jindong：京东，wangyi:网易严选)
    private String type;
    //缩略图
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
    private BigDecimal marketPrice;
    //协议价格(系统结算价格)
    private BigDecimal retailPrice;
    //商品产地
    private String productPlace;
    //商品描述信息（简要描述）
    private String features;
    //是否为热销商品
    private Integer hot;
    //是否支持7天无理由退货
    private Integer is7Toreturn;
    //商品详情信息（图文说明信息， 包含HTML代码）
    private String productDecription;
    //移动端商品详情信息（图文说明信息， 包含HTML代码）
    private String mobileProductDecription;
    //评价详情id
    private Long goodsCommentsId;
    //商品类别
    private Long goodsCategoryId;
    //图片地址
    private String goodsImagePathId;
    //关键字
    private String keywords;
    //删除标识 0 ：未删除 1:删除
    private Long deleteFlag;
    //创建时间
    private Date createTime;
    //创建者
    private Long createBy;
    //更新时间
    private Date updateTime;

    /**
     * 设置：主键id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取：主键id
     */
    public Long getId() {
        return id;
    }
    /**
     * 设置：商品id 唯一标识
     */
    public void setGoodsSn(Long goodsSn) {
        this.goodsSn = goodsSn;
    }

    /**
     * 获取：商品id 唯一标识
     */
    public Long getGoodsSn() {
        return goodsSn;
    }
    /**
     * 设置：商品名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取：商品名称
     */
    public String getName() {
        return name;
    }
    /**
     * 设置：产品来源(system: 系统, provider:供应商, jindong：京东，wangyi:网易严选)
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取：产品来源(system: 系统, provider:供应商, jindong：京东，wangyi:网易严选)
     */
    public String getType() {
        return type;
    }
    /**
     * 设置：缩略图
     */
    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    /**
     * 获取：缩略图
     */
    public String getThumbnailImage() {
        return thumbnailImage;
    }
    /**
     * 设置：品牌名称
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * 获取：品牌名称
     */
    public String getBrand() {
        return brand;
    }
    /**
     * 设置：商品分类
     */
    public void setProductCate(Integer productCate) {
        this.productCate = productCate;
    }

    /**
     * 获取：商品分类
     */
    public Integer getProductCate() {
        return productCate;
    }
    /**
     * 设置：商品型号
     */
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    /**
     * 获取：商品型号
     */
    public String getProductCode() {
        return productCode;
    }
    /**
     * 设置：商品状态 (selling: 上架销售中, undercarriage: 下架)
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取：商品状态 (selling: 上架销售中, undercarriage: 下架)
     */
    public String getStatus() {
        return status;
    }
    /**
     * 设置：市场价
     */
    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    /**
     * 获取：市场价
     */
    public BigDecimal getMarketPrice() {
        return marketPrice;
    }
    /**
     * 设置：协议价格(系统结算价格)
     */
    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    /**
     * 获取：协议价格(系统结算价格)
     */
    public BigDecimal getRetailPrice() {
        return retailPrice;
    }
    /**
     * 设置：商品产地
     */
    public void setProductPlace(String productPlace) {
        this.productPlace = productPlace;
    }

    /**
     * 获取：商品产地
     */
    public String getProductPlace() {
        return productPlace;
    }
    /**
     * 设置：商品描述信息（简要描述）
     */
    public void setFeatures(String features) {
        this.features = features;
    }

    /**
     * 获取：商品描述信息（简要描述）
     */
    public String getFeatures() {
        return features;
    }
    /**
     * 设置：是否为热销商品
     */
    public void setHot(Integer hot) {
        this.hot = hot;
    }

    /**
     * 获取：是否为热销商品
     */
    public Integer getHot() {
        return hot;
    }
    /**
     * 设置：是否支持7天无理由退货
     */
    public void setIs7Toreturn(Integer is7Toreturn) {
        this.is7Toreturn = is7Toreturn;
    }

    /**
     * 获取：是否支持7天无理由退货
     */
    public Integer getIs7Toreturn() {
        return is7Toreturn;
    }
    /**
     * 设置：商品详情信息（图文说明信息， 包含HTML代码）
     */
    public void setProductDecription(String productDecription) {
        this.productDecription = productDecription;
    }

    /**
     * 获取：商品详情信息（图文说明信息， 包含HTML代码）
     */
    public String getProductDecription() {
        return productDecription;
    }
    /**
     * 设置：移动端商品详情信息（图文说明信息， 包含HTML代码）
     */
    public void setMobileProductDecription(String mobileProductDecription) {
        this.mobileProductDecription = mobileProductDecription;
    }

    /**
     * 获取：移动端商品详情信息（图文说明信息， 包含HTML代码）
     */
    public String getMobileProductDecription() {
        return mobileProductDecription;
    }
    /**
     * 设置：评价详情id
     */
    public void setGoodsCommentsId(Long goodsCommentsId) {
        this.goodsCommentsId = goodsCommentsId;
    }

    /**
     * 获取：评价详情id
     */
    public Long getGoodsCommentsId() {
        return goodsCommentsId;
    }
    /**
     * 设置：商品类别
     */
    public void setGoodsCategoryId(Long goodsCategoryId) {
        this.goodsCategoryId = goodsCategoryId;
    }

    /**
     * 获取：商品类别
     */
    public Long getGoodsCategoryId() {
        return goodsCategoryId;
    }
    /**
     * 设置：主图地址
     */
    public void setGoodsImagePathId(String goodsImagePathId) {
        this.goodsImagePathId = goodsImagePathId;
    }

    /**
     * 获取：主图地址
     */
    public String getGoodsImagePathId() {
        return goodsImagePathId;
    }
    /**
     * 设置：关键字
     */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    /**
     * 获取：关键字
     */
    public String getKeywords() {
        return keywords;
    }
    /**
     * 设置：删除标识 0 ：未删除 1:删除
     */
    public void setDeleteFlag(Long deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    /**
     * 获取：删除标识 0 ：未删除 1:删除
     */
    public Long getDeleteFlag() {
        return deleteFlag;
    }
    /**
     * 设置：创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取：创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }
    /**
     * 设置：创建者
     */
    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取：创建者
     */
    public Long getCreateBy() {
        return createBy;
    }
    /**
     * 设置：更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取：更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }
}
