package com.kk.wechat.exception;

public class WechatPayException extends Exception {
    private String errCode;
    private String errMsg;

    public WechatPayException(String errCode, String errMsg) {
        super(errCode + ":" + errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public WechatPayException(Throwable cause) {
        super(cause);
        this.errMsg = cause.getMessage();
    }

    public WechatPayException(String message, Throwable cause) {
        super(message, cause);
        this.errMsg = message;
    }

    public WechatPayException(String message) {
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
