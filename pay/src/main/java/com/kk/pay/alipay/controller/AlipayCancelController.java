package com.kk.pay.alipay.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.TradeFundBill;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.demo.trade.model.builder.AlipayTradeCancelCententBuilder;
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
 * 撤销订单
 * <p/>
 * 支付交易返回失败或支付系统超时，调用该接口撤销交易。如果此订单用户支付失败，支付宝系统会将此订单关闭；
 * 如果用户支付成功，支付宝系统会将此订单资金退还给用户。
 * 注意：只有发生支付系统超时或者支付结果未知时可调用撤销，其他正常支付的单如需实现相同功能请调用申请退款API  。
 * 提交支付交易后调用【查询订单API】，没有明确的支付结果再调用【撤销订单API】。
 */
@Controller
@RequestMapping("/kk/alipay/cancel")
public class AlipayCancelController {

    private Log logger = LogFactory.getLog(AlipayCancelController.class);

    // 支付宝当面付2.0服务
    private AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();


    // https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.XHJf8I&treeId=26&articleId=866&docType=4
    @RequestMapping(value = "", method = {RequestMethod.GET,
            RequestMethod.POST}, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String refund(HttpServletRequest request, @RequestParam("voucherId") String voucherId,
                         @RequestParam(value = "trade_no", required = false) String tradeNo) {
        // 商户订单号，通过此商户订单号查询当面付的交易状态
        String outTradeNo = voucherId;

        // tradeNo  支付宝订单号，  同事存在时候，以tradeNo优先。

        AlipayTradeCancelCententBuilder builder = new AlipayTradeCancelCententBuilder()
                .setOutTradeNo(outTradeNo)
                .setTradeNo(tradeNo);
        AlipayTradeCancelResponse result = tradeService.tradeCancel(builder);
        logger.info("result=" + JSON.toJSONString(result));

        return "alipay.cancel";
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
