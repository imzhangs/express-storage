package com.mine.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.kd.commons.result.ModelResult;
import com.mine.commons.consts.CachePrefixConsts;
import com.mine.domain.UserAddress;
import com.mine.service.UserAddressService;

import redis.clients.jedis.Jedis;

@RestController
@RequestMapping("/userAddr")
public class UserAddressController {

	@Autowired
	Jedis jedis;

	@Autowired
	UserAddressService userAddressService;

	@RequestMapping("/save")
	public ModelResult<String> saveAddress(UserAddress address, String smsCode, Boolean isTest,HttpServletRequest request) {
		ModelResult<String> result = new ModelResult<>();
		if (address == null) {
			result.paramError("参数错误");
			return result;
		}

		if (StringUtils.isBlank(address.mobile) || !address.mobile.matches("^1[3-8]{1}[\\d]{9}$")) {
			result.paramError("手机号码错误");
			return result;
		}

		if (isTest == null || !isTest) {
			String getSmsCode = jedis.get(CachePrefixConsts.SMS_CODE_PREFIX + address.mobile);
			if (!getSmsCode.equalsIgnoreCase(smsCode)) {
				result.paramError("短信验证码错误");
			}
		}
		
		//X-real-ip
		String clientIp=request.getHeader("X-real-ip");
		if(StringUtils.isBlank(clientIp)){
			clientIp=request.getRemoteAddr();
		}
		address.ip=clientIp;
		
		if(StringUtils.isNoneBlank(address.address)){
			address.address=address.address.replaceAll("<|>", "#");
		}

		int insertResult = userAddressService.insertUserAddress(address);

		if (insertResult > 0) {
			result.ok();
			result.data="地址已提交成功，请耐心等待哟~ ^^";
		} else {
			result.code = 201;
			System.out.println("[[[[201]]]]" + JSONObject.toJSONString(address));
		}

		return result;
	}
}
