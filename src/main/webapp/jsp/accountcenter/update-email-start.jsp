<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width; initial-scale=0.8;  user-scalable=0;" />
    <title>无标题文档</title>
     <link href="css/bootstrap.css" rel="stylesheet" type="text/css">
     <link href="css/font-awesome.css" rel="stylesheet" type="text/css">
     <link href="css/frame.css" rel="stylesheet" type="text/css">
     <link href="css/global.css" rel="stylesheet" type="text/css">
     <link href="css/modular.css" rel="stylesheet" type="text/css">
     <script type="text/javascript" src="js/jquery-1.11.1.min.js" ></script>
     <script type="text/javascript" src="js/bootstrap.js" ></script>
     <script type="text/javascript" src="js/frame.js" ></script>
     <script type="text/javascript" src="js/comp.js" ></script>
</head>

<body>
<!--右侧弹出框-->
<div class="msg-cnt">
 <div class="p">
      <a ng-click="$hide()" class="pull-right text-muted"><img src="images/close.png"></a>
      审批待办事项
    </div>
 <div class="box-row">
      <div class="box-cell">
        <div class="box-inner">
          <div class="list-group no-radius no-borders">
            <a class="list-group-item p-h-md p-v-xs">
                <i class="icon-circle text-success text-xs m-r-xs"></i>
                <span>审批待办事项1</span>
            </a>
            <a class="list-group-item p-h-md p-v-xs">
                <i class="icon-circle text-success text-xs m-r-xs"></i>
                <span> 审批待办事项5条</span>
            </a>
            <a class="list-group-item p-h-md p-v-xs">
                <i class="icon-circle text-warning text-xs m-r-xs"></i>
                <span>审批待办事项2</span>
            </a>
            <a class="list-group-item p-h-md p-v-xs">
                <i class="icon-circle text-muted-lt text-xs m-r-xs"></i>
                <span>审批待办事项5条</span>
            </a>
            <a class="list-group-item p-h-md p-v-xs">
                <i class="icon-circle text-muted-lt text-xs m-r-xs"></i>
                <span>审批待办事项3个审批</span>
            </a>
            <a class="list-group-item p-h-md p-v-xs">
                <i class="icon-circle text-muted-lt text-xs m-r-xs"></i>
                <span>审批待办事项</span>
            </a>
            <a class="list-group-item p-h-md p-v-xs">
                <i class="icon-circle text-muted-lt text-xs m-r-xs"></i>
                <span>审批待办事项</span>
            </a>
            <a class="list-group-item p-h-md p-v-xs">
                <i class="icon-circle text-muted-lt text-xs m-r-xs"></i>
                <span>审批待办事项5条</span>
            </a>
            <a class="list-group-item p-h-md p-v-xs">
                <i class="icon-circle text-muted-lt text-xs m-r-xs"></i>
                <span>审批待办事项</span>
            </a>
            <a class="list-group-item p-h-md p-v-xs">
                <i class="icon-circle text-muted-lt text-xs m-r-xs"></i>
                <span>审批待办事项</span>
            </a>
          </div>
        </div>
      </div>
    </div>
</div>
<!--右侧弹出框结束-->


  <div class="header">
   <div class="logo">
    <a href="#">LOGO</a>
    <i class="icon-angle-down"></i> 
   </div>
   <div class="subnav">
   <div class="nav">
     <ul>
      <li><a href="#">账户中心</a></li>
      <li><a href="基本信息-初始状态-可填.html">基本信息</a></li>
      <li><a href="账户安全-修改密码.html">修改密码</a></li>
      <li><a href="账户安全-修改手机号.html">修改手机号</a></li>
      <li class="current"><a href="账户安全-修改邮箱.html">修改邮箱</a></li>
      <li><a href="邮箱绑定-身份验证.html">邮箱设置</a></li>
     </ul>
     </div>
    </div>
   <div class="breadcrumb">
    <ul>
     <li>账号管理</li>
     <li><i class="icon-angle-right"></i>修改邮箱</li>
    </ul>
   </div>
   <div class="user">
    <div class="msg"><a href="#"><i class="icon-bell-alt"></i><span class="badge">4</span></a></div>
    <div class="user-cnt">
     <p><img src="images/login_user.png"><span>你好，古珍珍</span><i class="icon-angle-down"></i></p>
     <ul style="display:none;">
      <li><a href="#">个人中心</a></li>
      <li><a href="#">修改密码</a></li>
      <li><a href="#">退出</a></li>
     </ul>
    </div>
   </div>
  </div>
  <div class="navbg"></div>
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
    <label>2.设置新邮箱</label>
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
          <p class="word">已验证手机</p>
         <p>150****1010</p> 
          <p class="tong"><A href="#">通过已验证邮箱验证</A></p>
         </li>
         <li class="user">
          <p class="word">图形验证码</p>
          <p><input type="text" class="int-medium" placeholder=""></p>
          <p><img src="images/ret-yzm.png"></p>
          <p><A href="#">看不清?换一换</A></p>
         </li>
         <li class="user">
          <p class="word">短信校验码</p>
          <p><input type="text" class="int-medium" placeholder=""></p>
          <p class="huoqu"><A href="#">获取短信校验码</A></p>
         </li>
         
         <li><input type="button" class="Submit-btn" value="提  交" onclick="location.href='账户安全-修改邮箱-设置新邮箱.html';" ></li>
       
          </ul>
        </div>
    
    
    
    </div>
  </div>
  <div class="footer">©2016 版权所有 亚信集团股份有限公司 京ICP备11005544号-15 京公网安备110108007119号</div>
</body>
</html>
