package com.kk.wechat.response;


import com.kk.wechat.annotation.ApiResponseField;

/**
 * 微信支付  统一下单接口 返回值
 */
public class WechatPayPrePayResponse extends WechatPayResponse {
    // 公众账号ID
    @ApiResponseField("appid")
    private String appId;

    // 微信支付商户号
    @ApiResponseField("mch_id")
    private String mchId;

    // 设备号
    @ApiResponseField("device_info")
    private String deviceInfo;

    // 随机字符串
    @ApiResponseField("nonce_str")
    private String nonceStr;

    // 签名
    @ApiResponseField("sign")
    private String sign;

    // 错误代码
    @ApiResponseField("err_code")
    private String errCode;

    // 错误代码描述
    @ApiResponseField("err_code_des")
    private String errCodeDes;

    // 以下字段 在return_code 和result_code都为SUCCESS的时候有返回
    // 交易类型 TradeType
    @ApiResponseField("trade_type")
    private String tradeType;

    // 预支付交易会话标识  微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
    @ApiResponseField("prepay_id")
    private String prepayId;

    // trade_type为NATIVE是有返回，可将该参数值生成二维码展示出来进行扫码支付
    @ApiResponseField("code_url")
    private String codeUrl;

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

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
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

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrCodeDes() {
        return errCodeDes;
    }

    public void setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }
}
