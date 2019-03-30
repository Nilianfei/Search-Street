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
import com.graduation.ss.dto.LocalAuthExecution;
import com.graduation.ss.entity.LocalAuth;
import com.graduation.ss.enums.LocalAuthStateEnum;
import com.graduation.ss.service.LocalAuthService;
import com.graduation.ss.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/superadmin")
public class LocalAuthController {
	@Autowired
	private LocalAuthService localAuthService;

	/**
	 * 登录验证
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/logincheck", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> loginCheck(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取前端传递过来的帐号和密码
		String userName = HttpServletRequestUtil.getString(request, "userName");
		String password = HttpServletRequestUtil.getString(request, "password");
		// 空值判断
		if (userName != null && password != null) {
			// 获取本地帐号授权信息
			LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPwd(userName, password);
			if (localAuth != null) {
				// 若帐号密码正确，则验证用户的身份是否为超级管理员
				if (localAuth.getPersonInfo().getUserType() == 1) {
					if (localAuth.getPersonInfo().getEnableStatus() == 1) {
						modelMap.put("success", true);
						// 登录成功则设置上session信息
						request.getSession().setAttribute("user", localAuth.getPersonInfo());
					} else {
						modelMap.put("success", false);
						modelMap.put("errMsg", "非法用户没有权限访问");
					}
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", "非管理员没有权限访问");
				}
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "用户名或密码错误");
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "用户名和密码均不能为空");
		}
		return modelMap;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> register(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取前端传递过来的帐号和密码以及userId
		String userName = HttpServletRequestUtil.getString(request, "userName");
		String password = HttpServletRequestUtil.getString(request, "password");
		Long userId = HttpServletRequestUtil.getLong(request, "userId");
		if (userName == null) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "用户名为空");
			return modelMap;
		}
		if (password == null) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "密码为空");
			return modelMap;
		}
		if (userId == null) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "userId为空");
			return modelMap;
		}

		LocalAuth localAuth = new LocalAuth();
		localAuth.setUserId(userId);
		localAuth.setUserName(userName);
		localAuth.setPassword(password);
		try {
			LocalAuthExecution lae = localAuthService.bindLocalAuth(localAuth);
			if (lae.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", lae.getStateInfo());
			}
			return modelMap;
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}

	}

	@RequestMapping(value = "/listlocalauths", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> listLocalAuths(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		LocalAuthExecution lae = null;
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_NO);
		int pageSize = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_SIZE);
		// 空值判断
		if (pageIndex > 0 && pageSize > 0) {
			try {
				lae = localAuthService.getLocalAuthList(pageIndex, pageSize);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (lae.getLocalAuthList() != null) {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, lae.getLocalAuthList());
				modelMap.put(ConstantForSuperAdmin.TOTAL, lae.getCount());
				modelMap.put("success", true);
			} else {
				// 取出数据为空，也返回new list以使得前端不出错
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, new ArrayList<LocalAuth>());
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

	@RequestMapping(value = "/searchlocalauthbyuserid", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> searchLocalAuthbyUserId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		LocalAuth localAuth = null;
		Long userId = HttpServletRequestUtil.getLong(request, "userId");
		if (userId != null) {
			try {
				localAuth = localAuthService.getLocalAuthByUserId(userId);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (localAuth != null) {
				List<LocalAuth> localAuthList = new ArrayList<LocalAuth>();
				localAuthList.add(localAuth);
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, localAuthList);
				modelMap.put(ConstantForSuperAdmin.TOTAL, 1);
				modelMap.put("success", true);
			} else {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, new ArrayList<LocalAuth>());
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

	@RequestMapping(value = "/modifylocalauth", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyLocalAuth(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		String userName = HttpServletRequestUtil.getString(request, "userName");
		String password = HttpServletRequestUtil.getString(request, "password");
		String newPassword = HttpServletRequestUtil.getString(request, "newPassword");
		Long userId = HttpServletRequestUtil.getLong(request, "userId");
		if (userName == null) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "用户名为空");
			return modelMap;
		}
		if (password == null) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "旧密码为空");
			return modelMap;
		}
		if (newPassword == null) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "新密码为空");
			return modelMap;
		}
		if (userId == null) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "userId为空");
			return modelMap;
		}
		try {
			LocalAuthExecution lae = localAuthService.modifyLocalAuth(userId, userName, password, newPassword);
			if (lae.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", lae.getStateInfo());
			}
			return modelMap;
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
	}
}
