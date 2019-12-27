package com.dekalong.gqqtmonitor.iotsource.function.busbar.unselfkeep;

import com.dekalong.gqqtmonitor.iotsource.function.busbar.IBusbarDataExecute;
import com.dekalong.gqqtmonitor.iotsource.function.busbar.IValveExecuteOpertion;
import com.dekalong.gqqtmonitor.po.DeviceModel;
import com.dekalong.gqqtmonitor.po.querymodel.BusbarQuery;
import com.dekalong.gqqtmonitor.util.regexutil.StrRegex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class BusbarOrdinaryImpl
  implements IBusbarDataExecute
{

  @Autowired
  @Qualifier("valveOrdenaryImpl")
  private IValveExecuteOpertion valveOrdinary;

  public boolean executeOperation(DeviceModel dm, String relayStatus, boolean isSuccess)
  {
    BusbarQuery dpq = null;
    if ((dm instanceof BusbarQuery)) dpq = (BusbarQuery)dm;
    int leftStatus = getRealTimeRelayStatus(dpq.getDriValveIdLeft(), relayStatus);
    int rightStatus = getRealTimeRelayStatus(dpq.getDriValveIdRight(), relayStatus);
    if ((leftStatus == 1) || (rightStatus == 1)) {
      if ((leftStatus == 1) && (dpq.getIotParamLeft() <= dpq.getDriAlarm())) {
        isSuccess = true;
      }
      if ((rightStatus == 1) && (dpq.getIotParamRight() <= dpq.getDriAlarm())) {
        isSuccess = true;
      }
    }
    dpq.setDriValveLeft(leftStatus);
    dpq.setDriValveRight(rightStatus);
    updateValveStatus(dpq);
    return isSuccess;
  }

  private void updateValveStatus(BusbarQuery dpq)
  {
    if (dpq.getDriAuto() == 1)
      if (dpq.getDriValveIdRight() != 999) {
        this.valveOrdinary.autoValveSwicth(dpq);
      }
      else {
    	  this.valveOrdinary.onlyOneValveSwicth(dpq); 
      }
  }

  private int getRealTimeRelayStatus(int position, String data)
  {
    int relayRealTimeStatus = StrRegex.getRelayPosition(data, position);
    return relayRealTimeStatus;
  }
}