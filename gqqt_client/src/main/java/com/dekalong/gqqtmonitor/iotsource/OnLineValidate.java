/**
 * Copyright 2019 TPRI. All Rights Reserved.
 */
package com.dekalong.gqqtmonitor.iotsource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dekalong.gqqtmonitor.initmodel.InitAppModel;
import com.dekalong.gqqtmonitor.websocket.WebSocketDataHandler;
import com.dekalong.gqqtmonitor.websocket.WebSocketSendDataTimer;
import com.dekalong.gqqtmonitor.po.DeviceModel;

/**
 * <B>概要说明：校验设备是否在线的后台线程</B><BR>
 * @author Long（Long）
 * @since 2019年2月8日
 * 
 */
@Service
public class OnLineValidate implements Runnable{
    
	private static final int SLEEP_TIME=30000;
	private static final int TIME_OUT=30000;;
	@Autowired
	private WebSocketDataHandler notifyWebSocket;
	@Override
	public void run() {
		while (true) {
			validateDeviceOnline();
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
    }
	private void validateDeviceOnline() {
		long currentTime=System.currentTimeMillis();
		for (DeviceModel dpq : InitAppModel.getBusbarAndMonList()) {
				if(currentTime-dpq.getLastTime()>=TIME_OUT){
					boolean isOnLine=true;
					  if(dpq.getOnLineStatus()!=0||
						 dpq.getIotParamLeft()!=0||
						 dpq.getIotParamRight()!=0) {
						  isOnLine=false;
					  }
					  if(!isOnLine) {
						    dpq.setOnLineStatus(0);
							dpq.setIotParamLeft(0);
							dpq.setIotParamRight(0);
							notifyWebSocket.sendNotifyDevice(dpq.getUuid());
							WebSocketSendDataTimer.isUpdateData=true;
					  }
						
				}
		}
	}
}
