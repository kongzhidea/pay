 package com.alipay.demo.trade.model.hb;
 
 import com.google.gson.annotations.SerializedName;
 
 public class SysTradeInfo
   implements TradeInfo
 {
 
   @SerializedName("OTN")
   private String outTradeNo;
 
   @SerializedName("TC")
   private double timeConsume;
 
   @SerializedName("STAT")
   private HbStatus status;
 
   public static SysTradeInfo newInstance(String outTradeNo, double timeConsume, HbStatus status)
   {
     SysTradeInfo info = new SysTradeInfo();
     info.setOutTradeNo(outTradeNo);
     if ((timeConsume > 99.0D) || (timeConsume < 0.0D)) {
       timeConsume = 99.0D;
     }
     info.setTimeConsume(timeConsume);
     info.setStatus(status);
     return info;
   }
 
   public String getOutTradeNo() {
     return this.outTradeNo;
   }
 
   public void setOutTradeNo(String outTradeNo) {
     this.outTradeNo = outTradeNo;
   }
 
   public HbStatus getStatus()
   {
     return this.status;
   }
 
   public void setStatus(HbStatus status) {
     this.status = status;
   }
 
   public double getTimeConsume()
   {
     return this.timeConsume;
   }
 
   public void setTimeConsume(double timeConsume) {
     this.timeConsume = timeConsume;
   }
 }
