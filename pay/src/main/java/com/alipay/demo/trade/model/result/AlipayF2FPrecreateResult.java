package com.alipay.demo.trade.model.result;

import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.model.TradeStatus;

public class AlipayF2FPrecreateResult
        implements Result {
    private TradeStatus tradeStatus;
    private AlipayTradePrecreateResponse response;

    public AlipayF2FPrecreateResult(AlipayTradePrecreateResponse response) {
        this.response = response;
    }

    public void setTradeStatus(TradeStatus tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public void setResponse(AlipayTradePrecreateResponse response) {
        this.response = response;
    }

    public TradeStatus getTradeStatus() {
        return this.tradeStatus;
    }

    public AlipayTradePrecreateResponse getResponse() {
        return this.response;
    }

    public boolean isTradeSuccess() {
        return (this.response != null) &&
                (TradeStatus.SUCCESS.equals(this.tradeStatus));
    }
}
