package com.platform.service;

import com.platform.entity.MemberEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 会员表Service接口
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2019-02-19 11:37:51
 */
public interface ApiMemberService {

    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    MemberEntity queryObject(Long id);

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    List<MemberEntity> queryList(Map<String, Object> map);

    /**
     * 分页统计总数
     *
     * @param map 参数
     * @return 总数
     */
    int queryTotal(Map<String, Object> map);

    /**
     * 保存实体
     *
     * @param member 实体
     * @return 保存条数
     */
    int save(MemberEntity member);

    /**
     * 根据主键更新实体
     *
     * @param member 实体
     * @return 更新条数
     */
    int update(MemberEntity member);

    /**
     * 根据主键删除
     *
     * @param id
     * @return 删除条数
     */
    int delete(Long id);

    /**
     * 根据主键批量删除
     *
     * @param ids
     * @return 删除条数
     */
    int deleteBatch(Long[] ids);

    /**
     *根据消费额度进行会员升级
     * @param monetary 充值金额
     * @param percentage  成长值与金额百分比
     * @param memberEntity 会员
     * @return
     */
    MemberEntity memberShipUpgrade(BigDecimal monetary, BigDecimal percentage , MemberEntity memberEntity,String growthType);

    /**
     * 消费支付后会员成长值和会员级别处理
     * @param monetary
     * @param userId
     * @return
     */
    MemberEntity memberShipUpgrade(BigDecimal monetary, Integer userId);
    /**
     * 充值开通或者续费vplus
     * @param money
     * @param userId
     * @param validIP
     * @param platFormType
     * @return
     */
    Map<String,Object> openOrRenewVplus(BigDecimal money,Integer userId,String validIP,Integer platFormType);

    /**
     * 易宝支付成功后并进行vplus权益赠送
     * @param tradeNo
     * @return
     */
    Map<String,Object> rechargeSuccessAndUpdateVplus(String tradeNo);

    /**
     * 通过userid查找
     * @param userId
     * @return
     */
    MemberEntity queryObjectByUserIdOrMobile(Integer userId,String mobile);

    /**
     * 根据用户id和手机号更新vplus权益
     * @param userId
     * @param mobile
     * @param money
     * @param type
     * @return
     */
    boolean updateMemberVplus(Integer userId,String mobile,BigDecimal money,Integer type);

    /**
     * 自动校验会员用户过期
     */
   void autoCheckMemberVplusValided();

    /**
     * 批量更新
     * @param list
     */
    void updateBatch(List<MemberEntity> list);


}
