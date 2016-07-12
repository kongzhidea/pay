package com.kk.pay.weixin.model;

public class PayQueryParam {
    private String appid;
    private String mchId;
    private String nonceStr;
    private String sign;
    //  客户，微信 订单号 二选一；
    private String outTradeNo; // 客户订单号  商户系统内部的订单号，当没提供transaction_id时需要传这个。
    private String transactionId; // 微信订单号  微信的订单号，优先使用；

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
