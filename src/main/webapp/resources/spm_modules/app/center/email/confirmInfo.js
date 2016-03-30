define('app/center/email/confirmInfo', function (require, exports, module) {
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
    var ConfirmInfoPager = Widget.extend({
    	//属性，使用时由类的构造函数传入
    	attrs: {
    	},
    	//事件代理
    	events: {
    		//key的格式: 事件+空格+对象选择器;value:事件方法
    		"click [id='submitBtn']":"_confirmInfo",
    		"click [id='sendVerify']":"_sendVerify",
    		"click [id='random_img']":"_getImageRandomCode",
    		"click [id='changeImage']":"_getImageRandomCode",
    		"click [id='changeConfirmType']":"_changeShowViewByType"
        },
        init: function(){
        	_initShowView();
        	_getImageRandomCode();
        },
    	//重写父类
    	setup: function () {
    		ConfirmInfoPager.superclass.setup.call(this);
    		this._renderAccountInfo();
    	},
    
    	//加载账户数据
    	_renderAccountInfo: function(){
			var _this = this;
			//初始化展示页面
			_this._initShowView();
		},
		//初始化展示页面
		_initShowView:function(){
			$("#confirmType").val("1");
			$("#confirmTypeName").html("已验证手机");
			$("#changeConfirmType").html("通过已验证邮箱验证");
			$("#verifyName").html("短信校验码");
			$("#email").attr("style","display:none");
			$("#phone").removeAttr("style");
		},
		//身份认证方式改变触发事件
		_changeShowViewByType:function(){
			var _this = this;
			var confirmType=$('#confirmType').val();
			if(confirmType == "1"){
				$('#confirmType').val("2")
				$("#confirmTypeName").html("已验证邮箱");
				$("#changeConfirmType").html("通过已验证手机验证");
				$("#verifyName").html("邮箱校验码");
				$("#email").removeAttr("style");
				$("#phone").attr("style","display:none");
			}else if(confirmType == "2"){
				$("#confirmType").val("1");
				$("#confirmTypeName").html("已验证手机");
				$("#changeConfirmType").html("通过已验证邮箱验证");
				$("#verifyName").html("短信校验码");
				$("#email").attr("style","display:none");
				$("#phone").removeAttr("style");
			}
		},
		_getImageRandomCode:function(){
			var timestamp = (new Date()).valueOf();
			$("#pictureVerifyCode").val("");
			$("#random_img").attr("src",_base+"/center/email/getImageVerifyCode?timestamp="+timestamp);
		},
		_sendVerify:function(){
			var _this = this;
			ajaxController.ajax({
				type : "POST",
				data : {
					"accountId":1,
					"checkType": function(){
						return $("#confirmType").val()
					}
				},
				url :_base+"/center/email/sendVerify",
				processing: true,
				message : "正在处理中，请稍候...",
				success : function(data) {
				},
				error : function(){
					alert("网络连接超时!");
				}
			});
		},
		//检查身份信息
		_confirmInfo:function(){
			var _this = this;
			ajaxController.ajax({
				type : "POST",
				data : _this._getSafetyConfirmData(),
				url :_base+"/center/email/confirmInfo",
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
		},
		//获取界面填写验证信息
		_getSafetyConfirmData:function(){
			return{
				"confirmType":function () {
			        return jQuery.trim($("#confirmType").val())
			    },
				"pictureVerifyCode":function () {
			        return jQuery.trim($("#pictureVerifyCode").val())
			    },
				"verifyCode":function () {
			        return jQuery.trim($("#verifyCode").val())
			    }
			}
		}
		
    });
    
    
    module.exports = ConfirmInfoPager
});
