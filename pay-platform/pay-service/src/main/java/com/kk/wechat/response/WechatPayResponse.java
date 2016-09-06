package com.kk.wechat.response;

import com.kk.wechat.annotation.ApiResponseField;
import org.apache.commons.lang3.StringUtils;

/**
 * 微信支付 返回结果  基础参数
 * <p/>
 * WechatPayClient.convert转换， 会处理data类型，WechatPayTradeStatus类型，
 */
public abstract class WechatPayResponse {
    // 业务结果  SUCCESS/FAIL   !!!!!!!!!!!!!!!
    @ApiResponseField("result_code")
    protected String resultCode;

    //  return_code, return_msg 是所有返回结果的 基础。

    // SUCCESS/FAIL      此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
    @ApiResponseField("return_code")
    protected String returnCode;

    // 返回信息，如非空，为错误原因 如，签名失败 ,参数格式校验错误
    @ApiResponseField("return_msg")
    protected String returnMsg;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

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

    // 判断操作是否成功，即通信结果和业务结果均为 SUCCESS时候表示操作成功
    public boolean isSuccess() {
        return StringUtils.isNotBlank(resultCode) && ResultCode.SUCCESS.getValue().equalsIgnoreCase(resultCode);
    }
}
