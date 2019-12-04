package com.dekalong.gqqtmonitor.util.sortutil;

import com.dekalong.gqqtmonitor.po.DeviceModel;
import com.dekalong.gqqtmonitor.po.Monitor;
import com.dekalong.gqqtmonitor.po.querymodel.BusbarQuery;
import com.dekalong.gqqtmonitor.po.querymodel.MonitorQuery;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortUtil
{
  public static List<DeviceModel> sortList(List<DeviceModel> list)
  {
    if ((list == null) || (list.size() == 0)) { return new ArrayList<>();}
    double tankPressure = 0.0D;
    double cylinderPressure = 0.0D;
    boolean tankFirst = true;
    boolean cylinderFirst = true;
    for (DeviceModel dpq : list) {
      BusbarQuery busbar = null;
      MonitorQuery monitor = null;
      if ((dpq instanceof BusbarQuery))
        busbar = (BusbarQuery)dpq;
      else if ((dpq instanceof MonitorQuery)) {
        monitor = (MonitorQuery)dpq;
      }
      if (busbar != null) {
        if ((busbar.getDriContainerType().equals("tank")) && (tankFirst)) {
          tankFirst = false;
          tankPressure = busbar.getDriGasPressure();
        } else if ((busbar.getDriContainerType().equals("cylinder")) && (cylinderFirst)) {
          cylinderFirst = false;
          cylinderPressure = busbar.getDriGasPressure();
        }
      }
      if (monitor != null) {
        if ((monitor.getMonContainerType().equals("tank")) && (tankFirst)) {
          tankFirst = false;
          tankPressure = monitor.getMonGasPressure();
        } else if ((monitor.getMonContainerType().equals("cylinder")) && (cylinderFirst)) {
          cylinderFirst = false;
          cylinderPressure = monitor.getMonGasPressure();
        }
      }
    }
     return null;
//    return ascList(list, tankPressure, cylinderPressure);
  }
//  private static List<DeviceModel> ascList(List<DeviceModel> list, double tankPressure, double cylinderPressure) {
//    Collections.sort(list, new Comparator()
//    {
//      public int compare(DeviceModel dpq1, DeviceModel dpq2) {
//        double multiple = this.val$cylinderPressure / this.val$tankPressure;
//        double dpq1Value = 0.0D;
//        double dpq2Value = 0.0D;
//        BusbarQuery busbar1 = null;
//        MonitorQuery monitor1 = null;
//        BusbarQuery busbar2 = null;
//        MonitorQuery monitor2 = null;
//        if ((dpq1 instanceof BusbarQuery))
//          busbar1 = (BusbarQuery)dpq1;
//        else if ((dpq1 instanceof Monitor)) {
//          monitor1 = (MonitorQuery)dpq1;
//        }
//        if ((dpq2 instanceof BusbarQuery))
//          busbar2 = (BusbarQuery)dpq2;
//        else if ((dpq2 instanceof MonitorQuery)) {
//          monitor2 = (MonitorQuery)dpq2;
//        }
//        if (busbar1 != null) {
//          if (busbar1.getDriContainerType().equals("tank"))
//            dpq1Value = busbar1.getIotParamLeft() * multiple + busbar1.getIotParamRight() * multiple;
//          else
//            dpq1Value = busbar1.getIotParamLeft() + busbar1.getIotParamRight();
//        }
//        else if (monitor1 != null) {
//          if (monitor1.getMonContainerType().equals("tank"))
//            dpq1Value = monitor1.getIotParamLeft() * multiple + monitor1.getIotParamRight() * multiple;
//          else {
//            dpq1Value = monitor1.getIotParamLeft() + monitor1.getIotParamRight();
//          }
//        }
//
//        if (busbar2 != null) {
//          if (busbar2.getDriContainerType().equals("tank"))
//            dpq2Value = busbar2.getIotParamLeft() * multiple + busbar2.getIotParamRight() * multiple;
//          else
//            dpq2Value = busbar2.getIotParamLeft() + busbar2.getIotParamRight();
//        }
//        else if (monitor2 != null) {
//          if (monitor2.getMonContainerType().equals("tank"))
//            dpq2Value = monitor2.getIotParamLeft() * multiple + monitor2.getIotParamRight() * multiple;
//          else {
//            dpq2Value = monitor2.getIotParamLeft() + monitor2.getIotParamRight();
//          }
//        }
//        double diff = dpq1Value - dpq2Value;
//        if (diff > 0.0D)
//          return 1;
//        if (diff < 0.0D) {
//          return -1;
//        }
//        return 0;
//      }
//    });
//    Collections.sort(list, new Comparator()
//    {
//      public int compare(DeviceModel dpq1, DeviceModel dpq2)
//      {
//        int a = 0;
//        int b = 0;
//        a = dpq1.getOnLineStatus();
//        b = dpq2.getOnLineStatus();
//        if (((a == b) && (a == 0)) || ((a == b) && (a == 1))) return 0;
//        if (a == 1) {
//          return 1;
//        }
//        return -1;
//      }
//    });
//    return list;
//  }
}