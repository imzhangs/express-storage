package com.mine.commons.utils;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cloopen.rest.sdk.CCPRestSDK;

@Component
public class SDKTestSendTemplateSMS {

	
	@Value("${sms.account}")
	String smsAccount;
	
	@Value("${sms.token}")
	String smsToken;
	
	@Value("${sms.appId}")
	String appId;
	
	/**
	 * @param args
	 */
	public  boolean send(String userMobile,String randomCode) {
		HashMap<String, Object> result = null;

		CCPRestSDK restAPI = new CCPRestSDK();
		restAPI.init("app.cloopen.com", "8883");// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
		restAPI.setAccount(smsAccount, smsToken);// 初始化主帐号和主帐号TOKEN
		restAPI.setAppId(appId);// 初始化应用ID
		result = restAPI.sendTemplateSMS(userMobile,"1" ,new String[]{randomCode+"","10"});
		
		if("000000".equals(result.get("statusCode"))){
			return true;
		}else{
			//异常返回输出错误码和错误信息
			System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
			return false;
		}
	}
	
	
	public static void main(String[] args) {
		SDKTestSendTemplateSMS sms=new SDKTestSendTemplateSMS();
		sms.send("15012638207",123456+"");
	}

}
