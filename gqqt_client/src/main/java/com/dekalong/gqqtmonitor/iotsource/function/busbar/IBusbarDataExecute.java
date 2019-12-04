package com.dekalong.gqqtmonitor.iotsource.function.busbar;

import com.dekalong.gqqtmonitor.po.DeviceModel;

public abstract interface IBusbarDataExecute
{
  public abstract boolean executeOperation(DeviceModel paramDeviceModel, String paramString, boolean paramBoolean);
}