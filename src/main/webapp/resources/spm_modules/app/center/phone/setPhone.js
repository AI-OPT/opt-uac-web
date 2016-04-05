define('app/center/phone/setPhone', function (require, exports, module) {
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
    var UpdatePhonePager = Widget.extend({
    	//属性，使用时由类的构造函数传入
    	attrs: {
    	},
    	//事件代理
    	events: {
    		//key的格式: 事件+空格+对象选择器;value:事件方法
    		"click [id='sendPhoneBtn']":"_sendPhone",
    		"click [id='submitBtn']":"_updatePhone",
    		"blur [id='phone']":"_checkPhone",
    		"blur [id='verifyCode']":"_checkVerifyCode"
        },
    	//重写父类
    	setup: function () {
    		UpdatePhonePager.superclass.setup.call(this);
    	},
    	_checkPhone: function(){
    		var phone=jQuery.trim($("#phone").val());
    		var msg = "";
    		if(phone==""|| phone == null || phone == undefined){
    			msg ="请输入手机号码 ";
    		}else if(!/^1\d{10}$/.test(phone)){
    			msg = "手机号码格式错误";
    		}
    		if(msg == ""){
				this._controlMsgText("phoneMsg","");
				this._controlMsgAttr("phoneMsgDiv",1);
				return true;
			}else{
				this._controlMsgText("phoneMsg",msg);
				this._controlMsgAttr("phoneMsgDiv",2);
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
    	_sendPhone:function(){
    		var isOk = this._checkPhone();
    		if(!isOk){
    			return false;
    		}
    		var step = 59;
            $('#sendPhoneBtn').val('重新发送60');
            var _res = setInterval(function(){
                $("#sendPhoneBtn").attr("disabled", true);//设置disabled属性
                $('#sendPhoneBtn').val('重新发送'+step);
                step-=1;
                if(step <= 0){
                $("#sendPhoneBtn").removeAttr("disabled"); //移除disabled属性
                $('#sendPhoneBtn').val('获取验证码');
                clearInterval(_res);//清除setInterval
                }
            },1000);
			var _this = this;
			ajaxController.ajax({
				type : "POST",
				data : {
					"phone": function(){
						return $("#phone").val()
					}
				},
				url :_base+"/center/phone/sendPhoneVerify?k="+uuid,
				processing: true,
				message : "正在处理中，请稍候...",
				success : function(data) {
					if(data.responseHeader.resultCode=="9999"){
		        		$('#showSmsMsg').text("1分钟后可重复发送 ");
		    			$("#errorSmsMsg").attr("style","display:block");
		    			$("#verifyCode").val("");
						return false;
		        	}
				},
				error : function(){
					alert("网络连接超时!");
				}
			});
		},
		//更新手机
		_updatePhone:function(){
			var _this = this;
			var checkPhone = this._checkPhone();
			var checkVerify = this._checkVerifyCode();
			if(!(checkPhone&&checkVerify)){
				return false;
			}
			ajaxController.ajax({
				type : "POST",
				data : _this._getSafetyConfirmData(),
				url :_base+"/center/phone/setNewPhone?k="+uuid,
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
				"phone":function () {
			        return jQuery.trim($("#phone").val())
			    },
				"verifyCode":function () {
			        return jQuery.trim($("#verifyCode").val())
			    }
			}
		}
		
    });
    
    
    module.exports = UpdatePhonePager
});
