package com.ai.opt.uac.web.util;

import static org.junit.Assert.assertEquals;

import org.hibernate.annotations.Cache;
import org.junit.Before;
import org.junit.Ignore;
import org.springframework.cache.annotation.CachePut;

import com.ai.opt.sdk.cache.factory.CacheClientFactory;
import com.ai.paas.ipaas.mcs.interfaces.ICacheClient;

public class cacheUtil {
    private ICacheClient cacheClient;

    private String namespace = "com.ai.opt.uac.web.util";

    @Before
    public void initData() {
        this.cacheClient = CacheClientFactory.getCacheClient(
                namespace);
    }
   
 @CachePut
    public void addCache(String key,String value) {
        System.out.println(key+"====="+value);
        cacheClient.set(key, value);
        assertEquals(value, cacheClient.get(key));
    }
    
 @CachePut
    public String getCache(String key) {
       return cacheClient.get(key);
        
    }
    
}
