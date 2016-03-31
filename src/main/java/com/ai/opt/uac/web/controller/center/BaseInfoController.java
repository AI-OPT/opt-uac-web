package com.ai.opt.uac.web.controller.center;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ai.net.xss.util.StringUtil;
import com.ai.opt.base.vo.BaseInfo;
import com.ai.opt.base.vo.ResponseHeader;
import com.ai.opt.sdk.util.DubboConsumerFactory;
import com.ai.opt.sdk.web.model.ResponseData;
import com.ai.opt.sso.client.filter.SSOClientConstants;
import com.ai.opt.sso.client.filter.SSOClientUser;
import com.ai.opt.uac.api.account.interfaces.IAccountManageSV;
import com.ai.opt.uac.api.account.interfaces.IIndustryManageSV;
import com.ai.opt.uac.api.account.interfaces.ITenantManageSV;
import com.ai.opt.uac.api.account.param.AccountBaseModifyRequest;
import com.ai.opt.uac.api.account.param.AccountQueryRequest;
import com.ai.opt.uac.api.account.param.AccountQueryResponse;
import com.ai.opt.uac.api.account.param.IndustryQueryResponse;
import com.ai.opt.uac.api.account.param.TenantInfoRequest;
import com.ai.opt.uac.api.account.param.TenantInsertResponse;
import com.ai.opt.uac.api.account.param.TenantQueryResponse;
import com.ai.opt.uac.web.model.baseinfo.AccountInfoData;

@RequestMapping("/center/baseInfo")
@Controller
public class BaseInfoController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseInfoController.class);
    @RequestMapping("/baseInfoStart")
    public ModelAndView baseInfoStart(HttpServletRequest request) {

        return new ModelAndView("jsp/accountcenter/baseinfo");
    }

    @RequestMapping("/initBaseInfo")
    public ModelAndView initBaseInfo(HttpServletRequest request) {

        return new ModelAndView("jsp/accountcenter/inital-baseinfo");
    }
    /**
     * 获得账户信息
     * 
     * @param request
     * @return
     */
    @RequestMapping("/getAccountInfo")
    @ResponseBody
    public ModelAndView checkUserInfo(HttpServletRequest reques, SSOClientUser userClient) {
        // 获取账户信息
        IAccountManageSV iAccountManageSV = DubboConsumerFactory.getService("iAccountManageSV");
        ITenantManageSV iTenantManageSV = DubboConsumerFactory.getService("iTenantManageSV");
        IIndustryManageSV iIndustryManageSV=DubboConsumerFactory.getService("iIndustryManageSV");
        AccountQueryRequest accountRequest = new AccountQueryRequest();
        accountRequest.setAccountId(userClient.getAccountId());
        AccountQueryResponse acc = iAccountManageSV.queryBaseInfo(accountRequest);
        BaseInfo req = new BaseInfo();
        req.setTenantId(userClient.getTenantId());
        TenantQueryResponse ten = iTenantManageSV.queryTenantInfo(req);
        Map<String,AccountInfoData> model = new HashMap<String,AccountInfoData>();
        AccountInfoData accountInfo = new AccountInfoData();
        accountInfo.setAccountId(acc.getAccountId());
        accountInfo.setEmail(acc.getEmail());
        accountInfo.setNickName(acc.getNickName());
        accountInfo.setPhone(acc.getPhone());
        accountInfo.setTenantName(ten.getTenantName());
        //翻译企业类型
        if(!StringUtil.isBlank(ten.getIndustryCode())){
            IndustryQueryResponse response= iIndustryManageSV.queryByIndustryCode(ten.getIndustryCode());
            accountInfo.setIndustryCode(response.getIndustryName());
        }
        accountInfo.setTenantId(ten.getTenantId());
        model.put("accountInfo", accountInfo);
        
        return new ModelAndView("jsp/center/inital-baseinfo",model);
    }
    @RequestMapping("/listIndutry")
    @ResponseBody
    public List<IndustryQueryResponse> getAllIndutry() {
        IIndustryManageSV iIndustryManageSV=DubboConsumerFactory.getService("iIndustryManageSV");
       return iIndustryManageSV.queryIndustryList();
    }
    /**
     * 修改基本信息
     * 
     * @param request
     * @param data
     * @return
     */
    @RequestMapping("/updateBaseInfo")
    @ResponseBody
    public ResponseData<String> updateBaseInfo(HttpServletRequest request, AccountInfoData data) {
        ResponseData<String> responseData = null;
        try {
            SSOClientUser userClient = (SSOClientUser) request.getSession().getAttribute(SSOClientConstants.USER_SESSION_KEY);
            ResponseHeader header = new ResponseHeader();
            header.setIsSuccess(true);
            IAccountManageSV iAccountManageSV = DubboConsumerFactory.getService("iAccountManageSV");
            ITenantManageSV iTenantManageSV = DubboConsumerFactory.getService("iTenantManageSV");
            AccountBaseModifyRequest accountBase =new AccountBaseModifyRequest();
            accountBase.setNickName(data.getNickName());
            accountBase.setUpdateAccountId(data.getAccountId());
            accountBase.setAccountId(data.getAccountId());
            TenantInfoRequest tenant = new TenantInfoRequest();
            tenant.setAccountId(data.getAccountId());
            tenant.setIndustryCode(data.getIndustryCode());
            tenant.setTenantName(data.getTenantName());
            tenant.setUpdateAccountId(userClient.getAccountId());
            iAccountManageSV.updateBaseInfo(accountBase);
            TenantInsertResponse re= iTenantManageSV.insertTenantInfo(tenant);
            //修改客户端存储的昵称、tenantID
            userClient.setNickName(data.getNickName());
            userClient.setTenantId(re.getTenantId());
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "成功",
                        null);
        } catch (Exception e) {
            LOGGER.error("基本信息修改失败！", e);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "基本信息修改失败",
                    null);
        }

        return responseData;
    }
        
        
}
