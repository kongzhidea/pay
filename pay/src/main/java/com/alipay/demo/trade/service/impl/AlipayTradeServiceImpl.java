package com.alipay.demo.trade.service.impl;

import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.TradeStatus;
import com.alipay.demo.trade.model.builder.AlipayTradePayContentBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPayResult;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

public class AlipayTradeServiceImpl extends AbsAlipayTradeService {
    public AlipayTradeServiceImpl(ClientBuilder builder) {
        if (StringUtils.isEmpty(builder.getGatewayUrl())) {
            throw new NullPointerException("gatewayUrl should not be NULL!");
        }
        if (StringUtils.isEmpty(builder.getAppid())) {
            throw new NullPointerException("appid should not be NULL!");
        }
        if (StringUtils.isEmpty(builder.getPrivateKey())) {
            throw new NullPointerException("privateKey should not be NULL!");
        }
        if (StringUtils.isEmpty(builder.getFormat())) {
            throw new NullPointerException("format should not be NULL!");
        }
        if (StringUtils.isEmpty(builder.getCharset())) {
            throw new NullPointerException("charset should not be NULL!");
        }
        if (StringUtils.isEmpty(builder.getAlipayPublicKey())) {
            throw new NullPointerException("alipayPublicKey should not be NULL!");
        }

        this.client = new DefaultAlipayClient(builder.getGatewayUrl(), builder.getAppid(), builder.getPrivateKey(),
                builder.getFormat(), builder.getCharset(), builder.getAlipayPublicKey());
    }

    public AlipayF2FPayResult tradePay(AlipayTradePayContentBuilder builder) {
        validateBuilder(builder);

        String outTradeNo = builder.getOutTradeNo();

        AlipayTradePayRequest request = new AlipayTradePayRequest();
        request.setBizContent(builder.toJsonString());
        this.log.info("trade.pay bizContent:" + request.getBizContent());

        AlipayTradePayResponse response = (AlipayTradePayResponse) getResponse(this.client, request);

        AlipayF2FPayResult result = new AlipayF2FPayResult(response);
        if ((response != null) && ("10000".equals(response.getCode()))) {
            result.setTradeStatus(TradeStatus.SUCCESS);
        } else {
            if ((response != null) && ("10003".equals(response.getCode()))) {
                AlipayTradeQueryResponse loopQueryResponse = loopQueryResult(outTradeNo);
                return checkQueryAndCancel(outTradeNo, result, loopQueryResponse);
            }
            if (tradeError(response)) {
                AlipayTradeQueryResponse queryResponse = tradeQuery(outTradeNo);
                return checkQueryAndCancel(outTradeNo, result, queryResponse);
            }

            result.setTradeStatus(TradeStatus.FAILED);
        }

        return result;
    }

    private AlipayF2FPayResult checkQueryAndCancel(String outTradeNo, AlipayF2FPayResult result, AlipayTradeQueryResponse queryResponse) {
        if (querySuccess(queryResponse)) {
            result.setTradeStatus(TradeStatus.SUCCESS);
            result.setResponse(toPayResponse(queryResponse));
            return result;
        }

        AlipayTradeCancelResponse cancelResponse = cancelPayResult(outTradeNo);
        if (tradeError(cancelResponse)) {
            result.setTradeStatus(TradeStatus.UNKNOWN);
        } else {
            result.setTradeStatus(TradeStatus.FAILED);
        }
        return result;
    }

    public static class ClientBuilder {
        private String gatewayUrl;
        private String appid;
        private String privateKey;
        private String format;
        private String charset;
        private String alipayPublicKey;

        public AlipayTradeServiceImpl build() {
            if (StringUtils.isEmpty(this.gatewayUrl)) {
                this.gatewayUrl = Configs.getOpenApiDomain();
            }
            if (StringUtils.isEmpty(this.appid)) {
                this.appid = Configs.getAppid();
            }
            if (StringUtils.isEmpty(this.privateKey)) {
                this.privateKey = Configs.getPrivateKey();
            }
            if (StringUtils.isEmpty(this.format)) {
                this.format = "json";
            }
            if (StringUtils.isEmpty(this.charset)) {
                this.charset = "utf-8";
            }
            if (StringUtils.isEmpty(this.alipayPublicKey)) {
                this.alipayPublicKey = Configs.getAlipayPublicKey();
            }

            return new AlipayTradeServiceImpl(this);
        }

        public ClientBuilder setAlipayPublicKey(String alipayPublicKey) {
            this.alipayPublicKey = alipayPublicKey;
            return this;
        }

        public ClientBuilder setAppid(String appid) {
            this.appid = appid;
            return this;
        }

        public ClientBuilder setCharset(String charset) {
            this.charset = charset;
            return this;
        }

        public ClientBuilder setFormat(String format) {
            this.format = format;
            return this;
        }

        public ClientBuilder setGatewayUrl(String gatewayUrl) {
            this.gatewayUrl = gatewayUrl;
            return this;
        }

        public ClientBuilder setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
            return this;
        }

        public String getAlipayPublicKey() {
            return this.alipayPublicKey;
        }

        public String getAppid() {
            return this.appid;
        }

        public String getCharset() {
            return this.charset;
        }

        public String getFormat() {
            return this.format;
        }

        public String getGatewayUrl() {
            return this.gatewayUrl;
        }

        public String getPrivateKey() {
            return this.privateKey;
        }
    }
}
