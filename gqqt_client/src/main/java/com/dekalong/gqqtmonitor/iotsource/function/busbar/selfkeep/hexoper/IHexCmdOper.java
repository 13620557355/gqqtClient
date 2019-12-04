/**
 * Copyright 2019 TPRI. All Rights Reserved.
 */
package com.dekalong.gqqtmonitor.iotsource.function.busbar.selfkeep.hexoper;

/**
 * <B>概要说明：</B><BR>
 * @author Long（Long）
 * @since 2019年7月23日
 * 
 */
public interface IHexCmdOper {
	public String getAllRelayStatus(String relayAddr,int iotAddr);
	public boolean transCtrlRelay(int iotAddr,int relayModbusAddr,int relayID,boolean isPulse,String openOrClose);
}
