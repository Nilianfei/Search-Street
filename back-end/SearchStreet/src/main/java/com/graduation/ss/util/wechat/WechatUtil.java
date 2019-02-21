package com.graduation.ss.util.wechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graduation.ss.dto.UserCode2Session;
import com.graduation.ss.dto.WechatUser;
import com.graduation.ss.entity.PersonInfo;

public class WechatUtil {
	
	private static Logger log = LoggerFactory.getLogger(WechatUtil.class);
	/**
	 * 获取UserAccessToken实体类
	 * 
	 * @param code
	 * @return
	 * @throws IOException
	 */
	public static UserCode2Session getUserCode2Session(String code) throws IOException {
		// 测试号信息里的appId
		String appId = "wxe352f27338f7b857";
		log.debug("appId:" + appId);
		// 测试号信息里的appsecret
		String appsecret = "5c2d9aaca4d884c59b948a000ed91b04";
		log.debug("secret:" + appsecret);
		// 根据传入的code,拼接出访问微信定义好的接口的URL
		String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId + "&secret=" + appsecret
				+ "&js_code=" + code + "&grant_type=authorization_code";
		// 向相应URL发送请求获取token json字符串
		String tokenStr = httpsRequest(url, "GET", null);
		log.debug("userAccessToken:" + tokenStr);
		UserCode2Session userCode2Session = new UserCode2Session();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			// 将json字符串转换成相应对象
			userCode2Session = objectMapper.readValue(tokenStr, UserCode2Session.class);
		} catch (JsonParseException e) {
			log.error("获取用户accessToken失败: " + e.getMessage());
			e.printStackTrace();
		} catch (JsonMappingException e) {
			log.error("获取用户accessToken失败: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.error("获取用户accessToken失败: " + e.getMessage());
			e.printStackTrace();
		}
		if (userCode2Session == null) {
			log.error("获取用户Code2Session失败。");
			return null;
		}
		return userCode2Session;
	}

	/**
	 * 将WechatUser里的信息转换成PersonInfo的信息并返回PersonInfo实体类
	 * 
	 * @param user
	 * @return
	 */
	public static PersonInfo getPersonInfoFromRequest(WechatUser user) {
		PersonInfo personInfo = new PersonInfo();
		personInfo.setUserName(user.getNickName());
		personInfo.setSex(user.getGender());
		personInfo.setProfileImg(user.getAvatarUrl());
		personInfo.setEnableStatus(1);
		personInfo.setSouCoin(100L);
		return personInfo;
	}

	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param outputStr
	 *            提交的数据
	 * @return json字符串
	 */
	public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			log.debug("https buffer:" + buffer.toString());
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}
		return buffer.toString();
	}
}
