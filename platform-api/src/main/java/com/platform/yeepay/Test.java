package com.platform.yeepay;

import com.platform.yeepay.utils.PaymobileUtils;

import java.util.TreeMap;

public class Test {

    public static void main(String[] args) {
        singleOrder("111");
    }

    public static void singleOrder(String orderid){


        TreeMap<String, Object> treeMap	= new TreeMap<String, Object>();

        treeMap.put("orderid", 	orderid);

        //第一步 生成AESkey及encryptkey
        String AESKey		= PaymobileUtils.buildAESKey();
        String encryptkey	= PaymobileUtils.buildEncyptkey(AESKey);

        //第二步 生成data
        String data			= PaymobileUtils.buildData(treeMap, AESKey);

        String merchantaccount				= PaymobileUtils.getMerchantaccount();
        String url							= PaymobileUtils.getRequestUrl(PaymobileUtils.QUERYORDERAPI_NAME);
        TreeMap<String, String> responseMap	= PaymobileUtils.httpGet(url, merchantaccount, data, encryptkey);

        System.out.println(responseMap.toString());

    }
}
