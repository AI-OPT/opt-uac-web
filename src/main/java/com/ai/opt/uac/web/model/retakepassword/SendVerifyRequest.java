package com.ai.opt.uac.web.model.retakepassword;

public class SendVerifyRequest {
	private Long accountId;
	/**验证方式*/
	private String checkType;
	/**验证地址*/
	private String checkAddress;
	
	public String getCheckType() {
		return checkType;
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	public String getCheckAddress() {
		return checkAddress;
	}
	public void setCheckAddress(String checkAddress) {
		this.checkAddress = checkAddress;
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
}
