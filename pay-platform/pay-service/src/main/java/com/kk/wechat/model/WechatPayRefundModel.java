package com.kk.wechat.model;

import com.kk.wechat.annotation.ApiRequestField;

/**
 * 微信退款接口 参数
 */
public class WechatPayRefundModel extends WechatPayModel {
    // 非必填 设备号  终端设备号(门店号或收银设备ID)
    @ApiRequestField(value = "device_info", required = false)
    private String deviceInfo;

    // 微信订单号，优先使用
    @ApiRequestField("transaction_id")
    private String transactionId;

    // 商户订单号， 商户内部系统的订单号，  32个字符内，可包含字母。 微信支付要求商户订单号保持唯一性
    @ApiRequestField("out_trade_no")
    private String outTradeNo;

    // 商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
    @ApiRequestField("out_refund_no")
    private String outRefundNo;

    // 订单金额，精确到分
    @ApiRequestField("total_fee")
    private int totalFee;

    // 退款金额 ，精确到分
    @ApiRequestField("refund_fee")
    private int refundFee;

    // 货币种类	默认人民币
    @ApiRequestField(value = "refund_fee_type", required = false)
    private String refundFeeType;


    // 操作员帐号, 默认为商户号
    @ApiRequestField("op_user_id")
    private String opUserId;


    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public int getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(int refundFee) {
        this.refundFee = refundFee;
    }

    public String getRefundFeeType() {
        return refundFeeType;
    }

    public void setRefundFeeType(String refundFeeType) {
        this.refundFeeType = refundFeeType;
    }

    public String getOpUserId() {
        return opUserId;
    }

    public void setOpUserId(String opUserId) {
        this.opUserId = opUserId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }
}
