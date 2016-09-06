package com.kk.platform.model;

import com.kk.platform.enums.TradeTypeCode;

/**
 * 商户允许使用的 支付方式，以及对应的 支付账号。
 * <p/>
 * 每个商户基础配置6个， 微信扫码，微信wap，微信app支付，  支付宝扫码，支付宝wap，支付宝app支付。
 */
public class PayMerchantChannel {
    private int id;
    private int payTypeId; // PayTypeCode， 微信 1， 支付宝 2
    private int payChannelId; // PayChannel
    private int payMerchantId; // PayMerchant
    private String tradeType; // TradeTypeCode， 如扫码支付，app支付等 。

    private PayChannel payChannel;
    private PayMerchant payMerchant;

    public TradeTypeCode getTradeTypeCode() {
        return TradeTypeCode.getTradeTypeCode(tradeType);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPayTypeId() {
        return payTypeId;
    }

    public void setPayTypeId(int payTypeId) {
        this.payTypeId = payTypeId;
    }

    public int getPayChannelId() {
        return payChannelId;
    }

    public void setPayChannelId(int payChannelId) {
        this.payChannelId = payChannelId;
    }

    public int getPayMerchantId() {
        return payMerchantId;
    }

    public void setPayMerchantId(int payMerchantId) {
        this.payMerchantId = payMerchantId;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public PayChannel getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(PayChannel payChannel) {
        this.payChannel = payChannel;
    }

    public PayMerchant getPayMerchant() {
        return payMerchant;
    }

    public void setPayMerchant(PayMerchant payMerchant) {
        this.payMerchant = payMerchant;
    }
}
