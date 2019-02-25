package com.graduation.ss.web.service;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.graduation.ss.dto.ConstantForSuperAdmin;
import com.graduation.ss.dto.ImageHolder;
import com.graduation.ss.dto.ShopCommentExecution;
import com.graduation.ss.dto.UserCode2Session;
import com.graduation.ss.entity.ShopComment;
import com.graduation.ss.entity.WechatAuth;
import com.graduation.ss.enums.ShopCommentStateEnum;
import com.graduation.ss.exceptions.ShopCommentOperationException;
import com.graduation.ss.exceptions.ShopOperationException;
import com.graduation.ss.service.ShopCommentService;
import com.graduation.ss.service.WechatAuthService;
import com.graduation.ss.util.HttpServletRequestUtil;
import com.graduation.ss.util.JWT;

@RestController
@RequestMapping("/shopComment")
public class ShopCommentController {
	@Autowired
	private ShopCommentService shopCommentService;
	@Autowired
	private WechatAuthService wechatAuthService;
	//通过店铺id获取评论列表 分页 
	@RequestMapping(value = "/getshopCommentlistbyshopid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopCommentListByShopId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		try {
			ShopCommentExecution se = shopCommentService.getByShopId(shopId, pageIndex, pageSize);
			int pageNum = (int) (se.getCount() / pageSize);
			if (pageNum * pageSize < se.getCount())
				pageNum++;
			modelMap.put("shopCommentList", se.getShopCommentList());
			modelMap.put("pageNum", pageNum);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
	//通过userId获取评论列表 分页 
		@RequestMapping(value = "/getshopCommentlistbyuserid", method = RequestMethod.GET)
		@ResponseBody
		private Map<String, Object> getShopCommentListByUserId(HttpServletRequest request) {
			Map<String, Object> modelMap = new HashMap<String, Object>();
			Long userId = HttpServletRequestUtil.getLong(request, "userId");
			int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
			int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
			try {
				ShopCommentExecution se = shopCommentService.getByUserId(userId, pageIndex, pageSize);
				int pageNum = (int) (se.getCount() / pageSize);
				if (pageNum * pageSize < se.getCount())
					pageNum++;
				modelMap.put("shopCommentList", se.getShopCommentList());
				modelMap.put("pageNum", pageNum);
				modelMap.put("success", true);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			return modelMap;
		}
	//根据查询条件获取评论列表 分页
	@RequestMapping(value = "/listShopComment", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listShopComment(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ShopCommentExecution se = null;
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_NO);
		int pageSize = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_SIZE);
		// 空值判断
		if (pageIndex > 0 && pageSize > 0) {
			ShopComment shopCommentCondition = new ShopComment();
			long shopId=HttpServletRequestUtil.getLong(request, "shopId");
			if(shopId>0)
			   shopCommentCondition.setShopId(shopId);
			long userId=HttpServletRequestUtil.getLong(request, "userId");
			if(userId>0)
			    shopCommentCondition.setUserId(userId);
			String shopCommentContent=HttpServletRequestUtil.getString(request, "commentContent");
			if(shopCommentContent!=null)
				 shopCommentCondition.setCommentContent(shopCommentContent);
			try {
				// 根据查询条件分页返回评论列表
				se = shopCommentService.getShopCommentList(shopCommentCondition, pageIndex, pageSize);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (se.getShopCommentList() != null) {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, se.getShopCommentList());
				modelMap.put(ConstantForSuperAdmin.TOTAL, se.getCount());
				modelMap.put("success", true);
			} else {
				// 取出数据为空，也返回new list以使得前端不出错
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, new ArrayList<ShopComment>());
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
	 * 根据id返回评论信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/searchshopCommentbyid", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> searchShopCommentById(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ShopComment shopComment = null;
		// 从请求中获取shopCommentId
		long shopCommentId = HttpServletRequestUtil.getLong(request, "shopCommentId");
		if (shopCommentId > 0) {
			try {
				// 根据Id获取评论实例
				shopComment = shopCommentService.getByShopCommentId(shopCommentId);
				
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (shopComment != null) {
				List<ShopComment> shopCommentList = new ArrayList<ShopComment>();
				shopCommentList.add(shopComment);
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, shopCommentList);
				modelMap.put(ConstantForSuperAdmin.TOTAL, 1);
				modelMap.put("success", true);
			} else {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, new ArrayList<ShopComment>());
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
	//添加评论
	@RequestMapping(value = "/addshopComment", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addShopComment(String shopCommentStr, HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		ShopComment shopComment = null;
		try {
			// 获取前端传递过来的shopComment json字符串，将其转换成shopComment实例
			shopComment = mapper.readValue(shopCommentStr, ShopComment.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		// 空值判断
		if (shopComment != null && shopComment.getShopCommentId() != null) {
			try {
				//添加评论
				ShopCommentExecution ae = shopCommentService.addShopComment(shopComment);
				if (ae.getState() == ShopCommentStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ae.getStateInfo());
				}
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入评论信息");
		}
		return modelMap;
	}
	//更新评论
		@RequestMapping(value = "/modifyshopComment", method = RequestMethod.POST)
		@ResponseBody
		private Map<String, Object> modifyShopComment(String shopCommentStr, HttpServletRequest request) {
			Map<String, Object> modelMap = new HashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();
			ShopComment shopComment = null;
			try {
				// 获取前端传递过来的shopComment json字符串，将其转换成shopComment实例
				shopComment = mapper.readValue(shopCommentStr, ShopComment.class);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			// 空值判断
			if (shopComment != null && shopComment.getShopCommentId() != null) {
				try {
					//更新评论
					ShopCommentExecution ae = shopCommentService.modifyShopComment(shopComment);
					if (ae.getState() == ShopCommentStateEnum.SUCCESS.getState()) {
						modelMap.put("success", true);
					} else {
						modelMap.put("success", false);
						modelMap.put("errMsg", ae.getStateInfo());
					}
				} catch (Exception e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.toString());
					return modelMap;
				}

			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "请输入评论信息");
			}
			return modelMap;
		}




}
