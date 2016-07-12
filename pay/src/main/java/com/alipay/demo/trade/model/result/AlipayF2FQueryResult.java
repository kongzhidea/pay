package com.alipay.demo.trade.model.result;

import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.demo.trade.model.TradeStatus;

public class AlipayF2FQueryResult
        implements Result {
    private TradeStatus tradeStatus;
    private AlipayTradeQueryResponse response;

    public AlipayF2FQueryResult(AlipayTradeQueryResponse response) {
        this.response = response;
    }

    public void setTradeStatus(TradeStatus tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public void setResponse(AlipayTradeQueryResponse response) {
        this.response = response;
    }

    public TradeStatus getTradeStatus() {
        return this.tradeStatus;
    }

    public AlipayTradeQueryResponse getResponse() {
        return this.response;
    }

    public boolean isTradeSuccess() {
        return (this.response != null) &&
                (TradeStatus.SUCCESS.equals(this.tradeStatus));
    }
}
