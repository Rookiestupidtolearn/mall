package com.platform.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.platform.dao.MemberGradeDao;
import com.platform.entity.MemberGradeEntity;
import com.platform.service.MemberGradeService;

/**
 * 会员等级表Service实现类
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2019-02-20 10:12:40
 */
@Service("memberGradeService")
public class MemberGradeServiceImpl implements MemberGradeService {
    private Logger longger= LoggerFactory.getLogger(MemberGradeServiceImpl.class);

    @Autowired
    private MemberGradeDao memberGradeDao;

    @Override
    public MemberGradeEntity queryObject(Long id) {
        return memberGradeDao.queryObject(id);
    }

    @Override
    public List<MemberGradeEntity> queryList(Map<String, Object> map) {
        return memberGradeDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return memberGradeDao.queryTotal(map);
    }

    @Override
    public int save(MemberGradeEntity memberGrade) {
        return memberGradeDao.save(memberGrade);
    }

    @Override
    public int update(MemberGradeEntity memberGrade) {
        return memberGradeDao.update(memberGrade);
    }

    @Override
    public int delete(Long id) {
        return memberGradeDao.delete(id);
    }

    @Override
    public int deleteBatch(Long[] ids) {
        return memberGradeDao.deleteBatch(ids);
    }
}
