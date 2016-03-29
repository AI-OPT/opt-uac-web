define('app/register/register', function (require, exports, module) {
    'use strict';
    var $=require('jquery'),
    Validator=require('arale-validator/0.10.2/index'),
    Calendar=require('arale-calendar/1.1.2/index'),
    Widget = require('arale-widget/1.2.0/widget'),
    Dialog = require("artDialog/src/dialog"),
    AjaxController=require('opt-ajax/1.0.0/index');
    
    //实例化AJAX控制处理对象
    var ajaxController = new AjaxController();
    
    //定义页面组件类
    var RegisterPager = Widget.extend({
    	//属性，使用时由类的构造函数传入
    	attrs: {
    	},
    	//重写父类
    	setup: function () {
    		RegisterPager.superclass.setup.call(this);
    		this._bindHandle();
    	},
    	
    	//带下划线的方法，约定为内部私有方法
    	_bindHandle: function(){
    		
    		$("#refresh").on("click",this._refrashVitentify);
    		$("#PHONE_IDENTIFY").on("click",this._getPhoneVitentify);
    		$("#phone").on("blur",this._validServicePho);
    		$("#password").on("blur",this._validServicePaw);
    		$("#pictureVitenfy").on("blur",this._validServicePic);
    		//$("#phoneVerifyCode").on("blur",this._validServiceSSM);
    		$("#BTN_REGISTER").on("click",this._validServicePho);
    		$("#BTN_REGISTER").on("click",this._validServicePaw);
    		$("#BTN_REGISTER").on("click",this._validServicePic);
    		$("#BTN_REGISTER").on("click",this._validServiceSSM);
    		$("#BTN_REGISTER").on("click",this._sumbit);
    	},
    	//获取短信验证码
    	_getPhoneVitentify: function(){
    		var	param={
					phone:	$("#phone").val()
				   };
    		ajaxController.ajax({
			        type: "post",
			        processing: false,
			        url: "../reg/toSendPhone",
			        dataType: "json",
			        data: param,
			        message: "正在加载数据..",
			        success: function (data) {
			        	alert("ok");
			        },
			        error: function(XMLHttpRequest, textStatus, errorThrown) {
						 alert(XMLHttpRequest.status);
						 alert(XMLHttpRequest.readyState);
						 alert(textStatus);
						   }
			        
			    }); 
    	},
    	//刷新验证码
    	_refrashVitentify: function(){
    		 $("#randomImg").removeAttr('src');
    		 $("#randomImg").attr('src',"../reg/getImageVerifyCode");  
    	},
    	
    	//校验手机
    	_validServicePho: function(){
    		var phone = $('#phone').val();
    		if (phone==""){
    			$('#showPhoneMsg').text("请输入手机号码");
    			$("#errorPhoneMsg").attr("style","display:block");
				return false;
			}else if( /^1\d{10}$/.test(phone)){
				$("#errorPhoneMsg").attr("style","display:none");
			}else{
				$('#showPhoneMsg').text("手机号码格式不正确");
				$("#errorPhoneMsg").attr("style","display:block");
				return false;
			}
    	},
    	//校验密码
    	_validServicePaw:function(){
    		$("#errorPawMsg").attr("style","display:none");
    		var password = $('#password').val();
    		if(password==""){
    			$('#showPawMsg').text("请输入密码");
    			$("#errorPawMsg").attr("style","display:block");
				return false;
    		}else if(/[\x01-\xFF]*/.test(password)){
    				if(/^\S*$/.test(password)){
    					if(/^\w{6,14}$/.test(password)){
    						$("#errorPawMsg").attr("style","display:none");
    					}else{
    						$('#showPawMsg').text("长度为6-14个字符 ");
    		    			$("#errorPawMsg").attr("style","display:block");
    						return false;
    					}
    					
    				}else{
    					$('#showPawMsg').text("不允许有空格 ");
            			$("#errorPawMsg").attr("style","display:block");
        				return false;
    				}
    			}else{
    				$('#showPawMsg').text("支持数字、字母、符号组合 ");
        			$("#errorPawMsg").attr("style","display:block");
    				return false;
    			}
    			
    	},
    	//图形验证码
    	_validServicePic: function(){
    		$("#errorPicMsg").attr("style","display:none");
    		var pictureCode = $('#pictureVitenfy').val();
    		if(pictureCode==""){
    			$('#showPicMsg').text("请输入图形验证码 ");
    			$("#errorPicMsg").attr("style","display:block");
				return false;
    		}
    		
    	},
    	//短信验证码
    	_validServiceSSM: function(){
    		$("#errorSmsMsg").attr("style","display:none");
    		var smsCode = $('#phoneVerifyCode').val();
    		if(smsCode==""){
    			$('#showSmsMsg').text("请输入短信验证码 ");
    			$("#errorSmsMsg").attr("style","display:block");
				return false;
    		}
    	},
    	_sumbit: function(){
    			var	param={
    					phone:	$("#phone").val(),  
    					accountPassword:$("#password").val(),		   
    					phoneVerifyCode:$("#phoneVerifyCode").val(),   
    					pictureVerifyCode:$("#pictureVitenfy").val()	
    				   };
        		ajaxController.ajax({
    			        type: "post",
    			        processing: false,
    			        url: "../reg/register",
    			        dataType: "json",
    			        data: param,
    			        message: "正在加载数据..",
    			        success: function (data) {
    			        	if(data.responseHeader.resultCode=="000002"){
    			        		$('#showPicMsg').text("验证码已失效 ");
    			    			$("#errorPicMsg").attr("style","display:block");
    							return false;
    			        	}else if(data.responseHeader.resultCode=="000001"){
    			        		$('#showPicMsg').text("图形验证码错误 ");
    			    			$("#errorPicMsg").attr("style","display:block");
    							return false;
    			        	}else if(data.responseHeader.resultCode=="000004"){
    			        		$('#showSmsMsg').text("验证码已失效  ");
    			    			$("#errorSmsMsg").attr("style","display:block");
    							return false;
    			        	}else if(data.responseHeader.resultCode=="000003"){
    			        		$('#showSmsMsg').text("短信验证码错误 ");
    			    			$("#errorSmsMsg").attr("style","display:block");
    							return false;
    			        	}else if(data.responseHeader.resultCode=="10003"){
    			        		$('#showPhoneMsg').text("手机号码已注册");
    							$("#errorPhoneMsg").attr("style","display:block");
    							return false;
    			        	}else if(data.responseHeader.resultCode=="000000"){
    			        		$("#errorSmsMsg").attr("style","display:none");
    			        		var accountId = data.data;
        			        	window.location.href="../reg/toRegisterEmail?accountId="+accountId;
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
    
    module.exports = RegisterPager
});

