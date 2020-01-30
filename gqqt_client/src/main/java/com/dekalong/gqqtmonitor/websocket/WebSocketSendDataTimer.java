package com.dekalong.gqqtmonitor.websocket;

import com.dekalong.gqqtmonitor.initmodel.InitAppModel;
import com.dekalong.gqqtmonitor.po.DeviceModel;
import com.dekalong.gqqtmonitor.po.querymodel.BusbarQuery;
import com.dekalong.gqqtmonitor.po.querymodel.CustomerQuery;
import com.dekalong.gqqtmonitor.po.querymodel.MonitorQuery;
import com.dekalong.gqqtmonitor.util.page.PageUtil;
import com.dekalong.gqqtmonitor.util.regex.StrRegex;
import com.dekalong.gqqtmonitor.util.sort.DeviceModelComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
public class WebSocketSendDataTimer
  implements Runnable
{

  @Autowired
  private WebSocketDataHandler handler;
  private static final int SLEEPTIME = 3000;
  public static boolean isUpdateData = false;

  private static Map<String, List<DeviceModel>> oldClientListMap = new ConcurrentHashMap<>();

  public void run()
  {
    try
    {
      while (true)
      {
        sendClientAnimationData();
        isUpdateData = false;
        Thread.sleep(SLEEPTIME);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void timingSendMessage(String data, WebSocketSession session)
  {
    this.handler.sendMessage(session, data);
  }
 

  private List<DeviceModel> dataCheck(List<DeviceModel> newList, List<DeviceModel> odlSet) {
	if(isUpdateData&&newList!=null&&newList.size()>0) {return newList;}
	if ((newList == null) || (odlSet == null)) { return null;}
    if (newList.size() == 0) return null;
    boolean isChange = false;
    for (DeviceModel oldData : odlSet) {
      boolean isActive = false;
      for (DeviceModel newData : newList) {
        if (oldData.getComparValue() != newData.getComparValue()) {
          isActive = true;
          break;
        }
      }
      if ((isActive)) {
        isChange = true; break;
      }
    }
    if (isChange) {
      return newList;
    }
    return null;
  }

  private void sendClientAnimationData(){
	    for (String completeUserID : WebSocketDataHandler.clientIotAddr.keySet()) { 
		      int iotAddr = Integer.valueOf(StrRegex.getWebsocketParm(completeUserID, "userID")).intValue();
		      int page = 1;
		      for (WebSocketSession key : WebSocketDataHandler.clientIotAddr.get(completeUserID).keySet()) {
		           page =WebSocketDataHandler.clientIotAddr.get(completeUserID).get(key).intValue();
		           timingSendMessage(getIotData(iotAddr, page, completeUserID), key); 
		       }  
	    } 
   } 
  private String getIotData(int iotAddr, int page, String completeUserID) {
		List<DeviceModel> dModels = new ArrayList<>();
	    boolean isActive = false;
	    for (CustomerQuery cq : InitAppModel.customerList) {
	      for (BusbarQuery bq : cq.getBusbarList())
	        if (bq.getDriIotAddr() == iotAddr) { 
	        	dModels.add(bq); isActive = true;
	        }
	      for (MonitorQuery mq : cq.getMonitorList()) {
	        if (mq.getMonIotAddr() == iotAddr) { 
	        	dModels.add(mq); isActive = true;
	        }
	      }
	        if (isActive) {break;}
	    }
	    if (!isActive) {oldClientListMap.remove(completeUserID);}
	    if (dModels.size() > 0) {
	      List<DeviceModel> resultList = PageUtil.conditionPageList(dModels, page, 10);
	      Collections.sort(resultList, DeviceModelComparator.getComparatorASC());
	      boolean isSend = dataCheck(resultList, oldClientListMap.get(completeUserID))!= null;
	      oldClientListMap.put(completeUserID, getNewResList(resultList));
	      if (isSend) {
	        return StrRegex.listToJsonAddExcessKey(resultList, "refresh", dModels.size());
	      }
	    }
	    return null;
	  }
  private  List<DeviceModel>  getNewResList(List<DeviceModel> resultList){
	  List<DeviceModel> newList = new ArrayList<>();
	  for (DeviceModel deviceModel :  resultList) {
		 DeviceModel dModel=new DeviceModel();
		 dModel.setComparValue(deviceModel.getComparValue());
		 newList.add(dModel);
	  }
	  return newList;
  }
}