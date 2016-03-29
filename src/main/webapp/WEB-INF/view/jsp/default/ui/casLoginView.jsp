<!DOCTYPE html>
<%@page import="java.net.URLDecoder"%>
<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html lang="zh-cn">
<%
String _basePath = request.getContextPath();
String _baasBase=_basePath+"/theme/baas";
request.setAttribute("_baasBase", _baasBase);
%>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width; initial-scale=0.8;  user-scalable=0;" />
    <title>统一登录认证系统</title>
    <link href="${_baasBase }/css/bootstrap.css" rel="stylesheet" type="text/css">
	<link href="${_baasBase }/css/font-awesome.css" rel="stylesheet" type="text/css">
	<link href="${_baasBase }/css/global.css" rel="stylesheet" type="text/css">
	<link href="${_baasBase }/css/login-regsiter.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="${_baasBase }/js/jquery-1.11.1.min.js" ></script>
	<script type="text/javascript" src="${_baasBase }/js/bootstrap.js" ></script>
	<script type="text/javascript" src="${_baasBase }/js/comp.js" ></script>
	<script type="text/javascript" src="${_baasBase }/js/md5.js" ></script>
	<script type="text/javascript" src="${_baasBase }/js/datacheck.js" ></script>
	
	
	<script language="javascript" type="text/javascript"> 
		$(function(){
			var errors=$("div.login-note").html();
			if(isNull(errors)){
				$("div.login-note").css("padding","0px");
			}
			else{
				$("div.login-note").css("padding","padding","3px 10px");
			}
			
			
			$("#username").bind("blur",function(){
				resetErrMsg();
			});
			$("#password").bind("blur",function(){
				resetErrMsg();
			});
			
		});
		
		
		function resetErrMsg(){
			$("div.login-note").html("");
			$("div.login-note").css("padding","0px");
		}
		
          function encryptPwd(event){
          	if (event.keyCode == 13){//IE Chrome 回车键
          		dologin(); 
          	}
          	else {
          		if (event.which == 13){//Firefox 回车键
          			dologin(); 
          		}
          	}
          }//end of encryPwd
          
          function dologin() {
          	if(validate()){ 
  				var inputPassword = document.getElementById("password").value;
  				var onceCode = "AIOPT_SALT_KEY";
  				var passwordMd5 = hex_md5(onceCode
  						+ hex_md5(inputPassword));
  				document.getElementById("password").value = passwordMd5;
  				document.getElementById("username").value = $.trim(document
  						.getElementById("username").value);
  				//提交表单
  				document.getElementById('fm1').submit();
  				return true;
          	 }
          	else{
          		return false;
          	} 
  			
  		}//end of dologin
         
  		function validate() {
  			var username=document.getElementById("username").value;
  			var password=document.getElementById("password").value;
  			try {
  				if (isNull(username)) {
  					$("div.login-note").html("请输入手机号码或邮箱地址");
  					$("div.login-note").css("padding","3px 10px");
  					return false;
  				}else{
  					$("div.login-note").html("");
  				}
  				if (isNull(password)) {
  					$("div.login-note").html("请输入密码");
  					$("div.login-note").css("padding","3px 10px");
  					return false;
  				}else{
  					$("div.login-note").html("");
  				}
  				
  				return true;
  			} catch (ex) {
  				return false;
  			} 			
  		}//end of validate
    </script>
</head>

<body>

  <div class="login-header"><!--登录头部-->
     <div class="login-header-cnt">
       <div class="login-header-cnt-logo"><img src="${_baasBase }/images/logo.png"></div>
       <div class="login-header-cnt-mail">账户登录</div>
       </div>
     
     </div>
  
   <div class="login-wrapper">
   	   <form:form method="post" id="fm1" name="fm1" commandName="${commandName}" htmlEscape="true">
       <div class="login-wrapper-cnt">
         <div class="login-wrapper-cnt-section">
	         <ul>
		         <div class="login-note"><form:errors path="*" id="msg" cssClass="errors" element="div" htmlEscape="false" /></div>
		         <li class="login-title">账户登录</li>
		         <li class="user"><i class="icon-user"></i><form:input cssClass="required int-xlarge" cssErrorClass="error" id="username" tabindex="1" accesskey="${userNameAccessKey}" path="username" autocomplete="off" htmlEscape="true" placeholder="手机号/邮箱"/></li>
		         <span><spring:message code="screen.welcome.label.netid.accesskey" var="userNameAccessKey" /></span>
		         <li class="password"><i class="icon-lock"></i><form:password cssClass="required int-xlarge-password" cssErrorClass="error" id="password" size="25" tabindex="2" path="password"  accesskey="${passwordAccessKey}" htmlEscape="true" autocomplete="off"  placeholder="密码" onkeydown="encryptPwd(event)"/></li>
		         <span><spring:message code="screen.welcome.label.password.accesskey" var="passwordAccessKey" /></span>
		         <li class="Remb-password" style="display: none;"><span><input id="rememberMe" name="rememberMe" type="checkbox" tabindex="3"></span><span>记住账号</span></li>
		         <li><input class="login-btn" value="登 录"  accesskey="l" type="button" tabindex="4" onclick="javascript:dologin();" ></li>
		         <li class="Forget-password"><a href="身份验证-手机号.html">忘记密码？</a><a href="regsiter.html" class="right">立即注册</a></li>
	         </ul>
	         <input type="hidden" name="lt" value="${loginTicket}" />
	    	 <input type="hidden" name="execution" value="${flowExecutionKey}" />
	    	 <input type="hidden" name="_eventId" value="submit" />
         </div>
       </div>
       </form:form>
       
    </div>

   <div class="login-foot">
   ©2016 版权所有 亚信集团股份有限公司 京ICP备11005544号-15 京公网安备110108007119号
   
   </div>

</body>
</html>
