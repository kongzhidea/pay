package com.kk.pay.weixin.model;

import java.util.HashMap;
import java.util.Map;

public class PayRefundQueryResult extends WeixinResult {
    private String appid;
    private String mchId;
    private String nonceStr;
    private String sign;
    private String resultCode; // 业务结果 SUCCESS/FAIL
    private String errCode;
    private String errCodeDes;

    private String deviceInfo;

    private String transactionId; // 微信支付订单号
    private String outTradeNo;// 客户订单号

    private String feeType; // 货币种类  默认人民币，CNY
    private String totalFee; // 总金额  单位为分

    private String cashFee; //  现金支付金额

    private String refundCount;// 退款笔数

    /**
     * 商户退款单号	out_refund_no_$n	是	String(32)	1217752501201407033233368018	商户退款单号
     * 微信退款单号	refund_id_$n	是	String(28)	1217752501201407033233368018	微信退款单号
     * 退款渠道	refund_channel_$n	否	String(16)	ORIGINAL     ORIGINAL—原路退款     BALANCE—退回到余额
     * 退款金额	refund_fee_$n	是	Int	100	退款总金额,单位为分,可以做部分退款
     * 代金券或立减优惠退款金额	coupon_refund_fee_$n	否	Int	100	代金券或立减优惠退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为现金，说明详见代金券或立减优惠
     * 代金券或立减优惠使用数量	coupon_refund_count_$n	否	Int	1	代金券或立减优惠使用数量 ,$n为下标,从0开始编号
     * 代金券或立减优惠批次ID	coupon_refund_batch_id_$n_$m	否	String(20)	100	批次ID ,$n为下标，$m为下标，从0开始编号
     * 代金券或立减优惠ID	coupon_refund_id_$n_$m	否	String(20)	10000 	代金券或立减优惠ID, $n为下标，$m为下标，从0开始编号
     * 单个代金券或立减优惠支付金额	coupon_refund_fee_$n_$m	否	Int	100	单个代金券或立减优惠支付金额, $n为下标，$m为下标，从0开始编号
     * 退款状态	refund_status_$n	是	String(16)	SUCCESS     退款状态：     SUCCESS—退款成功     FAIL—退款失败     PROCESSING—退款处理中     NOTSURE—未确定，需要商户原退款单号重新发起
     * CHANGE—转入代发，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，资金回流到商户的现金帐号，需要商户人工干预，通过线下或者财付通转账的方式进行退款。
     * 退款入账账户	refund_recv_accout_$n	是	String(64)	招商银行信用卡0403	取当前退款单的退款入账方
     * 1）退回银行卡：
     * {银行名称}{卡类型}{卡尾号}
     * 2）退回支付用户零钱:
     * 支付用户零钱
     */

    private Map<String, Object> resultMap = new HashMap<String, Object>();

    public Map<String, Object> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<String, Object> resultMap) {
        this.resultMap = resultMap;
    }

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

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
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

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getCashFee() {
        return cashFee;
    }

    public void setCashFee(String cashFee) {
        this.cashFee = cashFee;
    }

    public String getRefundCount() {
        return refundCount;
    }

    public void setRefundCount(String refundCount) {
        this.refundCount = refundCount;
    }
}