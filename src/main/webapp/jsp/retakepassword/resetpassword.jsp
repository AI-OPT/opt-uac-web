<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
    <%@ include file="/inc/inc.jsp"%>
    <meta charset="utf-8">
    <title>无标题文档</title>
    <link href="${_base}/theme/baas/css/bootstrap.css" rel="stylesheet" type="text/css">
     <link href="${_base}/theme/baas/css/font-awesome.css" rel="stylesheet" type="text/css">
     <link href="${_base}/theme/baas/css/frame.css" rel="stylesheet" type="text/css">
     <link href="${_base}/theme/baas/css/global.css" rel="stylesheet" type="text/css">
     <link href="${_base}/theme/baas/css/modular.css" rel="stylesheet" type="text/css">
     <script type="text/javascript" src="${_base}/theme/baas/js/jquery-1.11.1.min.js" ></script>
     <script type="text/javascript" src="${_base}/theme/baas/js/bootstrap.js" ></script>
     <script type="text/javascript" src="${_base}/theme/baas/js/frame.js" ></script>
     <script type="text/javascript" src="${_base}/theme/baas/js/comp.js" ></script>
</head>

<body>
  <%@ include file="/inc/head-pwd.jsp"%>
  <div class="wrappera">
      <!-- 步骤开始 -->
   <div class="Retrieve-password">
    <div class="Retrieve-steps">
      <div class="Retrieve-steps-round">
  		<div class="finished"><!--蓝色圆圈带蓝线 finished-->
		    <div class="wrap">
		      <div class="round"><i class="icon-user"></i></div>
		      <div class="bar"></div>
		    </div>
    		<label>1.填写用户名</label>
  		</div>
  		<div class="finished"><!--圆圈蓝色 current-->
		    <div class="wrap">
		      <div class="round"><i class="icon-key"></i></div>
		      <div class="bar"></div>
		    </div>
		    <label>2.身份验证</label>
	  	</div>
	  	<div class="finished"><!--圆圈蓝色 current-->
		    <div class="wrap">
		      <div class="round"><i class="icon-pencil"></i></div>
		      <div class="bar"></div>
		    </div>
		    <label>3.设置新密码</label>
	  	</div>
  		<div class="todo"><!--圆圈灰色 todo-->
		    <div class="wrap">
		      <div class="round"><i class=" icon-ok"></i></div>
		      
		    </div>
		    <label>4.完成</label>
  		</div>
	  </div>
 	</div>
 	<!--步骤结束-->
         
     <!--表单验证-->
    <div class="Retrieve-cnt">
          <ul>
          <li class="user">
	          <p class="word">新密码</p>
	          <p><input type="password"class="int-medium" id="newPassword">
	          <span class="regsiter-note" id="newPwdMsgDiv" style="display:none">
			     <i class="icon-caret-left"></i><img src="${_base}/theme/baas/images/error.png">
			     <span id="newPwdMsg"></span>
			  </span><p>
			  <div class="Set-password" style="display:none">
				          <p class="low" id="strength_L">
					          <span class="f00"></span>
					          <span>低</span>
				          </p>
				           <p class="in" id="strength_M">
					          <span class="eb6100"></span>
					          <span>中</span>
				          </p>
				          <p class="gao" id="strength_H">
					          <span class="green"></span>
					          <span>高</span>
				          </p>
	          			</div>	
          </li>
          
          <li class="user">
	          <p class="word">确认密码</p>
	          <p><input type="password" class="int-medium" id="confirmPassword">
	          <span class="regsiter-note" id="confirmPwdMsgDiv" style="display:none">
			     <i class="icon-caret-left"></i><img src="${_base}/theme/baas/images/error.png">
			     <span id="confirmPwdMsg"></span>
			  </span>
			  </p>
          </li>
       
         
          <li><input type="button" class="Submit-btn" value="提  交" id="submitBtn"></li>
       
          </ul>
        
        </div>
    
    
    
    </div>
  </div>
  <%@ include file="/inc/foot.jsp"%>
  <script type="text/javascript">
  		var uuid = "${uuid}";
		(function() {
			seajs.use([ 'app/retakepassword/resetpassword' ], function(ResetPasswordPage) {
				var pager = new ResetPasswordPage({
					element : document.body
				});
				pager.render();
			});
		})(); 
  </script>
</body>
</html>
