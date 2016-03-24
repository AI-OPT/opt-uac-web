<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
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
      <li class="current"><a href="基本信息-初始状态-可填.html">基本信息</a></li>
      <li><a href="账户安全-修改密码.html">修改密码</a></li>
      <li><a href="账户安全-修改手机号.html">修改手机号</a></li>
      <li><a href="账户安全-修改邮箱.html">修改邮箱</a></li>
      <li><a href="邮箱绑定-身份验证.html">邮箱设置</a></li>
     </ul>
     </div>
    </div>
   <div class="breadcrumb">
    <ul>
     <li>账号中心</li>
     <li><i class="icon-angle-right"></i>基本信息</li>
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
      
      <div class="Basic-information">
      <div class="information-wrap">
      
           <div class="information-left"><!--基本信息左边-->
          
          <div class="information-left-account">
          <ul>
          <li ><img src="images/account.png" class="account-img"></li>
          <li class="word">账户信息</li>
          </ul>
          </div>
          
          <div class="information-cnt">
          <ul>
          <li class="user">
          <p class="word">用户名</p>
          <p>156****1120</p>
          </li>
          
          <li class="user">
          <p class="word">昵称</p>
    
          <p class="ctn-a">cici<a href="#"><i class="icon-edit"></i></a></p>
          <p class="ctn-b" style=" display:none;"><input type="password" class="int-medium" placeholder=""></p>
          </li>
          
           <li class="user">
          <p class="word">手机号码</p>
          <p>156****1120</p>
          <p><a href="账户安全-修改手机号.html">去修改</a></p>
          </li>
          
          <li class="user">
          <p class="word">邮箱</p>
          <p>g***n@126.com</p>
          <p><a href="账户安全-修改邮箱.html">去修改</a></p>
          </li>

          </ul>
 
        </div>
       </div>  
       
       
            <div class="information-left information-right"><!--基本信息右边-->
          
          <div class="information-left-account">
          <ul>
          <li ><img src="images/account-qiy.png" class="account-img"></li>
          <li class="word">企业信息</li>
          </ul>
          </div>
          
          <div class="information-cnt">
          <ul>
          <li class="user">
          <p class="word">企业名称</p>
          <p>亚信科技</p>
          </li>
          
          <li class="user">
          <p class="word">企业类型</p>
          <p>计算机软件</p>
          </li>
   

          </ul>
 
        </div>
          
        
        
        </div>
        
        </div>
      
        <div class="btn_wrap" ><input type="button" class="information-btn" value="提  交" onclick="location.href='';" ></div>
        
        
       
      
  
      
      
    
  
   
  </div>
  <div class="footer">©2016 版权所有 亚信集团股份有限公司 京ICP备11005544号-15 京公网安备110108007119号</div>
</body>
</html>
