package com.kk.platform.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

public class SnUtils {
    private static final int DEFAULT_LENGTH = 32;
    private static final int PREFIX_LENGTH = 4;

    /**
     * 生成 微信，支付宝等第三方支付的 订单号
     *
     * @param prefix
     * @return
     */
    public static String generateOrderNo(String prefix) {
        return generateOrderNo(prefix, DEFAULT_LENGTH);
    }

    public static String generateOrderNo(String prefix, int length) {
        if (StringUtils.isBlank(prefix) || prefix.length() < PREFIX_LENGTH || prefix.length() > length || length < DEFAULT_LENGTH) {
            throw new IllegalArgumentException("Prefix Illegal");
        }

        String no = prefix;

        if (prefix.length() > PREFIX_LENGTH) {
            no = prefix.substring(0, PREFIX_LENGTH);
        }

        no += RandomStringUtils.randomNumeric(PREFIX_LENGTH);

        no += DateTime.now().toString("yyyyMMddHHmmss");

        no += RandomStringUtils.randomNumeric(length - no.length());

        return no;
    }

    public static void main(String[] args) {
        System.out.println(generateOrderNo("1001", DEFAULT_LENGTH));
    }
}
