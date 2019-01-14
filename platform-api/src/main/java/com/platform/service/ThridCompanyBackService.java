package com.platform.service;

import com.platform.utils.R;

public interface ThridCompanyBackService {


    /**
     * 生成商家rsa key
     * @return
     */
    R genKeyPair(String appId);

    /**
     * 绑定回调地址
     * @param appId
     * @param callBackUrl
     * @return
     */
    R updateCallBackUrl(String appId, String callBackUrl);

    /**
     * 查询
     * @param appId
     * @return
     */
    R getKeyPair(String appId);
}
