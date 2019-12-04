/**
 * Copyright 2019 TPRI. All Rights Reserved.
 */
package com.dekalong.gqqtmonitor.iotsource.function.busbar.selfkeep.hexoper;

import com.dekalong.gqqtmonitor.iotsource.function.busbar.selfkeep.modbusutil.BinaryConversion;
import com.dekalong.gqqtmonitor.iotsource.function.busbar.selfkeep.modbusutil.CRC16Util;

/**
 * <B>概要说明：</B><BR>
 * @author Long（Long）
 * @since 2019年7月25日
 * 
 */
public class HexCmdGenerate {
	
	/**
	 * 
	 * <B>方法名称：获取查询所有继电器位状态的指令</B><BR>
	 * <B>概要说明：</B><BR>
	 * @param relayAddr 继电器的modbus地址
	 * @return
	 */
	public  static String getQueryRSHexCmd(String relayAddr) {
		relayAddr=BinaryConversion.toHex(Integer.valueOf(relayAddr));
		String cmd=relayAddr+"01 00 00 00 10";
		cmd=cmd+CRC16Util.getCrc16(cmd);
		cmd=cmd.replaceAll(" ", "");
		return cmd;
	
	}
	
	/**
	 * 
	 * <B>方法名称：获取打开或关闭操作的modbus指令，此指令非闪开</B><BR>
	 * <B>概要说明：</B><BR>
	 * @param openOrClose  打开或者关闭
	 * @param relayModbusAddr 
	 * @param relayID  继电器位地址
	 * @return
	 */
	public static String getTransCtrlHexCmd(String openOrClose,int relayModbusAddr,int relayID) {
		String hexRelayModbusAddr=BinaryConversion.toHex(relayModbusAddr);
		String hexRelayID=BinaryConversion.toHex(relayID);
		String oper="00";
		if(openOrClose.equals("open")) {
			oper="FF00";
		}else {
			oper="0000";
		}
		String cmd=hexRelayModbusAddr+"05"+"00"+hexRelayID+oper;
		cmd=cmd+CRC16Util.getCrc16(cmd);
		cmd=cmd.replaceAll(" ", "");
		return cmd;
	}
	/**
	 * 
	 * <B>方法名称：获取闪开的modbus指令</B><BR>
	 * <B>概要说明：</B><BR>
	 * @param relayModbusAddr
	 * @param relayID
	 * @return
	 */
	public static String getTransCtrlInstantHexCmd(int relayModbusAddr,int relayID) {
		String hexRelayModbusAddr=BinaryConversion.toHex(relayModbusAddr);
		if(relayID==0) {
			relayID=3;
		}else {
			relayID=3+(relayID*5);
		}
		String hexRelayID=BinaryConversion.toHex(relayID);
		String cmd=hexRelayModbusAddr+"10"+"00"+hexRelayID+"00 02 04 00 04 00 0A";
		cmd=cmd+CRC16Util.getCrc16(cmd);
		cmd=cmd.replaceAll(" ", "");
		return cmd;
	}

}
