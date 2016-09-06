package com.kk.api.request;

import com.kk.api.ApiRequest;

/**
 * 查询 商户号 是否支持此支付类型
 */
public class PayTypeRequest extends ApiRequest {
    private String tradeType; // TradeTypeCode

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }
}
