package com.kk.api.response;


import com.kk.api.ApiResponse;
import com.kk.platform.enums.PayTypeCode;
import com.kk.platform.enums.TradeTypeCode;

public class PayResponse extends ApiResponse {
    private String tradePayNo;
    private String payOrderNo;
    private PayTypeCode payType;
    private TradeTypeCode tradeType;
    // 如果是 扫码，则返回二维码内容，string类型， 如果是wap和app，则返回 支付凭据 map类型，转成json则为JsonObject
    private Object credential;

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

    public PayTypeCode getPayType() {
        return payType;
    }

    public void setPayType(PayTypeCode payType) {
        this.payType = payType;
    }

    public TradeTypeCode getTradeType() {
        return tradeType;
    }

    public void setTradeType(TradeTypeCode tradeType) {
        this.tradeType = tradeType;
    }

    public Object getCredential() {
        return credential;
    }

    public void setCredential(Object credential) {
        this.credential = credential;
    }
}
