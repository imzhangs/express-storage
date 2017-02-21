package com.mine.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.kd.commons.result.BaseResult;
import com.kd.commons.result.ModelResult;
import com.mine.commons.consts.CachePrefixConsts;
import com.mine.commons.utils.SDKTestSendTemplateSMS;
import com.mine.commons.utils.VerifyCodeUtil;

import redis.clients.jedis.Jedis;

@RestController
@RequestMapping("/user")
public class UserController {

	// @Autowired
	// RedisTemplate<String,String> redisTemplate ;

	@Autowired
	Jedis jedis;
	
	@Autowired
	SDKTestSendTemplateSMS smsTemplate;

	@RequestMapping("/getSmsCode")
	public ModelResult<String> getSmsCode(String mobile,String imgCode, Boolean isTest,HttpServletRequest request) {
		ModelResult<String> result = new ModelResult<>();
		if (StringUtils.isBlank(mobile) || !mobile.matches("^1[3-8]{1}[\\d]{9}$")) {
			result.paramError("手机号码错误");
			return result;
		}
		
//		Object imgVerifyCode=request.getSession().getAttribute(CachePrefixConsts.VERIFY_CODE_PREFIX + request.getSession().getId());
//		
//		if(isTest==null || !isTest){
//			
//			if(StringUtils.isBlank(imgCode) || !imgVerifyCode.toString().equalsIgnoreCase(imgCode)){
//				result.paramError("图形码错误");
//				return result;
//			}
//		}
		boolean isSent =false;

		String code = jedis.get(CachePrefixConsts.SMS_CODE_PREFIX + mobile);
		if (StringUtils.isBlank(code)) {
			code = RandomUtils.nextInt(1000, 9999) + "";
			jedis.setex(CachePrefixConsts.SMS_CODE_PREFIX + mobile, 300, code);
		}

		String count = jedis.get(mobile);
		int i = 0;
		try {
			i = StringUtils.isBlank(count) ? 1 : Integer.valueOf(count) + 1;
		} catch (Throwable e) {
			i = 1;
		}
		if (i > 10) {
			jedis.setex(CachePrefixConsts.SMS_CODE_PREFIX + mobile, 600, code);
		}else{
			isSent = this.smsTemplate.send(mobile, code);
		}
		
		jedis.setex(mobile, 3600, i + "");

		if (isSent) {
			result.data = "sent OK";
		}

		return result;
	}

	@RequestMapping("/checkSmsCode")
	public BaseResult checkSmsCode(String mobile, String code) {
		BaseResult result = new BaseResult();

		String smsCode = jedis.get(CachePrefixConsts.SMS_CODE_PREFIX + mobile);
		if (smsCode.equalsIgnoreCase(code)) {
			result.ok();
		} else {
			result.paramError("短信验证码错误");
		}

		return result;
	}

	/***
	 * 生成验证码图片
	 * 
	 * @author ZhangShuai
	 * @version 1.0
	 * @created 2015年5月19日 下午2:28:44
	 * @param id
	 */
	@RequestMapping("/buildVerifyImage")
	public ModelAndView buildVerifyImage(String _rand, HttpServletResponse response, HttpServletRequest request) {
		char[] code = VerifyCodeUtil.generateCheckCode(4);
		try {
			OutputStream output = response.getOutputStream();
			BufferedImage image = VerifyCodeUtil.getVerifyImage(code);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(image, "JPEG", bos);
			byte[] buf = bos.toByteArray();
			response.setContentLength(buf.length);
			output.write(buf);
			bos.close();
			output.close();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			String verifyCodeKey=CachePrefixConsts.VERIFY_CODE_PREFIX + request.getSession().getId();
			request.getSession().setAttribute(verifyCodeKey,new String(code));
			Object getImgCode=request.getSession().getAttribute(verifyCodeKey );
			System.out.println(getImgCode);
		}
		return null;
	}
	
	@RequestMapping("/checkImageCode")
	public ModelResult<String> checkVerifyImgeCode(String imgCode,HttpServletResponse response,HttpServletRequest request){
		// 指定允许其他域名访问  
		response.setHeader("Access-Control-Allow-Origin","*");  
		ModelResult<String> result = new ModelResult<String>();
		String verifyCodeKey=CachePrefixConsts.VERIFY_CODE_PREFIX + request.getSession().getId();
		Object getImgCode=request.getSession().getAttribute(verifyCodeKey );
		if(null==getImgCode || !imgCode.equalsIgnoreCase(getImgCode.toString())){
			result.paramError("图形验证码错误");
		}else{
			result.ok();
			//request.getSession().removeAttribute(verifyCodeKey);
		}
		return result;
	}
}
