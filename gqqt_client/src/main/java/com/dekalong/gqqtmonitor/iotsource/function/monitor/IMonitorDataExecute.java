/**
 * Copyright 2019 TPRI. All Rights Reserved.
 */
package com.dekalong.gqqtmonitor.iotsource.function.monitor;

import java.util.List;

import com.dekalong.gqqtmonitor.po.querymodel.MonitorQuery;
import com.dekalong.networtdevice.po.DeviceNodeData;

/**
 * <B>概要说明：数据执行操作接口</B><BR>
 * @author Long（Long）
 * @since 2019年4月15日
 * 
 */
public interface IMonitorDataExecute {
	public void executeOperation(MonitorQuery mq,List<DeviceNodeData> nodeList);
	
}
