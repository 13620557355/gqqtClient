/**
 * Copyright 2019 TPRI. All Rights Reserved.
 */
package com.dekalong.gqqtmonitor.networtchannel.nettyconn.datarepeater;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import org.springframework.context.support.StaticApplicationContext;

import com.dekalong.gqqtmonitor.networtchannel.nettyconn.datarepeater.repeater.RepeaterServiceHandler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import io.netty.channel.socket.SocketChannel;

/**
 * <B>概要说明：</B><BR>
 * @author Long（Long）
 * @since 2019年7月31日
 * 
 */
public class ClientRefreshConn implements ChannelFutureListener {
	
  private  LocalClientBootstrap hardwareClientBootstrap;
  public  ClientRefreshConn(LocalClientBootstrap hardwareClientBootstrap) {
	  this.hardwareClientBootstrap=hardwareClientBootstrap;
  }
	/**
	 * <B>方法名称：</B><BR>
	 * <B>概要说明：</B><BR>
	 * @see io.netty.util.concurrent.GenericFutureListener#operationComplete(io.netty.util.concurrent.Future)
	 */
	@Override
	public void operationComplete(ChannelFuture future) {
		try {
			if (!future.isSuccess()) {
	            final EventLoop loop = future.channel().eventLoop();
	            loop.schedule(new Runnable() {
	                @Override
	                public void run() {
	                	hardwareClientBootstrap.connect();
	                }
	            }, 60, TimeUnit.SECONDS);
	        }
		} catch (Exception e) {
			
		}
	}
	


	public LocalClientBootstrap getJdrkClientBootstrap() {
		return hardwareClientBootstrap;
	}


	public void setJdrkClientBootstrap(LocalClientBootstrap hardwareClientBootstrap) {
		this.hardwareClientBootstrap = hardwareClientBootstrap;
	}



}
