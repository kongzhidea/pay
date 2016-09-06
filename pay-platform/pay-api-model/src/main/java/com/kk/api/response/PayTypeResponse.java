package com.kk.api.response;


import com.alibaba.fastjson.JSON;
import com.kk.api.ApiResponse;
import com.kk.platform.enums.PayTypeCode;
import com.kk.platform.enums.TradeTypeCode;

import java.util.ArrayList;
import java.util.List;

public class PayTypeResponse extends ApiResponse {
    private List<PayType> payTypes;

    public PayTypeResponse() {
        this.payTypes = new ArrayList<PayType>();
    }

    public List<PayType> getPayTypes() {
        return payTypes;
    }

    public void setPayTypes(List<PayType> payTypes) {
        this.payTypes = payTypes;
    }

    public void addPayType(PayTypeCode code, TradeTypeCode tradeType) {
        this.payTypes.add(new PayType(code.toString(), code.getName(), tradeType.getName()));
    }

    private class PayType {
        private String code; // WECHAT_PAY
        private String name; // 微信
        private String tradeType; //  扫码

        public PayType() {
        }

        public PayType(String code, String name, String tradeType) {
            this.code = code;
            this.name = name;
            this.tradeType = tradeType;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTradeType() {
            return tradeType;
        }

        public void setTradeType(String tradeType) {
            this.tradeType = tradeType;
        }

        @Override
        public String toString() {
            return JSON.toJSONString(PayType.this);
        }
    }
}
