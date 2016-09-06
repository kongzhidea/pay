package com.kk.wechat.client;

import com.kk.util.SignUtils;
import com.kk.util.XmlUtil;
import com.kk.wechat.annotation.ApiRequestField;
import com.kk.wechat.annotation.ApiResponseField;
import com.kk.wechat.exception.WechatPayException;
import com.kk.wechat.model.SignatureItem;
import com.kk.wechat.model.WechatPayModel;
import com.kk.wechat.model.WechatPayTradeStatus;
import com.kk.wechat.request.WechatPayPrePayRequest;
import com.kk.wechat.request.WechatPayRequest;
import com.kk.wechat.response.WechatPayPrePayResponse;
import com.kk.wechat.response.WechatPayResponse;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;

import javax.net.ssl.SSLContext;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.KeyStore;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * 微信支付， 分装的客户端， 对request自动加上签名，转成xml， 发送请求到微信支付，  会返回结果做解析
 */
public class WechatPayClient {
    private Log logger = LogFactory.getLog(this.getClass());

    private static final String SERVER_URL = "https://api.mch.weixin.qq.com";
    private String appId; // 微信为公众账号Id
    private String mchId; // 微信支付 商户号Id
    private String apiKey;// 秘钥，用于签名
    private byte[] certFile;// 退款时候 数字证书

    public WechatPayClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public WechatPayClient(String appId, String mchId, String apiKey) {
        this.appId = appId;
        this.mchId = mchId;
        this.apiKey = apiKey;
    }

    public WechatPayClient(String appId, String mchId, String apiKey, byte[] certFile) {
        this.appId = appId;
        this.mchId = mchId;
        this.apiKey = apiKey;
        this.certFile = certFile;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public byte[] getCertFile() {
        return certFile;
    }

    public void setCertFile(byte[] certFile) {
        this.certFile = certFile;
    }

    public <T extends WechatPayResponse> T execute(WechatPayRequest<? extends WechatPayModel, T> request) throws WechatPayException {
        String rsp = post(SERVER_URL + request.getApiAction(), request);
        logger.info("wechat.pay.rsp=" + rsp);

        T response = parse(request.getResponseClass(), rsp);

        return response;
    }

    /**
     * 获取 返回值中的sign，以及计算sign的字符串
     *
     * @param rsp
     * @return
     */
    private SignatureItem getSignatureItem(String rsp) {
        Map<String, String> data = XmlUtil.parseXml(rsp);
        if (data == null || data.isEmpty()) {
            return null;
        }
        if (StringUtils.isBlank(data.get("sign"))) {
            return null;
        }
        SignatureItem signatureItem = new SignatureItem();
        signatureItem.setSign(data.get("sign"));
        signatureItem.setSignContent(SignUtils.getSignContent(data, true));
        return signatureItem;
    }

    /**
     * 根据返回值 解析成responseModel，校验签名
     *
     * @param clazz
     * @param rsp
     * @param <T>
     * @return
     * @throws WechatPayException
     */
    public <T extends WechatPayResponse> T parse(Class<T> clazz, String rsp) throws WechatPayException {
        Map<String, String> data = XmlUtil.parseXml(rsp);
        T response = convert(clazz, data);

        if (response == null) {
            throw new WechatPayException("微信支付 解析结果失败!");
        }

        SignatureItem signItem = getSignatureItem(rsp);
        if (signItem == null) {
            throw new WechatPayException(response.getReturnCode(), response.getReturnMsg());
        }

        if (response.isSuccess() || (!response.isSuccess() && !StringUtils.isEmpty(signItem.getSign()))) {
            boolean checkContent = SignUtils.checkMd5Sign(signItem.getSignContent(), signItem.getSign(), this.apiKey);
            if (!checkContent) {
                throw new WechatPayException("sign check fail: check Sign and Data Fail!");
            }
        }
        return response;
    }

    /**
     * 根据返回值 解析成responseModel
     *
     * @param clazz
     * @param data
     * @param <T>
     * @return
     * @throws WechatPayException
     */
    public static <T> T convert(Class<T> clazz, Map<String, String> data) throws WechatPayException {
        T rsp = null;

        try {
            rsp = clazz.newInstance();
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();

            for (PropertyDescriptor pd : pds) {
                Method writeMethod = pd.getWriteMethod();
                if (writeMethod == null) { // ignore read-only fields
                    continue;
                }

                String itemName = pd.getName();

                Field field = FieldUtils.getField(clazz, itemName, true);
                ApiResponseField apiField = field.getAnnotation(ApiResponseField.class);
                if (apiField == null) {
                    continue;
                }

                String value = data.get(apiField.value());
                if (value == null) {
                    continue;
                }

                Class<?> typeClass = field.getType();
                if (String.class.isAssignableFrom(typeClass)) {
                    writeMethod.invoke(rsp, value.toString());
                } else if (Long.class.isAssignableFrom(typeClass)) {
                    if (StringUtils.isNumeric(value)) {
                        writeMethod.invoke(rsp, Long.valueOf(value.toString()));
                    }
                } else if (Integer.class.isAssignableFrom(typeClass)) {
                    if (StringUtils.isNumeric(value)) {
                        writeMethod.invoke(rsp, Integer.valueOf(value.toString()));
                    }
                } else if (Boolean.class.isAssignableFrom(typeClass)) {
                    if (value != null) {
                        writeMethod.invoke(rsp, Boolean.valueOf(value.toString()));
                    }
                } else if (Date.class.isAssignableFrom(typeClass)) {
                    DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                    writeMethod.invoke(rsp, format.parse(value.toString()));
                } else if (WechatPayTradeStatus.class.isAssignableFrom(typeClass)) {
                    if (StringUtils.isNotBlank(value)) {
                        writeMethod.invoke(rsp, WechatPayTradeStatus.valueOf(value));
                    }
                } else {
                    if (StringUtils.isNotBlank(value)) {
                        writeMethod.invoke(rsp, value);
                    }
                }
            }

        } catch (Exception e) {
            throw new WechatPayException(e);
        }

        return rsp;
    }

    /**
     * 发起微信支付请求
     *
     * @param url
     * @param request
     * @param <T>
     * @return
     * @throws WechatPayException
     */
    private <T extends WechatPayResponse> String post(String url, WechatPayRequest<? extends WechatPayModel, T> request) throws WechatPayException {
        WechatPayModel model = request.getModel();

        model.setAppId(appId);
        model.setMchId(mchId);
        model.setNonceStr(RandomStringUtils.random(32, true, true));

        Map<String, String> data = convert(model);

        logger.info(String.format("wechat.pay.url=%s,request=%s", url, data));

        String sign = SignUtils.md5(data, apiKey);
        model.setSign(sign);
        data.put("sign", sign);
        validate(model);


        String text = "";
        CloseableHttpClient client = this.getClient(request); //判断是否需要带上 支付证书
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");

            StringEntity payload = new StringEntity(XmlUtil.toXml(data), "UTF-8");
            httpPost.setEntity(payload);
            text = client.execute(httpPost, new ResponseHandler<String>() {
                @Override
                public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                    StringBuilder builder = new StringBuilder();
                    HttpEntity entity = response.getEntity();
                    String text;
                    if (entity != null) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
                        while ((text = bufferedReader.readLine()) != null) {
                            builder.append(text);
                        }

                    }
                    return builder.toString();
                }
            });
        } catch (Exception e) {
            throw new WechatPayException(e);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                throw new WechatPayException(e);
            }
        }
        return text;
    }

    //判断是否需要带上 支付证书
    private <T extends WechatPayResponse> CloseableHttpClient getClient(WechatPayRequest<? extends WechatPayModel, T> request) throws WechatPayException {
        CloseableHttpClient client;
        if (request.requireCert()) {
            try {
                KeyStore keyStore = KeyStore.getInstance("PKCS12");
                ByteArrayInputStream inputStream = new ByteArrayInputStream(certFile);
                try {
                    keyStore.load(inputStream, this.mchId.toCharArray());
                } finally {
                    inputStream.close();
                }

                SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, this.mchId.toCharArray()).build();
                SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
                client = HttpClients.custom().setSSLSocketFactory(factory).build();
            } catch (Exception e) {
                throw new WechatPayException(e);
            }

        } else {
            client = HttpClients.createDefault();
        }
        return client;
    }

    // 校验 支付参数
    private void validate(WechatPayModel model) throws WechatPayException {
        try {
            Class clazz = model.getClass();
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor pd : pds) {
                if (pd.getReadMethod() == null || pd.getWriteMethod() == null) {
                    continue;
                }
                String fieldName = pd.getName();
                Field field = FieldUtils.getField(clazz, fieldName, true);
                ApiRequestField requestField = field.getAnnotation(ApiRequestField.class);
                if (requestField == null) {
                    continue;
                }

                if (requestField.required()) {
                    Object value = pd.getReadMethod().invoke(model);

                    if (value == null) {
                        throw new WechatPayException(fieldName + " can not be empty");
                    }

                    if (value instanceof String) {
                        if (StringUtils.isBlank((String) value)) {
                            throw new WechatPayException(fieldName + " can not be empty");
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new WechatPayException(e);
        }
    }

    // 将支付参数 class转成map格式
    private Map<String, String> convert(WechatPayModel model) throws WechatPayException {
        Map<String, String> data = new HashMap<String, String>();
        try {
            Class clazz = model.getClass();
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor pd : pds) {
                if (pd.getReadMethod() == null || pd.getWriteMethod() == null) {
                    continue;
                }
                String fieldName = pd.getName();
                Field field = FieldUtils.getField(clazz, fieldName, true);
                ApiRequestField requestField = field.getAnnotation(ApiRequestField.class);
                if (requestField == null) {
                    continue;
                }

                Object value = pd.getReadMethod().invoke(model);
                String strValue;
                if (value == null) {
                    continue;
                } else if (value instanceof String) {
                    if (StringUtils.isBlank((String) value)) {
                        continue;
                    }
                    strValue = (String) value;
                } else if (value instanceof Integer) {
                    strValue = ((Integer) value).toString();
                } else if (value instanceof Long) {
                    strValue = ((Long) value).toString();
                } else if (value instanceof Float) {
                    strValue = ((Float) value).toString();
                } else if (value instanceof Double) {
                    strValue = ((Double) value).toString();
                } else if (value instanceof Boolean) {
                    strValue = ((Boolean) value).toString();
                } else if (value instanceof Date) {
                    DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                    strValue = format.format((Date) value);
                } else {
                    strValue = value.toString();
                }

                data.put(requestField.value(), strValue);
            }
        } catch (Exception e) {
            throw new WechatPayException(e);
        }
        return data;
    }
}


















































