<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html lang="zh-cn">
<head>
<%@ include file="/inc/inc.jsp"%>
    <title>无标题文档</title>
</head>

<body>
  <%@ include file="/inc/head-user.jsp"%>
  <%@ include file="/inc/head-logonav.jsp"%>
  
  <div class="wrapper">
   <div class="Retrieve-password">
    
         <div class="Retrieve-steps">
         <div class="Retrieve-steps-round">
  <div class="finished"><!--蓝色圆圈带蓝线 finished-->
    <div class="wrap">
      <div class="round"><i class="icon-user"></i></div>
      <div class="bar"></div>
    </div>
    <label>1.身份验证</label>
  </div>
  <div class="todo"><!--圆圈蓝色 current-->
    <div class="wrap">
      <div class="round"><i class="icon-pencil"></i></div>
      <div class="bar"></div>
    </div>
    <label>2.重置密码</label>
  </div>
  <div class="todo"><!--圆圈灰色 todo-->
    <div class="wrap">
      <div class="round"><i class=" icon-ok"></i></div>
      
    </div>
    <label>3.完成</label>
  </div>

</div>
 </div><!--步骤结束-->
         
     <!--表单验证-->
    <div class="Retrieve-cnt">
      <input type="hidden" id="confirmType" value="1">
      <ul>
         <li class="user">
          <p class="word" id="confirmTypeName">已验证手机</p>
          <p id="phone">${confirmInfo.phone}</p> 
          <p id="email">${confirmInfo.email}</p> 
          <p class="tong"><A id="changeConfirmType">通过已验证邮箱验证</A></p>
         </li>
         <li class="user">
          <p class="word">图形验证码</p>
          <p><input type="text" class="int-medium" placeholder="" id="pictureVerifyCode"></p>
          <p><img id="random_img" src="${_base}/center/password/getImageVerifyCode"></p>
          <p><A id="changeImage">看不清?换一换</A>
          <span class="regsiter-note" id="pictureVerifyMsgDiv" style="display:none">
			     <i class="icon-caret-left"></i><img src="${_base}/theme/baas/images/error.png">
			     <span id="pictureVerifyMsg"></span>
		  </span>
		  </p>
         </li>
         <li class="user">
	          <p class="word" id="verifyName">短信校验码</p>
	          <p><input type="text" class="int-medium" id="verifyCode"></p>
	          <p class="huoqu">
	          	<input id="sendVerify"  type="button" class="send-button" value="获取校验码" >
	          <span class="regsiter-note" id="verifyCodeMsgDiv" style="display:none">
			     <i class="icon-caret-left"></i><img src="${_base}/theme/baas/images/error.png">
			     <span id="verifyCodeMsg"></span>
			  </span>
	          </p>
         </li>
         
         <li><input id="submitBtn" type="button" class="Submit-btn" value="提  交"></li>
       </ul>
    </div>
    
    
    
    </div>
  </div>
  <%@ include file="/inc/foot.jsp"%>
  <script type="text/javascript">
  		var phone = "${confirmInfo.phone}";
  		var email = "${confirmInfo.email}";
		(function() {
			seajs.use([ 'app/center/password/confirmInfo' ], function(ConfirmInfoPager) {
				var pager = new ConfirmInfoPager({
					element : document.body
				});
				pager.render();
			});
		})(); 
  </script>
</body>
</html>
