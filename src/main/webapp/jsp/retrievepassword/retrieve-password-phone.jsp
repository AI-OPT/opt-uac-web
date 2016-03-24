<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html lang="zh-cn">
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
    <label>2.设置新密码</label>
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
          <ul>
          <li class="user">
          <p class="word">选择身份验证方式</p>
          <p><select class="select-medium">
            <option>手机号码</option>
            <option>邮箱地址</option>
          </select></p> 
          
         </li>
          <li class="user">
          <p class="word">手机号码</p>
          <p>150****1111</p>
         </li>
         <li class="user">
          <p class="word">图形验证码</p>
          <p><input type="text" class="int-medium"></p>
          <p><img src="images/ret-yzm.png"></p>
          <p><A href="#">看不清?换一换</A></p>
         </li>
         <li class="user">
          <p class="word">短信验证码</p>
          <p><input type="text" class="int-medium"></p>
          <p class="huoqu"><A href="#">获取验证码</A></p>
         </li>
         
         
         <li><input type="button" class="Submit-btn" value="提  交"  onclick="location.href='找回密码-身份验证-设置新密码.html';"></li>
       
          </ul>
        
        </div>
    
    
    
    </div>
  </div>
  <%@ include file="/inc/foot.jsp"%>
</body>
</html>
