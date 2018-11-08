package com.platform.youle.service;

import com.platform.youle.entity.*;
import com.platform.youle.util.TokenUtil;

import java.util.Calendar;
import java.util.Map;

/**
 * 抽象类
 */
public abstract  class AbsApiFuncServicein implements IApiFuncServicein{

    /**
     * 初始请求参数
     * @param entity
     */
    public void initRequestParam(RequestBaseEntity  entity){
        entity.setWid(TokenUtil.wid);
        entity.setTimestamp(String.valueOf(Calendar.getInstance().getTimeInMillis()));
        entity.setToken(TokenUtil.token);
    }

    /**
     * 1.1获取所有商品ID
     * @return
     */
    protected abstract ResponseBaseEntity getAllProductIds();

    /**
     * 1.2分页获取当前页商品ID, 每页数据100条
     * @param page
     * @return
     */
    protected abstract ResponseProductEntity getProductIdsByPage(Integer page);

    /**
     *1.3获取单个商品详情
     * @param entity
     * @return
     */
    protected abstract ResponseSkuDetailEntity getSkuDetail(Map<String,Object> params);


    protected abstract ResponseProductEntity stock(String pid, String num, String address);


    /**
     * 1.6查询商品可售状态
     * @param 商品ID
     * @return
     */
    protected abstract ResponseSaleStatusEntity getsaleStatus(Integer pid);




}
