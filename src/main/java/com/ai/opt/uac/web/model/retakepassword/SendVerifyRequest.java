package com.ai.opt.uac.web.model.retakepassword;

public class SendVerifyRequest {
	private Long accountId;
	/**验证方式*/
	private String checkType;
	
	public String getCheckType() {
		return checkType;
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
}
