package com.kk.wechat.model;


import com.kk.wechat.annotation.ApiRequestField;

import java.util.Date;

/**
 * 微信支付订单查询 接口参数
 * <p/>
 * 两个参数 至少传一个
 */
public class WechatPayQueryModel extends WechatPayModel {
    // 商户订单号：商户系统内部的订单号，当没提供transaction_id时需要传这个。
    @ApiRequestField(value = "out_trade_no", required = false)
    private String outTradeNo;

    // 微信订单号，优先使用
    @ApiRequestField(value = "transaction_id", required = false)
    private String transactionId;

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
