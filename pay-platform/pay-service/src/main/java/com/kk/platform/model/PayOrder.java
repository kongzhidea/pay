package com.kk.platform.model;


import com.kk.platform.enums.PayStatus;
import com.kk.platform.enums.PayTypeCode;
import com.kk.platform.enums.TradeTypeCode;

import java.util.Date;

/**
 * 支付订单
 */
public class PayOrder {
    private int id;
    private String payTypeCode; // PayTypeCode， 支付类型，微信 ，支付宝
    private String tradePayNo; // 业务方 支付订单号，
    private String payOrderNo;// 用于微信，支付宝的  商户交易流水号， 唯一索引。  out_trade_no
    private String prePayId;// 微信、支付宝返回的 给app或者网页的支付凭证，  客户端通过此信息调起支付界面。
    private String payId; // 微信支付内部订单号(transaction_id)，  支付宝内部交易号(trade_no)，  一般使用 payOrderNo,
    private String userIp;
    private int payAmount; // 支付金额，精确到分
    private Date payTime; // 支付时间
    private int status; // PayStatus
    private String errorCode; // 如果创建订单失败，则保存第三方返回的失败错误码
    private String errorMsg;
    private Date startTime;// 支付申请时间
    private Date expireTime; // 支付过期时间， 默认为2小时
    private String openId; // 微信为用户的openId，支付宝为buyer_id	买家支付宝用户号
    private String buyerLogonId;// 支付宝中：买家支付宝账号
    private String notifyUrl; // 回调业务方的url
    private String extra;// 附加信息， 支付完成后通知时候会原封不动返回业务方。
    private String subject; // 订单标题，微信中对应body字段，
    private String detail; // 订单描述，微信中对应detail字段，为json格式。 支付宝中对应 body字段，表示描述，字符串
    private String codeUrl; // 二维码链接
    private String merchantId; // 业务方 商户号 ,PayMerchant
    private String tradeType; // TradeTypeCode，支付类型，如扫码，app支付，wap支付等。
    private String returnUrl; // 支付成功页，  支付宝：页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问，  微信需要在前端自己设置
    private int refundAmount; // 退款额度，精确到分


    public PayTypeCode getPayTypeCodeEnum() {
        return PayTypeCode.getPayType(payTypeCode);
    }

    public TradeTypeCode getTradeTypeCodeEnum() {
        return TradeTypeCode.getTradeTypeCode(tradeType);
    }


    public String getStatusDesc() {
        PayStatus s = PayStatus.getPayStatus(status);
        if (s != null) {
            return s.getName();
        }
        return "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPayTypeCode() {
        return payTypeCode;
    }

    public void setPayTypeCode(String payTypeCode) {
        this.payTypeCode = payTypeCode;
    }

    public String getTradePayNo() {
        return tradePayNo;
    }

    public void setTradePayNo(String tradePayNo) {
        this.tradePayNo = tradePayNo;
    }

    public String getPayOrderNo() {
        return payOrderNo;
    }

    public void setPayOrderNo(String payOrderNo) {
        this.payOrderNo = payOrderNo;
    }

    public String getPrePayId() {
        return prePayId;
    }

    public void setPrePayId(String prePayId) {
        this.prePayId = prePayId;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public int getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(int payAmount) {
        this.payAmount = payAmount;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getBuyerLogonId() {
        return buyerLogonId;
    }

    public void setBuyerLogonId(String buyerLogonId) {
        this.buyerLogonId = buyerLogonId;
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

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public int getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(int refundAmount) {
        this.refundAmount = refundAmount;
    }
}
