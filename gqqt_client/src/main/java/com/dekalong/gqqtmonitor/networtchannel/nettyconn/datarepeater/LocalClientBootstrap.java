package com.dekalong.gqqtmonitor.networtchannel.nettyconn.datarepeater;


import java.util.concurrent.TimeUnit;

import com.dekalong.gqqtmonitor.networtchannel.nettyconn.MarshallingCodeCFactory;
import com.dekalong.gqqtmonitor.networtchannel.nettyconn.client.ClientStartThread;
import com.dekalong.gqqtmonitor.networtchannel.nettyconn.client.NettyClientHandler;
import com.dekalong.gqqtmonitor.networtchannel.nettyconn.datarepeater.localclient.LocalClientHandler;
import com.dekalong.gqqtmonitor.networtchannel.nettyconn.datarepeater.localclient.LocalClientThread;
import com.dekalong.gqqtmonitor.networtchannel.nettyconn.datarepeater.remotelyclient.RemotelyClientHandler;
import com.dekalong.gqqtmonitor.networtchannel.nettyconn.datarepeater.remotelyclient.RemotelyClientThread;
import com.dekalong.gqqtmonitor.networtchannel.nettyconn.datarepeater.repeater.RepeaterServiceHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class LocalClientBootstrap {
	
   private int port;
   private String host;
   private String bootstrapType;  
   private SocketChannel socketChannel;
    public LocalClientBootstrap(int port, String host,String bootstrapType) {
    	 this.port=port;
    	 this.host=host;
    	 this.bootstrapType=bootstrapType;
    	 if(bootstrapType.equals("dataClient")) {
    		 dataClientStart();
    	 }else {
    		 start();
    	 }
    }
    public LocalClientBootstrap(){}
    public void connect() {
    	if(bootstrapType.equals("dataClient")) {
    		 dataClientStart();
    	 }else {
    		 start();
    	 }
	}
    /**
	 * <B>方法名称：传递Driver数据的的通道</B><BR>
	 * <B>概要说明：</B><BR>
	 */
	private void dataClientStart()  {
//		ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);
		 EventLoopGroup eventLoopGroup=new NioEventLoopGroup();
	        Bootstrap bootstrap=new Bootstrap();
	        try {
	        	bootstrap.channel(NioSocketChannel.class);
	            bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
	            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
	            bootstrap.group(eventLoopGroup);
	            bootstrap.remoteAddress(host,port);
	            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
	                @Override
	                protected void initChannel(SocketChannel socketChannel) throws Exception {
	                	/**
	    				 *	    第一个参数 20 表示读操作空闲时间  20秒内，没有读操作，会触发userEventTriggered方法
	    				 *	    第二个参数 20 表示写操作空闲时间  20秒内，没有写操作，会触发userEventTriggered方法
	    				 *	    第三个参数 60*10 表示读写操作空闲时间
	    				 *	    第四个参数 单位/秒
	                	 */
	            		socketChannel.pipeline().addLast(new IdleStateHandler(20,20,60*10,TimeUnit.SECONDS));
	            		socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
	            		socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
	                    socketChannel.pipeline().addLast(new NettyClientHandler());
	                }
	            });
	           
	            ChannelFuture future =bootstrap.connect(host,port);
	            future.addListener(new ClientRefreshConn(this));
	            future.sync();
	            if (future.isSuccess()) {
	            	ClientStartThread.bootstrap=this;
	                socketChannel = (SocketChannel)future.channel();
	                initClientThread(bootstrapType,new ClientStartThread());
	            }
			   future.channel().closeFuture().sync();
			   eventLoopGroup.shutdownGracefully();
		}  catch (InterruptedException  e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 
	 * <B>方法名称：传递硬件数据的通道</B><BR>
	 * <B>概要说明：</B><BR>
	 */
    private  void start()  {
        Bootstrap bootstrap=new Bootstrap();
        EventLoopGroup eventLoopGroup=new NioEventLoopGroup();
        try {
        	 bootstrap.channel(NioSocketChannel.class);
             bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
             bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
             bootstrap.group(eventLoopGroup);
             bootstrap.remoteAddress(host,port);
             bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 protected void initChannel(SocketChannel socketChannel) throws Exception {
                 	if(bootstrapType.equals("remotely")) {
                 		  socketChannel.pipeline().addLast(new RemotelyClientHandler());
                 	}else if(bootstrapType.equals("localClient")){
                 		  socketChannel.pipeline().addLast(new LocalClientHandler());
                 	}
                   
                 }
             });
             ChannelFuture future =bootstrap.connect(host,port);
             future.addListener(new ClientRefreshConn(this));
             future.sync();
             if (future.isSuccess()) {
            	 if(bootstrapType.equals("remotely")) {
            		 RemotelyClientThread.remotely=this;
            		 RepeaterServiceHandler.remotelySocketChannel= (SocketChannel)future.channel();
            		 initClientThread(bootstrapType,new RemotelyClientThread());
            	 }else if(bootstrapType.equals("localClient")){
            		 LocalClientThread.localClient=this;
            		initClientThread(bootstrapType,new LocalClientThread());
            		socketChannel =(SocketChannel)future.channel();
            	 }
            	
             }

            future.channel().closeFuture().sync();
			 eventLoopGroup.shutdownGracefully();
		}  catch (InterruptedException  e) {
			e.printStackTrace();
		}finally {
		
		}
       
        
    }
    public static Thread remotely;
    public static Thread localClient;
    public static Thread dataClient;
    private void initClientThread(String name,Runnable runnable) {
    	try {
    		if(name.equals("remotely")) {
    			if(remotely==null||!remotely.isAlive()) {
    				remotely=new Thread(runnable);
        			remotely.start();
    			}
    		}else if(name.equals("localClient")){
    			if(localClient==null||!localClient.isAlive()) {
    				localClient=new Thread(runnable);
        			localClient.start();
    			}
    		}else if(name.equals("dataClient")){
    			if(dataClient==null||!dataClient.isAlive()) {
    			 dataClient=new Thread(runnable);
       			 dataClient.start();
    			}
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public SocketChannel getSocketChannel() {
		return socketChannel;
	}
}