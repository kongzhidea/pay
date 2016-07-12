package com.kk.pay.weixin.model;

public class PayBillParam {
    private String appid; // 公众账号ID
    private String mchId; // 微信支付 商户号
    private String deviceInfo; //非必填 设备号  终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
    private String nonceStr; // 随机字符串
    private String sign; // 签名

    private String billDate;// 对账日期  下载对账单的日期，格式：20140603
    private String bill_type;// 账单类型 ALL，返回当日所有订单信息，默认值    SUCCESS，返回当日成功支付的订单    REFUND，返回当日退款订单

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
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

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getBill_type() {
        return bill_type;
    }

    public void setBill_type(String bill_type) {
        this.bill_type = bill_type;
    }
}
