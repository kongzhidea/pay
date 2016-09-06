package com.kk.api.service;


import com.kk.api.response.RefundResponse;
import com.kk.platform.model.PayChannel;
import com.kk.platform.model.RefundOrder;

/**
 * 订单退款
 */
public interface InternalRefundService {
    /**
     * 实际退款接口，  如果退款失败，一定要抛出异常，在RefundService中如果有异常则返回 业务方。
     *
     * @param payChannel
     * @param refundOrder
     * @return
     */
    Object refund(PayChannel payChannel, RefundOrder refundOrder);

    /**
     * 异步通知时候使用，  目前支付宝调用此方法
     *
     * @return
     */
    RefundResponse parse(PayChannel channel, RefundOrder refundOrder, String notify);
}
