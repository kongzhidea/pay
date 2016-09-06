package com.kk.wechat.response;


import com.kk.wechat.annotation.ApiResponseField;

import java.util.Date;

/**
 * 微信刷卡支付  接口 返回值
 * <p/>
 * 目前比微信接口少了 cash_fee等字段
 */
public class WechatPayMicroPayResponse extends WechatPayResponse {
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

    // 错误代码  USERPAYING：用户支付中，需要输入密码， 此场景需要轮询订单，直到支付成功或超时。
    @ApiResponseField("err_code")
    private String errCode;

    // 错误代码描述
    @ApiResponseField("err_code_des")
    private String errCodeDes;

    // 以下字段 在return_code 和result_code都为SUCCESS的时候有返回

    // 交易类型 TradeType，刷卡支付为：MICROPAY
    @ApiResponseField("trade_type")
    private String tradeType;

    //付款银行	银行类型，采用字符串类型的银行标识
    @ApiResponseField("bank_type")
    private String bankType;

    // 总金额  单位为分
    @ApiResponseField("total_fee")
    private String totalFee;

    // 货币种类  默认人民币，CNY
    @ApiResponseField("fee_type")
    private String feeType;

    // 微信支付订单号，微信内部订单号
    @ApiResponseField("transaction_id")
    private String transactionId;

    // 客户订单号
    @ApiResponseField("out_trade_no")
    private String outTradeNo;

    // 附加数据， 统一下单时候 传过来的 再原样传回去
    @ApiResponseField("attach")
    private String attach;

    // 支付完成时间  格式为yyyyMMddHHmmss
    @ApiResponseField("time_end")
    private Date timeEnd;

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

}
