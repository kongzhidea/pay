package com.kk.api.response;


import com.kk.api.ApiResponse;

import java.util.Date;

/**
 * 退款查询接口
 */
public class RefundQueryResponse extends ApiResponse {
    private String tradePayNo;// 业务方 支付订单号，  对应 payOrder表
    private String payOrderNo;// 用于微信，支付宝的  支付交易流水号， 唯一索引。  对应 payOrder表
    private String tradeRefundNo;// 业务方 退款订单号
    private String refundOrderNo;// 微信，支付宝 退款交易流水号，用于退款
    private String refundTime; // 退款时间   yyyy-MM-dd HH:mm:ss 格式
    private int payAmount; // 支付订单金额
    private int refundAmount; // 退款金额
    private int status;// 退款状态 RefundStatus
    private String refundReason;// 退款原因

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

    public String getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(String refundTime) {
        this.refundTime = refundTime;
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

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }
}
