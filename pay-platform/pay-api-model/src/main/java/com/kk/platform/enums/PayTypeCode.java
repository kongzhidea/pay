package com.kk.platform.enums;

/**
 * 支付类型
 */
public enum PayTypeCode {
    WECHAT_PAY(1, "微信"), ALI_PAY(2, "支付宝");

    private int id;
    private String name;

    PayTypeCode(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static PayTypeCode getPayType(int id) {
        for (PayTypeCode payTypeCode : PayTypeCode.values()) {
            if (payTypeCode.getId() == id) {
                return payTypeCode;
            }
        }
        return null;
    }

    // type：WECHAT_PAY，ALI_PAY
    public static PayTypeCode getPayType(String type) {
        return PayTypeCode.valueOf(type);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
