define(
		'app/register/register',
		function(require, exports, module) {
			'use strict';
			var $ = require('jquery'), Validator = require('arale-validator/0.10.2/index'), Widget = require('arale-widget/1.2.0/widget')

			// 表单校验对象
			var validator = new Validator({
				element : $("#register-form")
			});

			// 定义页面组件类
			var RegisterPager = Widget
					.extend({
						// 属性，使用时由类的构造函数传入
						attrs : {},

						// 重写父类
						setup : function() {
							RegisterPager.superclass.setup.call(this);
							this._registerValidRules();
							this._registerHandle();
						},

						// 带下划线的方法，约定为内部私有方法
						// 注册方法
						_registerHandle : function() {
							$("#BTN_REGISTER").on("click", this._sumbit);
						},

						// 数据校验

						_registerValidRules : function() {
							validator.addItem(
							{
								element : '[name=phone]',
								required : true,
								rule : 'phone minlength{min:11}'
							}).addItem({
								element : '[name=password]',
								required : true,
								rule : 'password minlength{min:6}'
							});
						},

						_sumbit : function() {
							// 表单提交执行一次全局校验，回调函数里面，先判断是否出现校验错误在进行处理
							validator
									.execute(function(error, results, element) {
										if (error) {
											return;
										}
										alert("OK")
									});
						}
					});

			module.exports = RegisterPager
		});
