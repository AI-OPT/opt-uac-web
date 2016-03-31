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
    	//重写父类
    	setup: function () {
    		RegisterEmaillPager.superclass.setup.call(this);
    		//初始化组件：上传服务数据模块
    		this._bindHandle();
    	},
    	_bindHandle: function(){
    		$("#email").on("blur",this._validServiceEmail);
    		//$("#getIdentify").on("click",this._validServiceEmail);
    		$("#getIdentify").on("click",this._getIdentify);
    		$("#BTN_PASS").on("click",this._passEmail);
    		$("#BTN_SUBMIT").on("click",this._validServiceEmail);
    		$("#BTN_SUBMIT").on("click",this._bindEmail);
    	},
    	_validServiceEmail: function(){
    		$("#errorEmIdentifyMsg").attr("style","display:none");
    		var emailCode = $('#email').val();
    		if(emailCode!=""){
    			if(/^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/.test(emailCode)){
    				$("#errorEmIdentifyMsg").attr("style","display:none");
    				var emailIdenty = $('#identifyCode').val();
    				if(emailIdenty==""){
    					$("#showErroeEmIdentify").text("邮箱验证码不能为空 ");
	    				$("#errorEmIdentifyMsg").attr("style","display:block");
	    				return false;
    				}
    			}else{
    				$("#showErroeEmIdentify").text("邮箱地址格式错误 ");
    				$("#errorEmIdentifyMsg").attr("style","display:block");
    				return false;
    			}
    		}
    	},
    	_getIdentify: function(){
    		var emailCode = $('#email').val();
	    	if(emailCode==""){
	    		$("#showErroeEmIdentify").text("邮箱验证码不能为空 ");
				$("#errorEmIdentifyMsg").attr("style","display:block");
				return false;
	    	}
    		var	param={
					email:	$("#email").val(),
					accountIdKey:$("#accountIdKey").val()
				   };
    		ajaxController.ajax({
		        type: "post",
		        processing: false,
		        url: _base+"/reg/toSendEmail",
		        dataType: "json",
		        data: param,
		        message: "正在加载数据..",
		        success: function (data) {
		        	if(data.responseHeader.resultCode=="1100"){
		        		window.location.href=_base+"/reg/toRegister";
		        	}
		        },
		        error: function(XMLHttpRequest, textStatus, errorThrown) {
					 alert(XMLHttpRequest.status);
					 alert(XMLHttpRequest.readyState);
					 alert(textStatus);
					   }
		    }); 
    	},
    	_passEmail: function(){
    		window.location.href=_base+"/reg/toRegisterSuccess";
    	},
    	
    	_bindEmail: function(){
    		var	param={
					email:	$("#email").val(),
					accountIdKey:$("#accountIdKey").val(),
					identifyCode:$("#identifyCode").val()
				   };
    		ajaxController.ajax({
			        type: "post",
			        processing: false,
			        url: _base+"/reg/bindEmail",
			        dataType: "json",
			        data: param,
			        message: "正在加载数据..",
			        success: function (data) {
			        	if(data.responseHeader.resultCode=="000005"){
			        		$("#showErroeEmIdentify").text("邮箱验证码失效 ");
		    				$("#errorEmIdentifyMsg").attr("style","display:block");
		    				return false;
			        	}else if(data.responseHeader.resultCode=="000006"){
			        		$("#showErroeEmIdentify").html("邮箱验证码错误 ");
		    				$("#errorEmIdentifyMsg").attr("style","display:block");
		    				return false;
			        	}else if(data.responseHeader.resultCode=="10004"){
			        		$("#showErroeEmIdentify").html("邮箱已存在 ");
		    				$("#errorEmIdentifyMsg").attr("style","display:block");
		    				return false;
			        	}else if(data.responseHeader.resultCode=="1100"){
			        		window.location.href=_base+"/reg/toRegister";
			        	}else if(data.responseHeader.resultCode=="000000"){
			        		$("#errorEmIdentifyMsg").attr("style","display:none");
			        		window.location.href=_base+"/reg/toRegisterSuccess";
			        	}
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

