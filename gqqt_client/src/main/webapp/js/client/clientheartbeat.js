
//        var refreshTime=20000;
        var refreshTime=120*1000;
        var setTime=setInterval(connOper, refreshTime);
        var remoteAddr="http://test.gq-smartwatcher.cn";
        var localhostAddr="http://localhost:8080";
        var sIotAddr=$("#sIotAddr").val();
        var param="?sIotAddr="+sIotAddr;
        var connURI="/device/jsonpKeepConnection"+param;
     
        function connOper(){// 跨域访问dataType:"jsonp",
              $.ajax({
              url:localhostAddr+connURI,
              processData: false,
              async:true,
              type:"get",
              dataType:"jsonp",
              jsonp: "callback",
              success:function(result){
            	    if(typeof result=="object"){
                      var connStatus=result.status;
                      var currentIotAddr=result.sIotAddr;
                       if(connStatus=='success'){
                    	  if(navigator.onLine) { // true|false
                          	 window.location.href=remoteAddr+"/device/toClientPage?sIotAddr="+currentIotAddr;
                          }else{
                          	 window.location.href=localhostAddr+"/device/toClientPage?sIotAddr="+currentIotAddr;
                          }
                       }else{
                    	   window.location.href=localhostAddr+"/device/toClientPage?sIotAddr="+currentIotAddr;
                       }
		    		}
              },
              error:function(){
            	  window.location.href=localhostAddr+"/device/toClientPage?sIotAddr="+sIotAddr;
              }
            });
        }


