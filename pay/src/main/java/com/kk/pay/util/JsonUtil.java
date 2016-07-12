package com.kk.pay.util;

import com.alibaba.fastjson.JSONObject;

public class JsonUtil {

    public static final int JSON_CODE_OK = 0;
    public static final int JSON_CODE_ERROR = -1;

    public static JSONObject getJson(int code, String msg) {
        JSONObject json = new JSONObject();
        json.put("code", code + "");
        json.put("msg", msg);
        return json;
    }

    public static JSONObject getJson(int code, int left) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("left", left);
        return json;
    }

    public static JSONObject getJson(JSONObject obj, int code, String msg) {
        obj.put("code", code);
        obj.put("msg", msg);
        return obj;
    }

    public static JSONObject getOkJson(String msg) {
        JSONObject json = new JSONObject();
        json.put("code", JSON_CODE_OK);
        json.put("msg", msg);
        return json;
    }

    public static JSONObject getOkJson() {
        return getOkJson("OK");
    }


    public static JSONObject getOkJsonResult(Object value) {
        JSONObject obj = getOkJson();
        obj.put("data", value);
        return obj;
    }

    public static JSONObject getOkJsonResult(String msg, Object obj) {
        JSONObject json = new JSONObject();
        json.put("code", 0);
        json.put("msg", msg);
        json.put("data", obj);
        return json;
    }

    public static JSONObject getJsonObject(String key, String value) {
        JSONObject json = new JSONObject();
        json.put(key, value);
        return json;
    }


    public static JSONObject getJson(String code, String message) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", message);
        return json;
    }

}