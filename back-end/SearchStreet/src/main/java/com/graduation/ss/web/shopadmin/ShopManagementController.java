package com.graduation.ss.web.shopadmin;

import java.io.IOException;
import java.util.Date;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/shopadmin")
@Api(value = "ShopManagementController|对商铺操作的控制器")
public class ShopManagementController {
	@Autowired
	private ShopService shopService;
	@Autowired
	private ShopImgDao shopImgDao;
	@Autowired
	private WechatAuthService wechatAuthService;

	@RequestMapping(value = "/getshoplistbyuserid", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据用户ID获取其所有商铺信息（分页）")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "token", value = "包含用户信息的token", required = true, dataType = "String"),
			@ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true, dataType = "int"),
			@ApiImplicitParam(paramType = "query", name = "pageSize", value = "一页的商铺数目", required = true, dataType = "int") })
	private Map<String, Object> getShopListByUserId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		String token = request.getHeader("token");
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
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
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		try {
			Shop shopCondition = new Shop();
			shopCondition.setUserId(userId);
			ShopExecution se = shopService.getShopList(shopCondition, pageIndex, pageSize);
			int pageNum = (int) (se.getCount() / pageSize);
			if (pageNum * pageSize < se.getCount())
				pageNum++;
			modelMap.put("shopList", se.getShopList());
			modelMap.put("pageNum", pageNum);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		return modelMap;
	}

	@RequestMapping(value = "/getshopbyid", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据商铺ID获取商铺信息")
	@ApiImplicitParam(paramType = "query", name = "shopId", value = "商铺ID", required = true, dataType = "Long", example = "65")
	private Map<String, Object> getShopByShopId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if (shopId != null) {
			try {
				// 获取商铺信息
				Shop shop = shopService.getByShopId(shopId);
				// 获取商铺详情图列表
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
	@ApiOperation(value = "注册商铺（不添加图片）", notes = "不用传shopId")
	@ApiImplicitParam(paramType = "header", name = "token", value = "包含用户信息的token", required = true, dataType = "String")
	private Map<String, Object> registerShop(
			@RequestBody @ApiParam(name = "shop", value = "传入json格式,不用传shopId", required = true) Shop shop,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		String token = request.getHeader("token");
		// 注册商铺
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
			modelMap.put("errMsg", e.toString());
			return modelMap;
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
			modelMap.put("errMsg", e.toString());
		}
		return modelMap;
	}

	@RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "修改商铺信息（不修改图片）", notes = "要传shopId")
	@ApiImplicitParam(paramType = "header", name = "token", value = "包含用户信息的token", required = true, dataType = "String")
	private Map<String, Object> modifyShop(
			@RequestBody @ApiParam(name = "shop", value = "传入json格式,要传shopId", required = true) Shop shop,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		String token = request.getHeader("token");

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
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if (shop.getShopId() != null) {
			Shop tempShop = shopService.getByShopId(shop.getShopId());
			if (tempShop.getUserId() != userId) {
				modelMap.put("success", false);
				modelMap.put("errMsg", "该商铺不属于该用户");
			}
		}

		ShopExecution se;
		try {
			se = shopService.modifyShop(shop);
			if (se.getState() == ShopStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
				modelMap.put("shopId", se.getShop().getShopId());
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", se.getStateInfo());
			}
		} catch (ShopOperationException e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		return modelMap;
	}

	private void handleImage(HttpServletRequest request, ImageHolder shopImg, ImageHolder businessLicenseImg,
			ImageHolder profileImg) throws IOException {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
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

	@RequestMapping(value = "/uploadimg", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "上传商铺相关图片", notes = "注册和修改信息都适用,在swagger这个网站上看不了效果,请查看前端已使用过的页面")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "token", value = "包含用户信息的token", required = true, dataType = "String"),
			@ApiImplicitParam(paramType = "query", name = "shopId", value = "商铺id", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "createTime", value = "图片创建时间", required = true, dataType = "String") })
	private Map<String, Object> uploadImg(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 1.接收并转化相应的参数，包括商铺id以及图片信息
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		Date createTime = HttpServletRequestUtil.getDate(request, "createTime");
		String token = request.getHeader("token");

		ImageHolder shopImg = new ImageHolder("", null);
		ImageHolder businessLicenseImg = new ImageHolder("", null);
		ImageHolder profileImg = new ImageHolder("", null);

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
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if (shopId != null) {
			Shop tempShop = shopService.getByShopId(shopId);
			if (tempShop.getUserId() != userId) {
				modelMap.put("success", false);
				modelMap.put("errMsg", "该商铺不属于该用户");
			}
		}

		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try {
			if (commonsMultipartResolver.isMultipart(request)) {
				handleImage(request, shopImg, businessLicenseImg, profileImg);
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			System.out.println(e.toString());
			return modelMap;
		}
		// 2.上传商铺图片
		ShopExecution se;
		try {
			se = shopService.uploadImg(shopId, shopImg, businessLicenseImg, profileImg, createTime);
			if (se.getState() == ShopStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", se.getStateInfo());
				System.out.println(se.getStateInfo());
			}
		} catch (ShopOperationException e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		return modelMap;
	}

	@RequestMapping(value = "/searchnearbyshops", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "返回2.5km内的所有商铺")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "longitude", value = "屏幕中心的经度", required = true, dataType = "Float"),
			@ApiImplicitParam(paramType = "query", name = "latitude", value = "屏幕中心的纬度", required = true, dataType = "Float"),
			@ApiImplicitParam(paramType = "query", name = "shopName", value = "商铺名", required = false, dataType = "String") })
	private Map<String, Object> searchNearbyShops(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		float longitude = HttpServletRequestUtil.getFloat(request, "longitude");
		float latitude = HttpServletRequestUtil.getFloat(request, "latitude");
		String shopName = HttpServletRequestUtil.getString(request, "shopName");
		float minlat = 0f;// 定义经纬度四个极限值
		float maxlat = 0f;
		float minlng = 0f;
		float maxlng = 0f;

		// 先计算查询点的经纬度范围
		float r = 6371;// 地球半径千米
		float dis = 2.5f;// 查询范围2.5km内的所有商铺
		float dlng = (float) (2 * Math.asin(Math.sin(dis / (2 * r)) / Math.cos(latitude * Math.PI / 180)));
		dlng = (float) (dlng * 180 / Math.PI);
		float dlat = dis / r;
		dlat = (float) (dlat * 180 / Math.PI);
		if (dlng < 0) {
			minlng = longitude + dlng;
			maxlng = longitude - dlng;
		} else {
			minlng = longitude - dlng;
			maxlng = longitude + dlng;
		}
		minlat = latitude - dlat;
		maxlat = latitude + dlat;
		try {
			ShopExecution se = shopService.getNearbyShopList(maxlat, minlat, maxlng, minlng, shopName);
			modelMap.put("shopList", se.getShopList());
			modelMap.put("minlng", minlng);
			modelMap.put("maxlng", maxlng);
			modelMap.put("minlat", minlat);
			modelMap.put("maxlat", maxlat);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		return modelMap;
	}
}
