package com.kk.alipay.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alipay.api.AlipayApiException;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.kk.alipay.client.DefaultAliPayParser;
import com.kk.alipay.client.MyDefaultAliPayClient;
import com.kk.alipay.exception.AliPayException;
import com.kk.alipay.response.AliPayNotifyResponse;
import com.kk.api.response.PayNotifyResponse;
import com.kk.api.service.InternalPayService;
import com.kk.platform.enums.PayException;
import com.kk.platform.enums.PayStatus;
import com.kk.platform.enums.ResultCode;
import com.kk.platform.enums.TradeTypeCode;
import com.kk.platform.model.PayChannel;
import com.kk.platform.model.PayOrder;
import com.kk.platform.service.PayChannelService;
import com.kk.platform.service.PayOrderService;
import com.kk.util.DateUtil;
import com.kk.util.SignUtils;
import com.kk.utils.WebPropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 扫码支付wiki：https://doc.open.alipay.com/doc2/detail?spm=0.0.0.0.E3tvGh&treeId=26&articleId=103286&docType=1
 * 扫码支付，异步回调，订单查询，申请退款
 * <p/>
 * 网页支付wiki:https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.08w3Le&treeId=60&articleId=103564&docType=1
 * 网页支付接口，demo，签名规则，异步回调，同步回调，申请退款
 * <p/>
 * 移动支付（APP支付）wiki:
 * https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.Tn7dlq&treeId=59&articleId=103663&docType=1
 * app支付，签名机制，客户端调用， 异步回调
 * <p/>
 * 开发工具包下载， 扫码支付demo，app支付demo:
 * https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.S9aWYF&treeId=54&articleId=104506&docType=1
 * <p/>
 * 支付API：
 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7386797.0.0.ppgPy5&docType=4&apiId=757
 * <p/>
 * 支付宝秘钥见：README.md
 */
@Service
public class AliPayService implements InternalPayService {
    private Log logger = LogFactory.getLog(this.getClass());

    private static final BigDecimal HUNDRED = new BigDecimal("100");

    // 通知url必须为直接可访问的url，不能携带参数
    private String notifyUrl = WebPropertiesUtil.getInstance().getValue("ali.pay.notify.url");

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    PayChannelService payChannelService;

    @Override
    public Object pay(PayChannel payChannel, PayOrder order) {
        switch (order.getTradeTypeCodeEnum()) {
            case NATIVE: {
                if (order.getStatus() == PayStatus.CREATE_PAYMENT_SUCCESS.getValue()) {
                    return order.getCodeUrl();
                } else if (order.getStatus() == PayStatus.CREATE_PAYMENT.getValue()) {
                    return getNativePayUrl(payChannel, order);
                } else {
                    throw new PayException("订单状态异常");
                }
            }
            case WAP: {
                return getWapPayMap(payChannel, order);
            }
            case APP: {
                return getAppPayQuery(payChannel, order);
            }
        }
        return null;
    }

    // 网页支付
    private Map<String, String> getWapPayMap(PayChannel payChannel, PayOrder order) {
        String signType = payChannel.getSignType();

        Map<String, String> params = new HashMap<String, String>();
        params.put("service", "alipay.wap.create.direct.pay.by.user");
        params.put("partner", payChannel.getMchId());
        params.put("seller_id", payChannel.getMchId());
        params.put("_input_charset", "UTF-8");
        params.put("sign_type", signType);
        params.put("payment_type", "1");

        params.put("notify_url", notifyUrl);
        params.put("return_url", order.getReturnUrl());
        params.put("out_trade_no", order.getPayOrderNo());
        params.put("subject", order.getSubject());
        params.put("total_fee", new BigDecimal(order.getPayAmount()).divide(HUNDRED).setScale(2).toString());
//        params.put("show_url", show_url);
        params.put("body", order.getDetail());


        String sign = SignUtils.rsa(params, payChannel.getMchKey());
        params.put("sign", sign);

        return params;
    }

    // App支付
    private Map<String, String> getAppPayQuery(PayChannel payChannel, PayOrder order) {
        String signType = payChannel.getSignType();

        Map<String, String> params = new HashMap<String, String>();
        params.put("service", "mobile.securitypay.pay");
        params.put("partner", payChannel.getMchId());
        params.put("seller_id", payChannel.getMchId());
        params.put("_input_charset", "UTF-8");
        params.put("sign_type", signType);
        params.put("payment_type", "1");

        params.put("notify_url", notifyUrl);
        params.put("out_trade_no", order.getPayOrderNo());
        params.put("subject", order.getSubject());
        params.put("total_fee", new BigDecimal(order.getPayAmount()).divide(HUNDRED).setScale(2).toString());
        params.put("body", order.getDetail());


        String sign = SignUtils.rsa(params, payChannel.getMchKey());
        params.put("sign", sign);
        return params;

        // 直接返回拼好的query
//        String sign;
//        try {
//            sign = "\"" + URLEncoder.encode(SignUtils.rsa(params, payChannel.getMchKey()), "UTF-8") + "\"";
//        } catch (UnsupportedEncodingException e) {
//            throw new PayException("签名失败");
//        }
//        Map<String, String> resultMap = new HashMap<String, String>(1);
//        resultMap.put("query", SignUtils.getSignContent(params, false) + "&sign=" + sign + "&sign_type=\"" + signType  + "\"");
//        return resultMap;

    }

    // 扫码支付，获取支付二维码
    private String getNativePayUrl(PayChannel payChannel, PayOrder order) {
        MyDefaultAliPayClient client = new MyDefaultAliPayClient(payChannel.getAppId(), payChannel.getMchKey(), payChannel.getPlatformKey());
        AlipayTradePrecreateResponse response = null;
        try {
            AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
            request.setNotifyUrl(notifyUrl);
            Map<String, String> params = new HashMap<String, String>();
            params.put("out_trade_no", order.getPayOrderNo());
            params.put("seller_id", payChannel.getMchId());
            params.put("total_amount", new BigDecimal(order.getPayAmount()).divide(HUNDRED).setScale(2).toString());
            params.put("subject", order.getSubject());
            params.put("body", order.getDetail());
            // 一些非必填参数，这里就没有设置
            request.setBizContent(JSON.toJSONString(params));

            logger.info("request=" + JSON.toJSONString(request, SerializerFeature.WriteDateUseDateFormat));

            response = client.execute(request);

            logger.info("response=" + JSON.toJSONString(response, SerializerFeature.WriteDateUseDateFormat));
        } catch (AlipayApiException e) {
            logger.error(e.getMessage(), e);
            order.setStatus(PayStatus.CREATE_PAYMENT_FAIL.getValue());
            order.setErrorCode(e.getErrCode());
            order.setErrorMsg(e.getErrMsg());
            payOrderService.updateStatus(order.getId(), PayStatus.CREATE_PAYMENT_FAIL.getValue(), e.getErrCode(), e.getErrMsg());
            throw new PayException(e);
        }
        // 10000表示成功
        if (response.isSuccess() && "10000".equals(response.getCode())) {
            order.setStatus(PayStatus.CREATE_PAYMENT_SUCCESS.getValue());
            order.setCodeUrl(response.getQrCode()); // 支付的二维码地址
            payOrderService.updatePayRequest(order.getId(), null, response.getQrCode());
        } else {
            order.setStatus(PayStatus.CREATE_PAYMENT_FAIL.getValue());
            order.setErrorCode(response.getCode());
            order.setErrorMsg(response.getMsg());
            payOrderService.updateStatus(order.getId(), PayStatus.CREATE_PAYMENT_FAIL.getValue(), response.getCode(), response.getMsg());
            throw new PayException(response.getMsg());
        }
        return response.getQrCode();
    }

    @Override
    public String getOutTradeNo(String notify) {
        JSONObject object = JSON.parseObject(notify);
        return object.getString("out_trade_no");
    }

    @Override
    public PayNotifyResponse parse(PayChannel channel, PayOrder order, String notify) {
        if (StringUtils.isBlank(notify)) {
            return new PayNotifyResponse(ResultCode.FAIL.getValue(), ResultCode.FAIL.getValue());
        }

        String mchId = channel.getMchId();
        String platformKey = channel.getPlatformKey();
        DefaultAliPayParser parser = new DefaultAliPayParser(channel.getSignType(), mchId, platformKey);

        AliPayNotifyResponse response;
        try {
            response = parser.parse(AliPayNotifyResponse.class, notify);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new PayNotifyResponse(ResultCode.FAIL.getValue(), ResultCode.FAIL.getValue());
        }

        PayNotifyResponse notifyResponse = new PayNotifyResponse();
        notifyResponse.setTradePayNo(order.getTradePayNo());
        notifyResponse.setPayAmount(order.getPayAmount());
        notifyResponse.setExtra(order.getExtra());
        notifyResponse.setMerchantId(order.getMerchantId());
        notifyResponse.setPayOrderNo(order.getPayOrderNo());
        if (response.isSuccess()) {
            setOrderPaySuccess(order, response.getBuyerId(), response.getBuyerEmailOrLogonId(), response.getGmtPayment(), response.getTradeNo());

            notifyResponse.setCode(ResultCode.SUCCESS.getValue());
            notifyResponse.setMsg(ResultCode.SUCCESS.getValue());
            notifyResponse.setStatus(ResultCode.SUCCESS.getValue());
            notifyResponse.setPayTime(DateUtil.defaultTime(order.getPayTime()));
        } else {
            payOrderService.updateStatus(order.getId(), PayStatus.PAY_FAIL.getValue(), response.getTradeStatus(), "未成功");
            notifyResponse.setCode(ResultCode.FAIL.getValue());
            notifyResponse.setMsg("未成功");
            notifyResponse.setStatus(ResultCode.FAIL.getValue());
        }

        return notifyResponse;
    }

    // 订单支付成功， 设置payOrder状态
    private void setOrderPaySuccess(PayOrder order, String buyerId, String buyerLogonId, Date payTime, String tradeNo) {
        order.setStatus(PayStatus.PAY_SUCCESS.getValue());
        order.setOpenId(buyerId);
        order.setBuyerLogonId(buyerLogonId);
        order.setPayTime(payTime);
        order.setPayId(tradeNo);
        payOrderService.updatePayOrder(order);
    }

    //下单，异步通知 时候， 如果是创建的应用，则使用合作伙伴秘钥加密； 如果是服务窗应用，则使用开放平台秘钥。

    /**
     * 支付宝查询订单时候， 需要注意签名，  注意使用的是 开放平台的私钥还是合作商户的私钥。
     * <p/>
     * 使用开放平台私钥查询订单。
     *
     * @param payChannel 如果payChannel传入的是合作伙伴秘钥信息，需要转成开放平台秘钥信息
     * @param order
     * @return
     */
    @Override
    public boolean synchronize(PayChannel payChannel, PayOrder order) {
        if (payChannel.getQueryChannelId() != 0) {
            payChannel = payChannelService.getPayChannel(payChannel.getQueryChannelId());
        }

        MyDefaultAliPayClient client = new MyDefaultAliPayClient(payChannel.getAppId(), payChannel.getMchKey(), payChannel.getPlatformKey());
        AlipayTradeQueryResponse response = null;
        try {
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            Map<String, String> params = new HashMap<String, String>();
            params.put("out_trade_no", order.getPayOrderNo());
            params.put("trade_no", order.getPayId());

            request.setBizContent(JSON.toJSONString(params));

            logger.info("request=" + JSON.toJSONString(request, SerializerFeature.WriteDateUseDateFormat));

            response = client.execute(request);

            logger.info("response=" + JSON.toJSONString(response, SerializerFeature.WriteDateUseDateFormat));
        } catch (AlipayApiException e) {
            // 支付失败
            logger.error(e.getMessage(), e);
            throw new PayException(e.getMessage());
        }
        if (PayStatus.CREATE_PAYMENT.getValue() == order.getStatus()
                || PayStatus.CREATE_PAYMENT_SUCCESS.getValue() == order.getStatus()
                || PayStatus.PAY_CHECKING.getValue() == order.getStatus()) {
            if (querySuccess(response)) {
                setOrderPaySuccess(order, response.getBuyerUserId(), response.getBuyerLogonId(), new Date(), response.getTradeNo());
                return true;
            } else if (queryNotpay(response)) {
                Date now = new Date();
                if (now.after(order.getExpireTime())) {
                    payOrderService.updateStatus(order.getId(), PayStatus.PAY_CLOSE.getValue(), null, null);
                    return true;
                } else {
                    return false;
                }
            } else {
                payOrderService.updateStatus(order.getId(), PayStatus.PAY_CLOSE.getValue(), null, null);
                return true;
            }
        }
        return false;
    }

    // 支付成功
    public boolean querySuccess(AlipayTradeQueryResponse response) {
        return response != null &&
                ("10000".equals(response.getCode())) && (
                "TRADE_SUCCESS".equals(response.getTradeStatus()) ||
                        "TRADE_FINISHED".equals(response.getTradeStatus()));
    }

    //支付中
    public boolean queryNotpay(AlipayTradeQueryResponse response) {
        return response != null &&
                "10000".equals(response.getCode()) &&
                "WAIT_BUYER_PAY".equals(response.getTradeStatus());
    }
}
