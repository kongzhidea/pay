package com.alipay.demo.trade.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import com.alipay.demo.trade.model.builder.RequestBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

abstract class AbsAlipayService {
    protected Log log = LogFactory.getLog(getClass());

    protected void validateBuilder(RequestBuilder builder) {
        if (builder == null) {
            throw new NullPointerException("builder should not be NULL!");
        }

        if (!builder.validate())
            throw new IllegalStateException("builder validate failed! " + builder.toString());
    }

    protected AlipayResponse getResponse(AlipayClient client, AlipayRequest request) {
        try {
            AlipayResponse response = client.execute(request);
            if (response != null) {
                this.log.info(response.getBody());
            }
            return response;
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }
}
