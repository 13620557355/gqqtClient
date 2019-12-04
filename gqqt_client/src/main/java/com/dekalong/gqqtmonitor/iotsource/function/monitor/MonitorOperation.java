package com.dekalong.gqqtmonitor.iotsource.function.monitor;

import com.dekalong.gqqtmonitor.iotsource.function.TransmitterTypeFactory;
import com.dekalong.gqqtmonitor.iotsource.notify.sms.IValidateSMS;
import com.dekalong.gqqtmonitor.po.querymodel.MonitorQuery;
import com.dekalong.gqqtmonitor.websocket.WebSocketDataHandler;
import com.dekalong.networtdevice.po.DeviceNodeData;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonitorOperation
  implements IMonitorDataExecute
{

  @Autowired
  private IValidateSMS validateSMS;

  @Autowired
  private WebSocketDataHandler notifyWebSocket;

  public void executeOperation(MonitorQuery mq, List<DeviceNodeData> nodeList)
  {
    boolean nonDuplicate = false;
    int monIdLeft = mq.getMonIdLeft();
    int monIdRight = mq.getMonIdRight();
    int monIdComm = mq.getMonIdComm();
    for (DeviceNodeData nd : nodeList) {
      int nodeId = nd.getNodeId();
      double pressureValue = TransmitterTypeFactory.getTransmitterValue(mq, nd.getHum());
      if (monIdLeft == nodeId) {
        if (mq.getOnLineStatus() == 0) nonDuplicate = true;
        mq.setOnLineStatus(1);
        mq.setLastTime(System.currentTimeMillis());
        if (mq.getIotParamLeft() != pressureValue) nonDuplicate = true;
        leftBackflowValidate(pressureValue, mq);
      } else if (monIdRight == nodeId) {
        if (mq.getIotParamRight() != pressureValue) nonDuplicate = true;
        rightBackflowValidate(pressureValue, mq);
      } else if (monIdComm == nodeId) {
        if (mq.getIotParamComm() != pressureValue) nonDuplicate = true;
        commBackflowValidate(pressureValue, mq);
      }
    }
    if (nonDuplicate) {
      nonDuplicate = false;

      this.notifyWebSocket.sendNotifyDevice(mq.getUuid());
    }
  }

  private void leftBackflowValidate(double pressureValue, MonitorQuery dpq) { boolean isBackflow = false;
    if ((dpq.isLeftBackflowSwicth()) && 
      (pressureValue - dpq.getMonAlarm() <= 0.2D)) {
      isBackflow = true;
    }

    if ((pressureValue <= dpq.getMonAlarm()) || (isBackflow)) {
      dpq.setLeftBackflowSwicth(true);
      if (pressureValue < dpq.getMonAlarm())
        dpq.setIotParamLeft(pressureValue);
      else
        dpq.setIotParamLeft(dpq.getMonAlarm());
    }
    else {
      dpq.setLeftBackflowSwicth(false);
      dpq.setIotParamLeft(pressureValue);
    } }

  private void rightBackflowValidate(double pressureValue, MonitorQuery dpq) {
    boolean isBackflow = false;
    if ((dpq.isRightBackflowSwicth()) && 
      (pressureValue - dpq.getMonAlarm() <= 0.2D)) {
      isBackflow = true;
    }

    if ((pressureValue <= dpq.getMonAlarm()) || (isBackflow)) {
      dpq.setRightBackflowSwicth(true);
      if (pressureValue < dpq.getMonAlarm())
        dpq.setIotParamRight(pressureValue);
      else
        dpq.setIotParamRight(dpq.getMonAlarm());
    }
    else {
      dpq.setRightBackflowSwicth(true);
      dpq.setIotParamRight(pressureValue);
    }
  }

  private void commBackflowValidate(double pressureValue, MonitorQuery dpq) { boolean isBackflow = false;
    if ((dpq.isCommBackflowSwicth()) && 
      (pressureValue - dpq.getMonAlarm() <= 0.2D)) {
      isBackflow = true;
    }

    if ((pressureValue <= dpq.getMonAlarm()) || (isBackflow)) {
      dpq.setCommBackflowSwicth(true);
      if (pressureValue < dpq.getMonAlarm())
        dpq.setIotParamComm(pressureValue);
      else
        dpq.setIotParamComm(dpq.getMonAlarm());
    }
    else {
      dpq.setCommBackflowSwicth(false);
      dpq.setIotParamComm(pressureValue);
    }
  }
}