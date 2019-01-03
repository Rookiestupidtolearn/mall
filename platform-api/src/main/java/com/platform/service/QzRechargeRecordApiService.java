package com.platform.service;

import com.platform.dao.QzRechargeRecordMapper;
import com.platform.entity.QzRechargeRecordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("qzRechargeRecordApiService")
public class QzRechargeRecordApiService {

    @Autowired
    private QzRechargeRecordMapper qzRechargeRecordMapper;

    public boolean saveQzRechargeRecord(QzRechargeRecordEntity qzRechargeRecordEntity){

        int save = qzRechargeRecordMapper.save(qzRechargeRecordEntity);

        return save == 0 ? false : true ;
    }
}
