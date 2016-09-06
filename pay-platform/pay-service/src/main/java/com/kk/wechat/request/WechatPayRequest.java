package com.kk.wechat.request;

import com.kk.wechat.model.WechatPayModel;
import com.kk.wechat.response.WechatPayResponse;

/**
 * 微信支付 参数结构
 *
 * @param <R> WechatPayModel 请求参数
 * @param <T> WechatPayResponse  返回结果
 */
public interface WechatPayRequest<R extends WechatPayModel, T extends WechatPayResponse> {
    /**
     * 返回 WechatPayModel
     *
     * @return
     */
    R getModel();

    /**
     * 设置 WechatPayModel，设置请求参数
     *
     * @param model
     */
    void setModel(R model);

    /**
     * 获取支付接口 url后缀
     *
     * @return
     */
    String getApiAction();

    /**
     * 获取model.class
     *
     * @return
     */
    Class<R> getObjectClass();

    /**
     * 获取response.class
     *
     * @return
     */
    Class<T> getResponseClass();

    /**
     * 是否需要带上支付凭证
     *
     * @return
     */
    boolean requireCert();
}
