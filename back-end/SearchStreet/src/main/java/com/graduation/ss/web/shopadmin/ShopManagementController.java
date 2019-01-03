package com.graduation.ss.web.shopadmin;

import java.io.IOException;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.graduation.ss.dao.ShopImgDao;
import com.graduation.ss.dto.ImageHolder;
import com.graduation.ss.dto.ShopExecution;
import com.graduation.ss.entity.Shop;
import com.graduation.ss.entity.ShopImg;
import com.graduation.ss.enums.ShopStateEnum;
import com.graduation.ss.exceptions.ShopOperationException;
import com.graduation.ss.service.ShopService;
import com.graduation.ss.util.HttpServletRequestUtil;


@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
	@Autowired
	private ShopService shopService;
	@Autowired
	private ShopImgDao shopImgDao;
	
	// 支持上传店铺详情图的最大数量
	private static final int IMAGEMAXCOUNT = 6;
	
	@RequestMapping(value = "/getshoplistbyuserid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopList(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long userId = (Long) request.getSession().getAttribute("userId");
		try {
			Shop shopCondition = new Shop();
			shopCondition.setUserId(userId);
			ShopExecution se = shopService.getShopList(shopCondition, 0, 100);
			modelMap.put("shopList", se.getShopList());
			// 列出店铺成功之后，将店铺放入session中作为权限验证依据，即该帐号只能操作它自己的店铺
			request.getSession().setAttribute("shopList", se.getShopList());
			modelMap.put("userId", userId);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/getshopbyid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopById(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if (shopId > 0) {
			try {
				//获取店铺信息
				Shop shop = shopService.getByShopId(shopId);
				//获取店铺详情图列表
				List<ShopImg> shopImgList = shopImgDao.getShopImgList(shopId);
				shop.setShopImgList(shopImgList);
				modelMap.put("shop", shop);
				modelMap.put("success", true);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/registershop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> registerShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 1.接收并转化相应的参数，包括店铺信息以及图片信息
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		List<ImageHolder> shopImgList = new ArrayList<ImageHolder>();
		ImageHolder businessLicenseImg = null;
		ImageHolder profileImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try{
			if (commonsMultipartResolver.isMultipart(request)) {
				handleImage(request, shopImgList, businessLicenseImg, profileImg);
			}
		}catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		// 2.注册店铺
		if (shop != null) {
			Long userId = (Long) request.getSession().getAttribute("userId");
			shop.setUserId(userId);
			ShopExecution se;
			try {
				se = shopService.addShop(shop, shopImgList, businessLicenseImg, profileImg);
				if (se.getState() == ShopStateEnum.CHECK.getState()) {
					modelMap.put("success", true);
					// 该用户可以操作的店铺列表
					@SuppressWarnings("unchecked")
					List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
					if (shopList == null || shopList.size() == 0) {
						shopList = new ArrayList<Shop>();
					}
					shopList.add(se.getShop());
					request.getSession().setAttribute("shopList", shopList);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (ShopOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺信息");
			return modelMap;
		}
	}
	
	private void handleImage(HttpServletRequest request, List<ImageHolder> shopImgList, ImageHolder businessLicenseImg,
			ImageHolder profileImg) throws IOException {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 取出详情图列表并构建List<ImageHolder>列表对象，最多支持六张图片上传
		for (int i = 0; i < IMAGEMAXCOUNT; i++) {
			CommonsMultipartFile shopImgFile = (CommonsMultipartFile) multipartRequest.getFile("shopImg" + i);
			if (shopImgFile != null) {
				// 若取出的第i个详情图片文件流不为空，则将其加入详情图列表
				ImageHolder shopImg = new ImageHolder(shopImgFile.getOriginalFilename(),
						shopImgFile.getInputStream());
				shopImgList.add(shopImg);
			} else {
				// 若取出的第i个详情图片文件流为空，则终止循环
				break;
			}
		}
		// 取出businessImg并构建ImageHolder对象
		CommonsMultipartFile businessImgFile = (CommonsMultipartFile) multipartRequest.getFile("businessLicenseImg");
		if (businessImgFile != null) {
			businessLicenseImg = new ImageHolder(businessImgFile.getOriginalFilename(), businessImgFile.getInputStream());
		}
		// 取出profileImg并构建ImageHolder对象
		CommonsMultipartFile profileImgFile = (CommonsMultipartFile) multipartRequest.getFile("profileImg");
		if (profileImgFile != null) {
			profileImg = new ImageHolder(profileImgFile.getOriginalFilename(), profileImgFile.getInputStream());
		}
	}

	@RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 1.接收并转化相应的参数，包括店铺信息以及图片信息
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		List<ImageHolder> shopImgList = new ArrayList<ImageHolder>();
		ImageHolder businessLicenseImg = null;
		ImageHolder profileImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try{
			if (commonsMultipartResolver.isMultipart(request)) {
				handleImage(request, shopImgList, businessLicenseImg, profileImg);
			}
		}catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		// 2.修改店铺信息
		if (shop != null && shop.getShopId() != null) {
			ShopExecution se;
			try {
				se = shopService.addShop(shop, shopImgList, businessLicenseImg, profileImg);
				if (se.getState() == ShopStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (ShopOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺Id");
			return modelMap;
		}
	}
}
