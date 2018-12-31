package com.platform.thirdrechard.service;

import java.util.List;

import com.platform.dao.ThridCompanyMapperDao;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.platform.thirdrechard.entity.ThridCompany;
import com.platform.thirdrechard.entity.ThridCompanyExample;
import com.platform.thirdrechard.entity.ThridCompanyExample.Criteria;

@Service
public class ThridCompanyService {

	@Autowired
	private ThridCompanyMapperDao thridCompanyMapper;
	
	/**
	 * 根据商户号获取商户
	 * @param appId
	 * @return
	 * 
	 */
	public ThridCompany  getThridCompanyByAppId(String appId){
		
		if(StringUtils.isEmpty(appId)){
			return null;
		}
		ThridCompanyExample  companyExample  =  new  ThridCompanyExample();
		Criteria criteria = companyExample.createCriteria();
		criteria.andAppidEqualTo(appId);
		 List<ThridCompany>   thridCompany  = thridCompanyMapper.selectByExample(companyExample);
		if(CollectionUtils.isEmpty(thridCompany)){
			return null;
		}
		return thridCompany.get(0);
	}
	
	
}
