package com.ai.opt.uac.web.constants;

public final class Constants {
	private Constants(){}
	
	public static final String SYSTEM_ID = "opt-uac";
	
	public static final class ResultCode{
		private ResultCode(){}
    	
    	public static final String SUCCESS_CODE = "000000";
	}
	
	public static final class RetakePassword{
		private RetakePassword(){}
		
		public static final String USER_SESSION_KEY = "retake_user_session_id";
		
		/**手机验证方式*/
		public static final String CHECK_TYPE_PHONE = "1";
		/**邮件验证方式*/
		public static final String CHECK_TYPE_EMAIL = "2";
		
		/**邮箱模板路径*/
		public static final String TEMPLATE_EMAIL_URL = "email/template/uac-retakepassword-mail.xml";
		
		/** 缓存命名空间*/
		public static final String CACHE_NAMESPACE = "com.ai.opt.uac.retakepassword.cache";
		
		/** 邮箱验证码缓存key*/
		public static final String CACHE_KEY_VERIFY_EMAIL = "retake-password-verify-email";
		/** 手机验证码缓存key*/
		public static final String CACHE_KEY_VERIFY_PHONE = "retake-password-verify-phone";
		/** 图片验证码缓存key*/
		public static final String CACHE_KEY_VERIFY_PICTURE = "retake-password-verify-picture";
		/** 图片验证码缓存key(填写用户页)*/
		public static final String CACHE_KEY_VERIFY_PICTURE_USER = "retake-password-verify-picture-user";
	}
    
    public static final class Register {
        private Register() {
        }

        /** 邮箱验证码缓存key */
        public static final String REGISTER_EMAIL_KEY = "register-bind-email";
        /** 短信验证码缓存key */
        public static final String REGISTER_PHONE_KEY = "register-verify-phone";
        /** 缓存命名空间 */
        public static final String CACHE_NAMESPACE = "com.ai.opt.uac.register.cache";

        /** 邮箱模板路径 */
        public static final String TEMPLATE_EMAIL_URL = "email/template/uac-register-binemail.xml";

        /** 邮件称呼前缀 */
        public static final String REGISTER_EMAIL_NICK = "云计费";
        /** 图片验证码缓存key*/
        public static final String CACHE_KEY_VERIFY_PICTURE = "register-verify-picture";
        /** 注册成功ID */
        public static final String REGISTER_SUCCESS_ID = "000000";
        /** 图片验证码 错误ID*/
        public static final String REGISTER_PICTURE_ERROR = "000001";
        /** 图片验证码 失效ID*/
        public static final String REGISTER_PICTURE_OVERTIME_ERROR = "000002";
        /** 短信验证码错误ID */
        public static final String REGISTER_SSM_ERROR = "000003";
        /** 短信验证码失效ID */
        public static final String REGISTER_SSM_OVERTIME_ERROR = "000004";
        /** 邮箱验证码失效ID */
        public static final String REGISTER_EMAIL_OVERTIME_ERROR = "000005";
        /** 邮箱验证码错误ID */
        public static final String REGISTER_EMAIL_ERROR = "000006";
        /** 绑定邮箱成功ID */
        public static final String BAND_EMAIL_SUCCESS_ID = "000000";
        /** 手机已经注册ID */
        public static final String PHONE_NOTONE_ERROR = "10003";
        /** 邮箱已经注册ID */
        public static final String EMAIL_NOTONE_ERROR = "10004";
    }
    
    public static final class UpdatePassword{
		private UpdatePassword(){}
		
		/**手机验证方式*/
		public static final String CHECK_TYPE_PHONE = "1";
		/**邮件验证方式*/
		public static final String CHECK_TYPE_EMAIL = "2";
		
		/**邮箱模板路径*/
		public static final String TEMPLATE_EMAIL_URL = "email/template/uac-updatepassword-mail.xml";
		
		/** 缓存命名空间*/
		public static final String CACHE_NAMESPACE = "com.ai.opt.uac.updatepassword.cache";
		
		/** 邮箱验证码缓存key*/
		public static final String CACHE_KEY_VERIFY_EMAIL = "update-password-verify-email";
		/** 手机验证码缓存key*/
		public static final String CACHE_KEY_VERIFY_PHONE = "update-password-verify-phone";
		/** 图片验证码缓存key*/
		public static final String CACHE_KEY_VERIFY_PICTURE = "update-password-verify-picture";
	}
    
    public static final class UpdatePhone{
		private UpdatePhone(){}
		
		/**手机验证方式*/
		public static final String CHECK_TYPE_PHONE = "1";
		/**邮件验证方式*/
		public static final String CHECK_TYPE_EMAIL = "2";
		
		/**邮箱模板路径*/
		public static final String TEMPLATE_EMAIL_URL = "email/template/uac-updatephone-mail.xml";
		
		/** 缓存命名空间*/
		public static final String CACHE_NAMESPACE = "com.ai.opt.uac.updatephone.cache";
		
		/** 邮箱验证码缓存key*/
		public static final String CACHE_KEY_VERIFY_EMAIL = "update-phone-verify-email";
		/** 手机验证码缓存key*/
		public static final String CACHE_KEY_VERIFY_PHONE = "update-phone-verify-phone";
		/** 图片验证码缓存key*/
		public static final String CACHE_KEY_VERIFY_PICTURE = "update-phone-verify-picture";
	}
    
    public static final class UpdateEmail{
		private UpdateEmail(){}
		
		/**手机验证方式*/
		public static final String CHECK_TYPE_PHONE = "1";
		/**邮件验证方式*/
		public static final String CHECK_TYPE_EMAIL = "2";
		
		/**邮箱模板路径*/
		public static final String TEMPLATE_EMAIL_URL = "email/template/uac-updateemail-mail.xml";
		
		/** 缓存命名空间*/
		public static final String CACHE_NAMESPACE = "com.ai.opt.uac.updateemail.cache";
		
		/** 邮箱验证码缓存key*/
		public static final String CACHE_KEY_VERIFY_EMAIL = "update-email-verify-email";
		/** 手机验证码缓存key*/
		public static final String CACHE_KEY_VERIFY_PHONE = "update-email-verify-phone";
		/** 图片验证码缓存key*/
		public static final String CACHE_KEY_VERIFY_PICTURE = "update-email-verify-picture";
	}
}
