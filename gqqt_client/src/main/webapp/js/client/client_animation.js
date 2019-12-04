
    /*****************************************************
     * 
     */
	var canvasCloor='#2b2b2b';
    var driUuid = $('#driUuid').val();
    var addr=getPath();
    var canvasHeight=268;
    var canvasWidth=265.4;
    var zoomInstance=1;
    var canvasCacheMap=null;
    setCurrentNUM=10;
    function resolutionAdaptor(){ //屏幕适配器
        var he=document.getElementById("aniBody").offsetHeight;
        var wi=document.getElementById("aniBody").offsetWidth;
        var pageHi=document.getElementById("pageUtil").offsetHeight;
//        var originalHeight=268,originalWidth=265.4; //原始大小：height:268;width:265.4;
        
        if(oldDeviceNum<=5){
        	setCanvasSize(oldDeviceNum);
        }else{
        	 zoomInstance=getAdjustSize(canvasWidth,canvasHeight,5,true);
        	 canvasHeight*=zoomInstance;
        	 canvasWidth*=zoomInstance;
        	 $(".canvasClass").css("top",(he-canvasHeight*2)/4);
             $("#cbox1").css("margin-left",(wi-canvasWidth*5)/2);
             $("#cbox6").css("margin-left",(wi-canvasWidth*5)/2);
        }
        function setCanvasSize(num){
        	canvasHeight=he-pageHi-10;
        	var boxtop=-10;
        	if(num>2){canvasHeight=wi/num+20;boxtop=(he-canvasHeight)/4;}
        	zoomInstance=getAdjustSize(canvasWidth,canvasHeight,num,false);
    		canvasWidth=canvasWidth*zoomInstance;
    		 $(".canvasClass").css("top",boxtop);
             $("#cbox1").css("margin-left",(wi-canvasWidth*num)/2);
        }
        function getAdjustSize(canvasWid,canvasHe,num,isOrigin){
        	if(canvasWid==0||canvasHe==0){return 0;}
        	var zoomSize=1;
        	while(true){
        		if(canvasWid*zoomSize<canvasHe){
        			zoomSize+=0.1;
        		}else{
        			if(isOrigin){return zoomSize}
        			zoomSize=zoomSize-0.1;
        			if(canvasWid*zoomSize*num>wi){
        				while(true){
        					zoomSize-=0.1;
                			if(canvasWid*zoomSize*num<wi){return zoomSize;}
                	    }
        			}
        			return zoomSize;
        		}
        	}
        }
    }
	
	function canvasCreatFactorys(wsdataArray){
		canvasCacheMap=getObjCacheMap();
		for(var i=0;i<wsdataArray.length;i++){
			var wdata=wsdataArray[i];
			var canvasName='canvas'+(i+1);
			canvasFactory(canvasName,wdata);
		}
		function canvasFactory(name,wsData){
			 var canvasObject = new fabric.Canvas(name,{
				    backgroundColor : canvasCloor,
				    width:canvasWidth,
				    height:canvasHeight,
				    selectable:false
				});
			  var comparMachiningValue=null;
			  var mediumType=null;
			  var custName=null;
			  var uuid=null;
			  if(wsData!=undefined){
				   uuid=wsData.uuid;
				   comparMachiningValue=0;
				   mediumType='';
			       custName=wsData.custUuid.custName;
				 if(wsData.onLineStatus==0){
						comparMachiningValue=0;
					}else{
						comparMachiningValue=wsData.getComparValue+1;
				 }
				 if(wsData.driType!=undefined){
					 mediumType=wsData.driType;
				 }else if(wsData.monType!=undefined){
					 mediumType=wsData.monType;
				 }
			  }
			  
			 var canvasMap=getObjCacheMap();
			 var cacheMap=getObjCacheMap();
			 var attributeCacheMap=getObjCacheMap();
			 canvasMap.set('uuid',uuid);
			 canvasMap.set('cacheMap',cacheMap);
			 canvasMap.set('canvasObject',canvasObject);
			 canvasMap.set('comparValue',comparMachiningValue);
			 canvasMap.set('mediumType',mediumType);
			 canvasMap.set('custName',custName);
			 canvasCacheMap.set(name,canvasMap);
			 if(wsData!=undefined&&wsData!=null&&uuid!=null){
				 initIndexAnimation(canvasObject,cacheMap,wsData,attributeCacheMap,true);
			 }			 
		 }
	
		 hideCacheFactorys()
		  /**隐藏盒子***/
		  function hideCacheFactorys(){ //隐藏多有没有使用的画布
				for(var i=1;i<=10;i++){
					var canvasMap=null;
					var name="canvas"+i;
					if(canvasCacheMap!=null){canvasMap=canvasCacheMap.get(name);}
		          if(canvasMap!=undefined&&canvasMap!=null){
			          	if(canvasMap.get('uuid')!=null){
			          		$("#cbox"+i).show();
			          	}
					}
				}
			}
	}
    
	function removeCacheDataFactorys(){ //删除canvas所有缓存
		for(var i=1;i<=10;i++){
			var canvasMap=null;
			if(canvasCacheMap!=null){canvasMap=canvasCacheMap.get("canvas"+i);}
            if(canvasMap!=undefined&&canvasMap!=null){
            	canvasMap.set('uuid',null);
    			var objCacheMap=canvasMap.get('cacheMap');
    			var canvas=canvasMap.get('canvasObject');
    			for(var j=0;j<Object.keys(objCacheMap).length;j++){
    				var key=Object.keys(objCacheMap)[j];
    				canvas.remove(objCacheMap.get(key));
    			}
			}
		}
	}
	function resreshCanvas(){
		removeCacheDataFactorys();
		animationDataHandler(cacheData);
	}
	function removeCanvasObject(){
		if( canvasCacheMap!=null){
			removeCacheDataFactorys();
		    initCanvasIsFirst=true;
		    canvasCacheMap=null;
		}
	}
	function reInitCanvas(){
		animationDataHandler(cacheData);
	}
	var sortDivArr=[]; //存储着div的顺序的数组
	var initCanvasIsFirst=true;
	var oldDeviceNum=-1; //用于初始化canvas的大小
  function animationDataHandler(originalWebSocketData){
	    if(isExcuteValve){return false;}
		var wdata=originalWebSocketData.rows;
		if(wdata.length!=0&&oldDeviceNum!=wdata.length){
			initCanvasIsFirst=true;
			removeCanvasObject();
		}
		oldDeviceNum=wdata.length;
		if(initCanvasIsFirst){
			  oldDeviceNum=wdata.length;
			  resolutionAdaptor();
			  initCanvasIsFirst=false;
			  canvasCreatFactorys(wdata);
		}else {
			removeCacheDataFactorys();
            for(var i=0;i<wdata.length ;i++){
				var canvasMap =canvasCacheMap.get('canvas'+(i+1));
				if(wdata[i]!=undefined&&wdata[i]!=null&&canvasMap!=undefined&&canvasMap!=null){
					var canvas=canvasMap.get('canvasObject');
					var canvasData=wdata[i];
					canvasMap.set('uuid',wdata[i].uuid);
					canvasMap.set('comparValue', getComparValue(wdata[i]));
					if(canvasMap.get('uuid')!=null){
					   initIndexAnimation(canvas,canvasMap.get('cacheMap'),canvasData);
					}
					
				}
            }
            if(wdata.length==0){return false;}
			function getComparValue(data){
				if(data.onLineStatus==0){
					return 0;
				}else {
					return data.comparValue+1;
				}
			}
	        function setCanvasInputValue(){
				for(var j=1;j<=10;j++){//检查已经描绘的对象是否已存在当前的画布中	
				  var canvasMap = canvasCacheMap.get("canvas"+j);
				  var inputid="#canvasInput"+j;
				  if(canvasMap!=undefined&&canvasMap.get('uuid')!=null){
					  if(canvasMap.get('canvasObject').onLineStatus==0){
						  $(inputid).val(0);
					  }else if(canvasMap.get('canvasObject').comparValue==0){
						  $(inputid).val(1);
					  }else{
						$(inputid).val(canvasMap.get('comparValue'));
					  }
				  }else{
					  $(inputid).val(999);
				  }
				}
	        }
			function sortDiv(){
				setCanvasInputValue();
				sortDivArr=[];
				for(var i=1;i<=10;i++){
					var inputName='#canvasInput'+i;
					 var canvasMap = canvasCacheMap.get("canvas"+i);
					if(canvasMap!=undefined&&canvasMap.get('uuid')!=null&&$(inputName).val()!=999){
						sortDivArr.push({canvasInputName:inputName,value:$(inputName).val()});
					}
				}
				sortDivArr.sort(compare('value'));
				var fillCanvasBox=[];
				for(var i=1;i<=10;i++){ //补齐盒子
					var isContain=false;
					var inputName='#canvasInput'+i;
					for(var j=0;j<sortDivArr.length;j++){
						if(sortDivArr[j].canvasInputName==inputName){
							 isContain=true;
							 break;
						}
					}
					if(!isContain){
						fillCanvasBox.push(i);
					}
				}
			   for(var i=0;i<fillCanvasBox.length;i++){ //补齐盒子,确保没有画图的盒子在最后
				   sortDivArr.push({canvasInputName:'#canvasInput'+fillCanvasBox[i],value:999});
				   $('#canvasInput'+fillCanvasBox[i]).val(999);
			   }
				for(var i=0;i<sortDivArr.length;i++){ //把元素重新插一遍
				 $("#canvasBox").append($(sortDivArr[i].canvasInputName).parent());
				}
				function compare(property){
				    return function(a,b){
				        var value1 = a[property];
				        var value2 = b[property];
				        return value1 - value2;
				    }
				}
			}
			sortDiv();/** 排序DIV */
	   }
}

/*****************/
	function getObjCacheMap(){
	var map = {  
	        set : function(key,value){this[key] = value},  
	        get : function(key){return this[key]},  
	        contains : function(key){return this.Get(key) == null?false:true},  
	        remove : function(key){delete this[key]}, 
	        getSize:function(){	return Object.keys(this).length-6}
	}
	return map ;
	}
	
	
/*************************************************************************/
	var serializer = new XMLSerializer();
	var sss=0;
	var isRepeat=false;
	var isExcuteValve=false;//手动修改时的锁，不让刷新
	var cacheCanvasData=null;
function initIndexAnimation(canvas,objCacheMap,canvasData){
	    canvas.setZoom(zoomInstance);
	    isRepeat=false;
		var data=null;
		var iotParamLeftOld=null;
		var iotParamRightOld=null;
		var driValveLeftOld=null;
		var driValveRightOld=null;
		var beginInit=true;
		var lockScreenModify=true;
		var refresh=true;
		var timeID;
		initData(canvasData);
		initImage();
		initDownEven(canvas);
	
		function initDownEven(canvasObject){
			canvas.on('mouse:down', function(options) { //触发点击更改事件
				if(!isExcuteValve){
					if(options.target!=null){
						isExcuteValve=true;
						if (options.target.id=='onOrOffLeft') {
				     		executeOnOrOff('left');
				     	  }else if(options.target.id=='onOrOffRight'){
				     		executeOnOrOff('right');  
				     	  }
					}
				}
		    });
		}
		//初始化后端数据 
		function initData(rows){
			if(rows==null||rows==undefined){return false;}
			      //初始化参数
			if(rows.driType!=undefined){
				data={  
						uuid:rows.uuid,
						name:rows.pipName,
						custName:rows.custUuid.custName,
						gasType:rows.driType,
						iotParamLeft:rows.iotParamLeft,
						iotParamRight:rows.iotParamRight,
						driValveLeft:rows.driValveLeft,
						driValveRight:rows.driValveRight,   
		         		driAlarm:rows.driAlarm,
		         		driContainerType:rows.driContainerType,
		         		onLineStatus:rows.onLineStatus,
		         		driGasPressure:rows.driGasPressure,
		             	driLeftCylindersNum:rows.driLeftCylindersNum,
		             	driRightCylindersNum:rows.driRightCylindersNum
		   	  }  
			}else if(rows.monType!=undefined){
				data={  
						uuid:rows.uuid,
						name:rows.monName,
						custName:rows.custUuid.custName,
						gasType:rows.monType,
						iotParamLeft:rows.iotParamLeft,
						iotParamRight:rows.iotParamRight,
						driValveLeft:null,
						driValveRight:null,   
		         		driAlarm:rows.monAlarm,
		         		driContainerType:rows.monContainerType,
		         		onLineStatus:rows.onLineStatus,
		         		driGasPressure:rows.monGasPressure,
		             	driLeftCylindersNum:rows.monLeftCylindersNum,
		             	driRightCylindersNum:rows.monRightCylindersNum
		   	  } 
			}
		}
		
		function initImage(){
			 beginInit=true;
			 var canvasBoxHeight=document.getElementById("canvasBox").offsetHeight;
			 var canvasBoxWidth=document.getElementById("canvasBox").offsetWidth;
			 if(canvasBoxWidth==0||canvasBoxHeight==0){//用户不在此页面时,不再加载,否则抛异常
				 beginInit=false;
			     if(refresh){
			    	 refresh=false;
			    	 timeID=setInterval(initImage,1000);
			     }
			 }
			if(data!=null&&beginInit&&lockScreenModify){
				var svgDatas=[];
				refresh=true;
				clearInterval(timeID); 
				initPipeline();
				
			if(data.driContainerType=="cylinder"){initContentHint();}
			if(iotParamLeftOld==null){ //加载名称和类型
				canvas.remove(objCacheMap.get('warning'));
				canvas.remove(objCacheMap.get('deviceName'));
				canvas.remove(objCacheMap.get('customerName'));
				canvas.remove(objCacheMap.get('gType'));
				objCacheMap.remove('deviceName');
		    	objCacheMap.remove('customerName');
		    	objCacheMap.remove('gType');
				var ncolor="#FFBB00";
				var customerName=new fabric.Text('客户名称:'+data.custName, { left: 20, top: 5,fill:ncolor,fontSize:12,selectable:false});
				var deviceName = new fabric.Text('设备名(地址):'+data.name, { left: 20, top: 20,fill:ncolor,fontSize:12,selectable:false});
				var gType = new fabric.Text('监控介质:'+data.gasType, { left: 20, top: 35,fill:ncolor,fontSize:12,selectable:false});
		    	objCacheMap.set('deviceName',deviceName);
		    	objCacheMap.set('customerName',customerName);
		    	objCacheMap.set('gType',gType);
		    	canvas.add(customerName,deviceName,gType);
		    }
		   	if(data.driContainerType=="tank"){//tank在此加载，否则文字不能显示
					var tankLeft=[getSVGData('tank','tank'),80,43,177,'tankLeft'];
					var tankRight=[getSVGData('tank','tank'),80,163,177,'tankRight'];
		   		svgDatas.push(tankLeft,tankRight);
			    }
		   	if(data.onLineStatus==0){ //加载警告！
			   	 var color='red';
				 var swicthRight=[getSVGData('warning',color),50,107,100,'warning'];
				 initSVG([swicthRight]);
				 var warningText = new fabric.Text('传感器未连接', { left: 95, top: 155,fill:'red',fontSize:12,selectable:false});
				 objCacheMap.set('warningText',warningText);
			     canvas.add(warningText);
		   	}
		   	 if(data.iotParamLeft!=iotParamLeftOld){
		    		if(data.driContainerType=="cylinder"){
		    			var cLeft;
		    			var lcp=data.iotParamLeft;
		        		if(lcp==0){
		        			cLeft="cylinder0";
		        		}else if(lcp>0&&lcp<=(data.driGasPressure/10)){
		        			cLeft="cylinder1";
		        		}else if(lcp>(data.driGasPressure/10)){
		        			cLeft="cylinder"+parseInt(lcp*10/data.driGasPressure);
		        		}
		        		if(data.driLeftCylindersNum<1){
		        			 cLeft="notcylinder";
		        		}
		        		canvas.remove(objCacheMap.get('leftConvergesName'));
	        			objCacheMap.remove('leftConvergesName');
	        		   if(data.driLeftCylindersNum<=1){
		               	    var cylinderLeft=[getSVGData('cylinder', cLeft),70,27.5,195,'cylinderLeft'];
		            		svgDatas.push(cylinderLeft);
		               }else if(data.driLeftCylindersNum==2){
		               	genLeftCylinder(2,16);
		               }else if(data.driLeftCylindersNum==3){
		               	genLeftCylinder(3,5.5);
		               }else if(data.driLeftCylindersNum==4){
		               	genLeftCylinder(4,-7);
		               }else if(data.driLeftCylindersNum>=5){
		               	genLeftCylinder(5,-16.6);
		               }
		        		/**
		        		 * 钢瓶范围每个22宽，高60，左右钢瓶距离
		        		 */
		        		function genLeftCylinder(num,leftPosition){
		        			var name='cylinderLeft';
		        			var convergesPosition=leftPosition+43;
		        			for(var i=0;i<num;i++){
		        				name=name+i;
		        				var cylinderLeft=[getSVGData('cylinder', cLeft),70,leftPosition,195,name];
		        				svgDatas.push(cylinderLeft);
		        				name='cylinderLeft';
		        				leftPosition=leftPosition+22;
		        			}
		        			initCylinderConverges(data.driLeftCylindersNum,convergesPosition,data.iotParamLeft>data.driAlarm ? 'green':'red','leftConvergesName');
		        		}
		        		
		    		}
		    	}
		    	if(data.iotParamRight!=iotParamRightOld){
		    		if(data.driContainerType=="cylinder"){
		    			var cRight;
		    			var rcp=data.iotParamRight;
		        		if(rcp==0){
		        			cRight="cylinder0";
		        		}else if(rcp>0&&rcp<=(data.driGasPressure/10)){
		        			cRight="cylinder1";
		        		}else if(rcp>(data.driGasPressure/10)){
		        			cRight="cylinder"+parseInt(rcp*10/data.driGasPressure);
		        		}
		        		if(data.driRightCylindersNum<1){
		        			 cRight="notcylinder";
		        		}
		        		canvas.remove(objCacheMap.get('rightConvergesName'));
	       			    objCacheMap.remove('rightConvergesName');
		       		  if(data.driRightCylindersNum<=1){
		        			var cylinderRight=[getSVGData('cylinder', cRight),70,147.6,195,'cylinderRight'];
		            		svgDatas.push(cylinderRight);
		               }else if(data.driRightCylindersNum==2){
		               	 genRightCylinder(2,136);
		               }else if(data.driRightCylindersNum==3){
		               	 genRightCylinder(3,125.5);
		               }else if(data.driRightCylindersNum==4){
		               	 genRightCylinder(4,114);
		               }else if(data.driRightCylindersNum>=5){
		               	 genRightCylinder(5,103.3);
		               }
		        		
		       		function genRightCylinder(num,rightPosition){
		        			var name='cylinderRight';
		        			var convergesPosition=rightPosition+43;
		        			for(var i=0;i<num;i++){
		        				name=name+i;
		        				var cylinderRight=[getSVGData('cylinder', cRight),70,rightPosition,195,name];
		        				svgDatas.push(cylinderRight);
		        				name='cylinderRight';
		        				rightPosition=rightPosition+22;
		        			}
		        			initCylinderConverges(num,convergesPosition,data.iotParamRight>data.driAlarm ? 'green':'red','rightConvergesName');
		        		}
		    		}
		    	}
		    var manometerHeight=110;
		     if(data.driValveLeft!=null){manometerHeight=130;}
		   	if(data.iotParamLeft!=iotParamLeftOld){
		   		var manometerLeft=[getSVGData('manometer',(data.iotParamLeft>data.driAlarm)?'manometer_green':'manometer_red'),40,60,manometerHeight,'manometerLeft'];
		   	   	 svgDatas.push(manometerLeft);
		   	}
		   	if(data.iotParamRight!=iotParamRightOld){
		   		var manometerRight=[getSVGData('manometer',(data.iotParamRight>data.driAlarm)?'manometer_green':'manometer_red'),40,180,manometerHeight,'manometerRight'];         		
		   	     svgDatas.push(manometerRight);
		   	}
		    if(data.driValveLeft!=null){
		    	if(data.driValveLeft!=driValveLeftOld){
		    		 var swicthLeft=[getSVGData('swicth',(data.driValveLeft==0)?'redSwicth':'greenSwicth'),22,56,90,'swicthLeft']; 
		    		 var onOrOffLeft=[getSVGData('swicth',(data.driValveLeft==0)?'offSwicth':'onSwicth'),43,10,72,'onOrOffLeft'];
		    		 svgDatas.push(swicthLeft,onOrOffLeft);
		    		
		    	}
		       	if(data.driValveRight!=driValveRightOld){
		       		 var swicthRight=[getSVGData('swicth',(data.driValveRight==0)?'redSwicth':'greenSwicth'),22,176.6,90,'swicthRight'];
		       		 var onOrOffRight=[getSVGData('swicth',(data.driValveRight==0)?'offSwicth':'onSwicth'),43,204,72,'onOrOffRight'];
		       		 svgDatas.push(swicthRight,onOrOffRight);
		       	} 
		       	
		    }
			    initSVG(svgDatas);
			   if(data.driContainerType=="tank"){initContentHint();}
			    driValveLeftOld=data.driValveLeft;
			    driValveRightOld=data.driValveRight;
			    iotParamLeftOld=data.iotParamLeft;
			    iotParamRightOld=data.iotParamRight;
			    canvas.renderAll();
			    beginInit=true;
				
			}
		}
		
	
		//addr:svg地址 （iframeID），svgName：svg的ID名称 
		function getSVGData(addr,svgId){
			return  svgdata= document.getElementById(addr).contentWindow.document.getElementById(svgId);
		}
		
		function initSVG(data){ 	 
			if(data.length>0){
				if(data.length==1){
					loadSVG(data[0][0],data[0][1],data[0][2],data[0][3],data[0][4]);
				}else{
					for(var i in data){
						loadSVG(data[i][0],data[i][1],data[i][2],data[i][3],data[i][4]);	
					}
				}
			}  	
		   //svgData:图片数据 ，svgHeight：图片高度 ，positionLeft：图片位置距离左 ，positionTop：图片位置距离上 
			     function loadSVG(svgData,svgHeight,positionLeft,positionTop,imageName){
			     canvas.remove(objCacheMap.get(imageName));
			     objCacheMap.remove(imageName);
			     if(svgData==null||svgData==undefined){ return false;}
		 	     var svgStr = serializer.serializeToString(svgData);
		 	     fabric.loadSVGFromString(svgStr,function(objects, options){
		 	         var obj = fabric.util.groupSVGElements(objects,options);
		 	         obj.id=imageName;
		 	         objCacheMap.set(imageName,obj);
		 	         obj.scaleToHeight(svgHeight).set({ left: positionLeft, top: positionTop}).setCoords().set('selectable', false);
		 	         //set('selectable', false)
		 	         canvas.add(obj);
		 	    });
			 }
		}
		
		function initCylinderConverges(num,distanceLeft,color,name){ //初始化钢瓶汇流排管道	
			 var  convergesPath='';
			if(num==2){//宽3，横70，高20，单个钢瓶19
				convergesPath= 'M 1 0 '+  
				'L 1 -20 L 26 -20 L 26 0 L 23 0 '+
				'L 23 -17 L 4 -17 L 4 0 '+
				'Z'; 
			}else if(num==3){
				convergesPath= 'M 1 0 '+  
				'L 1 -20 L 48 -20 L 48 0 L 45 0 '+
				'L 45 -17 L 26 -17 L 26 0 L 23 0 L 23 -17 L 4 -17 L 4 0 '+
				'Z'; 
			}else if(num==4){ //宽3，横70，高20，单个钢瓶19
				convergesPath= 'M 1 0 '+  
				'L 1 -20 L 70 -20 L 70 0 L 67 0 '+
				'L 67 -17 L 48 -17 L 48 0 L 45 0 L 45 -17 L 26 -17 L 26 0 L 23 0 L 23 -17 L 4 -17 L 4 0'+
				'Z'; 	
			}else if(num>=5){
				convergesPath= 'M 1 0 '+     
			 	 'L 1 -20 L 92 -20 L 92 0'+
				 'L 89 0 L 89 -17 L 70 -17 L 70 0 L 67 0'+ 
			 	 'L 67 -17 L 48 -17 L 48 0 L 45 0 L 45 -17 L 26 -17 L 26 0 L 23 0 L 23 -17 L 4 -17 L 4 0'+
			     'Z';
			}
				 
			 var converges = new fabric.Path(convergesPath);
			 converges.set({ left: distanceLeft, top: 177,fill: color,selectable:false});
		   	 objCacheMap.set(name,converges);
		   	 canvas.add(converges);		 
		} 
		function initContentHint(){
				if(data.iotParamLeft!=iotParamLeftOld){
					canvas.remove(objCacheMap.get('leftManometerText'));
					objCacheMap.remove('leftManometerText');
					canvas.remove(objCacheMap.get('leftManometerText2'));
					objCacheMap.remove('leftManometerText2');
					var fontColor="#00FF00";
					if(data.iotParamLeft<=data.driAlarm){
						fontColor="red";
					}
					var manometerValue='压力:'+data.iotParamLeft;
					var leftManometerText = new fabric.Text(manometerValue, { left: 8, top: 120,fill:fontColor,fontSize:12,selectable:false});
			    	objCacheMap.set('leftManometerText',leftManometerText);
			    	var manometerValue2='(Mpa)';
			    	var leftManometerText2 = new fabric.Text(manometerValue2, { left: 20, top: 137,fill:fontColor,fontSize:13,selectable:false});
			    	objCacheMap.set('leftManometerText2',leftManometerText2);
			    	canvas.add(leftManometerText,leftManometerText2);
				}
				
				if(data.iotParamRight!=iotParamRightOld){
					
					canvas.remove(objCacheMap.get('rightManometerText'));
					objCacheMap.remove('rightManometerText');
					canvas.remove(objCacheMap.get('rightManometerText2'));
					objCacheMap.remove('rightManometerText2');
					var fontColor="#00FF00";
					if(data.iotParamRight<=data.driAlarm){
						fontColor="red";
					}
					var manometerValue='压力:'+data.iotParamRight;
					var rightManometerText = new fabric.Text(manometerValue, { left: 210, top: 120,fill:fontColor,fontSize:12,selectable:false});
					objCacheMap.set('rightManometerText',rightManometerText);
					var manometerValue2='(Mpa)';
					var rightManometerText2 = new fabric.Text(manometerValue2, { left: 215, top: 137,fill:fontColor,fontSize:13,selectable:false});
					objCacheMap.set('rightManometerText2',rightManometerText2);
			    	canvas.add(rightManometerText,rightManometerText2);
				}
				
				if(data.driContainerType=="tank"){//储罐文字和能量条
					initLeftTankText();
					initRightTankText();
				}else if(data.driContainerType=="cylinder"){
					initLeftCylinderText();
					initRightCylinderText();
				}
				
				   function initLeftTankText(){
							canvas.remove(objCacheMap.get('leftTankText'));
							objCacheMap.remove('leftTankText');
							var leftTankID=parseInt(data.iotParamLeft*10/data.driGasPressure);
							var fontColor=getColor(leftTankID);
							var percent;
							if((data.iotParamLeft*100/data.driGasPressure)==0){
								percent="0%";
							}else{
								var percentData=0;
								percentData=(data.iotParamLeft*100/data.driGasPressure).toFixed(0);
								if(percentData>100){
									 percentData=100;
								}
								percent=percentData+"%";
							}
							var leftTankText = new fabric.Text(percent, { left: 8, top: 200,fill:fontColor, 
					    		fontSize:14,selectable:false});
					    	objCacheMap.set('leftTankText',leftTankText);
					    	canvas.add(leftTankText);
						
					    canvas.remove(objCacheMap.get('leftBar'));
						objCacheMap.remove('leftBar');
						var barHeight=221-(leftTankID*2);
						var leftBar=initLine(59,221,barHeight,fontColor,1.6);
						objCacheMap.set('leftBar',leftBar);
						canvas.add(leftBar);
				   }
				   function initRightTankText(){
					    	canvas.remove(objCacheMap.get('rightTankText'));
					    	objCacheMap.remove('rightTankText');
					    	var rightTankID=parseInt(data.iotParamRight*10/data.driGasPressure);
					    	var fontColor=getColor(rightTankID);
					    	var percent;
							if((data.iotParamRight*100/data.driGasPressure)==0){
								percent="0%";
							}else{
								var percentData=0;
								percentData=(data.iotParamRight*100/data.driGasPressure).toFixed(0);
								if(percentData>100){
									 percentData=100;
								}
								percent=percentData+"%";
							}
					    	var rightTankText = new fabric.Text(percent, { left: 223, top: 200,fill:fontColor, 
					    		fontSize:14,selectable:false});
					    	objCacheMap.set('rightTankText',rightTankText);
					    	canvas.add(rightTankText);
				    	    canvas.remove(objCacheMap.get('rightBar'));
							objCacheMap.remove('rightBar');
							var barHeight=221-(rightTankID*2);
							var rightBar=initLine(179,221,barHeight,fontColor,1.6);
							objCacheMap.set('rightBar',rightBar);
							canvas.add(rightBar);
					   
				   }
				   function initLeftCylinderText(){
					   var leftCylinderID=parseInt(data.iotParamLeft*10/data.driGasPressure);
						var cylinderLeftIdNew="cylinder"+leftCylinderID; 
						if(cylinderLeftIdNew!=iotParamLeftOld){
							canvas.remove(objCacheMap.get('leftCylinderText'));
							objCacheMap.remove('leftCylinderText');
							var fontColor=getColor(leftCylinderID);
							var percent;
							if((data.iotParamLeft*100/data.driGasPressure)==0){
								percent="0%";
							}else{
								var percentData=0;
								percentData=(data.iotParamLeft*100/data.driGasPressure).toFixed(0);
								if(percentData>100){
									 percentData=100;
								}
								percent=percentData+"%";
							}
							var leftPo=25,leftTopPo=220;
							if(data.driLeftCylindersNum>1){
								leftTopPo=190;
								if(data.driLeftCylindersNum==2){
									leftPo=25;
								}else if(data.driLeftCylindersNum==3){
									leftPo=13;	
								}else if(data.driLeftCylindersNum==4){
									leftPo=3;	
								}else if(data.driLeftCylindersNum==5){
									leftPo=13;
									leftTopPo=162;
								}else if(data.driLeftCylindersNum>5){
									leftPo=13;
									leftTopPo=162;
									canvas.remove(objCacheMap.get('leftCylindersNum'));
									objCacheMap.remove('leftCylindersNum');
									canvas.remove(objCacheMap.get('leftCylindersOmit'));
									objCacheMap.remove('leftCylindersOmit');
									var leftCylindersOmit = new fabric.Text('...', { left: 3, top: 220,fill:fontColor, 
							    		fontSize:15,selectable:false});
									var leftMu=2;
									if(data.driLeftCylindersNum>9){leftMu=0;}
									var leftCylindersNum = new fabric.Text(data.driLeftCylindersNum+'瓶', { left: leftMu, top: 240,fill:fontColor, 
							    		fontSize:9,selectable:false});
									objCacheMap.set('leftCylindersOmit',leftCylindersOmit);
									objCacheMap.set('leftCylindersNum',leftCylindersNum);
							    	canvas.add(leftCylindersNum,leftCylindersOmit);
								}
								
							}
							var leftCylinderText = new fabric.Text(percent, { left: leftPo, top: leftTopPo,fill:fontColor, 
					    		fontSize:12,selectable:false});
					    	objCacheMap.set('leftCylinderText',leftCylinderText);
					    	canvas.add(leftCylinderText);
						}
				   }
				   function initRightCylinderText(){
						var rightCylinderID=parseInt(data.iotParamRight*10/data.driGasPressure);
						var cylinderRightIdNew="cylinder"+rightCylinderID;
						if(cylinderRightIdNew!=iotParamRightOld){
							canvas.remove(objCacheMap.get('rightCylinderText'));
							objCacheMap.remove('rightCylinderText');
					    	var fontColor=getColor(rightCylinderID);
					    	var percent;
							if((data.iotParamRight*100/data.driGasPressure)==0){
								percent="0%";
							}else{
								var percentData=0;
								percentData=(data.iotParamRight*100/data.driGasPressure).toFixed(0);
								if(percentData>100){
									 percentData=100;
								}
								percent=percentData+"%";
							}
							
							var rightPo=215,rightTopPo=220;
							if(data.driRightCylindersNum>1){
								rightTopPo=190;
								if(data.driRightCylindersNum==2){
									rightPo=217;
								}else if(data.driRightCylindersNum==3){
									rightPo=226;
								}else if(data.driRightCylindersNum==4){
									rightPo=235;
								}else if(data.driRightCylindersNum==5){
									rightTopPo=162;
									rightPo=228;
								}else if(data.driRightCylindersNum>5){
									rightTopPo=162;
									rightPo=228;
									canvas.remove(objCacheMap.get('rightCylindersNum'));
									objCacheMap.remove('rightCylindersNum');
									canvas.remove(objCacheMap.get('rightCylindersOmit'));
									objCacheMap.remove('rightCylindersOmit');
									var rightCylindersOmit = new fabric.Text('...', { left:249, top: 220,fill:fontColor, 
							    		fontSize:15,selectable:false});
									var leftMu=249;
									if(data.driRightCylindersNum>9){leftMu=246;}
									var rightCylindersNum = new fabric.Text(data.driRightCylindersNum+'瓶', { left: leftMu, top: 240,fill:fontColor, 
							    		fontSize:9,selectable:false});
									objCacheMap.set('rightCylindersOmit',rightCylindersOmit);
									objCacheMap.set('rightCylindersNum',rightCylindersNum);
							    	canvas.add(rightCylindersNum,rightCylindersOmit);
								}
							}
							var rightCylinderText = new fabric.Text(percent, { left: rightPo, top: rightTopPo,fill:fontColor, 
					    		fontSize:12,selectable:false});
					    	objCacheMap.set('rightCylinderText',rightCylinderText);
					    	canvas.add(rightCylinderText);
						}
				   }
				   function getColor(parm){
					   var fontColor="#00FF00";
					    if(parm==3){
							fontColor="yellow";
						}else if(parm==2){
							fontColor="#FF00FF";
						}else if(parm==1||parm==0) {
							fontColor="red";
						}
					    return fontColor;
				   }
			}
		
		function initPipeline(){
			 var publicPathColor='red';
			 var  leftPathColor='red';
			 var rightPathColor='red';
			 var leftLineColor='red';
			 var rightLineColor='red';
			 
			 
			 if(data.driValveLeft==1||data.driValveRight==1){
				 if(data.driValveLeft==1){
					 if(data.iotParamLeft>data.driAlarm){
						 publicPathColor='green';
						 leftPathColor='green';
					 }
				 }
				 if(data.driValveRight==1){
					 if(data.iotParamRight>data.driAlarm){
						 publicPathColor='green';
		   			 rightPathColor='green';
					 }
				 }
			 }
			 if(data.iotParamLeft>data.driAlarm){
				 leftLineColor='green';
			 }
			 if(data.iotParamRight>data.driAlarm){
				 rightLineColor='green';
			 }
			 if(data.iotParamLeft!=iotParamLeftOld||data.iotParamRight!=iotParamRightOld||
				data.driValveLeft!=driValveLeftOld||data.driValveRight!=driValveRightOld){
				 canvas.remove(objCacheMap.get('publicPath'));
				objCacheMap.remove('publicPath');
				var publicPath = new fabric.Path('M 0 0 L 3 0 L 3 17 L 0 17 Z');
		   	publicPath.set({ left: 129.5, top: 57,fill: publicPathColor,selectable:false});
		   	objCacheMap.set('publicPath',publicPath);
		    canvas.add(publicPath);
		   	canvas.remove(objCacheMap.get('leftPath'));
			objCacheMap.remove('leftPath');
		     var lHeight=40;
		     if(data.driValveLeft!=null){
		    	 lHeight=21;
		     }
			 var leftPath = new fabric.Path('M 0 0 L 0 '+lHeight+' L 3 '+lHeight+' L 3 3 L 59 3 L 59 0 Z');
		   	 leftPath.set({ left: 70, top: 70,fill: leftPathColor,selectable:false});
		   	 objCacheMap.set('leftPath',leftPath);
		   	  canvas.add(leftPath);
		   	  
		   	 canvas.remove(objCacheMap.get('rightPath'));
		  		 objCacheMap.remove('rightPath');
		  		 var rightPath = new fabric.Path('M 0 0 L 61 0 L 61 '+lHeight+' L 58 '+lHeight+' L 58 3 L 0 3 Z');
		      	 rightPath.set({ left: 133, top: 70,fill: rightPathColor,selectable:false});
		      	 objCacheMap.set('rightPath',rightPath);
		      	 canvas.add(rightPath);
		  
			 }
			
			var lstartp=178;
			var lendp=148;
			var rstartp=178;
			var rendp=148;
			if(data.driValveLeft!=null){
				lendp=110.8;
				rendp=110.8;
			 }
			if(data.iotParamLeft!=iotParamLeftOld){
				 canvas.remove(objCacheMap.get('leftLine'));
				 objCacheMap.remove('leftLine');
				 if(data.driLeftCylindersNum<=1){
					 lstartp=197;
				 }
				 var leftLine=initLine(71,lendp,lstartp,leftLineColor,3);//第一个参数，距离左，第二个leftLineRange 为终点，第三个参数为起点
				 objCacheMap.set('leftLine',leftLine);
				 canvas.add(leftLine);
			}
		   if(data.iotParamRight!=iotParamRightOld){
		   	 canvas.remove(objCacheMap.get('rightLine'));
		   	 objCacheMap.remove('rightLine');
			 if(data.driRightCylindersNum<=1){
				 rstartp=197;
			 }
		   	 var rightLine=initLine(191,rendp,rstartp,rightLineColor,3);
		   	 objCacheMap.set('rightLine',rightLine);
		   	 canvas.add(rightLine);
		   }
		   
			
		} 
		
		function initLine(start,end,positionTop,color,strokeWidth){
			  var line= new fabric.Line([start,end,start,positionTop],
		        {//终止位置，线长，起始位置，top，这里是从项目中截下来的我用了变量代替，要用的话lineheight和lineleft用自己的变量或者数字代替。如果两个终止位置和起始位置的数值一样那么这个线条会垂直。
		            fill: color,//填充颜色
		            stroke: color,//笔触颜色
		            strokeWidth: strokeWidth,//笔触宽度
		            hasControls: false, //选中时是否可以放大缩小
		            hasRotatingPoint: false,//选中时是否可以旋转
		            hasBorders:false,//选中时是否有边框
		            transparentCorners:true,
		            perPixelTargetFind:true,//默认false。当设置为true，对象的检测会以像互点为基础，而不是以边界的盒模型为基础。
		            selectable:false,//是否可被选中
		            lockMovementX: true,//X轴是否可被移动(true为不可，因为前缀是lock)
		            lockMovementY: true//Y轴是否可被移动(true为不可，因为前缀是lock)
		        }
			  );
			  return line;
		 }
		
		
		var isCommitting=true;
		function executeOnOrOff(operationPosition){ //开关执行操作
			   if(data.onLineStatus==0){
			     $.messager.alert('修改提示','传感器未连接,无法进行修改！','warning',function(){
			    	 isExcuteValve=false;
			    	 animationDataHandler(cacheData);
			    	 return false;
		      });
		      return false;
			   }
			   $.messager.confirm('确认执行修改', '提交修改后会立刻更改电磁阀状态,请确认是否修改？', function(r){
					if (r){
					    if(data!=null){
					       var rows=canvasData;
				    	   if(operationPosition=='left'){
				    		   data.driValveLeft=(data.driValveLeft==0)?1:0;
				    		   isCurrentOpen('left');
				    	   }else if(operationPosition=='right'){
				    		   data.driValveRight=(data.driValveRight==0)?1:0;
				    		   isCurrentOpen('right');
				    			
				    	   }
				    	    function isCurrentOpen(postion){
				    	       if(data.driValveLeft==1&&data.driValveRight==1){
				    	    	   $.messager.alert('修改提示','同时打开电磁阀时,会自动关闭另一侧电磁阀！','warning',function(){
				        	    		 if(postion=='left'){
				        	    			 data.driValveRight=0;
				        	    		 }else if(postion=='right'){
				        	    			 data.driValveLfet=0;
				        	    		 }
				        	    		 ajixRequest(rows);
				        	    	 });
				      		   }else{
				      			   ajixRequest(rows);
				      		   }
				    	    }
					   }
					   
					   function ajixRequest(reqData){
				           var jsonData= getCloneJsonData(reqData);
				           var strData=JSON.stringify(jsonData);
				            if(isCommitting){//防止重复提交 
				             isCommitting=false;
				           	   $.ajax({
				                      type: "POST",
				                      async: false,//同步，需等待返回 
				                      url:addr+"busbar/valveStatusUpd",
				                      contentType:"application/json", //必须有
				                      data: strData,
				                      beforeSend:function(){
				                    	lockScreenModify=false; //锁屏期间禁止websocket渲染图形
				                   	    $('#dlg1').dialog('open').dialog('center');
				                      },
				                      success: function (result) {
				                   	   isCommitting=true;
				                   	   $('#dlg1').dialog('close');
				                 	      if(result=='error'){//修改失败，重新加载修改之前的按钮样式
				                 	    	 $.messager.show({
				                 				title:'提示消息',
				                 				msg:"修改失败",
				                 				timeout:2000,
				                 				showType:'slide'
				                 	          });
				                    	  }else if(result=='success'){
				                    		  $.messager.show({
					                 				title:'提示消息',
					                 				msg:"修改成功",
					                 				timeout:2000,
					                 				showType:'slide'
					                 	          });
				                    	 } 
				                 	     lockScreenModify=true;
				                 	     isExcuteValve=false;
		                    			 animationDataHandler(cacheData); 
				                      },
				                      error:function(XMLHttpRequest, textStatus){
					                   	   $('#dlg1').dialog('close');
					                   	   isCommitting=true; 
					                   	    $.messager.show({
				                 				title:'提示消息',
				                 				msg:"修改失败",
				                 				timeout:2000,
				                 				showType:'slide'
				                 	          });
						                   lockScreenModify=true;
						                   isExcuteValve=false;
					          	    	   animationDataHandler(cacheData);
				                      }
				               });
				            }
				            
				            function getCloneJsonData(jsonData){
				            	 var cloneObject= JSON.parse(JSON.stringify(jsonData));
				                  	delete cloneObject.page;
				                  	delete cloneObject.iotParamLeft;
				                  	delete cloneObject.iotParamRight;
				                   return cloneObject;
				             }
					   }      
					}else{ //if (r){}
						isExcuteValve=false;
						 animationDataHandler(cacheData);
					}
				});	  
		}
}

