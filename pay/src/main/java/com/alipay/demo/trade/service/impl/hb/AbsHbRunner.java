 package com.alipay.demo.trade.service.impl.hb;
 
 import com.alipay.api.response.MonitorHeartbeatSynResponse;
 import com.alipay.demo.trade.config.Configs;
 import com.alipay.demo.trade.model.builder.AlipayHeartbeatSynContentBuilder;
 import com.alipay.demo.trade.service.AlipayMonitorService;
 import java.util.concurrent.Executors;
 import java.util.concurrent.ScheduledExecutorService;
 import java.util.concurrent.TimeUnit;
 import org.apache.commons.lang.StringUtils;
 import org.apache.commons.logging.Log;
 import org.apache.commons.logging.LogFactory;
 
 public abstract class AbsHbRunner
   implements Runnable
 {
   protected Log log = LogFactory.getLog(getClass());
 
   private ScheduledExecutorService scheduledService = Executors.newSingleThreadScheduledExecutor();
   private AlipayMonitorService monitorService;
   private long delay = 0L;
   private long duration = 0L;
 
   public abstract AlipayHeartbeatSynContentBuilder getBuilder();
 
   public abstract String getAppAuthToken();
 
   public AbsHbRunner(AlipayMonitorService monitorService) { this.monitorService = monitorService; }
 
 
   public void run()
   {
     AlipayHeartbeatSynContentBuilder builder = getBuilder();
     String appAuthToken = getAppAuthToken();
 
     MonitorHeartbeatSynResponse response = this.monitorService.heartbeatSyn(builder, appAuthToken);
 
     StringBuilder sb = new StringBuilder(response.getCode())
       .append(":")
       .append(response.getMsg());
     if (StringUtils.isNotEmpty(response.getSubCode())) {
       sb.append(", ")
         .append(response.getSubCode())
         .append(":")
         .append(response.getSubMsg());
     }
     this.log.info(sb.toString());
   }
 
   public void schedule() {
     if (this.delay == 0L) {
       this.delay = Configs.getHeartbeatDelay();
     }
     if (this.duration == 0L) {
       this.duration = Configs.getCancelDuration();
     }
     this.scheduledService.scheduleAtFixedRate(this, this.delay, this.duration, TimeUnit.SECONDS);
   }
 
   public void shutdown() {
     this.scheduledService.shutdown();
   }
 
   public long getDelay() {
     return this.delay;
   }
 
   public void setDelay(long delay) {
     this.delay = delay;
   }
 
   public long getDuration() {
     return this.duration;
   }
 
   public void setDuration(long duration) {
     this.duration = duration;
   }
 }
