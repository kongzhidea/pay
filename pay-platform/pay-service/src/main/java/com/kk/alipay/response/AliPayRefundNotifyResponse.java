package com.kk.alipay.response;

import java.util.Date;

/**
 * 支付宝 退款通知
 */
public class AliPayRefundNotifyResponse extends AliPayResponse {
    private Date notifyTime; // 通知发送的时间。    格式为：yyyy-MM-dd HH:mm:ss。
    private String notifyType; // 通知类型 batch_refund_notify
    private String notifyId;
    private String signType;
    private String sign;
    private String batchNo; // 退款批次号
    private String successNum; // 退交易成功的笔数。    0<= success_num<= 总退款笔数。
    /**
     * 退款结果明细。
     * <p/>
     * 退手续费结果返回格式：交易号^退款金额^处理结果$退费账号^退费账户ID^退费金额^处理结果；
     * 不退手续费结果返回格式：交易号^退款金额^处理结果。
     * 若退款申请提交成功，处理结果会返回“SUCCESS”。
     */
    private String resultDetails;

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

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getSuccessNum() {
        return successNum;
    }

    public void setSuccessNum(String successNum) {
        this.successNum = successNum;
    }

    public String getResultDetails() {
        return resultDetails;
    }

    public void setResultDetails(String resultDetails) {
        this.resultDetails = resultDetails;
    }

    @Override
    public boolean isSuccess() {
        return "1".equals(successNum);
    }

    @Override
    public String getSign() {
        return sign;
    }


}
