<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="header">
  <div class="head-auto">
    <img id="img_logincheck" style="display:none;" src="${baas_pt_index_url }/logincheck?req=<%=new Date().getTime() %>">
   	<div class="logo"><a href="${baas_pt_index_url }"><img src="${_baasBase }/images/about.png"></a></div>
   	<div class="breadcrumb">
   		<ul>
			<li><span id="set_title_id"></span></li>
		</ul>
	</div>
	<div class="user">
    <div class="user-cnt">
     <c:choose>
	    <c:when test="${sessionScope.user_session_key.shortNickName==''}">
	      <p><a href="${_base}/center/baseInfo/getAccountInfo"><img src="${_baasBase }/images/login_user.png"></a><span>${sessionScope.user_session_key.nickName}</span><a href="${_base}/ssologout">退出</a></p>
	    </c:when>
	    <c:when test="${sessionScope.user_session_key.shortNickName==null}">
	      <p><a href="${_base}/center/baseInfo/getAccountInfo"><img src="${_baasBase }/images/login_user.png"></a><span>${sessionScope.user_session_key.nickName}</span><a href="${_base}/ssologout">退出</a></p>
	    </c:when>
	    <c:otherwise>
	      <p><a href="${_base}/center/baseInfo/getAccountInfo"><img src="${_baasBase }/images/login_user.png"></a><span>${sessionScope.user_session_key.shortNickName}</span><a href="${_base}/ssologout">退出</a></p>
	    </c:otherwise>
	 </c:choose>
    </div>
   </div>
  </div>
</div>
