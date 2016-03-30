define('app/retakepassword/userInfo', function (require, exports, module) {
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
    var UsetInfoPager = Widget.extend({
    	//属性，使用时由类的构造函数传入
    	attrs: {
    	},
    	//事件代理
    	events: {
    		//key的格式: 事件+空格+对象选择器;value:事件方法
    		"click [id='submitBtn']":"_checkUserInfo",
    		"click [id='random_img']":"_getImageRandomCode",
    		"click [id='changeImage']":"_getImageRandomCode"
        },
        init: function(){
        	_getImageRandomCode();
        },
    	//重写父类
    	setup: function () {
    		UsetInfoPager.superclass.setup.call(this);
    	},
    	_getImageRandomCode:function(){
			var timestamp = (new Date()).valueOf();
			$("#pictureVerifyCode").val("");
			$("#random_img").attr("src",_base+"/retakePassword/getUserImageVerifyCode?timestamp="+timestamp);
		},
		//检查账户信息
    	_checkUserInfo: function(){
			ajaxController.ajax({
				type : "POST",
				data : {
					"username":function(){
						return jQuery.trim($("#userName").val());
					},
					"pictureVerifyCode":function(){
						return jQuery.trim($("#pictureVerifyCode").val());
					}
				},
				url :_base+"/retakePassword/checkUserInfo",
				processing: true,
				message : "正在处理中，请稍候...",
				success : function(data) {
					var status = data.statusCode;
					if(status == "1"){
						var url = data.data;
						window.location.href = _base+url;
					}else{
						alert("该用户不存在！");
					}
				},
				error: function() {
					alert("连接服务器超时")
				}
			});
		}		
    });
    
    module.exports = UsetInfoPager
});
