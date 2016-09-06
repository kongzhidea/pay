package com.kk.test.api;

import com.alibaba.fastjson.JSON;
import com.kk.platform.enums.PayException;
import com.kk.platform.enums.PayStatus;
import com.kk.platform.enums.TradeTypeCode;
import com.kk.platform.model.PayChannel;
import com.kk.utils.ConsoleLogger;
import com.kk.utils.LocalIPUtil;
import com.kk.wechat.client.WechatPayClient;
import com.kk.wechat.exception.WechatPayException;
import com.kk.wechat.model.WechatPayMicroPayModel;
import com.kk.wechat.request.WechatPayMicroPayRequest;
import com.kk.wechat.response.WechatPayMicroPayResponse;
import org.junit.Test;

/**
 * 测试刷卡支付
 */
public class WeChatMicroPayTest {
    ConsoleLogger logger = new ConsoleLogger();

    @Test
    public void testPay(){
        PayChannel payChannel = new PayChannel();
        payChannel.setAppId("");
        payChannel.setApiKey("");
        payChannel.setMchId("");

        WechatPayClient client = new WechatPayClient(payChannel.getAppId(), payChannel.getMchId(), payChannel.getApiKey());

        WechatPayMicroPayRequest request = new WechatPayMicroPayRequest();

        // model中 appId,sign,mchId,nonceStr 在WechatClient中设置
        WechatPayMicroPayModel model = new WechatPayMicroPayModel();
        model.setOutTradeNo("kk_micr" + System.currentTimeMillis());
        model.setSpbillCreateIp(LocalIPUtil.getLocalAddr());
        model.setBody("刷卡支付测试");
        model.setDetail("刷卡支付测试");
        model.setTotalFee(1);

        //!!!!
        model.setAuthCode("");

        request.setModel(model);

        // 统一下单
        WechatPayMicroPayResponse response;
        try {
            response = client.execute(request);
        } catch (WechatPayException e) {
            // 支付失败
            logger.error(e.getMessage(), e);
            throw new PayException(e.getErrMsg());
        }
        logger.info(JSON.toJSONString(response));
    }
}
