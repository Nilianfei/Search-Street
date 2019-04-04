package com.graduation.ss.web.superadmin;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.graduation.ss.dto.ConstantForSuperAdmin;
import com.graduation.ss.dto.ImageHolder;
import com.graduation.ss.dto.ShopExecution;
import com.graduation.ss.entity.Shop;
import com.graduation.ss.enums.ShopStateEnum;
import com.graduation.ss.service.ShopService;
import com.graduation.ss.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/superadmin")
public class ShopController {
	@Autowired
	private ShopService shopService;

	/**
	 * 获取店铺列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listshops", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> listShops(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ShopExecution se = null;
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_NO);
		int pageSize = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_SIZE);
		// 空值判断
		if (pageIndex > 0 && pageSize > 0) {
			Shop shopCondition = new Shop();
			int enableStatus = HttpServletRequestUtil.getInt(request, "enableStatus");
			if (enableStatus >= 0) {
				// 若传入可用状态，则将可用状态添加到查询条件里
				shopCondition.setEnableStatus(enableStatus);
			}
			String shopName = HttpServletRequestUtil.getString(request, "shopName");
			if (shopName != null) {
				try {
					// 若传入店铺名称，则将店铺名称解码后添加到查询条件里，进行模糊查询
					shopCondition.setShopName(URLDecoder.decode(shopName, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.toString());
				}
			}
			try {
				// 根据查询条件分页返回店铺列表
				se = shopService.getShopList(shopCondition, pageIndex, pageSize);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (se.getShopList() != null) {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, se.getShopList());
				modelMap.put(ConstantForSuperAdmin.TOTAL, se.getCount());
				modelMap.put("success", true);
			} else {
				// 取出数据为空，也返回new list以使得前端不出错
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, new ArrayList<Shop>());
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

	/**
	 * 根据id返回店铺信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/searchshopbyid", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> searchShopById(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Shop shop = null;
		// 从请求中获取店铺Id
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if (shopId != null) {
			try {
				// 根据Id获取店铺实例
				shop = shopService.getByShopId(shopId);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (shop != null) {
				List<Shop> shopList = new ArrayList<Shop>();
				shopList.add(shop);
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, shopList);
				modelMap.put(ConstantForSuperAdmin.TOTAL, 1);
				modelMap.put("success", true);
			} else {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, new ArrayList<Shop>());
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

	/**
	 * 修改店铺信息
	 * 
	 * @param shopStr
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		Long userId = HttpServletRequestUtil.getLong(request, "userId");
		String shopName = HttpServletRequestUtil.getString(request, "shopName");
		String businessLicenseCode = HttpServletRequestUtil.getString(request, "businessLicenseCode");
		int perCost = HttpServletRequestUtil.getInt(request, "perCost");
		String phone = HttpServletRequestUtil.getString(request, "phone");
		String province = HttpServletRequestUtil.getString(request, "province");
		String city = HttpServletRequestUtil.getString(request, "city");
		String district = HttpServletRequestUtil.getString(request, "district");
		String fullAddress = HttpServletRequestUtil.getString(request, "fullAddress");
		String shopMoreInfo = HttpServletRequestUtil.getString(request, "shopMoreInfo");
		int isMobile = HttpServletRequestUtil.getInt(request, "isMobile");
		Float latitude = HttpServletRequestUtil.getFloat(request, "latitude");
		Float longitude = HttpServletRequestUtil.getFloat(request, "longitude");
		int enableStatus = HttpServletRequestUtil.getInt(request, "enableStatus");
		String businessScope = HttpServletRequestUtil.getString(request, "businessScope");
		
		Shop shop = new Shop();
		shop.setShopId(shopId);
		shop.setUserId(userId);
		shop.setShopName(shopName);
		shop.setBusinessLicenseCode(businessLicenseCode);
		shop.setPerCost(perCost);
		shop.setPhone(phone);
		shop.setProvince(province);
		shop.setCity(city);
		shop.setDistrict(district);
		shop.setFullAddress(fullAddress);
		shop.setShopMoreInfo(shopMoreInfo);
		shop.setIsMobile(isMobile);
		shop.setLatitude(latitude);
		shop.setLongitude(longitude);
		shop.setEnableStatus(enableStatus);
		shop.setBusinessScope(businessScope);

		CommonsMultipartFile profileImg = null;
		CommonsMultipartFile businessLicenseImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			profileImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("profileImg");
			businessLicenseImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("businessLicenseImg");
		}
		// 空值判断
		if (shop.getShopId() != null) {
			try {
				ImageHolder profileImgHolder = null;
				if (profileImg != null) {
					profileImgHolder = new ImageHolder(profileImg.getOriginalFilename(), profileImg.getInputStream());
				}
				ImageHolder businessLicenseImgHolder = null;
				if (businessLicenseImg != null) {
					businessLicenseImgHolder = new ImageHolder(businessLicenseImg.getOriginalFilename(),
							businessLicenseImg.getInputStream());
				}
				ShopExecution se = shopService.modifyShop(shop, businessLicenseImgHolder, profileImgHolder);
				if (se.getState() == ShopStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺信息");
		}
		return modelMap;
	}

	/**
	 * 添加店铺信息
	 * 
	 * @param shopStr
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addshop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long userId = HttpServletRequestUtil.getLong(request, "userId");
		String shopName = HttpServletRequestUtil.getString(request, "shopName");
		String businessLicenseCode = HttpServletRequestUtil.getString(request, "businessLicenseCode");
		int perCost = HttpServletRequestUtil.getInt(request, "perCost");
		String phone = HttpServletRequestUtil.getString(request, "phone");
		String province = HttpServletRequestUtil.getString(request, "province");
		String city = HttpServletRequestUtil.getString(request, "city");
		String district = HttpServletRequestUtil.getString(request, "district");
		String fullAddress = HttpServletRequestUtil.getString(request, "fullAddress");
		String shopMoreInfo = HttpServletRequestUtil.getString(request, "shopMoreInfo");
		int isMobile = HttpServletRequestUtil.getInt(request, "isMobile");
		Float latitude = HttpServletRequestUtil.getFloat(request, "latitude");
		Float longitude = HttpServletRequestUtil.getFloat(request, "longitude");
		int enableStatus = HttpServletRequestUtil.getInt(request, "enableStatus");
		String businessScope = HttpServletRequestUtil.getString(request, "businessScope");
		
		Shop shop = new Shop();
		shop.setUserId(userId);
		shop.setShopName(shopName);
		shop.setBusinessLicenseCode(businessLicenseCode);
		shop.setPerCost(perCost);
		shop.setPhone(phone);
		shop.setProvince(province);
		shop.setCity(city);
		shop.setDistrict(district);
		shop.setFullAddress(fullAddress);
		shop.setShopMoreInfo(shopMoreInfo);
		shop.setIsMobile(isMobile);
		shop.setLatitude(latitude);
		shop.setLongitude(longitude);
		shop.setEnableStatus(enableStatus);
		shop.setBusinessScope(businessScope);

		CommonsMultipartFile profileImg = null;
		CommonsMultipartFile businessLicenseImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			profileImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("profileImg");
			businessLicenseImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("businessLicenseImg");
		}
		// 空值判断

		try {
			ImageHolder profileImgHolder = null;
			if (profileImg != null) {
				profileImgHolder = new ImageHolder(profileImg.getOriginalFilename(), profileImg.getInputStream());
			}
			ImageHolder businessLicenseImgHolder = null;
			if (businessLicenseImg != null) {
				businessLicenseImgHolder = new ImageHolder(businessLicenseImg.getOriginalFilename(),
						businessLicenseImg.getInputStream());
			}
			ShopExecution se = shopService.addShop(shop, businessLicenseImgHolder, profileImgHolder);
			if (se.getState() == ShopStateEnum.CHECK.getState()) {
				modelMap.put("success", true);
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", se.getStateInfo());
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		return modelMap;
	}
}
