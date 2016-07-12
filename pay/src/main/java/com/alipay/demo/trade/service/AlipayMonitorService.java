package com.alipay.demo.trade.service;

import com.alipay.api.response.MonitorHeartbeatSynResponse;
import com.alipay.demo.trade.model.builder.AlipayHeartbeatSynContentBuilder;

public interface AlipayMonitorService {
    MonitorHeartbeatSynResponse heartbeatSyn(AlipayHeartbeatSynContentBuilder paramAlipayHeartbeatSynContentBuilder);

    MonitorHeartbeatSynResponse heartbeatSyn(AlipayHeartbeatSynContentBuilder paramAlipayHeartbeatSynContentBuilder, String paramString);
}
