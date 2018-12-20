package com.platform.thirdrechard.service;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.platform.dao.ApiUserMapper;
import com.platform.dao.QzRechargeRecordDao;
import com.platform.dao.QzUserAccountMapper;
import com.platform.dao.ThirdRechargeRecordDao;
import com.platform.entity.QzRechargeRecordEntity;
import com.platform.entity.QzUserAccountVo;
import com.platform.entity.ThirdRechargeRecordEntity;
import com.platform.entity.UserVo;
import com.platform.thirdrechard.entity.RechargeResponseEntity;
import com.platform.thirdrechard.util.EncryptUtil;
import com.platform.utils.GenerateCodeUtil;

/**
 * 用户充值记录Service实现类
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-12-17 13:35:56
 */
@Service("rechargeBizService")
public class RechargeBizService {
	private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ThirdRechargeRecordDao  thirdRechargeRecordDao;
    @Autowired
    private ApiUserMapper userDao;
	@Autowired
	private QzRechargeRecordDao qzRechargeRecordDao;
   @Autowired
   private  QzUserAccountMapper qzUserAccountMapper;
    
	/**
	 * 充值
	 * @param encrypt
	 * @return
	 * @throws Exception
	 */
	public RechargeResponseEntity recharge(String encrypt) throws Exception {
		        // 解密
			 String data = EncryptUtil.aesDecrypt(encrypt);
			 logger.info("解密后的密文是"+data);
			   JSONObject jsonObject = JSONObject.parseObject(data);
			   RechargeResponseEntity  responseEntity =  this.checkOrder(jsonObject);
	           if (!responseEntity.getCode().equals("success")) {
				   return responseEntity;
			  }
			 //开始充值
	           ThirdRechargeRecordEntity   entity  =    thirdRechargeRecordDao.queryByThirdTradeNo(jsonObject.getString("thirdTradeNo"));
			    if (entity != null) {
			    	  responseEntity.setCode("error");
					  responseEntity.setMsg("请勿重复下订单");
					  return responseEntity;
				} 
			    ThirdRechargeRecordEntity nEntity =  JSONObject.parseObject(data, ThirdRechargeRecordEntity.class);
				RechargeResponseEntity result = new  RechargeResponseEntity();
					 //新充值
					result = this.newRecharge(nEntity);
				 return result;

	}

	/**
	 * 新充值
	 * @param entity
	 * @return
	 */
	@Transactional
	public  RechargeResponseEntity  newRecharge(ThirdRechargeRecordEntity   entity){
		logger.info("三方用户充值流水入参:"+entity.toString());
		RechargeResponseEntity  responseEntity = new  RechargeResponseEntity();
		if (StringUtils.isEmpty(entity.getMobile())) {
			responseEntity.setCode("1001");
			responseEntity.setMsg("手机号不能为空");
			return  responseEntity;
		}
		
    	synchronized (entity.getMobile()) {
    		//记录流水
    		entity.setState("0");
    		entity.setTradeNo(GenerateCodeUtil.buildBizNo());
    		entity.setVersion(0);
    		thirdRechargeRecordDao.save(entity);
    	
    		UserVo user = userDao.queryByMobile(entity.getMobile());
    		if (user == null) {
    			//暂不做处理
    			
    		}else {
				//给用户做充值
    			// 充值记录流水
				QzRechargeRecordEntity record = new QzRechargeRecordEntity();
				record.setShopUserId(entity.getId());
				record.setState("0");
				record.setOperateId(user.getUserId());
				record.setAmount(entity.getAmount());
				record.setMemo("三方充值");
				record.setTradeNo(GenerateCodeUtil.buildBizNo());
//				record.setMobile(entity.getMobile());
//				record.setRechargeType(entity.getRechargeType());
				logger.info("充值记录信息"+JSON.toJSONString(record));
				qzRechargeRecordDao.save(record);
    			
				QzUserAccountVo account = qzUserAccountMapper.queruUserAccountInfo(user.getUserId());
				// 操作用户余额
				if (account == null) {
					QzUserAccountVo accountEntity = new QzUserAccountVo();
					accountEntity.setShop_user_id(Integer.parseInt(user.getUserId().toString()));
					accountEntity.setAmount(entity.getAmount());
					accountEntity.setLast_update_time(new Date());
					logger.info("初次创建用户账户余额信息"+JSON.toJSONString(accountEntity));
					qzUserAccountMapper.save(accountEntity);
				} else {
					account.setLast_update_time(new Date());
					account.setAmount(entity.getAmount().add(account.getAmount()));
					logger.info("用户账户余额信息"+JSON.toJSONString(account));
					qzUserAccountMapper.update(account);
				}
				
			}

		}
		
	
		responseEntity.setCode("success");
		responseEntity.setMsg("充值成功");
		
		return  responseEntity;
		
	}
	
    public RechargeResponseEntity  checkOrder(JSONObject jsonObject){
    	RechargeResponseEntity responseEntity = new RechargeResponseEntity();
			logger.info("校验充值解密后的值：" + jsonObject.toString());
			// 手机号
			String mobile = jsonObject.getString("mobile");
			if (StringUtils.isEmpty(mobile)) {
				responseEntity.setCode("1001");
				responseEntity.setMsg("手机号错误");
				return responseEntity;
			}
			String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
			if (mobile.length() != 11) {
				responseEntity.setCode("1001");
				responseEntity.setMsg("手机号错误");
				return responseEntity;
			}
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(mobile);
			boolean isMatch = m.matches();

			if (!isMatch) {
				responseEntity.setCode("1001");
				responseEntity.setMsg("手机号错误");
				return responseEntity;
			}
			//金额
			String amount = jsonObject.getString("amount");
			  if (StringUtils.isEmpty(amount)) {
				  responseEntity.setCode("1002");
					responseEntity.setMsg("金额错误");
					return responseEntity;
			}
		        
		        Pattern pattern=Pattern.compile("\\d\\.\\d*|[1-9]\\d*|\\d*\\.\\d*|\\d"); 
		        Matcher match=pattern.matcher(amount); 
		        if(match.matches()==false){ 
		        	  responseEntity.setCode("1002");
						responseEntity.setMsg("金额错误");
						return responseEntity;
		        }
		        
		        Double checkAmount = Double.valueOf(amount);
		        if (checkAmount <=0) {
		      	  responseEntity.setCode("1002");
				  responseEntity.setMsg("金额应大于0");
				return responseEntity;
				}
		        
		        Double bigAmount = Double.valueOf(amount);
		        if (bigAmount > 100000) {
		        	  responseEntity.setCode("1002");
					  responseEntity.setMsg("充值金额不能大于10万元");
					  return responseEntity;
				}
		        
		        Pattern pattern2=Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
		        Matcher match2=pattern2.matcher(amount); 
		        if(match2.matches()==false){ 
		        	  responseEntity.setCode("1002");
					  responseEntity.setMsg("充值金额格式错误");
					  return responseEntity;
		        }
		        //订单编号
		        String thirdTradeNo = jsonObject.getString("thirdTradeNo");
			    if (StringUtils.isEmpty(thirdTradeNo)) {
			    	  responseEntity.setCode("1003");
					  responseEntity.setMsg("订单编号错误");
					  return responseEntity;
				}
			    if (thirdTradeNo.length()>50) {
			    	  responseEntity.setCode("1003");
					  responseEntity.setMsg("订单编号错误");
					  return responseEntity;
				}
			    
			    String rechargeType = jsonObject.getString("rechargeType");
			    if (StringUtils.isEmpty(rechargeType)) {
			    	  responseEntity.setCode("1004");
					  responseEntity.setMsg("充值渠错误");
					  return responseEntity;
				}
			    if (!(rechargeType.equals("2") || rechargeType.equals("3")) ) {
			    	  responseEntity.setCode("1004");
					  responseEntity.setMsg("充值渠错误");
					  return responseEntity;
				}
			    //检查订单号是否重复
			    ThirdRechargeRecordEntity   entity  =    thirdRechargeRecordDao.queryByThirdTradeNo(thirdTradeNo);
			    if (entity != null) {
			    	  responseEntity.setCode("1005");
					  responseEntity.setMsg("重复下订单");
					  return responseEntity;
				}
			    responseEntity.setCode("success");
			    responseEntity.setMsg("校验成功");
		        return responseEntity;
		  }

}
