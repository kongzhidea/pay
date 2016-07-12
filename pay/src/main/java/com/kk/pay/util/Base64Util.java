package com.kk.pay.util;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {
    // 将 s 进行 BASE64 编码
    public static String encode(String s) {
        if (s == null) return null;
        //return (new sun.misc.BASE64Encoder()).encode( s.getBytes() ).replaceAll("\n", "").replaceAll("\r", "");
        return new String(Base64.encodeBase64(s.getBytes()));
    }

    // 将 BASE64 编码的字符串 s 进行解码
    public static String decode(String s) {
        if (s == null) return null;
        //BASE64Decoder decoder = new BASE64Decoder();
        try {
            //byte[] b = decoder.decodeBuffer(s);
            byte[] b = Base64.decodeBase64(s);
            return new String(b);
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(encode("abcd"));
        System.out.println(decode("YWJjZA=="));
    }

}
