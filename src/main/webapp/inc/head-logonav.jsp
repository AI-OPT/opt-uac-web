<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="logo">
 <a href="#">LOGO</a>
 <i class="icon-angle-down"></i> 
</div>
<div class="subnav">
<div class="nav">
  <ul>
   <li id="baseInfo"><a href="${_base}/center/baseInfo/getAccountInfo">基本信息</a></li>
   <li id="updatePassword" ><a href="${_base}/center/password/confirminfo" >修改密码</a></li>
   <li id="updatePhone"><a href="${_base}/center/phone/confirminfo" >修改手机号</a></li>
   <li id="updateEmail"><a href="${_base}/center/email/confirminfo" ></a></li>
  </ul>
  </div>
</div>
<div class="navbg"></div>
<script type="text/javascript">
		(function() {
			seajs.use([ 'app/inc/headlogonav' ], function(HeadLogoNav) {
				var pager = new HeadLogoNav({
					element : document.body
				});
				pager.render();
			});
		})(); 
  </script>