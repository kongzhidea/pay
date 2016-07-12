 package com.alipay.demo.trade.model.builder;
 
 import com.alipay.demo.trade.model.ExtendParams;
 import com.alipay.demo.trade.model.GoodsDetail;
 import com.google.gson.annotations.SerializedName;
 import java.util.List;
 import java.util.regex.Pattern;
 import org.apache.commons.lang.StringUtils;
 
 public class AlipayTradePayContentBuilder extends AlipayTradePrecreateContentBuilder
 {
   private String scene;
 
   @SerializedName("auth_code")
   private String authCode;
 
   public boolean validate()
   {
     if (StringUtils.isEmpty(this.scene)) {
       throw new NullPointerException("scene should not be NULL!");
     }
     if (StringUtils.isEmpty(this.authCode)) {
       throw new NullPointerException("auth_code should not be NULL!");
     }
     if (!Pattern.matches("^\\d{10,}$", this.authCode)) {
       throw new IllegalStateException("invalid auth_code!");
     }
     return super.validate();
   }
 
   public AlipayTradePayContentBuilder()
   {
     this.scene = "bar_code";
   }
 
   public String toString()
   {
     StringBuilder sb = new StringBuilder("AlipayTradePayContentBuilder{");
     sb.append("scene='").append(this.scene).append('\'');
     sb.append(", authCode='").append(this.authCode).append('\'');
     sb.append(", outTradeNo='").append(getOutTradeNo()).append('\'');
     sb.append(", sellerId='").append(getSellerId()).append('\'');
     sb.append(", totalAmount='").append(getTotalAmount()).append('\'');
     sb.append(", discountableAmount='").append(getDiscountableAmount()).append('\'');
     sb.append(", undiscountableAmount='").append(getUndiscountableAmount()).append('\'');
     sb.append(", subject='").append(getSubject()).append('\'');
     sb.append(", body='").append(getBody()).append('\'');
     sb.append(", goodsDetailList=").append(getGoodsDetailList());
     sb.append(", operatorId='").append(getOperatorId()).append('\'');
     sb.append(", storeId='").append(getStoreId()).append('\'');
     sb.append(", alipayStoreId='").append(getAlipayStoreId()).append('\'');
     sb.append(", terminalId='").append(getTerminalId()).append('\'');
     sb.append(", extendParams=").append(getExtendParams());
     if (StringUtils.isNotEmpty(getTimeExpress())) {
       sb.append(", timeExpire='").append(getTimeExpire()).append('\'');
     }
     sb.append(", timeExpress='").append(getTimeExpire()).append('\'');
     sb.append('}');
     return sb.toString();
   }
 
   public String getScene() {
     return this.scene;
   }
 
   public AlipayTradePayContentBuilder setScene(String scene) {
     this.scene = scene;
     return this;
   }
 
   public String getAuthCode() {
     return this.authCode;
   }
 
   public AlipayTradePayContentBuilder setAuthCode(String authCode) {
     this.authCode = authCode;
     return this;
   }
 
   public AlipayTradePayContentBuilder setAlipayStoreId(String alipayStoreId)
   {
     return (AlipayTradePayContentBuilder)super.setAlipayStoreId(alipayStoreId);
   }
 
   public AlipayTradePayContentBuilder setBody(String body)
   {
     return (AlipayTradePayContentBuilder)super.setBody(body);
   }
 
   public AlipayTradePayContentBuilder setDiscountableAmount(String discountableAmount)
   {
     return (AlipayTradePayContentBuilder)super.setDiscountableAmount(discountableAmount);
   }
 
   public AlipayTradePayContentBuilder setExtendParams(ExtendParams extendParams)
   {
     return (AlipayTradePayContentBuilder)super.setExtendParams(extendParams);
   }
 
   public AlipayTradePayContentBuilder setGoodsDetailList(List<GoodsDetail> goodsDetailList)
   {
     return (AlipayTradePayContentBuilder)super.setGoodsDetailList(goodsDetailList);
   }
 
   public AlipayTradePayContentBuilder setOperatorId(String operatorId)
   {
     return (AlipayTradePayContentBuilder)super.setOperatorId(operatorId);
   }
 
   public AlipayTradePayContentBuilder setOutTradeNo(String outTradeNo)
   {
     return (AlipayTradePayContentBuilder)super.setOutTradeNo(outTradeNo);
   }
 
   public AlipayTradePayContentBuilder setSellerId(String sellerId)
   {
     return (AlipayTradePayContentBuilder)super.setSellerId(sellerId);
   }
 
   public AlipayTradePayContentBuilder setStoreId(String storeId)
   {
     return (AlipayTradePayContentBuilder)super.setStoreId(storeId);
   }
 
   public AlipayTradePayContentBuilder setSubject(String subject)
   {
     return (AlipayTradePayContentBuilder)super.setSubject(subject);
   }
 
   public AlipayTradePayContentBuilder setTerminalId(String terminalId)
   {
     return (AlipayTradePayContentBuilder)super.setTerminalId(terminalId);
   }
 
   public AlipayTradePayContentBuilder setTimeExpire(String timeExpire)
   {
     return (AlipayTradePayContentBuilder)super.setTimeExpire(timeExpire);
   }
 
   public AlipayTradePayContentBuilder setTotalAmount(String totalAmount)
   {
     return (AlipayTradePayContentBuilder)super.setTotalAmount(totalAmount);
   }
 
   public AlipayTradePayContentBuilder setUndiscountableAmount(String undiscountableAmount)
   {
     return (AlipayTradePayContentBuilder)super.setUndiscountableAmount(undiscountableAmount);
   }
 
   public AlipayTradePayContentBuilder setTimeExpress(String timeExpress)
   {
     return (AlipayTradePayContentBuilder)super.setTimeExpress(timeExpress);
   }
 }
