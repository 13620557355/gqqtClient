<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
     <%@include file="/public/path.jspf" %>
     <%@include file="/public/mobile.jspf" %>
    <style>
      html, body{width:100%;height: 100%;margin: 0; padding: 0;border: 0;overflow-x:auto;overflow-y:auto;background-color:#2b2b2b;}
      #box{text-align:center;}
      .canvasClass{ position:relative;float:left;border:0;padding:0;top:-15px;outline:none;display:none}
      iframe{display:none}
    </style>
</head>
<body id='aniBody' >
<input id="sIotAddr" name="sIotAddr" type="hidden" value="<%=request.getAttribute("sIotAddr")%>" /> 
	<div id="p2" class="easyui-navpanel" style="position:relative; background:#2b2b2b;" >
		<div id="dlg1" class="easyui-dialog" style="padding:20px 6px;width:80%;" data-options="inline:true,modal:true,closed:true,title:'正在修改，请稍后...'">
				<img src="${addr}/images/wait.gif" style=" position: absolute; top: 65%; left: 50%;transform: translate(-50%, -50%);"/>
	     </div>
	</div> 
    <div id="box">
	   <div id="canvasBox">
	    <br>
	     <div class="canvasClass" id="cbox1">
	          <input type="hidden" value="0" id="canvasInput1" name="canvasSort"/>
	          <canvas id="canvas1" ></canvas>
	     </div>
	     <div class="canvasClass" id="cbox2">
	        <input type="hidden" value="0" id="canvasInput2" name="canvasSort"/>
	          <canvas id="canvas2" ></canvas>
	     </div>
	     <div class="canvasClass" id="cbox3">
	       <input type="hidden" value="0" id="canvasInput3" name="canvasSort"/>
	          <canvas id="canvas3" ></canvas>
	     </div>
	      <div class="canvasClass" id="cbox4">
	      <input type="hidden" value="0" id="canvasInput4" name="canvasSort"/>
	          <canvas id="canvas4" ></canvas>
	     </div>
	      <div class="canvasClass" id="cbox5">
	      <input type="hidden" value="0" id="canvasInput5" name="canvasSort"/>
	          <canvas id="canvas5" ></canvas>
	     </div>
	   
	      <div class="canvasClass" id="cbox6">
	      <input type="hidden" value="0" id="canvasInput6" name="canvasSort"/>
	          <canvas id="canvas6" ></canvas>
	     </div>
	      <div class="canvasClass" id="cbox7">
	      <input type="hidden" value="0" id="canvasInput7" name="canvasSort"/>
	          <canvas id="canvas7" ></canvas>
	     </div>
	      <div class="canvasClass" id="cbox8">
	       <input type="hidden" value="0" id="canvasInput8" name="canvasSort"/>
	          <canvas id="canvas8" ></canvas>
	     </div>
	      <div class="canvasClass" id="cbox9">
	         <input type="hidden" value="0" id="canvasInput9" name="canvasSort"/>
	          <canvas id="canvas9" ></canvas>
	     </div>
	      <div class="canvasClass" id="cbox10">
	          <input type="hidden" value="0" id="canvasInput10" name="canvasSort"/>
	          <canvas id="canvas10" ></canvas>
	     </div>
	   </div>
   </div>
<div id="pageUtil" style="position:fixed;margin:10px 0 0 0;bottom:0px;height:20px;left:0px;color: #2b2b2b;"></div>
<iframe id="cylinder" src="${addr}/svg/cylinder.html"></iframe>
<iframe id="logo" src="${addr}/svg/logo.html"></iframe>
<iframe id="manometer" src="${addr}/svg/manometer.html"></iframe>
<iframe id="message" src="${addr}/svg/message.html"></iframe>
<iframe id="swicth" src="${addr}/svg/swicth.html"></iframe>
<iframe id="tank"   src="${addr}/svg/tank.html"></iframe>
<iframe id="warning"   src="${addr}/svg/warning.html"></iframe>
<script type="text/javascript" src="${addr}/js/gqqtjm/aes.js"></script>
<script type="text/javascript" src="${addr}/js/fabricjs/fabric.min.js"></script>
<script type="text/javascript" src="${addr}/js/client/client_animation_20200103.js"></script>
<script type="text/javascript" src="${addr}/js/client/datasource_20200103.js"></script>
</body>
</html>