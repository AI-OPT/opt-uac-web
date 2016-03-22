package com.ai.opt.sso.handler;

import java.util.HashMap;
import java.util.Map;

import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.PrincipalResolver;
import org.jasig.cas.authentication.principal.SimplePrincipal;

import com.ai.opt.sso.principal.BssCredentials;

public class BssCredentialToPrincipalResolver implements PrincipalResolver {

	public BssCredentialToPrincipalResolver() {}

	@Override
	public final Principal resolve(Credential credential) {
		BssCredentials bssCredentials = (BssCredentials) credential;

		String principalId = bssCredentials.getUsername();
		
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("username", bssCredentials.getUsername());
		attributes.put("accountId", bssCredentials.getAccountId());
		attributes.put("tenantId", bssCredentials.getTenantId());
		attributes.put("accountName", bssCredentials.getAccountName());
        attributes.put("nickName", bssCredentials.getNickName());
        attributes.put("phone", bssCredentials.getPhone());
        attributes.put("email", bssCredentials.getEmail());
        attributes.put("accountType", bssCredentials.getAccountType());
        attributes.put("accountLevel", bssCredentials.getAccountLevel());
        attributes.put("weixin", bssCredentials.getWeixin());
        attributes.put("weibo", bssCredentials.getWeibo());
        attributes.put("qq", bssCredentials.getQq());
		return new SimplePrincipal(principalId , attributes);
	}

	@Override
	public boolean supports(Credential paramCredential) {
		return paramCredential!=null&&BssCredentials.class.isAssignableFrom(paramCredential.getClass());
	}

}
