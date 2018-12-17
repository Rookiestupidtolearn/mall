package com.platform.api;

import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.LoginUser;
import com.platform.cache.J2CacheUtils;
import com.platform.dao.ApiCartMapper;
import com.platform.dao.ApiTranInfoRecordMapper;
import com.platform.dao.ApiUserCouponMapper;
import com.platform.dao.GoodsCouponConfigMapper;
import com.platform.dao.QzUserAccountMapper;
import com.platform.entity.ApiTranInfoRecordVo;
import com.platform.entity.BuyGoodsVo;
import com.platform.entity.CartVo;
import com.platform.entity.GoodsCouponConfigVo;
import com.platform.entity.ProductVo;
import com.platform.entity.QzUserAccountVo;
import com.platform.entity.UserCouponVo;
import com.platform.entity.UserVo;
import com.platform.service.ApiProductService;
import com.platform.util.ApiBaseAction;
import com.platform.util.PayMatchingUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "商品购买")
@RestController
@RequestMapping("/api/buy")
public class ApiBuyController extends ApiBaseAction {
	@Autowired
    private GoodsCouponConfigMapper goodsCouponConfigMapper;
    @Autowired
    private  ApiUserCouponMapper apiUserCouponMapper;
    @Autowired
    private  QzUserAccountMapper qzUserAccountMapper;
    @Autowired
    private ApiCartMapper apiCartMapper;
    @Autowired
    private ApiTranInfoRecordMapper apiTranInfoRecordMapper;
    @Autowired
    private PayMatchingUtil payMatchingUtils;
    
    @ApiOperation(value = "商品添加")
    @PostMapping("/add")
    @Transactional
    public Object addBuy(@LoginUser UserVo loginUser) {
        JSONObject jsonParam = getJsonRequest();
        Integer goodsId = jsonParam.getInteger("goodsId");
        Integer productId = jsonParam.getInteger("productId");
        Integer number = jsonParam.getInteger("number");
        BuyGoodsVo goodsVo = new BuyGoodsVo();
        goodsVo.setGoodsId(goodsId);
        goodsVo.setProductId(productId);
        goodsVo.setNumber(number);
        J2CacheUtils.put(J2CacheUtils.SHOP_CACHE_NAME, "goods" + loginUser.getUserId() + "", goodsVo);
        return toResponsMsgSuccess("添加成功");
    }
    
    
    @ApiOperation(value = "取消商品")
    @PostMapping("/cancelAddBuyCoupons")
    public Object updateBuyUserCouponPrice(@LoginUser UserVo loginUser){
    	JSONObject jsonParam = getJsonRequest();
    	BuyGoodsVo goodsVO = (BuyGoodsVo) J2CacheUtils.get(J2CacheUtils.SHOP_CACHE_NAME, "goods" + loginUser.getUserId() + "");
    	if(goodsVO != null){
    		if("true".equals(jsonParam.getString("isBuy"))){
    		}
    	}
    	return this.toResponsObject(0, "执行成功", "");
    }
    
    
    
   
   
    /**
     * 生成平台币、优惠券流水
     * @param userId
     * @param tranType
     * @param TranFlag
     * @param tranAmount
     * @param currentAmount
     * @param remark
     */
    public void saveTranInfoRecord(Long userId,String tranType,String TranFlag,BigDecimal tranAmount,BigDecimal currentAmount
    		,String remark){
    	 ApiTranInfoRecordVo tranInfo = new ApiTranInfoRecordVo();
    	 tranInfo.setUser_id(userId);
    	 tranInfo.setTran_type(tranType);//1优惠券 2 平台币
    	 tranInfo.setTran_flag(TranFlag);//1收入 2支出
    	 tranInfo.setTran_amount(tranAmount);
    	 tranInfo.setCurrent_amount(currentAmount);
    	 tranInfo.setCreate_time(new Date());
    	 tranInfo.setRemark(remark);
    	 apiTranInfoRecordMapper.save(tranInfo);
    }
}
