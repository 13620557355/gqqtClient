
var path=getPath(); 
window.onload=function(){ //iframe嵌套问题，解决session清除后首页跳转到子页面中
	  if(window.parent!=window){
	    window.parent.location.href=window.location.href;
	  }
	  if($('#login_error')!=undefined&&$('#login_error').val()!=''&&$('#login_error').val()!='null'){	 
		 var msg=$('#login_error').val();
		  $.messager.show({
				title:'提示消息',
				msg:msg,
				timeout:4000,
				showType:'slide'
	      });
	  }
	  
};

function checkInputClient() {
	loginValidate("device/validate") ;
}

 function loginValidate(uri){
		
		  var iotAddr=$("#iotAddr").val();
		  var isAlert=false;
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
		  	var from=document.getElementById("submit");
			    from.method="post";
			    from.action=path+uri;
		  	    from.submit(); 
		  	   $('#login_error').val('');
		  } 
 }
    