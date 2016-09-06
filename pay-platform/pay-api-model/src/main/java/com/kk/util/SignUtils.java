package com.kk.util;

import com.kk.utils.MD5Util;
import com.kk.utils.RSA;
import org.apache.commons.lang.StringUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * 签名算法，见sign.md
 * <p/>
 * 业务方 计算签名方法：
 * Map<String, String> data = BeanUtil.object2Map(response);
 * String sign = SignUtils.md5(data, apiKey);
 * assert(sign.equals(response.getSign()));
 * <p/>
 * <p/>
 * 业务方计算签名的方法  与  微信计算签名的方法一致(https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_3)
 */
public class SignUtils {

    public static String md5(Map<String, String> data, String key) {
        String content = getSignContent(data, true);
        if (StringUtils.isBlank(content)) {
            return null;
        }
        StringBuilder buf = new StringBuilder(content);
        buf.append("&").append("key=" + key);

        String sign = MD5Util.md5(buf.toString()).toUpperCase();
        return sign;
    }

    public static String rsa(Map<String, String> data, String privateKey) {
        String content = getSignContent(data, false);
        if (StringUtils.isBlank(content)) {
            return null;
        }
        return RSA.encrypt(content, privateKey);
    }

    /**
     * @param data
     * @param withSignType sign_type字段，  支付宝支付回调会有此字段。 true,计算签名(微信)；  false 不计算签名(支付宝)
     * @return
     */
    public static String getSignContent(Map<String, String> data, boolean withSignType) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        TreeMap<String, String> map = new TreeMap<String, String>(data);

        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String k = entry.getKey();
            if (StringUtils.isBlank(k)) {
                continue;
            }
            if ("class".equalsIgnoreCase(k) || "key".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {
                continue;
            }
            if (!withSignType && "sign_type".equalsIgnoreCase(k)) {
                continue;
            }
            String v = entry.getValue();
            // 字段为空，不参与签名
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

    public static boolean checkMd5Sign(String content, String sign, String key) {
        if (StringUtils.isBlank(content)) {
            return false;
        }
        StringBuilder buf = new StringBuilder(content);
        buf.append("&key=" + key);

        return MD5Util.md5(buf.toString()).equalsIgnoreCase(sign);
    }

    /**
     * 如果是map：
     * SignUtils.checkRSASign(SignUtils.getSignContent(map, false), sign, publicKey)
     *
     * @param content
     * @param sign
     * @param publicKey
     * @return
     */
    public static boolean checkRSASign(String content, String sign, String publicKey) {
        if (StringUtils.isBlank(content)) {
            return false;
        }
        return RSA.verify(sign, content, publicKey);
    }

}
