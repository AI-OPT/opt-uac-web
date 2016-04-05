<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html >
<head>
<%@ include file="/inc/inc.jsp"%>
    <title>无标题文档</title>
     <link href="${_base}/theme/baas/css/bootstrap.css" rel="stylesheet" type="text/css">
     <link href="${_base}/theme/baas/css/font-awesome.css" rel="stylesheet" type="text/css">
     <link href="${_base}/theme/baas/css/frame.css" rel="stylesheet" type="text/css">
     <link href="${_base}/theme/baas/css/global.css" rel="stylesheet" type="text/css">
     <link href="${_base}/theme/baas/css/modular.css" rel="stylesheet" type="text/css">
     <script type="text/javascript" src="${_base}/theme/baas/js/frame.js" ></script>
     <script type="text/javascript" src="${_base}/theme/baas/js/comp.js" ></script>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
(function () {
	seajs.use('app/center/baseinfo/initBaseInfo', function (BaseInfoPager) {
		var pager = new BaseInfoPager();
		pager.render();
	});
	
})(); 

</script>
</head>

<body>
  <%@ include file="/inc/head-user.jsp"%>
  <%@ include file="/inc/head-logonav.jsp"%>
  <div class="wrapper">
      
      <div class="Basic-information">
      <div class="information-wrap">
      
           <div class="information-left"><!--基本信息左边-->
          
          <div class="information-left-account">
          <ul>
          <li ><img src="${_base}/theme/baas/images/account.png" class="account-img"></li>
          <li class="word">账户信息</li>
          </ul>
          </div>
          
          <div class="information-cnt">
          <ul>
          <li class="user">
          <p class="word">用户名</p>
          <p>
          	${accountInfo.phone}
          	<input type="hidden" id="accountId" value="${accountInfo.accountId}">
          	<input type="hidden" id="flag">
          </p>
          </li>
          
          <li class="user">
          <p class="word">昵称</p>
          <p class="ctn-a">
          ${accountInfo.nickName}
          <input type="hidden" value="${accountInfo.nickName}" id="nk">
          <a href="#"><i class="icon-edit"></i></a>
          </p>
          <p class="ctn-b" style=" display:none;" id="setnick">
          	<input type="text" id="nickName" class="int-medium" placeholder="">
          </p>
          <span  id="errorNickNameMsg">
		     <i class="icon-caret-left"></i>
		     <img src="${_base}/theme/baas/images/error.png"><span id="showNickNameMsg"></span>
		  </span>
          </li>
          
           <li class="user">
          <p class="word">手机号码</p>
          <p>${accountInfo.phone}</p>
          <p><a href="${_base}/center/phone/confirminfo">去修改</a></p>
          </li>
          
           <li class="user">
           <div id="bandEmail">
	           	<p class="word">邮箱</p>
	          	<p>
	          		<a href="${_base}/center/email/setEmail"><i class="icon-link"></i>绑定邮箱</a>
	          		<input type="hidden" id="email" value="${accountInfo.email}">
	          	</p>
           </div>
          <div id="haveEmail">
	          <p class="word">邮箱</p>
	          <p id="email">${accountInfo.email}</p>
	          <p><a href="${_base}/center/email/confirminfo">去修改</a></p>
          </div>
          </li>

          </ul>
 
        </div>
       </div>  
       
       
            <div class="information-left information-right"><!--基本信息右边-->
          
          <div class="information-left-account">
          <ul>
          <li ><img src="${_base}/theme/baas/images/account-qiy.png" class="account-img"></li>
          <li class="word">企业信息</li>
          </ul>
          </div>
          
          <div class="information-cnt">
          <ul>
	          <div id="allInfo">
		          <li class="user">
		          <p class="word">企业名称</p>
		          <p>
		          	<input type="text" class="int-medium" placeholder="" id="tenantName">
		          </p>
		          <span  id="errorTenMsg">
		         		<i class="icon-caret-left"></i>
		         		<img src="${_base}/theme/baas/images/error.png"><span id="showTenMsg"></span>
		         	</span>
		          </li>
		          
		          <li class="user">
		          <p class="word">企业类型</p>
		          <p>
		          	<select class="select-medium" id="indutry">
		          	</select>
		          </p>
		          <span  id="errorTypeMsg">
		         		<i class="icon-caret-left"></i>
		         		<img src="${_base}/theme/baas/images/error.png"><span id="showTypeMsg"></span>
		         	</span>
		          </li>
	   			</div>
	   			<div id="oneInfo">
	   				<ul>
			          <li class="user">
			          <p class="word">企业名称</p>
			          <p >
			          	${accountInfo.tenantName}
			          	<input type="hidden" id="tenant" value="${accountInfo.tenantName}">
			          </p>
			          </li>
			          <li class="user">
			          <p class="word">企业类型</p>
			          <p>${accountInfo.industryCode}</p>
			          </li>
          			</ul>
	   			</div>

          </ul>
 
        </div>
          
        
        
        </div>
        
        </div>
      
        <div class="btn_wrap" >
        	<input type="button" class="information-btn" value="提  交" id="submitBtn" >
        	
        </div>
        
    
  
   </div>
  </div>
  <%@ include file="/inc/foot.jsp"%>
</body>
</html>
