package com.kk.api.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.CaseFormat;
import com.google.gson.GsonBuilder;
import com.kk.api.request.PayQueryRequest;
import com.kk.api.request.PayRequest;
import com.kk.api.response.PayNotifyResponse;
import com.kk.api.response.PayQueryResponse;
import com.kk.api.response.PayResponse;
import com.kk.api.response.PaySynResponse;
import com.kk.platform.enums.PayException;
import com.kk.platform.enums.PayStatus;
import com.kk.platform.enums.PayTypeCode;
import com.kk.platform.enums.ResultCode;
import com.kk.platform.model.PayChannel;
import com.kk.platform.model.PayMerchant;
import com.kk.platform.model.PayMerchantChannel;
import com.kk.platform.model.PayOrder;
import com.kk.platform.service.PayOrderService;
import com.kk.util.DateUtil;
import com.kk.utils.HttpClientUtil;
import com.kk.utils.LocalIPUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PayService extends AbstractPayService {
    private Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private Map<String, InternalPayService> payServices;


    /**
     * 统一下单
     *
     * @param request
     * @return
     */
    public PayResponse request(PayRequest request) {
        PayMerchant merchant = payMerchantService.getPayMerchant(request.getMerchantId());

        // 校验参数
        PayResponse response = (PayResponse) validate(merchant, request, PayResponse.class);
        if (response != null) {
            return response;
        }

        // 支付类型校验
        if (request.getPayType() == null || request.getTradeType() == null) {
            throw new PayException("支付类型不支持");
        }

        PayTypeCode payType = request.getPayType();

        // 查看该商户是否有此支付类型权限
        PayMerchantChannel merchantChannel = payMerchantChannelService.getPayMerchantChannel(merchant.getId(), payType.getId(), request.getTradeType());
        if (merchantChannel == null) {
            throw new PayException("支付类型不支持");
        }


        PayChannel payChannel = merchantChannel.getPayChannel();
        if (payChannel == null) {
            throw new PayException("此商户支付类型配置有误");
        }
        // 业务方 交易流水号
        String voucherId = request.getTradePayNo();
        // 重复下单校验
        List<PayOrder> payOrders = payOrderService.getPayOrders(request.getMerchantId(), request.getTradePayNo());
        if (payOrders.size() > 0) {
            for (PayOrder order : payOrders) {
                if (order.getStatus() == PayStatus.PAY_SUCCESS.getValue()
                        || order.getStatus() == PayStatus.REFUND_PART.getValue()
                        || order.getStatus() == PayStatus.REFUND_SUCCESS.getValue()
                        ) {
                    throw new PayException("订单已支付，请勿重新支付");
                }
            }
            for (PayOrder order : payOrders) {
                if (order.getStatus() == PayStatus.CREATE_PAYMENT_SUCCESS.getValue()) {
                    if (order.getPayTypeCode().equals(request.getPayType()) && order.getExpireTime().after(new Date())) {
                        return getPayResponse(merchant, payChannel, order);
                    } else {
                        // 支付请求过期
                        continue;
                    }
                }
                if (order.getStatus() == PayStatus.CREATE_PAYMENT.getValue()) {
                    return getPayResponse(merchant, payChannel, order);
                }
            }
        }

        // 下单
        PayOrder order = new PayOrder();
        order.setNotifyUrl(request.getNotifyUrl());
        order.setDetail(request.getDetail());
        order.setSubject(request.getSubject());
        order.setUserIp(request.getClientIp());
        order.setTradeType(request.getTradeType().toString());
        order.setPayTypeCode(request.getPayType().toString());
        order.setExtra(request.getExtra());
        order.setReturnUrl(request.getReturnUrl());
        order.setStatus(PayStatus.CREATE_PAYMENT.getValue());
        if (StringUtils.isBlank(request.getClientIp())) {
            order.setUserIp(LocalIPUtil.getLocalAddr());
        }

        DateTime now = DateTime.now();
        order.setStartTime(now.toDate());
        order.setExpireTime(now.plusHours(2).toDate());
        order.setTradePayNo(request.getTradePayNo());
        order.setPayAmount(request.getPayAmount());
        order.setMerchantId(request.getMerchantId());
        order.setOpenId(request.getOpenId());
        payOrderService.createPayOrder(order);

        return getPayResponse(merchant, payChannel, order);
    }

    private PayResponse getPayResponse(PayMerchant merchant, PayChannel payChannel, PayOrder order) {
        PayResponse response = new PayResponse();
        // 统一下单接口，获取支付凭据
        response.setCredential(getService(order.getPayTypeCodeEnum()).pay(payChannel, order));

        // 基本参数
        response.setMerchantId(merchant.getMerchantId());
        response.setCode(ResultCode.SUCCESS.getValue());
        response.setMsg(ResultCode.SUCCESS.getValue());

        response.setTradePayNo(order.getTradePayNo());
        response.setPayOrderNo(order.getPayOrderNo());
        response.setPayType(order.getPayTypeCodeEnum());
        response.setTradeType(order.getTradeTypeCodeEnum());
        response.setSign(sign(response, merchant.getApiKey()));
        return response;
    }

    // payType：WECHAT_PAY
    private InternalPayService getService(PayTypeCode payType) {

        // key：wechatPayService
        String key = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, payType.toString()) + "Service";

        InternalPayService service = payServices.get(key);
        if (service == null) {
            throw new PayException("支付方式不存在");
        }
        return service;
    }

    /**
     * 支付成功回调，设置订单状态，同时回调业务方。
     *
     * @param payType   支付类型：微信，支付宝等
     * @param payNotify
     * @return
     */
    public boolean handlePayNotify(PayTypeCode payType, String payNotify) {
        if (StringUtils.isBlank(payNotify)) {
            return false;
        }

        String orderNo = getService(payType).getOutTradeNo(payNotify);
        if (StringUtils.isBlank(orderNo)) {
            return false;
        }

        PayOrder order = payOrderService.getPayOrder(orderNo);
        if (order == null) {
            return false;
        }
        // 如果已经回调成功
        if (order.getStatus() == PayStatus.PAY_SUCCESS.getValue()) {
            return false;
        }

        PayMerchantChannel channel = payMerchantChannelService.getPayMerchantChannel(order.getMerchantId(), order.getPayTypeCodeEnum(), order.getTradeTypeCodeEnum());

        PayNotifyResponse notifyResponse = getService(payType).parse(channel.getPayChannel(), order, payNotify);

        notifyResponse.setPayType(order.getPayTypeCodeEnum());
        notifyResponse.setSign(sign(notifyResponse, channel.getPayMerchant().getApiKey()));
        if (ResultCode.SUCCESS.getValue().equals(notifyResponse.getStatus())) {
            if (StringUtils.isNotBlank(order.getNotifyUrl())) {
                HttpClientUtil.postData(order.getNotifyUrl(), JSON.toJSONString(notifyResponse, SerializerFeature.WriteDateUseDateFormat));
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 优先根据 payOrderNo查询
     * 如果根据tradePayNo ，如果只有一条记录，则直接返回； 如果有多条记录，优先返回 成功或失败的记录， 如果找不到则返回最后一条。
     *
     * @param request
     * @return
     */
    public PayQueryResponse query(PayQueryRequest request) {
        PayMerchant merchant = payMerchantService.getPayMerchant(request.getMerchantId());

        // 校验参数
        PayQueryResponse response = (PayQueryResponse) validate(merchant, request, PayQueryResponse.class);
        if (response != null) {
            return response;
        }

        response = new PayQueryResponse();

        if (StringUtils.isBlank(request.getTradePayNo()) && StringUtils.isBlank(request.getPayOrderNo())) {
            response.setCode(ResultCode.FAIL.getValue());
            response.setMsg("订单号不存在");
            return response;
        }

        PayOrder order = getPayOrder(request);

        if (order == null) {
            response.setCode(ResultCode.FAIL.getValue());
            response.setMsg("订单不存在");
            return response;
        }
        response.setTradePayNo(order.getTradePayNo());
        response.setPayOrderNo(order.getPayOrderNo());
        response.setExt(order.getExtra());
        response.setPrePayId(order.getPrePayId());
        response.setSubject(order.getSubject());
        response.setDetail(order.getDetail());
        response.setCodeUrl(order.getCodeUrl());
        response.setPayType(order.getPayTypeCode());
        response.setTradeType(order.getTradeType());
        response.setRefundAmount(order.getRefundAmount());

        // 基本参数
        response.setMsg(ResultCode.SUCCESS.getValue());
        response.setCode(ResultCode.SUCCESS.getValue());
        response.setMerchantId(order.getMerchantId());

        response.setPayTime(DateUtil.defaultTime(order.getPayTime()));
        response.setPayAmount(order.getPayAmount());
        response.setStatus(order.getStatus() + "");
        response.setSign(sign(response, merchant.getApiKey()));
        return response;
    }

    /**
     * 同步订单状态，与第三方支付的订单状态保持一致
     *
     * @param request
     * @return
     */
    public PaySynResponse synchronize(PayQueryRequest request) {
        PayMerchant merchant = payMerchantService.getPayMerchant(request.getMerchantId());

        // 校验参数
        PaySynResponse response = (PaySynResponse) validate(merchant, request, PaySynResponse.class);
        if (response != null) {
            return response;
        }

        response = new PaySynResponse();

        if (StringUtils.isBlank(request.getTradePayNo()) && StringUtils.isBlank(request.getPayOrderNo())) {
            response.setCode(ResultCode.FAIL.getValue());
            response.setMsg("订单号不存在");
            return response;
        }

        PayOrder order = getPayOrder(request);

        if (order == null) {
            response.setCode(ResultCode.FAIL.getValue());
            response.setMsg("订单不存在");
            return response;
        }

        boolean syn = synchronize(order);
        if (syn) {
            response.setStatus(ResultCode.SUCCESS.getValue());
        } else {
            response.setStatus(ResultCode.FAIL.getValue());
        }
        response.setMsg(ResultCode.SUCCESS.getValue());
        response.setCode(ResultCode.SUCCESS.getValue());
        response.setMerchantId(order.getMerchantId());

        response.setSign(sign(response, merchant.getApiKey()));
        return response;
    }

    public boolean synchronize(PayOrder order) {
        if (order == null) {
            return false;
        }

        PayMerchantChannel channel = payMerchantChannelService.getPayMerchantChannel(order.getMerchantId(), order.getPayTypeCodeEnum(), order.getTradeTypeCodeEnum());

        if (PayStatus.CREATE_PAYMENT.getValue() == order.getStatus()
                || PayStatus.CREATE_PAYMENT_SUCCESS.getValue() == order.getStatus()
                || PayStatus.PAY_SUCCESS.getValue() == order.getStatus()
                || PayStatus.PAY_CHECKING.getValue() == order.getStatus()) {
            return getService(order.getPayTypeCodeEnum()).synchronize(channel.getPayChannel(), order);
        }

        return false;
    }

    private PayOrder getPayOrder(PayQueryRequest request) {
        PayOrder order = null;
        if (StringUtils.isNotBlank(request.getPayOrderNo())) {
            order = payOrderService.getPayOrder(request.getPayOrderNo());
        }
        if (order == null) {
            List<PayOrder> orderList = payOrderService.getPayOrders(request.getMerchantId(), request.getTradePayNo());
            if (orderList.size() == 1) {
                order = orderList.get(0);
            } else {
                for (PayOrder o : orderList) {
                    if (o.getStatus() != PayStatus.CREATE_PAYMENT.getValue()
                            && o.getStatus() != PayStatus.CREATE_PAYMENT_SUCCESS.getValue()
                            && o.getStatus() != PayStatus.CREATE_PAYMENT_FAIL.getValue()) {
                        order = o;
                        break;
                    }
                }
                if (order == null) {
                    if (orderList.size() > 0) {
                        order = orderList.get(orderList.size() - 1);
                    }
                }
            }
        }
        if (order != null) {
            if (!order.getMerchantId().equals(request.getMerchantId())) {
                logger.error(String.format("order can not match merchantId:order.id=%s,req.mchId=%s", order.getId(), request.getMerchantId()));
                return null;
            }
        }
        return order;
    }
}
