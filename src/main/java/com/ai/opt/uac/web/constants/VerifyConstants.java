package com.ai.opt.uac.web.constants;

public final class VerifyConstants {
	private VerifyConstants(){}
	
	public final class PhoneVerifyConstants{
		private PhoneVerifyConstants(){}
		
		/**手机验证码长度*/
		public static final int VERIFY_SIZE = 6;
		
		/**手机验证码超时时间*/
		public static final int VERIFY_OVERTIME = 300;
		
		/** 短信注册模板ID */
        public static final String TEMPLATE_REGISTER_ID = "1";
        /** 短信修改密码模板ID */
        public static final String TEMPLATE_UPDATE_PASSWORD_ID = "2";
        /** 短信修改手机模板ID */
        public static final String TEMPLATE_UPDATE_PHONE_ID = "3";
        /** 短信修改邮箱模板ID */
        public static final String TEMPLATE_UPDATE_EMAIL_ID = "4";
        /** 短信修改邮箱模板ID */
        public static final String TEMPLATE_SET_PASSWORD_ID = "5";
        /** 短信找回密码模板ID */
        public static final String TEMPLATE_RETAKE_PASSWORD_ID = "6";
        /** 短信设置新手机号模板ID */
        public static final String TEMPLATE_RETAKE_SETPHONE_ID = "7";
        
        public static final String SERVICE_TYPE = "1";
		
	}
	
	public final class EmailVerifyConstants{
		private EmailVerifyConstants(){}
		
		/** 邮箱主题 */
        public static final String EMAIL_SUBJECT = "亚信云计费";
		
		/**邮箱验证码长度*/
		public static final int VERIFY_SIZE = 6;
    	
    	/**邮件验证码超时时间*/
		public static final int VERIFY_OVERTIME = 1800;
		
		/**邮箱模板路径*/
		public static final String TEMPLATE_URL = "email/template/uac-retakepassword-mail.xml";
	}
	
	public final class PictureVerifyConstants{
		private PictureVerifyConstants(){}
		
		/**图片验证码长度*/
		public static final int VERIFY_SIZE = 4;
		/**图片验证码超时时间*/
		public static final int VERIFY_OVERTIME = 600;
	}
	
	public final class ResultCodeConstants{
		private ResultCodeConstants(){}
		/*** 成功ID*/
		public static final String SUCCESS_CODE = "000000";
		/*** 失败ID*/
		public static final String ERROR_CODE = "111111";
		
		/** 图片验证码 错误ID*/
        public static final String REGISTER_PICTURE_ERROR = "100001";
        /** 短信验证码错误ID */
        public static final String REGISTER_SSM_ERROR = "100002";
        /** 邮箱验证码错误ID */
        public static final String REGISTER_EMAIL_ERROR = "100003";
        /** 用户名错误 */
        public static final String USERNAME_ERROR = "100004";
        /** 验证码错误ID */
        public static final String REGISTER_VERIFY_ERROR = "100005";
	}
}
