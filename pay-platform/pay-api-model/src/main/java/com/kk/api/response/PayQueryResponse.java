package com.kk.api.response;


import com.kk.api.ApiResponse;

/**
 * 订单查询结果
 */
public class PayQueryResponse extends ApiResponse {
    private String merchantId;
    private String ext;
    private int payAmount;
    private String tradePayNo;
    private String payOrderNo;
    private String status; // PayOrder.status, PayStatus
    private String payTime; // 支付时间 yyyy-MM-dd HH:mm:ss
    private String prePayId;// 微信、支付宝返回的 给app或者网页的支付凭证，  客户端通过此信息调起支付界面。
    private String subject; // 订单标题，微信中对应body字段
    private String detail; // 订单描述，微信中对应
    private String codeUrl; // 二维码链接
    private String payType;// PayTypeCode，支付类型， 微信，支付宝
    private String tradeType; // TradeTypeCode，支付方式，如扫码，app支付，wap支付等。
    private int refundAmount; // 退款额度，精确到分


    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public int getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(int payAmount) {
        this.payAmount = payAmount;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getPrePayId() {
        return prePayId;
    }

    public void setPrePayId(String prePayId) {
        this.prePayId = prePayId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public int getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(int refundAmount) {
        this.refundAmount = refundAmount;
    }
}
