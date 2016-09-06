package com.kk.alipay.client;


import com.alipay.api.DefaultAlipayClient;

/**
 * 扫码支付，订单查询
 */
public class MyDefaultAliPayClient extends DefaultAlipayClient {
    private static final String SERVER_URL = "https://openapi.alipay.com/gateway.do";
    private static final String DATA_TYPE = "json";
    private static final String CHARSET = "utf-8";

    public MyDefaultAliPayClient(String appId, String privateKey, String publicKey) {
        super(SERVER_URL, appId, privateKey, DATA_TYPE, CHARSET, publicKey);
    }

}
