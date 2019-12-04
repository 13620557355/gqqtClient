/**
 * Copyright 2019 TPRI. All Rights Reserved.
 */
package com.dekalong.gqqtmonitor.po.querymodel;


import com.dekalong.gqqtmonitor.po.Busbar;

/**
 * <B>概要说明：此对象需要序列化，因为需要通过网络传输</B><BR>
 * @author Long（Long）
 * @since 2019年1月4日
 * 
 */
public class BusbarQuery  extends Busbar {
    
	    /**  */
	private static final long serialVersionUID = 1L;
	
		private int  driValveLeft;
		private int  driValveRight;

		
		private boolean clientServiceConnStatus=true;
        private long clientServiceConnLastTime=0;
        private int faultStatistics=0;       //故障统计数，设定此数值时将会丢弃部分数据
        private String faultIdentifier="not";//故障标识符，用于发现电磁阀或继电器硬件故障
        
		

		
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
		@Override
		public String toString() {
			return "BusbarQuery ["+super.toString()+", driValveLeft=" + driValveLeft + ", driValveRight=" + driValveRight
					+ ", clientServiceConnStatus=" + clientServiceConnStatus + ", clientServiceConnLastTime="
					+ clientServiceConnLastTime + ", faultStatistics=" + faultStatistics + ", faultIdentifier="
					+ faultIdentifier + "]";
		}
		@Override
		public void setIotParamLeft(double iotParamLeft) {
			this.iotParamLeft=iotParamLeft;
			this.comparValue=getComparValue();
			super.setComparValue(comparValue);
		}
		@Override
		public void setIotParamRight(double iotParamRight) {
			this.iotParamRight=iotParamRight;
			this.comparValue=getComparValue();
			super.setComparValue(comparValue);
		}
		@Override
		public void setIotParamComm(double iotParamComm) {
			this.iotParamComm=iotParamComm;
			this.comparValue=getComparValue();
			super.setComparValue(comparValue);
		}
	  /**
	   * 左右两边值，每边100%为满，相加最大值为200%，省去%号，越接近0为越小，越接近200为越大！
	   * @return返回比较值
	   */
		@Override
		public int getComparValue () {
			double left=getIotParamLeft()/(getDriGasPressure()/100);
			left=left>100?100:left;
			double right=getIotParamRight()/(getDriGasPressure()/100);
			right=left>100?100:right;
			
			return new Long((int)Math.round(left+right)).intValue();
		}
		
		
}
