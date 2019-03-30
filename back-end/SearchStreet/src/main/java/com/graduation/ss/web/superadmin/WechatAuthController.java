package com.graduation.ss.web.superadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.graduation.ss.dto.ConstantForSuperAdmin;
import com.graduation.ss.dto.WechatAuthExecution;
import com.graduation.ss.entity.WechatAuth;
import com.graduation.ss.service.WechatAuthService;
import com.graduation.ss.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/superadmin")
public class WechatAuthController {
	@Autowired
	private WechatAuthService wechatAuthService;

	@RequestMapping(value = "/listwechatauths", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> listWechatAuths(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		WechatAuthExecution wae = null;
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_NO);
		int pageSize = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_SIZE);
		// 空值判断
		if (pageIndex > 0 && pageSize > 0) {
			try {
				wae = wechatAuthService.getWechatAuthList(pageIndex, pageSize);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (wae.getWechatAuthList() != null) {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, wae.getWechatAuthList());
				modelMap.put(ConstantForSuperAdmin.TOTAL, wae.getCount());
				modelMap.put("success", true);
			} else {
				// 取出数据为空，也返回new list以使得前端不出错
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, new ArrayList<WechatAuth>());
				modelMap.put(ConstantForSuperAdmin.TOTAL, 0);
				modelMap.put("success", true);
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "空的查询信息");
			return modelMap;
		}
	}

	@RequestMapping(value = "/searchwechatauthbyuserid", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> searchLocalAuthbyUserId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		WechatAuth wechatAuth = null;
		Long userId = HttpServletRequestUtil.getLong(request, "userId");
		if (userId != null) {
			try {
				wechatAuth = wechatAuthService.getWechatAuthByUserId(userId);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (wechatAuth != null) {
				List<WechatAuth> wechatAuthList = new ArrayList<WechatAuth>();
				wechatAuthList.add(wechatAuth);
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, wechatAuthList);
				modelMap.put(ConstantForSuperAdmin.TOTAL, 1);
				modelMap.put("success", true);
			} else {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, new ArrayList<WechatAuth>());
				modelMap.put(ConstantForSuperAdmin.TOTAL, 0);
				modelMap.put("success", true);
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "空的查询信息");
			return modelMap;
		}
	}
}
