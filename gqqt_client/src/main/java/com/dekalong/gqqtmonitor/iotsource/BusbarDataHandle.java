/**
 * Copyright 2019 TPRI. All Rights Reserved.
 */
package com.dekalong.gqqtmonitor.iotsource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.dekalong.gqqtmonitor.iotsource.function.TransmitterTypeFactory;
import com.dekalong.gqqtmonitor.iotsource.function.busbar.BusbarUtil;
import com.dekalong.gqqtmonitor.iotsource.function.busbar.IBusbarDataExecute;
import com.dekalong.gqqtmonitor.iotsource.notify.sms.IValidateSMS;
import com.dekalong.gqqtmonitor.po.querymodel.BusbarQuery;
import com.dekalong.gqqtmonitor.websocket.WebSocketDataHandler;
import com.dekalong.networtdevice.po.DeviceNodeData;


/**
 * <B>概要说明：物联网源数据处理器</B><BR>
 * @author Long（Long）
 * @since 2019年2月6日
 * 
 */
@Service
public class BusbarDataHandle {
	
	@Autowired
    @Qualifier("busbarSelfKeepImpl")
	private IBusbarDataExecute selfKeepOpertion;
   
    @Autowired
    @Qualifier("busbarOrdinaryImpl")
	private IBusbarDataExecute ordinaryImpl;
	
	@Autowired
	private IValidateSMS validateSMS;
	@Autowired
	private WebSocketDataHandler notifyWebSocket;
	
	
	public  void executeOperation (BusbarQuery dpq,List<DeviceNodeData> nodeList,String relayStatus) {			
		boolean isSuccess=false;
		boolean nonDuplicate=false;
		int driIdLeft=dpq.getDriIdLeft();
		int driIdRight=dpq.getDriIdRight();
		int driIdComm=dpq.getDriIdComm();
		for (DeviceNodeData nd : nodeList) {
			int nodeId=nd.getNodeId();
			double pressureValue=TransmitterTypeFactory.getTransmitterValue(dpq, nd.getHum());
			if(driIdLeft==nodeId) {
				isSuccess=true;//如果成功进来，则退出循环
				if(dpq.getOnLineStatus()==0) {nonDuplicate=true;}
				dpq.setOnLineStatus(1);
				dpq.setLastTime(System.currentTimeMillis());
			    if(dpq.getIotParamLeft()!= pressureValue) {nonDuplicate=true;}										    
			    BusbarUtil.leftBackflowValidate(pressureValue, dpq);
			}else if(driIdRight==nodeId) {
				 if(dpq.getIotParamRight()!=pressureValue) {nonDuplicate=true;}
				 BusbarUtil.rightBackflowValidate(pressureValue, dpq); 
			}else if (driIdComm==nodeId) {
				if(dpq.getIotParamComm()!=pressureValue) {nonDuplicate=true;}
				 BusbarUtil.commBackflowValidate(pressureValue, dpq);
			}
	   }
		if(isSuccess) {
			boolean isSendMessage=valveOperType(dpq,relayStatus,nonDuplicate);
			if(isSendMessage||nonDuplicate) {
			   //validateSMS.validateCustomerSendSMS(dpq);
				notifyWebSocket.sendNotifyDevice(dpq.getUuid());
			}
		}
	}
	private boolean valveOperType(BusbarQuery dpq,String relayStatus,boolean nonDuplicate) {
		if(dpq.getDriValveType().equals("selfkeep")) {
				return selfKeepOpertion.executeOperation(dpq, relayStatus,nonDuplicate);
		}else if(dpq.getDriValveType().equals("onselfkeep")) {
				return  ordinaryImpl.executeOperation(dpq, relayStatus,nonDuplicate);
		}
		return false;
	}
}
