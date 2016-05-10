package com.ai.opt.sso.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ai.opt.base.exception.RPCSystemException;
import com.ai.opt.sdk.dubbo.util.DubboConsumerFactory;
import com.ai.opt.sso.principal.BssCredentials;
import com.ai.opt.uac.api.sso.interfaces.ILoginSV;
import com.ai.opt.uac.api.sso.param.UserLoginResponse;

/**
 * 加载账号信息服务（Dubbo服务）
 *
 * Date: 2016年3月17日 <br>
 * Copyright (c) 2016 asiainfo.com <br>
 * @author gucl
 */
@Service
public class LoadAccountService {

	private static final Logger LOG = LoggerFactory.getLogger(LoadAccountService.class);

	/**
	 * 加载账号信息
	 * 
	 * @param bssCredentials
	 * @throws RPCSystemException 
	 */
	public UserLoginResponse loadAccount(BssCredentials bssCredentials)
			throws RPCSystemException {
		UserLoginResponse user = null;
		if (bssCredentials != null) {
			user= DubboConsumerFactory.getService(ILoginSV.class).queryAccountByUserName(bssCredentials.getUsername());
		} // end if
		
		return user;
	}// end

}
