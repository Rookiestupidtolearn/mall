package com.platform.service;

import com.platform.dao.QzUserAccountMapper;
import com.platform.entity.QzUserAccountEntity;
import com.platform.entity.QzUserAccountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("qzUserAccountApiService")
public class QzUserAccountApiService {

    @Autowired
    private QzUserAccountMapper qzUserAccountMapper;



    public QzUserAccountVo queruUserAccountInfo(Integer id){
        return qzUserAccountMapper.queruUserAccountInfo(Long.valueOf(id));
    }

    public boolean updateUserAccount(QzUserAccountVo userAmountVo){
        int i = qzUserAccountMapper.updateUserAccount(userAmountVo);
        return i == 0 ? false : true ;
    }

    public boolean saveQzUserAccountVo(QzUserAccountVo qzUserAccountVo){
        int save = qzUserAccountMapper.save(qzUserAccountVo);
        return save == 0 ? false : true ;
    }
}
