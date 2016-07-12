package com.kk.pay.weixin.model;

public class WeixinResult {
    private String returnCode;//SUCCESS/FAIL      此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
    private String returnMsg;// 返回信息，如非空，为错误原因 如，签名失败 ,参数格式校验错误

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }
}
