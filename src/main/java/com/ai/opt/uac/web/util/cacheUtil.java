package com.ai.opt.uac.web.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.ai.opt.sdk.cache.factory.CacheClientFactory;
import com.ai.paas.ipaas.mcs.interfaces.ICacheClient;

public class cacheUtil {
    private ICacheClient cacheClient;

    private String namespace = "com.ai.opt.test.mcs";

    @Before
    public void initData() {
        this.cacheClient = CacheClientFactory.getCacheClient(
                namespace);
    }
    @Ignore
    @Test
    public void addCache(String key,String value) {
        cacheClient.set("testKey", "testValue");
        assertEquals("testValue", cacheClient.get("testKey"));
    }
    @Ignore
    @Test
    public String getCache(String key) {
       return cacheClient.get(key);
        
    }
}
