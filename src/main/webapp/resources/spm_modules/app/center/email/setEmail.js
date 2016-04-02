define('app/center/email/setEmail', function (require, exports, module) {
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
    var UpdateEmailPager = Widget.extend({
    	//属性，使用时由类的构造函数传入
    	attrs: {
    	},
    	//事件代理
    	events: {
    		//key的格式: 事件+空格+对象选择器;value:事件方法
    		"click [id='sendEmailBtn']":"_sendEmail",
    		"click [id='submitBtn']":"_updateEmail",
    		"blur [id='email']":"_checkEmail",
    		"blur [id='verifyCode']":"_checkVerifyCode"
        },
    	//重写父类
    	setup: function () {
    		UpdateEmailPager.superclass.setup.call(this);
    	},
    	//检查新密码格式
		_checkEmail: function(){
			var email = jQuery.trim($("#email").val());
			var msg = "";
			if(email == "" || email == null || email == undefined){
				msg = "请输入邮箱地址";
			}else if(!/^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/.test(email)){
				msg = "邮箱地址格式错误";
			}
			if(msg == ""){
				this._controlMsgText("emailMsg","");
				this._controlMsgAttr("emailMsgDiv",1);
				return true;
			}else{
				this._controlMsgText("emailMsg",msg);
				this._controlMsgAttr("emailMsgDiv",2);
				return false;
			}
		},
		//检查验证码
		_checkVerifyCode: function(){
			var verifyCode = jQuery.trim($("#verifyCode").val());
			if(verifyCode == "" || verifyCode == null || verifyCode == undefined){
				this._controlMsgText("verifyCodeMsg","请输入验证码");
				this._controlMsgAttr("verifyCodeMsgDiv",2);
				return false;
			}else{
				this._controlMsgText("verifyCodeMsg","");
				this._controlMsgAttr("verifyCodeMsgDiv",1);
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
    	_sendEmail:function(){
			var _this = this;
			var isOk = this._checkEmail();
			if(!isOk){
				return false;
			}
			ajaxController.ajax({
				type : "POST",
				data : {
					"email": function(){
						return $("#email").val()
					}
				},
				url :_base+"/center/email/sendEmailVerify?k="+uuid,
				processing: true,
				message : "正在处理中，请稍候...",
				success : function(data) {
					var url = data.data;
					if(url!=null || url!=undefined){
						window.location.href = _base+url;
					}
				},
				error : function(){
					alert("网络连接超时!");
				}
			});
		},
		//更新邮箱
		_updateEmail:function(){
			var _this = this;
			var checkEmail = this._checkEmail();
			var checkVerify = this._checkVerifyCode();
			if(!(checkEmail&&checkVerify)){
				return false;
			}
			ajaxController.ajax({
				type : "POST",
				data : _this._getSafetyConfirmData(),
				url :_base+"/center/email/setNewEmail?k="+uuid,
				processing: true,
				message : "正在处理中，请稍候...",
				success : function(data) {
					var statusCode = data.responseHeader.resultCode;
					var url = data.data;
					if(statusCode == "000000"){
						window.location.href = _base+url;
					}else {
						var msg = data.statusInfo;
						if(statusCode == "100002"){
							_controlMsgText("verifyCodeMsg",msg);
							_controlMsgAttr("verifyCodeMsgDiv",2);
						}else{
							_controlMsgText("verifyCodeMsg","");
							_controlMsgAttr("verifyCodeMsgDiv",1);
						}
					}
				},
				error : function(){
					alert("网络连接超时，请重新修改登录密码");
				}
			});
		},
		//获取界面填写验证信息
		_getSafetyConfirmData:function(){
			return{
				"email":function () {
			        return jQuery.trim($("#email").val())
			    },
				"verifyCode":function () {
			        return jQuery.trim($("#verifyCode").val())
			    }
			}
		}
		
    });
    
    
    module.exports = UpdateEmailPager
});
