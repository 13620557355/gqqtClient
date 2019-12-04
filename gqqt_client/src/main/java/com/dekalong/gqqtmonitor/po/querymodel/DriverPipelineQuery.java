/**
 * Copyright 2019 TPRI. All Rights Reserved.
 */
package com.dekalong.gqqtmonitor.po.querymodel;

import java.io.Serializable;
import java.math.BigDecimal;

import com.dekalong.gqqtmonitor.po.DriverPipeline;

/**
 * <B>概要说明：此对象需要序列化，因为需要通过网络传输</B><BR>
 * @author Long（Long）
 * @since 2019年1月4日
 * 
 */
public class DriverPipelineQuery  extends DriverPipeline  implements Serializable{
    
	    /**  */
	private static final long serialVersionUID = 1L;
		//查询参数
	    private String custQueryUuid;	
	    //接收的iot参数 
		private double iotParamLeft=0;
		private double iotParamRight=0;
		private double iotParamComm=0;
		private int  driValveLeft;
		private int  driValveRight;
		private int onLineStatus=0;//是否在线，1表示在线，0表示不在线
		private long lastTime=0;
		private int  leftSMSFullNotifly=0;
		private int  rightSMSFullNotifly=0;
		private int  leftSMSEmptyNotifly=1;
		private int  rightSMSEmptyNotifly=1;
		
		private boolean clientServiceConnStatus=true;
        private long clientServiceConnLastTime=0;
        private int faultStatistics=0;       //故障统计数，设定此数值时将会丢弃部分数据
        private String faultIdentifier="not";//故障标识符，用于发现电磁阀或继电器硬件故障
        
		public String getCustQueryUuid() {
			return custQueryUuid;
		}
		public double getIotParamLeft() {
			BigDecimal b = new  BigDecimal(iotParamLeft); 
			double   b1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();  
			return b1;
		}
		public double getIotParamRight() {
			BigDecimal b = new  BigDecimal(iotParamRight); 
			double   b1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();  
			return b1;
		}
		public double getIotParamComm() {
			BigDecimal b = new  BigDecimal(iotParamComm); 
			double   b1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();  
			return b1;
		}
		public void setCustQueryUuid(String custQueryUuid) {
			this.custQueryUuid = custQueryUuid;
		}
		public void setIotParamLeft(double iotParamLeft) {
			BigDecimal b = new  BigDecimal(iotParamLeft); 
			double   b1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();  
			this.iotParamLeft = b1;
		}
		public void setIotParamRight(double iotParamRight) {
			BigDecimal b = new  BigDecimal(iotParamRight); 
			double   b1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();  
			this.iotParamRight = b1;
		}
	
		public void setIotParamComm(double iotParamComm) {
			BigDecimal b = new  BigDecimal(iotParamComm); 
			double   b1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();  
			this.iotParamComm = b1;
		}
	
		public int getOnLineStatus() {
			return onLineStatus;
		}
		public void setOnLineStatus(int onLineStatus) {
			this.onLineStatus = onLineStatus;
		}
		public long getLastTime() {
			return lastTime;
		}
		public void setLastTime(long lastTime) {
			this.lastTime = lastTime;
		}
		
		public int getDriValveLeft() {
			return driValveLeft;
		}
		public int getDriValveRight() {
			return driValveRight;
		}
		public void setDriValveLeft(int driValveLeft) {
			this.driValveLeft = driValveLeft;
		}
		public void setDriValveRight(int driValveRight) {
			this.driValveRight = driValveRight;
		}
		public int getLeftSMSFullNotifly() {
			return leftSMSFullNotifly;
		}
		public int getRightSMSFullNotifly() {
			return rightSMSFullNotifly;
		}
		public int getLeftSMSEmptyNotifly() {
			return leftSMSEmptyNotifly;
		}
		public int getRightSMSEmptyNotifly() {
			return rightSMSEmptyNotifly;
		}
		public void setLeftSMSFullNotifly(int leftSMSFullNotifly) {
			this.leftSMSFullNotifly = leftSMSFullNotifly;
		}
		public void setRightSMSFullNotifly(int rightSMSFullNotifly) {
			this.rightSMSFullNotifly = rightSMSFullNotifly;
		}
		public void setLeftSMSEmptyNotifly(int leftSMSEmptyNotifly) {
			this.leftSMSEmptyNotifly = leftSMSEmptyNotifly;
		}
		public void setRightSMSEmptyNotifly(int rightSMSEmptyNotifly) {
			this.rightSMSEmptyNotifly = rightSMSEmptyNotifly;
		}

		public boolean isClientServiceConnStatus() {
			return clientServiceConnStatus;
		}
		public void setClientServiceConnStatus(boolean clientServiceStatus) {
			this.clientServiceConnStatus = clientServiceStatus;
		}
		public long getClientServiceConnLastTime() {
			return clientServiceConnLastTime;
		}
		public void setClientServiceConnLastTime(long clientServiceConnLastTime) {
			this.clientServiceConnLastTime = clientServiceConnLastTime;
		}
		@Override
		public String toString() {
			return "DriverPipelineQuery ["+super.toString() +", custQueryUuid=" + custQueryUuid + ", iotParamLeft=" + iotParamLeft
					+ ", iotParamRight=" + iotParamRight + ", iotParamComm=" + iotParamComm + ", driValveLeft="
					+ driValveLeft + ", driValveRight=" + driValveRight + ", onLineStatus=" + onLineStatus
					+ ", lastTime=" + lastTime + ", leftSMSFullNotifly=" + leftSMSFullNotifly + ", rightSMSFullNotifly="
					+ rightSMSFullNotifly + ", leftSMSEmptyNotifly=" + leftSMSEmptyNotifly + ", rightSMSEmptyNotifly="
					+ rightSMSEmptyNotifly + ", clientServiceConnStatus=" + clientServiceConnStatus
					+ ", clientServiceConnLastTime=" + clientServiceConnLastTime + "]";
		}
		public int getFaultStatistics() {
			return faultStatistics;
		}
		public void setFaultStatistics(int faultStatistics) {
			this.faultStatistics = faultStatistics;
		}
		public String getFaultIdentifier() {
			return faultIdentifier;
		}
		public void setFaultIdentifier(String faultIdentifier) {
			this.faultIdentifier = faultIdentifier;
		}
}
