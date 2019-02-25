package com.platform.constant;


import com.platform.entity.MemberGradeEntity;
import com.platform.service.MemberGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 系统启动时从数据库表中查出vip等级保存到map中
 */
@Component
public final class MemberGradeInit {
    @Autowired
    MemberGradeService memberGradeService;

    private final static Map<String, MemberGradeEntity> gradeMap = new HashMap<>();


    private MemberGradeInit() {

    }

    public static Map<String, MemberGradeEntity> getGradeMap() {
        return gradeMap;
    }

    @PostConstruct
    public void init() {
        Map<String, Object> map = new HashMap<String, Object>();
        List<MemberGradeEntity> list = memberGradeService.queryList(map);
        if (list != null || list.size() > 0) {
            for (MemberGradeEntity memberGradeEntity : list) {
                gradeMap.put(memberGradeEntity.getGrade(), memberGradeEntity);
            }
        }

    }


}
