package com.platform.thirdrechard.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.platform.dao.ThirdPreCompanyRechargeRecordMapper;

import org.apache.shiro.util.CollectionUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.thirdrechard.entity.ThirdPreCompanyRechargeRecord;
import com.platform.thirdrechard.entity.ThirdPreCompanyRechargeRecordExample;
import com.platform.thirdrechard.entity.ThirdPreCompanyRechargeRecordExample.Criteria;

/**
 * 
 * 预充值记录服务
 * 
 * @author zct
 *
 */
@Service
public class PreRechargeRecordService {

	@Autowired
	private ThirdPreCompanyRechargeRecordMapper thirdPreCompanyRechargeRecordMapper;

	/**
	 * &#x4fdd;&#x5b58;&#x9884;&#x652f;&#x4ed8;&#x8bb0;&#x5f55;
	 *
	 * @param reRechargeRecord
	 * @return
	 */
	public Boolean savePreRechargeRecord(ThirdPreCompanyRechargeRecord reRechargeRecord) {

		int num = thirdPreCompanyRechargeRecordMapper.insert(reRechargeRecord);

		return num == 0 ? false : true;

	}

	/**
	 * 获取订单 -->三方订单号
	 * 
	 * @param thridOrder
	 * @return
	 */
	public ThirdPreCompanyRechargeRecord getPreRechargeRecordByThirdOrder(String thridOrder, String appId) {

		ThirdPreCompanyRechargeRecordExample preRechargeRecordExample = new ThirdPreCompanyRechargeRecordExample();

		preRechargeRecordExample.createCriteria().andOrderThirdEqualTo(thridOrder).andAppIdEqualTo(appId);

		List<ThirdPreCompanyRechargeRecord> preRechargeRecords = thirdPreCompanyRechargeRecordMapper.selectByExample(preRechargeRecordExample);

		if (CollectionUtils.isEmpty(preRechargeRecords)) {

			return null;

		}

		return preRechargeRecords.get(preRechargeRecords.size()-1);
	}

    /**
     * 获取订单 -->自己订单号
     *
     * @param orderNo
     * @return
     */
    public ThirdPreCompanyRechargeRecord getPreRechargeRecordByOrderNo(String orderNo ) {

        ThirdPreCompanyRechargeRecordExample preRechargeRecordExample = new ThirdPreCompanyRechargeRecordExample();

        preRechargeRecordExample.createCriteria().andOrderNoEqualTo(orderNo);

        List<ThirdPreCompanyRechargeRecord> preRechargeRecords = thirdPreCompanyRechargeRecordMapper.selectByExample(preRechargeRecordExample);

        if (CollectionUtils.isEmpty(preRechargeRecords)) {

            return null;

        }

        return preRechargeRecords.get(0);
    }




	
	public Boolean updateByPrimaryKeySelective(ThirdPreCompanyRechargeRecord record) {
		int updateByPrimaryKeySelective = thirdPreCompanyRechargeRecordMapper.updateByPrimaryKeySelective(record);
		return updateByPrimaryKeySelective > 0?true:false;		
	}
	
}
