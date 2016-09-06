package com.kk.wechat.response;

import com.kk.wechat.annotation.ApiResponseField;

/**
 * 微信退款接口 返回结果
 */
public class WechatPayRefundResponse extends WechatPayResponse {
    @ApiResponseField("appid")
    private String appId;
    @ApiResponseField("mch_id")
    private String mchId;
    @ApiResponseField("device_info")
    private String deviceInfo;
    @ApiResponseField("nonce_str")
    private String nonceStr;
    @ApiResponseField("sign")
    private String sign;
    @ApiResponseField("err_code")
    private String errCode;
    @ApiResponseField("err_code_des")
    private String errCodeDes;

    // 商户退款单号
    @ApiResponseField("out_refund_no")
    private String outRefundNo;

    // 微信退款单号
    @ApiResponseField("refund_id")
    private String refundId;

    // 退款渠道 ORIGINAL—原路退款，    BALANCE—退回到余额
    @ApiResponseField("refund_channel")
    private String refundChannel;

    @ApiResponseField("total_fee")
    private String totalFee;

    @ApiResponseField("fee_type")
    private String feeType;

    // 微信订单号
    @ApiResponseField("transaction_id")
    private String transactionId;

    // 商户订单号， 商户内部系统的订单号，  32个字符内，可包含字母。 微信支付要求商户订单号保持唯一性
    @ApiResponseField("out_trade_no")
    private String outTradeNo;


    @ApiResponseField("refund_fee")
    private String refundFee;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
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

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrCodeDes() {
        return errCodeDes;
    }

    public void setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getRefundChannel() {
        return refundChannel;
    }

    public void setRefundChannel(String refundChannel) {
        this.refundChannel = refundChannel;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(String refundFee) {
        this.refundFee = refundFee;
    }
}
