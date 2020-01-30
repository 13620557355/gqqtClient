package com.dekalong.gqqtmonitor.iotsource.function.busbar.unselfkeep;

import com.dekalong.gqqtmonitor.initmodel.InitAppModel;
import com.dekalong.gqqtmonitor.iotsource.function.busbar.IValveExecuteOpertion;
import com.dekalong.gqqtmonitor.po.querymodel.BusbarQuery;
import com.dekalong.gqqtmonitor.po.querymodel.CustomerQuery;
import com.dekalong.gqqtmonitor.websocket.WebSocketDataHandler;
import com.dekalong.gqqtmonitor.websocket.WebSocketSendDataTimer;
import com.dekalong.networtdevice.datadown.DataDownExecutor;
import com.dekalong.networtdevice.po.DeviceTelecontrolAck;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValveOrdenaryImpl implements IValveExecuteOpertion {

  @Autowired
  private WebSocketDataHandler notifyWebSocket;


  private static Map<Map<Long, String>, Map<Integer, Integer>> telecontrolAckMap = new ConcurrentHashMap<>();

  private static ConcurrentLinkedQueue<long[]> resultList = new ConcurrentLinkedQueue<>();
  public static final int TIMEOUT = 5000;

  public boolean manualExecuteValve(final BusbarQuery dpq)
  {
    String duplicateRequestID = dpq.getDriIotAddr() + "|" + dpq.getDriIdLeft();
    InitAppModel.duplicateRequest.add(duplicateRequestID);
    Future<Boolean> result = InitAppModel.threadPool.submit(new Callable<Boolean>()
    {
      public Boolean call() throws Exception {
        return Boolean.valueOf(ValveOrdenaryImpl.this.manualExecute(dpq));
      }
    });
    try {
      return ((Boolean)result.get()).booleanValue();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } finally {
      InitAppModel.duplicateRequest.remove(duplicateRequestID);
    }
  }

  public boolean manualExecute(BusbarQuery dpq) {
    BusbarQuery pipeline = null;
    for (CustomerQuery cq : InitAppModel.customerList) {
      if (dpq.getCustUuid().getUuid().equals(cq.getUuid())) {
        for (BusbarQuery driverPipelineQuery : cq.getBusbarList()) {
          if (driverPipelineQuery.getUuid().equals(dpq.getUuid())) {
            pipeline = driverPipelineQuery;
          }
        }
      }
    }

    if (dpq.getDriValveIdRight() != 999) {
      if (pipeline != null) {
        int oldLeftValve = pipeline.getDriValveLeft();
        int oldRightValve = pipeline.getDriValveRight();
        int newLeftValve = dpq.getDriValveLeft();
        int newRightValve = dpq.getDriValveRight();
        if ((oldLeftValve != newLeftValve) || (oldRightValve != newRightValve)) {
          if ((newLeftValve == 1) || (newRightValve == 1)) {
            boolean leftIsSucc = true;
            boolean rightIsSucc = true;
            if (oldLeftValve != newLeftValve) {
              if (newLeftValve == 0) {
                leftIsSucc = colseValve(pipeline, "left");
                return leftIsSucc;
              }
              leftIsSucc = openValve(pipeline, "left");
              if (oldRightValve == 1) {
                rightIsSucc = colseValve(pipeline, "right");
              }
              return (leftIsSucc) && (rightIsSucc);
            }

            if (oldRightValve != newRightValve) {
              if (newRightValve == 0) {
                rightIsSucc = colseValve(pipeline, "right");
                return rightIsSucc;
              }
              rightIsSucc = openValve(pipeline, "right");
              if (oldLeftValve == 1) {
                leftIsSucc = colseValve(pipeline, "left");
              }
              return (leftIsSucc) && (rightIsSucc);
            }
          }
          else {
            boolean leftIsSucc = true;
            boolean rightIsSucc = true;
            if (oldLeftValve == 1) {
              leftIsSucc = colseValve(pipeline, "left");
            }
            if (oldRightValve == 1) {
              rightIsSucc = colseValve(pipeline, "right");
            }
            return (leftIsSucc) && (rightIsSucc);
          }
        }
      }
      return false;
    }
    boolean leftIsSucc = true;
    if (dpq.getDriValveLeft() == 0)
      leftIsSucc = colseValve(pipeline, "left");
    else if (dpq.getDriValveLeft() == 1) {
      leftIsSucc = openValve(pipeline, "left");
    }
    return leftIsSucc;
  }

  private boolean teleCloseValve(int custIotAddr, int relayID)
  {
    try
    {
      Thread.sleep(100L);
      boolean isSuccess = true;
      isSuccess = DataDownExecutor.telecontrol(custIotAddr, relayID, 1, 0);
      Map<Integer,Integer> map = new HashMap<>();
      Map<Long,String> timerAndOperType = new HashMap<>();
      map.put(Integer.valueOf(custIotAddr), Integer.valueOf(relayID));
      long currenTime = System.currentTimeMillis();
      timerAndOperType.put(Long.valueOf(currenTime), "close");
      telecontrolAckMap.put(timerAndOperType, map);
      return isSuccess;
    } catch (Exception e) {
      e.printStackTrace();
    }return false;
  }

  private boolean teleOpenValve(int custIotAddr, int relayID)
  {
    try
    {
      Thread.sleep(100L);
      boolean isSuccess = true;
      isSuccess = DataDownExecutor.telecontrol(custIotAddr, relayID, 0, 0);
      Map<Integer,Integer> map = new HashMap<>();
      Map<Long,String> timerAndOperType = new HashMap<>();
      map.put(Integer.valueOf(custIotAddr), Integer.valueOf(relayID));
      long currenTime = System.currentTimeMillis();
      timerAndOperType.put(Long.valueOf(currenTime), "open");
      telecontrolAckMap.put(timerAndOperType, map);
      return isSuccess;
    } catch (Exception e) {
      e.printStackTrace();
    }return false;
  }

  public boolean autoValveSwicth(BusbarQuery dpq)
  {
    double driAlerm = dpq.getDriAlarm();
    double leftPar = dpq.getIotParamLeft();
    double rightPar = dpq.getIotParamRight();

    int valveLeft = dpq.getDriValveLeft();
    int valveRight = dpq.getDriValveRight();

    if ((valveLeft == 0) && (valveRight == 0)) {
      if ((leftPar > driAlerm) && (rightPar > driAlerm)) {
        return openValve(dpq, "left");
      }
      if ((leftPar <= driAlerm) && (rightPar > driAlerm)) {
        return openValve(dpq, "right");
      }
      if ((leftPar > driAlerm) && (rightPar <= driAlerm)) {
        return openValve(dpq, "left");
      }
      return false;
    }

    if ((valveLeft == 1) && (valveRight == 1)) {
      if ((leftPar <= driAlerm) && (rightPar <= driAlerm)) {
        colseValve(dpq, "all");
        return false;
      }
      if ((leftPar > driAlerm) && (rightPar > driAlerm)) {
        colseValve(dpq, "left");
        return false;
      }
      if ((leftPar <= driAlerm) && (rightPar > driAlerm)) {
        colseValve(dpq, "left");
        return false;
      }
      if ((leftPar > driAlerm) && (rightPar <= driAlerm)) {
        colseValve(dpq, "right");
        return false;
      }
      return false;
    }

    if (((valveLeft == 1) && (valveRight == 0)) || ((valveLeft == 0) && (valveRight == 1))) {
      if ((valveLeft == 1) && (leftPar <= driAlerm)) {
        boolean isSuccessSwicth = true;
        boolean isColse = true;
        boolean isOpen = true;
        if (rightPar > driAlerm) {
          isOpen = openValve(dpq, "right");
        }
        if (isOpen) {
          isColse = colseValve(dpq, "left");
        }
        if ((!isColse) || (!isOpen)) {
          isSuccessSwicth = false;
        }
        return isSuccessSwicth;
      }if ((valveRight == 1) && (rightPar <= driAlerm)) {
        boolean isSuccessSwicth = true;
        boolean isColse = true;
        boolean isOpen = true;
        if (leftPar > driAlerm) {
          isOpen = openValve(dpq, "left");
        }
        if (isOpen) {
          isColse = colseValve(dpq, "right");
        }

        if ((!isColse) || (!isOpen)) {
          isSuccessSwicth = false;
        }
        return isSuccessSwicth;
      }
    }
    return false;
  }

  public boolean onlyOneValveSwicth(BusbarQuery dpq)
  {
    double alarm = dpq.getDriAlarm();
    double leftparm = dpq.getIotParamLeft();
    int leftValveStatus = dpq.getDriValveLeft();
    if ((leftValveStatus == 1) && (leftparm <= alarm)) {
      boolean isSuccess = colseValve(dpq, "left");
      return isSuccess;
    }
    if ((leftValveStatus == 0) && (leftparm > alarm)) {
      boolean isSuccess = openValve(dpq, "left");
      return isSuccess;
    }
    return false;
  }

  private boolean colseValve(BusbarQuery dpq, String name)
  {
    boolean isAuto = true;
    if (dpq.getDriAuto() == 1)
      dpq.setDriAuto(0);
    else
      isAuto = false;
    try
    {
      boolean isSuccess = true;
      if (name.equals("all")) {
        boolean isCloseLeft = teleCloseValve(dpq.getDriIotAddr(), dpq.getDriValveIdLeft());
        boolean closeIsSuc = validateResult(dpq, System.currentTimeMillis());
        if ((isCloseLeft) && (closeIsSuc)) {
          dpq.setDriValveLeft(0);
        }
        else if (isCloseLeft) {
          isSuccess = false;
        } else {
          isSuccess = false;
        }

        boolean isCloseRight = teleCloseValve(dpq.getDriIotAddr(), dpq.getDriValveIdRight());
        validateResult(dpq, System.currentTimeMillis());
        if ((isCloseRight) && (closeIsSuc)) {
          dpq.setDriValveRight(0);
        }
        else if (isCloseRight) {
          isSuccess = false;
        } else {
          isSuccess = false;
        }

      }
      else if (name.equals("left")) {
        boolean isCloseLeft = teleCloseValve(dpq.getDriIotAddr(), dpq.getDriValveIdLeft());
        boolean closeIsSuc = validateResult(dpq, System.currentTimeMillis());
        if ((isCloseLeft) && (closeIsSuc)) {
          dpq.setDriValveLeft(0);
        }
        else if (isCloseLeft) {
          isSuccess = false;
        } else {
          isSuccess = false;
        }
      }
      else if (name.equals("right")) {
        boolean isCloseRight = teleCloseValve(dpq.getDriIotAddr(), dpq.getDriValveIdRight());
        boolean closeIsSuc = validateResult(dpq, System.currentTimeMillis());
        if ((isCloseRight) && (closeIsSuc)) {
          dpq.setDriValveRight(0);
        }
        else if (isCloseRight) {
          isSuccess = false;
        } else {
          isSuccess = false;
        }

      }

      this.notifyWebSocket.sendNotifyDevice(dpq.getUuid());
      WebSocketSendDataTimer.isUpdateData=true;
      if (isAuto) dpq.setDriAuto(1);
      return isSuccess;
    } catch (Exception e) {
      e.printStackTrace();
      this.notifyWebSocket.sendNotifyDevice(dpq.getUuid());
      WebSocketSendDataTimer.isUpdateData=true;
      if (isAuto) dpq.setDriAuto(1); 
    }
    return false;
  }

  private boolean openValve(BusbarQuery dpq, String name)
  {
    boolean isAuto = true;
    if (dpq.getDriAuto() == 1)
      dpq.setDriAuto(0);
    else
      isAuto = false;
    try
    {
      boolean isSuccess = true;
      if (name.equals("left")) {
        boolean isOpenLeft = teleOpenValve(dpq.getDriIotAddr(), dpq.getDriValveIdLeft());
        boolean openIsSuc = validateResult(dpq, System.currentTimeMillis());
        Thread.sleep(1000L);
        if ((isOpenLeft) && (openIsSuc)) {
          dpq.setDriValveLeft(1);
        }
        else if (isOpenLeft) {
          isSuccess = false;
        } else {
          isSuccess = false;
        }

      }
      else if (name.equals("right")) {
        boolean isOpenRight = teleOpenValve(dpq.getDriIotAddr(), dpq.getDriValveIdRight());
        boolean openIsSuc = validateResult(dpq, System.currentTimeMillis());
        Thread.sleep(1000L);
        if ((isOpenRight) && (openIsSuc)) {
          dpq.setDriValveRight(1);
          this.notifyWebSocket.sendNotifyDevice(dpq.getUuid());
          WebSocketSendDataTimer.isUpdateData=true;
        }
        else if (isOpenRight) {
          isSuccess = false;
        } else {
          isSuccess = false;
        }

      }

      this.notifyWebSocket.sendNotifyDevice(dpq.getUuid());
      WebSocketSendDataTimer.isUpdateData=true;
      if (isAuto) dpq.setDriAuto(1);
      return isSuccess;
    } catch (Exception e) {
      e.printStackTrace();
      this.notifyWebSocket.sendNotifyDevice(dpq.getUuid());
      WebSocketSendDataTimer.isUpdateData=true;
      if (isAuto) dpq.setDriAuto(1); 
    }
    return false;
  }

  private boolean validateResult(BusbarQuery dpq, long time)
  {
    long currentTime = System.currentTimeMillis();
    boolean loop = true;
    while (loop) {
      if (System.currentTimeMillis() - currentTime >= 5000L) {
        loop = false;
      }
      if (resultList.size() != 0) {
        Iterator<long[]> it = resultList.iterator();
        if (it.hasNext()) {
          long[] data = (long[])it.next();
          long timeStamp = data[0];
          long iotAddr = data[1];
          long nodeID = data[2];
          long status = data[3];
          if (timeStamp - time < 5000L) {
            if ((dpq.getDriIotAddr() == iotAddr) && ((dpq.getDriValveIdLeft() == nodeID) || (dpq.getDriValveIdRight() == nodeID))) {
              if (status == 1L) {
                it.remove();
                return true;
              }
              it.remove();
              return false;
            }
          }
          else
            it.remove();
        }
      }
      else {
        try {
          Thread.sleep(100L);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
    return false;
  }

  public static void telecontrolAckOper(DeviceTelecontrolAck data) {
	  InitAppModel.threadPool.execute(new Runnable() {
  		@Override
  		public void run() {
  			Map<Long, String> timerID=null; //用于从Map中删除
  			for (Map<Long, String> timerMap : telecontrolAckMap.keySet()) {
  				for (Long timer : timerMap.keySet()) {
//  					String operType=timerMap.get(timer);
  						Map<Integer, Integer> map=(Map<Integer, Integer>)telecontrolAckMap.get(timerMap);
  						for (int iotAddr : map.keySet()) {
  							if(iotAddr==data.getDeviceId()) {
  								int nodeID=map.get(iotAddr);
  								if(nodeID==data.getRealyId()) {
  									long[] resultData = new long[4];
  									resultData[0]=timer;
  									resultData[1]=iotAddr;
  									resultData[2]=nodeID;
  									resultData[3]=data.getStatus();
  									resultList.add(resultData);
  									timerID=timerMap;
  								}
  							}
  						}
  				}
  			}
  			if(timerID!=null) {
  				telecontrolAckMap.remove(timerID);//删除记录
  			}
  		}
  	});
  }
}