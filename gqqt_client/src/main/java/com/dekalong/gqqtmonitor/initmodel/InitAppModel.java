package com.dekalong.gqqtmonitor.initmodel;


import com.dekalong.gqqtmonitor.iotsource.OnLineValidate;
import com.dekalong.gqqtmonitor.networtchannel.nettyconn.client.ClientStartThread;
import com.dekalong.gqqtmonitor.networtchannel.nettyconn.datarepeater.LocalClientBootstrap;
import com.dekalong.gqqtmonitor.networtchannel.nettyconn.datarepeater.localclient.LocalClientThread;
import com.dekalong.gqqtmonitor.networtchannel.nettyconn.datarepeater.remotelyclient.RemotelyClientThread;
import com.dekalong.gqqtmonitor.networtchannel.nettyconn.datarepeater.repeater.RepeaterServiceBootstrap;
import com.dekalong.gqqtmonitor.po.DeviceModel;
import com.dekalong.gqqtmonitor.po.querymodel.CustomerQuery;
import com.dekalong.gqqtmonitor.util.propertiesUtil.InitPropertiesData;
import com.dekalong.gqqtmonitor.websocket.WebSocketSendDataTimer;
import com.dekalong.networtdevice.DataSourceThread;
import com.dekalong.networtdevice.dataupload.IDataReceive;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class InitAppModel implements InitializingBean
{
  
  @Autowired
  private OnLineValidate onLineValidate;
  @Autowired
  private IDataReceive IDataReceive;
  
  @Autowired
  private WebSocketSendDataTimer webSocketSendDataTimer;
  
  public static final ExecutorService threadPool=Executors.newCachedThreadPool();
  //此无界队列用于锁定执行操作期间的重复请求
  public static final BlockingQueue<String> duplicateRequest=new LinkedBlockingDeque<String>();
  public static final List<CustomerQuery> customerList = Collections.synchronizedList(new ArrayList<>());
  public void afterPropertiesSet() throws Exception
  {
	InitDeviceData.initData();
	DataSourceThread.registerIDataReceive(IDataReceive);//注册
    initClientThread();
    initLocalClient();
    initRemotelyClient();
    initThread();
  }
  public static void initRemotelyClient() {
	threadPool.execute(new Runnable()
    {
      public void run() {
        try {
          int port = Integer.valueOf(InitPropertiesData.getContextProperty("networdchannel.jdrkRemotelyPort")).intValue();
          String host = InitPropertiesData.getContextProperty("networdchannel.dataIpAddr");
          RemotelyClientThread.remotely = new LocalClientBootstrap(port, host, "remotely");
        } catch (Exception e) {
          e.printStackTrace();
          RemotelyClientThread.remotely = null;
        }
      }
    });
  }

  public static void initClientThread() {
	 threadPool.execute(new Runnable()
    {
      public void run() {
        try {
          String ipAddr = InitPropertiesData.getContextProperty("networdchannel.dataIpAddr");
          int port = Integer.valueOf(InitPropertiesData.getContextProperty("networdchannel.dataPort")).intValue();
          ClientStartThread.bootstrap = new LocalClientBootstrap(port, ipAddr, "dataClient");
        } catch (Exception e) {
          e.printStackTrace();
          ClientStartThread.bootstrap = null;
        }
      }
    });
  }

  public static void initLocalClient() {
	 threadPool.execute(new Runnable()
    {
      public void run() {
        try {
        	
          int port = Integer.valueOf(InitPropertiesData.getContextProperty("networdchannel.jdrkLocalPort")).intValue();       
          DataSourceThread.start(port);	
          String host = "127.0.0.1";
          LocalClientThread.localClient = new LocalClientBootstrap(port, host, "localClient");
        } catch (Exception e) {
          e.printStackTrace();
          LocalClientThread.localClient = null;
        }
      }
    });
  }

  private void initThread() {
	threadPool.execute(new Runnable() {
		@Override
		public void run() {
			int port=Integer.valueOf(InitPropertiesData.getContextProperty("networdchannel.hardwarePort"));
			RepeaterServiceBootstrap.initBootstrap(port);
		}
	});
	threadPool.execute(this.onLineValidate);
	threadPool.execute(this.webSocketSendDataTimer);
  }

  public static List<CustomerQuery> getCustomerList()
  {
    return customerList;
  }
  public static List<DeviceModel> getBusbarAndMonList() {
		List<DeviceModel> list=new ArrayList<>();
		for (CustomerQuery customer : customerList) {
			list.addAll(customer.getBusbarList());
			list.addAll(customer.getBusbarList());
		}
		return list;
	}
}