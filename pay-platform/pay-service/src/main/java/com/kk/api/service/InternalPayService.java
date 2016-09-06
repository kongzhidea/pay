package com.kk.api.service;


import com.kk.api.response.PayNotifyResponse;
import com.kk.platform.model.PayChannel;
import com.kk.platform.model.PayOrder;

/**
 * 支付接口定义， 微信，支付宝支付逻辑均 继承此接口。
 */
public interface InternalPayService {
    /**
     * 统一下单接口
     *
     * @param payChannel 微信，支付宝支付账户
     * @param order      支付订单，包含 订单号，title，金额等信息。
     * @return 扫码支付则返回二维码（String），其他则返回 支付凭证(Map<string,string>)，返回给客户端，调起支付页面。
     */
    Object pay(PayChannel payChannel, PayOrder order);

    /**
     * @param notify 回调的内容
     * @return 返回支付宝，微信  商户订单号，payorder.payOrderNo
     */
    String getOutTradeNo(String notify);

    /**
     * 解析支付回调内容，校验签名
     *
     * @param channel 微信，支付宝支付账户
     * @param order   支付订单
     * @param notify  回调的内容
     * @return
     */
    PayNotifyResponse parse(PayChannel channel, PayOrder order, String notify);

    /**
     * 同步订单， 查询微信、支付宝 交易查询状态，来同步订单状态
     *
     * @param channel
     * @param order
     * @return
     */
    boolean synchronize(PayChannel channel, PayOrder order);
}
