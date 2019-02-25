package com.platform.task;


import com.platform.service.ApiMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("autoCheckMemberVplusValidedTask")
public class AutoCheckMemberVplusValidedTask {


    @Autowired
    private ApiMemberService memberService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public void AutoCheckMemberVplusValided(){
        logger.info("【定时检查更新Vplus会员】任务开始。。。。");
        try {
            memberService.autoCheckMemberVplusValided();

        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("【定时检查更新Vplus会员】任务结束。。。。");

    }


}
