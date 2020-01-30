     var path=getPath();
	 var ws;//websocket实例
	 var deviceDataIsFirst=true;
	 var iotAddr=0;
	 window.onload=function (){
		initIotAddr(); //必须在websocket之前
//		initBox();
		initWebSocket();
		initClientConn();
	};
	function initIotAddr(){
		var iot=getDeviceIotAddr();
		 if(iot!='null'&&iot!=undefined){
			iotAddr= iot;
		 } 
	}
   function initBox(){
	    $('#pageUtil').pagination({
	    	pageList: [10],
	    	total:0, 
	    	pageSize:10,
	    	showRefresh:false,
	    	onSelectPage:function(pageNumber, pageSize){
	    		paginationSerach();
	    	}
	    });
	    pageObj=$('#pageUtil').pagination('options');
	    
   }
   
  
   function paginationSerach(){
	   if(ws!=null){
		     var pageNumber=pageObj.pageNumber;
			 var parm='clientAnimation='
				 +'|iotAddr='+iotAddr
				 +'|page='+pageNumber
				 +'|';
			 if(iotAddr!=0){
				 ws.send(parm);
			 }
	   }
   }
    
    var pageObj=null;
    var cacheData=null;
    function initWebSocket(){	
    		 var webSocketPath = document.location.host;
    		 var lockReconnect = false;//避免重复连接
    		 var wsUrl = getwsurl();
    		 createWebSocket(wsUrl);
    			function createWebSocket(url) {		
    	    	    try {
    	    	        ws = new WebSocket(url);
    	    	        initEventHandle();
    	    	    } catch (e) {
    	    	        reconnect(url);
    	    	    }    	    
    		     }
    			function initEventHandle() {
    				
    			    ws.onopen = function(evnt) {
    			    	connOper(true);
    			    	remoteAddrIsActive();
    		            heartCheck.reset().start();//心跳检测重置
    		    	};
    		    	ws.onmessage = function(evnt) { 
    		    		var  dataValue= evnt.data;
    		    		if(dataValue!=null&&dataValue.indexOf("HeartBeatPackage") == -1){  	
		    				var data=[];
    			    	    data= JSON.parse(dataValue);
    			    	    cacheData=data;
			    	    	if(data.search!=undefined||data.total>1){
			    	    		if(pageObj!=null&&pageObj.pageNumber!=null){
			    	    			$('#pageUtil').pagination('refresh',{	// 改变选项并刷新分页栏信息
	    			    	    		total: data.total,
	    			    	    		pageNumber: pageObj.pageNumber
	    			    	    	});	
			    	    		}
			    	         }
			    	    	if(data!=null){
			    	    		window.animationDataHandler(data);
			    	    	}
    		    		}
    		    		 heartCheck.reset().start();//如果获取到消息，心跳检测重置
    		    	};
    		    	ws.onerror = function(evnt) {
    		    		reconnect(wsUrl);
    		    	};
    		    	ws.onclose = function(evnt) {
                       ws.close();
    		    	   connOper(true);
    		    	   remoteAddrIsActive();
                       reconnect(wsUrl);
    		    		
    		    	};
    		        //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    		        window.onbeforeunload = function(){
    		    	    ws.close(); 
    		    	};
    			}
    	    	function getwsurl() {
    	    		//作兼容性连接
    	    		if ('WebSocket' in window &&iotAddr!=0&&iotAddr!=undefined) {  
    			       return "ws://" + webSocketPath + "/dataHandler?user="+iotAddr+"&wstype=clientAnimation";  
    			     } else if ('MozWebSocket' in window) {  
    			    	 return "ws://" + webSocketPath + "/dataHandler?user="+iotAddr+"&wstype=clientAnimation";  
    			     } else {  
    			        return "http://" + webSocketPath + "/dataHandler?user="+iotAddr+"&wstype=clientAnimation";  
    			     }  
    	    	}
    	    	//重新连接
    			function reconnect(url) {
    			    if(lockReconnect) return;
    			    lockReconnect = true;
    			    setTimeout(function () {
    			        createWebSocket(url);
    			        lockReconnect = false;
    			    }, 5000);
    			}
    			//心跳检测,每30s心跳一次
    			var heartCheck = { 
    					timeout: 30000,  
    					timeoutObj: null, 
    					serverTimeoutObj: null,
    					reset: function(){ 
    						clearTimeout(this.timeoutObj);
    						clearTimeout(this.serverTimeoutObj); 
    						return this; 
    					}, 
    					start: function(){ 
    						var self = this; 
    						this.timeoutObj = setTimeout(function(){ 
    							ws.send("HeartBeatPackage"+Math.random());
    							self.serverTimeoutObj = setTimeout(function(){
    							}, self.timeout)}, 
    						this.timeout)}
    			};	
    	 }
    
      function getDeviceIotAddr(){
    	 var sIotAddr= $("#sIotAddr").val();
    	  var aesFun = {
                  options:{
                      aesKey:"dekalong_secret."
                  },
                  encrypt:function(word){
                      var key = CryptoJS.enc.Utf8.parse(this.options.aesKey);
                      var srcs = CryptoJS.enc.Utf8.parse(word);
                      var encrypted = CryptoJS.AES.encrypt(srcs, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
                      return encrypted.toString();
                  
                  },
                  decrypt:function(word){
                      var key = CryptoJS.enc.Utf8.parse(this.options.aesKey);
                      var decrypt = CryptoJS.AES.decrypt(word, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
                      return CryptoJS.enc.Utf8.stringify(decrypt).toString();
                  }
                  
               }
           return  aesFun.decrypt(sIotAddr);   

      }
    var setTime=null;
    var isFirstConn=true;
    var isWebSocketOper=false;
  	function initClientConn(){
        var refreshTime=3600*1000;//600
        setTime=setInterval(connOper, refreshTime);
        isFirstConn=false;
    }
  	
  	function connOper(isWebsoket){// 跨域访问dataType:"jsonp",
  	  if(isWebsoket==true){
  		  isWebSocketOper=true;
  	  }else{
  		  isWebSocketOper=false;
  	  }
  	  var remoteAddr="http://test.gq-smartwatcher.cn";
       var localhostAddr="http://localhost:8080";
       var sIotAddr=$("#sIotAddr").val();
       var param="?sIotAddr="+sIotAddr;
       var connURI="/device/jsonpKeepConnection"+param;
       var reconnURI="/device/toClientPage"+param;
    	if (navigator.onLine) {  //判断硬件是否在线联网
    		 $.ajax({
                 url:remoteAddr+connURI,
                 processData: false,
                 timeout:3000,
                 async:true,
                 type:"get",
                 dataType:"jsonp",
                 jsonp: "callback",
                 success:function(result){
               	    if(typeof result=="object"){
                         var connStatus=result.status;
                          if(connStatus=='success'){
                        	  var isSkip=false;
                        	  if(isWebSocketOper){
                        		  if(path.indexOf(remoteAddr) == -1){isSkip=true;}
                        	  }else{
                        		  if(!isFirstConn){isSkip=true;}
                        	  }
                        	  if(isSkip){
                        		  clearInterval(remoteAddrSkipTest);
                        		  clearInterval(setTime);
                      		      window.location.href=remoteAddr+reconnURI;
                        	  }
                          }else{
                        	  toLocalAddr();
                          }
   		    		}
                 },
                 error:function(){
                	 toLocalAddr();
                 }
               });
    	}else{
    		toLocalAddr();
    	}
    	function toLocalAddr(){
    		var isSkip=false;
    		if(isWebSocketOper){
    			  if(path.indexOf(localhostAddr) == -1){ isSkip=true;}
    		}else{ 
    			  if(!isFirstConn){ isSkip=true;}
    		}
    		if(isSkip){
    			  clearInterval(remoteAddrSkipTest);
    			  clearInterval(setTime);
     			  window.location.href=localhostAddr+reconnURI;
    		}
    	}
    }
  	
  	var remoteAddrSkipTest=null;
  	function remoteAddrIsActive(){
        if (!navigator.onLine&&remoteAddrSkipTest==null) {
       		  remoteAddrSkipTest=setInterval(operConnTest, 5000);
        }
  	}
   
  	function operConnTest(){
  		if(navigator.onLine){
  			var remoteAddr="http://test.gq-smartwatcher.cn";
  	        var sIotAddr=$("#sIotAddr").val();
  	        var param="?sIotAddr="+sIotAddr;
  	        var connURI="/device/jsonpKeepConnection"+param;
  	        var reconnURI="/device/toClientPage"+param;
  	  		$.ajax({
  	            url:remoteAddr+connURI,
  	            processData: false,
  	            timeout:3000,
  	            async:true,
  	            type:"get",
  	            dataType:"jsonp",
  	            jsonp: "callback",
  	            success:function(result){
  	          	    if(typeof result=="object"){
  	                    var connStatus=result.status;
  	                     if(connStatus=='success'){
  	                    	  clearInterval(remoteAddrSkipTest);
  	                    	  remoteAddrSkipTest=null;
  	                    	  window.location.href=remoteAddr+reconnURI;
  	                     }
  			    	}
  	            }
  	          });
  		}
  	}
