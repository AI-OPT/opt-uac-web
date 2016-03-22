package com.ai.opt.sso.handler;

import java.lang.reflect.InvocationTargetException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.security.auth.login.CredentialException;
import javax.security.auth.login.LoginException;
import javax.validation.constraints.NotNull;

import org.apache.commons.beanutils.BeanUtils;
import org.jasig.cas.Message;
import org.jasig.cas.authentication.BasicCredentialMetaData;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.handler.NoOpPrincipalNameTransformer;
import org.jasig.cas.authentication.handler.PasswordEncoder;
import org.jasig.cas.authentication.handler.PlainTextPasswordEncoder;
import org.jasig.cas.authentication.handler.PrincipalNameTransformer;
import org.jasig.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.jasig.cas.authentication.principal.SimplePrincipal;
import org.jasig.cas.authentication.support.PasswordPolicyConfiguration;
import org.springframework.util.StringUtils;

import com.ai.opt.base.exception.RPCSystemException;
import com.ai.opt.sdk.util.Md5Encoder;
import com.ai.opt.sso.constants.SSOConstants;
import com.ai.opt.sso.exception.PasswordIsNullException;
import com.ai.opt.sso.exception.SystemException;
import com.ai.opt.sso.exception.TenantIdIsNullException;
import com.ai.opt.sso.exception.UsernameIsNullException;
import com.ai.opt.sso.principal.BssCredentials;
import com.ai.opt.sso.service.LoadAccountService;
import com.ai.opt.uac.api.sso.param.UserLoginResponse;

public final class BssCredentialsAuthencationHandler extends AbstractPreAndPostProcessingAuthenticationHandler{

	@Resource
	private LoadAccountService loadAccountService;
	@NotNull
	private PasswordEncoder passwordEncoder;

	@NotNull
	private PrincipalNameTransformer principalNameTransformer;
	private PasswordPolicyConfiguration passwordPolicyConfiguration;
	
	public BssCredentialsAuthencationHandler(){
		this.passwordEncoder = new PlainTextPasswordEncoder();
		this.principalNameTransformer = new NoOpPrincipalNameTransformer();
	}
	@Override
	public boolean supports(Credential credentials) {
		return credentials!=null&&(BssCredentials.class.isAssignableFrom(credentials.getClass()));
	}

	@Override
	protected HandlerResult doAuthentication(final Credential credentials)
			throws GeneralSecurityException, PreventedException {
		logger.debug("开始认证用户凭证credentials");
		if(credentials == null){
			logger.info("用户凭证credentials为空");
			throw new LoginException("Credentials is null");
		}
		BssCredentials bssCredentials = (BssCredentials) credentials;
		final String username = bssCredentials.getUsername();
		final String pwdFromPage = bssCredentials.getPassword();
		
		if(StringUtils.hasText(username)&&StringUtils.hasText(pwdFromPage)){
			UserLoginResponse user = null;
			try {
				user = loadAccountService.loadAccount(bssCredentials);
				if(user == null){
					throw new CredentialException("用户不存在");
				}
				String dbPwd=user.getAccountPassword();
				String encryDbPwd=Md5Encoder.encodePassword(SSOConstants.AIOPT_SALT_KEY+dbPwd);
				if(!pwdFromPage.equals(encryDbPwd)){
					//密码不对
					throw new CredentialException("密码错误");
				}
				/*if(!SSOConstants.ACCOUNT_ACITVE_STATE.equals(user.getState())){
					//密码不对
					throw new CredentialException("账号状态异常");
				}*/
				Date currentDate=new Date();
				Date acitveDate=user.getActiveTime();
				Date inactiveDate=user.getInactiveTime();
				if(acitveDate!=null&&currentDate.before(acitveDate)){
					throw new CredentialException("账号未生效");
				}
				if(inactiveDate!=null&&inactiveDate.before(currentDate)){
					throw new CredentialException("账号已失效");
				}
				
				BeanUtils.copyProperties(bssCredentials, user);
			}
			catch (IllegalAccessException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (RPCSystemException e) {
				//TODO
				logger.error("验证员工登录失败",e);
				throw new CredentialException("系统错误");
			}
			catch (SystemException e) {
				//TODO
				logger.error("验证员工登录失败",e);
				throw new CredentialException("系统异常");
			}
			logger.info("用户 [" + username + "] 认证成功。");
            return creatHandlerResult(bssCredentials, new SimplePrincipal(username),
    				null);
		}else{
			logger.error("用户 [" + username + "] 认证失败。");
			if(!StringUtils.hasText(username)){
				throw new UsernameIsNullException();
			}else if(!StringUtils.hasText(pwdFromPage)){
				throw new PasswordIsNullException();
			}else{
				throw new TenantIdIsNullException();
			}
		}
	}

	private HandlerResult creatHandlerResult(BssCredentials bssCredentials,
			SimplePrincipal simplePrincipal, List<Message> warnings) {
		return new HandlerResult(this, new BasicCredentialMetaData(bssCredentials), simplePrincipal, warnings);
	}
	
	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public PrincipalNameTransformer getPrincipalNameTransformer() {
		return principalNameTransformer;
	}

	public void setPrincipalNameTransformer(
			PrincipalNameTransformer principalNameTransformer) {
		this.principalNameTransformer = principalNameTransformer;
	}

	public PasswordPolicyConfiguration getPasswordPolicyConfiguration() {
		return passwordPolicyConfiguration;
	}

	public void setPasswordPolicyConfiguration(
			PasswordPolicyConfiguration passwordPolicyConfiguration) {
		this.passwordPolicyConfiguration = passwordPolicyConfiguration;
	}

}
