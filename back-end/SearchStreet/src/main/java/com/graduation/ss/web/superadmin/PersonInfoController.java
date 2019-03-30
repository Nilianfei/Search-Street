package com.graduation.ss.web.superadmin;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.graduation.ss.dto.ConstantForSuperAdmin;
import com.graduation.ss.dto.ImageHolder;
import com.graduation.ss.dto.PersonInfoExecution;
import com.graduation.ss.entity.PersonInfo;
import com.graduation.ss.enums.PersonInfoStateEnum;
import com.graduation.ss.service.PersonInfoService;
import com.graduation.ss.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/superadmin")
public class PersonInfoController {
	@Autowired
	private PersonInfoService personInfoService;

	/**
	 * 列出用户信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listpersonInfos", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> listPersonInfos(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		PersonInfoExecution pie = null;
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_NO);
		int pageSize = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_SIZE);
		if (pageIndex > 0 && pageSize > 0) {
			try {
				PersonInfo personInfo = new PersonInfo();
				int enableStatus = HttpServletRequestUtil.getInt(request, "enableStatus");
				if (enableStatus > -1) {
					// 若查询条件中有按照可用状态来查询，则将其作为查询条件传入
					personInfo.setEnableStatus(enableStatus);
				}
				String name = HttpServletRequestUtil.getString(request, "name");
				if (name != null) {
					// 若查询条件中有按照名字来查询，则将其作为查询条件传入，并decode
					personInfo.setUserName(URLDecoder.decode(name, "UTF-8"));
				}
				pie = personInfoService.getPersonInfoList(personInfo, pageIndex, pageSize);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (pie.getPersonInfoList() != null) {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, pie.getPersonInfoList());
				modelMap.put(ConstantForSuperAdmin.TOTAL, pie.getCount());
				modelMap.put("success", true);
			} else {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, new ArrayList<PersonInfo>());
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
	
	@RequestMapping(value = "/addpersoninfo", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addPersonInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 从前端请求中获取用户信息
		int enableStatus = HttpServletRequestUtil.getInt(request, "enableStatus");
		String phone = HttpServletRequestUtil.getString(request, "phone");
		String userName = HttpServletRequestUtil.getString(request, "userName");
		String email = HttpServletRequestUtil.getString(request, "email");
		String sex = HttpServletRequestUtil.getString(request, "sex");
		Date birth = HttpServletRequestUtil.getDate(request, "birth");
		Long souCoin = HttpServletRequestUtil.getLong(request, "souCoin");
		int userType = HttpServletRequestUtil.getInt(request, "userType");
		Date createTime = HttpServletRequestUtil.getDate(request, "createTime");
		Date lastEditTime = HttpServletRequestUtil.getDate(request, "lastEditTime");
		PersonInfo pi = new PersonInfo();
		pi.setUserName(userName);
		pi.setEmail(email);
		pi.setSex(sex);
		pi.setBirth(birth);
		pi.setPhone(phone);
		pi.setSouCoin(souCoin);
		pi.setUserType(userType);
		pi.setEnableStatus(enableStatus);
		pi.setCreateTime(createTime);
		pi.setLastEditTime(lastEditTime);
		
		CommonsMultipartFile profileImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			profileImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("profileImg");
		}
		// 非空判断
		if (enableStatus >= 0 && userType >= 0) {
			try {
				PersonInfoExecution ae;
				if (profileImg == null) {
					ae = personInfoService.addPersonInfo(pi, null);
				} else {
					ImageHolder imageHolder = new ImageHolder(profileImg.getOriginalFilename(), profileImg.getInputStream());
					ae = personInfoService.addPersonInfo(pi, imageHolder);
				}
				if (ae.getState() == PersonInfoStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ae.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入需要添加的帐号信息");
		}
		return modelMap;
	}

	/**
	 * 修改用户信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifypersonInfo", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyPersonInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 从前端请求中获取用户信息
		Long userId = HttpServletRequestUtil.getLong(request, "userId");
		int enableStatus = HttpServletRequestUtil.getInt(request, "enableStatus");
		String phone = HttpServletRequestUtil.getString(request, "phone");
		String userName = HttpServletRequestUtil.getString(request, "userName");
		String email = HttpServletRequestUtil.getString(request, "email");
		String sex = HttpServletRequestUtil.getString(request, "sex");
		Date birth = HttpServletRequestUtil.getDate(request, "birth");
		Long souCoin = HttpServletRequestUtil.getLong(request, "souCoin");
		int userType = HttpServletRequestUtil.getInt(request, "userType");
		Date createTime = HttpServletRequestUtil.getDate(request, "createTime");
		Date lastEditTime = HttpServletRequestUtil.getDate(request, "lastEditTime");
		PersonInfo pi = new PersonInfo();
		pi.setUserId(userId);
		pi.setUserName(userName);
		pi.setEmail(email);
		pi.setSex(sex);
		pi.setBirth(birth);
		pi.setPhone(phone);
		pi.setSouCoin(souCoin);
		pi.setUserType(userType);
		pi.setEnableStatus(enableStatus);
		pi.setCreateTime(createTime);
		pi.setLastEditTime(lastEditTime);
		
		CommonsMultipartFile profileImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			profileImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("profileImg");
		}

		// 非空判断
		if (userId != null) {
			try {
				PersonInfoExecution ae;
				if (profileImg == null) {
					ae = personInfoService.modifyPersonInfo(pi, null);
				} else {
					ImageHolder imageHolder = new ImageHolder(profileImg.getOriginalFilename(), profileImg.getInputStream());
					ae = personInfoService.modifyPersonInfo(pi, imageHolder);
				}
				if (ae.getState() == PersonInfoStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ae.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入需要修改的帐号信息");
		}
		return modelMap;
	}

}