package com.alipay.demo.trade.model.hb;

public enum EquipStatus {
    ON("10"),

    OFF("20"),

    NORMAL("30"),

    SLEEP("40"),

    AWAKE("41");

    private String value;

    private EquipStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
