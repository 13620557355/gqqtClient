
package com.dekalong.gqqtmonitor.iotsource.function.busbar.selfkeep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.dekalong.gqqtmonitor.iotsource.function.busbar.selfkeep.hexoper.IHexCmdOper;
import com.dekalong.gqqtmonitor.iotsource.function.busbar.IBusbarDataExecute;
import com.dekalong.gqqtmonitor.iotsource.function.busbar.ISelfKeepValveOpertion;
import com.dekalong.gqqtmonitor.iotsource.function.busbar.IValveExecuteOpertion;
import com.dekalong.gqqtmonitor.po.DeviceModel;
import com.dekalong.gqqtmonitor.po.querymodel.BusbarQuery;
import com.dekalong.gqqtmonitor.util.regexutil.StrRegex;

@Service
public class BusbarSelfKeepImpl implements IBusbarDataExecute{
      

    @Autowired
    private ISelfKeepValveOpertion hexOper;
  
    @Autowired
    @Qualifier("valveSelfKeepImpl")
    private IValveExecuteOpertion valveSelfKeep;
	@Autowired
	private IHexCmdOper hexCmdOper;
	
	private boolean isValveFault=false;
	/**
	 * 
	 * <B>概要说明：</B><BR>
	 * @see com.dekalong.gqqtmonitor.iotsource.IotDataExecuteInterface#executeOperation(com.jnrsmcu.sdk.netdevice.RealTimeData)
	 */
	@Override
	public boolean executeOperation (DeviceModel dm,String oldRelayStatus,boolean isSuccess) {
		 isValveFault=false;
		 BusbarQuery dpq=null;
		 if(dm instanceof BusbarQuery) {dpq=(BusbarQuery) dm;}
		 String relayStatus=sendHexReqGetRelayStatus(dpq);
		   if(dpq!=null&&relayStatus!=null) {
				int leftStatus=getRealTimeRelayStatus(dpq.getDriValveIdLeft(),relayStatus,dpq);
				int rightStatus=getRealTimeRelayStatus(dpq.getDriValveIdRight(),relayStatus,dpq);
				if(dpq.getDriValveLeft()!=leftStatus||dpq.getDriValveRight()!=rightStatus) {
					isSuccess=true;
				}
				dpq.setDriValveLeft(leftStatus);
				dpq.setDriValveRight(rightStatus);
				if(leftStatus==1||rightStatus==1) {//如果电磁阀有一边打开，而两边都压力小于报警值的话(手动更改电磁阀,重复数据不进入，导致压力电磁阀不切换问题)
					if(leftStatus==1&&dpq.getIotParamLeft()<=dpq.getDriAlarm()) {
						isSuccess=true;
					}
					if(rightStatus==1&&dpq.getIotParamRight()<=dpq.getDriAlarm()) {
						isSuccess=true;
					}
				}
				updateValveStatus(dpq);//不能放到判断里面
				if(isSuccess&&!isValveFault) {
                      return true;
				}
		   }
		  return false;
	}

			 
			
		 /**
	     * 
	     * <B>方法名称：更新电磁阀状态</B><BR>
	     * <B>概要说明：</B><BR>
	     */
	    private void updateValveStatus(BusbarQuery dpq) {
			if(dpq.getDriAuto()==1) {
				if(!isValveFault) { //电磁阀故障，不执行之后的指令，等下一条数据再执行
					if(!(dpq.getDriValveIdRight()==999)) { //如果客户只有一个电磁阀（做电磁阀），执行另一套流程			
						valveSelfKeep.autoValveSwicth(dpq);
					}else {
						//只执行左电磁阀的流程
						valveSelfKeep.onlyOneValveSwicth(dpq);
					}	
				}	
			}
	    }
	   
    
	    /**
	     * 
	     * <B>方法名称：发送十六进制请求到继电器，获取其当前所有继电器位的状态</B><BR>
	     * <B>概要说明：</B><BR>
	     * @param dpq
	     * @return
	     */
	   private String sendHexReqGetRelayStatus(BusbarQuery dpq) {
		   if(dpq.getDriValveIdLeft()<10) {return null;};
		   String relayAddr=String.valueOf(dpq.getDriValveIdLeft()).substring(1, 3);
		   return hexCmdOper.getAllRelayStatus(relayAddr,dpq.getDriIotAddr());
	   };
	    
	    /**
		 *  
		 * <B>方法名称：根据继电器ID得到继电器状态</B><BR>
		 * <B>概要说明：100010203 电磁阀ID 1——00——01——02——03</B><BR>
		 * 第一位1无意义，00表示继电器RS485地址，建大仁科设备也无意义-01表示正极电磁阀地址，02表示负极地址，03表示公共点触点地址
		 * @param dpq
		 * @param data
		 * @return
		 */
		 private  int getRealTimeRelayStatus(int valveID,String relayStatusData,BusbarQuery dpq) {
			 String id=String.valueOf(valveID);
			 int positivePoleID=Integer.valueOf(id.substring(3,5));
			 int negativePoleID=Integer.valueOf(id.substring(5,7));
			 int commPoleID=Integer.valueOf(id.substring(7,9));
			int positivePoleStatus=StrRegex.getRelayPosition(relayStatusData, positivePoleID);
			int negativePoleStatus=StrRegex.getRelayPosition(relayStatusData, negativePoleID);
			int commPoleIDStatus=StrRegex.getRelayPosition(relayStatusData,commPoleID);//此触点为脉冲触点，不能常开			
			if(commPoleIDStatus==1) {
				hexOper.closePoleOper(dpq, commPoleID);
				
        		isValveFault=true;
			}else if(positivePoleStatus==1&&negativePoleStatus==1) {
				hexOper.valveOper(dpq, "all", "close");
				
        		isValveFault=true;
			}
			if(positivePoleStatus==1&&negativePoleStatus==0) {//正极通电，负极断电。
				return 1;
			}
			 return 0;  //正极通电，负极断电或正极断电，负极断电
		 }

}


