package com.kk.pay.weixin.model.enums;

import com.kk.pay.base.service.Validator;

public enum PayQueryField implements Validator {
    APP_ID("appid", true),
    MCH_ID("mch_id", true),
    NONCE_STR("nonce_str", true),
    SIGN("sign", true),
    OUT_TRADE_NO("out_trade_no", false),
    TRANSACTION_ID("transaction_id", false),;

    private String field;
    private boolean required;

    PayQueryField(String field, boolean required) {
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
