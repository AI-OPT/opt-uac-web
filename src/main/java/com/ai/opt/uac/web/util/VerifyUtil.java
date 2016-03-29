package com.ai.opt.uac.web.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.opt.base.vo.ResponseHeader;
import com.ai.opt.sdk.cache.factory.CacheClientFactory;
import com.ai.opt.sdk.mail.EmailFactory;
import com.ai.opt.sdk.mail.EmailTemplateUtil;
import com.ai.opt.sdk.util.DubboConsumerFactory;
import com.ai.opt.sdk.util.RandomUtil;
import com.ai.opt.uac.api.seq.interfaces.ICreateSeqSV;
import com.ai.opt.uac.api.seq.param.PhoneMsgSeqResponse;
import com.ai.opt.uac.web.constants.Constants;
import com.ai.opt.uac.web.constants.VerifyConstants.PictureVerifyConstants;
import com.ai.opt.uac.web.model.email.SendEmailRequest;
import com.ai.paas.ipaas.mcs.interfaces.ICacheClient;

public class VerifyUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(VerifyUtil.class);

	public static BufferedImage getImageVerifyCode(HttpServletRequest request, String namespace, String cacheKey) {
		int width = 100, height = 40;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// 获取图形上下文
		Graphics g = image.getGraphics();

		// 设定背景色
		g.setColor(new Color(0xDCDCDC));
		g.fillRect(0, 0, width, height);

		// 画边框
		g.setColor(Color.black);
		g.drawRect(0, 0, width - 1, height - 1);

		// 取随机产生的认证码
		String verifyCode = RandomUtil.randomString(PictureVerifyConstants.VERIFY_SIZE);
		// 将认证码存入缓存
		ICacheClient cacheClient = CacheClientFactory.getCacheClient(namespace);
		cacheClient.setex(cacheKey, PictureVerifyConstants.VERIFY_OVERTIME, verifyCode);
		LOGGER.debug("cacheKey=" + cacheKey + ",verifyCode=" + verifyCode);
		// 将认证码显示到图象中
		g.setColor(Color.black);

		g.setFont(new Font("Atlantic Inline", Font.PLAIN, 30));
		String Str = verifyCode.substring(0, 1);
		g.drawString(Str, 8, 25);

		Str = verifyCode.substring(1, 2);
		g.drawString(Str, 28, 30);
		Str = verifyCode.substring(2, 3);
		g.drawString(Str, 48, 27);

		Str = verifyCode.substring(3, 4);
		g.drawString(Str, 68, 32);
		// 随机产生88个干扰点，使图象中的认证码不易被其它程序探测到
		Random random = new Random();
		for (int i = 0; i < 30; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			g.drawOval(x, y, 0, 0);
		}

		// 图象生效
		g.dispose();
		return image;

	}

	public static void sendEmail(SendEmailRequest emailRequest) {
		String htmlcontext = EmailTemplateUtil.buildHtmlTextFromTemplate(emailRequest.getTemplateRUL(), emailRequest.getData());
		try {
			EmailFactory.SendEmail(emailRequest.getTomails(), emailRequest.getCcmails(), emailRequest.getSubject(), htmlcontext);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建短信信息seq
	 * @return
	 */
	public static String createPhoneMsgSeq() {
		ICreateSeqSV service = DubboConsumerFactory.getService("iCreateSeqSV");
		PhoneMsgSeqResponse msgSeqResponse = service.createPhoneMsgSeq();
		if (msgSeqResponse != null) {
			ResponseHeader responseHeader = msgSeqResponse.getResponseHeader();
			String resultCode = responseHeader.getResultCode();
			if (Constants.ResultCode.SUCCESS_CODE.equals(resultCode)) {
				return msgSeqResponse.getMsgSeqId();
			}
		}
		return null;
	}
}
