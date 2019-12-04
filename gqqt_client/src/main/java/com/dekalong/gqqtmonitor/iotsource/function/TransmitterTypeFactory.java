package com.dekalong.gqqtmonitor.iotsource.function;

import com.dekalong.gqqtmonitor.po.DeviceModel;
import com.dekalong.gqqtmonitor.po.querymodel.BusbarQuery;
import com.dekalong.gqqtmonitor.po.querymodel.MonitorQuery;
import java.math.BigDecimal;

public class TransmitterTypeFactory
{
  public static double getTransmitterValue(DeviceModel dm, double hum)
  {
    double range = 0.0D;
    double pressure = 0.0D;
    String name = dm.getTransmitterType();
    if ((dm instanceof BusbarQuery)) {
      BusbarQuery bQuery = (BusbarQuery)dm;
      range = bQuery.getDriRange();
      pressure = bQuery.getDriGasPressure();
    } else if ((dm instanceof MonitorQuery)) {
      MonitorQuery mQuery = (MonitorQuery)dm;
      range = mQuery.getMonRange();
      pressure = mQuery.getMonGasPressure();
    }
    if ((name != null) && (name.equals("xingyi"))) {
      return xingyi(range, pressure, hum);
    }
    return 0.0D;
  }

  private static double xingyi(double range, double pressure, double data)
  {
    double result = 0.0D;
    if (data == 0.0D) {
      return 0.0D;
    }
    double valve = range * data * 10.0D / 2000.0D;
    if (range <= 5.0D) {
      if (valve + valve * 0.1D >= range)
        result = pressure;
      else
        result = valve + valve * 0.1D;
    }
    else if (range >= 15.0D) {
      if (valve + valve * 0.04D >= range)
        result = pressure;
      else
        result = valve + valve * 0.04D;
    }
    else {
      result = valve;
    }
    return Intercept2Number(result);
  }
  private static double Intercept2Number(double parm) {
    BigDecimal b = new BigDecimal(parm);
    double f2 = b.setScale(1, 4).doubleValue();
    return f2;
  }

  public static void main(String[] args) {
    System.out.println(xingyi(20.0D, 13.0D, 130.0D));
  }
}