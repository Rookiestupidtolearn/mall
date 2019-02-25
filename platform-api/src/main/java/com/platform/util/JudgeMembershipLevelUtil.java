package com.platform.util;

import com.platform.constant.GradeEnum;
import com.platform.constant.MemberGradeInit;
import com.platform.entity.MemberGradeEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * vip等级判断工具类
 */
public class JudgeMembershipLevelUtil {
    private static Logger logger = LoggerFactory.getLogger(JudgeMembershipLevelUtil.class);


    /**
     * 根据成长值判断会员等级
     *
     * @param membersGrowthValue 成长值
     * @param type  VIP  or  VPLUS
     * @return
     */
    public static String judgeLevel(Long membersGrowthValue, GradeEnum type) {

        if (membersGrowthValue==null){
            return null;
        }

        Map<String, MemberGradeEntity> map = MemberGradeInit.getGradeMap();
        for (String key : map.keySet()) {
            MemberGradeEntity mge = map.get(key);
            String claim = mge.getClaim();
            String[] claims = claim.split("-");

            if (membersGrowthValue >= Long.parseLong(claims[0]) && membersGrowthValue <= Long.parseLong(claims[1])) {
                if (type.equals(GradeEnum.VIP)) {
                    if (key.contains("vip")) {
                        return mge.getGrade();
                    }
                } else {
                    if (key.contains("VPLUS")) {
                        return mge.getGrade();
                    }
                }
            }

        }

        return null;

    }


}
