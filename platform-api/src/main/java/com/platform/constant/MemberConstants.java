package com.platform.constant;

public class MemberConstants {



    /**
     * 是否常量 0 否  1 是
     */
    public static class FlagType{
        public static  final Integer NO =0 ;
        public static  final Integer YES =1 ;
    }




    /**
     * 订单常量 0 初始花 1 成功 2 失败
     */
    public static class OrderType{

        public static  final Integer INIT =0 ;
        public static  final Integer SUCCESS =1 ;
        public static  final Integer FAIL =2 ;



    }
    /**
     * 充值平台类型 1 后台 2 三方 3 前端
     */
    public static class RechargePlatFormType{

        public static  final Integer BACKGROUND =1;
        public static  final Integer THIRD =2 ;
        public static  final Integer FRONT =3;



    }
    /**
     * 审核状态 1 初始化 2 同过 3 没审核通过拒绝
     */
    public static class AuditState{
        public static  final String INIT ="0";
        public static  final String PASS ="1" ;
        public static  final String REJECT ="2";
    }

    /**
     * 会员充值类型 1 -开通 2 续费
     */
    public static class OpenOrRenewType{
        public static  final String OPEN ="1";
        public static  final String RENEW ="2";
    }
    /**
     * 成长值类型 1 支付 2 充值
     */
    public static class GrowthType{
        public static  final String PAY ="1";
        public static  final String RECHARGE ="2";
    }

    /**
     * '资金变动类型 1-充值 2-克拉币 3-回滚/扣减冻结克拉币'
     */
    public static class MoneyChangeType{
        public static  final String PAY ="1";
        public static  final String CARAT_CURRENCY ="2";
        public static  final String ROLLBACK_CART ="3";
    }
    /**
     * 成长值类型 1 收入 2 支付
     */
    public static class PayMentType{
        public static  final String INCOME ="1";
        public static  final String EXPEND ="2";
    }

}
