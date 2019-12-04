package com.dekalong.gqqtmonitor.iotsource.function.busbar;

import com.dekalong.gqqtmonitor.po.querymodel.BusbarQuery;

public abstract interface IValveExecuteOpertion
{
  public abstract boolean autoValveSwicth(BusbarQuery paramBusbarQuery);

  public abstract boolean onlyOneValveSwicth(BusbarQuery paramBusbarQuery);

  public abstract boolean manualExecuteValve(BusbarQuery paramBusbarQuery);
}