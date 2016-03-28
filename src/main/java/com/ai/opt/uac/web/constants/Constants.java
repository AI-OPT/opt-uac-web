package com.ai.opt.uac.web.constants;

public final class Constants {
	private Constants(){}
	
	public static final class ResultCode{
		private ResultCode(){}
    	
    	public static final String SUCCESS_CODE = "000000";
	}
	
	public static final class RetakePassword{
		private RetakePassword(){}
		
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
		/** 图片验证码缓存key*/
		public static final String CACHE_KEY_VERIFY_PICTURE = "retake-password-verify-picture";
	}
	public static final String REGISTER_EMAIL_SUBJECT = "亚信云计费";
    public static final String REGISTER_EMAIL_TIME = "30";
    public static final String REGISTER_EMAIL_KEY = "REG";
    
    public class VerifyCode{
    	private VerifyCode(){}
    	
    	/**邮箱验证码长度*/
		public static final int VERIFY_SIZE_EMAIL = 6;
		/**手机验证码长度*/
		public static final int VERIFY_SIZE_PHONE = 6;
		/**图片验证码长度*/
		public static final int VERIFY_SIZE_PICTURE = 4;
    	
    	/**邮件验证码超时时间*/
		public static final int VERIFY_OVERTIME_EMAIL = 1800;
		/**手机验证码超时时间*/
		public static final int VERIFY_OVERTIME_PHONE = 300;
		/**图片验证码超时时间*/
		public static final int VERIFY_OVERTIME_PICTURE = 60;
    }
}
