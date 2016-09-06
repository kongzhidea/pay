package com.kk.api.service;

import com.kk.api.request.PayTypeRequest;
import com.kk.api.response.PayTypeResponse;
import com.kk.platform.enums.PayTypeCode;
import com.kk.platform.enums.ResultCode;
import com.kk.platform.enums.TradeTypeCode;
import com.kk.platform.model.PayMerchant;
import com.kk.platform.model.PayMerchantChannel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConfigService extends AbstractPayService {

    public PayTypeResponse getPayTypes(PayTypeRequest request) {
        PayMerchant merchant = payMerchantService.getPayMerchant(request.getMerchantId());

        PayTypeResponse response = (PayTypeResponse) validate(merchant, request, PayTypeResponse.class);
        if (response != null) {
            return response;
        }

        response = new PayTypeResponse();

        List<PayMerchantChannel> merchantChannels;
        if (StringUtils.isNotBlank(request.getTradeType())) {
            merchantChannels = payMerchantChannelService.getPayMerchantChannel(merchant.getId(), request.getTradeType());
        } else {
            merchantChannels = payMerchantChannelService.getPayMerchantChannels(merchant.getId());
        }

        for (PayMerchantChannel merchantChannel : merchantChannels) {
            PayTypeCode type = PayTypeCode.getPayType(merchantChannel.getPayTypeId());
            TradeTypeCode tradeType = TradeTypeCode.getTradeTypeCode(merchantChannel.getTradeType());
            response.addPayType(type, tradeType);
        }
        response.setMerchantId(request.getMerchantId());
        response.setCode(ResultCode.SUCCESS.getValue());
        response.setMsg(ResultCode.SUCCESS.getValue());
        response.setSign(sign(response, merchant.getApiKey()));
        return response;
    }
}
