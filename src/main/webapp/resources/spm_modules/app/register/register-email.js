define('app/register/register-email', function (require, exports, module) {
    'use strict';
    var $=require('jquery'),
    Widget = require('arale-widget/1.2.0/widget'),
    Dialog = require("artDialog/src/dialog"),
    AjaxController=require('opt-ajax/1.0.0/index');
   
  
    
    //实例化AJAX控制处理对象
    var ajaxController = new AjaxController();
    
    //定义页面组件类
    var RegisterEmaillPager = Widget.extend({
    	//属性，使用时由类的构造函数传入
    	attrs: {
    	},
    	//事件代理
    	events: {
    		  //"click [id='BTN_PASS']":"_passEmail",
    		  //"click [id='BTN_SUBMIT']":"_bindEmail"
        },
    	//重写父类
    	setup: function () {
    		RegisterEmaillPager.superclass.setup.call(this);
    		//初始化组件：上传服务数据模块
    		this._bindHandle();
    	},
    	_bindHandle: function(){
    		$("#BTN_PASS").on("click",this._passEmail);
    		$("#BTN_SUBMIT").on("click",this._bindEmail);
    		$("#getIdentify").on("click",this._getIdentify);
    	},
    	_getIdentify: function(){
    		var	param={
					email:	$("#email").val(),
					accountId:$("#accountId").val()
				   };
    		ajaxController.ajax({
		        type: "post",
		        processing: false,
		        url: "../reg/toSendEmail",
		        dataType: "json",
		        data: param,
		        message: "正在加载数据..",
		        success: function (data) {
		        	//校验验证码是否正确
		        	alert("ok");
		        },
		        error: function(XMLHttpRequest, textStatus, errorThrown) {
					 alert(XMLHttpRequest.status);
					 alert(XMLHttpRequest.readyState);
					 alert(textStatus);
					   }
		    }); 
    	},
    	_passEmail: function(){
    		window.location.href="../reg/toRegisterSuccess";
    	},
    	
    	_bindEmail: function(){
    		var	param={
					email:	$("#email").val(),
					accountId:$("#accountId").val(),
					identifyCode:$("#identifyCode").val()
				   };
    		ajaxController.ajax({
			        type: "post",
			        processing: false,
			        url: "../reg/bindEmail",
			        dataType: "json",
			        data: param,
			        message: "正在加载数据..",
			        success: function (data) {
			        	window.location.href="../reg/toRegisterSuccess";
			        },
			        error: function(XMLHttpRequest, textStatus, errorThrown) {
						 alert(XMLHttpRequest.status);
						 alert(XMLHttpRequest.readyState);
						 alert(textStatus);
						   }
			    }); 
    	}
    	
    });   
    module.exports = RegisterEmaillPager
});

