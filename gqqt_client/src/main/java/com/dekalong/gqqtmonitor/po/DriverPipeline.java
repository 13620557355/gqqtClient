/**
 * Copyright 2019 TPRI. All Rights Reserved.
 */
package com.dekalong.gqqtmonitor.po;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <B>概要说明：</B><BR>
 * @author Long（Long）
 * @since 2019年1月4日
 * 
 */
public class DriverPipeline  extends BaseModel implements Serializable{
    
	
	/**  */
	private static final long serialVersionUID = 1L;
	private Customer custUuid;
	private String  pipName;
	private String  driType;           
	private int  driIotAddr;
	private int  driIdLeft;
	private int  driIdRight;
	private int  driIdComm;
	private int  driValveIdLeft;
	private int  driValveIdRight;
	private String driContainerType;
	private int     driAuto;
	private double  driAlarm;
	private double   driGasPressure;
	private String  driRemarks ;
	private double  driRange;
	private int  driCylindersNum;
	public Customer getCustUuid() {
		return custUuid;
	}
	public String getPipName() {
		return pipName;
	}
	public String getDriType() {
		return driType;
	}
	
	public int getDriIdLeft() {
		return driIdLeft;
	}
	public int getDriIdRight() {
		return driIdRight;
	}

	public int getDriValveIdLeft() {
		return driValveIdLeft;
	}
	public int getDriValveIdRight() {
		return driValveIdRight;
	}
	public String getDriContainerType() {
		return driContainerType;
	}
	public int getDriAuto() {
		return driAuto;
	}
	public double getDriAlarm() {
		BigDecimal   b   =   new   BigDecimal(driAlarm);  
		double   f2   =   b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue();  
		return f2;
	}
	public double getDriGasPressure() {
		BigDecimal   b   =   new   BigDecimal(driGasPressure);  
		double   f2   =   b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue();  
		return f2;
	}
	public String getDriRemarks() {
		return driRemarks;
	}
	public void setCustUuid(Customer custUuid) {
		this.custUuid = custUuid;
	}
	public void setPipName(String pipName) {
		this.pipName = pipName;
	}
	public void setDriType(String driType) {
		this.driType = driType;
	}
	
	public void setDriIdLeft(int driIdLeft) {
		this.driIdLeft = driIdLeft;
	}
	public void setDriIdRight(int driIdRight) {
		this.driIdRight = driIdRight;
	}

	public void setDriValveIdLeft(int driValveIdLeft) {
		this.driValveIdLeft = driValveIdLeft;
	}
	public void setDriValveIdRight(int driValveIdRight) {
		this.driValveIdRight = driValveIdRight;
	}
	public void setDriContainerType(String driContainerType) {
		this.driContainerType = driContainerType;
	}
	public void setDriAuto(int driAuto) {
		this.driAuto = driAuto;
	}
	public void setDriAlarm(double driAlarm) {
		this.driAlarm = driAlarm;
	}
	public void setDriGasPressure(double driGasPressure) {
		this.driGasPressure = driGasPressure;
	}
	public void setDriRemarks(String driRemarks) {
		this.driRemarks = driRemarks;
	}
	public int getDriIotAddr() {
		return driIotAddr;
	}
	public void setDriIotAddr(int driIotAddr) {
		this.driIotAddr = driIotAddr;
	}
	
	
	public double getDriRange() {
		BigDecimal   b   =   new   BigDecimal(driRange);  
		double   f2   =   b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue();  
		return f2;
	}
	public void setDriRange(double driRange) {
		this.driRange = driRange;
	}
	
	public int getDriIdComm() {
		return driIdComm;
	}
	public void setDriIdComm(int driIdComm) {
		this.driIdComm = driIdComm;
	}
	@Override
	public String toString() {
		return "DriverPipeline [uuid=" + getUuid() + ", custUuid=" + custUuid + ", pipName=" + pipName + ", driType=" + driType
				+ ", driIotAddr=" + driIotAddr + ", driIdLeft=" + driIdLeft + ", driIdRight=" + driIdRight
				+ ", driIdComm=" + driIdComm + ", driValveIdLeft=" + driValveIdLeft + ", driValveIdRight="
				+ driValveIdRight + ", driContainerType=" + driContainerType + ", driAuto=" + driAuto + ", driAlarm="
				+ driAlarm + ", driGasPressure=" + driGasPressure + ", driRemarks=" + driRemarks + ", driRange="
				+ driRange + ", driCylindersNum="+ driCylindersNum + "]"; 
	}
	public int getDriCylindersNum() {
		return driCylindersNum;
	}
	public void setDriCylindersNum(int driCylindersNum) {
		this.driCylindersNum = driCylindersNum;
	}
	
}
