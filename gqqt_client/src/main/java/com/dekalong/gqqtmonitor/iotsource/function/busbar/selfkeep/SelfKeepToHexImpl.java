package com.dekalong.gqqtmonitor.iotsource.function.busbar.selfkeep;

import com.dekalong.gqqtmonitor.iotsource.function.busbar.ISelfKeepValveOpertion;
import com.dekalong.gqqtmonitor.iotsource.function.busbar.selfkeep.hexoper.IHexCmdOper;
import com.dekalong.gqqtmonitor.po.querymodel.BusbarQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SelfKeepToHexImpl
  implements ISelfKeepValveOpertion
{

  @Autowired
  private IHexCmdOper hexCmdOper;

  public boolean valveOper(BusbarQuery dpq, String direction, String openOrClose)
  {
    boolean isAll = false;
    String operID = "not";
    if (direction.equals("left")) {
      operID = String.valueOf(dpq.getDriValveIdLeft());
    } else if (direction.equals("right")) {
      operID = String.valueOf(dpq.getDriValveIdRight());
    } else if ((direction.equals("all")) && (openOrClose.equals("close"))) {
      isAll = true;
      operID = String.valueOf(dpq.getDriValveIdLeft());
    }
    if (operID.equals("not")) return false;
    int positivePoleID = Integer.valueOf(operID.substring(3, 5)).intValue();
    int negativePoleID = Integer.valueOf(operID.substring(5, 7)).intValue();
    int commPoleID = Integer.valueOf(operID.substring(7, 9)).intValue();

    int op = 99999;
    int cl = 99999;
    if (openOrClose.equals("open")) {
      op = positivePoleID;
      cl = negativePoleID;
    } else if (openOrClose.equals("close")) {
      op = negativePoleID;
      cl = positivePoleID;
    }
    if ((op == 99999) || (cl == 99999)) return false;
    boolean closeIsSuc = closePoleOper(dpq, cl);
    boolean openIsSuc = openPoleOper(dpq, op);

    boolean commIsSuc = commPoleOper(dpq, commPoleID);
    if ((!closeIsSuc) || (!openIsSuc) || (!commIsSuc)) {
      return false;
    }
    if (isAll) {
      return valveOper(dpq, "right", "close");
    }
    return true;
  }

  public boolean closePoleOper(BusbarQuery dpq, int cl)
  {
    boolean close = true;
    String openOrClose = "close";
    boolean isPulse = false;
    int relayModbusAddr = Integer.valueOf(String.valueOf(dpq.getDriValveIdLeft()).substring(1, 3)).intValue();
    close = this.hexCmdOper.transCtrlRelay(dpq.getDriIotAddr(), relayModbusAddr, cl, isPulse, openOrClose);
    if (!close) {
      close = this.hexCmdOper.transCtrlRelay(dpq.getDriIotAddr(), relayModbusAddr, cl, isPulse, openOrClose);
      if (!close) return false;
    }
    return true;
  }

  public boolean openPoleOper(BusbarQuery dpq, int op)
  {
    boolean open = true;
    String openOrClose = "open";
    boolean isPulse = false;
    int relayModbusAddr = Integer.valueOf(String.valueOf(dpq.getDriValveIdLeft()).substring(1, 3)).intValue();
    open = this.hexCmdOper.transCtrlRelay(dpq.getDriIotAddr(), relayModbusAddr, op, isPulse, openOrClose);
    if (!open) {
      open = this.hexCmdOper.transCtrlRelay(dpq.getDriIotAddr(), relayModbusAddr, op, isPulse, openOrClose);
      if (!open) return false;
    }
    return true;
  }

  public boolean commPoleOper(BusbarQuery dpq, int commPoleID)
  {
    String openOrClose = "instant";
    boolean isPulse = true;
    int relayModbusAddr = Integer.valueOf(String.valueOf(dpq.getDriValveIdLeft()).substring(1, 3)).intValue();
    boolean comm = this.hexCmdOper.transCtrlRelay(dpq.getDriIotAddr(), relayModbusAddr, commPoleID, isPulse, openOrClose);
    if (!comm) {
      comm = this.hexCmdOper.transCtrlRelay(dpq.getDriIotAddr(), relayModbusAddr, commPoleID, isPulse, openOrClose);
      if (!comm) return false;
    }
    return true;
  }
}