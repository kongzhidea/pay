package com.kk.api.response;


import com.kk.api.ApiResponse;

/**
 * 订单退款
 */
public class RefundResponse extends ApiResponse {
    private String tradePayNo;// 业务方 支付订单号，  对应 payOrder表
    private String payOrderNo;// 用于微信，支付宝的  支付交易流水号， 唯一索引。  对应 payOrder表
    private String tradeRefundNo;// 业务方 退款订单号，如果业务方不设置则 支付平台自己生成
    private String refundOrderNo;// 微信，支付宝 退款交易流水号，用于退款
    private int payAmount;// 支付金额  对应 payOrder表
    private int refundAmount;// 退款金额

    public RefundResponse() {
    }

    public RefundResponse(String code, String msg) {
        super(code, msg);
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
}
