package com.kk.api.request;


import com.kk.api.ApiRequest;
import org.apache.commons.lang.StringUtils;

/**
 * 退款查询
 */
public class RefundQueryRequest extends ApiRequest {
    private String refundOrderNo;// 微信，支付宝 退款交易流水号，用于退款， 优先使用此字段
    private String tradeRefundNo;// 业务方 退款订单号， （退款的时候如果业务方不设置则 支付平台自己生成，返回给业务方）

    @Override
    public void validate() {
        super.validate();
        if (StringUtils.isBlank(refundOrderNo) && StringUtils.isBlank(tradeRefundNo)) {
            throw new IllegalArgumentException("refundOrderNo and tradeRefundNo cannot be null at the same time!");
        }
    }

    public String getRefundOrderNo() {
        return refundOrderNo;
    }

    public void setRefundOrderNo(String refundOrderNo) {
        this.refundOrderNo = refundOrderNo;
    }

    public String getTradeRefundNo() {
        return tradeRefundNo;
    }

    public void setTradeRefundNo(String tradeRefundNo) {
        this.tradeRefundNo = tradeRefundNo;
    }
}
