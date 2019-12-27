/**
 * Copyright 2019 TPRI. All Rights Reserved.
 */
package com.dekalong.gqqtmonitor.networtchannel.nettyconn.client;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.dekalong.gqqtmonitor.initmodel.InitAppModel;
import com.dekalong.gqqtmonitor.po.DeviceModel;
import com.dekalong.gqqtmonitor.po.querymodel.BusbarQuery;
import com.dekalong.gqqtmonitor.po.querymodel.CustomerQuery;
import com.dekalong.gqqtmonitor.po.querymodel.MonitorQuery;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * <B>概要说明：</B><BR>
 * @author Long（Long）
 * @since 2019年7月26日
 * 
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
 
    @SuppressWarnings("unchecked")
	@Override
   	public void channelRead(ChannelHandlerContext ctx, Object msg)
   			throws Exception {
       if(msg instanceof List) {
    	   overrideData((List<DeviceModel>)msg);
       }
   	}
   @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
	   InitAppModel.initDataThread();
    }
   
   @Override
   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	   cause.printStackTrace();
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
   
   private void overrideData(List<DeviceModel> list) {
	   CustomerQuery cqQuery=InitAppModel.customerList.get(0);
	   Iterator<BusbarQuery> bIterator=cqQuery.getBusbarList().iterator();
	   Iterator<MonitorQuery> mIterator=cqQuery.getMonitorList().iterator();
		 for (DeviceModel dm : list) {
			 boolean isSuccess=false;
			 if(dm instanceof BusbarQuery) {
				 while (bIterator.hasNext()) {
					 if(dm.getUuid().equals(bIterator.next().getUuid())) {
						 bIterator.remove();
						 isSuccess=true;
					 }
				 }
				 if(isSuccess) {cqQuery.getBusbarList().add((BusbarQuery)dm);}
			 }else if(dm instanceof MonitorQuery){
                 while (mIterator.hasNext()) {
                	 if(dm.getUuid().equals(mIterator.next().getUuid())) {
                		 mIterator.remove();
                		 isSuccess=true;
					 }
				 }
                 if(isSuccess) {cqQuery.getMonitorList().add((MonitorQuery)dm);}
			 }
		 }
   }
    
}
