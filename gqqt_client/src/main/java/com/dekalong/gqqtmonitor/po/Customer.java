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
public class Customer extends BaseModel {
    
	/**  */
	private static final long serialVersionUID = 1L;
	private String custName;
	private String  custUser; 
	private String  custPwd;
	private String  custContact; 
	private String  custPhone; 
	private String  custAddress;
	private String   custRemarks;
	private String   custCreateDate;	
	private Manager manUuid;
	private String  custManPhone;
	
	public String getCustName() {
		return custName;
	}
	public String getCustUser() {
		return custUser;
	}
	public String getCustPwd() {
		return custPwd;
	}
	public String getCustContact() {
		return custContact;
	}
	public String getCustPhone() {
		return custPhone;
	}
	public String getCustAddress() {
		return custAddress;
	}
	
	public String getCustRemarks() {
		return custRemarks;
	}
	public String getCustCreateDate() {
		return custCreateDate;
	}
	public Manager getManUuid() {
		return manUuid;
	}
	
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public void setCustUser(String custUser) {
		this.custUser = custUser;
	}
	public void setCustPwd(String custPwd) {
		this.custPwd = custPwd;
	}
	public void setCustContact(String custContact) {
		this.custContact = custContact;
	}
	public void setCustPhone(String custPhone) {
		this.custPhone = custPhone;
	}
	public void setCustAddress(String custAddress) {
		this.custAddress = custAddress;
	}

	public void setCustRemarks(String custRemarks) {
		this.custRemarks = custRemarks;
	}
	public void setCustCreateDate(String custCreateDate) {
		this.custCreateDate = custCreateDate;
	}
	public void setManUuid(Manager manUuid) {
		this.manUuid = manUuid;
	}
	@Override
	public String toString() {
		return "Customer [uuid=" + getUuid() + ", custName=" + custName + ", custUser=" + custUser + ", custPwd="
				+ custPwd + ", custContact=" + custContact + ", custPhone=" + custPhone + ", custAddress=" + custAddress
				+ ", custRemarks=" + custRemarks + ", custCreateDate=" + custCreateDate
				+ ", manUuid=" + manUuid + ", custManPhone=" + custManPhone+ "]";
	}
	public String getCustManPhone() {
		return custManPhone;
	}
	public void setCustManPhone(String custManPhone) {
		this.custManPhone = custManPhone;
	}
	
	
	
}
