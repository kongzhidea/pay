package com.kk.alipay.exception;

public class AliPayException extends Exception {
    private String errCode;
    private String errMsg;

    public AliPayException(String errCode, String errMsg) {
        super(errCode + ":" + errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public AliPayException(Throwable cause) {
        super(cause);
        this.errMsg = cause.getMessage();
    }

    public AliPayException(String message, Throwable cause) {
        super(message, cause);
        this.errMsg = message;
    }

    public AliPayException(String message) {
        super(message);
        this.errMsg = message;
    }

    public String getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

}
