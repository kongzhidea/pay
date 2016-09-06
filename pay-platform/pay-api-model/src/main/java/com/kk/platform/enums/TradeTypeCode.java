package com.kk.platform.enums;

/**
 * JSAPI 微信公众号， 微信浏览器支付（数据库中配置为  WAP,service中特殊处理即可）
 * <p/>
 * 业务方调用的时候，传以下3中类型即可，payService中对自动把WAP根据需要解析成JSAPI：
 * NATIVE 扫码
 * App  app支付
 * WAP 浏览器，网页版支付
 * <p/>
 * 微信支付类型：
 * https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=4_2
 */
public enum TradeTypeCode {
    JSAPI(1, "WAP支付"), NATIVE(2, "扫码支付"), APP(3, "APP支付"), WAP(4, "WAP支付"), MICROPAY(5, "刷卡支付");
    private int id;
    private String name;

    TradeTypeCode(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static TradeTypeCode getTradeTypeCode(int id) {
        for (TradeTypeCode tradeTypeCode : TradeTypeCode.values()) {
            if (tradeTypeCode.getId() == id) {
                return tradeTypeCode;
            }
        }
        return null;
    }

    // type： NATIVE,APP,WAP 等
    public static TradeTypeCode getTradeTypeCode(String type) {
        return TradeTypeCode.valueOf(type);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
