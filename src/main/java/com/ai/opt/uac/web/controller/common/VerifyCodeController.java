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

import com.ai.opt.uac.web.util.VerifyCodeUtil;

@Controller
@RequestMapping("/verifyCode")
public class VerifyCodeController {
	private static final Logger LOGGER = LoggerFactory.getLogger(VerifyCodeController.class);

	@RequestMapping("/getImageVerifyCode")
	public String getImageVerifyCode(HttpServletRequest request, HttpServletResponse response) {
		try {
			BufferedImage image = VerifyCodeUtil.getImageVerifyCode(request, "", "");
			ImageIO.write(image, "PNG", response.getOutputStream());
		} catch (IOException e) {
			LOGGER.error("生成图片错误：" + e);
			e.printStackTrace();
		}
		return null;
	}
}
