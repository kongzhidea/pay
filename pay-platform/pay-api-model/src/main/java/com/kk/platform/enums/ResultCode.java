package com.kk.platform.enums;

public enum ResultCode {
    SUCCESS("SUCCESS"),FAIL("FAIL"), ;
    private String value;

    ResultCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
