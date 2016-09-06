package com.kk.wechat.request;


import com.kk.wechat.model.WechatPayQueryModel;
import com.kk.wechat.response.WechatPayQueryResponse;

/**
 * 微信查询接口
 */
public class WechatPayQueryRequest implements WechatPayRequest<WechatPayQueryModel, WechatPayQueryResponse> {
    private WechatPayQueryModel model;

    @Override
    public String getApiAction() {
        return "/pay/orderquery";
    }

    @Override
    public WechatPayQueryModel getModel() {
        return this.model;
    }

    @Override
    public void setModel(WechatPayQueryModel model) {
        this.model = model;
    }

    @Override
    public Class<WechatPayQueryModel> getObjectClass() {
        return WechatPayQueryModel.class;
    }

    @Override
    public Class<WechatPayQueryResponse> getResponseClass() {
        return WechatPayQueryResponse.class;
    }

    @Override
    public boolean requireCert() {
        return false;
    }
}
