<%@ page contentType="text/html;charset=UTF-8" language="java" %>

   <div class="user">
    <div class="msg" style="display:none;"><a href="#"><i class="icon-bell-alt"></i><span class="badge">4</span></a></div>
    <div class="user-cnt">
     <p><img src="${_baasBase }/images/login_user.png"><span>你好，古珍珍</span><i class="icon-angle-down"></i></p>
     <ul style="display:none;">
      <li><a href="${_base}/center/baseInfo/getAccountInfo">个人中心</a></li>
      <li><a href="${_base}/center/password/confirminfo">修改密码</a></li>
      <li><a href="${_base}/logout">退出</a></li>
     </ul>
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