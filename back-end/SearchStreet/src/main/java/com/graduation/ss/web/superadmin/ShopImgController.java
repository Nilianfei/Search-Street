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
import com.graduation.ss.dto.ShopImgExecution;
import com.graduation.ss.entity.ShopImg;
import com.graduation.ss.service.ShopService;
import com.graduation.ss.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/superadmin")
public class ShopImgController {
	@Autowired
	private ShopService shopService;

	@RequestMapping(value = "/listshopimgs", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> listShopImgs(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ShopImgExecution sie = null;
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_NO);
		int pageSize = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_SIZE);
		// 空值判断
		if (pageIndex > 0 && pageSize > 0) {
			try {
				ShopImg shopImg = new ShopImg();
				sie = shopService.getShopImgList(shopImg, pageIndex, pageSize);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (sie.getShopImgList() != null) {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, sie.getShopImgList());
				modelMap.put(ConstantForSuperAdmin.TOTAL, sie.getCount());
				modelMap.put("success", true);
			} else {
				// 取出数据为空，也返回new list以使得前端不出错
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, new ArrayList<ShopImg>());
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

	@RequestMapping(value = "/searchshopimgsbyshopid", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> searchShopImgsbyShopId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ShopImgExecution sie = null;
		// 获取商铺ID
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_NO);
		int pageSize = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_SIZE);
		// 空值判断
		if (shopId != null) {
			try {
				ShopImg shopImg = new ShopImg();
				shopImg.setShopId(shopId);
				sie = shopService.getShopImgList(shopImg, pageIndex, pageSize);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (sie.getShopImgList() != null) {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, sie.getShopImgList());
				modelMap.put(ConstantForSuperAdmin.TOTAL, sie.getCount());
				modelMap.put("success", true);
			} else {
				// 取出数据为空，也返回new list以使得前端不出错
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, new ArrayList<ShopImg>());
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

	@RequestMapping(value = "/delshopimg", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> delShopImg(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long shopImgId = HttpServletRequestUtil.getLong(request, "shopImgId");
		try {
			shopService.delShopImg(shopImgId);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		modelMap.put("success", true);
		return modelMap;
	}

	@RequestMapping(value = "/addshopimg", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addShopImg(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取商铺ID
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");

		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("imgAddr");
		}
		if (shopImg == null) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "缺少商铺图片");
			return modelMap;
		}
		try {
			ImageHolder shopImgHolder = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
			shopService.addShopImg(shopId, shopImgHolder);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		modelMap.put("success", true);
		return modelMap;
	}
}
