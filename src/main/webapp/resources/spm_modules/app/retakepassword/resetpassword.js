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
    		"click [id='submitBtn']":"_resetPassword",
    		"blur [id='newPassword']":"_checkNewPassword",
    		"blur [id='confirmPassword']":"_checkConfirmPassword"
        },
    	//重写父类
    	setup: function () {
    		ResetPasswordPage.superclass.setup.call(this);
    	},
    	//检查新密码格式
		_checkNewPassword: function(){
			var newPassword = $("#newPassword").val();
			var msg = "";
			if(newPassword == "" || newPassword == null || newPassword == undefined){
				msg = "请输入密码";
			}else{
				if(!/^[\x21-\x7E]{6,14}$/.test(newPassword)){
					msg = msg +"长度为6-14个字符 \n";
				}
				if(!/[\x01-\xFF]*/.test(newPassword)){
					msg = msg +"支持数字、字母、符号组合\n";
				}
				if(!/^\S*$/.test(newPassword)){
					msg = msg +"不允许有空格 \n";
				}
			}
			if(msg == ""){
				this._controlMsgText("newPwdMsg","");
				this._controlMsgAttr("newPwdMsgDiv",1);
				return true;
			}else{
				this._controlMsgText("newPwdMsg",msg);
				this._controlMsgAttr("newPwdMsgDiv",2);
				return false;
			}
		},
		//检查确认密码
		_checkConfirmPassword: function(){
			var isOk = this._checkNewPassword();
			if(!isOk){
				return false;
			}
			var confirmPassword = $("#confirmPassword").val();
			var newPassword = $("#newPassword").val();
			if(confirmPassword == "" || confirmPassword == null || confirmPassword == undefined){
				this._controlMsgText("confirmPwdMsg","请输入确认密码");
				this._controlMsgAttr("confirmPwdMsgDiv",2);
				return false;
			}else if(newPassword != confirmPassword){
				this._controlMsgText("confirmPwdMsg","两次输入的密码不匹配");
				this._controlMsgAttr("confirmPwdMsgDiv",2);
				return false;
			}else{
				this._controlMsgText("confirmPwdMsg","");
				this._controlMsgAttr("confirmPwdMsgDiv",1);
				return true;
			}
		},
    	//控制显示内容
		_controlMsgText(id,msg){
			var doc = document.getElementById(id+"");
			doc.innerText=msg;
		},
		//控制显隐属性 1:隐藏 2：显示
		_controlMsgAttr(id,flag){
			var doc = document.getElementById(id+"");
			if(flag == 1){
				doc.setAttribute("style","display:none");
			}else if(flag == 2){
				doc.setAttribute("style","display");
			}
		},
		//获取账户信息
    	_resetPassword: function(){
    		var checkNewPwd = this._checkNewPassword();
    		var checkConfirmPwd = this._checkConfirmPassword();
    		var newPassword = $("#newPassword").val();
    		if(!(checkNewPwd&&checkConfirmPwd)){
    			return false;
    		}
			ajaxController.ajax({
				type : "POST",
				data : {"password":newPassword},
				url :_base+"/retakePassword/setNewPassword?k="+uuid,
				processing: true,
				message : "正在处理中，请稍候...",
				success : function(data) {
					var status = data.responseHeader.resultCode;
					if(status == "000000"){
						var url = data.data;
						window.location.href = _base+url;
					}
				},
				error : function(){
					alert("网络连接超时，请重新修改登录密码");
				}
			});
    		
		}
    });
    module.exports = ResetPasswordPage
});
