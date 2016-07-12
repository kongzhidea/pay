 package com.alipay.demo.trade.service.impl.hb;
 
 import com.alipay.demo.trade.model.hb.SysTradeInfo;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.concurrent.ArrayBlockingQueue;
 import java.util.concurrent.BlockingQueue;
 import org.apache.commons.logging.Log;
 import org.apache.commons.logging.LogFactory;
 
 public class HbQueue
 {
   private static Log log = LogFactory.getLog(HbQueue.class);
   public static final int QUEUE_SIZE = 300;
   private static final BlockingQueue<SysTradeInfo> queue = new ArrayBlockingQueue(300);
 
   public static synchronized void offer(SysTradeInfo info)
   {
     if (info != null)
       try
       {
         queue.put(info);
       } catch (InterruptedException e) {
         log.warn("interrupted for tradeInfo:" + info);
         e.printStackTrace();
       }
   }
 
   public static synchronized List<SysTradeInfo> poll()
   {
     if (queue.isEmpty())
     {
       return null;
     }
 
     int size = 30;
     List tradeInfoList = new ArrayList(size);
     for (int i = 0; i < size; i++)
     {
       SysTradeInfo info = (SysTradeInfo)queue.poll();
       if (info == null) {
         break;
       }
       tradeInfoList.add(info);
     }
     return tradeInfoList;
   }
 }
