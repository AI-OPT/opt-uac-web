define('app/retakepassword/success', function (require, exports, module) {
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
    var SuccessPager = Widget.extend({
    	//属性，使用时由类的构造函数传入
    	attrs: {
    	},
    	//事件代理
    	events: {
    		//key的格式: 事件+空格+对象选择器;value:事件方法
    		"click [id='gotoLogin']":"_gotoLogin"
        },
    	//重写父类
    	setup: function () {
    		SuccessPager.superclass.setup.call(this);
    	},
		//检查账户信息
    	_gotoLogin: function(){
			ajaxController.ajax({
				url :_base+"/retakePassword/login?k="+uuid,
				processing: true,
				message : "正在处理中，请稍候...",
				success : function(data) {
					//var status = data.responseHeader.isSuccess;
					var status=data.responseHeader.resultCode;
					var url = data.data;
					if(status == "1"){
						window.location.href = _base+url;
					}else{
						window.location.href = url;
					}
				},
				error: function() {
					alert("连接服务器超时")
				}
			});
		}		
    });
    
    module.exports = SuccessPager
});
