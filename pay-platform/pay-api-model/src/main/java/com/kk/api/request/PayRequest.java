package com.kk.api.request;


import com.kk.api.ApiRequest;
import com.kk.platform.enums.PayTypeCode;
import com.kk.platform.enums.TradeTypeCode;
import com.kk.validate.Assert;

public class PayRequest extends ApiRequest {
    private String tradePayNo; // 业务方 交易流水号
    private int payAmount; // 支付金额，精确到分
    private PayTypeCode payType; // 微信，支付宝
    private TradeTypeCode tradeType; // 支付方式， 扫码，网页等
    private String subject; // 标题
    private String detail;// 描述
    private String clientIp; // 客户端Ip，如果客户端不设置，服务端设置为服务器的ip
    private String notifyUrl; // 回调url

    //支付成功页，  支付宝：页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问，  微信需要在前端自己设置
    private String returnUrl;
    private String openId; // 微信 openId
    private String currency; // 货币种类，目前仅支持 人民币
    private String extra; // 附加信息， 支付成功后会原封不动回调回去


    @Override
    public void validate() {
        super.validate();
        Assert.hasLength(tradePayNo, validateMissMsg("tradePayNo"));
        Assert.isTrue(payAmount > 0, "payAmount must greater than zero");
        Assert.notNull(payType, validateMissMsg("payType"));
        Assert.notNull(tradeType, validateMissMsg("tradeType"));
        Assert.hasLength(subject, validateMissMsg("subject"));
        Assert.hasLength(notifyUrl, validateMissMsg("notifyUrl"));
        if (payType == PayTypeCode.ALI_PAY && tradeType == TradeTypeCode.WAP) {
            Assert.hasLength(returnUrl, validateMissMsg("returnUrl"));
        }
        if (payType == PayTypeCode.WECHAT_PAY && (tradeType == TradeTypeCode.WAP || tradeType == TradeTypeCode.JSAPI)) {
            Assert.hasLength(openId, validateMissMsg("openId"));
        }
    }

    public String getTradePayNo() {
        return tradePayNo;
    }

    public void setTradePayNo(String tradePayNo) {
        this.tradePayNo = tradePayNo;
    }

    public int getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(int payAmount) {
        this.payAmount = payAmount;
    }

    public PayTypeCode getPayType() {
        return payType;
    }

    public void setPayType(PayTypeCode payType) {
        this.payType = payType;
    }

    public TradeTypeCode getTradeType() {
        return tradeType;
    }

    public void setTradeType(TradeTypeCode tradeType) {
        this.tradeType = tradeType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "PayRequest{" +
                ", tradePayNo='" + tradePayNo + '\'' +
                ", payAmount=" + payAmount +
                ", subject='" + subject + '\'' +
                ", detail='" + detail + '\'' +
                ", clientIp='" + clientIp + '\'' +
                ", notifyUrl='" + notifyUrl + '\'' +
                ", returnUrl='" + returnUrl + '\'' +
                ", extra='" + extra + '\'' +
                ", openId='" + openId + '\'' +
                '}';
    }
}
