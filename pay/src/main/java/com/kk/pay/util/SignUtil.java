package com.kk.pay.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class SignUtil {
    public static final Log logger = LogFactory.getLog(SignUtil.class);

    public static String sign(Object object, String key) {
        if (object == null) {
            return null;
        }

        Map<String, Object> data = BeanUtil.object2Map(object);
        if (data == null || data.isEmpty()) {
            return null;
        }

        return sign(data, key);
    }

    public static String sign(Map<String, Object> data, String key) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        TreeMap<String, Object> map = new TreeMap<String, Object>(data);

        Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            Object k = entry.getKey();
            if ("class".equals(k) || "key".equals(k) || "sign".equals(k)) {
                continue;
            }
            Object v = entry.getValue(); //  非空
            if (v == null || "".equals(v.toString())) {
                continue;
            }
            buf.append(k);
            buf.append("=");
            buf.append(v);
            buf.append("&");
        }
        buf.append("key=" + key);
        logger.debug(buf.toString());

        String sign = EncryptUtil.md5(buf.toString()).toUpperCase();
        return sign;
    }
}
