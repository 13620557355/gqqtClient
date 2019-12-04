/**
 * Copyright 2019 TPRI. All Rights Reserved.
 */
package com.dekalong.gqqtmonitor.networtchannel.nettyconn.datarepeater.remotelyclient;


import org.springframework.stereotype.Service;

import com.dekalong.gqqtmonitor.networtchannel.nettyconn.datarepeater.LocalClientBootstrap;
import com.dekalong.gqqtmonitor.networtchannel.nettyconn.datarepeater.repeater.RepeaterServiceHandler;

import io.netty.channel.socket.SocketChannel;

/**
 * <B>概要说明：</B><BR>
 * @author Long（Long）
 * @since 2019年7月26日
 * 
 */
@Service
public class RemotelyClientThread implements Runnable{
	
	
	public static LocalClientBootstrap remotely;
	
	/**
	 * <B>方法名称：</B><BR>
	 * <B>概要说明：</B><BR>
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
	
		try {
		   if(remotely!=null) {
			   while (true){
	        	   Object msg=RepeaterServiceHandler.toRemotelyJdrk.take();
	        	  SocketChannel socketChannel =RepeaterServiceHandler.remotelySocketChannel;
	        	  if(socketChannel!=null&&socketChannel.isActive()) {
	        		   socketChannel.writeAndFlush(msg);
	        	   }
		        }
		   }
		} catch (Exception e) {
		
		}
		    
	}

}
