package com.kk.pay.weixin.model.enums;


import com.kk.pay.base.service.Validator;

/**
 * payOrderParam 参数 必填信息
 */
public enum PayOrderField implements Validator {
    APP_ID("appid", true),
    MCH_ID("mch_id", true),
    DEVICE_INFO("device_info", false),
    NONCE_STR("nonce_str", true),
    SIGN("sign", true),
    BODY("body", true),
    DETAIL("detail", false),
    ATTACH("attach", false),
    OUT_TRADE_NO("out_trade_no", true),
    FEE_TYPE("fee_type", false),
    TOTAL_FEE("total_fee", true),
    SPBILL_CREATE_IP("spbill_create_ip", true),
    TIME_START("time_start", false),
    TIME_EXPIRE("time_expire", false),
    GOODS_TAG("goods_tag", false),
    NOTIFY_URL("notify_url", true),
    TRADE_TYPE("trade_type", true),
    PRODUCT_ID("product_id", false),
    LIMIT_PAY("limit_pay", false),
    OPEN_ID("openid", false),;

    private String field;
    private boolean required;

    PayOrderField(String field, boolean required) {
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
