package com.kk.platform.service;

import com.kk.platform.dao.PayMerchantChannelDao;
import com.kk.platform.enums.PayTypeCode;
import com.kk.platform.enums.TradeTypeCode;
import com.kk.platform.model.PayChannel;
import com.kk.platform.model.PayMerchant;
import com.kk.platform.model.PayMerchantChannel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PayMerchantChannelService {
    @Autowired
    private PayMerchantChannelDao payMerchantChannelDao;
    @Autowired
    private PayChannelService payChannelService;
    @Autowired
    private PayMerchantService payMerchantService;

    public void createPayMerchantChannel(PayMerchantChannel payMerchantChannel) {
        payMerchantChannelDao.insert(payMerchantChannel);
    }

    public PayMerchantChannel getPayMerchantChannel(int id) {
        PayMerchantChannel merchantChannel = payMerchantChannelDao.selectById(id);
        merchantChannel.setPayMerchant(payMerchantService.getPayMerchant(merchantChannel.getPayMerchantId()));
        merchantChannel.setPayChannel(payChannelService.getPayChannel(merchantChannel.getPayChannelId()));
        return merchantChannel;
    }

    public PayMerchantChannel getPayMerchantChannel(String merchantId, PayTypeCode payTypeCode, TradeTypeCode tradeType) {
        PayMerchant payMerchant = payMerchantService.getPayMerchant(merchantId);
        PayMerchantChannel merchantChannel = this.getPayMerchantChannel(payMerchant.getId(), payTypeCode.getId(), tradeType);
        if (merchantChannel == null) {
            return null;
        }
        merchantChannel.setPayMerchant(payMerchant);
        merchantChannel.setPayMerchant(payMerchantService.getPayMerchant(merchantChannel.getPayMerchantId()));
        return merchantChannel;
    }

    public List<PayMerchantChannel> getPayMerchantChannels(int merchantId) {
        List<PayMerchantChannel> merchantChannels = payMerchantChannelDao.selectByMerchantId(merchantId);
        return setPayChannels(merchantChannels);
    }

    private List<PayMerchantChannel> setPayChannels(List<PayMerchantChannel> merchantChannels) {
        if (CollectionUtils.isEmpty(merchantChannels)) {
            return merchantChannels;
        }
        Set<Integer> channelIds = new HashSet<Integer>();
        for (PayMerchantChannel merchantChannel : merchantChannels) {
            channelIds.add(merchantChannel.getPayChannelId());
        }
        List<PayChannel> channels = payChannelService.getPayChannel(channelIds);
        Map<Integer, PayChannel> channelMap = new HashMap<Integer, PayChannel>(channels.size());
        for (PayChannel channel : channels) {
            channelMap.put(channel.getId(), channel);
        }
        for (PayMerchantChannel merchantChannel : merchantChannels) {
            merchantChannel.setPayChannel(channelMap.get(merchantChannel.getPayChannelId()));
        }
        return merchantChannels;
    }

    public List<PayMerchantChannel> getPayMerchantChannel(int merchantId, String tradeType) {
        List<PayMerchantChannel> merchantChannels = payMerchantChannelDao.selectByTradeType(merchantId, tradeType);
        return setPayChannels(merchantChannels);
    }

    public PayMerchantChannel getPayMerchantChannel(int merchantId, int payTypeId, TradeTypeCode tradeType) {
        PayMerchantChannel merchantChannel = payMerchantChannelDao.selectByPayType(merchantId, payTypeId, tradeType.toString());
        if (merchantChannel == null) {
            return null;
        }
        merchantChannel.setPayChannel(payChannelService.getPayChannel(merchantChannel.getPayChannelId()));
        return merchantChannel;
    }

    public void delete(int id) {
        payMerchantChannelDao.deleteById(id);
    }
}
