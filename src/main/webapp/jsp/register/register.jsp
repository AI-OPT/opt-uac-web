<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<meta charset="UTF-8">
<%@ include file="/inc/inc.jsp"%>
<title>注册</title>
<link href="${_base}/theme/baas/css/bootstrap.css" rel="stylesheet" type="text/css">
<link href="${_base}/theme/baas/css/font-awesome.css" rel="stylesheet" type="text/css">
<link href="${_base}/theme/baas/css/global.css" rel="stylesheet" type="text/css">
<link href="${_base}/theme/baas/css/login-regsiter.css" rel="stylesheet" type="text/css">
<%-- <script type="text/javascript" src="${_base}/theme/baas/js/jquery-1.11.1.min.js" ></script>
<script type="text/javascript" src="${_base}/theme/baas/js/bootstrap.js" ></script> --%>
<script type="text/javascript" src="${_base}/theme/baas/js/comp.js" ></script>

<script type="text/javascript">
(function () {
	seajs.use('app/register/register', function (RegisterPager) {
		var pager = new RegisterPager();
		pager.render();
	});
})();

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
		         <label for="phone" class="int-xxlarge-user">
		         	<input type="text" name="phone" id="phone"class="int-xxlarge-user" placeholder="手机号码作为登录账号">
		         </label>
		         	<span class="regsiter-note">
		         		<i class="icon-caret-left"></i>
		         		<img src="${_base}/theme/baas/images/error.png">密码必须由字母和数字/符号组成，不能低于6个字符
		         	</span>
		         </li>
		         <li class="password">
		         <label for="phone" class="int-xxlarge">
		         	<input type="password" name="password" id="password"class="int-xxlarge" placeholder="密码" >
		         </label>
		         	<i class="icon-eye-open"></i>
		         	<span class="regsiter-note">
		         		<i class="icon-caret-left"></i>
		         		<img src="${_base}/theme/baas/images/warning.png">密码必须由字母和数字/符号组成，不能低于6个字符
		         	</span>
		         </li>
		         
		         <li class="identifying">
		         	<input type="text" class="int-xlarge-identifying" placeholder="验证码" >
		         	<span>
		         		<A href="#"><img src="${_base}/theme/baas/images/yzm.png"></A></span><span><A href="#">看不清？换一个</A>
		         	</span>
		         </li>
		         <li class="SMSidentifying">
		         	<input type="text" class="int-xlarge-SMSidentifying" placeholder="短信验证码" >
		         	<span class="yzm"><A href="#">获取验证码</A></span>
		         	<span class="regsiter-note"><i class="icon-caret-left"></i><img src="${_base}/theme/baas/images/correct.png">密码必须由字母和数字/符号组成，不能低于6个字符
		         	</span>
		         </li>
		         <li><input type="button" class="regsiter-btn" value="注 册"  id="BTN_REGISTER"></li>
		         <li class="zuns">* 注册表示您同意遵守<A href="#">《云计费服务条款》</A></li>
	
	         </ul>
   		</div>
    </div>
   <div class="login-foot">
   ©2016 版权所有 亚信集团股份有限公司 京ICP备11005544号-15 京公网安备110108007119号
   
   </div>
   
  

</body>
</html>
