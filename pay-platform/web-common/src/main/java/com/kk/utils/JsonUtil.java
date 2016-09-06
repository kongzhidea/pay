package com.kk.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JsonUtil {

	public static Map<Integer, String> code2msgMap = new HashMap<Integer, String>();

	public static final int JSON_CODE_OK = 0;
	public static final int JSON_CODE_ERROR = -1;

	public static final int PERMISSION_DENY = 1;
	public static final int PARAM_ILLEGAL = 2;

	public static final String JSON_MSG_OK = "ok";
	public static final String JSON_MSG_ERROR = "内部错误";

	static {
		code2msgMap.put(JSON_CODE_OK, JSON_MSG_OK);
		code2msgMap.put(JSON_CODE_ERROR, JSON_MSG_ERROR);
		code2msgMap.put(PERMISSION_DENY, "不允许的操作");
		code2msgMap.put(PARAM_ILLEGAL, "参数不合法");
	}

	
	public static JSONObject getJson(int code) 
	{
		JSONObject json = new JSONObject();
		json.put("code",code);
		return json;
	}
	public static JSONObject getJson(int code, String msg) {
		JSONObject json = new JSONObject();
		json.put("code", code+"");
		json.put("msg", msg);
		return json;
	}
	
	public static JSONObject getJson(int code, int left) 
	{
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
	
	public static JSONObject getErrJson() {
		return getErrJson(JsonUtil.JSON_CODE_ERROR);
	}

	public static JSONObject getErrJson(int code, String message) {
		final JSONObject json = new JSONObject();
		json.put("code", code);
		json.put("msg", message);
		return json;
	}

	public static JSONObject getErrJson(String msg) {
		return getJson(JsonUtil.JSON_CODE_ERROR, msg);
	}

	public static JSONObject getErrJson(final int code) {
		final JSONObject json = new JSONObject();
		json.put("code", code);
		json.put("msg", getMessageByCode(code));
		return json;
	}

	public static JSONObject getOkJson(String msg) {
		JSONObject json = new JSONObject();
		json.put("code", JSON_CODE_OK);
		json.put("msg", msg);
		return json;
	}

	public static JSONObject getOkJson() {
		return getOkJson(JSON_MSG_OK);
	}

	private static String getMessageByCode(int jsonCode) {
		String msg = code2msgMap.get(jsonCode);
		if (msg == null) {
			msg = "";
		}
		return msg;
	}

	public static JSONObject getOkJsonResult(JSONArray jar, boolean hasMore) {
		JSONObject obj = getOkJson();
		obj.put("hasMore", hasMore ? 1 : 0);
		obj.put("data", jar);
		return obj;
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
	
	public static JSONObject getSuccessJsonResult(String msg, Object obj) {
		JSONObject json = new JSONObject();
		json.put("code", "0");
		json.put("msg", msg);
		json.put("body", obj);
		return json;
	}

	public static JSONObject getJsonObject(String key, String value) {
		JSONObject json = new JSONObject();
		json.put(key, value);
		return json;
	}

	public static JSONObject getForwardJsonResult(int code, String message,
			String url) {
		JSONObject json = new JSONObject();
		json.put("statusCode", String.valueOf(code));
		json.put("message", message);
		json.put("callbackType", "forward");
		json.put("forwardUrl", url);
		return json;
	}

	public static JSONObject getErrorJsonResult(String message) {
		JSONObject json = new JSONObject();
		json.put("statusCode", 300);
		json.put("message", message);
		return json;
	}

	public static JSONObject getForwardJsonResult(String url) {
		return getForwardJsonResult(200, null, url);
	}
	
	public static String messageAjax(int statusCode, String message, String navTabId) {
		JSONObject json = new JSONObject();
		json.put("statusCode", statusCode);
		json.put("message", message);
		json.put("navTabId", navTabId);
		return json.toString();
	}

	public static JSONObject getDWZSuccessJsonResult(String message) {
		JSONObject json = new JSONObject();
		json.put("statusCode", 200);
		json.put("message", message);
		return json;
	}

	/**
	 * 和其他公司合作，json返回格式
	 * 
	 * @param error
	 * @param description
	 * @return
	 */
	public static JSONObject getAjax2Json(int error, String description) {
		JSONObject json = new JSONObject();
		json.put("error", error);
		if (description == null) {
			description = "";
		}
		json.put("description", description);
		return json;
	}
	public static JSONObject getTechnicianJson(String message) {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		json.put("code", "20003");
		json.put("msg", message);
		return json;
	}
	
	public static JSONObject getJson(String code , String message) {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		json.put("code", code);
		json.put("msg", message);
		return json;
	}

}