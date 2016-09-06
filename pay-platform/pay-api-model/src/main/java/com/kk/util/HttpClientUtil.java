package com.kk.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HttpClient调用工具类
 */
public class HttpClientUtil {
    private static final Log logger = LogFactory.getLog("httpClient");

    /**
     * 发送GET请求
     *
     * @param uri
     * @return
     */
    public static String sendGet(String uri) {
        String responseBody = null;
        HttpClient httpClient = new DefaultHttpClient();
        // 设置超时时间
        httpClient.getParams().setParameter(
                CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                10000);
        try {
            // URLEncoder.encode(uri, "utf-8");
            HttpGet httpGet = new HttpGet(uri);
            logger.info("executing request " + httpGet.getURI());
            // Create a response handler
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            responseBody = httpClient.execute(httpGet, responseHandler);
            logger.info(responseBody);
        } catch (ClientProtocolException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseBody;
    }

    /**
     * 发送GET请求,返回字节数组
     *
     * @param uri
     * @return
     */
    public static byte[] sendGetReturnByte(String uri) {
        byte[] output = null;
        HttpClient httpClient = new DefaultHttpClient();
        // 设置超时时间
        httpClient.getParams().setParameter(
                CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                10000);
        try {
            HttpGet httpGet = new HttpGet(uri);
            HttpResponse responseBody = httpClient.execute(httpGet);
            HttpEntity entry = responseBody.getEntity();
            InputStream input = entry.getContent();
            output = IOUtils.toByteArray(input);
            input.close();
        } catch (ClientProtocolException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return output;
    }

    /**
     * 发送POST请求
     *
     * @param uri
     * @param paramMap 请求参数
     * @return
     */
    public static String sendPost(String uri, Map<String, String> paramMap) {
        return sendPost(uri, paramMap, null);
    }

    /**
     * 发送POST请求
     *
     * @param uri
     * @param paramMap 请求参数
     * @param charset  参数编码
     * @return
     */
    public static String sendPost(String uri, Map<String, String> paramMap,
                                  String charset) {
        String responseBody = null;
        HttpClient httpClient = new DefaultHttpClient();
        // 设置超时时间
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
        try {
            HttpPost httpPost = new HttpPost(uri);
            logger.info("executing request " + httpPost.getURI());
            if (paramMap != null) {
                List<NameValuePair> nvps = new ArrayList<NameValuePair>(paramMap.size());
                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    NameValuePair nvp = new BasicNameValuePair(entry.getKey(), entry.getValue());
                    nvps.add(nvp);
                }
                if (charset != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(nvps, charset));
                } else {
                    httpPost.setEntity(new UrlEncodedFormEntity(nvps));
                }
            }
            // Create a response handler
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            responseBody = httpClient.execute(httpPost, responseHandler);
            logger.info("----------------------------------------");
            logger.info(responseBody);
            logger.info("----------------------------------------");
        } catch (ClientProtocolException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseBody;

    }

    /**
     * Extends the post data
     */
    public static String sendPostRequest(String _url, String data) {

        try {
            // Send the request
            URL url = new URL(_url);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("content-type", "application/json");
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

            // write parameters
            if (data != null) {
                writer.write(data);
            }
            writer.flush();

            // Get the response
            StringBuffer answer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                answer.append(line);
            }
            writer.close();
            reader.close();

            // Output the response
            return answer.toString();

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * 可以处理中文乱码，
     */
    public static String postData(String url, String data) {
        StringBuilder sb = new StringBuilder();
        HttpPost httpPost = new HttpPost(url);
        HttpEntity entity = null;

        httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
        try {

            HttpClient client = new DefaultHttpClient();
            StringEntity payload = new StringEntity(data, "UTF-8");
            httpPost.setEntity(payload);
            HttpResponse response = client.execute(httpPost);
            entity = response.getEntity();
            String text;
            if (entity != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
                while ((text = bufferedReader.readLine()) != null) {
                    sb.append(text);
                }

            }
        } catch (Exception e) {
            logger.error("与[" + url + "]通信过程中发生异常,堆栈信息如下", e.getCause());
        } finally {
            try {
                EntityUtils.consume(entity);
            } catch (IOException ex) {
                ex.printStackTrace();
                logger.error("net io exception");
            }
        }
        return sb.toString();
    }

}
