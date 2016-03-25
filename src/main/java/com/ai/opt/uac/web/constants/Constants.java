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
		
		/**邮件验证码超时时间*/
		public static final int VERIFY_EMAIL_OVERTIME = 1800;
		/**手机验证码超时时间*/
		public static final int VERIFY_PHONE_OVERTIME = 300;
	}
	public static final String REGISTER_EMAIL_SUBJECT = "亚信云计费";
    public static final String REGISTER_EMAIL_TIME = "30";
    public static final String REGISTER_EMAIL_KEY = "REG";
}
