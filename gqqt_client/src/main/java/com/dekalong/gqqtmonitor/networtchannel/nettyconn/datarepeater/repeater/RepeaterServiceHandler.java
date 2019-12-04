/**
 * Copyright 2019 TPRI. All Rights Reserved.
 */
package com.dekalong.gqqtmonitor.networtchannel.nettyconn.datarepeater.repeater;


import java.io.IOException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;


/**
 * <B>概要说明：</B><BR>
 * @author Long（Long）
 * @since 2019年7月26日
 * 
 */
public class RepeaterServiceHandler extends ChannelInboundHandlerAdapter{
	public static  BlockingQueue<Object> toLocalhostJdrk=new LinkedBlockingQueue<>();
	public static  BlockingQueue<Object> toRemotelyJdrk=new LinkedBlockingQueue<>();
	public static ChannelHandlerContext deviceChannel;
    public static SocketChannel remotelySocketChannel;
    public static boolean remotelyIsConn=true;
	
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	
    }
    @Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
	        deviceChannel=ctx;
	        validateRemotelyChannel();
			if(!toRemotelyJdrk.isEmpty()) {
				toRemotelyJdrk.clear();
				toRemotelyJdrk.add(msg);
			}else {
				toRemotelyJdrk.add(msg);
			}
			
			if(!toLocalhostJdrk.isEmpty()) {
				toLocalhostJdrk.clear();
				toLocalhostJdrk.add(msg);
			}else {
				toLocalhostJdrk.add(msg);
			}	
	}
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	if(cause instanceof IOException) {}
    	 ctx.close();
    	 cause.printStackTrace();
    }
    /**
 	 * channelReadComplete channel 通道 Read 读取 Complete 完成
 	 * 在通道读取完成后会在这个方法里通知，对应可以做刷新操作 ctx.flush()
 	 */
   @Override
 	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
 		ctx.flush();
 	}
    private static void validateRemotelyChannel() {
		if(remotelySocketChannel!=null&&remotelySocketChannel.isActive()) {
			remotelyIsConn=true;
    	}else {
    		remotelyIsConn=false;
    	}
    }
}