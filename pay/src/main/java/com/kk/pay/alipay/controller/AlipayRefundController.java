package com.kk.pay.alipay.controller;

import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.TradeFundBill;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.demo.trade.model.builder.AlipayTradeRefundContentBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FQueryResult;
import com.alipay.demo.trade.model.result.AlipayF2FRefundResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 订单退款
 */
@Controller
@RequestMapping("/kk/alipay/refund")
public class AlipayRefundController {

    private Log logger = LogFactory.getLog(AlipayRefundController.class);

    // 支付宝当面付2.0服务
    private AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();


    // https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.D90kgM&treeId=26&articleId=759&docType=4
    @RequestMapping(value = "", method = {RequestMethod.GET,
            RequestMethod.POST}, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String refund(HttpServletRequest request, @RequestParam("voucherId") String voucherId,
                         @RequestParam(value = "trade_no", required = false) String tradeNo) {
        // (必填) 外部订单号，需要退款交易的商户外部订单号
        String outTradeNo = voucherId;

        // (必填) 退款金额，该金额必须小于等于订单的支付金额，单位为元
        String refundAmount = "0.01";

        // (可选，需要支持重复退货时必填) 商户退款请求号，相同支付宝交易号下的不同退款请求号对应同一笔交易的不同退款申请，
        // 对于相同支付宝交易号下多笔相同商户退款请求号的退款交易，支付宝只会进行一次退款
        String outRequestNo = "";

        // (必填) 退款原因，可以说明用户退款原因，方便为商家后台提供统计
        String refundReason = "正常退款，用户买多了";

        // (必填) 商户门店编号，退款情况下可以为商家后台提供退款权限判定和统计等作用，详询支付宝技术支持,  还可以填写操作员等信息
        String storeId = "test_store_id";

        AlipayTradeRefundContentBuilder builder = new AlipayTradeRefundContentBuilder()
                .setOutTradeNo(outTradeNo)
                .setTradeNo(tradeNo)
                .setRefundAmount(refundAmount)
                .setRefundReason(refundReason)
                .setOutRequestNo(outRequestNo)
                .setStoreId(storeId);

        AlipayF2FRefundResult result = tradeService.tradeRefund(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                dumpResponse(result.getResponse());
                logger.info("支付宝退款成功: )");
                break;

            case FAILED:
                logger.error("支付宝退款失败!!!");
                break;

            case UNKNOWN:
                logger.error("系统异常，订单退款状态未知!!!");
                break;

            default:
                logger.error("不支持的交易状态，交易返回异常!!!");
                break;
        }
        return "alipay.refund";
    }

    public void dumpResponse(AlipayResponse response) {
        if (response != null) {
            logger.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                logger.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(), response.getSubMsg()));
            }
            logger.info("body:" + response.getBody());
        }
    }
}
