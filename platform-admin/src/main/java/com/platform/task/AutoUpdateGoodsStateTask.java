package com.platform.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.platform.entity.GoodsPureInterestRateEntity;
import com.platform.service.GoodsPureInterestRateService;
import com.platform.service.GoodsService;

/**
 * 根据毛利率自动更新商品上下架状态(毛利率>0 原下架jd商品自动上架 、毛利率<=0 原上架jd商品自动下架)
 *
 */
@Component("autoUpdateGoodsStateTask")
public class AutoUpdateGoodsStateTask {
	
	@Autowired
	private GoodsPureInterestRateService goodsPureInterestRateService;
	@Autowired
	private GoodsService goodsService;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public void updateGoodsState(){
		logger.info("【jd商品根据毛利率自动更新上下架状态】--开始");
		try{
			Map<String,Object> map = new HashMap<>();
			map.put("pureInterestRate","lessThan");
			map.put("isOnSale",1); //0 下架  1上架 2 申请上架  3申请下架  -1编辑状态
			//查询毛利率为0且上架的商品
			List<GoodsPureInterestRateEntity> onSalelist = goodsPureInterestRateService.queryGoodsByPureInterestRate(map);
			if(CollectionUtils.isEmpty(onSalelist)){
				logger.info("【jd商品根据毛利率自动更新上下架状态】--未查询到毛利率为0且为上架状态的商品");
			}else{
				logger.info("【jd商品根据毛利率自动更新上下架状态】--查询到毛利率为0且为上架状态的商品共"+onSalelist.size()+"条");
				Integer[] goodsIds = new Integer[onSalelist.size()];
				for(int i = 0;i < onSalelist.size();i++){
					goodsIds[i] = onSalelist.get(i).getGoodsId();
				}
				//调用下架接口处理商品
				goodsService.unSaleBatch(goodsIds,"jd");
				logger.info("【jd商品根据毛利率自动更新上下架状态】--下架毛利率为0且为上架状态的商品共"+goodsIds.length+"条成功");
			}
			map.put("pureInterestRate","greaterThan");
			map.put("isOnSale",0); //0 下架  1上架 2 申请上架  3申请下架  -1编辑状态
			//查询毛利率非0且下架的商品
			List<GoodsPureInterestRateEntity> notOnSalelist = goodsPureInterestRateService.queryGoodsByPureInterestRate(map);
			if(CollectionUtils.isEmpty(notOnSalelist)){
				logger.info("【jd商品根据毛利率自动更新上下架状态】--未查询到毛利率非0且为下架状态的商品");
			}else{
				logger.info("【jd商品根据毛利率自动更新上下架状态】--查询到毛利率非0且为下架状态的商品共"+notOnSalelist.size()+"条");
				//调用接口处理上架
				Integer[] goodsIds = new Integer[notOnSalelist.size()];
				for(int i = 0;i < notOnSalelist.size();i++){
					goodsIds[i] = notOnSalelist.get(i).getGoodsId();
				}
				goodsService.applySaleBatch(goodsIds,"jd");
				logger.info("【jd商品根据毛利率自动更新上下架状态】--申请上架毛利率非0且为下架状态的商品共"+goodsIds.length+"条成功");
			}
		}catch(Exception e){
			logger.info("【jd商品根据毛利率自动更新上下架状态】--异常"+e);
		}
		logger.info("【jd商品根据毛利率自动更新上下架状态】--结束");
	}
}
