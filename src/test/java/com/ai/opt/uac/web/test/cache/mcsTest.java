package com.ai.opt.uac.web.test.cache;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.ai.opt.sdk.cache.factory.CacheClientFactory;
import com.ai.paas.ipaas.mcs.interfaces.ICacheClient;

public class mcsTest {
    private ICacheClient cacheClient;

    private String namespace = "com.ai.opt.uac.sso.unicache";

    @Before
    public void initData() {
        this.cacheClient = CacheClientFactory.getCacheClient(
                namespace);
    }
    //@Ignore
    @Test
    public void addCache() {
        cacheClient.set("testKey", "testValue");
        assertEquals("testValue", cacheClient.get("testKey"));
    }
}
