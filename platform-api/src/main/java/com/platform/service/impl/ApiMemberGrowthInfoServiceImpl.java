package com.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.platform.dao.ApiMemberGrowthInfoDao;
import com.platform.entity.MemberGrowthInfoEntity;
import com.platform.service.ApiMemberGrowthInfoService;

/**
 * 会员成长值记录表Service实现类
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2019-02-19 11:37:52
 */
@Service("memberGrowthInfoService")
public class ApiMemberGrowthInfoServiceImpl implements ApiMemberGrowthInfoService {
    @Autowired
    private ApiMemberGrowthInfoDao memberGrowthInfoDao;

    @Override
    public MemberGrowthInfoEntity queryObject(Long id) {
        return memberGrowthInfoDao.queryObject(id);
    }

    @Override
    public List<MemberGrowthInfoEntity> queryList(Map<String, Object> map) {
        return memberGrowthInfoDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return memberGrowthInfoDao.queryTotal(map);
    }

    @Override
    public int save(MemberGrowthInfoEntity memberGrowthInfo) {
        return memberGrowthInfoDao.save(memberGrowthInfo);
    }

    @Override
    public int update(MemberGrowthInfoEntity memberGrowthInfo) {
        return memberGrowthInfoDao.update(memberGrowthInfo);
    }

    @Override
    public int delete(Long id) {
        return memberGrowthInfoDao.delete(id);
    }

    @Override
    public int deleteBatch(Long[] ids) {
        return memberGrowthInfoDao.deleteBatch(ids);
    }
}
