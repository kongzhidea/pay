package com.kk.api.response;

import com.kk.api.ApiResponse;

/**
 * 同步订单状态 与第三方支付状态保持一致
 */
public class PaySynResponse extends ApiResponse {
    private String status;// ResultCode：SUCCESS,FAIL，  表示订单状态是否有变化。

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
