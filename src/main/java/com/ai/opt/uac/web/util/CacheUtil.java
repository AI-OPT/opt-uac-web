package com.ai.opt.uac.web.util;

import net.sf.json.JSONObject;

import com.ai.opt.sdk.cache.factory.CacheClientFactory;
import com.ai.opt.sdk.util.StringUtil;
import com.ai.paas.ipaas.mcs.interfaces.ICacheClient;

public final class CacheUtil {
	private CacheUtil(){}
	
	public static void setValue(String key, int second, Object value, String namespace){
		JSONObject userObject = JSONObject.fromObject(value);
		ICacheClient cacheClient = CacheClientFactory.getCacheClient(namespace);
		cacheClient.setex(key, second, userObject.toString());
	}
	
	
	public static Object getValue(String key,String namespace,Class<?> beanClass){
		if(StringUtil.isBlank(key)||StringUtil.isBlank(namespace)){
			return null;
		}
		ICacheClient cacheClient = CacheClientFactory.getCacheClient(namespace);
		String userClientStr = cacheClient.get(key);
		if(StringUtil.isBlank(userClientStr)){
			return null;
		}
		JSONObject userObject = JSONObject.fromObject(userClientStr);
		return JSONObject.toBean(userObject, beanClass);
	}
	
	public static void deletCache(String key,String namespace){
		ICacheClient cacheClient = CacheClientFactory.getCacheClient(namespace);
		cacheClient.del(key);
	}
	
}
