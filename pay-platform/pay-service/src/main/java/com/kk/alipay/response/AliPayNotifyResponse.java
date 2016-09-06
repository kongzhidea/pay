package com.kk.alipay.response;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 支付成功，异步通知
 * <p/>
 * 有一些字段暂时没哟用到，没有列在此处。
 * <p/>
 * 扫码支付和wap支付，  回调的参数不一样。 例如：扫码支付只有 buyer_logon_id， 但是wap支付中只有 buyer_email
 */
public class AliPayNotifyResponse extends AliPayResponse {
    private Date notifyTime; // 通知的发送时间。格式为yyyy-MM-dd HH:mm:ss
    private String notifyType;// 通知的类型	trade_status_sync
    private String notifyId; // 通知校验ID
    private String signType; // 签名方式，RSA
    private String sign; // 签名
    private String outTradeNo; // 商户订单号

    /**
     * WAIT_BUYER_PAY	交易创建，等待买家付款
     * TRADE_CLOSED	未付款交易超时关闭，或支付完成后全额退款
     * TRADE_SUCCESS	交易支付成功， 触发通知
     * TRADE_FINISHED	交易结束，不可退款
     */
    private String tradeStatus; // 交易状态
    private String sellerId;// 卖家支付宝用户号
    private String buyerId;// 买家支付宝用户号,买家支付宝账号对应的支付宝唯一用户号。以2088开头的纯16位数字
    private String buyerLogonId;// 买家支付宝账号， 用*号隐藏中间几位
    private String buyerEmail;// 买家支付宝账号，可以是Email或手机号码。
    private Date gmtPayment;// 交易付款时间
    private String tradeNo; // 支付宝交易号

    public boolean isSuccess() {
        if (StringUtils.isNotBlank(tradeStatus)) {
            if (tradeStatus.equals("TRADE_SUCCESS") || tradeStatus.equals("TRADE_FINISHED")) {
                return true;
            }
        }
        return false;
    }

    public String getBuyerEmailOrLogonId() {
        if (StringUtils.isNotBlank(buyerEmail)) {
            return buyerEmail;
        }
        if (StringUtils.isNotBlank(buyerLogonId)) {
            return buyerLogonId;
        }
        return "";
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Date getGmtPayment() {
        return gmtPayment;
    }

    public void setGmtPayment(Date gmtPayment) {
        this.gmtPayment = gmtPayment;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public Date getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(Date notifyTime) {
        this.notifyTime = notifyTime;
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getBuyerLogonId() {
        return buyerLogonId;
    }

    public void setBuyerLogonId(String buyerLogonId) {
        this.buyerLogonId = buyerLogonId;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }
}
