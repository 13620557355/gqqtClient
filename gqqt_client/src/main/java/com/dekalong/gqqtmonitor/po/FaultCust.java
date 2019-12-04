/**
 * Copyright 2019 TPRI. All Rights Reserved.
 */
package com.dekalong.gqqtmonitor.po;

import java.io.Serializable;

/**
 * <B>概要说明：</B><BR>
 * @author Long（Long）
 * @since 2019年1月4日
 * 
 */public class FaultCust  extends BaseModel implements Serializable{
   
     
	/**  */
	private static final long serialVersionUID = 1L;
	private Customer custUuid ;            
	private String   fauTime ;         
	private String   fauReason;          
	private String   fauMaintain ;   
	private String   fauResult ;         
	private String   fauPersonnel ;   
	private String   fauCustFeedback;   
	private String   fauRemarks ;
	
	public Customer getCustUuid() {
		return custUuid;
	}
	public String getFauTime() {
		return fauTime;
	}
	public String getFauReason() {
		return fauReason;
	}
	public String getFauMaintain() {
		return fauMaintain;
	}
	public String getFauResult() {
		return fauResult;
	}
	public String getFauPersonnel() {
		return fauPersonnel;
	}
	public String getFauCustFeedback() {
		return fauCustFeedback;
	}
	public String getFauRemarks() {
		return fauRemarks;
	}
	
	public void setCustUuid(Customer custUuid) {
		this.custUuid = custUuid;
	}
	public void setFauTime(String fauTime) {
		this.fauTime = fauTime;
	}
	public void setFauReason(String fauReason) {
		this.fauReason = fauReason;
	}
	public void setFauMaintain(String fauMaintain) {
		this.fauMaintain = fauMaintain;
	}
	public void setFauResult(String fauResult) {
		this.fauResult = fauResult;
	}
	public void setFauPersonnel(String fauPersonnel) {
		this.fauPersonnel = fauPersonnel;
	}
	public void setFauCustFeedback(String fauCustFeedback) {
		this.fauCustFeedback = fauCustFeedback;
	}
	public void setFauRemarks(String fauRemarks) {
		this.fauRemarks = fauRemarks;
	}
	@Override
	public String toString() {
		return "FaultCust [uuid=" + getUuid() + ", custUuid=" + custUuid + ", fauTime=" + fauTime + ", fauReason="
				+ fauReason + ", fauMaintain=" + fauMaintain + ", fauResult=" + fauResult + ", fauPersonnel="
				+ fauPersonnel + ", fauCustFeedback=" + fauCustFeedback + ", fauRemarks=" + fauRemarks + "]";
	}
	
	    
}
