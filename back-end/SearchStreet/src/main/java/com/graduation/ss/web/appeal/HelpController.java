package com.graduation.ss.web.appeal;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.graduation.ss.dto.HelpExecution;
import com.graduation.ss.dto.UserCode2Session;
import com.graduation.ss.entity.Help;
import com.graduation.ss.entity.WechatAuth;
import com.graduation.ss.enums.HelpStateEnum;
import com.graduation.ss.exceptions.HelpOperationException;
import com.graduation.ss.service.HelpService;
import com.graduation.ss.service.WechatAuthService;
import com.graduation.ss.util.HttpServletRequestUtil;
import com.graduation.ss.util.JWT;

@RestController
@RequestMapping("/help")
public class HelpController {
	@Autowired
	private HelpService helpService;
	@Autowired
	private WechatAuthService wechatAuthService;

	@RequestMapping(value = "/gethelplistbyappealid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getHelpListByAppealId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long appealId = HttpServletRequestUtil.getLong(request, "appealId");
		if (appealId <= 0) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "appealId无效");
		} else {
			try {
				Help helpCondition = new Help();
				helpCondition.setAppealId(appealId);
				HelpExecution helpExecution = helpService.getHelpList(helpCondition, 0, 100);
				modelMap.put("helpList", helpExecution.getHelpList());
				modelMap.put("success", true);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/queryishelp", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> queryisHelp(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long appealId = HttpServletRequestUtil.getLong(request, "appealId");
		String token = HttpServletRequestUtil.getString(request, "token");
		Long userId = null;
		UserCode2Session userCode2Session = null;
		// 将token解密成openId 和session_key
		userCode2Session = JWT.unsign(token, UserCode2Session.class);
		// 获取个人信息
		String openId = userCode2Session.getOpenId();
		try {
			WechatAuth wechatAuth = wechatAuthService.getWechatAuthByOpenId(openId);
			userId = wechatAuth.getUserId();
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		if (appealId <= 0) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "appealId无效");
		} else {
			try {
				Help helpCondition = new Help();
				helpCondition.setAppealId(appealId);
				helpCondition.setUserId(userId);
				HelpExecution helpExecution = helpService.getHelpList(helpCondition, 0, 100);
				if(helpExecution.getCount()>0){
					modelMap.put("ishelp", true);
					modelMap.put("success", true);
				} else{
					modelMap.put("ishelp", false);
					modelMap.put("success", true);
				}
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/gethelplistbyuserid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getHelpListByUserId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		String token = HttpServletRequestUtil.getString(request, "token");
		Long userId = null;
		UserCode2Session userCode2Session = null;
		// 将token解密成openId 和session_key
		userCode2Session = JWT.unsign(token, UserCode2Session.class);
		// 获取个人信息
		String openId = userCode2Session.getOpenId();
		try {
			WechatAuth wechatAuth = wechatAuthService.getWechatAuthByOpenId(openId);
			userId = wechatAuth.getUserId();
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		try {
			Help helpCondition = new Help();
			helpCondition.setUserId(userId);
			HelpExecution helpExecution = helpService.getHelpList(helpCondition, 0, 100);
			modelMap.put("helpList", helpExecution.getHelpList());
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}

	@RequestMapping(value = "/gethelpbyhelpid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getHelpByHelpId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long helpId = HttpServletRequestUtil.getLong(request, "helpId");
		if (helpId > 0) {
			try {
				// 获取帮助信息
				Help help = helpService.getByHelpId(helpId);
				modelMap.put("help", help);
				modelMap.put("success", true);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "helpId无效");
		}
		return modelMap;
	}

	@RequestMapping(value = "/addHelp", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addHelp(@RequestBody Help help, String token) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long userId = null;
		System.out.println(help.toString());
		UserCode2Session userCode2Session = null;
		// 将token解密成openId 和session_key
		userCode2Session = JWT.unsign(token, UserCode2Session.class);
		// 获取个人信息
		String openId = userCode2Session.getOpenId();
		try {
			WechatAuth wechatAuth = wechatAuthService.getWechatAuthByOpenId(openId);
			userId = wechatAuth.getUserId();
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		help.setUserId(userId);
		HelpExecution helpExecution;
		try {
			helpExecution = helpService.addHelp(help);
			if (helpExecution.getState() == HelpStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
				modelMap.put("helpId", helpExecution.getHelp().getHelpId());
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", helpExecution.getStateInfo());
			}
		} catch (HelpOperationException e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}

	@RequestMapping(value = "/modifyhelp", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyHelp(@RequestBody Help help) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		HelpExecution helpExecution;
		try {
			helpExecution = helpService.modifyHelp(help);
			if (helpExecution.getState() == HelpStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
				modelMap.put("helpId", helpExecution.getHelp().getHelpId());
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", helpExecution.getStateInfo());
			}
		} catch (HelpOperationException e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
}
