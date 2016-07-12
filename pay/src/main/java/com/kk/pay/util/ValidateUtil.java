package com.kk.pay.util;

import com.kk.pay.base.service.Validator;

import java.util.Map;

public class ValidateUtil {
    public static void validate(Validator[] values, Map<String, Object> data) {
        for (Validator v : values) {
            if (v.isRequired()) {
                if (data.get(v.getField()) == null) {
                    throw new IllegalArgumentException(v.getField() + "不能为空");
                }
            }
        }
    }
}
