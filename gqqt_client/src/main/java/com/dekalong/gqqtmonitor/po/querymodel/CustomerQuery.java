/**
 * Copyright 2019 TPRI. All Rights Reserved.
 */
package com.dekalong.gqqtmonitor.po.querymodel;


import java.util.List;

import com.dekalong.gqqtmonitor.po.Customer;

/**
 * <B>概要说明：</B><BR>
 * @author Long（Long）
 * @since 2019年1月4日
 * 
 */
public class CustomerQuery extends Customer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		//每个CWM对象都有自己的设备列表,用于接受物联网对象的参数
		private  List<BusbarQuery> busbarList;
		
		private  List<MonitorQuery> monitorList;
		
		
		
		public List<BusbarQuery> getBusbarList() {
			return busbarList;
		}

		public void setBusbarList(List<BusbarQuery> busbarList) {
			this.busbarList = busbarList;
		}
		public List<MonitorQuery> getMonitorList() {
			return monitorList;
		}
		public void setMonitorList(List<MonitorQuery> monitorList) {
			this.monitorList = monitorList;
		}
		

		
	
}
