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
    		"click [id='submitBtn']":"_updateEmail"
        },
    	//重写父类
    	setup: function () {
    		UpdateEmailPager.superclass.setup.call(this);
    	},
    
    	_sendEmail:function(){
			var _this = this;
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
			ajaxController.ajax({
				type : "POST",
				data : _this._getSafetyConfirmData(),
				url :_base+"/center/email/setNewEmail?k="+uuid,
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
