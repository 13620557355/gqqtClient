/**
 * Copyright 2019 TPRI. All Rights Reserved.
 */
package com.dekalong.gqqtmonitor.websocket;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.dekalong.gqqtmonitor.util.regex.StrRegex;


/**
 * <B>概要说明：</B><BR>
 * @author Long（Long）
 * @since 2019年1月27日
 * 
 */
public class WebSocketInterceptor implements HandshakeInterceptor {
	
	@Override 
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, 
			WebSocketHandler handler, Map<String, Object> attributes) throws Exception {
		
		if (request instanceof ServletServerHttpRequest) { 
			ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest) request;
			Object requestUser=serverHttpRequest.getServletRequest().getParameter("user");
			Object wstype=serverHttpRequest.getServletRequest().getParameter("wstype");
			try {Integer.valueOf(requestUser.toString());} catch (Exception e) {requestUser=null;}
			if(requestUser!=null) {
				String userID=requestUser.toString();
				if(userID.trim()!="") {
					if(wstype!=null&&!wstype.toString().trim().equals("")) {
						userID=System.currentTimeMillis()+"|userID="+userID+"|wstype="+wstype.toString()+"|";
					}else {
						userID=System.currentTimeMillis()+"|userID="+userID+"|";
					}
					attributes.put("userID", userID); //把用户添加到
					return true;
				}
				return false;
			}
		     	return false;
	   }else {
		        return false;
	   } 
	} 
	@Override 
	public void afterHandshake(ServerHttpRequest serverHttpRequest, 
			ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
		
	}
	

}
