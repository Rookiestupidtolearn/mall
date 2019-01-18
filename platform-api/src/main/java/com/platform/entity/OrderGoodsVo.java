package com.platform.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-15 08:03:40
 */
public class OrderGoodsVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    private Integer id;
    //订单Id
    private Integer order_id;
    //商品id
    private Integer goods_id;
    //商品名称
    private String goods_name;
    //商品序列号
    private String goods_sn;
    //产品Id
    private Integer product_id;
    //商品数量
    private Integer number;
    //市场价
    private BigDecimal market_price;
    //零售价格
    private BigDecimal retail_price;
    //商品规格详情
    private String goods_specifition_name_value;
    //虚拟商品
    private Integer is_real;
    //商品规格Ids
    private String goods_specifition_ids;
    //图片链接
    private String list_pic_url;
    
    //渠道
    private String channel;
    //创建时间
    private Date createTime;
    //订单的状态 0-初始;
    private Integer goodStatus;
    
    private BigDecimal goods_total_price ; //商品总价格
    private BigDecimal caller_price;           // 单品克拉金额
    private BigDecimal caller_total_price;  // 使用的克拉总金额
    private String logistics_no  ;         //物流单号 

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public Integer getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(Integer goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_sn() {
        return goods_sn;
    }

    public void setGoods_sn(String goods_sn) {
        this.goods_sn = goods_sn;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public BigDecimal getMarket_price() {
        return market_price;
    }

    public void setMarket_price(BigDecimal market_price) {
        this.market_price = market_price;
    }

    public BigDecimal getRetail_price() {
        return retail_price;
    }

    public void setRetail_price(BigDecimal retail_price) {
        this.retail_price = retail_price;
    }

    public String getGoods_specifition_name_value() {
        return goods_specifition_name_value;
    }

    public void setGoods_specifition_name_value(String goods_specifition_name_value) {
        this.goods_specifition_name_value = goods_specifition_name_value;
    }

    public Integer getIs_real() {
        return is_real;
    }

    public void setIs_real(Integer is_real) {
        this.is_real = is_real;
    }

    public String getGoods_specifition_ids() {
        return goods_specifition_ids;
    }

    public void setGoods_specifition_ids(String goods_specifition_ids) {
        this.goods_specifition_ids = goods_specifition_ids;
    }

    public String getList_pic_url() {
        return list_pic_url;
    }

    public void setList_pic_url(String list_pic_url) {
        this.list_pic_url = list_pic_url;
    }

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getGoodStatus() {
		return goodStatus;
	}

	public void setGoodStatus(Integer goodStatus) {
		this.goodStatus = goodStatus;
	}

	public BigDecimal getGoods_total_price() {
		return goods_total_price;
	}

	public void setGoods_total_price(BigDecimal goods_total_price) {
		this.goods_total_price = goods_total_price;
	}

	public BigDecimal getCaller_price() {
		return caller_price;
	}

	public void setCaller_price(BigDecimal caller_price) {
		this.caller_price = caller_price;
	}

	public BigDecimal getCaller_total_price() {
		return caller_total_price;
	}

	public void setCaller_total_price(BigDecimal caller_total_price) {
		this.caller_total_price = caller_total_price;
	}

	public String getLogistics_no() {
		return logistics_no;
	}

	public void setLogistics_no(String logistics_no) {
		this.logistics_no = logistics_no;
	}
    
    
}
