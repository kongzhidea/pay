package com.alipay.demo.trade.model.builder;

import com.alipay.demo.trade.utils.GsonFactory;
import com.google.gson.Gson;

public abstract class RequestBuilder {
    public abstract boolean validate();

    public String toJsonString() {
        return GsonFactory.getGson().toJson(this);
    }
}
