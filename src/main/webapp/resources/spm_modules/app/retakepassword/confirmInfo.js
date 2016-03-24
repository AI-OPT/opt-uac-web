define('app/retakepassword/confirmInfo', function (require, exports, module) {
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
//    		 "click [name='TR_IN_EXPANDER_NAME']":"_testAction" , 
        },
    	//重写父类
    	setup: function () {
    		ConfirmInfoPager.superclass.setup.call(this);
    		//加载数据
    		this._renderAccountInfo();
    	},
    
    	//加载账户数据
    	_renderAccountInfo: function(){
			var _this = this;
			
			var accountData = _this._getAccountData();
			$("#checkTypeName").html("手机号码");
			//$("#checkTypeValue").html(accountData.phone);
			$("#verifyName").html("短信验证码");
		},
		
		//获取账户信息
    	_getAccountData: function(){
			var accountData = {};
			ajaxController.ajax({
				type : "POST",
				url :"../retakePassword/getAccountInfo",
				processing: true,
				message : "正在处理中，请稍候...",
				success : function(data) {
					accountData = data.data;
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					 alert(XMLHttpRequest.status);
					 alert(XMLHttpRequest.readyState);
					 alert(textStatus);
					   }
			});
			return accountData;
		}
    });
    module.exports = ConfirmInfoPager
});
