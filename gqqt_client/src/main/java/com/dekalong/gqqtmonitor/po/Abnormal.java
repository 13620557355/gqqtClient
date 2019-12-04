/**
 * Copyright 2019 TPRI. All Rights Reserved.
 */
package com.dekalong.gqqtmonitor.po;

/**
 * <B>概要说明：</B><BR>
 * @author Long（Long）
 * @since 2019年2月21日
 * 
 */
public class Abnormal extends BaseModel{
	
	   /**  */
	private static final long serialVersionUID = 1L;
	private Customer custUuid;
	   private Busbar pipUuid;
	   private Monitor monitorUuid;
	   private String excType;
	   private String  excTime;
	   private String  excReason;
	   private String  maintainTime;
	   private String  maintainStaff;
	   private String  maintainResult;
	   private String  excRemarks;
	   
	public enum ExcTypeEnum {
		VALVE_EXC("电磁阀异常"), SMS_EXC("短信异常"),NETWORD_EXC("网络异常");
	    private String type;   
	    private ExcTypeEnum(String type) {  
	        this.type = type;
	    }  
	}
	
	public Customer getCustUuid() {
		return custUuid;
	}
	public Busbar getPipUuid() {
		return pipUuid;
	}
	public String getExcType() {
		return excType;
	}
	public String getExcTime() {
		return excTime;
	}
	public String getExcReason() {
		return excReason;
	}
	public String getMaintainTime() {
		return maintainTime;
	}
	public String getMaintainStaff() {
		return maintainStaff;
	}
	public String getMaintainResult() {
		return maintainResult;
	}
	public String getExcRemarks() {
		return excRemarks;
	}
	public void setCustUuid(Customer custUuid) {
		this.custUuid = custUuid;
	}
	public void setPipUuid(Busbar pipUuid) {
		this.pipUuid = pipUuid;
	}
	public void setExcType(ExcTypeEnum  excTypeEnum) {
		this.excType = excTypeEnum.type;
	}
	public void setExcType(String  excType) { //重载，用于数据库查询
		this.excType = excType;
	}
	public void setExcTime(String excTime) {
		this.excTime = excTime;
	}
	public void setExcReason(String excReason) {
		this.excReason = excReason;
	}
	public void setMaintainTime(String maintainTime) {
		this.maintainTime = maintainTime;
	}
	public void setMaintainStaff(String maintainStaff) {
		this.maintainStaff = maintainStaff;
	}
	public void setMaintainResult(String maintainResult) {
		this.maintainResult = maintainResult;
	}
	public void setExcRemarks(String excRemarks) {
		this.excRemarks = excRemarks;
	}
	public Monitor getMonitorUuid() {
		return monitorUuid;
	}
	public void setMonitorUuid(Monitor monitorUuid) {
		this.monitorUuid = monitorUuid;
	}
	@Override
	public String toString() {
		return "Abnormal [custUuid=" + custUuid + ", pipUuid=" + pipUuid + ", monitorUuid=" + monitorUuid + ", excType="
				+ excType + ", excTime=" + excTime + ", excReason=" + excReason + ", maintainTime=" + maintainTime
				+ ", maintainStaff=" + maintainStaff + ", maintainResult=" + maintainResult + ", excRemarks="
				+ excRemarks + "]";
	}
	
	
}
