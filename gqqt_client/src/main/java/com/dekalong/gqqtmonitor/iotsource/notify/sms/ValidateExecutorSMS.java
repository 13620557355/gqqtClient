package com.dekalong.gqqtmonitor.iotsource.notify.sms;

import com.dekalong.gqqtmonitor.po.BaseModel;
import com.dekalong.gqqtmonitor.po.querymodel.BusbarQuery;
import com.dekalong.gqqtmonitor.po.querymodel.MonitorQuery;
import org.springframework.stereotype.Service;

@Service
public class ValidateExecutorSMS
  implements IValidateSMS
{



  public void validateCustomerSendSMS(BaseModel bm)
  {
    if ((bm instanceof BusbarQuery))
      busbarSMS((BusbarQuery)bm);
    else if ((bm instanceof MonitorQuery))
      monitorSMS((MonitorQuery)bm);
  }

  private void busbarSMS(BusbarQuery dpq)
  {
    boolean leftIsEmpty = false;
    boolean leftIsFull = false;
    boolean rightIsEmpty = false;
    boolean rightIsFull = false;
    if (dpq.getIotParamLeft() <= dpq.getDriAlarm()) {
      if (dpq.getLeftSMSEmptyNotifly() == 1) {
        leftIsEmpty = true;
        dpq.setLeftSMSEmptyNotifly(0);
        dpq.setLeftSMSFullNotifly(1);
      }
    }
    else if (dpq.getLeftSMSFullNotifly() == 1) {
      leftIsFull = true;
      dpq.setLeftSMSFullNotifly(0);
      dpq.setLeftSMSEmptyNotifly(1);
    }

    if (dpq.getIotParamRight() <= dpq.getDriAlarm()) {
      if (dpq.getRightSMSEmptyNotifly() == 1) {
        rightIsEmpty = true;
        dpq.setRightSMSEmptyNotifly(0);
        dpq.setRightSMSFullNotifly(1);
      }
    }
    else if (dpq.getRightSMSFullNotifly() == 1) {
      rightIsFull = true;
      dpq.setRightSMSFullNotifly(0);
      dpq.setRightSMSEmptyNotifly(1);
    }

    if ((leftIsEmpty) || (rightIsEmpty)) {
      String compleMessage = "【广气气体】尊敬的客户，贵单位的" + dpq.getDriType() + "低压报警,气体即将用完,如需帮助请拨打020-34001422.";

      sendMessageValidate(compleMessage, dpq);
    }
    if ((leftIsFull) || (rightIsFull)) {
      String compleMessage = "【广气气体】尊敬的客户，贵单位的" + dpq.getDriType() + "已经置换完毕,如需帮助请拨打020-34001422.";
      sendMessageValidate(compleMessage, dpq);
    }
  }

  private void monitorSMS(MonitorQuery dpq) {
    boolean leftIsEmpty = false;
    boolean leftIsFull = false;
    boolean rightIsEmpty = false;
    boolean rightIsFull = false;
    if (dpq.getIotParamLeft() <= dpq.getMonAlarm()) {
      if (dpq.getLeftSMSEmptyNotifly() == 1) {
        leftIsEmpty = true;
        dpq.setLeftSMSEmptyNotifly(0);
        dpq.setLeftSMSFullNotifly(1);
      }
    }
    else if (dpq.getLeftSMSFullNotifly() == 1) {
      leftIsFull = true;
      dpq.setLeftSMSFullNotifly(0);
      dpq.setLeftSMSEmptyNotifly(1);
    }

    if (dpq.getIotParamRight() <= dpq.getMonAlarm()) {
      if (dpq.getRightSMSEmptyNotifly() == 1) {
        rightIsEmpty = true;
        dpq.setRightSMSEmptyNotifly(0);
        dpq.setRightSMSFullNotifly(1);
      }
    }
    else if (dpq.getRightSMSFullNotifly() == 1) {
      rightIsFull = true;
      dpq.setRightSMSFullNotifly(0);
      dpq.setRightSMSEmptyNotifly(1);
    }

    if ((leftIsEmpty) || (rightIsEmpty)) {
      String compleMessage = "【广气气体】尊敬的客户，贵单位的" + dpq.getMonType() + "低压报警,气体即将用完,如需帮助请拨打020-34001422.";

      sendMessageValidate(compleMessage, dpq);
    }
    if ((leftIsFull) || (rightIsFull)) {
      String compleMessage = "【广气气体】尊敬的客户，贵单位的" + dpq.getMonType() + "已经置换完毕,如需帮助请拨打020-34001422.";

      sendMessageValidate(compleMessage, dpq);
    }
  }

  private void sendMessageValidate(String message, BaseModel bm) {
    String custPhone = "";
    if ((bm instanceof BusbarQuery)) {
      BusbarQuery bQuery = (BusbarQuery)bm;
      custPhone = bQuery.getCustUuid().getCustPhone();
    } else if ((bm instanceof MonitorQuery)) {
      MonitorQuery mQuery = (MonitorQuery)bm;
      custPhone = mQuery.getCustUuid().getCustPhone();
    }

     SendSMSUtil.sendMessage(message, custPhone);
  }

}