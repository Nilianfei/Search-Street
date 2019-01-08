package com.graduation.ss.web.wechat;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.graduation.ss.dto.UserCode2Session;
import com.graduation.ss.dto.WechatAuthExecution;
import com.graduation.ss.dto.WechatUser;
import com.graduation.ss.entity.PersonInfo;
import com.graduation.ss.entity.WechatAuth;
import com.graduation.ss.enums.WechatAuthStateEnum;
import com.graduation.ss.service.PersonInfoService;
import com.graduation.ss.service.WechatAuthService;
import com.graduation.ss.util.JWT;
import com.graduation.ss.util.wechat.WechatUtil;

@RestController
@RequestMapping("/wechat")
public class WechatLoginController {
	@Autowired
	private WechatAuthService wechatAuthService;
	@Autowired
	private PersonInfoService personInfoService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> wechatLogin(@RequestBody WechatUser wechatUser) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		String code = wechatUser.getCode();
		String openId = null;
		WechatAuth auth = null;
		if(null != code) {
			UserCode2Session code2Session;
			try{
				code2Session = WechatUtil.getUserCode2Session(code);
				openId = code2Session.getOpenId();
				System.out.println(code2Session.toString());
				String token = JWT.sign(code2Session, 30L * 24L * 3600L * 1000L);
				System.out.println(token);
				modelMap.put("token", token);
				auth = wechatAuthService.getWechatAuthByOpenId(openId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(auth == null) {
			PersonInfo personInfo = WechatUtil.getPersonInfoFromRequest(wechatUser);
			auth = new WechatAuth();
			auth.setOpenId(openId);
			WechatAuthExecution we = wechatAuthService.register(auth, personInfo);
			if(we.getState() != WechatAuthStateEnum.SUCCESS.getState()) {
				modelMap.put("success", false);
				modelMap.put("errMsg", "wechatAuthService.register失败");
				return modelMap;
			}
		}
		modelMap.put("success", true);
		return modelMap;
	}
	
	@RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getWechatUserInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		String token = request.getParameter("token");
		UserCode2Session userCode2Session = null;
		if(null != token) {
			userCode2Session = JWT.unsign(token, UserCode2Session.class);
			if(null != userCode2Session && null != userCode2Session.getOpenId() && null != userCode2Session.getSession_key()){
				System.out.println("getWechatUserInfo:"+userCode2Session.toString());
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "token无效");
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "token获取失败");
			return modelMap;
		}
		String openId = userCode2Session.getOpenId();
		try{
			WechatAuth wechatAuth = wechatAuthService.getWechatAuthByOpenId(openId);
			Long userId = wechatAuth.getUserId();
			PersonInfo personInfo = personInfoService.getPersonInfoByUserId(userId);
			if(null != personInfo){
				modelMap.put("success", true);
				modelMap.put("personInfo", personInfo);
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "获取用户信息失败");
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
}
