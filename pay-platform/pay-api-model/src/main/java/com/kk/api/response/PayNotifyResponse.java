package com.kk.api.response;


import com.kk.api.ApiResponse;
import com.kk.platform.enums.PayTypeCode;


/**
 * 支付回调内容
 */
public class PayNotifyResponse extends ApiResponse {
    private PayTypeCode payType;
    private String extra;
    private int payAmount;
    private String tradePayNo;
    private String payOrderNo;
    private String status; // ResultCode：SUCCESS，FAIL
    private String payTime; // 支付时间， yyyy-MM-dd HH:mm:ss

    public PayNotifyResponse() {
    }

    public PayNotifyResponse(String code, String msg) {
        super(code, msg);
    }

    public PayTypeCode getPayType() {
        return payType;
    }

    public void setPayType(PayTypeCode payType) {
        this.payType = payType;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
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
}
