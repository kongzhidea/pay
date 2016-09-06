package com.kk.alipay.response;

import java.util.Map;

/**
 * DefaultAliPayParser.parse 把json转成 实例，
 * <p/>
 * 支持驼峰式写法，与下划线写法的转换， 支持Date类型（yyyy-MM-dd HH:mm:ss格式）
 * <p/>
 * trade_status;  交易状态如下：
 * WAIT_BUYER_PAY	交易创建，等待买家付款
 * TRADE_CLOSED	未付款交易超时关闭，或支付完成后全额退款
 * TRADE_SUCCESS	交易支付成功， 触发通知
 * TRADE_FINISHED	交易结束，不可退款
 */
public abstract class AliPayResponse {
    private Map<String, String> params;

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public abstract boolean isSuccess();

    public abstract String getSign();
}
