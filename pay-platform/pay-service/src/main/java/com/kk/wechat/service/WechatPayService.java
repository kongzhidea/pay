package com.kk.wechat.service;

import com.kk.api.response.PayNotifyResponse;
import com.kk.api.service.InternalPayService;
import com.kk.platform.enums.PayException;
import com.kk.platform.enums.PayStatus;
import com.kk.platform.enums.TradeTypeCode;
import com.kk.platform.model.PayChannel;
import com.kk.platform.model.PayOrder;
import com.kk.platform.service.PayOrderService;
import com.kk.util.DateUtil;
import com.kk.util.SignUtils;
import com.kk.utils.WebPropertiesUtil;
import com.kk.wechat.client.WechatPayClient;
import com.kk.wechat.exception.WechatPayException;
import com.kk.wechat.model.WechatPayPrePayModel;
import com.kk.wechat.model.WechatPayQueryModel;
import com.kk.wechat.model.WechatPayTradeStatus;
import com.kk.wechat.request.WechatPayPrePayRequest;
import com.kk.wechat.request.WechatPayQueryRequest;
import com.kk.wechat.response.ResultCode;
import com.kk.wechat.response.WechatPayPayNotifyResponse;
import com.kk.wechat.response.WechatPayPrePayResponse;
import com.kk.wechat.response.WechatPayQueryResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class WechatPayService implements InternalPayService {
    private Log logger = LogFactory.getLog(this.getClass());

    // 通知url必须为直接可访问的url，不能携带参数
    private String notifyUrl = WebPropertiesUtil.getInstance().getValue("wechat.pay.notify.url");

    @Autowired
    private PayOrderService payOrderService;

    /**
     * 扫码支付，网页支付，app支付 统一下单接口
     * <p/>
     * 微信扫码支付，使用模式二。
     *
     * @return
     */
    @Override
    public Object pay(PayChannel payChannel, PayOrder order) {
        // 订单刚创建，需要 到微信支付 统一下单，获取支付凭据
        if (order.getStatus() == PayStatus.CREATE_PAYMENT.getValue()) {
            WechatPayClient client = new WechatPayClient(payChannel.getAppId(), payChannel.getMchId(), payChannel.getApiKey());

            WechatPayPrePayRequest request = new WechatPayPrePayRequest();

            // model中 appId,sign,mchId,nonceStr 在WechatClient中设置
            WechatPayPrePayModel model = new WechatPayPrePayModel();
            model.setOutTradeNo(order.getPayOrderNo());
            model.setSpbillCreateIp(order.getUserIp());
            model.setBody(order.getSubject());
            model.setDetail(order.getDetail());
            model.setTimeStart(order.getStartTime());
            model.setTimeExpire(order.getExpireTime());
            model.setTotalFee(order.getPayAmount());
            model.setNotifyUrl(notifyUrl);
            model.setTradeType(order.getTradeType().toString());
            if (TradeTypeCode.WAP == order.getTradeTypeCodeEnum()) {
                model.setTradeType(TradeTypeCode.JSAPI.toString());
            }
            model.setOpenId(order.getOpenId());

            request.setModel(model);

            // 统一下单
            WechatPayPrePayResponse response;
            try {
                response = client.execute(request);
            } catch (WechatPayException e) {
                // 支付失败
                logger.error(e.getMessage(), e);

                order.setStatus(PayStatus.CREATE_PAYMENT_FAIL.getValue());
                order.setErrorMsg(e.getErrMsg());
                order.setErrorCode(e.getErrCode());
                payOrderService.updateStatus(order.getId(), PayStatus.CREATE_PAYMENT_FAIL.getValue(), e.getErrCode(), e.getErrMsg());
                throw new PayException(e.getErrMsg());
            }

            if (response.isSuccess()) {
                order.setStatus(PayStatus.CREATE_PAYMENT_SUCCESS.getValue());
                order.setCodeUrl(response.getCodeUrl());
                order.setPrePayId(response.getPrepayId());
                payOrderService.updatePayRequest(order.getId(), response.getPrepayId(), response.getCodeUrl());
            } else {
                order.setStatus(PayStatus.CREATE_PAYMENT_FAIL.getValue());
                order.setErrorCode(response.getErrCode());
                order.setErrorMsg(response.getErrCodeDes());
                payOrderService.updateStatus(order.getId(), PayStatus.CREATE_PAYMENT_FAIL.getValue(), response.getErrCode(), response.getErrCodeDes());
                throw new PayException(response.getErrCodeDes());
            }

        }
        // 如果已经下过单，则直接返回
        if (order.getStatus() != PayStatus.CREATE_PAYMENT_SUCCESS.getValue()) {
            throw new PayException("订单状态异常");
        }

        switch (order.getTradeTypeCodeEnum()) {
            case NATIVE: {
                return order.getCodeUrl();
            }
            case WAP: {
                return getWapPayMap(payChannel, order);
            }
            case APP: {
                return getAppPayMap(payChannel, order);
            }
        }

        throw new PayException("交易类型不存在");
    }

    // https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=8_5
    // 需要在 微信开放平台创建app，设置秘钥和package。
    private Map<String, String> getAppPayMap(PayChannel payChannel, PayOrder order) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("appid", payChannel.getAppId());
        data.put("partnerid", payChannel.getMchId());
        data.put("prepayid", order.getPrePayId());
        data.put("package", "Sign=WXPay");
        data.put("noncestr", RandomStringUtils.random(32, true, true));
        data.put("timestamp", (System.currentTimeMillis() / 1000 + ""));
        data.put("sign", SignUtils.md5(data, payChannel.getApiKey()));
        return data;
    }

    // https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=7_7&index=6
    private Map<String, String> getWapPayMap(PayChannel payChannel, PayOrder order) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("appId", payChannel.getAppId());
        data.put("timeStamp", System.currentTimeMillis() + "");
        data.put("nonceStr", RandomStringUtils.random(32, true, true));
        data.put("signType", "MD5");
        data.put("package", "prepay_id=" + order.getPrePayId());

        data.put("paySign", SignUtils.md5(data, payChannel.getApiKey())); // 计算sign
        return data;
    }

    @Override
    public String getOutTradeNo(String notify) {
        try {
            Document doc = DocumentHelper.parseText(notify);
            Element root = doc.getRootElement();
            Element element = root.element("out_trade_no");

            if (element == null) {
                return null;
            }
            return element.getText();
        } catch (DocumentException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public PayNotifyResponse parse(PayChannel channel, PayOrder order, String notify) {
        WechatPayClient client = new WechatPayClient(channel.getApiKey());
        WechatPayPayNotifyResponse response = null;
        try {
            // 解析的时候 会校验签名
            response = client.parse(WechatPayPayNotifyResponse.class, notify);
        } catch (WechatPayException e) {
            logger.error(e.getMessage(), e);
            return new PayNotifyResponse(ResultCode.FAIL.getValue(), ResultCode.FAIL.getValue());
        }

        PayNotifyResponse notifyResponse = new PayNotifyResponse();
        notifyResponse.setTradePayNo(order.getTradePayNo()); // 业务方支付订单号
        notifyResponse.setPayAmount(order.getPayAmount());
        notifyResponse.setExtra(order.getExtra());
        notifyResponse.setMerchantId(order.getMerchantId());
        notifyResponse.setPayOrderNo(order.getPayOrderNo());
        if (response.isSuccess()) {
            setOrderPaySuccess(order, response.getOpenId(), response.getTimeEnd(), response.getTransactionId());

            notifyResponse.setCode(ResultCode.SUCCESS.getValue());
            notifyResponse.setMsg(ResultCode.SUCCESS.getValue());
            notifyResponse.setStatus(ResultCode.SUCCESS.getValue());
            notifyResponse.setPayTime(DateUtil.defaultTime(order.getPayTime()));
        } else {
            order.setStatus(PayStatus.PAY_FAIL.getValue());
            order.setErrorCode(response.getErrCode());
            order.setErrorMsg(response.getErrCodeDes());
            payOrderService.updateStatus(order.getId(), PayStatus.PAY_FAIL.getValue(), response.getErrCode(), response.getErrCodeDes());

            notifyResponse.setCode(ResultCode.FAIL.getValue());
            notifyResponse.setMsg(response.getErrCodeDes());
            notifyResponse.setStatus(ResultCode.FAIL.getValue());
        }
        return notifyResponse;
    }

    // 订单支付成功， 设置payOrder状态
    private void setOrderPaySuccess(PayOrder order, String openId, Date payTime, String transactionId) {
        order.setStatus(PayStatus.PAY_SUCCESS.getValue());
        order.setOpenId(openId);
        order.setPayTime(payTime);
        order.setPayId(transactionId);
        payOrderService.updatePayOrder(order);
    }

    @Override
    public boolean synchronize(PayChannel payChannel, PayOrder order) {
        WechatPayClient client = new WechatPayClient(payChannel.getAppId(), payChannel.getMchId(), payChannel.getApiKey());

        WechatPayQueryRequest request = new WechatPayQueryRequest();

        WechatPayQueryModel model = new WechatPayQueryModel();
        model.setOutTradeNo(order.getPayOrderNo());
        model.setTransactionId(order.getPayId());

        request.setModel(model);

        // 统一下单
        WechatPayQueryResponse response = null;
        try {
            response = client.execute(request);
        } catch (WechatPayException e) {
            // 支付失败
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }

        //如果订单 还在 待支付状态
        if (PayStatus.CREATE_PAYMENT.getValue() == order.getStatus()
                || PayStatus.CREATE_PAYMENT_SUCCESS.getValue() == order.getStatus()
                || PayStatus.PAY_CHECKING.getValue() == order.getStatus()) {
            switch (response.getTradeState()) {
                case SUCCESS: {
                    setOrderPaySuccess(order, response.getOpenId(), response.getTimeEnd(), response.getTransactionId());
                    return true;
                }
                case REVOKED: // 已撤销（刷卡支付）
                case CLOSED:
                case PAYERROR: {
                    payOrderService.updateStatus(order.getId(), PayStatus.PAY_CLOSE.getValue(), null, null);
                    return true;
                }
                case NOTPAY: {
                    Date now = new Date();
                    if (now.after(order.getExpireTime())) {
                        payOrderService.updateStatus(order.getId(), PayStatus.PAY_CLOSE.getValue(), null, null);
                        return true;
                    } else {
                        return false;
                    }
                }
                // USERPAYING 支付中，不处理
            }
        } else if (PayStatus.PAY_SUCCESS.getValue() == order.getStatus()) {
            // 订单已支付成功，判断是否有退款信息
            if (WechatPayTradeStatus.REFUND == response.getTradeState()) {
                payOrderService.updateStatus(order.getId(), PayStatus.REFUND_SUCCESS.getValue(), null, null);
                return true;
            }
        }

        return false;
    }
}
