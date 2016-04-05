<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="logo">
 <a href="#">LOGO</a>
 <i class="icon-angle-down"></i> 
</div>
<div class="subnav">
<style type="text/css">
.w1{ font-size:14px;}
</style>
<div class="nav">
  <ul>
   <li><a href="#">账户中心</a></li>
   <li id="baseInfo"><a href="${_base}/center/baseInfo/getAccountInfo">基本信息</a></li>
   <li id="updatePassword" ><a href="${_base}/center/password/confirminfo" >修改密码</a></li>
   <li id="updatePhone"><a href="${_base}/center/phone/confirminfo" >修改手机号</a></li>
   <li id="updateEmail"><a href="${_base}/center/email/confirminfo" >修改邮箱</a></li>
   <li id="setEmail"><a href="${_base}/center/email/setEmail" >邮箱设置</a></li>
  </ul>
  </div>
</div>
<div class="navbg"></div>