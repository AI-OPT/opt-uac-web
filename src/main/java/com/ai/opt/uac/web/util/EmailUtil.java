package com.ai.opt.uac.web.util;

import com.ai.opt.sdk.mail.EmailFactory;
import com.ai.opt.sdk.mail.EmailTemplateUtil;
import com.ai.opt.uac.web.model.email.SendEmailRequest;

public final class EmailUtil {

	public static final String BIND_EMAIL = "email/template/uac-register-binemail.xml";
	
	private EmailUtil(){}
	
	public static void sendEmail(SendEmailRequest emailRequest) {
		String htmlcontext = EmailTemplateUtil.buildHtmlTextFromTemplate(emailRequest.getTemplateRUL(), emailRequest.getData());
		try {
			EmailFactory.SendEmail(emailRequest.getTomails(), emailRequest.getCcmails(), emailRequest.getSubject(), htmlcontext);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
