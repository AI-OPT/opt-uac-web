<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/inc/inc.jsp"%>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width; initial-scale=0.8;  user-scalable=0;" />
    <title>无标题文档</title>
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
          <li ><img src="${_base}/theme/baas/images/account-qiy.png" class="account-img"></li>
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
  </div>
  <%@ include file="/inc/foot.jsp"%>
</body>
</html>
