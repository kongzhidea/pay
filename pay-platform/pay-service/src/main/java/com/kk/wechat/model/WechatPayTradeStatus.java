package com.kk.wechat.model;

/**
 * 微信支付-订单查询的时候 返回的交易状态
 * SUCCESS—支付成功
 * REFUND—转入退款
 * NOTPAY—未支付
 * CLOSED—已关闭
 * REVOKED—已撤销（刷卡支付）
 * USERPAYING--用户支付中
 * PAYERROR--支付失败(其他原因，如银行返回失败)
 */
public enum WechatPayTradeStatus {
    SUCCESS, REFUND, NOTPAY, CLOSED, REVOKED, USERPAYING, PAYERROR;
}
