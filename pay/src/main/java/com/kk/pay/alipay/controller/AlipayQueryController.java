package com.kk.pay.alipay.controller;

import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.TradeFundBill;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.demo.trade.model.builder.AlipayTradeQueryCententBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FQueryResult;
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
 * 订单查询
 */
@Controller
@RequestMapping("/kk/alipay/query")
public class AlipayQueryController {

    private Log logger = LogFactory.getLog(AlipayQueryController.class);

    // 支付宝当面付2.0服务
    private AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();


    // https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.AVzdhN&treeId=26&articleId=757&docType=4
    @RequestMapping(value = "", method = {RequestMethod.GET,
            RequestMethod.POST}, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String query(HttpServletRequest request, @RequestParam("voucherId") String voucherId,
                        @RequestParam(value = "trade_no", required = false) String tradeNo) {
        // 商户订单号，通过此商户订单号查询当面付的交易状态
        String outTradeNo = voucherId;

        // tradeNo  支付宝订单号，  同事存在时候，以tradeNo优先。

        AlipayTradeQueryCententBuilder builder = new AlipayTradeQueryCententBuilder()
                .setOutTradeNo(outTradeNo)
                .setTradeNo(tradeNo);
        AlipayF2FQueryResult result = tradeService.queryTradeResult(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                logger.info("查询返回该订单支付成功: )");

                AlipayTradeQueryResponse response = result.getResponse();
                dumpResponse(response);

                logger.info(response.getTradeStatus());
                if (Utils.isListNotEmpty(response.getFundBillList())) {
                    for (TradeFundBill bill : response.getFundBillList()) {
                        logger.info(bill.getFundChannel() + ":" + bill.getAmount());
                    }
                }
                break;

            case FAILED:
                dumpResponse(result.getResponse());
                logger.error("查询返回该订单支付失败或被关闭!!!");
                break;

            case UNKNOWN:
                logger.error("系统异常，订单支付状态未知!!!");
                break;

            default:
                logger.error("不支持的交易状态，交易返回异常!!!");
                break;
        }
        return "alipay.query";
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
