package com.dekalong.gqqtmonitor.web;

import com.dekalong.gqqtmonitor.initmodel.InitDeviceData;
import com.dekalong.gqqtmonitor.util.encrypt.AESUtil;
import com.dekalong.gqqtmonitor.util.josnutil.JsonHelper;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"/device"})
public class DeviceController
{

  @RequestMapping(value={"toClientPage"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String clientKeepConnection(HttpServletRequest request, String sIotAddr)
  {
    request.setAttribute("sIotAddr", sIotAddr);
    return "client/clientAnimation";
  }
  @ResponseBody
  @RequestMapping(value={"jsonpKeepConnection"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String jsonpKeepConnection(String callback, String sIotAddr) { 
	Map<String,Object> map = new HashMap<>();
    map.put("status", "success");
    map.put("sIotAddr", sIotAddr);
    String json = JsonHelper.map2String(map);
    String result = callback + "(" + json + ");";
    return result; 
  }
  
  @RequestMapping(value={"validate"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public String validate(HttpServletRequest request,HttpServletRequest session, Integer iotAddr)
  {
     if(iotAddr>0) {
    	 if(iotAddr.equals(InitDeviceData.IOTADDR)) {
    		 request.setAttribute("sIotAddr", AESUtil.aesDecryptNotExcption(iotAddr.toString()));
    		 session.setAttribute("login_error", "");
    		   return "client/clientAnimation";
    	 }
     }
     session.setAttribute("login_error", "物联网地址错误!,请重新输入！");
     return "redirect:/clientLogin.jsp"; 
  }
  
}