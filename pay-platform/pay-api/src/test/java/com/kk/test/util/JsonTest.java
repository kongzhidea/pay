package com.kk.test.util;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class JsonTest {
    @Test
    public void testJson() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("out_trade_no", "1");
        params.put("seller_id", "a=2");
        System.out.println(JSON.toJSONString(params)); // {"out_trade_no":"1","seller_id":"a=2"}
        System.out.println(new Gson().toJson(params)); // {"seller_id":"a\u003d2","out_trade_no":"1"}
    }
}
