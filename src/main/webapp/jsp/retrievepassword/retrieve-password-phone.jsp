<%@ page contentType="text/html;charset=UTF-8" language="java"%>
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
   <div class="logo-no">
    <a href="#">LOGO</a>
  </div>
   
   <div class="breadcrumb">
    <ul>
     <li>找回密码</li>
    
    </ul>
   </div>

  </div>
  <div class="navbg"></div>
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
  <div class="footer">©2016 版权所有 亚信集团股份有限公司 京ICP备11005544号-15 京公网安备110108007119号</div>
</body>
</html>
