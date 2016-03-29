<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<%@ include file="/inc/inc.jsp"%>
<title>注册－邮箱绑定</title>
<link href="${_base}/theme/baas/css/bootstrap.css" rel="stylesheet" type="text/css">
<link href="${_base}/theme/baas/css/font-awesome.css" rel="stylesheet" type="text/css">
<link href="${_base}/theme/baas/css/global.css" rel="stylesheet" type="text/css">
<link href="${_base}/theme/baas/css/login-regsiter.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${_base}/theme/baas/js/comp.js" ></script>
<script type="text/javascript">
(function () {
	seajs.use('app/register/register-email', function (RegisterEmaillPager) {
		var pager = new RegisterEmaillPager();
		pager.render();
	});
})();

</script>
</head>

<body>

  <div class="login-header"><!--登录头部-->
     <div class="login-header-cnt">
       <div class="login-header-cnt-logo"><img src="${_base}/theme/baas/images/logo.png"></div>
       <div class="login-header-cnt-mail">邮箱绑定</div>
       </div>
     
     </div>
  
   <div class="regsiter-wrapper">
        <div class="regsiter-email-cnt">
          <ul>
          <li class="user">
          <span><p class="word">邮箱地址</p></span>
          	<input type="text" class="int-medium" placeholder="" id="email" name="email">
          	<input type="hidden" name="accountId" id="accountId" value="${requestScope.accountId}"/>
          <span class="yzm"><p><A href="#" id="getIdentify">获取校验码</A></p></span>
          <span class="regsiter-note" id="errorEmailMsg">
         		<i class="icon-caret-left"></i><img src="${_base}/theme/baas/images/error.png">
         		<span  id="showErroeEmail"></span>
		   </span>
         </li>
          <li class="user">
          <p class="word">邮箱校验码</p>
          <p><input type="text" class="int-medium" placeholder="" id="identifyCode"></p>
         <span class="regsiter-note" id="errorEmIdentifyMsg">
         		<i class="icon-caret-left"></i><img src="${_base}/theme/baas/images/error.png">
         		<span  id="showErroeEmIdentify"></span>
		   </span>
         </li>
         <li class="reminder">
         <p><i class="icon-lightbulb"></i>温馨提示</p>
         <p class="reminder-word">
         <span>1 . 邮箱也可以作为登录账号</span>
         <span>2 . 邮箱可以帮助您找回账户密码</span>
         <span>3 . 接收产品开通、到期、故障等通知服务</span>
         </p>
         </li>
         
         <li class="regsiter-email-btn">
         	<input type="button" value="下次再说" class="next-btn" id="BTN_PASS" name="BTN_PASS">
         	<input type="button" value="提交" class="next-btn next-btn-hover" id="BTN_SUBMIT" name="BTN_SUBMIT">
         </li>
          </ul>
        
        </div>
       
    
     
    </div>

   <div class="login-foot">
   ©2016 版权所有 亚信集团股份有限公司 京ICP备11005544号-15 京公网安备110108007119号
   
   </div>
   
  

</body>
</html>
