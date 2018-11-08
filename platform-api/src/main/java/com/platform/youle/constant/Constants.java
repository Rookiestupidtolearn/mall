package com.platform.youle.constant;

/**
 * 常量
 */
public class Constants {

    /**
     * 路径常量
     */
    public static  class Urls {

        //测试
        public static String base_test_url = "http://beta.open.limofang.cn";

        //生产
        public static String base_prod_url = "http://open.fygift.com";

        //1.1获取所有商品ID
        public static String getAllProductIdsUrl = "/api/product/getAllProductIds.php";

        //1.2分页获取当前页商品ID
        public static String getProductIdsByPage = "/api/product/v2/getProductIdsByPage.php";

        //1.3获取单个商品详情
        public static String detial = "/api/product/detial.php";

        //1.4单个查询商品库存
        public static String stock  = "/api/product/stock.php";

        // 1.5批量查询商品库存
        public static String stockBatch = "/api/product/stockBatch.php";

        //1.6查询商品可售状态
        public static String saleStatus = "/api/product/saleStatus.php";

        //1.7查询商品协议价
        public static String getPrice = "/api/product/getPrice.php";

        //1.8批量查询商品可售状态
        public static String batchSaleStatus = "/api/product/batchSaleStatus.php";

        //1.9批量查询商品协议价
        public static String batchGetPrice = "/api/product/batchGetPrice.php";
        
        //2.3订单反查询接口, 用于确认订单是否创建成功
        public static String thirdOrder = "/api/order/thirdOrder.php";
        
        //2.4订单反查询接口, 用于确认订单是否创建成功
        public static String orderTrack = "/api/order/orderTrack.php";
    }




}
