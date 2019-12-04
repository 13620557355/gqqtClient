package com.dekalong.gqqtmonitor.iotsource.function.busbar;

import com.dekalong.gqqtmonitor.po.querymodel.BusbarQuery;

public class BusbarUtil
{
  public static void leftBackflowValidate(double pressureValue, BusbarQuery dpq)
  {
    boolean isBackflow = false;
    if ((dpq.isLeftBackflowSwicth()) && 
      (pressureValue - dpq.getDriAlarm() <= 0.2D)) {
      isBackflow = true;
    }

    if ((pressureValue <= dpq.getDriAlarm()) || (isBackflow)) {
      dpq.setLeftBackflowSwicth(true);
      if (pressureValue < dpq.getDriAlarm())
        dpq.setIotParamLeft(pressureValue);
      else
        dpq.setIotParamLeft(dpq.getDriAlarm());
    }
    else {
      dpq.setLeftBackflowSwicth(false);
      dpq.setIotParamLeft(pressureValue);
    }
  }

  public static void rightBackflowValidate(double pressureValue, BusbarQuery dpq) { boolean isBackflow = false;
    if ((dpq.isRightBackflowSwicth()) && 
      (pressureValue - dpq.getDriAlarm() <= 0.2D)) {
      isBackflow = true;
    }

    if ((pressureValue <= dpq.getDriAlarm()) || (isBackflow)) {
      dpq.setRightBackflowSwicth(true);
      if (pressureValue < dpq.getDriAlarm())
        dpq.setIotParamRight(pressureValue);
      else
        dpq.setIotParamRight(dpq.getDriAlarm());
    }
    else {
      dpq.setRightBackflowSwicth(true);
      dpq.setIotParamRight(pressureValue);
    } }

  public static void commBackflowValidate(double pressureValue, BusbarQuery dpq) {
    boolean isBackflow = false;
    if ((dpq.isCommBackflowSwicth()) && 
      (pressureValue - dpq.getDriAlarm() <= 0.2D)) {
      isBackflow = true;
    }

    if ((pressureValue <= dpq.getDriAlarm()) || (isBackflow)) {
      dpq.setCommBackflowSwicth(true);
      if (pressureValue < dpq.getDriAlarm())
        dpq.setIotParamComm(pressureValue);
      else
        dpq.setIotParamComm(dpq.getDriAlarm());
    }
    else {
      dpq.setCommBackflowSwicth(false);
      dpq.setIotParamComm(pressureValue);
    }
  }
}