package com.kk.alipay.client;

import com.google.common.base.CaseFormat;
import com.kk.alipay.exception.AliPayException;
import com.kk.alipay.response.AliPayResponse;
import com.kk.util.BeanUtil;
import com.kk.util.SignUtils;
import com.kk.utils.HttpClientUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class DefaultAliPayParser {
    private Log logger = LogFactory.getLog(this.getClass());

    private static final String SERVER_URL = "https://mapi.alipay.com/gateway.do";
    private static final String CHARSET = "utf-8";


    protected String partnerId;
    protected String publicKey;
    protected String privateKey;
    protected String signType;

    public DefaultAliPayParser(String signType, String partnerId, String publicKey) {
        this.signType = signType;
        this.partnerId = partnerId;
        this.publicKey = publicKey;
    }

    public DefaultAliPayParser(String signType, String partnerId, String publicKey, String privateKey) {
        this.signType = signType;
        this.partnerId = partnerId;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    protected boolean verify(String notifyId) throws AliPayException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("service", "notify_verify");
        params.put("partner", partnerId);
        params.put("notify_id", notifyId);
        String rsp = HttpClientUtil.sendGet(SERVER_URL + "?" + convert(params));
        try {
            return Boolean.parseBoolean(rsp);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 校验签名时候使用map（response中字段不一定全）， 将json解析成 java实例
     *
     * @throws Exception
     */
    public <T extends AliPayResponse> T parse(Class<T> clazz, String rsp) throws Exception {
        Map<String, String> map = BeanUtil.jsonStr2Map(rsp);

        T response = convert(clazz, map);

        String sign = map.get("sign");

        if (StringUtils.isBlank(sign)) {
            throw new AliPayException("", "");
        }

        if (response.isSuccess() || (!response.isSuccess() && StringUtils.isNotBlank(response.getSign()))) {
            boolean checkContent = SignUtils.checkRSASign(SignUtils.getSignContent(map, false), sign, publicKey);
            if (checkContent) {
                String notifyId = map.get("notify_id");
                if (StringUtils.isBlank(notifyId)) { // 此处省略 校验notifyId步骤
                    throw new AliPayException("verify fail: verify notify fail!");
                }
            } else {
                throw new AliPayException("sign check fail: check Sign and Data Fail!");
            }
        }

        return response;
    }

    public static String convert(Map<String, String> params) {
        StringBuilder buf = new StringBuilder();
        Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String k = entry.getKey();
            if (StringUtils.isBlank(k)) {
                continue;
            }
            String v = entry.getValue();
            if (StringUtils.isBlank(v)) {
                continue;
            }
            buf.append(k);
            buf.append("=");
            buf.append(v);
            buf.append("&");
        }

        buf = buf.deleteCharAt(buf.length() - 1);
        return buf.toString();
    }

    /**
     * 解析的时候， 支持驼峰式写法，与下划线写法的转换
     *
     * @throws Exception
     */
    public static <T extends AliPayResponse> T convert(Class<T> clazz, Map<String, String> data) throws Exception {
        T rsp = clazz.newInstance();
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();

        for (PropertyDescriptor pd : pds) {
            Method writeMethod = pd.getWriteMethod();
            if (writeMethod == null) { // ignore read-only fields
                continue;
            }

            String itemName = pd.getName();
            // 驼峰式写法->下划线写法
            String mapName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, pd.getName());

            Field field = FieldUtils.getField(clazz, itemName, true);

            String value = data.get(mapName) == null ? data.get(itemName) : data.get(mapName);
            if (value == null) {
                continue;
            }

            Class<?> typeClass = field.getType();
            if (String.class.isAssignableFrom(typeClass)) {
                writeMethod.invoke(rsp, value.toString());
            } else if (Long.class.isAssignableFrom(typeClass) || long.class.isAssignableFrom(typeClass)) {
                if (StringUtils.isNumeric(value)) {
                    writeMethod.invoke(rsp, Long.valueOf(value.toString()));
                }
            } else if (Integer.class.isAssignableFrom(typeClass) || int.class.isAssignableFrom(typeClass)) {
                if (StringUtils.isNumeric(value)) {
                    writeMethod.invoke(rsp, Integer.valueOf(value.toString()));
                }
            } else if (Boolean.class.isAssignableFrom(typeClass) || boolean.class.isAssignableFrom(typeClass)) {
                if (value != null) {
                    writeMethod.invoke(rsp, Boolean.valueOf(value.toString()));
                }
            } else if (Short.class.isAssignableFrom(typeClass) || short.class.isAssignableFrom(typeClass)) {
                if (value != null) {
                    writeMethod.invoke(rsp, Short.valueOf(value.toString()));
                }
            } else if (Double.class.isAssignableFrom(typeClass) || double.class.isAssignableFrom(typeClass)) {
                if (value != null) {
                    writeMethod.invoke(rsp, Double.valueOf(value.toString()));
                }
            } else if (Float.class.isAssignableFrom(typeClass) || float.class.isAssignableFrom(typeClass)) {
                if (value != null) {
                    writeMethod.invoke(rsp, Float.valueOf(value.toString()));
                }
            } else if (Date.class.isAssignableFrom(typeClass)) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                writeMethod.invoke(rsp, format.parse(value.toString()));
            } else {
                if (StringUtils.isNotBlank(value)) {
                    writeMethod.invoke(rsp, value);
                }
            }
        }

        return rsp;
    }
}
