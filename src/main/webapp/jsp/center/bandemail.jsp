<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<head>
    <%@ include file="/inc/inc.jsp"%>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width; initial-scale=0.8;  user-scalable=0;" />
    <title>绑定邮箱-身份验证</title>
</head>

<body>
<div class="header">
  <div class="head-auto">
   <div class="logo"><a href="#"><img src="images/about.png"></a></div>
   <div class="breadcrumb">
    <ul>
     <li>修改手机号</li>
    </ul>
   </div><div class="user">
    <div class="user-cnt">
     <p><img src="images/login_user.png"><span>你好，古珍珍</span><a href="#">退出</a></p>
    </div>
   </div>
  </div>
  </div>
  <div class="box">
  <div class="subnav" >
   <div class="nav" >
     <ul>
      
      <li><a href="基本信息-初始状态-可填.html">基本信息</a></li>
      <li><a href="账户安全-修改密码.html">修改密码</a></li>
      <li class="current"><a href="账户安全-修改手机号.html">修改手机号</a></li>
      <li><a href="账户安全-修改邮箱.html">修改邮箱</a></li>
      <li><a href="邮箱绑定-身份验证.html">邮箱设置</a></li>
     </ul>
     </div>
    </div>
  <div class="wrapper">
  <div class="Retrieve-password">
    
         <div class="Retrieve-steps newsteps">
         <div class="Retrieve-steps-round">
  <div class="finished"><!--蓝色圆圈带蓝线 finished-->
    <div class="wrap">
      <div class="round"><i class="icon-user"></i></div>
      <div class="bar"></div>
    </div>
    <label>1.身份验证</label>
  </div>
  <div class="current"><!--圆圈蓝色 current-->
    <div class="wrap">
      <div class="round"><i class="icon-pencil"></i></div>
      <div class="bar"></div>
    </div>
    <label>2.绑定邮箱</label>
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
          <p class="word">邮箱地址</p>
          <p><input type="text" class="int-medium" placeholder=""></p>
          <p class="huoqu"><a href="#">获取邮箱校验码</a></p>
         </li>
         <li class="user">
          <p class="word">邮箱校验码</p>
          <p><input type="text" class="int-medium" placeholder=""><span class="regsiter-note">密码必须由字母和数字/符号组成，不能低于6个字符</span></p>
         </li>
         
         
         <li><input type="button" class="Submit-btn" value="提 交" onclick="location.href='邮箱绑定-完成.html';"></li>
       
          </ul>
        </div>
    
    </div>
   </div>
  </div>
  <div class="footer-index">
©2016 版权所有 亚信集团股份有限公司 京ICP备11005544号-15 京公网安备110108007119号
</div>
</body>
</html>
