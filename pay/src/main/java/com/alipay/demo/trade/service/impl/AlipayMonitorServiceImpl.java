package com.alipay.demo.trade.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.MonitorHeartbeatSynRequest;
import com.alipay.api.response.MonitorHeartbeatSynResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.builder.AlipayHeartbeatSynContentBuilder;
import com.alipay.demo.trade.service.AlipayMonitorService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

public class AlipayMonitorServiceImpl extends AbsAlipayService
        implements AlipayMonitorService {
    private AlipayClient client;

    public AlipayMonitorServiceImpl(ClientBuilder builder) {
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

        this.client = new DefaultAlipayClient(builder.getGatewayUrl(), builder.getAppid(), builder.getPrivateKey(),
                builder.getFormat(), builder.getCharset());
    }

    public MonitorHeartbeatSynResponse heartbeatSyn(AlipayHeartbeatSynContentBuilder builder) {
        return heartbeatSyn(builder, null);
    }

    public MonitorHeartbeatSynResponse heartbeatSyn(AlipayHeartbeatSynContentBuilder builder, String appAuthToken) {
        validateBuilder(builder);

        MonitorHeartbeatSynRequest request = new MonitorHeartbeatSynRequest();
        if (StringUtils.isNotEmpty(appAuthToken)) {
            request.putOtherTextParam("app_auth_token", appAuthToken);
        }

        request.setBizContent(builder.toJsonString());
        this.log.info("heartbeat.sync bizContent:" + request.getBizContent());

        return (MonitorHeartbeatSynResponse) getResponse(this.client, request);
    }

    public static class ClientBuilder {
        private String gatewayUrl;
        private String appid;
        private String privateKey;
        private String format;
        private String charset;

        public AlipayMonitorServiceImpl build() {
            if (StringUtils.isEmpty(this.gatewayUrl)) {
                this.gatewayUrl = Configs.getMcloudApiDomain();
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
            return new AlipayMonitorServiceImpl(this);
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
