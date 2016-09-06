package com.kk.api.request;


import com.kk.api.ApiRequest;
import org.apache.commons.lang.StringUtils;

/**
 * 优先根据 payOrderNo查询， 建议使用payOrderNo查询
 */
public class PayQueryRequest extends ApiRequest {
    private String tradePayNo;// 业务方 支付订单号，
    private String payOrderNo;// 用于微信，支付宝的  交易流水号， 唯一索引。


    @Override
    public void validate() {
        super.validate();
        if (StringUtils.isBlank(tradePayNo) && StringUtils.isBlank(payOrderNo)) {
            throw new IllegalArgumentException("tradePayNo and payOrderNo  can not be null at same time!");
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
}
