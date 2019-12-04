/**
 * Copyright 2019 TPRI. All Rights Reserved.
 */
package com.dekalong.gqqtmonitor.iotsource.function.busbar.selfkeep;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dekalong.gqqtmonitor.initmodel.InitAppModel;
import com.dekalong.gqqtmonitor.iotsource.function.busbar.ISelfKeepValveOpertion;
import com.dekalong.gqqtmonitor.iotsource.function.busbar.IValveExecuteOpertion;
import com.dekalong.gqqtmonitor.websocket.WebSocketDataHandler;
import com.dekalong.gqqtmonitor.po.querymodel.CustomerQuery;
import com.dekalong.gqqtmonitor.po.querymodel.BusbarQuery;


/**
 * <B>概要说明：</B><BR>
 * @author Long（Long）
 * @since 2019年4月15日
 * 
 */
@Service
public class ValveSelfKeepImpl implements IValveExecuteOpertion{
	
	@Autowired
	private WebSocketDataHandler notifyWebSocket;
    @Autowired
    private ISelfKeepValveOpertion hexOper;
	//此队列用于电磁阀关闭是否成功的遥控应答。Long 时间戳，String：执行的操作（关或开）。Integer：客户IOT地址，String节点id
		
		public static final int TIMEOUT=8000;//超时时间，逾期无应答将记录异常
		
		/**
		 * <B>方法名称：电磁阀手动切换实现</B><BR>
		 * <B>概要说明：</B><BR>
		 * @see com.dekalong.gqqtmonitor.service.BusbarService#manualValveSwicth(com.dekalong.gqqtmonitor.po.querymodel.BusbarQuery)
		 */
		@Override
	    public  boolean manualExecuteValve(BusbarQuery dpq) {
			String duplicateRequestID=dpq.getDriIotAddr()+"|"+dpq.getDriIdLeft();
			InitAppModel.duplicateRequest.add(duplicateRequestID);//添加正在执行信息，防止重复执行
			Future<Boolean> result = (Future<Boolean>) InitAppModel.threadPool.submit(new Callable<Boolean>() {	    		
		    		@Override
		    		public Boolean call() throws Exception {
		    			return manualExecute(dpq);
		    		}
			 });
				try {
					return result.get();
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}finally {
					InitAppModel.duplicateRequest.remove(duplicateRequestID);
				}
		    } 
	
		private boolean manualExecute(BusbarQuery dpq) {
			BusbarQuery busbar=null;
			for (CustomerQuery cq : InitAppModel.customerList) {
				if(dpq.getCustUuid().getUuid().equals(cq.getUuid())) {
					for (BusbarQuery driverPipelineQuery : cq.getBusbarList()) {
						if(driverPipelineQuery.getUuid().equals(dpq.getUuid())) {
							busbar=driverPipelineQuery;
						}
					}
				}
			}
			
			if(!(dpq.getDriValveIdRight()==999)) {
				if(busbar!=null) {
					int oldLeftValve=busbar.getDriValveLeft();
					int oldRightValve=busbar.getDriValveRight();
					int newLeftValve=dpq.getDriValveLeft();
					int newRightValve=dpq.getDriValveRight();
					if(oldLeftValve!=newLeftValve||oldRightValve!=newRightValve) {
						if(newLeftValve==1||newRightValve==1) {
							boolean leftIsSucc=true;
							boolean rightIsSucc=true;
							if(oldLeftValve!=newLeftValve) {
								if(newLeftValve==0) {
									leftIsSucc=colseValve(busbar, "left");
									return leftIsSucc;
								}else {
									leftIsSucc=openValve(busbar, "left");
									if(oldRightValve==1) {
										rightIsSucc=colseValve(busbar, "right");
									}
									return (leftIsSucc&&rightIsSucc)?true:false;
								}
							}
	                         if(oldRightValve!=newRightValve) {
								if(newRightValve==0) {
									rightIsSucc=colseValve(busbar, "right");
									return rightIsSucc;
								}else {
									rightIsSucc=openValve(busbar, "right");
									if(oldLeftValve==1) {
										leftIsSucc=colseValve(busbar, "left");
									}
									return (leftIsSucc&&rightIsSucc)?true:false;
								}
							}
						}else {
							boolean leftIsSucc=true;
							boolean rightIsSucc=true;
							if(oldLeftValve==1) {
								leftIsSucc=colseValve(busbar, "left");
							}
							if(oldRightValve==1) {
								rightIsSucc=colseValve(busbar, "right");
							}
							return (leftIsSucc&&rightIsSucc)?true:false;
						}
					}
				}
				return false;
			}else { //只有一个电磁阀是手动执行
				boolean leftIsSucc=true;
				if(dpq.getDriValveLeft()==0) {
					leftIsSucc=colseValve(busbar, "left");
				}else if(dpq.getDriValveLeft()==1) {
					leftIsSucc=openValve(busbar, "left");
				}
				return leftIsSucc;
			}
		}
		
	  
		@Override
		public boolean onlyOneValveSwicth(BusbarQuery dpq) {		
				double alarm=dpq.getDriAlarm();
				double leftparm=dpq.getIotParamLeft();
				int leftValveStatus=dpq.getDriValveLeft();
			    if(leftValveStatus==1&&leftparm<=alarm) {
					boolean isSuccess=colseValve(dpq, "left");
					return  isSuccess;	
				}
			   if(leftValveStatus==0&&leftparm>alarm) {
					boolean isSuccess=openValve(dpq, "left");
					return isSuccess;
				}  
			return false;
		}	
	/**
	 * 
	 * <B>方法名称：自动电磁阀实现</B><BR>
	 * <B>概要说明：</B><BR>
	 * @param dpq
	 * @param leftRelayStatus
	 * @param rightRelayStatus
	 * @return
	 */
	@Override
	public  boolean autoValveSwicth(BusbarQuery dpq) {         		
		        
		       double  driAlarm=dpq.getDriAlarm();
	    	   double leftPar=dpq.getIotParamLeft();
	           double rightPar=dpq.getIotParamRight();
	        	//继电器实时状态
	        	int valveLeft=dpq.getDriValveLeft();
	        	int valveRight=dpq.getDriValveRight();
	        	
//	        	if(!inspectCommManometer(dpq)) { //故障压力变送器检测。
//	        		return false;
//	        	}	        

//            
        		//如果电磁阀同时处于关闭状态，根据压力调整电磁阀。
            	if(valveLeft==0&&valveRight==0) {
            		if(leftPar>driAlarm&&rightPar>driAlarm) {
            			if(leftPar>rightPar) {
            				return  openValve( dpq,"right");
            			}else {
            				return  openValve( dpq,"left");
            			}
            		}
            		if(leftPar<=driAlarm&&rightPar>driAlarm) {
            			return  openValve( dpq,"right");
            		}
                    if(leftPar>driAlarm&&rightPar<=driAlarm) {              
                    	return   openValve( dpq,"left");
            		}
                    return false;
            	} 
	                 
        		//如果电磁阀同时处于开启状态。
            	if(valveLeft==1&&valveRight==1) {
            		if(leftPar<=driAlarm&&rightPar<=driAlarm) {//如果左右两侧压力低于警告值，全部关闭
        				colseValve( dpq,"all");
        				return  false;
           		    }
            		if(leftPar>driAlarm&&rightPar>driAlarm) {//如果左右侧压力正常，则默认关闭左电磁阀。
            			if(leftPar>rightPar) {
            				colseValve( dpq,"right");
            			}else {
            				colseValve( dpq,"left");
            			}
            			return  false;
            		}
            		if(leftPar<=driAlarm&&rightPar>driAlarm) {//如果左侧压力低于警告值，右边正常，关闭左侧
            			colseValve( dpq,"left");
            			return  false;
            		}
                    if(leftPar>driAlarm&&rightPar<=driAlarm) {//如果右侧压力低于警告值，左边正常，关闭右侧
                    	colseValve( dpq,"right");
                    	return  false;
            		}
                    return false;
            	}
            	//两边电磁阀只有一个打开的时候
            	if((valveLeft==1&&valveRight==0)||(valveLeft==0&&valveRight==1)) {
            		if(valveLeft==1&&leftPar<=driAlarm) {//关闭左侧，开启右侧电磁阀
            			boolean isSuccessSwicth =true;
            			boolean isColse=true;
            			boolean isOpen=true;
            			if(rightPar>driAlarm) {
            				isOpen=openValve(dpq, "right");
            			}
            			if(isOpen) {
            				isColse=colseValve(dpq, "left");
            			}
            			if(!isColse||!isOpen) {  //只有同时成功时才返回成功，否则说明出异常
            				isSuccessSwicth =false;
            			}
            			return isSuccessSwicth;
                	}else if(valveRight==1&&rightPar<=driAlarm) {//关闭右侧，开启左侧电磁阀
                		boolean isSuccessSwicth =true;
            			boolean isColse=true;
            			boolean isOpen=true;
            			if(leftPar>driAlarm) {
            				isOpen=openValve(dpq, "left");
            			}
            			if(isOpen) {
            				isColse=colseValve(dpq, "right");
            			}
            			
            			if(!isColse||!isOpen) {  //只有同时成功时才返回成功，否则说明出异常
            				isSuccessSwicth =false;
            			}
                		return isSuccessSwicth;
                	}
            	}
    	return false;
	}
	/**
	 * 
	 * <B>方法名称：检查故障检测压力变送器</B><BR>
	 * <B>概要说明：</B><BR>
	 */
	private static final double commAlarm=0.2;
	private boolean inspectCommManometer(BusbarQuery dpq) {
		 double  driAlarm=commAlarm;
  	     double leftPar=dpq.getIotParamLeft();
         double rightPar=dpq.getIotParamRight();
         double commPar=dpq.getIotParamComm();
      	//继电器实时状态
      	int valveLeft=dpq.getDriValveLeft();
      	int valveRight=dpq.getDriValveRight();
      	int fs=dpq.getFaultStatistics();
      	if(fs<0) {dpq.setFaultStatistics(0);fs=0;}
		if(fs>0) {//故障值不为0,直接放弃本条数据
			if(fs==1) { //等于1时，做硬件故障检测
				if(faultIdentifierValidate(dpq)) {
					dpq.setFaultStatistics(0);
					return true;
				}else {
					return false;
				}
			}
			dpq.setFaultStatistics(fs-1);//如果校验失败,不减1
    		return false;
    	}
    	//电磁阀有一侧打开并且气体足够时，故障压力变送器低于警告值，说明电磁阀故障！
    	if(commPar<driAlarm&&((leftPar>driAlarm)||(rightPar>driAlarm))) {
    		dpq.setFaultStatistics(3);
    		int leftCommPoleID=Integer.valueOf(String.valueOf(dpq.getDriValveIdLeft()).substring(7,9));
    		int rightCommPoleID=Integer.valueOf(String.valueOf(dpq.getDriValveIdRight()).substring(7,9));
    	    if(valveLeft==1&&valveRight==0){
    	    	hexOper.commPoleOper(dpq, leftCommPoleID);
    			 dpq.setFaultIdentifier("openLeft");
    		}else if(valveRight==1&&valveLeft==0){
    			hexOper.commPoleOper(dpq, rightCommPoleID);
    			dpq.setFaultIdentifier("openRight");
    		}else {
    			if(leftPar<rightPar) {
    			hexOper.commPoleOper(dpq, leftCommPoleID);
   				 int clRight=Integer.valueOf(String.valueOf(dpq.getDriValveIdRight()).substring(3,5));
   				hexOper.closePoleOper(dpq, clRight);
   				 dpq.setFaultIdentifier("openLeft");
   			    }else {
   			    hexOper.commPoleOper(dpq, rightCommPoleID);
   				 int clLeft=Integer.valueOf(String.valueOf(dpq.getDriValveIdLeft()).substring(3,5));
   				hexOper.closePoleOper(dpq,clLeft);
   				 dpq.setFaultIdentifier("openRight");
				}
    		}
    		return false;
    	}
    	dpq.setFaultIdentifier("not");
    	return true;
	}
	/**
	 * 
	 * <B>方法名称：验证是否开启成功，不成功的话代表硬件故障</B><BR>
	 * <B>概要说明：</B><BR>
	 * @param dpq
	 * @return
	 */
	private boolean faultIdentifierValidate(BusbarQuery dpq) {
		 double  driAlarm=commAlarm;
  	     double leftPar=dpq.getIotParamLeft();
         double rightPar=dpq.getIotParamRight();
         double commPar=dpq.getIotParamComm();
        int valveLeft=dpq.getDriValveLeft();
       	int valveRight=dpq.getDriValveRight();
		if(dpq.getFaultIdentifier().equals("openLeft")) {
			if(commPar>driAlarm&&((leftPar>driAlarm&&valveLeft==1))) {
				return true;
			}else {
				hexOper.valveOper(dpq,"right","open");
				dpq.setFaultIdentifier("openRight");
				dpq.setFaultStatistics(3);
				return false;
			}
		}else if(dpq.getFaultIdentifier().equals("openRight") ){
			if(commPar>driAlarm&&((rightPar>driAlarm&&valveRight==1))) {
				return true;
			}else {
				hexOper.valveOper(dpq,"left","open");
				dpq.setFaultIdentifier("openLeft");
				dpq.setFaultStatistics(3);
				return false;
			}
		}
		return true;
	}
	/**
	 * <B>方法名称：只有一个电磁阀时执行的开启和关闭操作</B><BR>
	 * <B>概要说明：</B><BR>
	 * @see com.dekalong.gqqtmonitor.service.BusbarService#onlyOneValveSwicth()
	 */
	

	
	/**
	 * 
	 * <B>方法名称：远程关闭电磁阀</B><BR>
	 * <B>概要说明：</B><BR>
	 * @param dpq
	 * @param name
	 * @return
	 */
	private boolean colseValve(BusbarQuery dpq,String name) {       //远程控制电磁阀
		boolean isAuto=true;
		
		if(dpq.getDriAuto()==1) {
			dpq.setDriAuto(0);
		}else {
			isAuto=false;
		}
		boolean isSuccess=hexOper.valveOper(dpq,name,"close");
		if(isSuccess) {
			if(name.equals("all")) {
				dpq.setDriValveLeft(0);
				dpq.setDriValveRight(0); 
			}else if(name.equals("left")) {
				dpq.setDriValveLeft(0);
			}else if(name.equals("right")) {
				dpq.setDriValveRight(0);
			}
			notifyWebSocket.sendNotifyDevice(dpq.getUuid());
			 if(isAuto) {dpq.setDriAuto(1);}
			return true;
		}else {
			notifyWebSocket.sendNotifyDevice(dpq.getUuid());
			 if(isAuto) {dpq.setDriAuto(1);}
    		return false;
		}
	
	}
	
	
	/**
	 * 
	 * <B>方法名称：远程打开电磁阀</B><BR>
	 * <B>概要说明：</B><BR>
	 * @param dpq
	 * @param name
	 * @return
	 *   
	 */
	private boolean openValve(BusbarQuery dpq,String name) {
		
		boolean isAuto=true;
		if(dpq.getDriAuto()==1) {
			dpq.setDriAuto(0);
		}else {
			isAuto=false;
		}
		boolean isSuccess=hexOper.valveOper(dpq,name,"open");
		if(isSuccess) {
			if(name.equals("left")) {
				dpq.setDriValveLeft(1);
			}else if(name.equals("right")) {
				dpq.setDriValveRight(1);
			}
			notifyWebSocket.sendNotifyDevice(dpq.getUuid());
			 if(isAuto) {dpq.setDriAuto(1);}
			return true;
		}else {
			notifyWebSocket.sendNotifyDevice(dpq.getUuid());
			 if(isAuto) {dpq.setDriAuto(1);}
    		return false;
		}
	}

}
	

