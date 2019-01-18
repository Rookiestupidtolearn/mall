package com.platform.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.ApiUserLevelMapper;
import com.platform.dao.ApiUserMapper;
import com.platform.entity.QzMoneyRecordEntity;
import com.platform.entity.QzUserAccountEntity;
import com.platform.entity.SmsLogVo;
import com.platform.entity.UserLevelVo;
import com.platform.entity.UserVo;
import com.platform.utils.RRException;
import com.platform.validator.Assert;


@Service
public class ApiUserService {
    @Autowired
    private ApiUserMapper userDao;
    @Autowired
    private ApiUserLevelMapper userLevelDao;

    public UserVo queryObject(Long userId) {
        return userDao.queryObject(userId);
    }

    public UserVo queryByOpenId(String openId) {
        return userDao.queryByOpenId(openId);
    }

    public List<UserVo> queryList(Map<String, Object> map) {
        return userDao.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return userDao.queryTotal(map);
    }

    public void save(String mobile, String password) {
        UserVo user = new UserVo();
        user.setMobile(mobile);
        user.setUsername(mobile);
        user.setPassword(DigestUtils.sha256Hex(password));
        user.setRegisterTime(new Date());
        userDao.save(user);
    }

    public void save(UserVo userVo) {
        userDao.save(userVo);
    }
    public UserVo saveFromThrid(UserVo userVo) {
        userDao.saveFromThrid(userVo);
        return userVo;
    }

    public void update(UserVo user) {
        userDao.update(user);
    }

    public void delete(Long userId) {
        userDao.delete(userId);
    }

    public void deleteBatch(Long[] userIds) {
        userDao.deleteBatch(userIds);
    }

    public UserVo queryByMobile(String mobile) {
        return userDao.queryByMobile(mobile);
    }

    public long login(String mobile, String password) {
        UserVo user = queryByMobile(mobile);
        Assert.isNull(user, "手机号或密码错误");

        //密码错误
        if (!user.getPassword().equals(DigestUtils.sha256Hex(password))) {
            throw new RRException("手机号或密码错误");
        }

        return user.getUserId();
    }

    public SmsLogVo querySmsCodeByUserId(Long user_id) {
    	SmsLogVo vo=  userDao.querySmsCodeByUserId(user_id);

    	
        return vo;
    }


    public int saveSmsCodeLog(SmsLogVo smsLogVo) {
        return userDao.saveSmsCodeLog(smsLogVo);
    }

    public String getUserLevel(UserVo loginUser) {
        String result = "普通用户";
        UserLevelVo userLevelVo = userLevelDao.queryObject(loginUser.getUserLevelId());
        if (null != userLevelVo) {
            result = userLevelVo.getName();
        }
        return result;
    }
    
    /**
     * 根据userId查询用户账户实体
     *
     * @param userid 主键
     * @return 实体
     */
    public QzUserAccountEntity queryUserAccount(Integer userId) {
		return userDao.queryUserAccount(userId);
	}

	/**
	 * 查询用户资金流水
	 * @param userId
	 * @return
	 */
	public List<QzMoneyRecordEntity> queryuserAccountDetail(Integer userId) {
		return userDao.queryuserAccountDetail(userId);
	}

	/**
	 * 根据条件查询用户信息
	 * @param Map
	 * map中可传参数：userid 用户id、username 用户名、gender 性别、nickname 昵称、mobile 手机号、weixin_openid 微信openId
	 * @return List<UserVo>
	 */
	public List<UserVo> queryUserInfo(Map paramMap) {
		return userDao.queryUserInfo(paramMap);
	}
    public UserVo thridQueryUserInfo(Map paramMap) {
        return userDao.thridQueryUserInfo(paramMap);
    }

    
}
