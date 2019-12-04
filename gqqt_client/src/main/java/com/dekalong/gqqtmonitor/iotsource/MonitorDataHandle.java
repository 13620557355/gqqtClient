package com.dekalong.gqqtmonitor.iotsource;

import com.dekalong.gqqtmonitor.iotsource.function.monitor.IMonitorDataExecute;
import com.dekalong.gqqtmonitor.po.querymodel.MonitorQuery;
import com.dekalong.networtdevice.po.DeviceNodeData;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonitorDataHandle
{

  @Autowired
  private IMonitorDataExecute monitorOperation;

  public void executeOperation(MonitorQuery mq, List<DeviceNodeData> nodeList)
  {
    this.monitorOperation.executeOperation(mq, nodeList);
  }
}