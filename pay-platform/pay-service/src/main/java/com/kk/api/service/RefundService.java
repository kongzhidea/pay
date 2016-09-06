package com.kk.api.service;

import com.google.common.base.CaseFormat;
import com.kk.api.ApiRequest;
import com.kk.api.request.RefundQueryRequest;
import com.kk.api.request.RefundRequest;
import com.kk.api.response.RefundQueryResponse;
import com.kk.api.response.RefundResponse;
import com.kk.platform.enums.PayException;
import com.kk.platform.enums.PayTypeCode;
import com.kk.platform.enums.ResultCode;
import com.kk.platform.model.*;
import com.kk.platform.service.PayChannelService;
import com.kk.platform.service.PayOrderService;
import com.kk.platform.service.RefundOrderService;
import com.kk.util.BeanUtil;
import com.kk.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 订单退款
 */
@Service
public class RefundService extends AbstractPayService {
    @Autowired
    private PayOrderService payOrderService;
    @Autowired
    private RefundOrderService refundOrderService;
    @Autowired
    private PayChannelService payChannelService;

    @Autowired
    private Map<String, InternalRefundService> refundServices;

    /**
     * 创建支付订单，立即退款
     *
     * @param request
     * @return
     */
    public RefundResponse request(RefundRequest request) {
        return request(request, true);
    }

    /**
     * 创建支付订单，  实际退款操作在refund方法
     *
     * @param request
     * @param refund  是否立即退款， 如果否 则仅创建退款订单，等后台人员审核再退款。
     * @return
     */
    private RefundResponse request(RefundRequest request, boolean refund) {
        PayMerchant merchant = payMerchantService.getPayMerchant(request.getMerchantId());
        RefundResponse response = (RefundResponse) validate(merchant, request, RefundResponse.class);
        if (response != null) {
            return response;
        }
        response = new RefundResponse();

        PayOrder payOrder = payOrderService.getPayOrder(request.getPayOrderNo());
        if (payOrder == null || !merchant.getMerchantId().equals(payOrder.getMerchantId())) {
            return new RefundResponse(ResultCode.FAIL.getValue(), "支付订单不存在");
        }

        if (request.getRefundAmount() > payOrder.getPayAmount()) {
            return new RefundResponse(ResultCode.FAIL.getValue(), "退款金额高于支付订单额度");
        }

        PayMerchantChannel merchantChannel = payMerchantChannelService.getPayMerchantChannel(payOrder.getMerchantId(), payOrder.getPayTypeCodeEnum(), payOrder.getTradeTypeCodeEnum());
        PayChannel payChannel = merchantChannel.getPayChannel();

        RefundOrder refundOrder = refundOrderService.createRefundOrder(payOrder, payChannel.getId(), request.getTradeRefundNo(), request.getRefundAmount(), request.getRefundReason());

        // 实际退款流程， 如果退款失败会抛出异常，返回controller处理
        if (refund) {
            refund(payChannel, refundOrder);
        }

        response.setMsg(ResultCode.SUCCESS.getValue());
        response.setMerchantId(request.getMerchantId());
        response.setCode(ResultCode.SUCCESS.getValue());

        response.setTradePayNo(payOrder.getTradePayNo());
        response.setPayOrderNo(payOrder.getPayOrderNo());

        response.setTradeRefundNo(refundOrder.getTradeRefundNo());
        response.setRefundOrderNo(refundOrder.getRefundOrderNo());

        response.setRefundAmount(refundOrder.getRefundAmount());
        response.setPayAmount(refundOrder.getPayAmount());

        response.setSign(sign(response, merchantChannel.getPayMerchant().getApiKey()));
        return response;
    }

    /**
     * 实际退款操作， 调用微信，支付宝的退款接口
     *
     * @param payChannel
     * @param refundOrder
     * @return
     */
    private Object refund(PayChannel payChannel, RefundOrder refundOrder) {
        return getService(refundOrder.getPayTypeCodeEnum()).refund(payChannel, refundOrder);
    }

    // payType：WECHAT_PAY
    private InternalRefundService getService(PayTypeCode payType) {

        // key：wechatPayService
        String key = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, payType.toString().replace("PAY", "REFUND")) + "Service";

        InternalRefundService service = refundServices.get(key);
        if (service == null) {
            throw new PayException("支付方式不存在");
        }
        return service;
    }

    /**
     * 退款查询， 只查询 refundOrder，没有调用微信 支付宝的退款查询接口
     *
     * @return
     */
    public RefundQueryResponse query(RefundQueryRequest request) {
        PayMerchant merchant = payMerchantService.getPayMerchant(request.getMerchantId());
        RefundQueryResponse response = (RefundQueryResponse) validate(merchant, request, RefundQueryResponse.class);
        if (response != null) {
            return response;
        }

        response = new RefundQueryResponse();

        RefundOrder refundOrder = null;
        if (StringUtils.isNotBlank(request.getRefundOrderNo())) {
            refundOrder = refundOrderService.getRefundOrder(request.getRefundOrderNo());
        }
        if (refundOrder == null) {
            refundOrder = refundOrderService.getRefundOrderByTradeRefundNoMchId(request.getTradeRefundNo(), request.getMerchantId());
        }
        if (refundOrder == null) {
            throw new PayException("退款订单不存在");
        }

        response.setMsg(ResultCode.SUCCESS.getValue());
        response.setCode(ResultCode.SUCCESS.getValue());
        response.setMerchantId(refundOrder.getMerchantId());

        response.setTradePayNo(refundOrder.getTradePayNo());
        response.setPayOrderNo(refundOrder.getPayOrderNo());
        response.setRefundOrderNo(refundOrder.getRefundOrderNo());
        response.setTradeRefundNo(refundOrder.getTradeRefundNo());
        response.setRefundTime(DateUtil.defaultTime(refundOrder.getRefundTime()));
        response.setPayAmount(refundOrder.getPayAmount());
        response.setRefundAmount(refundOrder.getRefundAmount());
        response.setStatus(refundOrder.getStatus());
        response.setRefundReason(refundOrder.getRefundReason());

        response.setSign(sign(response, merchant.getApiKey()));

        return response;
    }

    /**
     * 目前支付宝使用
     *
     * @return
     */
    public boolean handleRefundNotify(PayTypeCode payTypeCode, String refundNotify) {
        if (StringUtils.isBlank(refundNotify)) {
            return false;
        }

        RefundOrder refundOrder = refundOrderService.getRefundOrder(BeanUtil.jsonStr2Map(refundNotify).get("batch_no"));
        if (refundOrder == null) {
            return false;
        }

        PayMerchantChannel channel = payMerchantChannelService.getPayMerchantChannel(refundOrder.getMerchantId(), refundOrder.getPayTypeCodeEnum(), refundOrder.getTradeTypeCodeEnum());
        RefundResponse refundResponse = getService(payTypeCode).parse(channel.getPayChannel(), refundOrder, refundNotify);
        return ResultCode.SUCCESS.getValue().equals(refundResponse.getCode());
    }
}
