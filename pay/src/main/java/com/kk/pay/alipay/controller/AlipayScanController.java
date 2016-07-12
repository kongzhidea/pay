package com.kk.pay.alipay.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayResponse;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePayContentBuilder;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateContentBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPayResult;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeWithHBServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 扫码支付
 */
@Controller
@RequestMapping("/kk/alipay")
public class AlipayScanController {

    private Log logger = LogFactory.getLog(AlipayScanController.class);

    // 支付宝当面付2.0服务
    private AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();


    // https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.AReTP2&treeId=26&articleId=862&docType=4
    @RequestMapping(value = "tradeprecreate", method = {RequestMethod.GET,
            RequestMethod.POST}, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String tradeprecreate(HttpServletRequest request) {
        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = "tradeprecreate" + System.currentTimeMillis() + (long) (Math.random() * 10000000L);

        // (必填) 订单标题，粗略描述用户的支付目的
        String subject = "e保养支付测试";

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = "0.01";

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = Configs.getPid();

        // 订单描述，可以对交易或商品进行一个详细地描述
        String body = "发动机舱清洗测试";

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
//        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
        GoodsDetail goods1 = GoodsDetail.newInstance("goods_id001", "发动机舱杭虎", 5900, 1);
        // 创建好一个商品后添加至商品明细列表
        goodsDetailList.add(goods1);

        // 继续创建并添加第一条商品信息，用户购买的产品为“黑人牙刷”，单价为5.05元，购买了两件
        GoodsDetail goods2 = GoodsDetail.newInstance("goods_id002", "玻璃水", 1500, 2);
        goodsDetailList.add(goods2);

        AlipayTradePrecreateContentBuilder builder = new AlipayTradePrecreateContentBuilder()
                .setSubject(subject)
                .setTotalAmount(totalAmount)
                .setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount)
                .setSellerId(sellerId)
                .setBody(body)
                .setOperatorId(operatorId)
                .setStoreId(storeId)
                .setExtendParams(extendParams)
                .setTimeExpress(timeExpress)
                .setGoodsDetailList(goodsDetailList);


        // 支付成功回调url
        String notifyUrl = "http://www.kk.com/kk/alipay/notify";

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder, notifyUrl);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                logger.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                // 需要修改为运行机器上的路径
                String filePath = String.format("/data/zhihui.kong/data/alipay/qr-%s.png", response.getOutTradeNo());
                logger.info("filePath:" + filePath + ",qrcode=" + response.getQrCode());
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);
                break;

            case FAILED:
                logger.error("支付宝预下单失败!!!");
                break;

            case UNKNOWN:
                logger.error("系统异常，预下单状态未知!!!");
                break;

            default:
                logger.error("不支持的交易状态，交易返回异常!!!");
                break;
        }
        return "alipay.scan";
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

    // https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.zW5mtU&treeId=26&articleId=103296&docType=1#s1
    @RequestMapping(value = "notify", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    public String notify(HttpServletRequest request) throws AlipayApiException {

        Map map = request.getParameterMap();
        Map<String, String> params = new HashMap<String, String>(map.size());
        for (Object key : map.keySet()) {
            params.put(key.toString(), getParamString(map, key.toString()));
        }
        logger.info("params=" + params);
        if (AlipaySignature.rsaCheckV1(params, Configs.getAlipayPublicKey(), "utf-8")) {
            logger.info("签名正确");
            return "success";
        }
        return "success";
    }

    private static String getParamString(Map params, String key) {
        String buf = "";
        if (params.get(key) instanceof String[]) {
            buf = ((String[]) params.get(key))[0];
        } else {
            buf = (String) params.get(key);
        }
        return buf;
    }
}
