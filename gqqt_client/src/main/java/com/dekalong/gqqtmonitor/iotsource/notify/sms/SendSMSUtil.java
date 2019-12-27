/**
 * Copyright 2019 TPRI. All Rights Reserved.
 */
package com.dekalong.gqqtmonitor.iotsource.notify.sms;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.dekalong.gqqtmonitor.util.http.HttpUtils;
import com.dekalong.gqqtmonitor.util.josn.JsonHelper;


/**
 * <B>概要说明：</B><BR>
 * @author Long（Long）
 * @since 2019年5月4日
 * 
 */

public class SendSMSUtil {
    public static boolean sendMessage(String content,String phone) {
    	String host = "https://dxyzm.market.alicloudapi.com";
 	    String path = "/chuangxin/dxjk";
 	    String method = "POST";
 	    String appcode = "b3539b2f8bd44c5b947139b347a8a1b9";
 	    Map<String, String> headers = new HashMap<String, String>();
 	    headers.put("Authorization", "APPCODE " + appcode);
 	    Map<String, String> querys = new HashMap<String, String>();
 	    querys.put("content",content);
 	    querys.put("mobile", phone);
 	    Map<String, String> bodys = new HashMap<String, String>();
 	    try {
 	    	HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
 	    	String jsonMessage=EntityUtils.toString(response.getEntity());
 	    	if(JsonHelper.str2Object(jsonMessage,Object.class).toString().contains("ReturnStatus=Success, Message=ok")) {
 	        	return true;
 	        }else {
				return false;
			}
 	    } catch (Exception e) {
 	    	e.printStackTrace();
 	    	return false;
 	    }
    }

/**
//  阿里云第三方（成都创信信息技术有限公司）
	public static void  main(String[] args) {
	    String host = "https://dxyzm.market.alicloudapi.com";
	    String path = "/chuangxin/dxjk"; b
	    String method = "POST";
	    String appcode = "b3539b2f8bd44c5b947139b347a8a1b9";
	    Map<String, String> headers = new HashMap<String, String>();
	    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
	    headers.put("Authorization", "APPCODE " + appcode);
	    Map<String, String> querys = new HashMap<String, String>();
//	    String mesString="【广气气体】尊敬的客户，贵单位的氮气低压报警,气体即将用完,如需帮助请拨打020-34001422.";
	    String mesString="【广气气体】尊敬的客户，贵单位的氮气已经置换完毕,如需帮助请拨打020-34001422.";
	    
	    querys.put("content", mesString);
            //注意测试可用：【创信】你的验证码是：#code#，3分钟内有效！，发送自定义内容联系旺旺或QQ：726980650报备
	    querys.put("mobile", "13620557355");
	    Map<String, String> bodys = new HashMap<String, String>();
	    try {	    	
//	    	 重要提示如下:
	    	
//	    	 HttpUtils请从
//	    	 https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
//	    	 下载
//	    	
//	    	相应的依赖请参照
//	    	 https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml    	
	    	HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);	    	
//	    	System.out.println(response.toString());
	    	//获取response的body
	    	System.out.println(EntityUtils.toString(response.getEntity()));
//	    	{"ReturnStatus":"Success","Message":"ok","RemainPoint":509551,"TaskID":22666365,"SuccessCounts":1}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
*/
}
