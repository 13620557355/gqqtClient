/**
 * Copyright 2019 TPRI. All Rights Reserved.
 */
package com.dekalong.gqqtmonitor.networtchannel.nettyconn.datarepeater.remotelyclient;

import java.io.IOException;

import com.dekalong.gqqtmonitor.initmodel.InitAppModel;
import com.dekalong.gqqtmonitor.networtchannel.nettyconn.datarepeater.repeater.RepeaterServiceHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * <B>概要说明：</B><BR>
 * @author Long（Long）
 * @since 2019年7月30日
 * 
 */
public class  RemotelyClientHandler extends ChannelInboundHandlerAdapter{

	public static ChannelHandlerContext toRemotelyClientChannel;

    @Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
    	toRemotelyClientChannel=ctx;
    	if(RepeaterServiceHandler.deviceChannel!=null) {
    		RepeaterServiceHandler.deviceChannel.writeAndFlush(msg); 
    	}
    	
	}
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	//使用过程中断线重连
    	 InitAppModel.initRemotelyClient();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	if(cause instanceof IOException) {}
    	 ctx.close();
    }
    /**
  	 * channelReadComplete channel 通道 Read 读取 Complete 完成
  	 * 在通道读取完成后会在这个方法里通知，对应可以做刷新操作 ctx.flush()
  	 */
    @Override
  	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
  		ctx.flush();
  	}
}
