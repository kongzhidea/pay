package com.kk.alipay.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alipay.api.AlipayApiException;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.kk.alipay.client.DefaultAliPayParser;
import com.kk.alipay.client.MyDefaultAliPayClient;
import com.kk.alipay.response.AliPayRefundNotifyResponse;
import com.kk.api.response.RefundResponse;
import com.kk.api.service.InternalRefundService;
import com.kk.platform.enums.PayException;
import com.kk.platform.enums.RefundStatus;
import com.kk.platform.enums.ResultCode;
import com.kk.platform.enums.TradeTypeCode;
import com.kk.platform.model.PayChannel;
import com.kk.platform.model.RefundOrder;
import com.kk.platform.service.FileResourceService;
import com.kk.platform.service.PayChannelService;
import com.kk.platform.service.PayOrderService;
import com.kk.platform.service.RefundOrderService;
import com.kk.util.SignUtils;
import com.kk.utils.WebPropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝退款
 * <p/>
 * 扫码退款WIki： https://doc.open.alipay.com/doc2/detail?treeId=26&articleId=103269&docType=1
 * 扫码支付，不会异步通知
 * <p/>
 * 即时到账有密退款接口，会异步通知
 * wiki:https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7386797.0.0.RUVbIh&treeId=62&articleId=104744&docType=1#s1
 */
@Service
public class AliRefundService implements InternalRefundService {
    private Log logger = LogFactory.getLog(this.getClass());

    private static final BigDecimal HUNDRED = new BigDecimal("100");

    // 即时到账有密退款接口，异步通知url
    private String refundNotifyUrl = WebPropertiesUtil.getInstance().getValue("ali.refund.notify.url");

    @Autowired
    private PayOrderService payOrderService;
    @Autowired
    private RefundOrderService refundOrderService;
    @Autowired
    PayChannelService payChannelService;

    /**
     * 支付宝退款的时候， 需要注意签名，  注意使用的是 开放平台的私钥还是合作商户的私钥。
     * <p/>
     * 使用开放平台私钥查询订单。
     *
     * @param payChannel 如果payChannel传入的是合作伙伴秘钥信息，需要转成开放平台秘钥信息
     * @return
     */
    @Override
    public Object refund(PayChannel payChannel, RefundOrder refundOrder) {
        if (payChannel.getQueryChannelId() != 0) {
            payChannel = payChannelService.getPayChannel(payChannel.getQueryChannelId());
        }

        MyDefaultAliPayClient client = new MyDefaultAliPayClient(payChannel.getAppId(), payChannel.getMchKey(), payChannel.getPlatformKey());

        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        Map<String, String> params = new HashMap<String, String>();
        params.put("out_trade_no", refundOrder.getPayOrderNo());
        params.put("trade_no", refundOrder.getPayId());
        params.put("seller_id", payChannel.getMchId());

        params.put("refund_amount", new BigDecimal(refundOrder.getRefundAmount()).divide(HUNDRED).setScale(2).toString());
        // 退款原因
        params.put("refund_reason", refundOrder.getRefundReason());
        // 商户退款请求号
        params.put("out_request_no", refundOrder.getRefundOrderNo());

        request.setBizContent(JSON.toJSONString(params));
        AlipayTradeRefundResponse response;
        try {
            logger.info("request=" + JSON.toJSONString(request, SerializerFeature.WriteDateUseDateFormat));

            response = client.execute(request);

            logger.info("response=" + JSON.toJSONString(response, SerializerFeature.WriteDateUseDateFormat));
        } catch (AlipayApiException e) {
            logger.error(e.getMessage(), e);
            refundOrderService.updateStatus(refundOrder.getId(), RefundStatus.REFUND_FAIL.getValue(), e.getErrCode(), e.getErrMsg());
            throw new PayException(e);
        }
        if (response.isSuccess()) {
            refundOrder.setRefundTime(response.getGmtRefundPay());
            refundOrder.setStatus(RefundStatus.REFUND_SUCCESS.getValue());
            refundOrderService.updateRefundOrder(refundOrder);

            payOrderService.updateRefundStatus(refundOrder.getPayOrderNo(), refundOrder.getRefundAmount());

            return true;
        } else {
            refundOrder.setErrorCode(response.getSubCode());
            refundOrder.setErrorMsg(response.getSubMsg());
            refundOrder.setStatus(RefundStatus.REFUND_FAIL.getValue());
            refundOrderService.updateRefundOrder(refundOrder);
        }

        throw new PayException("不支持退款");
    }

    // 即时到账有密退款接口
    public Map<String, String> refundWithPwd(PayChannel payChannel, RefundOrder refundOrder) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("service", "refund_fastpay_by_platform_pwd");
        params.put("partner", payChannel.getMchId());
        params.put("_input_charset", "UTF-8");
        params.put("sign_type", payChannel.getSignType());
        params.put("notify_url", refundNotifyUrl);

        /**
         * 每进行一次即时到账批量退款，都需要提供一个批次号，通过该批次号可以查询这一批次的退款交易记录，对于每一个合作伙伴，传递的每一个批次号都必须保证唯一性。
         格式为：退款日期（8位）+流水号（3～24位）。
         不可重复，且退款日期必须是当天日期。流水号可以接受数字或英文字符，建议使用数字，但不可接受“000”。
         */
        params.put("batch_no", refundOrder.getRefundOrderNo());

        //卖家用户ID，  与seller_email(卖家支付宝账号)不能同时为空
        params.put("seller_user_id", payChannel.getMchId());

        // 退款请求的当前时间。        格式为：yyyy-MM-dd HH:mm:ss。
        params.put("refund_date", new DateTime().toString("yyyy-MM-dd hh:mm:ss"));

        params.put("batch_num", "1");

        // detail_data中的退款笔数总和要等于参数batch_num的值；
        // “退款理由”长度不能大于256字节，“退款理由”中不能有“^”、“|”、“$”、“#”等影响detail_data格式的特殊字符；
        params.put("detail_data", refundOrder.getPayId() + "^" + new BigDecimal(refundOrder.getRefundAmount()).divide(HUNDRED).setScale(2).toString() + "^" + refundOrder.getRefundReason());//原付款支付宝交易号^退款总金额^退款理由

        String sign = SignUtils.rsa(params, payChannel.getMchKey());
        params.put("sign", sign);
        return params;
    }

    /**
     * 即时到账有密退款接口 异步通知
     * wiki:https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7386797.0.0.RUVbIh&treeId=62&articleId=104744&docType=1#s1
     *
     * @param channel
     * @param refundOrder
     * @param notify
     * @return
     */
    @Override
    public RefundResponse parse(PayChannel channel, RefundOrder refundOrder, String notify) {
        if (StringUtils.isBlank(notify)) {
            return new RefundResponse(ResultCode.FAIL.getValue(), ResultCode.FAIL.getValue());
        }

        String mchId = channel.getMchId();
        String platformKey = channel.getPlatformKey();
        DefaultAliPayParser parser = new DefaultAliPayParser(channel.getSignType(), mchId, platformKey);

        AliPayRefundNotifyResponse response = null;
        try {
            response = parser.parse(AliPayRefundNotifyResponse.class, notify);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new RefundResponse(ResultCode.FAIL.getValue(), ResultCode.FAIL.getValue());
        }

        RefundResponse refundResponse = new RefundResponse();
        refundResponse.setTradePayNo(refundOrder.getTradePayNo());
        refundResponse.setPayAmount(refundOrder.getPayAmount());
        refundResponse.setMerchantId(refundOrder.getMerchantId());
        refundResponse.setPayOrderNo(refundOrder.getPayOrderNo());
        refundResponse.setRefundAmount(refundOrder.getRefundAmount());
        if (response.isSuccess()) {
            refundOrder.setRefundTime(new Date());
            refundOrder.setStatus(RefundStatus.REFUND_SUCCESS.getValue());
            refundOrderService.updateRefundOrder(refundOrder);

            payOrderService.updateRefundStatus(refundOrder.getPayOrderNo(), refundOrder.getRefundAmount());

            refundResponse.setCode(ResultCode.SUCCESS.getValue());
            refundResponse.setMsg(ResultCode.SUCCESS.getValue());
        } else {
            refundOrderService.updateStatus(refundOrder.getId(), RefundStatus.REFUND_FAIL.getValue(), "-1", "未成功");
            refundResponse.setCode(ResultCode.FAIL.getValue());
            refundResponse.setMsg("未成功");
        }

        return refundResponse;
    }
}
