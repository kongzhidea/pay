package com.kk.pay.weixin.model.enums;

import com.kk.pay.base.service.Validator;

public enum PayRefundField implements Validator {
    APP_ID("appid", true),
    MCH_ID("mch_id", true),
    DEVICE_INFO("device_info", false),
    NONCE_STR("nonce_str", true),
    SIGN("sign", true),
    OUT_TRADE_NO("out_trade_no", false),
    TRANSACTION_ID("transaction_id", false),
    OUT_REFUND_NO("out_refund_no", true),
    TOTAL_FEE("total_fee", true),
    REFUND_FEE("refund_fee", true),
    REFUND_FEE_TYPE("refund_fee_type", false),
    OP_USER_ID("op_user_id", true),;

    private String field;
    private boolean required;

    PayRefundField(String field, boolean required) {
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
