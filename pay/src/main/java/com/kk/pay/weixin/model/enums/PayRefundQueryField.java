package com.kk.pay.weixin.model.enums;

import com.kk.pay.base.service.Validator;

public enum PayRefundQueryField implements Validator {
    APP_ID("appid", true),
    MCH_ID("mch_id", true),
    DEVICE_INFO("device_info", false),
    NONCE_STR("nonce_str", true),
    SIGN("sign", true),
    OUT_TRADE_NO("out_trade_no", false),
    TRANSACTION_ID("transaction_id", false),
    OUT_REFUND_NO("out_refund_no", false),
    REFUND_ID("refund_id", false),;

    private String field;
    private boolean required;

    PayRefundQueryField(String field, boolean required) {
        this.field = field;
        this.required = required;
    }

    @Override
    public String getField() {
        return field;
    }

    @Override
    public boolean isRequired() {
        return required;
    }
}
