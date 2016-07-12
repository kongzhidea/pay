package com.alipay.demo.trade.model.hb;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class EquipStatusAdapter
        implements JsonSerializer<EquipStatus> {
    public JsonElement serialize(EquipStatus equipStatus, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(equipStatus.getValue());
    }
}
