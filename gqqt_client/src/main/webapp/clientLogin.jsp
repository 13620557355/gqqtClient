<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- 核心类库 --> 
<%@include file="/public/path.jspf" %>
  <%@include file="/public/mobile.jspf" %>	
 <script type="text/ecmascript" src="${addr}/js/gqqtjm/md5.js"></script>
 <script type="text/ecmascript" src="${addr}/js/gqqtjm/sha1.js"></script>
  <script type="text/ecmascript" src="${addr}/js/loginValidate.js"></script>
</head>
<body>
     <input id="login_error" name="login_error" type="hidden" value="<%=session.getAttribute("login_error")%>" />
    <div style="margin-bottom:20px" > </div>
	<div class="easyui-navpanel" style="position:relative;padding:20px">
		<header>
			<div class="m-toolbar" style="line-height:60px;">
				<div class="m-title" plain="true" outline="true" style="font-size:40px;line-height:40px;"></div>
				<div class="m-right">
					<a href="javascript:void(0)" class="easyui-linkbutton" plain="true" outline="true" onclick="$('#submit').form('reset')" style="width:60px;">刷新</a>
				</div>
			</div>
		</header>
		<form  id="submit">
		     <div style="margin-bottom:40px" > </div>
		  	 <div style="margin-bottom:15px;text-align:center;">
				<!--  <img alt="gqqt" src="${addr}/images/newlogo.png" style="height:80px;weight:38px;" />-->
			</div>
			<div style="margin-bottom:50px;text-align:center;font-size:20px;color:#1D1F88;font-weight: bold;">
				客户端登陆界面
			</div>
			<input id="loginType" name="loginType" type="hidden" value="client" />		
			<div style="margin-bottom:10px;text-align:center">
				<label>物联网地址:</label>
				<input id="iotAddr" name="iotAddr" placeholder="请输入8位物联网地址"  type="text" value="" oninput = "value=value.replace(/[^\d]/g,'')" maxlength="8" style="width:77%;height:50px;" />
			</div>
			 <span style="left:20%;color:red;font-size:20px"></span>
			<div style="text-align:center;margin-top:0px;">
			    <input type="button"  value="登录" id="submit1" class="easyui-linkbutton" style="width:60%;height:50px;font-size:16px;background:#24748F;color:#FFFFFF" onclick="checkInputClient();"/>
			</div>
			
				<!-- 
		<footer style="position:fixed; bottom:20px;">
			  <div  id="dekalong" style="text-align:center;" >
					广州德卡隆电子科技有限公司   |技术支持 | 
					 邮箱：dekalong@qq.com | 电话：020-39158005 
					网址：<a href="http://www.dekalong.cn/" target="_Blank">www.dekalong.cn</a>	
			  </div>	
	        </footer>
	     --> 
	     <!--  
	         <footer style="position:fixed; bottom:40px;">
			  <div  id="dekalong" style="text-align:center;" >
					<a  onclick="window.open('http://www.beian.miit.gov.cn','_blank');">粤ICP备18041034号-2</a>	
			  </div>	
	        </footer> 
	        -->
		</form>
	
	</div>
</body>
</html>