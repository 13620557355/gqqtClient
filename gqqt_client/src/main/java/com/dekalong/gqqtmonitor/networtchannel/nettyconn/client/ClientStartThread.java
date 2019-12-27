/**
 * Copyright 2019 TPRI. All Rights Reserved.
 */
package com.dekalong.gqqtmonitor.networtchannel.nettyconn.client;


import org.springframework.stereotype.Service;

import com.dekalong.gqqtmonitor.initmodel.InitDeviceData;
import com.dekalong.gqqtmonitor.networtchannel.nettyconn.datarepeater.LocalClientBootstrap;
import com.dekalong.gqqtmonitor.util.properties.InitPropertiesData;

/**
 * <B>概要说明：</B><BR>
 * @author Long（Long）
 * @since 2019年7月26日
 * 
 */
@Service
public class ClientStartThread implements Runnable{
     
	public static LocalClientBootstrap bootstrap;
	/**
	 * <B>方法名称：</B><BR>
	 * <B>概要说明：</B><BR>
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
		    int sendInterval=Integer.valueOf(InitPropertiesData.getContextProperty("networdchannel.sendInterval"));
			while (true) {
		      if(bootstrap!=null&&bootstrap.getSocketChannel().isActive()) {
		        	bootstrap.getSocketChannel().writeAndFlush(InitDeviceData.IOTADDR);
		       }
				Thread.sleep(sendInterval*1000);
			}
		} catch (Exception e) {
		
		}
		    
	}

}
