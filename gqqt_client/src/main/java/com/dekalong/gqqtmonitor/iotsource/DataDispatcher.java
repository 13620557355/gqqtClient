package com.dekalong.gqqtmonitor.iotsource;

import com.dekalong.gqqtmonitor.initmodel.InitAppModel;
import com.dekalong.gqqtmonitor.iotsource.function.busbar.selfkeep.hexoper.HexCmdOperImpl;
import com.dekalong.gqqtmonitor.iotsource.function.busbar.unselfkeep.ValveOrdenaryImpl;
import com.dekalong.gqqtmonitor.po.DeviceModel;
import com.dekalong.gqqtmonitor.po.querymodel.BusbarQuery;
import com.dekalong.gqqtmonitor.po.querymodel.CustomerQuery;
import com.dekalong.gqqtmonitor.po.querymodel.MonitorQuery;
import com.dekalong.networtdevice.dataupload.IDataReceive;
import com.dekalong.networtdevice.po.DeviceNodeData;
import com.dekalong.networtdevice.po.DeviceRealTimeData;
import com.dekalong.networtdevice.po.DeviceTelecontrolAck;
import com.dekalong.networtdevice.po.DeviceTransDataAck;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataDispatcher
  implements IDataReceive
{

  @Autowired
  private BusbarDataHandle busbarDataHandle;

  @Autowired
  private MonitorDataHandle monitorDataHandle;

  public void dataHandle(DeviceRealTimeData data)
  {
    validateData(data);
  }

  public void transAck(DeviceTransDataAck data)
  {
    HexCmdOperImpl.transDataAckMap.put(Long.valueOf(System.currentTimeMillis()), data);
  }

  public void telecontrolAck(DeviceTelecontrolAck data)
  {
    ValveOrdenaryImpl.telecontrolAckOper(data);
  }
  private void validateData(DeviceRealTimeData data) {
    Map<DeviceModel,String> resultMap = dataDistinguish(data.getDeviceId(), data.getNodeList());
    if (resultMap.size() > 0)
      for (DeviceModel key : resultMap.keySet())
        excuteOper(resultMap.get(key), key, data.getNodeList(), data.getRelayStatus());
  }

  private void excuteOper(final String duplicateRequestID, final DeviceModel deviceData, final List<DeviceNodeData> nodeList, final String relayStatus)
  {
    InitAppModel.threadPool.execute(new Runnable()
    {
    @Override
      public void run() {
	        if (!InitAppModel.duplicateRequest.contains(duplicateRequestID)) {
	          InitAppModel.duplicateRequest.add(duplicateRequestID);
	          try {
	            DataDispatcher.this.dispatcher(deviceData, nodeList, relayStatus);
	          } catch (Exception e) {
	            e.printStackTrace();
	          } finally {
	            InitAppModel.duplicateRequest.remove(duplicateRequestID);
	          }
	        }
	      } 
      } 
    );
  }

  private Map<DeviceModel, String> dataDistinguish(int iotAddr, List<DeviceNodeData> nodeList) {
    List<CustomerQuery> customers = InitAppModel.customerList;
    Map<DeviceModel,String> map = new HashMap<>();
    boolean isSuccess = false;
    if (!nodeList.isEmpty())
      for (CustomerQuery customer : customers) {
        List<BusbarQuery> dQueries = customer.getBusbarList();
        if (dQueries != null)
          for (BusbarQuery dpq : dQueries ) { 
            if (dpq.getDriIotAddr() == iotAddr)
              for (DeviceNodeData nd : nodeList) {
                if (dpq.getDriIdLeft() == nd.getNodeId()) {
                  map.put(dpq, iotAddr + "|" + dpq.getDriIdLeft());
                  isSuccess = true;
                  break;
                }
              }
          }
        List<MonitorQuery> mQueries = customer.getMonitorList();
        if ((mQueries != null) && (mQueries != null)) {
          for (MonitorQuery mq : mQueries) { 
            if (mq.getMonIotAddr() == iotAddr)
              for (DeviceNodeData nd : nodeList)
                if (mq.getMonIdLeft() == nd.getNodeId()) {
                  map.put(mq, iotAddr + "|" + mq.getMonIdLeft());
                  isSuccess = true;
                  break;
                }
          }
         if (isSuccess)
          break;
         }
      }
    return map;
  }

  private void dispatcher(DeviceModel deviceData, List<DeviceNodeData> nodeList, String relayStatus) {
    if ((deviceData instanceof BusbarQuery)) {
      BusbarQuery bQuery = (BusbarQuery)deviceData;
      this.busbarDataHandle.executeOperation(bQuery, nodeList, relayStatus);
    } else if ((deviceData instanceof MonitorQuery)) {
      MonitorQuery mQuery = (MonitorQuery)deviceData;
      this.monitorDataHandle.executeOperation(mQuery, nodeList);
    }
  }
}