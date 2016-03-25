define('app/retakepassword/resetpassword', function (require, exports, module) {
    'use strict';
    var $=require('jquery'),
    Widget = require('arale-widget/1.2.0/widget'),
    Dialog = require("artDialog/src/dialog"),
    Uploader = require('arale-upload/1.2.0/index'),
    AjaxController=require('opt-ajax/1.0.0/index');
    
    require("jsviews/jsrender.min");
    require("jsviews/jsviews.min");
    require("treegrid/js/jquery.treegrid.min");
    require("treegrid/js/jquery.cookie");
    
    
    //实例化AJAX控制处理对象
    var ajaxController = new AjaxController();
    
    //定义页面组件类
    var ResetPasswordPage = Widget.extend({
    	//属性，使用时由类的构造函数传入
    	attrs: {
    	},
    	//事件代理
    	events: {
    		//key的格式: 事件+空格+对象选择器;value:事件方法
    		"click [id='submitBtn']":"_resetPassword"
        },
    	//重写父类
    	setup: function () {
    		ResetPasswordPage.superclass.setup.call(this);
    	},
    
		//获取账户信息
    	_resetPassword: function(){
			var accountData = {};
			ajaxController.ajax({
				type : "POST",
				data : {"password":function(){return jQuery.trim($("#newPassword").val())}},
				url :_base+"/retakePassword/setNewPassword",
				processing: true,
				message : "正在处理中，请稍候...",
				success : function(data) {
					var statusCode = data.statusCode;
					var url = data.data;
					if(statusCode == "1"){
						window.location.href = _base+url;
					}else{
						alert(data.statusInfo);
					}
				},
				error : function(){
					alert("网络连接超时，请重新修改登录密码");
				}
			});
			return accountData;
		}
    });
    module.exports = ResetPasswordPage
});
