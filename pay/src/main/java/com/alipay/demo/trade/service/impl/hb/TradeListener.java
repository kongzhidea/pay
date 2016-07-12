package com.alipay.demo.trade.service.impl.hb;

public abstract interface TradeListener
{
  public abstract void onPayTradeSuccess(String paramString, long paramLong);

  public abstract void onPayInProgress(String paramString, long paramLong);

  public abstract void onPayFailed(String paramString, long paramLong);

  public abstract void onConnectException(String paramString, long paramLong);

  public abstract void onSendException(String paramString, long paramLong);

  public abstract void onReceiveException(String paramString, long paramLong);
}
