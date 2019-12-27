
var path=getPath(); 
window.onload=function(){ //iframe嵌套问题，解决session清除后首页跳转到子页面中
	  if(window.parent!=window){
	    window.parent.location.href=window.location.href;
	  }
	  autoAction();

	  
};

function checkInputClient() {
	loginValidate("device/validate") ;
}


function autoAction(){// 跨域访问dataType:"jsonp",
	var sIotAddr=getDeviceSiotAddr(40017680);
	 var remoteAddr="http://test.gq-smartwatcher.cn";
    var localhostAddr="http://localhost:8080";
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
function getDeviceSiotAddr(iotAddr){
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
      return  aesFun.encrypt(iotAddr);   
 }
 function loginValidate(uri){
		  if(iotAddr==""){
			  isAlert=true;
			  if(iotAddr==""){msg='必须输入物联网地址';}
		  }
		  if(isAlert){
			  $.messager.show({
					title:'提示消息',
					msg:msg,
					timeout:2000,
					showType:'slide'
		     });
		  }else{  
//		  	var from=document.getElementById("submit");
			  var from= document.createElement('form');
			    from.method="post";
			    from.action=path+uri;
		  	    from.submit(); 
		  	   $('#login_error').val('');
		  } 
 }
    