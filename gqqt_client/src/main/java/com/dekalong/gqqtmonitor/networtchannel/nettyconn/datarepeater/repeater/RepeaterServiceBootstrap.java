/**
 * Copyright 2019 TPRI. All Rights Reserved.
 */
package com.dekalong.gqqtmonitor.networtchannel.nettyconn.datarepeater.repeater;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
/**
 * <B>概要说明：</B><BR>
 * @author Long（Long）
 * @since 2019年7月26日
 * 
 */
public class RepeaterServiceBootstrap {
    private static int port;
    public static void initBootstrap(int port) {
    	RepeaterServiceBootstrap.port=port;
    	 bind();
    }
    private static void bind()  {
    	ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);
    	EventLoopGroup boss=new NioEventLoopGroup();
        EventLoopGroup worker=new NioEventLoopGroup();
        ServerBootstrap bootstrap=new ServerBootstrap();
    	try {
		   bootstrap.group(boss,worker);
	        bootstrap.channel(NioServerSocketChannel.class);
	        bootstrap.option(ChannelOption.SO_BACKLOG, 128);
	        bootstrap.option(ChannelOption.TCP_NODELAY, true);
	        //保持长连接状态
	        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
	        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
	            @Override
	            protected void initChannel(SocketChannel socketChannel) throws Exception {
	                ChannelPipeline p = socketChannel.pipeline();
	                p.addLast(new RepeaterServiceHandler());
	                
	            }
	        });
	        ChannelFuture future= bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
		} catch (Exception e) {
		}finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
    }
  
}
