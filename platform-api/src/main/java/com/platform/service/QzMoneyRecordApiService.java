package com.platform.service;

import com.platform.dao.QzMoneyRecordMapper;
import com.platform.entity.QzMoneyRecordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("qzMoneyRecordApiService")
public class QzMoneyRecordApiService {

    @Autowired
    private QzMoneyRecordMapper qzMoneyRecordMapper;


    /**
     * 查询 最后一笔记录
     * @param shopUserId
     * @return
     */
    public QzMoneyRecordEntity queryLastMoneyRecord(Integer shopUserId){

        return qzMoneyRecordMapper.queryLastMoneyRecord(shopUserId);
    }


    public boolean saveQzMoneyRecord(QzMoneyRecordEntity entity){
        int save = qzMoneyRecordMapper.save(entity);
        return  save == 0 ? false : true ;
    }

}
