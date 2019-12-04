/**
 * Copyright 2019 TPRI. All Rights Reserved.
 */
package com.dekalong.gqqtmonitor.networtchannel.nettyconn.datarepeater.localclient;

import org.springframework.stereotype.Service;

import com.dekalong.gqqtmonitor.networtchannel.nettyconn.datarepeater.LocalClientBootstrap;
import com.dekalong.gqqtmonitor.networtchannel.nettyconn.datarepeater.repeater.RepeaterServiceHandler;



/**
 * <B>概要说明：</B><BR>
 * @author Long（Long）
 * @since 2019年7月26日
 * 
 */
@Service
public class LocalClientThread implements Runnable{
 
	public static LocalClientBootstrap localClient;

	
	
	/**
	 * <B>方法名称：</B><BR>
	 * <B>概要说明：</B><BR>
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			if(localClient!=null) {
				 while (true){
		        	   Object msg=RepeaterServiceHandler.toLocalhostJdrk.take();
		        	   if(localClient!=null&&localClient.getSocketChannel()!=null&&localClient.getSocketChannel().isActive()) {
		        		   localClient.getSocketChannel().writeAndFlush(msg);
		        	   }
			      }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		    
	}

}
