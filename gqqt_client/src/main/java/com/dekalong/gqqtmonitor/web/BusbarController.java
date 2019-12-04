/**
 * Copyright 2019 TPRI. All Rights Reserved.
 */
package com.dekalong.gqqtmonitor.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.dekalong.gqqtmonitor.iotsource.function.busbar.IValveExecuteOpertion;
import com.dekalong.gqqtmonitor.po.querymodel.BusbarQuery;

/**
 * <B>概要说明：</B><BR>
 * @author Long（Long）
 * @since 2019年1月5日
 * 
 */
@Controller
@RequestMapping(value="/busbar")
public class BusbarController{
	
	
	
	@Autowired
	@Qualifier("valveOrdenaryImpl")
	private IValveExecuteOpertion ordinary;
	@Autowired
	@Qualifier("valveSelfKeepImpl")
	private IValveExecuteOpertion selfKeep;
	

	
	@ResponseBody
	@RequestMapping(value="valveStatusUpd",method=RequestMethod.POST)
	public  String animationUpdate(@RequestBody BusbarQuery dpq){
		  boolean isSuccess =false;
		  String status="success";
		  if(dpq.getDriValveType().equals("selfkeep")) {
			  isSuccess= selfKeep.manualExecuteValve(dpq);
			}else if(dpq.getDriValveType().equals("onselfkeep")) {
			  isSuccess= ordinary.manualExecuteValve(dpq);
		  }
		  if(!isSuccess) { //如果不成功，返回失败
			  status="error";
		  }
		  return status;
	}
	
}
