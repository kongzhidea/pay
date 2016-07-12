package com.kk.pay.weixin.model.enums;

import com.kk.pay.base.service.Validator;

public enum PayBillField implements Validator {
    APP_ID("appid", true),
    MCH_ID("mch_id", true),
    DEVICE_INFO("device_info", false),
    NONCE_STR("nonce_str", true),
    SIGN("sign", true),
    BILL_DATE("bill_date", false),
    BILL_TYPE("bill_type", false),;

    private String field;
    private boolean required;

    PayBillField(String field, boolean required) {
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
