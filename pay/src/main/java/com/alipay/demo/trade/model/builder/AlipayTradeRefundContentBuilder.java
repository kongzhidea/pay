package com.alipay.demo.trade.model.builder;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.StringUtils;

public class AlipayTradeRefundContentBuilder extends RequestBuilder {

    @SerializedName("trade_no")
    private String tradeNo;

    @SerializedName("out_trade_no")
    private String outTradeNo;

    @SerializedName("refund_amount")
    private String refundAmount;

    @SerializedName("out_request_no")
    private String outRequestNo;

    @SerializedName("refund_reason")
    private String refundReason;

    @SerializedName("store_id")
    private String storeId;

    @SerializedName("alipay_store_id")
    private String alipayStoreId;

    @SerializedName("terminal_id")
    private String terminalId;

    public boolean validate() {
        if ((StringUtils.isEmpty(this.tradeNo)) &&
                (StringUtils.isEmpty(this.outTradeNo))) {
            throw new NullPointerException("trade_no and out_trade_no should not both be NULL!");
        }
        if (StringUtils.isEmpty(this.refundAmount)) {
            throw new NullPointerException("refund_amount should not be NULL!");
        }
        if (StringUtils.isEmpty(this.refundReason)) {
            throw new NullPointerException("refund_reson should not be NULL!");
        }
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("AlipayTradeRefundContentBuilder{");
        sb.append("outTradeNo='").append(this.outTradeNo).append('\'');
        if (StringUtils.isNotEmpty(this.tradeNo)) {
            sb.append(", tradeNo='").append(this.tradeNo).append('\'');
        }
        sb.append(", refundAmount='").append(this.refundAmount).append('\'');
        sb.append(", outRequestNo='").append(this.outRequestNo).append('\'');
        sb.append(", refundReason='").append(this.refundReason).append('\'');
        sb.append(", storeId='").append(this.storeId).append('\'');
        sb.append(", alipayStoreId='").append(this.alipayStoreId).append('\'');
        sb.append(", terminalId='").append(this.terminalId).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getOutTradeNo() {
        return this.outTradeNo;
    }

    public AlipayTradeRefundContentBuilder setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
        return this;
    }

    public AlipayTradeRefundContentBuilder setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
        return this;
    }

    public AlipayTradeRefundContentBuilder setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
        return this;
    }

    public AlipayTradeRefundContentBuilder setOutRequestNo(String outRequestNo) {
        this.outRequestNo = outRequestNo;
        return this;
    }

    public AlipayTradeRefundContentBuilder setRefundReason(String refundReason) {
        this.refundReason = refundReason;
        return this;
    }

    public AlipayTradeRefundContentBuilder setStoreId(String storeId) {
        this.storeId = storeId;
        return this;
    }

    public AlipayTradeRefundContentBuilder setAlipayStoreId(String alipayStoreId) {
        this.alipayStoreId = alipayStoreId;
        return this;
    }

    public AlipayTradeRefundContentBuilder setTerminalId(String terminalId) {
        this.terminalId = terminalId;
        return this;
    }

    public String getTradeNo() {
        return this.tradeNo;
    }

    public String getRefundAmount() {
        return this.refundAmount;
    }

    public String getOutRequestNo() {
        return this.outRequestNo;
    }

    public String getRefundReason() {
        return this.refundReason;
    }

    public String getStoreId() {
        return this.storeId;
    }

    public String getAlipayStoreId() {
        return this.alipayStoreId;
    }

    public String getTerminalId() {
        return this.terminalId;
    }
}
