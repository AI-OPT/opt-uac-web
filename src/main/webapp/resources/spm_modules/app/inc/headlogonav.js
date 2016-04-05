define('app/inc/headlogonav', function (require, exports, module) {
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
    var HeadLogoNav = Widget.extend({
    	//属性，使用时由类的构造函数传入
    	attrs: {
    	},
        init: function(){
        	_getImageRandomCode();
        },
    	//重写父类
    	setup: function () {
    		HeadLogoNav.superclass.setup.call(this);
    		this._controlMenu();
    	},
		//检查账户信息
    	_controlMenu: function(){
    		var _this = this;
			ajaxController.ajax({
				type : "POST",
				url :_base+"/headLogoNav/isHasEmail",
				processing: true,
				message : "正在处理中，请稍候...",
				success : function(data) {
					var status = data.statusCode;
					if(status == "1" ){
						var resultCode = data.data;
						if(resultCode == "1"){
							$("#setEmail").attr("style","display:none");
							$("#updateEmail").attr("style","display:block");
						}else if(resultCode == "0"){
							$("#setEmail").attr("style","display:block");
							$("#updateEmail").attr("style","display:none");
						}
					}
				},
				error: function() {
					alert("连接服务器超时")
				}
			});
		}		
    });
    
    module.exports = HeadLogoNav
});
