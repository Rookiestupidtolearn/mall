package com.platform.service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.platform.api.response.RechargeResponseEntity;
import com.platform.api.yeepay.EncryptUtil;
import com.platform.dao.QzRechargeRecordDao;
import com.platform.entity.QzRechargeRecordEntity;
import com.platform.entity.ThirdRechargeRecordEntity;
import com.platform.utils.R;

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
	private QzRechargeRecordDao qzRechargeRecordDao;
     
	
	/**
	 * 充值
	 * @param encrypt
	 * @return
	 * @throws Exception
	 */
	public RechargeResponseEntity recharge(String encrypt) throws Exception {
		RechargeResponseEntity responseEntity = new RechargeResponseEntity();
		responseEntity.setCode("error");
		responseEntity.setMsg("充值失败");
		// 解密
		// String data = EncryptUtil.aesDecrypt(encrypt);
		String aa = "{amount':1,'mobile':'13391506299','rechargeType':'2','thirdTradeNo':'123123123'}";
		String data = aa;
		if (StringUtils.isNotEmpty(data)) {
			logger.info("充值解密后的传值：" + data.toString());
			// 校验
			JSONObject jsonObject = JSONObject.parseObject(data);
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
			    
			    
			    
			    

		}

		return responseEntity;

	}

	public QzRechargeRecordEntity queryByThirdTradeNo(String thirdTradeNo) {
		return qzRechargeRecordDao.queryByThirdTradeNo(thirdTradeNo);
	}

	public List<QzRechargeRecordEntity> queryList(Map<String, Object> map) {
		return qzRechargeRecordDao.queryList(map);
	}

	public int queryTotal(Map<String, Object> map) {
		return qzRechargeRecordDao.queryTotal(map);
	}

	public int save(QzRechargeRecordEntity qzRechargeRecord) {
		return qzRechargeRecordDao.save(qzRechargeRecord);
	}

	public int update(QzRechargeRecordEntity qzRechargeRecord) {
		return qzRechargeRecordDao.update(qzRechargeRecord);
	}

	public int delete(Integer id) {
		return qzRechargeRecordDao.delete(id);
	}

	public int deleteBatch(Integer[] ids) {
		return qzRechargeRecordDao.deleteBatch(ids);
	}
}
