package com.kk.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 采用fastjson，不采用Gson，是因为 gson转json的时候，如果等号，则转成\u003d， 而fastjson则直接转成'='
 */
public class BeanUtil {
    public static Log logger = LogFactory.getLog(BeanUtil.class);

    /**
     * @param object 可以处理父类的field
     * @return
     */
    public static Map<String, String> object2Map(Object object) {
        Map<String, String> data = new TreeMap<String, String>();
        try {
            BeanInfo info = Introspector.getBeanInfo(object.getClass(), Introspector.IGNORE_ALL_BEANINFO);
            PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
            for (PropertyDescriptor pd : descriptors) {
                String name = pd.getName();
                Object value = pd.getReadMethod().invoke(object);
                if ("class".equals(name) || value == null || StringUtils.isBlank(value.toString()))
                    continue;
                if (value instanceof Date) {
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    data.put(name, format.format((Date) value));
                } else if (value instanceof Map) {
                    data.put(name, JSON.toJSONString(value, SerializerFeature.WriteDateUseDateFormat));
                } else if (value instanceof List || value instanceof Set) {
                    // 如果调用list.toString方法，会在元素之间加一个空格，但是解析的时候解析成json又去掉了空格，导致解析失败，所以使用json来转string。
                    data.put(name, JSON.toJSONString(value, SerializerFeature.WriteDateUseDateFormat));
                } else {
                    data.put(name, value.toString());
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return data;
    }

    public static Map<String, String> jsonStr2Map(String objStr) {
        Map<String, String> data = new TreeMap<String, String>();
        JSONObject obj = JSONObject.parseObject(objStr);
        for (String key : obj.keySet()) {
            String value = obj.getString(key);
            if (StringUtils.isBlank(value)) {
                continue;
            }
            data.put(key, value);
        }
        return data;
    }
}
