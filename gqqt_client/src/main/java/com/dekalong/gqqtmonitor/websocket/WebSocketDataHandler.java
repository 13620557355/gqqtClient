/**
 * Copyright 2019 TPRI. All Rights Reserved.
 */
package com.dekalong.gqqtmonitor.websocket;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage; 
import org.springframework.web.socket.WebSocketMessage; 
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.dekalong.gqqtmonitor.initmodel.InitAppModel;
import com.dekalong.gqqtmonitor.po.DeviceModel;
import com.dekalong.gqqtmonitor.po.querymodel.BusbarQuery;
import com.dekalong.gqqtmonitor.po.querymodel.CustomerQuery;
import com.dekalong.gqqtmonitor.po.querymodel.MonitorQuery;
import com.dekalong.gqqtmonitor.util.josnutil.JsonHelper;
import com.dekalong.gqqtmonitor.util.pageutil.PageUtil;
import com.dekalong.gqqtmonitor.util.regexutil.StrRegex;
import com.dekalong.gqqtmonitor.util.sortutil.DeviceModelComparator;

import java.io.EOFException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap; 
@Service 
public class WebSocketDataHandler extends TextWebSocketHandler { 
	
//	//客户，普通管理员，超级管理员	session

	public static  final Map<String, Map<WebSocketSession,Integer>> clientIotAddr;
	//维护当前websocket的前端首页动画的信息
	
;
	static {
		 clientIotAddr=new ConcurrentHashMap<String, Map<WebSocketSession,Integer>>();
	}
	
	//用户标识 
	private static final String CLIENT_ID = "userID"; 
//
	@Override 
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
	
		
		String completeUserID= getClientId(session);
	    Integer userID=Integer.valueOf(StrRegex.getWebsocketParm(completeUserID, "userID"));
	    String wsType=StrRegex.getWebsocketParm(completeUserID, "wstype");
	    if(wsType.equals("clientAnimation")) {
			if(iotAddrIsActive(userID)) {
				Map<WebSocketSession,Integer> clientMap=new HashMap<>();
				clientMap.put(session,1);// 当前页数
				clientIotAddr.put(completeUserID, clientMap);
				sendMessage(session, getIotData(userID, 1));
			}
		}
	
	} 
    private boolean iotAddrIsActive(Integer iotAddr) {
    	boolean isActive=false;
    	for (CustomerQuery cq : InitAppModel.customerList) {
			for (BusbarQuery bq : cq.getBusbarList()) {
				if(bq.getDriIotAddr()==iotAddr) {isActive=true;break;}
			}
			if(isActive) {break;}
			for (MonitorQuery mq : cq.getMonitorList()) {
				if(mq.getMonIotAddr()==iotAddr) {isActive=true;break;}
			}
			if(isActive) {break;}
		}
    	return isActive;
    }
    private String getIotData(int iotAddr,int page) {
   	 List<DeviceModel> dModels =new ArrayList<>();
   	 boolean isActive=false;
		for (CustomerQuery cq : InitAppModel.customerList) {
			for (BusbarQuery bq : cq.getBusbarList()) {
				if(bq.getDriIotAddr()==iotAddr) {dModels.add(bq);isActive=true;}
			}
			for (MonitorQuery mq : cq.getMonitorList()) {
				if(mq.getMonIotAddr()==iotAddr) {dModels.add(mq);isActive=true;}
			}
			if(isActive) {break;}
		}
   	 if(dModels.size()>0) {
   		 List<DeviceModel> resultList=PageUtil.conditionPageList(dModels, page, 10);
   		 Collections.sort(resultList,DeviceModelComparator.getComparatorASC());
   		 return StrRegex.listToJsonAddExcessKey(dModels,"refresh",dModels.size());
   	 }
   	 return null;
    }
	@Override 
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception { 	
	
			if (session.isOpen()) {
				session.close(); 
			}
			String key=getClientId(session);
			if(key!=null) {
				clientIotAddr.remove(key);
			}
			if(!(exception instanceof EOFException)) {
				exception.printStackTrace();
			}
	} 
	@Override 
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception { 
		
		try {
			if (session.isOpen()) {
				session.close(); 
			}
			String key=getClientId(session);
			if(key!=null) {
				clientIotAddr.remove(key);
			}
		} catch (Exception e) {
            if(!(e instanceof EOFException)) {
            	e.printStackTrace();
			}
           
		}
	} 
	
	@Override 
	public boolean supportsPartialMessages() { 
		return false;
	} 
	 /**
     * 消息处理，在客户端通过Websocket API发送的消息会经过这里，然后进行相应的处理，相当与@OnMessage注解
     */
	@Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		String smessage=message.getPayload().toString();
		if(smessage.contains("clientAnimation")) {
			int iotAddr =StrRegex.getSearcherParm(smessage,"iotAddr");
			int spage =StrRegex.getSearcherParm(smessage,"page");
			setClientAnimation(iotAddr,spage);
		}
		if(smessage.contains("HeartBeatPackage")) {
        	session.sendMessage(new TextMessage("HeartBeatPackage"+System.currentTimeMillis()));
    	}
    }
	private void setClientAnimation(Integer iotAddr ,int spage) { //设置页面
		for (String key : clientIotAddr.keySet()) {
			if(Integer.valueOf(StrRegex.getWebsocketParm(key, "userID")).equals(iotAddr)) {
				Map<WebSocketSession,Integer> paramMap=clientIotAddr.get(key);
				for (WebSocketSession paramkey : paramMap.keySet()) {
					paramMap.put(paramkey, spage);
				}
			}
		}
	}
   
	
 
	
	

	/**
    * 获取用户标识
    * @param session
    * @return
    */ 
	private String getClientId(WebSocketSession session) { 
    	try { 
    		String clientId = (String) session.getAttributes().get(CLIENT_ID); 
    		return clientId; 
    		} 
    	catch (Exception e) { 
    		return null;
    	}
    } 
	public void sendMessage(WebSocketSession session,String jsonInfo) {
		if (session!=null&&session.isOpen()) { 
			try { 
				if(jsonInfo!=null&&!jsonInfo.trim().equals("")) {
					session.sendMessage(new TextMessage(jsonInfo));
				}
				
			} catch (Exception e) {
				//e.printStackTrace();				 
			}
		} 
	}
	public void sendNotifyDevice(Integer driverId) {
		for (String key :clientIotAddr.keySet()) {
			if(Integer.valueOf(StrRegex.getWebsocketParm(key, "userID")).equals(driverId)) {
				 String jsonData=getOneDriJson(driverId);
				 for (WebSocketSession session : clientIotAddr.get(key).keySet()) {
					 sendMessage(session, jsonData);
				 }
			}
		}
	}
	  private String getOneDriJson(Integer driverUuid) {
	    	List<DeviceModel> dcqList=new ArrayList<>();
	    	for (DeviceModel deviceModel : InitAppModel.getBusbarAndMonList()) {
				if(deviceModel.getUuid().equals(driverUuid)) {
					dcqList.add(deviceModel);
				}
			}
	    	 if(!dcqList.isEmpty()) {
	    		 return  listToJson(dcqList);
	    	 }else {
	    		 return "";
	    	 }	
	    }
    /**
     * 
     * <B>方法名称：得到一条已更新的json信息</B><BR>
     * <B>概要说明：</B><BR>
     * @return
     */
    
    @SuppressWarnings({ "rawtypes", "unchecked" }) 
    private  String listToJson(List list) {
    	if(list.size()>0) {
    		Map map=new HashMap();
        	int total=list.size();
        	map.put("total", total);
    		map.put("rows",list);
    		map.put("deviceData","deviceData");
    		String json =JsonHelper.map2String(map);
        	return json;
    	}
    	return null;
    }
}
