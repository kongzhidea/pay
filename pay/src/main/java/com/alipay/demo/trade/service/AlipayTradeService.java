package com.alipay.demo.trade.service;

import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.demo.trade.model.builder.*;
import com.alipay.demo.trade.model.result.AlipayF2FPayResult;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.model.result.AlipayF2FQueryResult;
import com.alipay.demo.trade.model.result.AlipayF2FRefundResult;

public interface AlipayTradeService {
    AlipayF2FPayResult tradePay(AlipayTradePayContentBuilder builder);

    AlipayF2FQueryResult queryTradeResult(AlipayTradeQueryCententBuilder builder);

    AlipayF2FRefundResult tradeRefund(AlipayTradeRefundContentBuilder builder);

    AlipayF2FPrecreateResult tradePrecreate(AlipayTradePrecreateContentBuilder builder, String notifyUrl);

    AlipayTradeCancelResponse tradeCancel(AlipayTradeCancelCententBuilder builder);
}
