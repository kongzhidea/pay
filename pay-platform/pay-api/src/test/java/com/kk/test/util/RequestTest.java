package com.kk.test.util;

import com.alibaba.fastjson.JSON;
import com.kk.api.request.PayRequest;
import com.kk.platform.enums.PayTypeCode;
import com.kk.util.XmlUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RequestTest {

    @Test
    public void testRequest() {
        PayRequest request = new PayRequest();
        request.setMerchantId(Consts.mchId);
        request.setPayType(PayTypeCode.WECHAT_PAY);

        String str = request.toJsonString();

        System.out.println(str);

        request = JSON.parseObject(str, PayRequest.class);

        System.out.println(request.getPayType().getName());
    }

    @Test
    public void testXml() {
        Map<String, String> data = new HashMap<String, String>();
        data.put("a", "b");

        String xml = XmlUtil.toXml(data);
        System.out.println(xml);

        data = XmlUtil.parseXml(xml);
        System.out.println(data);


    }
}
