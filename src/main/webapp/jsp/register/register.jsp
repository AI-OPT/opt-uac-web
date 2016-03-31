<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<%@ include file="/inc/inc.jsp"%>
<title>注册</title>
<link href="${_base}/theme/baas/css/bootstrap.css" rel="stylesheet" type="text/css">
<link href="${_base}/theme/baas/css/font-awesome.css" rel="stylesheet" type="text/css">
<link href="${_base}/theme/baas/css/global.css" rel="stylesheet" type="text/css">
<link href="${_base}/theme/baas/css/login-regsiter.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${_base}/theme/baas/js/jquery.toggle-password.js" ></script> 
<script type="text/javascript" src="${_base}/theme/baas/js/comp.js" ></script>

<script type="text/javascript">
 (function () {
	seajs.use('app/register/register', function (RegisterPager) {
		var pager = new RegisterPager();
		pager.render();
	});
})(); 

 $(function(){
	$('#password').togglePassword({
		el: '#togglePassword'
	});
}); 

</script>
</head>

<body>

  <div class="login-header"><!--登录头部-->
     <div class="login-header-cnt">
       <div class="login-header-cnt-logo"><img src="${_base}/theme/baas/images/logo.png"></div>
       <div class="login-header-cnt-mail">账户注册</div>
       <div class="login-header-cnt-right">已有云计费账号？ <a href="#">立即登录>></a></div>
       </div>
     
  </div>

   <div class="regsiter-wrapper" id="register-form">
        <div class="regsiter-wrapper-cnt">
	         <ul>
		         <li class="regsiter-title">账户注册</li>
		         <li class="user">
		         	<input type="text" name="phone" id="phone"class="int-xxlarge-user" placeholder="手机号码作为登录账号">
		         	
		         	<span class="regsiter-note" id="errorPhoneMsg">
		         		<i class="icon-caret-left"></i>
		         		<img src="${_base}/theme/baas/images/error.png"><span id="showPhoneMsg"></span>
		         	</span>
		         </li>
		         
		         <li class="password">
		         	<input type="password" name="password" id="password"class="int-xxlarge" placeholder="密码" >
		         	<i class="icon-eye-open" id="togglePassword"></i>
		         	<span class="regsiter-note" id="errorPawMsg">
		         		<i class="icon-caret-left"></i>
		         		<img src="${_base}/theme/baas/images/error.png"><span id="showPawMsg"></span>
		         	</span>
		         </li>
		         
		         <li class="identifying">
		         	<input type="text" class="int-xlarge-identifying" placeholder="验证码" id="pictureVitenfy">
		         	<span ><A href="#"><img src="${_base}/reg/getImageVerifyCode" id="randomImg"></A></span>
		         	<span ><a href="#"id="refresh">换一个</a></span>
		         	<span class="regsiter-note" id="errorPicMsg">
		         		<i class="icon-caret-left"></i>
		         		<img src="${_base}/theme/baas/images/error.png"><span id="showPicMsg"></span>
		         	</span>
		         </li>
		         <li class="SMSidentifying">
		         	<input type="text" class="int-xlarge-SMSidentifying" placeholder="短信验证码" id="phoneVerifyCode">
		         	<span class="yzm">
		         	 		<input id="PHONE_IDENTIFY"  type="button" value="获取验证码" >
		         	 </span>
		         		
		         	<span class="regsiter-note" id="errorSmsMsg">
		         		<i class="icon-caret-left"></i><img src="${_base}/theme/baas/images/error.png">
		         		<span id="showSmsMsg"></span>
		         	</span>
		         </li>
		         <li>
		         	<input type="button" class="regsiter-btn" value="注 册"  id="BTN_REGISTER">
		         	<input type="hidden" id="errorFlag">
		         </li>
		         <li class="zuns">* 注册表示您同意遵守<A href="#">《云计费服务条款》</A></li>
	
	         </ul>
   		</div>
    </div>
   <div class="login-foot">
   ©2016 版权所有 亚信集团股份有限公司 京ICP备11005544号-15 京公网安备110108007119号
   
   </div>
   
  

</body>
</html>
