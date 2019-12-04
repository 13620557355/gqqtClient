/**
 * Copyright 2019 TPRI. All Rights Reserved.
 */
package com.dekalong.gqqtmonitor.iotsource.function.busbar.selfkeep.hexoper;


import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import org.springframework.stereotype.Service;
import com.dekalong.gqqtmonitor.initmodel.InitAppModel;
import com.dekalong.gqqtmonitor.iotsource.function.busbar.selfkeep.modbusutil.BinaryConversion;
import com.dekalong.gqqtmonitor.iotsource.function.busbar.selfkeep.modbusutil.CRC16Util;
import com.dekalong.networtdevice.datadown.DataDownExecutor;
import com.dekalong.networtdevice.po.DeviceTransDataAck;

/**
 * <B>概要说明：远程控制电磁阀，modbus十六进制命令版本！</B><BR>
 * @author Long（Long）
 * @since 2019年7月23日
 * 
 */
@Service
public class HexCmdOperImpl implements IHexCmdOper {
     
	 //此同步哈希表用于获取返回的修改状态，保存的相关的数组信息
	public static final Map<Long,DeviceTransDataAck> transDataAckMap=new ConcurrentHashMap<Long,DeviceTransDataAck>();
	
	/**
	 * <B>方法名称：获取所有继电器状态</B><BR>
	 * <B>概要说明：</B><BR>
	 * @see com.dekalong.gqqtmonitor.iotsource.function.busbar.IHexCmdOper#getAllRelayStatus(com.dekalong.gqqtmonitor.po.querymodel.DriverPipelineQuery)
	 */
	@Override
	public String getAllRelayStatus(String relayAddr,int iotAddr) {
		
		String hexData=HexCmdGenerate.getQueryRSHexCmd(relayAddr);
		try {
			Thread.sleep(100);   //必须休眠才能真正发送成功，才有数据回传
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		boolean isTransSuccess=DataDownExecutor.dataTrans(iotAddr, hexData);
		
		if(isTransSuccess) {
			String currentTime=String.valueOf(System.currentTimeMillis());
			//时间，IOT地址，16进制命令
			return validateData(new String[]{currentTime,String.valueOf(iotAddr),hexData});
		}
		return null;
	}

	
	private String validateData(String[] condition) {
           
			Future<String> result = (Future<String>) InitAppModel.threadPool.submit(new Callable<String>() {    		
	    		@Override
	    		public String call() throws Exception {
	    			long pastTime=Long.valueOf(condition[0]);
					int iotAddr=Integer.valueOf(condition[1]);
					String hexString=condition[2];
					int timeout=5000;
	    			boolean isExit=false;
	    			boolean operating=false;//防止重复进入
	    			while (!isExit) {
	    				long tenseTime=System.currentTimeMillis();
	    				if(tenseTime-pastTime>timeout) {//5秒内没返回数据的话
	    					isExit=true;
	    					return null;
	    				}
	    				try {
	    					if(!transDataAckMap.isEmpty()&&!operating) {
	    		    			for (long time :transDataAckMap.keySet()) {
	    		    				if(!(tenseTime-time>timeout)) {
	    		    					DeviceTransDataAck dataAck=transDataAckMap.get(time);
	        		    				String data=dataAck.getData().replaceAll(" ", "");
	    		    					if(iotAddr==dataAck.getDeviceId()&&
	    		    						data.substring(0, 4)  //十六进制前四位要相等，也就是地址位和功能码位
	    		    					   .equals(hexString.substring(0,4))) {
	    		    						if(validate(data)) {
	    		    							    isExit=true;
	    		    							    operating=true;
	        		    							transDataAckMap.remove(time);
	        		    							return getByteString(data);
	        		    					}
	    		    					}
	    		    				}else {
	    		    					transDataAckMap.remove(time);
	    		    			    }
	    		    				
								}
	    		    			Thread.sleep(100);
			    		    }else { //哈希表没有元素时休眠，防止过快遍历
	    						Thread.sleep(100);
	    		            }
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
	    			return null;
	    		}
	    		/**
	    		 * 
	    		 * <B>方法名称：把16进制字符串分解成字符串</B><BR>
	    		 * <B>概要说明：0100000000000010，0代表此继电器不通电，1代表通电</B><BR>
	    		 * @param s
	    		 * @return
	    		 */
	    		private String getByteString(String s) {
	    			s=s.replaceAll(" ", "");
	    			String realTimeHexRelay=s.substring(6, s.length()-4
	    					);//得到继电器的十六进制数据，再转二进制
	    			if(realTimeHexRelay.length()==4) {
	    				String onebyte=realTimeHexRelay.substring(0, 2);//获取第一个字节数据，如0102中的01
	    				BinaryConversion.getTurnBinary(onebyte);
	    				String twobyte=realTimeHexRelay.substring(2, 4);
	    				return BinaryConversion.getTurnBinary(onebyte)+BinaryConversion.getTurnBinary(twobyte);
	    			}else {
	    				String onebyte=realTimeHexRelay.substring(0, 2);
	    				return BinaryConversion.getTurnBinary(onebyte)+"00000000";
	    			}
	    		}
		   });
			
			try {
				return result.get();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	  
	    } 
	
	
	
	/**
	 * <B>方法名称：远程透传控制电磁阀</B><BR>
	 * <B>概要说明：</B><BR>
	 * @see com.dekalong.gqqtmonitor.iotsource.function.busbar.hexoper.IHexCmdOper#transCtrlRelay(int, int, boolean)
	 */
	@Override
	public boolean transCtrlRelay(int iotAddr,int relayModbusAddr,int relayID, boolean isPulse,String openOrClose) {
		try {
			String currentTime=String.valueOf(System.currentTimeMillis());
			if(!isPulse) {
				String hexData= HexCmdGenerate.getTransCtrlHexCmd(openOrClose, relayModbusAddr, relayID);				
				Thread.sleep(100);
				boolean isTransSuccess=DataDownExecutor.dataTrans(iotAddr, hexData);
				if(isTransSuccess) {
					//时间，IOT地址，16进制命令
					return validateTransCtrlStatus(new String[]{
							currentTime,
							String.valueOf(iotAddr),
							hexData,
					        String.valueOf(relayModbusAddr),

				    });
				}
			}else {
				String hexData=HexCmdGenerate.getTransCtrlInstantHexCmd(relayModbusAddr,relayID);
				Thread.sleep(100);
				boolean isTransSuccess=DataDownExecutor.dataTrans(iotAddr, hexData);
				if(isTransSuccess) {
					//时间，IOT地址，16进制命令
					 return validateTransCtrlStatus(new String[]{
							currentTime,
							String.valueOf(iotAddr),
							hexData,
					        String.valueOf(relayModbusAddr),
				    });
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * <B>方法名称：modbus校验，防止网络乱码或篡改,0000表示此modbus指令正确</B><BR>
	 * <B>概要说明：</B><BR>
	 * @param s
	 * @return
	 */
	private  boolean validate(String s) {
		return CRC16Util.getCrc16(s).equals("0000");
	}
	

	
	/**
	 * 
	 * <B>方法名称：验证修改返回的状态</B><BR>
	 * <B>概要说明：</B><BR>
	 * @param condition
	 * @return
	 */
	private boolean validateTransCtrlStatus(String[] condition) {
		Future<Boolean> result = (Future<Boolean>) InitAppModel.threadPool.submit(new Callable<Boolean>() {
    		
    		@Override
    		public Boolean call() throws Exception {
    			long pastTime=Long.valueOf(condition[0]);
				int iotAddr=Integer.valueOf(condition[1]);
				String hexString=condition[2];
				String relayAddr=(condition[3]);
				relayAddr=relayAddr.length()==1?"0"+relayAddr:relayAddr;
				int timeout=10000;
    			boolean isExit=false;
    			boolean operating=false;//防止重复进入
    			while (!isExit) {
    				long tenseTime=System.currentTimeMillis();
    				if(tenseTime-pastTime>timeout) {//10秒内没返回数据的话
    					isExit=true;
    					return false;
    				}
    				try {
    					if(!transDataAckMap.isEmpty()&&!operating) {
    		    			for (long time :transDataAckMap.keySet()) {
    		    				if(!(tenseTime-time>timeout)) {
    		    					DeviceTransDataAck dataAck=transDataAckMap.get(time);
        		    				String data=dataAck.getData().replaceAll(" ", "");
    		    					if(iotAddr==dataAck.getDeviceId()&&
    		    						data.substring(0, 4)  //十六进制前四位要相等，也就是地址位和功能码位
    		    					   .equals(hexString.substring(0,4))) {
    		    						if(validate(data)) {
    		    							transDataAckMap.remove(time);
    		    							isExit=true;
    		    							operating=true;
    		    							return true;
    		    						}
    		    					}
    		    				}else {
    		    					transDataAckMap.remove(time);
    		    			    }
    		    				
    						}
    		    			Thread.sleep(100);
    	    		    }else { //无界队列大小为0就休眠，防止过快遍历
    		    			try {
    							Thread.sleep(100);
    						} catch (InterruptedException e) {
    							e.printStackTrace();
    						}
    		            }
					} catch (Exception e) {
						
					}
	    		    
				}
    			return false;
    		}
    	
	   });
		
		try {
			return result.get();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	public static void main(String[] args) {
//		 System.out.println("22010200407DCB".substring(5,6));
	}
	
	
}
