package com.kk.wechat.model;


import com.kk.wechat.annotation.ApiRequestField;

import java.util.Date;

/**
 * 微信统一下单 接口参数
 */
public class WechatPayPrePayModel extends WechatPayModel {
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

    //  非必填  交易开始时间  格式为yyyyMMddHHmmss
    @ApiRequestField(value = "time_start", required = false)
    private Date timeStart;

    // 非必填   交易结束时间  格式为yyyyMMddHHmmss
    @ApiRequestField(value = "time_expire", required = false)
    private Date timeExpire;

    // 非必填  商品标记，代金券或立减优惠功能的参数
    @ApiRequestField(value = "goods_tag", required = false)
    private String goodsTag;

    // 通知地址， 接受微信支付异步通知回调地址，不能携带参数。
    @ApiRequestField("notify_url")
    private String notifyUrl;

    // 交易类型，  JSAPI,NATIVE,APP
    @ApiRequestField("trade_type")
    private String tradeType;

    // 非必填  商品Id trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
    @ApiRequestField(value = "product_id", required = false)
    private String productId;

    // 非必填  指定支付方式  no_credit--指定不能使用信用卡支付
    @ApiRequestField(value = "limit_pay", required = false)
    private String limitPay;

    // 非必填 trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识
    @ApiRequestField(value = "openid", required = false)
    private String openId;

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

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(Date timeExpire) {
        this.timeExpire = timeExpire;
    }

    public String getGoodsTag() {
        return goodsTag;
    }

    public void setGoodsTag(String goodsTag) {
        this.goodsTag = goodsTag;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getLimitPay() {
        return limitPay;
    }

    public void setLimitPay(String limitPay) {
        this.limitPay = limitPay;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
