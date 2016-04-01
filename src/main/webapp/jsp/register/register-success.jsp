<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<%@ include file="/inc/inc.jsp"%>
<title>注册成功</title>
<link href="${_base}/theme/baas/css/bootstrap.css" rel="stylesheet" type="text/css">
<link href="${_base}/theme/baas/css/font-awesome.css" rel="stylesheet" type="text/css">
<link href="${_base}/theme/baas/css/global.css" rel="stylesheet" type="text/css">
<link href="${_base}/theme/baas/css/login-regsiter.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${_base}/theme/baas/js/jquery-1.11.1.min.js" ></script>
<script type="text/javascript" src="${_base}/theme/baas/js/bootstrap.js" ></script>
<script type="text/javascript" src="${_base}/theme/baas/js/comp.js" ></script>
 <script type="text/javascript">
(function () {
	seajs.use('app/register/register-success', function (RegisterSucessPager) {
		var pager = new RegisterSucessPager();
		pager.render();
	});
})();
</script>
</head>

<body>

  <div class="login-header"><!--登录头部-->
     <div class="login-header-cnt">
       <div class="login-header-cnt-logo"><img src="${_base}/theme/baas/images/logo.png"></div>
       <div class="login-header-cnt-mail">注册成功</div>
       </div>
     
     </div>
  
   <div class="regsiter-wrapper">
        <div class="regsiter-success-cnt">
         <p class="clts">恭喜您，注册成功！
         		<input type="hidden" name="accountIdKey" id="accountIdKey" value="${requestScope.accountIdKey}"/>
         </p>
          <p>
          	<span id="jumpTo">5</span>s后会自动跳转到首页。
          </p>
        <script type="text/javascript"></script>  
        </div>
       
    
     
    </div>

   <div class="login-foot">
   ©2016 版权所有 亚信集团股份有限公司 京ICP备11005544号-15 京公网安备110108007119号
   
   </div>
   
  

</body>
</html>
