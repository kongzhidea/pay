package com.kk.wechat.service;

import com.kk.api.response.RefundResponse;
import com.kk.api.service.InternalRefundService;
import com.kk.platform.enums.PayException;
import com.kk.platform.enums.PayStatus;
import com.kk.platform.enums.RefundStatus;
import com.kk.platform.model.FileResource;
import com.kk.platform.model.PayChannel;
import com.kk.platform.model.RefundOrder;
import com.kk.platform.service.FileResourceService;
import com.kk.platform.service.PayOrderService;
import com.kk.platform.service.RefundOrderService;
import com.kk.wechat.client.WechatPayClient;
import com.kk.wechat.exception.WechatPayException;
import com.kk.wechat.model.WechatPayRefundModel;
import com.kk.wechat.request.WechatPayRefundRequest;
import com.kk.wechat.response.WechatPayRefundResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 微信退款相关
 */
@Service
public class WechatRefundService implements InternalRefundService {
    private Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private PayOrderService payOrderService;
    @Autowired
    private RefundOrderService refundOrderService;
    @Autowired
    private FileResourceService fileResourceService;

    @Override
    public Object refund(PayChannel payChannel, RefundOrder refundOrder) {
        FileResource resource = fileResourceService.getFileResource(payChannel.getCertFileId());
        WechatPayRefundRequest refundRequest = new WechatPayRefundRequest();
        WechatPayRefundModel refundModel = new WechatPayRefundModel();
        refundRequest.setModel(refundModel);
        refundModel.setOutTradeNo(refundOrder.getPayOrderNo());
        refundModel.setTransactionId(refundOrder.getPayId());
        refundModel.setOutRefundNo(refundOrder.getRefundOrderNo());
        refundModel.setTotalFee(refundOrder.getPayAmount());
        refundModel.setRefundFee(refundOrder.getRefundAmount());
        refundModel.setOpUserId(payChannel.getMchId());
        WechatPayClient client = new WechatPayClient(payChannel.getAppId(), payChannel.getMchId(), payChannel.getApiKey(), resource.getData());
        WechatPayRefundResponse refundResponse;
        try {
            refundResponse = client.execute(refundRequest);
        } catch (WechatPayException e) {
            logger.error(e.getMessage(), e);
            refundOrder.setStatus(PayStatus.CREATE_PAYMENT_FAIL.getValue());
            refundOrder.setErrorMsg(e.getErrMsg());
            refundOrder.setErrorCode(e.getErrCode());
            refundOrderService.updateStatus(refundOrder.getId(), RefundStatus.REFUND_FAIL.getValue(), e.getErrCode(), e.getErrMsg());
            throw new PayException(e.getErrMsg());
        }

        if (refundResponse.isSuccess()) {
            refundOrder.setStatus(RefundStatus.REFUND_SUCCESS.getValue());
            refundOrder.setRefundTime(new Date());
            refundOrder.setRefundId(refundResponse.getRefundId());
            refundOrderService.updateRefundOrder(refundOrder);
            // 更新payOrder的状态和退款额度字段，
            payOrderService.updateRefundStatus(refundOrder.getPayOrderNo(), refundOrder.getRefundAmount());
            return true;
        } else {
            refundOrder.setStatus(RefundStatus.REFUND_FAIL.getValue());
            refundOrder.setRefundId(refundResponse.getRefundId());
            refundOrder.setErrorCode(refundResponse.getErrCode());
            refundOrder.setErrorMsg(refundResponse.getErrCodeDes());
            refundOrderService.updateStatus(refundOrder.getId(), RefundStatus.REFUND_FAIL.getValue(), refundResponse.getErrCode(), refundResponse.getErrCodeDes());

            throw new PayException("微信退款失败：" + refundResponse.getErrCode() + "," + refundResponse.getErrCodeDes());
        }

    }

    @Override
    public RefundResponse parse(PayChannel channel, RefundOrder refundOrder, String notify) {
        throw new PayException("此方法不支持");
    }
}
