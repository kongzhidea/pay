package com.kk.wechat.model;


import com.kk.wechat.annotation.ApiRequestField;


/**
 * 微信支付接口的 基础 参数
 *
 * 返回值中，java类中字段可以比 微信返回的xml结果少字段， 在WechatPayClient中对返回值计算签名的时候，是对xml进行校验签名。
 *
 * WechatPayClient.convert转换， 会处理data类型
 */
public class WechatPayModel {
    // 公众账号ID
    @ApiRequestField("appid")
    protected String appId;

    // 随机字符串
    @ApiRequestField("nonce_str")
    protected String nonceStr;

    // 签名
    @ApiRequestField("sign")
    protected String sign;

    // 微信支付 商户号
    @ApiRequestField("mch_id")
    protected String mchId;

    public WechatPayModel() {
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
