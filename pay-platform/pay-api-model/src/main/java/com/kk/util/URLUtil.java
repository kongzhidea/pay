package com.kk.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLUtil {

    public static String BAIDU_SHORT_SERVICE = "http://dwz.cn/create.php";

    public static String SINA_SHORT_SERVICE = "http://api.t.sina.com.cn/short_url/shorten.json?source=1681459862&url_long=";

    public static String getParameter(String url, String parameter) {
        if (url == null) {
            return null;
        }
        String reg = "(^|&|\\?)" + parameter + "=([^&]*)(&|$)";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            try {
                String src = URLDecoder.decode(matcher.group(2), "UTF-8");
                return src;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 生成短连接
     */
    public static String generateShortUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return "";
        }
        String shortUrl = generateBaiduShortUrl(url);
        if (shortUrl.equals(url)) {
            shortUrl = generateSinaShortUrl(url);
        }
        return shortUrl;
    }

    // 新浪 短链接
    public static String generateSinaShortUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return "";
        }
        try {
            String jsonStr = HttpClientUtil.sendGet(SINA_SHORT_SERVICE + url);

            if (StringUtils.isBlank(jsonStr)) {
                return url;
            }
            JSONArray jsonArray = JSON.parseArray(jsonStr);
            if (jsonArray.size() == 1) {
                return jsonArray.getJSONObject(0).getString("url_short");
            }
            return url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    // 百度 短链接
    public static String generateBaiduShortUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return "";
        }
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("url", url);

            String jsonStr = HttpClientUtil.sendPost(BAIDU_SHORT_SERVICE, params, "utf-8");
            JSONObject object = JSON.parseObject(jsonStr);

            String shortUrl = null;
            if (object.getString("status").equals("0")) {
                shortUrl = object.getString("tinyurl");
            } else {
                shortUrl = url;
            }
            return shortUrl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public static void main(String[] args) {
//        System.out.println(getParameter("page=2&t=1", "page"));
        System.out.println(generateShortUrl("https://www.baidu.com"));
    }
    
}