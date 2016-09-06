package com.kk.platform.model;


import com.kk.platform.enums.PayTypeCode;

/**
 * 存储微信、支付宝  apikey，公钥私钥等信息。
 *
 * 支付宝支付采用了RSA加密签名的安全通信机制，开发者可以通过支付宝的公钥验证消息的来源，同时使用自己的私钥进行信息加密
 */
public class PayChannel {
    private int id;
    private int payTypeId; // PayTypeCode.id
    private String payTypeName; // PayTypeCode.name
    private String payTypeCode; // 支付类型
    private String payChannelName; // 支付描述， 如 官方app支付， 扫码支付等。
    private String signType; // 加密方式，如 MD5 微信,  RSA 支付宝
    private String certFileId; // 仅微信使用，凭证文件Id，对应 FileResources
    private String apiKey; // 仅微信使用，开通微信支付后，会把 微信支付的账号，密码，以及 apikey发给开发者。  用于签名
    private String appId; // 微信为公众账号Id，  支付宝为20开头的一串数字（管理中心-我的应用）
    private String mchId; // 商户Id/合作伙伴Id， 例如 微信为12开头的一串数字(账户信息-微信支付商户号)，支付宝为（从我的应用-查看-使用者管理-使用者Id）
    private int status; // 1正常，2 不可用
    private String mchKey; // 商户私钥，  商户公钥需要在支付宝开放平台设置，
    private String platformKey; // 支付宝公钥，  又支付宝开放平台提供
    private int queryChannelId;// 对应PayChannel,0表示本身， 在调用支付宝查询订单(AliPayService.synchronize)功能时候，对应的开放平台秘钥Id。

    public PayTypeCode getPayTypeCodeEnum() {
        return PayTypeCode.getPayType(payTypeCode);
    }

    public String getMchKey() {
        return mchKey;
    }

    public void setMchKey(String mchKey) {
        this.mchKey = mchKey;
    }

    public String getPlatformKey() {
        return platformKey;
    }

    public void setPlatformKey(String platformKey) {
        this.platformKey = platformKey;
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

    public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }

    public String getPayTypeCode() {
        return payTypeCode;
    }

    public void setPayTypeCode(String payTypeCode) {
        this.payTypeCode = payTypeCode;
    }

    public String getPayChannelName() {
        return payChannelName;
    }

    public void setPayChannelName(String payChannelName) {
        this.payChannelName = payChannelName;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getCertFileId() {
        return certFileId;
    }

    public void setCertFileId(String certFileId) {
        this.certFileId = certFileId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getQueryChannelId() {
        return queryChannelId;
    }

    public void setQueryChannelId(int queryChannelId) {
        this.queryChannelId = queryChannelId;
    }
}
