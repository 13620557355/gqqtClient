/**
 * 
 */
package com.dekalong.gqqtmonitor.initmodel;

import java.util.ArrayList;
import java.util.List;
import com.dekalong.gqqtmonitor.po.querymodel.BusbarQuery;
import com.dekalong.gqqtmonitor.po.querymodel.CustomerQuery;
import com.dekalong.gqqtmonitor.po.querymodel.MonitorQuery;
import com.dekalong.gqqtmonitor.util.properties.InitPropertiesData;

/**
 * @author Administrator
 *
 */
public class InitDeviceData {

    public static final int IOTADDR=getIntegerParm("deviceParam.iotAddr");
    public  static void initData() {
    	   CustomerQuery cQuery=new CustomerQuery();
		   cQuery.setUuid(getIntegerParm("deviceParam.custUuid"));
		   cQuery.setCustName(getStringParm("deviceParam.custName"));
		   initBusbar(cQuery);
		   initMonitor(cQuery);
		   InitAppModel.customerList.add(cQuery);
    }
	private static void initBusbar(CustomerQuery cQuery) {
		int i=1;
		String fileName="busbarParam";
		List<BusbarQuery> reList=new ArrayList<>();
		while (getIntegerParm(fileName+i+".uuid")!=null) {
			String busbarKey=fileName+i+".";
			 BusbarQuery busbar=new  BusbarQuery();
			   CustomerQuery customer=new CustomerQuery();
			   customer.setUuid(getIntegerParm("deviceParam.custUuid"));
			   customer.setCustName(getStringParm("deviceParam.custName"));
			   busbar.setCustUuid(customer);
			   busbar.setDriIotAddr(getIntegerParm("deviceParam.iotAddr")); 
			   
			   busbar.setDriAlarm(getDoubleParm(busbarKey+"driAlarm"));
			   busbar.setDriRange(getDoubleParm(busbarKey+"driRange")); 
			   busbar.setDriGasPressure(getDoubleParm(busbarKey+"driGasPressure")); 
			   busbar.setUuid(getIntegerParm(busbarKey+"uuid"));
			   busbar.setDriIdLeft(getIntegerParm(busbarKey+"driIdLeft"));
			   busbar.setDriIdRight(getIntegerParm(busbarKey+"driIdRight"));
			   busbar.setDriIdComm(getIntegerParm(busbarKey+"driIdComm")); 
			   busbar.setDriValveIdLeft(getIntegerParm(busbarKey+"driValveIdLeft")); 
			   busbar.setDriValveIdRight(getIntegerParm(busbarKey+"driValveIdRight")); 
			   busbar.setDriAuto(getIntegerParm(busbarKey+"driAuto")); 
			   busbar.setDriLeftCylindersNum(getIntegerParm(busbarKey+"driLeftCylindersNum"));
			   busbar.setDriRightCylindersNum(getIntegerParm(busbarKey+"driRightCylindersNum"));
			   busbar.setPipName(getStringParm(busbarKey+"pipName"));
			   busbar.setDriType(getStringParm(busbarKey+"driType"));
			   busbar.setDriContainerType(getStringParm(busbarKey+"driContainerType"));
			   busbar.setTransmitterType(getStringParm(busbarKey+"transmitterType"));
			   busbar.setDriValveType(getStringParm(busbarKey+"driValveType"));
			   reList.add(busbar);
			   i++;
		}
		cQuery.setBusbarList(reList);
    }
	
	private static void initMonitor(CustomerQuery cQuery) {
		int i=1;
		String fileName="monitorParam";
		List<MonitorQuery> reList=new ArrayList<>();
		while (getIntegerParm(fileName+i+".uuid")!=null) {
			String monitorKey=fileName+i+".";
			MonitorQuery monitor = new  MonitorQuery();
			 CustomerQuery customer=new CustomerQuery();
			 customer.setUuid(getIntegerParm("deviceParam.custUuid"));
			 customer.setCustName(getStringParm("deviceParam.custName"));
			 monitor.setCustUuid(customer);
		     monitor.setMonIotAddr(getIntegerParm("deviceParam.iotAddr"));
		     
		      monitor.setMonAlarm(getDoubleParm(monitorKey+"monAlarm"));	
		      monitor.setMonRange(getDoubleParm(monitorKey+"monRange"));
		      monitor.setMonGasPressure(getDoubleParm(monitorKey+"monGasPressure"));
		      monitor.setUuid(getIntegerParm(monitorKey+"uuid"));
		      monitor.setMonIdLeft(getIntegerParm(monitorKey+"monIdLeft"));
		      monitor.setMonIdRight(getIntegerParm(monitorKey+"monIdRight"));
		      monitor.setMonIdComm(getIntegerParm(monitorKey+"monIdComm"));
		      monitor.setMonLeftCylindersNum(getIntegerParm(monitorKey+"monLeftCylindersNum"));
		      monitor.setMonRightCylindersNum(getIntegerParm(monitorKey+"monRightCylindersNum"));
		      monitor.setMonName(getStringParm(monitorKey+"monName"));
		      monitor.setMonType(getStringParm(monitorKey+"monType"));
		      monitor.setMonContainerType(getStringParm(monitorKey+"monContainerType"));
		      monitor.setTransmitterType(getStringParm(monitorKey+"transmitterType"));
		      monitor.setTransmitterType(getStringParm(monitorKey+"transmitterType"));
		      reList.add(monitor);
		      i++;
		}
		 cQuery.setMonitorList(reList);
	}
	
	private static Integer getIntegerParm(String parm) {
		String value=InitPropertiesData.getContextProperty(parm);
		   return value==null?null:Integer.valueOf(value);
	 }
	private static String getStringParm(String parm) {
		   return InitPropertiesData.getContextProperty(parm);
	 }
	private static Double getDoubleParm(String parm) {
		String value=InitPropertiesData.getContextProperty(parm);
		  return value==null?null:Double.valueOf(value);
	 }
}
