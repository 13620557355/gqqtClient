package com.dekalong.gqqtmonitor.iotsource.function.busbar;

import com.dekalong.gqqtmonitor.po.querymodel.BusbarQuery;

public abstract interface ISelfKeepValveOpertion
{
  public abstract boolean commPoleOper(BusbarQuery paramBusbarQuery, int paramInt);

  public abstract boolean openPoleOper(BusbarQuery paramBusbarQuery, int paramInt);

  public abstract boolean closePoleOper(BusbarQuery paramBusbarQuery, int paramInt);

  public abstract boolean valveOper(BusbarQuery paramBusbarQuery, String paramString1, String paramString2);
}