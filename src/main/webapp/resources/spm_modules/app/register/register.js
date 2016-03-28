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
    
    //表单校验对象
    //var validator = new Validator({
        //element: $("#test-form")
    //});
    
    //定义页面组件类
    var RegisterPager = Widget.extend({
    	//属性，使用时由类的构造函数传入
    	attrs: {
    	},
    	//重写父类
    	setup: function () {
    		RegisterPager.superclass.setup.call(this);
    		//this._bindValidRules();
    		this._bindHandle();
    	},
    	
    	//带下划线的方法，约定为内部私有方法
    	_bindHandle: function(){
    		//console.log(this.get('name'));
    		$("#BTN_REGISTER").on("click",this._sumbit);
    		$("#refresh").on("click",this._refrashVitentify);
    		$("#PHONE_IDENTIFY").on("click",this._getPhoneVitentify);
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
    		$("#randomImg").src = "../reg/getImageVerifyCode";
    	},
    	
    	_bindValidRules: function(){
            validator.addItem({
                element: '[name=username]',
                required: true,
                rule: 'email minlength{min:1} maxlength{max:20}'
            }).addItem({
                element: '[name=password]',
                required: true,
                rule: 'minlength{min:6}'
            }).addItem({
                element: '[name=password-confirmation]',
                required: true,
                rule: 'confirmation{target: "#password"}'
            }).addItem({
                element: '[name=born]',
                required: true
            });
    	},
    	
    	_sumbit: function(){
    		//表单提交执行一次全局校验，回调函数里面，先判断是否出现校验错误在进行处理
    		/*validator.execute(function(error, results, element) {
    		    if(error){return;}
    		    alert("OK")
    		});*/
    		var	param={
					phone:	$("#phone").val(),  
					accountPassword:$("#password").val(),		   
					phoneVerifyCode:$("#phoneVerifyCode").val(),   
					pictureVerifyCode:$("#pictureVitenfy").val(),	
				   };
    		ajaxController.ajax({
			        type: "post",
			        processing: false,
			        url: "../reg/register",
			        dataType: "json",
			        data: param,
			        message: "正在加载数据..",
			        success: function (data) {
			        	var accountId = data.data;
			        	window.location.href="../reg/toRegisterEmail?accountId="+accountId;
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

