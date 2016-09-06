package com.kk.test.api;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kk.api.request.PayTypeRequest;
import com.kk.api.response.PayResponse;
import com.kk.api.response.PayTypeResponse;
import com.kk.api.service.AbstractPayService;
import com.kk.test.util.Consts;
import com.kk.util.BeanUtil;
import com.kk.util.SignUtils;
import com.kk.utils.HttpClientUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigTest {

    @Test
    public void testConfig() {
        String url = Consts.domain + "/config/payTypes";

        PayTypeRequest request = new PayTypeRequest();
//        request.setTradeType("WAP");
        request.setMerchantId(Consts.mchId);

        // 生成签名
        Map<String, String> data = BeanUtil.object2Map(request);
        String sign = SignUtils.md5(data, Consts.apiKey);

        request.setSign(sign);


        String ret = HttpClientUtil.postData(url, request.toJsonString());

        PayTypeResponse response = JSON.parseObject(ret, PayTypeResponse.class);

        System.out.println(JSON.toJSONString(response));

        System.out.println(PayTypeResponse.isSuccess(response.getCode()));

        // 校验签名
        if (PayTypeResponse.isSuccess(response.getCode())) {
            data = BeanUtil.object2Map(response);
            System.out.println(data);
            sign = SignUtils.md5(data, Consts.apiKey);
            if (!sign.equals(response.getSign())) {
                System.out.println("签名不正确,sign=" + sign);
            }
        }
        if (PayResponse.isSuccess(response.getCode())) {
            data = BeanUtil.jsonStr2Map(ret);
            System.out.println(data);
            sign = SignUtils.md5(data, Consts.apiKey);
            if (!sign.equals(response.getSign())) {
                System.out.println("签名不正确,sign=" + sign);
            } else {
                System.out.println("使用json来校验签名，签名正确");
            }
        }

        List<Integer> ss = new ArrayList<Integer>();
        ss.add(1);
        ss.add(2);
        System.out.println(ss);
        System.out.println(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(ss));
    }
}