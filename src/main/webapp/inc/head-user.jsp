<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 <div class="header">
	<div class="breadcrumb">
	    <ul>
	     <li>账户中心</li><!--这个以后用参数传-->
	     <li><i class="icon-angle-right"></i>基本信息</li>
	    </ul>
   </div>

   <div class="user">
    <div class="msg" style="display:none;"><a href="#"><i class="icon-bell-alt"></i><span class="badge">4</span></a></div>
    <div class="user-cnt">
    <c:if test="${sessionScope.user_session_key.shortNickName==''}">
    	 <p><img src="${_baasBase }/images/login_user.png"><span>${sessionScope.user_session_key.nickName}</span><i class="icon-angle-down"></i></p>
    </c:if>
    <c:if test="${sessionScope.user_session_key.shortNickName!=''}">
    	 <p><img src="${_baasBase }/images/login_user.png"><span>${sessionScope.user_session_key.shortNickName}</span><i class="icon-angle-down"></i></p>
    </c:if>
    
     <ul style="display:none;">
      <li><a href="${_base}/center/baseInfo/getAccountInfo">个人中心</a></li>
      <li><a href="${_base}/center/password/confirminfo">修改密码</a></li>
      <li><a href="${_base}/ssologout">退出</a></li>
     </ul>
    </div>
   </div>
 </div>  
   <!--右侧弹出框-->
<div class="msg-cnt">
 <div class="p">
      <a ng-click="$hide()" class="pull-right text-muted"><img src="${_baasBase }/images/close.png"></a>
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