package com.alipay.demo.trade.model.hb;

import com.alipay.demo.trade.utils.Utils;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class TradeInfoAdapter
        implements JsonSerializer<List<TradeInfo>> {
    public JsonElement serialize(List<TradeInfo> tradeInfoList, Type type, JsonSerializationContext jsonSerializationContext) {
        if (Utils.isListEmpty(tradeInfoList)) {
            return null;
        }

        TradeInfo tradeInfo = (TradeInfo) tradeInfoList.get(0);
        if ((tradeInfo instanceof PosTradeInfo)) {
            return new JsonPrimitive(StringUtils.join(tradeInfoList, ""));
        }

        return jsonSerializationContext.serialize(tradeInfoList);
    }
}
