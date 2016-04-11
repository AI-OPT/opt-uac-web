package com.ai.opt.uac.web.test.ccs;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.ai.opt.sdk.configcenter.client.IConfigCenterClient;
import com.ai.opt.sdk.configcenter.factory.ConfigCenterFactory;
import com.ai.opt.sdk.constants.SDKConstants;
import com.ai.opt.uac.web.constants.Constants;
import com.ai.opt.uac.web.constants.VerifyConstants;
import com.ai.opt.uac.web.constants.VerifyConstants.EmailVerifyConstants;
import com.ai.opt.uac.web.constants.VerifyConstants.PhoneVerifyConstants;
import com.ai.opt.uac.web.constants.VerifyConstants.PictureVerifyConstants;

public class IConfigCenterClientTest {

    private IConfigCenterClient client;

    @Before
    public void initData() {
        this.client = ConfigCenterFactory.getConfigCenterClient();
    }

    @Ignore
    @Test
    public void testGetConfig() throws Exception {
        client.add("/test", "test");
        assertEquals("test", client.get("/test"));
        System.out.println("aaaaaa");
    }

    //@Ignore
    @Test
    public void addMcsConfig() {
        // 缓存服务主机
        String uacRedisHost = "uacRedisHost";
        // 缓存空间
        String cachesnsConfig = "{\"com.ai.opt.uac.sso.unicache\":\"" + uacRedisHost
                + "\",\"com.ai.opt.uac.register.cache\":\"" + uacRedisHost
                + "\",\""+Constants.RetakePassword.CACHE_NAMESPACE+"\":\"" + uacRedisHost
                + "\",\""+Constants.UpdateEmail.CACHE_NAMESPACE+"\":\"" + uacRedisHost
                + "\",\""+Constants.UpdatePhone.CACHE_NAMESPACE+"\":\"" + uacRedisHost
                + "\",\""+Constants.UpdatePassword.CACHE_NAMESPACE+"\":\"" + uacRedisHost
                + "\",\""+Constants.LoginConstant.CACHE_NAMESPACE+"\":\"" + uacRedisHost
                + "\",\""+Constants.BandEmail.CACHE_NAMESPACE+"\":\"" + uacRedisHost
                + "\",\"com.ai.opt.uni.session.sessionclient.uacweb\":\"" + uacRedisHost + "\"}";
        
        StringBuilder bu=new StringBuilder();
        bu.append("{								");
        bu.append("  \"uacRedisHost\":                     ");
        bu.append("  {                                      ");
        bu.append("		  \"mcsHost\":\"10.1.130.84:16379\",     ");
        //bu.append("		  \"mcsHost\":\"localhost:6379\",     ");
        bu.append("	  	\"mcsMaxtotal\":\"200\",            ");
        bu.append("		  \"mcsMaxIdle\":\"10\",              ");
        bu.append("		  \"mcsMinIdle\":\"5\",               ");
        bu.append("		  \"mcsTestOnBorrow\":\"true\",       ");
        bu.append("		  \"mcsPassword\":\"\"          ");
        bu.append("  }                                     ");
        bu.append("}                                        ");
        
        
        
        // 缓存服务主机和密码设置
        if (!client.exists(
                SDKConstants.PAAS_CACHE_REDIS_CLUSTER_MAPPED_PATH)) {
            client.add(
                    SDKConstants.PAAS_CACHE_REDIS_CLUSTER_MAPPED_PATH,
                    bu.toString());
        } else {
            client.modify(
                    SDKConstants.PAAS_CACHE_REDIS_CLUSTER_MAPPED_PATH,
                    bu.toString());
        }

        // 缓存空间配置
        if (!client.exists(SDKConstants.PAAS_CACHENS_MCS_MAPPED_PATH))
            client.add(SDKConstants.PAAS_CACHENS_MCS_MAPPED_PATH,
                    cachesnsConfig);
        else {
            client.modify(SDKConstants.PAAS_CACHENS_MCS_MAPPED_PATH,
                    cachesnsConfig);
        }
    }
    //@Ignore
    @Test
    public void readMcsConfig() {
    	
    	String cachesns=client.get(SDKConstants.PAAS_CACHENS_MCS_MAPPED_PATH);
    	String redisconf=client.get(SDKConstants.PAAS_CACHE_REDIS_CLUSTER_MAPPED_PATH);
    	
    	System.out.println("cachesns:"+cachesns);
    	System.out.println("redisconf:"+redisconf);
    	
    }

    

    /**
     * DBS配置
     */
     @Test
    public void addDbConfInfo() {
        System.out.println("DBConf config ... start");
        StringBuilder sb = new StringBuilder();

        sb.append("{																																																				");
        sb.append("		\"opt-uac-db\":                                                                                   ");
        sb.append("		{                                                                                                     ");
        sb.append("			\"driverClassName\":\"com.mysql.jdbc.Driver\",                                                          ");
        sb.append("			\"jdbcUrl\":\"jdbc:mysql://10.1.235.245:31306/dev_baas_uacdb1?useUnicode=true&characterEncoding=UTF-8\",   ");
        sb.append("			\"username\":\"uacusr01\",                                                                         ");
        sb.append("			\"password\":\"uacusr01_123\",                                                                         ");

//        sb.append("			\"jdbcUrl\":\"jdbc:mysql://127.0.0.1:3306/system?useUnicode=true&characterEncoding=UTF-8\",   ");
//        sb.append("			\"username\":\"root\",                                                                         ");
//        sb.append("			\"password\":\"123456\",                                                                         ");

        
        sb.append("			\"autoCommit\":\"true\",                                                                                ");
        sb.append("			\"connectionTimeout\":\"30000\",                                                                        ");
        sb.append("			\"idleTimeout\":\"600000\",                                                                             ");
        sb.append("			\"maxLifetime\":\"1800000\",                                                                            ");
        sb.append("			\"maximumPoolSize\":\"10\"                                                                              ");
        sb.append("		}                                                                                                     ");
        sb.append("}                                                                                                        ");

        if (!client.exists(SDKConstants.DB_CONF_PATH)) {
            client.add(SDKConstants.DB_CONF_PATH, sb.toString());
        } else {
            client.modify(SDKConstants.DB_CONF_PATH, sb.toString());
        }

        System.out.println("DBConf config ... end");
    }
     
     @Test
     public void addUrlConfig(){
    	 System.out.println("url config ... start");
    	 String indexUrl = "http://10.1.235.245:14115/uac";
    	 if (!client.exists(Constants.URLConstant.INDEX_URL_KEY)) {
             client.add(Constants.URLConstant.INDEX_URL_KEY, indexUrl);
         } else {
             client.modify(Constants.URLConstant.INDEX_URL_KEY, indexUrl);
         }

    	 System.out.println("url config ... end");
     }
     
     @Test
     public void addSendVerifyTimesConfig(){
    	 System.out.println("addSendVerifyTimesConfig ... start");
    	 String PHONE_VERIFY_OVERTIME = "300";
    	 String PHONE_SEND_VERIFY_MAX_TIME = "60";
    	 String EMAIL_VERIFY_OVERTIME = "1800";
    	 String EMAIL_SEND_VERIFY_MAX_TIME = "60";
    	 String PICTURE_VERIFY_OVERTIME = "600";
    	 String PHONE_IP_SEND_MAXTIMES="5";
    	 String EMAIL_IP_SEND_MAXTIMES="5";
    	 String PHONE_IP_SEND_OVERTIME="300";
    	 String EMAIL_IP_SEND_OVERTIME="300";
    	 if (!client.exists(VerifyConstants.PhoneVerifyConstants.SEND_VERIFY_MAX_TIME_KEY)) {
             client.add(PhoneVerifyConstants.SEND_VERIFY_MAX_TIME_KEY, PHONE_SEND_VERIFY_MAX_TIME);
         } else {
             client.modify(PhoneVerifyConstants.SEND_VERIFY_MAX_TIME_KEY, PHONE_SEND_VERIFY_MAX_TIME);
         }
    	 if (!client.exists(VerifyConstants.PhoneVerifyConstants.VERIFY_OVERTIME_KEY)) {
             client.add(PhoneVerifyConstants.VERIFY_OVERTIME_KEY, PHONE_VERIFY_OVERTIME);
         } else {
             client.modify(PhoneVerifyConstants.VERIFY_OVERTIME_KEY, PHONE_VERIFY_OVERTIME);
         }
    	 
    	 if (!client.exists(VerifyConstants.EmailVerifyConstants.SEND_VERIFY_MAX_TIME_KEY)) {
             client.add(EmailVerifyConstants.SEND_VERIFY_MAX_TIME_KEY, EMAIL_SEND_VERIFY_MAX_TIME);
         } else {
             client.modify(EmailVerifyConstants.SEND_VERIFY_MAX_TIME_KEY, EMAIL_SEND_VERIFY_MAX_TIME);
         }
    	 if (!client.exists(VerifyConstants.EmailVerifyConstants.VERIFY_OVERTIME_KEY)) {
             client.add(EmailVerifyConstants.VERIFY_OVERTIME_KEY, EMAIL_VERIFY_OVERTIME);
         } else {
             client.modify(EmailVerifyConstants.VERIFY_OVERTIME_KEY, EMAIL_VERIFY_OVERTIME);
         }
    	 if (!client.exists(VerifyConstants.PictureVerifyConstants.VERIFY_OVERTIME_KEY)) {
             client.add(PictureVerifyConstants.VERIFY_OVERTIME_KEY, PICTURE_VERIFY_OVERTIME);
         } else {
             client.modify(PictureVerifyConstants.VERIFY_OVERTIME_KEY, PICTURE_VERIFY_OVERTIME);
         }
    	 if (!client.exists(VerifyConstants.PhoneVerifyConstants.SEND_VERIFY_IP_MAX_NO_KEY)) {
             client.add(PhoneVerifyConstants.SEND_VERIFY_IP_MAX_NO_KEY, PHONE_IP_SEND_MAXTIMES);
         } else {
             client.modify(PhoneVerifyConstants.SEND_VERIFY_IP_MAX_NO_KEY, PHONE_IP_SEND_MAXTIMES);
         }
    	 if (!client.exists(VerifyConstants.EmailVerifyConstants.SEND_VERIFY_IP_MAX_NO_KEY)) {
             client.add(EmailVerifyConstants.SEND_VERIFY_IP_MAX_NO_KEY, EMAIL_IP_SEND_MAXTIMES);
         } else {
             client.modify(EmailVerifyConstants.SEND_VERIFY_IP_MAX_NO_KEY, EMAIL_IP_SEND_MAXTIMES);
         }
    	 
    	 if (!client.exists(VerifyConstants.PhoneVerifyConstants.IP_SEND_OVERTIME_KEY)) {
             client.add(PhoneVerifyConstants.IP_SEND_OVERTIME_KEY, PHONE_IP_SEND_OVERTIME);
         } else {
             client.modify(PhoneVerifyConstants.IP_SEND_OVERTIME_KEY, PHONE_IP_SEND_OVERTIME);
         }
    	 
    	 if (!client.exists(VerifyConstants.EmailVerifyConstants.IP_SEND_OVERTIME_KEY)) {
             client.add(EmailVerifyConstants.IP_SEND_OVERTIME_KEY, EMAIL_IP_SEND_OVERTIME);
         } else {
             client.modify(EmailVerifyConstants.IP_SEND_OVERTIME_KEY, EMAIL_IP_SEND_OVERTIME);
         }
    	 System.out.println("addSendVerifyTimesConfig ... end");
     }
    
}