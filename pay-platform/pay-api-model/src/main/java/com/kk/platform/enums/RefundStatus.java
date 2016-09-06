package com.kk.platform.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单退款状态
 */
public enum RefundStatus {
    CREATE_REFUND_SUCCESS(1, "创建退款订单成功"),
    REFUND_SUCCESS(2, "退款成功"),
    REFUND_FAIL(3, "退款失败"),
    REFUND_CHECKING(4, "退款中");

    private int value;
    private String name;

    RefundStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static RefundStatus getRefundStatus(int status) {
        for (RefundStatus it : RefundStatus.values()) {
            if (it.getValue() == status) {
                return it;
            }
        }
        return null;
    }

    private static Map<Integer, String> map = new HashMap<Integer, String>(RefundStatus.values().length);

    static {
        for (RefundStatus refundStatus : RefundStatus.values()) {
            map.put(refundStatus.getValue(), refundStatus.getName());
        }
    }

    public static Map<Integer, String> getRefundStatusMap() {
        return map;
    }
}
