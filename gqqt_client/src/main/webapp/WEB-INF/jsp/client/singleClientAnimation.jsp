<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
     <%@include file="/public/path.jspf" %>
     <%@include file="/public/mobile.jspf" %>
    <style>
      html{background:#2b2b2b;}
      body{
            margin: 0;
            padding: 0;
            border: 0;
            width:100%;
            height:100%;
            text-align:center;
       }
       iframe{display:none}
       .box {
		    width:50%;
		    height: 560px;
		    margin: 0 auto;
		    background: blue;
		    overflow: hidden;
		    position: relative;	 
		}
       #canvasBox{position: absolute; left: 50%; top: 50%;transform: translate(-50%, -50%);}
    </style>
</head>
<body >
  <input id="driUuid" name="driUuid" type="hidden" value="<%=request.getAttribute("driUuid")%>" />
   	<div id="p2" class="easyui-navpanel" style="position:relative; background:#2b2b2b;" >
		<header style="background:#2b2b2b;border-color:#2b2b2b" >
			<div class="m-toolbar" >
				<div class="m-left" >
					<a  id="returnOld" href="javascript:void(0)" ></a>
				</div>
			</div>
		</header>
			<div id="dlg1" class="easyui-dialog" style="padding:20px 6px;width:80%;" data-options="inline:true,modal:true,closed:true,title:'正在修改，请稍后...'">
				<img src="${addr}/images/wait.gif" style=" position: absolute; top: 65%; left: 50%;transform: translate(-50%, -50%);"/>
	        </div>
	</div>
    <div id="box">
	   <div id="canvasBox">
	    <canvas id="canvas" style="display:block"></canvas>
	   </div>
   </div>
<iframe id="cylinder" src="${addr}/svg/cylinder.html"></iframe>
<iframe id="logo" src="${addr}/svg/logo.html"></iframe>
<iframe id="manometer" src="${addr}/svg/manometer.html"></iframe>
<iframe id="message" src="${addr}/svg/message.html"></iframe>
<iframe id="swicth" src="${addr}/svg/swicth.html"></iframe>
<iframe id="tank"   src="${addr}/svg/tank.html"></iframe>
<iframe id="warning"   src="${addr}/svg/warning.html"></iframe>
<script type="text/javascript" src="${addr}/js/fabricjs/fabric.min.js"></script>
<script type="text/javascript" src="${addr}/js/animation/animation.js"></script>
<script type="text/javascript" src="${addr}/js/client/clientheartbeat.js"></script>

</body>
</html>