<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<%@ include file="/inc/inc.jsp"%>
<!--Support IE Text -->
<meta http-equiv="X-UA-Compatible" content="IE=Edge" /> 
<title>注册</title>
<script type="text/javascript" src="${_base}/theme/baas/js/jquery.toggle-password.js" ></script> 

<script type="text/javascript">
 (function () {
	seajs.use('app/register/register', function (RegisterPager) {
		var pager = new RegisterPager();
		pager.render();
	});
})(); 

 $(function(){
	$('#password').togglePassword({
		el: '#togglePassword'
	});
}); 
//判断输入密码的类型  
function CharMode(iN){  
if (iN>=48 && iN <=57) //数字  
return 1;  
if (iN>=65 && iN <=90) //大写  
return 2;  
if (iN>=97 && iN <=122) //小写  
return 4;  
else  
return 8;   
}  
//bitTotal函数  
//计算密码模式  
function bitTotal(num){  
modes=0;  
for (i=0;i<4;i++){  
if (num & 1) modes++;  
num>>>=1;  
}  
return modes;  
}  
//返回强度级别  
function checkStrong(sPW){  
if (sPW.length<=8)  
return 0; //密码太短  
Modes=0;  
for (i=0;i<sPW.length;i++){  
//密码模式  
Modes|=CharMode(sPW.charCodeAt(i));  
}  
return bitTotal(Modes);  
}  
 
//显示颜色  
function pwStrength(pwd){  
O_color="#eeeeee";  
L_color="#FF0000";  
M_color="#FF9900";  
H_color="#33CC00";  
if (pwd==null||pwd==''){  
Lcolor=Mcolor=Hcolor=O_color;  
}  
else{  
S_level=checkStrong(pwd);  
switch(S_level) {  
case 0:  
Lcolor=Mcolor=Hcolor=O_color;  
case 1:  
Lcolor=L_color;  
Mcolor=Hcolor=O_color;  
break;  
case 2:  
Lcolor=Mcolor=M_color;  
Hcolor=O_color;  
break;  
default:  
Lcolor=Mcolor=Hcolor=H_color;  
}  
}  
document.getElementById("strength_L").style.background=Lcolor;  
document.getElementById("strength_M").style.background=Mcolor;  
document.getElementById("strength_H").style.background=Hcolor;  
return;  
}  
</script>
</head>

<body>

  <div class="login-header"><!--登录头部-->
     <div class="login-header-cnt">
       <div class="login-header-cnt-logo"><a href="${baas_pt_index_url }"><img src="${_base}/theme/baas/images/logo.png"></a></div>
       <div class="login-header-cnt-mail">账户注册</div>
       <div class="login-header-cnt-right">已有账号？ <a href="${_base}/login">立即登录>></a></div>
       </div>
     
  </div>

   <div class="regsiter-wrapper" id="register-form">
        <div class="regsiter-wrapper-cnt">
	         <ul>
		         <li class="regsiter-title">账户注册</li>
		         <li class="user">
		         	<input type="text" name="phone" id="phone"class="int-xxlarge-user" placeholder="手机号码">
		         	<p class="regsiter-po">
			         	<span class="regsiter-note" id="errorPhoneMsg" style="display: none;">
			         		<i class="icon-caret-left"></i>
			         		<img src="${_base}/theme/baas/images/error.png">
			         		<span id="showPhoneMsg" ></span>
			         	</span>
		         	</p>
		         </li>
		         
		         <li class="password">
		         	<input type="password" name="password" id="password"class="int-xxlarge" placeholder="密码"><!-- onKeyUp=pwStrength(this.value) onBlur=pwStrength(this.value) -->
		         	<i class="icon-eye-open" id="togglePassword"></i>
		         	<p class="regsiter-po">
		         	<span class="regsiter-note" id="errorPawMsg" style="display: none;">
		         		<i class="icon-caret-left"></i>
		         		<img src="${_base}/theme/baas/images/error.png"><span id="showPawMsg"></span>
		         	</span>
		         	</p>
		         	<p class="regsiter-po">
		         	<span class="regsiter-note" id="errorShowPM" style="display: none;">
		         		<i class="icon-caret-left"></i>
		         		<img src="${_base}/theme/baas/images/correct.png"><span id="showPM"></span>
		         	</span>
		         	</p>
		         	<div class="regsiter-set-password" style="display:none">
				          <p class="low" id="strength_L">
					          <span class="f00" id=""></span>
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
		         	
		         <li class="identifying">
		         	<input type="text" class="int-xlarge-identifying" style="width:176px;" placeholder="图形验证码" id="pictureVitenfy">
		         	<span ><A ><img src="${_base}/reg/getImageVerifyCode" id="randomImg"></A></span>
		         	<span ><a id="refresh">看不清?换一个</a></span>
		         	<p class="regsiter-po">
			         	<span class="regsiter-note" id="errorPicMsg" style="display: none;">
			         		<i class="icon-caret-left"></i>
			         		<img src="${_base}/theme/baas/images/error.png">
			         		<span id="showPicMsg" ></span>
			         	</span>
		         	</p>
		         </li>
		         <li class="SMSidentifying">
		         	<input type="text" class="int-xlarge-SMSidentifying" placeholder="短信验证码" id="phoneVerifyCode">
		         	 <span class="yzm">
		         	 	<input class="button"id="PHONE_IDENTIFY"  type="button" value="获取验证码" >
		         	 </span>
		         		
		         	<span class="regsiter-note" id="errorSmsMsg" style="display: none;">
		         		<i class="icon-caret-left"></i><img src="${_base}/theme/baas/images/error.png">
		         		<span id="showSmsMsg"></span>
		         	</span>
		         </li>
		         <li>
		         	<input type="button" class="regsiter-btn" value="注 册"  id="BTN_REGISTER">
		         	<input type="hidden" id="errorPhoneFlag">
		         	<input type="hidden" id="errorPicFlag">
		         	<input type="hidden" id="errorPassFlag">
		         	<input type="hidden" id="errorSMSFlag">
		         </li>
		         <li class="zuns">* 注册表示您同意遵守<A href="${_base }/reg/protocol" target="_blank">《亚信云计费服务条款》</A></li>
	
	         </ul>
   		</div>
    </div>
   <div class="login-foot">
   ©2016 版权所有 亚信集团股份有限公司 京ICP备11005544号-15 京公网安备110108007119号
   
   </div>
   
  

</body>
</html>
