package com.ai.opt.uac.web.controller.retakepassword;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ai.opt.base.exception.RPCSystemException;
import com.ai.opt.sdk.util.DubboConsumerFactory;
import com.ai.opt.sdk.web.model.ResponseData;
import com.ai.opt.uac.api.account.interfaces.IAccountManageSV;
import com.ai.opt.uac.api.account.param.AccountQueryRequest;
import com.ai.opt.uac.api.account.param.AccountQueryResponse;
import com.ai.opt.uac.web.model.retakepassword.ConfirmData;

@RequestMapping("/retakePassword")
@Controller
public class RetakePasswordController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RetakePasswordController.class);
	
    /**
     * 身份认证界面
     * @param request
     * @return
     */
    @RequestMapping("/confirminfo")
    public ModelAndView retakePassPhone(HttpServletRequest request) {
        return new ModelAndView("jsp/retakepassword/confirminfo");
    }
    
    /**
     * 获得账户信息
     * @param request
     * @return
     */
    @RequestMapping("/getAccountInfo")
    @ResponseBody
    public ResponseData<ConfirmData> getAccountInfo(HttpServletRequest request){
    	//Long accountId = (Long)request.getSession().getAttribute("accountId");
    	Long accountId = 1L;
    	LOGGER.info("查询账户信息开始，查询参数为： accountId="+accountId);
    	AccountQueryRequest accountQueryRequest = new AccountQueryRequest();
    	accountQueryRequest.setAccountId(accountId);
    	ResponseData<ConfirmData> responseData = null;
		try {
			IAccountManageSV accountManageSV = DubboConsumerFactory.getService("iAccountManageSV");
			AccountQueryResponse accountQueryResponse = accountManageSV.queryBaseInfo(accountQueryRequest);
			String phone = accountQueryResponse.getPhone();
			String email = accountQueryResponse.getEmail();
			ConfirmData confirmInfo = new ConfirmData(phone,email);
			responseData = new ResponseData<ConfirmData>(ResponseData.AJAX_STATUS_SUCCESS, "信息查询成功", confirmInfo);
		} catch (RPCSystemException e) {
			LOGGER.error("查询失败！",e);
			responseData = new ResponseData<ConfirmData>(ResponseData.AJAX_STATUS_FAILURE, "信息查询失败", null);
			e.printStackTrace();
		}
		return responseData;
    } 

}
