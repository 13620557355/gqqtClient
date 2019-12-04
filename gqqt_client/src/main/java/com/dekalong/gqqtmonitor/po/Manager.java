/**
 * Copyright 2019 TPRI. All Rights Reserved.
 */
package com.dekalong.gqqtmonitor.po;


/**
 * <B>概要说明：</B><BR>
 * @author Long（Long）
 * @since 2019年1月4日
 * 
 */
public class Manager  extends BaseModel {
   
	/**  */
	private static final long serialVersionUID = 1L;
	private String   manName;           
	private String   manPhone;
	private String   manUser;
	private String   manPassword;
	private int   manPower;
	private DataDictionary   systemMessage;
	public String getManName() {
		return manName;
	}
	public String getManPhone() {
		return manPhone;
	}
	public String getManUser() {
		return manUser;
	}
	public String getManPassword() {
		return manPassword;
	}
	public int getManPower() {
		return manPower;
	}
	public void setManName(String manName) {
		this.manName = manName;
	}
	public void setManPhone(String manPhone) {
		this.manPhone = manPhone;
	}
	public void setManUser(String manUser) {
		this.manUser = manUser;
	}
	public void setManPassword(String manPassword) {
		this.manPassword = manPassword;
	}
	public void setManPower(int manPower) {
		this.manPower = manPower;
	}


	@Override
	public String toString() {
		return "Manager [uuid=" + getUuid() + ", manName=" + manName + ", manPhone=" + manPhone + ", manUser=" + manUser + ", manPassword="
				+ manPassword + ", manPower=" + manPower + ", systemMessage=" +  systemMessage + "]";
	}
	public DataDictionary getSystemMessage() {
		return systemMessage;
	}
	public void setSystemMessage(DataDictionary systemMessage) {
		this.systemMessage = systemMessage;
	}
	
	
	
}
