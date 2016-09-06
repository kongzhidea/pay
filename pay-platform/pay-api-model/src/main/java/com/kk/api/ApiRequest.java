package com.kk.api;

import com.alibaba.fastjson.JSON;
import com.kk.validate.Assert;

/**
 * 业务方传递的参数，
 * 不能够传 request中没有的字段，否则服务端采用bean方式来计算签名，bean中无此字段，计算签名会有问题，
 * 采用json来计算签名没有问题，但是在服务端一般没有采用此种方式， 接口方式和参数，一旦定下来基本上就不再改。
 * <p/>
 * Request中 尽量使用基本类型和Date， 如果使用自定义类，需要重写toString方法。
 * <p/>
 * 可以使用枚举，FastJson会自动处理枚举，BeanUtil会自动调用枚举的toString方法。
 * <p/>
 * 在发起请求之前，可以调用 request.validate方法，来本地校验参数是否齐全，
 * 每个继承ApiRequest的类也需要实现自己的validate方法，来校验参数
 */
public class ApiRequest {
    private String merchantId; // 商户号
    private String sign; // 参数签名

    public String toJsonString() {
        return JSON.toJSONString(this);
    }

    public void validate() {
        Assert.hasLength(merchantId, validateMissMsg("merchantId"));
    }

    protected static String validateMissMsg(String fieldName) {
        return String.format("[Assertion failed] - %s argument miss", fieldName);
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
