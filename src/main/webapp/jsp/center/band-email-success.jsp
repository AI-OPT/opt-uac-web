<%@ page contentType="text/html;charset=UTF-8" language="java"%>
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
  <div class="finished"><!--圆圈蓝色 current-->
    <div class="wrap">
      <div class="round"><i class="icon-pencil"></i></div>
      <div class="bar"></div>
    </div>
    <label>2.绑定邮箱</label>
  </div>
  <div class="current"><!--圆圈灰色 todo-->
    <div class="wrap">
      <div class="round"><i class=" icon-ok"></i></div>
      
    </div>
    <label>3.完成</label>
  </div>

</div>
 </div><!--步骤结束-->
         
    <div class="password-success">恭喜您，邮箱设置成功！</div>
    
    </div>
   
  </div>
  <%@ include file="/inc/foot.jsp"%>
</body>
</html>
