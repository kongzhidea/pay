package com.kk.platform.model;


import com.kk.platform.enums.PayTypeCode;
import com.kk.platform.enums.TradeTypeCode;

import java.util.Date;

/**
 * 退款订单
 */
public class RefundOrder {
    private int id;
    private int payChannelId;// 微信支付宝 商户信息， PayChannel

    private String payTypeCode;// PayTypeCode， 支付类型，微信 1，支付宝 2，  对应 payOrder表
    private String tradePayNo;// 业务方 支付订单号，  对应 payOrder表
    private String payOrderNo;// 用于微信，支付宝的  支付交易流水号， 唯一索引。  对应 payOrder表

    private String tradeRefundNo; // 业务方 退款订单号，如果业务方不设置则 支付平台自己生成， 和merchantId组成唯一索引。
    private String refundOrderNo; // 微信，支付宝 退款交易流水号，用于退款，  唯一索引

    private String payId;// 微信支付内部订单号(transaction_id)，  支付宝内部交易号(trade_no)，  对应 payOrder表

    private String refundId; // 微信退款单号

    private int payAmount; // 支付金额  对应 payOrder表

    private int refundAmount; // 退款金额
    private int status; // 退款状态 RefundStatus， 2退款成功
    private String errorCode;// 如果退款失败，则保存第三方返回的失败错误码
    private String errorMsg;
    private Date refundTime; // 退款时间

    private String merchantId;// 业务方 商户号 ,PayMerchant， 对应 payOrder表
    private String tradeType;// TradeTypeCode，支付类型，如扫码，app支付，wap支付等。  对应 payOrder表

    private String notifyUrl; // 退款成功后通知url, 预留字段
    private String refundReason; // 退款原因
    private Date createTime;//  创建时间

    public PayTypeCode getPayTypeCodeEnum() {
        return PayTypeCode.getPayType(payTypeCode);
    }

    public TradeTypeCode getTradeTypeCodeEnum() {
        return TradeTypeCode.getTradeTypeCode(tradeType);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPayTypeCode() {
        return payTypeCode;
    }

    public void setPayTypeCode(String payTypeCode) {
        this.payTypeCode = payTypeCode;
    }

    public String getTradePayNo() {
        return tradePayNo;
    }

    public void setTradePayNo(String tradePayNo) {
        this.tradePayNo = tradePayNo;
    }

    public String getPayOrderNo() {
        return payOrderNo;
    }

    public void setPayOrderNo(String payOrderNo) {
        this.payOrderNo = payOrderNo;
    }

    public String getTradeRefundNo() {
        return tradeRefundNo;
    }

    public void setTradeRefundNo(String tradeRefundNo) {
        this.tradeRefundNo = tradeRefundNo;
    }

    public String getRefundOrderNo() {
        return refundOrderNo;
    }

    public void setRefundOrderNo(String refundOrderNo) {
        this.refundOrderNo = refundOrderNo;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public int getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(int payAmount) {
        this.payAmount = payAmount;
    }

    public int getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(int refundAmount) {
        this.refundAmount = refundAmount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public int getPayChannelId() {
        return payChannelId;
    }

    public void setPayChannelId(int payChannelId) {
        this.payChannelId = payChannelId;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
