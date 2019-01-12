package com.graduation.ss.web.shopadmin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.graduation.ss.dao.ShopImgDao;
import com.graduation.ss.dto.ImageHolder;
import com.graduation.ss.dto.ShopExecution;
import com.graduation.ss.dto.UserCode2Session;
import com.graduation.ss.entity.Shop;
import com.graduation.ss.entity.ShopImg;
import com.graduation.ss.entity.WechatAuth;
import com.graduation.ss.enums.ShopStateEnum;
import com.graduation.ss.exceptions.ShopOperationException;
import com.graduation.ss.service.ShopService;
import com.graduation.ss.service.WechatAuthService;
import com.graduation.ss.util.HttpServletRequestUtil;
import com.graduation.ss.util.JWT;


@RestController
@RequestMapping("/shopadmin")
public class ShopManagementController {
	@Autowired
	private ShopService shopService;
	@Autowired
	private ShopImgDao shopImgDao;
	@Autowired
	private WechatAuthService wechatAuthService;
	
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
	private Map<String, Object> registerShop(@RequestBody Shop shop, String token) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 注册店铺
		Long userId = null;
		UserCode2Session userCode2Session = null;
		//将token解密成openId 和session_key
		userCode2Session = JWT.unsign(token, UserCode2Session.class);
		//获取个人信息
		String openId = userCode2Session.getOpenId();
		try{
			WechatAuth wechatAuth = wechatAuthService.getWechatAuthByOpenId(openId);
			userId = wechatAuth.getUserId();
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		shop.setUserId(userId);
		ShopExecution se;
		try {
			se = shopService.addShop(shop);
			if (se.getState() == ShopStateEnum.CHECK.getState()) {
				modelMap.put("success", true);
				modelMap.put("shopId", se.getShop().getShopId());
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", se.getStateInfo());
			}
		} catch (ShopOperationException e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
	
	private void handleImage(HttpServletRequest request, ImageHolder shopImg, ImageHolder businessLicenseImg,
			ImageHolder profileImg) throws IOException {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 取出详情图列表并构建List<ImageHolder>列表对象，最多支持三张图片上传
		CommonsMultipartFile shopImgFile = (CommonsMultipartFile) multipartRequest.getFile("shopImg");
		if (shopImgFile != null && shopImg != null) {
			shopImg.setImage(shopImgFile.getInputStream());
			shopImg.setImageName(shopImgFile.getOriginalFilename());
		}
		// 取出businessImg并构建ImageHolder对象
		CommonsMultipartFile businessImgFile = (CommonsMultipartFile) multipartRequest.getFile("businessLicenseImg");
		if (businessImgFile != null && businessLicenseImg != null) {
			businessLicenseImg.setImage(businessImgFile.getInputStream());
			businessLicenseImg.setImageName(businessImgFile.getOriginalFilename());
		}
		// 取出profileImg并构建ImageHolder对象
		CommonsMultipartFile profileImgFile = (CommonsMultipartFile) multipartRequest.getFile("profileImg");
		if (profileImgFile != null && profileImg != null) {
			profileImg.setImage(profileImgFile.getInputStream());
			profileImg.setImageName(profileImgFile.getOriginalFilename());
		}
	}

	@RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 1.接收并转化相应的参数，包括店铺信息以及图片信息
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		ImageHolder shopImg = new ImageHolder("", null);
		ImageHolder businessLicenseImg = new ImageHolder("", null);
		ImageHolder profileImg = new ImageHolder("", null);
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try{
			if (commonsMultipartResolver.isMultipart(request)) {
				handleImage(request, shopImg, businessLicenseImg, profileImg);
			}
		}catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			System.out.println(e.getMessage());
			return modelMap;
		}
		// 2.修改店铺信息
		ShopExecution se;
		try {
			Shop shop = shopService.getByShopId(shopId);
			se = shopService.modifyShop(shop, shopImg, businessLicenseImg, profileImg);
			if (se.getState() == ShopStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", se.getStateInfo());
				System.out.println(se.getStateInfo());
			}
		} catch (ShopOperationException e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			System.out.println(e.getMessage());
		}
		return modelMap;
	}
}
