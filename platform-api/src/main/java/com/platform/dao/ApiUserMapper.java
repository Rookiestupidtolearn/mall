package com.platform.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.platform.entity.QzMoneyRecordEntity;
import com.platform.entity.QzUserAccountEntity;
import com.platform.entity.SmsLogVo;
import com.platform.entity.UserVo;

/**
 * 用户
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-03-23 15:22:06
 */
public interface ApiUserMapper extends BaseDao<UserVo> {

    UserVo queryByMobile(String mobile);

    UserVo queryByOpenId(@Param("openId") String openId);

    /**
     * 获取用户最后一条短信
     *
     * @param user_id
     * @return
     */
    SmsLogVo querySmsCodeByUserId(@Param("user_id") Long user_id);
    SmsLogVo  querySmsCodeByMobile(String mobile);
    /**
     * 保存短信
     *
     * @param smsLogVo
     * @return
     */
    int saveSmsCodeLog(SmsLogVo smsLogVo);
    
    /**
    	根据用户id查询用户账户
    */
    QzUserAccountEntity queryUserAccount(Integer userId);
    
    /**
     * 查询用户资金流水
     * @return
     */
    List<QzMoneyRecordEntity> queryuserAccountDetail(Integer userId);
    
    /**根据条件查询实体
     * 
     * @param paramMap
     * @return
     */
	List<UserVo> queryUserInfo(Map paramMap);

    UserVo thridQueryUserInfo(Map paramMap);

    /**
     * 添加第三方用户
     * @return
     */
	 void saveFromThird(UserVo userVo);
}
