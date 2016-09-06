package com.kk.test.api;

import com.alibaba.fastjson.JSON;
import com.google.gson.GsonBuilder;
import com.kk.api.ApiResponse;
import com.kk.api.request.PayQueryRequest;
import com.kk.api.request.PayRequest;
import com.kk.api.response.PayNotifyResponse;
import com.kk.api.response.PayQueryResponse;
import com.kk.api.response.PayResponse;
import com.kk.api.response.PaySynResponse;
import com.kk.platform.enums.PayTypeCode;
import com.kk.platform.enums.TradeTypeCode;
import com.kk.test.util.Consts;
import com.kk.util.BeanUtil;
import com.kk.util.SignUtils;
import com.kk.utils.ConsoleLogger;
import com.kk.utils.HttpClientUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 调用的时候，如果使用sdk，那么 依赖的model 一定要和后端的保持一致，否则签名错误，不推荐
 * <p/>
 * 如果校验签名 直接用json来校验则没有问题。  推荐
 */
public class PayTest {
    ConsoleLogger logger = new ConsoleLogger();

    @Test
    public void testNotifySign() {
        String notify = "{\"payType\":\"WECHAT_PAY\",\"extra\":\"extra.env\\u003dtest\",\"payAmount\":1,\"tradePayNo\":\"kk_test1472636613445\",\"payOrderNo\":\"10019022201608311739103793520313\",\"status\":\"SUCCESS\",\"payTime\":\"2016-08-31 17:39:52\",\"code\":\"SUCCESS\",\"msg\":\"SUCCESS\",\"sign\":\"CF50C168ADD457884C1A0F140F9C219F\",\"merchantId\":\"0001\"}";
        PayNotifyResponse response = JSON.parseObject(notify, PayNotifyResponse.class);

        logger.info(JSON.toJSONString(response, true));

        Map<String, String> data = BeanUtil.object2Map(response);
        String sign = SignUtils.md5(data, Consts.apiKey);

        logger.info(sign.equals(response.getSign()));

        data = new HashMap<String, String>();
        data.put("key", "a=1");

        logger.info(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(data));
        logger.info(JSON.toJSONString(data));
    }

    /**
     * 微信 扫码支付
     */
    @Test
    public void testWeChatNativePay() {
        String url = Consts.domain + "/pay/request";

        PayRequest request = new PayRequest();
        request.setMerchantId(Consts.mchId);
        request.setTradePayNo("kk_test" + System.currentTimeMillis());
//        request.setTradePayNo("kk_test1472552781014");
        request.setPayAmount(1);
        request.setPayType(PayTypeCode.WECHAT_PAY);
        request.setTradeType(TradeTypeCode.NATIVE);
        request.setSubject("kk扫码测试标题");
        request.setDetail("kk扫码测试详情");
        // 支付成功后回调url
        request.setNotifyUrl(Consts.notifyUrl);
        request.setExtra("extra.env=test");

        request.validate();
        // 生成签名
        Map<String, String> data = BeanUtil.object2Map(request);
        String sign = SignUtils.md5(data, Consts.apiKey);

        request.setSign(sign);


        String ret = HttpClientUtil.postData(url, request.toJsonString());

        logger.info("ret=" + ret);

        PayResponse response = JSON.parseObject(ret, PayResponse.class);

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
     * 微信 h5支付
     */
    @Test
    public void testWeChatWapPay() {
        String url = Consts.domain + "/pay/request";

        PayRequest request = new PayRequest();
        request.setMerchantId(Consts.mchId);
        request.setTradePayNo("kk_test" + System.currentTimeMillis());
//        request.setTradePayNo("kk_test1472552781014");
        request.setPayAmount(1);
        request.setPayType(PayTypeCode.WECHAT_PAY);

        request.setTradeType(TradeTypeCode.WAP); //!!

        request.setSubject("kk网页测试标题");
        request.setDetail("kk网页测试详情");
        // 支付成功后回调url
        request.setNotifyUrl(Consts.notifyUrl);
        request.setExtra("extra.env=test");

        request.setOpenId(Consts.openId);// !!

        request.validate();

        // 生成签名
        Map<String, String> data = BeanUtil.object2Map(request);
        String sign = SignUtils.md5(data, Consts.apiKey);

        request.setSign(sign);


        String ret = HttpClientUtil.postData(url, request.toJsonString());

        logger.info("ret=" + ret);

        PayResponse response = JSON.parseObject(ret, PayResponse.class);

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
                logger.info("json签名不正确,sign=" + sign);
            } else {
                logger.info("使用json来校验签名，签名正确");
            }
        }
    }


    /**
     * 微信 app支付
     */
    @Test
    public void testWeChatAppPay() {
        String url = Consts.domain + "/pay/request";

        PayRequest request = new PayRequest();
        request.setMerchantId(Consts.mchId);
        request.setTradePayNo("kk_test" + System.currentTimeMillis());
//        request.setTradePayNo("kk_test1472552781014");
        request.setPayAmount(1);
        request.setPayType(PayTypeCode.WECHAT_PAY);
        request.setTradeType(TradeTypeCode.APP);
        request.setSubject("kk app测试标题");
        request.setDetail("kk app测试详情");
        // 支付成功后回调url
        request.setNotifyUrl(Consts.notifyUrl);
        request.setExtra("extra.env=test");

        request.validate();
        // 生成签名
        Map<String, String> data = BeanUtil.object2Map(request);
        String sign = SignUtils.md5(data, Consts.apiKey);

        request.setSign(sign);


        String ret = HttpClientUtil.postData(url, request.toJsonString());

        logger.info("ret=" + ret);

        PayResponse response = JSON.parseObject(ret, PayResponse.class);

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

    @Test
    public void testQuery() {
        String url = Consts.domain + "/pay/query";

        String payOrderNo = "10017708201609061927393089937178";


        PayQueryRequest request = new PayQueryRequest();
        request.setMerchantId("0001");
        request.setPayOrderNo(payOrderNo);
//        request.setTradePayNo("kk_test1472635152812");

        request.validate();

        // 生成签名
        Map<String, String> data = BeanUtil.object2Map(request);
        String sign = SignUtils.md5(data, Consts.apiKey);

        request.setSign(sign);

        String ret = HttpClientUtil.postData(url, request.toJsonString());

        logger.info("ret=" + ret);

        PayQueryResponse response = JSON.parseObject(ret, PayQueryResponse.class);

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

    @Test
    public void testSync() {
        String url = Consts.domain + "/pay/synchronize";

        String payOrderNo = "10011637201609061416428077639761";


        PayQueryRequest request = new PayQueryRequest();
        request.setMerchantId(Consts.mchId);
        request.setPayOrderNo(payOrderNo);
//        request.setTradePayNo("kk_test1472635152812");
        // 生成签名
        Map<String, String> data = BeanUtil.object2Map(request);
        String sign = SignUtils.md5(data, Consts.apiKey);

        request.setSign(sign);

        String ret = HttpClientUtil.postData(url, request.toJsonString());

        logger.info("ret=" + ret);

        PaySynResponse response = JSON.parseObject(ret, PaySynResponse.class);

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
     * 微信 扫码支付
     */
    @Test
    public void testAliNativePay() {
        String url = Consts.domain + "/pay/request";

        PayRequest request = new PayRequest();
        request.setMerchantId(Consts.mchId);
        request.setTradePayNo("kk_test" + System.currentTimeMillis());
        request.setPayAmount(1);
        request.setPayType(PayTypeCode.ALI_PAY);
        request.setTradeType(TradeTypeCode.NATIVE);
        request.setSubject("kk扫码测试标题");
        request.setDetail("kk扫码测试详情");
        // 支付成功后回调url
        request.setNotifyUrl(Consts.notifyUrl);
        request.setExtra("extra.env=test");

        request.validate();
        // 生成签名
        Map<String, String> data = BeanUtil.object2Map(request);
        String sign = SignUtils.md5(data, Consts.apiKey);

        request.setSign(sign);


        String ret = HttpClientUtil.postData(url, request.toJsonString());

        logger.info("ret=" + ret);

        PayResponse response = JSON.parseObject(ret, PayResponse.class);

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
     * 支付宝 h5支付
     */
    @Test
    public void testAliWapPay() {
        String url = Consts.domain + "/pay/request";

        PayRequest request = new PayRequest();
        request.setMerchantId(Consts.mchId);
        request.setTradePayNo("kk_test" + System.currentTimeMillis());
//        request.setTradePayNo("kk_test1472552781014");
        request.setPayAmount(1);
        request.setPayType(PayTypeCode.ALI_PAY);

        request.setTradeType(TradeTypeCode.WAP); //!!
        request.setReturnUrl("return.url");

        request.setSubject("kk网页测试标题");
        request.setDetail("kk网页测试详情");
        // 支付成功后回调url
        request.setNotifyUrl(Consts.notifyUrl);
        request.setExtra("extra.env=test");

        request.setOpenId(Consts.openId);// !!

        request.validate();

        // 生成签名
        Map<String, String> data = BeanUtil.object2Map(request);
        String sign = SignUtils.md5(data, Consts.apiKey);

        request.setSign(sign);


        String ret = HttpClientUtil.postData(url, request.toJsonString());

        logger.info("ret=" + ret);

        PayResponse response = JSON.parseObject(ret, PayResponse.class);

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
                logger.info("json签名不正确,sign=" + sign);
            } else {
                logger.info("使用json来校验签名，签名正确");
            }
        }
    }
}
