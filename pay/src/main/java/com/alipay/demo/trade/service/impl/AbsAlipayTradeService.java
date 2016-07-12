package com.alipay.demo.trade.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.TradeStatus;
import com.alipay.demo.trade.model.builder.AlipayTradeCancelCententBuilder;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateContentBuilder;
import com.alipay.demo.trade.model.builder.AlipayTradeQueryCententBuilder;
import com.alipay.demo.trade.model.builder.AlipayTradeRefundContentBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.model.result.AlipayF2FQueryResult;
import com.alipay.demo.trade.model.result.AlipayF2FRefundResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.utils.Utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

abstract class AbsAlipayTradeService extends AbsAlipayService
        implements AlipayTradeService {
    protected static ExecutorService executorService = Executors.newCachedThreadPool();
    protected AlipayClient client;

    public AlipayF2FQueryResult queryTradeResult(AlipayTradeQueryCententBuilder builder) {
        AlipayTradeQueryResponse response = tradeQuery(builder);

        AlipayF2FQueryResult result = new AlipayF2FQueryResult(response);
        if (querySuccess(response)) {
            result.setTradeStatus(TradeStatus.SUCCESS);
        } else if (tradeError(response)) {
            result.setTradeStatus(TradeStatus.UNKNOWN);
        } else {
            result.setTradeStatus(TradeStatus.FAILED);
        }
        return result;
    }


    protected AlipayTradeQueryResponse tradeQuery(String outTradeNo) {
        return tradeQuery(new AlipayTradeQueryCententBuilder()
                .setOutTradeNo(outTradeNo));
    }

    protected AlipayTradeQueryResponse tradeQuery(AlipayTradeQueryCententBuilder builder) {

        validateBuilder(builder);

        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizContent(builder.toJsonString());
        this.log.info("trade.query bizContent:" + request.getBizContent());

        return (AlipayTradeQueryResponse) getResponse(this.client, request);
    }

    public AlipayF2FRefundResult tradeRefund(AlipayTradeRefundContentBuilder builder) {
        validateBuilder(builder);

        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizContent(builder.toJsonString());
        this.log.info("trade.refund bizContent:" + request.getBizContent());

        AlipayTradeRefundResponse response = (AlipayTradeRefundResponse) getResponse(this.client, request);

        AlipayF2FRefundResult result = new AlipayF2FRefundResult(response);
        if ((response != null) && ("10000".equals(response.getCode()))) {
            result.setTradeStatus(TradeStatus.SUCCESS);
        } else if (tradeError(response)) {
            result.setTradeStatus(TradeStatus.UNKNOWN);
        } else {
            result.setTradeStatus(TradeStatus.FAILED);
        }
        return result;
    }

    public AlipayF2FPrecreateResult tradePrecreate(AlipayTradePrecreateContentBuilder builder, String notifyUrl) {
        validateBuilder(builder);

        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setBizContent(builder.toJsonString());
        request.setNotifyUrl(notifyUrl);

        this.log.info("trade.precreate bizContent:" + request.getBizContent());

        AlipayTradePrecreateResponse response = (AlipayTradePrecreateResponse) getResponse(this.client, request);

        AlipayF2FPrecreateResult result = new AlipayF2FPrecreateResult(response);
        if ((response != null) && ("10000".equals(response.getCode()))) {
            result.setTradeStatus(TradeStatus.SUCCESS);
        } else if (tradeError(response)) {
            result.setTradeStatus(TradeStatus.UNKNOWN);
        } else {
            result.setTradeStatus(TradeStatus.FAILED);
        }
        return result;
    }

    public AlipayTradeCancelResponse tradeCancel(AlipayTradeCancelCententBuilder builder) {
        validateBuilder(builder);

        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
        request.setBizContent(builder.toJsonString());
        this.log.info("trade.cancel bizContent:" + request.getBizContent());

        return (AlipayTradeCancelResponse) getResponse(this.client, request);
    }

    protected AlipayTradeCancelResponse tradeCancel(String outTradeNo) {
        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
        String bizContent = "{'out_trade_no':'" + outTradeNo + "'}";
        request.setBizContent(bizContent);
        this.log.info("trade.cancel bizContent:" + request.getBizContent());

        return (AlipayTradeCancelResponse) getResponse(this.client, request);
    }

    protected AlipayTradeQueryResponse loopQueryResult(String outTradeNo) {
        AlipayTradeQueryResponse queryResult = null;
        for (int i = 0; i < Configs.getMaxQueryRetry(); i++) {
            Utils.sleep(Configs.getQueryDuration());

            AlipayTradeQueryResponse response = tradeQuery(outTradeNo);
            if (response != null) {
                if (stopQuery(response)) {
                    return response;
                }
                queryResult = response;
            }
        }
        return queryResult;
    }

    protected boolean stopQuery(AlipayTradeQueryResponse response) {
        if (("10000".equals(response.getCode())) && (
                ("TRADE_FINISHED".equals(response.getTradeStatus())) ||
                        ("TRADE_SUCCESS".equals(response.getTradeStatus())) ||
                        ("TRADE_CLOSED".equals(response.getTradeStatus())))) {

            return true;
        }

        return false;
    }

    protected AlipayTradeCancelResponse cancelPayResult(String outTradeNo) {
        AlipayTradeCancelResponse response = tradeCancel(outTradeNo);
        if (cancelSuccess(response)) {
            return response;
        }

        if (needRetry(response)) {
            this.log.warn("begin async cancel outTradeNo:" + outTradeNo);
            asyncCancel(outTradeNo);
        }
        return response;
    }

    protected void asyncCancel(final String outTradeNo) {
        executorService.submit(new Runnable() {
            public void run() {
                for (int i = 0; i < Configs.getMaxCancelRetry(); i++) {
                    Utils.sleep(Configs.getCancelDuration());

                    AlipayTradeCancelResponse response = AbsAlipayTradeService.this.tradeCancel(outTradeNo);
                    if ((AbsAlipayTradeService.this.cancelSuccess(response)) ||
                            (!AbsAlipayTradeService.this.needRetry(response))) {

                        return;
                    }
                }
            }
        });
    }

    protected AlipayTradePayResponse toPayResponse(AlipayTradeQueryResponse response) {
        AlipayTradePayResponse payResponse = new AlipayTradePayResponse();

        payResponse.setCode(querySuccess(response) ? "10000" : "40004");

        StringBuilder msg = new StringBuilder(response.getMsg())
                .append(" tradeStatus:")
                .append(response.getTradeStatus());
        payResponse.setMsg(msg.toString());
        payResponse.setSubCode(response.getSubCode());
        payResponse.setSubMsg(response.getSubMsg());
        payResponse.setBody(response.getBody());
        payResponse.setParams(response.getParams());
        payResponse.setBuyerLogonId(response.getBuyerLogonId());
        payResponse.setFundBillList(response.getFundBillList());
        payResponse.setOpenId(response.getOpenId());
        payResponse.setOutTradeNo(response.getOutTradeNo());
        payResponse.setReceiptAmount(response.getReceiptAmount());
        payResponse.setTotalAmount(response.getTotalAmount());
        payResponse.setTradeNo(response.getTradeNo());
        return payResponse;
    }

    protected boolean needRetry(AlipayTradeCancelResponse response) {
        return (response == null) ||
                ("Y".equals(response.getRetryFlag()));
    }

    protected boolean querySuccess(AlipayTradeQueryResponse response) {
        return (response != null) &&
                ("10000".equals(response.getCode())) && (
                ("TRADE_SUCCESS".equals(response.getTradeStatus())) ||
                        ("TRADE_FINISHED".equals(response.getTradeStatus())));
    }

    protected boolean cancelSuccess(AlipayTradeCancelResponse response) {
        return (response != null) &&
                ("10000".equals(response.getCode()));
    }

    protected boolean tradeError(AlipayResponse response) {
        return (response == null) ||
                ("20000".equals(response.getCode()));
    }
}
