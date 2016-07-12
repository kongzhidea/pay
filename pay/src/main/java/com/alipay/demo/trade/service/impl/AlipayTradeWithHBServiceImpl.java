 package com.alipay.demo.trade.service.impl;
 
 import com.alipay.api.AlipayApiException;
 import com.alipay.api.AlipayClient;
 import com.alipay.api.DefaultAlipayClient;
 import com.alipay.api.request.AlipayTradePayRequest;
 import com.alipay.api.response.AlipayTradeCancelResponse;
 import com.alipay.api.response.AlipayTradePayResponse;
 import com.alipay.api.response.AlipayTradeQueryResponse;
 import com.alipay.demo.trade.config.Configs;
 import com.alipay.demo.trade.model.TradeStatus;
 import com.alipay.demo.trade.model.builder.AlipayTradePayContentBuilder;
 import com.alipay.demo.trade.model.result.AlipayF2FPayResult;
 import com.alipay.demo.trade.service.impl.hb.HbListener;
 import com.alipay.demo.trade.service.impl.hb.TradeListener;
 import java.net.ConnectException;
 import java.net.NoRouteToHostException;
 import java.net.SocketException;
 import java.net.SocketTimeoutException;
 import java.util.concurrent.ExecutorService;
 import org.apache.commons.lang.StringUtils;
 import org.apache.commons.logging.Log;
 
 public class AlipayTradeWithHBServiceImpl extends AbsAlipayTradeService
 {
   private TradeListener listener;
 
   public AlipayTradeWithHBServiceImpl(ClientBuilder builder)
   {
     if (StringUtils.isEmpty(builder.getGatewayUrl())) {
       throw new NullPointerException("gatewayUrl should not be NULL!");
     }
     if (StringUtils.isEmpty(builder.getAppid())) {
       throw new NullPointerException("appid should not be NULL!");
     }
     if (StringUtils.isEmpty(builder.getPrivateKey())) {
       throw new NullPointerException("privateKey should not be NULL!");
     }
     if (StringUtils.isEmpty(builder.getFormat())) {
       throw new NullPointerException("format should not be NULL!");
     }
     if (StringUtils.isEmpty(builder.getCharset())) {
       throw new NullPointerException("charset should not be NULL!");
     }
     if (StringUtils.isEmpty(builder.getAlipayPublicKey())) {
       throw new NullPointerException("alipayPublicKey should not be NULL!");
     }
     if (builder.getListener() == null) {
       throw new NullPointerException("listener should not be NULL!");
     }
 
     this.listener = builder.getListener();
     this.client = new DefaultAlipayClient(builder.getGatewayUrl(), builder.getAppid(), builder.getPrivateKey(), 
       builder.getFormat(), builder.getCharset(), builder.getAlipayPublicKey());
   }
 
   private AlipayTradePayResponse getResponse(AlipayClient client, AlipayTradePayRequest request, final String outTradeNo, final long beforeCall)
   {
     try {
       AlipayTradePayResponse response = (AlipayTradePayResponse)client.execute(request);
       if (response != null) {
         this.log.info(response.getBody());
       }
       return response;
     }
     catch (AlipayApiException e)
     {
       Throwable cause = e.getCause();
 
       if (((cause instanceof ConnectException)) || 
         ((cause instanceof NoRouteToHostException)))
       {
         executorService.submit(new Runnable()
         {
           public void run() {
             AlipayTradeWithHBServiceImpl.this.listener.onConnectException(outTradeNo, beforeCall);
           }
         });
       }
       else if ((cause instanceof SocketException))
       {
         executorService.submit(new Runnable()
         {
           public void run() {
             AlipayTradeWithHBServiceImpl.this.listener.onSendException(outTradeNo, beforeCall);
           }
         });
       }
       else if ((cause instanceof SocketTimeoutException))
       {
         executorService.submit(new Runnable()
         {
           public void run() {
             AlipayTradeWithHBServiceImpl.this.listener.onReceiveException(outTradeNo, beforeCall);
           }
         });
       }
 
       e.printStackTrace();
     }return null;
   }
 
   public AlipayF2FPayResult tradePay(AlipayTradePayContentBuilder builder)
   {
     validateBuilder(builder);
 
     final String outTradeNo = builder.getOutTradeNo();
 
     AlipayTradePayRequest request = new AlipayTradePayRequest();
     request.setBizContent(builder.toJsonString());
     this.log.info("trade.pay bizContent:" + request.getBizContent());
 
     final long beforeCall = System.currentTimeMillis();
     AlipayTradePayResponse response = getResponse(this.client, request, outTradeNo, beforeCall);
 
     AlipayF2FPayResult result = new AlipayF2FPayResult(response);
     if ((response != null) && ("10000".equals(response.getCode())))
     {
       result.setTradeStatus(TradeStatus.SUCCESS);
 
       executorService.submit(new Runnable()
       {
         public void run() {
           AlipayTradeWithHBServiceImpl.this.listener.onPayTradeSuccess(outTradeNo, beforeCall);
         } } );
     }
     else {
       if ((response != null) && ("10003".equals(response.getCode())))
       {
         executorService.submit(new Runnable()
         {
           public void run() {
             AlipayTradeWithHBServiceImpl.this.listener.onPayInProgress(outTradeNo, beforeCall);
           }
         });
         AlipayTradeQueryResponse loopQueryResponse = loopQueryResult(outTradeNo);
         return checkQueryAndCancel(outTradeNo, result, loopQueryResponse, beforeCall);
       }
       if (tradeError(response))
       {
         AlipayTradeQueryResponse queryResponse = tradeQuery(outTradeNo);
         return checkQueryAndCancel(outTradeNo, result, queryResponse, beforeCall);
       }
 
       result.setTradeStatus(TradeStatus.FAILED);
 
       executorService.submit(new Runnable()
       {
         public void run() {
           AlipayTradeWithHBServiceImpl.this.listener.onPayFailed(outTradeNo, beforeCall);
         }
       });
     }
 
     return result;
   }
 
   private AlipayF2FPayResult checkQueryAndCancel(final String outTradeNo, AlipayF2FPayResult result, AlipayTradeQueryResponse queryResponse, final long beforeCall)
   {
     if (querySuccess(queryResponse))
     {
       result.setTradeStatus(TradeStatus.SUCCESS);
       result.setResponse(toPayResponse(queryResponse));
 
       executorService.submit(new Runnable() {
         public void run() {
           AlipayTradeWithHBServiceImpl.this.listener.onPayTradeSuccess(outTradeNo, beforeCall);
         }
       });
       return result;
     }
 
     executorService.submit(new Runnable()
     {
       public void run() {
         AlipayTradeWithHBServiceImpl.this.listener.onPayFailed(outTradeNo, beforeCall);
       }
     });
     AlipayTradeCancelResponse cancelResponse = cancelPayResult(outTradeNo);
     if (tradeError(cancelResponse))
     {
       result.setTradeStatus(TradeStatus.UNKNOWN);
     }
     else
     {
       result.setTradeStatus(TradeStatus.FAILED);
     }
 
     return result;
   }
 
   public static class ClientBuilder
   {
     private String gatewayUrl;
     private String appid;
     private String privateKey;
     private String format;
     private String charset;
     private String alipayPublicKey;
     private TradeListener listener;
 
     public AlipayTradeWithHBServiceImpl build()
     {
       if (StringUtils.isEmpty(this.gatewayUrl)) {
         this.gatewayUrl = Configs.getOpenApiDomain();
       }
       if (StringUtils.isEmpty(this.appid)) {
         this.appid = Configs.getAppid();
       }
       if (StringUtils.isEmpty(this.privateKey)) {
         this.privateKey = Configs.getPrivateKey();
       }
       if (StringUtils.isEmpty(this.format)) {
         this.format = "json";
       }
       if (StringUtils.isEmpty(this.charset)) {
         this.charset = "utf-8";
       }
       if (StringUtils.isEmpty(this.alipayPublicKey)) {
         this.alipayPublicKey = Configs.getAlipayPublicKey();
       }
       if (this.listener == null) {
         this.listener = new HbListener();
       }
 
       return new AlipayTradeWithHBServiceImpl(this);
     }
 
     public TradeListener getListener() {
       return this.listener;
     }
 
     public ClientBuilder setListener(TradeListener listener) {
       this.listener = listener;
       return this;
     }
 
     public ClientBuilder setAlipayPublicKey(String alipayPublicKey) {
       this.alipayPublicKey = alipayPublicKey;
       return this;
     }
 
     public ClientBuilder setAppid(String appid) {
       this.appid = appid;
       return this;
     }
 
     public ClientBuilder setCharset(String charset) {
       this.charset = charset;
       return this;
     }
 
     public ClientBuilder setFormat(String format) {
       this.format = format;
       return this;
     }
 
     public ClientBuilder setGatewayUrl(String gatewayUrl) {
       this.gatewayUrl = gatewayUrl;
       return this;
     }
 
     public ClientBuilder setPrivateKey(String privateKey) {
       this.privateKey = privateKey;
       return this;
     }
 
     public String getAlipayPublicKey() {
       return this.alipayPublicKey;
     }
 
     public String getAppid() {
       return this.appid;
     }
 
     public String getCharset() {
       return this.charset;
     }
 
     public String getFormat() {
       return this.format;
     }
 
     public String getGatewayUrl() {
       return this.gatewayUrl;
     }
 
     public String getPrivateKey() {
       return this.privateKey;
     }
   }
 }
