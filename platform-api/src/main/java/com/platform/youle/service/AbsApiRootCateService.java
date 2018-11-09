package com.platform.youle.service;

import java.util.Calendar;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.platform.youle.entity.RequestBaseEntity;
import com.platform.youle.entity.ResponseChildsEntity;
import com.platform.youle.entity.ResponseRootCateEntity;
import com.platform.youle.entity.ResponseRootDetailEntity;
import com.platform.youle.util.TokenUtil;

public abstract class AbsApiRootCateService implements IApiFuncServicein{

	 /**
	   * 初始请求参数
	   * @param entity
	   */
	  @Override
	  public void initRequestParam(RequestBaseEntity  entity){
	      entity.setWid(TokenUtil.wid);
	      entity.setTimestamp(String.valueOf(Calendar.getInstance().getTimeInMillis()));
	      entity.setToken(TokenUtil.token);
	  }
	
	  /**
	   * 实体转map
	   * @param entity
	   * @return
	   * @throws Exception
	   */
	  @Override
	  public Map<String,Object>  objectToMap(RequestBaseEntity entity) throws Exception{
	          String str = JSON.toJSONString(entity);
	          Map<String,Object> map = (Map<String,Object>) JSON.parse(str);
	          return map;
	  }
	
	  /**
	  * 获取时间戳
	  * @return
	  */
	   @Override
	   public String getTimestamp (){
     	  return String.valueOf(Calendar.getInstance().getTimeInMillis());
	   }
	   
	   /**
	    * 5.1获取一级产品分类
	    * @return
	    */
	   protected abstract ResponseRootCateEntity rootCate();
	   /**
	    * 5.2获取下级产品分类
	    * @return
	    */
	   protected abstract ResponseChildsEntity childs(Integer parentCate);
	   /**
	    * 5.3获取单个分类详情
	    * @param cid
	    * @return
	    */
	   protected abstract ResponseRootDetailEntity detial(Integer cid);
}
