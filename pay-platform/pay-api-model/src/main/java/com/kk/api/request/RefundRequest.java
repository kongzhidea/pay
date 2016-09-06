package com.kk.api.request;


import com.kk.api.ApiRequest;
import com.kk.validate.Assert;
import org.apache.commons.lang.StringUtils;

/**
 * 订单退款，  退款时候使用 payOrderNo，refundAmount,refundReason。
 * <p/>
 * tradeRefundNo 建议不填，让支付平台自己生成
 * <p/>
 * tradePayNo 此字段没有使用。
 * payAmount 此字段没有使用
 */
public class RefundRequest extends ApiRequest {
    private String tradePayNo; // 业务方 支付订单号，  对应 payOrder表
    private String payOrderNo;// 用于微信，支付宝的  支付交易流水号， 唯一索引。  对应 payOrder表
    private String tradeRefundNo;// 业务方 退款订单号，如果业务方不设置则 支付平台自己生成
    private int payAmount; // 支付金额  对应 payOrder表
    private int refundAmount; // 退款金额
    private String refundReason; // 退款原因

    @Override
    public void validate() {
        super.validate();
        Assert.isTrue(refundAmount > 0, "refundAmount must greater than zero");
        Assert.hasLength(payOrderNo, validateMissMsg("payOrderNo"));
        if (StringUtils.isNotBlank(refundReason)) {
            if (refundReason.length() >= 200) {
                throw new IllegalArgumentException("refundReason is too long");
            }
            if (refundReason.contains("^") || refundReason.contains("|")
                    || refundReason.contains("#") || refundReason.contains("#")) {
                throw new IllegalArgumentException("refundReason cannot contain special character");
            }
        }
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

    public String getTradeRefundNo() {
        return tradeRefundNo;
    }

    public void setTradeRefundNo(String tradeRefundNo) {
        this.tradeRefundNo = tradeRefundNo;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }
}
