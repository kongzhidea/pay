package com.kk.test.util;

import com.kk.api.response.PayTypeResponse;
import com.kk.platform.enums.PayTypeCode;
import com.kk.platform.enums.TradeTypeCode;
import org.junit.Test;

public class ResponseTest {

    @Test
    public void testToJson() {
        PayTypeResponse response = new PayTypeResponse();

        response.setMerchantId("mid");
        response.setSign("sign");
        response.setCode("su");
        response.addPayType(PayTypeCode.ALI_PAY, TradeTypeCode.NATIVE);

        System.out.println(response.toJsonString());
    }
}
