package com.ai.opt.uac.web.controller.common;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.opt.sdk.web.model.ResponseData;
import com.ai.opt.uac.web.constants.Constants.RetakePassword;
import com.ai.opt.uac.web.util.VerifyCodeUtil;

@Controller
@RequestMapping("/verifyCode")
public class VerifyCodeController {
	private static final Logger LOGGER = LoggerFactory.getLogger(VerifyCodeController.class);

	@RequestMapping("/getImageVerifyCode")
	@ResponseBody
	public void getImageVerifyCode(HttpServletRequest request, HttpServletResponse response) {
		BufferedImage image = VerifyCodeUtil.getImageVerifyCode(request, RetakePassword.CACHE_NAMESPACE, RetakePassword.CACHE_KEY_VERIFY_PICTURE);
		try {
			ImageIO.write(image, "PNG", response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
