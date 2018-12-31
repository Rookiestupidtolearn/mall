package com.platform.nideshopuser.service;

import com.platform.nideshopuser.entity.NideshopUserExample;
import com.platform.nideshopuser.entity.NideshopUserExample.Criteria;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.NideshopUserMapper;
import com.platform.nideshopuser.entity.NideshopUser;

import java.util.List;



@Service
public class NideshopuserService {

	@Autowired
	private  NideshopUserMapper nideshopUserMapper ;
	
	/**
	 * 保存用户
	 * @param record
	 * @return
	 */
	public Boolean  saveNideshopUser(NideshopUser record){
		int  num = 	nideshopUserMapper.insert(record);
		return num>0?true:false;
	}

	/**
	 * 通过手机号查询用户需信息 t
	 * @param phone
	 * @return
	 */
	public NideshopUser  selectByMobile(String phone){
		NideshopUserExample example = new NideshopUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andMobileEqualTo(phone);
		List<NideshopUser> nideshopUsers = nideshopUserMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(nideshopUsers)){
			return null;
		}
		return nideshopUsers.get(0);
	}

}
