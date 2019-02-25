package com.platform.dao;

import com.platform.entity.MemberEntity;

import java.util.List;

/**
 * 会员表Dao
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2019-02-19 11:37:51
 */
public interface ApiMemberDao extends BaseDao<MemberEntity> {


    MemberEntity queryObjectByUserIdOrMobile(Integer userId,String mobile);

    void updateBatch(List<MemberEntity> list);

}
