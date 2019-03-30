package com.graduation.ss.web.superadmin;

import java.util.ArrayList;
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
import com.graduation.ss.dto.AppealImgExecution;
import com.graduation.ss.entity.AppealImg;
import com.graduation.ss.service.AppealService;
import com.graduation.ss.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/superadmin")
public class AppealImgController {
	@Autowired
	private AppealService appealService;

	@RequestMapping(value = "/listappealimgs", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> listAppealImgs(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		AppealImgExecution aie = null;
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_NO);
		int pageSize = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_SIZE);
		// 空值判断
		if (pageIndex > 0 && pageSize > 0) {
			try {
				AppealImg appealImg = new AppealImg();
				aie = appealService.getAppealImgList(appealImg, pageIndex, pageSize);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (aie.getAppealImgList() != null) {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, aie.getAppealImgList());
				modelMap.put(ConstantForSuperAdmin.TOTAL, aie.getCount());
				modelMap.put("success", true);
			} else {
				// 取出数据为空，也返回new list以使得前端不出错
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, new ArrayList<AppealImg>());
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

	@RequestMapping(value = "/searchappealimgsbyappealid", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> searchAppealImgsbyAppealId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		AppealImgExecution aie = null;
		// 获取求助ID
		Long appealId = HttpServletRequestUtil.getLong(request, "appealId");
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_NO);
		int pageSize = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_SIZE);
		// 空值判断
		if (appealId != null) {
			try {
				AppealImg appealImg = new AppealImg();
				appealImg.setAppealId(appealId);
				aie = appealService.getAppealImgList(appealImg, pageIndex, pageSize);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (aie.getAppealImgList() != null) {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, aie.getAppealImgList());
				modelMap.put(ConstantForSuperAdmin.TOTAL, aie.getCount());
				modelMap.put("success", true);
			} else {
				// 取出数据为空，也返回new list以使得前端不出错
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, new ArrayList<AppealImg>());
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

	@RequestMapping(value = "/delappealimg", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> delAppealImg(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long appealImgId = HttpServletRequestUtil.getLong(request, "appealImgId");
		try {
			appealService.delAppealImg(appealImgId);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		modelMap.put("success", true);
		return modelMap;
	}

	@RequestMapping(value = "/addappealimg", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addAppealImg(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取求助ID
		Long appealId = HttpServletRequestUtil.getLong(request, "appealId");

		CommonsMultipartFile appealImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			appealImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("imgAddr");
		}
		if (appealImg == null) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "缺少求助图片");
			return modelMap;
		}
		try {
			ImageHolder appealImgHolder = new ImageHolder(appealImg.getOriginalFilename(), appealImg.getInputStream());
			appealService.createAppealImg(appealId, appealImgHolder);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		modelMap.put("success", true);
		return modelMap;
	}
}
