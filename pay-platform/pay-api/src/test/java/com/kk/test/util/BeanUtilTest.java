package com.kk.test.util;

import com.google.common.base.CaseFormat;
import com.kk.api.request.PayTypeRequest;
import com.kk.platform.enums.TradeTypeCode;
import com.kk.util.BeanUtil;
import com.kk.util.SignUtils;
import com.kk.wechat.model.WechatPayPrePayModel;
import org.apache.commons.lang.reflect.FieldUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BeanUtilTest {

    @Test
    public void testBean() {
        PayTypeRequest request = new PayTypeRequest();
        request.setTradeType(TradeTypeCode.NATIVE.toString());
        request.setMerchantId("mchid");
        request.setSign("sign");

        System.out.println(BeanUtil.object2Map(request));
    }

    @Test
    public void testField() {
        WechatPayPrePayModel model = new WechatPayPrePayModel();

        model.setAppId("appid");

        model.setBody("body");

        Field ap = FieldUtils.getField(model.getClass(), "appId", true);
        System.out.println(ap);

        Field b = FieldUtils.getField(model.getClass(), "body", true);
        System.out.println(b);
    }

    @Test
    public void testStr() {
        String name = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "userName");
        System.out.println(name); //  user_name
    }

    @Test
    public void testSignContent() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("partner", "2001");
        System.out.println(SignUtils.getSignContent(map, false));
    }
}
