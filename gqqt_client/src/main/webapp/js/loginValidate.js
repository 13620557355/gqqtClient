
var path=getPath(); 
window.onload=function(){ //iframe嵌套问题，解决session清除后首页跳转到子页面中
	  if(window.parent!=window){
	    window.parent.location.href=window.location.href;
	  }
	  autoAction();
//	  if($('#login_error')!=undefined&&$('#login_error').val()!=''&&$('#login_error').val()!='null'){	 
//		 var msg=$('#login_error').val();
//		  $.messager.show({
//				title:'提示消息',
//				msg:msg,
//				timeout:4000,
//				showType:'slide'
//	      });
//	  }
	  
};

function checkInputClient() {
	loginValidate("device/validate") ;
}

function autoAction(){
	  var uri="device/toClientPage";
	  window.location.href=path+uri+"?"+"sIotAddr="+getDeviceSiotAddr(10015600);
//	  var form= document.createElement('form');
//	  form.method="get";
//	  form.action=path+uri;
//	  var input = document.createElement('input');
//	    input.type = 'hidden';
//	    input.name = 'iotAddr';
//	    input.value = getDeviceSiotAddr(10015600);
//	    form.appendChild(input);   
//	    $(document.body).append(form);
//	    form.submit(); 
//  	   $('#login_error').val('');
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
    