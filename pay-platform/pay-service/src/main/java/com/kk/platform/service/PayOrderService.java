package com.kk.platform.service;

import com.kk.platform.dao.PayOrderDao;
import com.kk.platform.enums.PayStatus;
import com.kk.platform.model.PayOrder;
import com.kk.platform.utils.SnUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayOrderService {
    @Autowired
    private PayOrderDao payOrderDao;

    private String generatePayOrderNo() {
        return SnUtils.generateOrderNo("1001");
    }

    // 创建支付订单，同时生成 第三方订单号
    public void createPayOrder(PayOrder order) {
        order.setPayOrderNo(generatePayOrderNo());
        payOrderDao.insert(order);
    }

    public PayOrder getPayOrder(int id) {
        return payOrderDao.selectById(id);
    }

    public PayOrder getPayOrder(String payOrderNo) {
        return payOrderDao.selectByPayOrderNo(payOrderNo);
    }

    public List<PayOrder> getPayOrders(String mchId, String tradeOrderNo) {
        return payOrderDao.selectByTradeOrderNo(mchId, tradeOrderNo);
    }

    public void updatePayType(int id, String payTypeCode, String tradeType) {
        payOrderDao.updatePayTypeById(id, payTypeCode, tradeType);
    }

    public void updatePayRequest(int id, String prePayId, String codeUrl) {
        payOrderDao.updatePrePayById(id, prePayId, codeUrl, PayStatus.CREATE_PAYMENT_SUCCESS.getValue());
    }

    public void updateStatus(int id, int status, String errorCode, String errorMsg) {
        payOrderDao.updateStatusById(id, status, errorCode, errorMsg);
    }

    public void updateRefund(int id, int status, int refundAmount) {
        payOrderDao.updateRefundById(id, status, refundAmount);
    }

    // 更新订单退款状态（全部退款，部分退款）， 同时更新 payOrder的已退款金额
    public void updateRefundStatus(String payOrderNo, int refundAmount) {
        PayOrder payOrder = getPayOrder(payOrderNo);
        int status = PayStatus.REFUND_SUCCESS.getValue();
        int rfamount = payOrder.getRefundAmount() + refundAmount;
        if (rfamount < payOrder.getPayAmount()) {
            status = PayStatus.REFUND_PART.getValue();
        }
        updateRefund(payOrder.getId(), status, rfamount);
    }

    public void updatePayOrder(PayOrder order) {
        payOrderDao.update(order);
    }

    public void delete(int id) {
        payOrderDao.deleteById(id);
    }
}
