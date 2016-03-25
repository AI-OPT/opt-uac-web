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
    		"change [name='confirmType']":"_changeShowViewByType"
        },
    	//重写父类
    	setup: function () {
    		ConfirmInfoPager.superclass.setup.call(this);
    		//加载数据
    		this.accountData = {};
    		this._renderAccountInfo();
    	},
    
    	//加载账户数据
    	_renderAccountInfo: function(){
			var _this = this;
			//var accountData = _this._getAccountData();
			_this.accountData = {"phone":"132****1586","email":"1****2@test.com"};
			_this._controlShowView();
			_this._changeShowViewByType();
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
		},
		//控制显示界面
		_controlShowView:function(){
			var _this = this;
			var email = _this.accountData.email;
			if(email == null){
				$("#confirmTypeDiv").attr("disabled","disabled");
			}else{
				$("#confirmTypeDiv").removeAttr("disabled");
			}
		},
		//身份认证方式改变触发事件
		_changeShowViewByType:function(){
			var _this = this;
			var email = _this.accountData.email;
			var phone = _this.accountData.phone;
			if(email != null){
				var confirmType=$('#confirmType option:selected').val();
				if(confirmType == "1"){
					$("#checkTypeName").html("手机号码");
					$("#checkTypeValue").html(phone);
					$("#verifyName").html("短信验证码");
				}else if(confirmType == "2"){
					$("#checkTypeName").html("邮箱地址");
					$("#checkTypeValue").html(email);
					$("#verifyName").html("邮箱验证码");
				}
			}else{
				$("#checkTypeName").html("手机号码");
				$("#checkTypeValue").html(phone);
				$("#verifyName").html("短信验证码");
			}
		}
    });
    
    
    module.exports = ConfirmInfoPager
});
