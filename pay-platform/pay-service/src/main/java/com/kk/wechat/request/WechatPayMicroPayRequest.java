package com.kk.wechat.request;


import com.kk.wechat.model.WechatPayMicroPayModel;
import com.kk.wechat.response.WechatPayMicroPayResponse;

/**
 * 刷卡支付， 没有notify_url
 * <p/>
 * https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_10&index=1
 * <p/>
 * 提醒1：提交支付请求后微信会同步返回支付结果。
 * 当返回结果为“系统错误”时，商户系统等待5秒后调用【查询订单API】，查询支付实际交易结果；
 * 当返回结果为“USERPAYING”时，商户系统可设置间隔时间(建议10秒)重新查询支付结果，直到支付成功或超时(建议30秒)；
 */
public class WechatPayMicroPayRequest implements WechatPayRequest<WechatPayMicroPayModel, WechatPayMicroPayResponse> {
    private WechatPayMicroPayModel model;

    @Override
    public String getApiAction() {
        return "/pay/micropay";
    }

    @Override
    public WechatPayMicroPayModel getModel() {
        return this.model;
    }

    @Override
    public void setModel(WechatPayMicroPayModel model) {
        this.model = model;
    }

    @Override
    public Class<WechatPayMicroPayModel> getObjectClass() {
        return WechatPayMicroPayModel.class;
    }

    @Override
    public Class<WechatPayMicroPayResponse> getResponseClass() {
        return WechatPayMicroPayResponse.class;
    }

    @Override
    public boolean requireCert() {
        return false;
    }
}
