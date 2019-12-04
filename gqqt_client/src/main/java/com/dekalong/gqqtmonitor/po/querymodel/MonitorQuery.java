package com.dekalong.gqqtmonitor.po.querymodel;

import com.dekalong.gqqtmonitor.po.Monitor;

public class MonitorQuery extends Monitor
{
  private static final long serialVersionUID = 1L;

  public void setIotParamLeft(double iotParamLeft)
  {
    this.iotParamLeft = iotParamLeft;
    this.comparValue = getComparValue();
  }

  public void setIotParamRight(double iotParamRight) {
    this.iotParamRight = iotParamRight;
    this.comparValue = getComparValue();
  }

  public void setIotParamComm(double iotParamComm) {
    this.iotParamComm = iotParamComm;
    this.comparValue = getComparValue();
  }

  public int getComparValue() {
    double left = getIotParamLeft() / (getMonGasPressure() / 100.0D);
    left = left > 100.0D ? 100.0D : left;
    double right = getIotParamRight() / (getMonGasPressure() / 100.0D);
    right = left > 100.0D ? 100.0D : right;

    return new Long((int)Math.round(left + right)).intValue();
  }
}