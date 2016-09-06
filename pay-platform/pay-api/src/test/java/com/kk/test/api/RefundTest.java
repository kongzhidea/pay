package com.kk.test.api;

import com.alibaba.fastjson.JSON;
import com.kk.api.ApiResponse;
import com.kk.api.request.RefundQueryRequest;
import com.kk.api.request.RefundRequest;
import com.kk.api.response.PayResponse;
import com.kk.api.response.RefundQueryResponse;
import com.kk.api.response.RefundResponse;
import com.kk.test.util.Consts;
import com.kk.util.BeanUtil;
import com.kk.util.SignUtils;
import com.kk.utils.ConsoleLogger;
import com.kk.utils.HttpClientUtil;
import org.junit.Test;

import java.util.Map;

/**
 * 退款单元测试
 */
public class RefundTest {
    ConsoleLogger logger = new ConsoleLogger();


    /**
     * 微信，支付宝  退款
     */
    @Test
    public void testRefund() {
        String url = Consts.domain + "/refund/request";

        RefundRequest request = new RefundRequest();
        request.setMerchantId(Consts.mchId);

        request.setPayOrderNo("10017708201609061927393089937178");
        request.setRefundAmount(1);
        request.setRefundReason("测试");

        request.validate();
        // 生成签名
        Map<String, String> data = BeanUtil.object2Map(request);
        String sign = SignUtils.md5(data, Consts.apiKey);

        request.setSign(sign);


        String ret = HttpClientUtil.postData(url, request.toJsonString());

        logger.info("ret=" + ret);

        RefundResponse response = JSON.parseObject(ret, RefundResponse.class);

        logger.info(JSON.toJSONString(response));

        logger.info(PayResponse.isSuccess(response.getCode()));

        // 校验签名
        if (ApiResponse.isSuccess(response.getCode())) {
            data = BeanUtil.object2Map(response);
            sign = SignUtils.md5(data, Consts.apiKey);
            if (!sign.equals(response.getSign())) {
                logger.info("签名不正确,sign=" + sign);
            }
        }
        if (ApiResponse.isSuccess(response.getCode())) {
            data = BeanUtil.jsonStr2Map(ret);
            sign = SignUtils.md5(data, Consts.apiKey);
            if (!sign.equals(response.getSign())) {
                logger.info("签名不正确,sign=" + sign);
            } else {
                logger.info("使用json来校验签名，签名正确");
            }
        }
    }

    /**
     * 微信  退款查询
     */
    @Test
    public void testRefundQuery() {
        String url = Consts.domain + "/refund/query";

        RefundQueryRequest request = new RefundQueryRequest();
        request.setMerchantId(Consts.mchId);

        request.setRefundOrderNo("20160906175502618920009053404606");
//        request.setTradeRefundNo("");

        request.validate();
        // 生成签名
        Map<String, String> data = BeanUtil.object2Map(request);
        String sign = SignUtils.md5(data, Consts.apiKey);

        request.setSign(sign);


        String ret = HttpClientUtil.postData(url, request.toJsonString());

        logger.info("ret=" + ret);

        RefundQueryResponse response = JSON.parseObject(ret, RefundQueryResponse.class);

        logger.info(JSON.toJSONString(response));

        logger.info(PayResponse.isSuccess(response.getCode()));

        // 校验签名
        if (ApiResponse.isSuccess(response.getCode())) {
            data = BeanUtil.object2Map(response);
            sign = SignUtils.md5(data, Consts.apiKey);
            if (!sign.equals(response.getSign())) {
                logger.info("签名不正确,sign=" + sign);
            }
        }
        if (ApiResponse.isSuccess(response.getCode())) {
            data = BeanUtil.jsonStr2Map(ret);
            sign = SignUtils.md5(data, Consts.apiKey);
            if (!sign.equals(response.getSign())) {
                logger.info("签名不正确,sign=" + sign);
            } else {
                logger.info("使用json来校验签名，签名正确");
            }
        }
    }


}
