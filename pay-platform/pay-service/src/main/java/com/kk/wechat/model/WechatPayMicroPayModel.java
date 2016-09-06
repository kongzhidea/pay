package com.kk.wechat.model;


import com.kk.wechat.annotation.ApiRequestField;

import java.util.Date;

/**
 * 微信刷卡支付 接口参数
 * <p/>
 * Note见：WechatPayMicroPayRequest
 * <p/>
 * 目前比微信接口少了 cash_fee等字段
 */
public class WechatPayMicroPayModel extends WechatPayModel {
    // 非必填 设备号  终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
    @ApiRequestField(value = "device_info", required = false)
    private String deviceInfo;

    // 商品描述  商品或支付简要描述
    @ApiRequestField("body")
    private String body;

    // 商品名称 明细列表，json格式，传输签名前请务必使用CDATA标签将JSON文本串保护起来。
    @ApiRequestField(value = "detail", required = false)
    private String detail;

    // 非必填 附加数据， 在查询Api和支付通知中 原样返回，该字段主要用于商户携带订单的自定义数据
    @ApiRequestField(value = "attach", required = false)
    private String attach;

    // 商户订单号， 商户内部系统的订单号，  32个字符内，可包含字母。 微信支付要求商户订单号保持唯一性
    @ApiRequestField("out_trade_no")
    private String outTradeNo;

    //非必填 默认人民币  CNY
    @ApiRequestField(value = "fee_type", required = false)
    private String feeType;

    // 订单总金额，单位为分
    @ApiRequestField("total_fee")
    private int totalFee;

    //  app和网页支付提交用户Id，native支付填调用微信支付Api的机器IP
    @ApiRequestField("spbill_create_ip")
    private String spbillCreateIp;

    // 非必填  商品标记，代金券或立减优惠功能的参数
    @ApiRequestField(value = "goods_tag", required = false)
    private String goodsTag;

    // 非必填  指定支付方式  no_credit--指定不能使用信用卡支付
    @ApiRequestField(value = "limit_pay", required = false)
    private String limitPay;

    // 授权码  扫码支付授权码，设备读取用户微信中的条码或者二维码信息
    @ApiRequestField(value = "auth_code")
    private String authCode;

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public String getSpbillCreateIp() {
        return spbillCreateIp;
    }

    public void setSpbillCreateIp(String spbillCreateIp) {
        this.spbillCreateIp = spbillCreateIp;
    }

    public String getGoodsTag() {
        return goodsTag;
    }

    public void setGoodsTag(String goodsTag) {
        this.goodsTag = goodsTag;
    }

    public String getLimitPay() {
        return limitPay;
    }

    public void setLimitPay(String limitPay) {
        this.limitPay = limitPay;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }
}
