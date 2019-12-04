/**
 * Copyright 2019 TPRI. All Rights Reserved.
 */
package com.dekalong.gqqtmonitor.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket 
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer { 
	
	
	@Override 
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) { 
		//允许连接的域,只能以http或https开头
//        String[] allowsOrigins = {"http://localhost:8080"};
		
				registry.addHandler(myHandler(), "/dataHandler").
				addInterceptors(new WebSocketInterceptor()).setAllowedOrigins("*");

			    registry.addHandler(myHandler(), "/dataHandler").setAllowedOrigins("*").
			    addInterceptors(new WebSocketInterceptor()).withSockJS();
	} 
	@Bean
	public WebSocketHandler myHandler() {
		return new WebSocketDataHandler();
		}
}

