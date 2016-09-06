package com.kk.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.kk.platform.enums.ResultCode;

/**
 * 返回给业务方的数据
 * <p/>
 * Response中 尽量使用基本类型， 如果使用自定义类，需要重写toString方法。
 * <p/>
 * 尽量不要使用Date， 如果使用，则需要保证返回给业务方的 date类型 转成 yyyy-MM-dd HH:mm:ss格式（response.toJsonString），与BeanUtil.object2Map保持一致。
 * <p/>
 * 返回给业务方时候，是转成json，
 * 计算签名的时候，调用的是 BeanUtil.object2Map方法， 里面对map,list做了处理，
 */
public class ApiResponse {
    private String code; // ResultCode：SUCCESS，FAIL， code为FAIL时候 不用校验参数
    private String msg;
    private String sign;
    private String merchantId;

    public ApiResponse() {
    }

    public ApiResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static boolean isSuccess(String code) {
        return ResultCode.SUCCESS.toString().equals(code);
    }

    public static ApiResponse createErrorResponse(String msg) {
        return new ApiResponse(ResultCode.FAIL.toString(), msg);
    }

    public String toJsonString() {
        return JSON.toJSONString(this, SerializerFeature.WriteDateUseDateFormat);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
}
