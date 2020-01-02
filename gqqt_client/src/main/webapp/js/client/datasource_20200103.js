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
    		            heartCheck.reset().start();//心跳检测重置
    		    	};
    		    	ws.onmessage = function(evnt) { 
    		    		var  dataValue= evnt.data;
    		    		if(dataValue!=null&&dataValue.indexOf("HeartBeatPackage") == -1){
    		    			var data=[];
    			    	    data= JSON.parse(dataValue);
    			    	    cacheData=data;
			    	    	if(data.search!=undefined||data.total>1){
    			    	    	$('#pageUtil').pagination('refresh',{	// 改变选项并刷新分页栏信息
    			    	    		total: data.total,
    			    	    		pageNumber: pageObj.pageNumber
    			    	    	});	   
			    	         }
			    	    	if(data!=null){
	    			    	    animationDataHandler(data);
			    	    	}
    		    		}
    		    		 heartCheck.reset().start();//如果获取到消息，心跳检测重置
    		    	};
    		    	ws.onerror = function(evnt) {
    		    		reconnect(wsUrl);
    		    	};
    		    	ws.onclose = function(evnt) {
                       ws.close();
    		    	//   connOper(true);
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
      
  	function initClientConn(){
        var refreshTime=10*1000;
        var setTime=setInterval(connOper, refreshTime);
   }
  	
  	function connOper(istoLocal){// 跨域访问dataType:"jsonp",
  		 var remoteAddr="http://test.gq-smartwatcher.cn";
//         var localhostAddr="http://localhost:8080";
//  		var remoteAddr="http://localhost:8080";
        var localhostAddr="http://localhost:9090";
         var sIotAddr=$("#sIotAddr").val();
         var param="?sIotAddr="+sIotAddr;
         var connURI="/device/jsonpKeepConnection"+param;
         var reconnURI="/device/toClientPage"+param;
         if(istoLocal){
        	 toLocalAddr();
         }
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
                       	   window.location.href=remoteAddr+reconnURI;
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
    	    	window.location.href=localhostAddr+reconnURI;
    	}
    }
   
