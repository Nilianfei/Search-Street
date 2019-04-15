package com.graduation.ss.web.superadmin;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.graduation.ss.dto.ConstantForSuperAdmin;
import com.graduation.ss.dto.OrderExecution;
import com.graduation.ss.dto.ShopCommentExecution;
import com.graduation.ss.entity.OrderInfo;
import com.graduation.ss.entity.ShopComment;
import com.graduation.ss.enums.OrderStateEnum;
import com.graduation.ss.enums.ShopCommentStateEnum;
import com.graduation.ss.service.OrderService;
import com.graduation.ss.service.ShopCommentService;
import com.graduation.ss.util.HttpServletRequestUtil;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/superadmin")
public class ShopCommentSuperController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private ShopCommentService shopCommentService;
	//获取其所有服务评论信息（分页）
	@RequestMapping(value = "/listShopComment", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> listShopComment(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ShopCommentExecution se = null;
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_NO);
		int pageSize = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_SIZE);
		System.out.println(pageIndex+" "+pageSize);
		// 空值判断
		if (pageIndex > 0 && pageSize > 0) {
			ShopComment shopCommentCondition = new ShopComment();
			/*
			long shopId=HttpServletRequestUtil.getLong(request, "shopId");
			if(shopId>0)
			   shopCommentCondition.setShopId(shopId);
			long orderId=HttpServletRequestUtil.getLong(request, "orderId");
			if(orderId>0)
			    shopCommentCondition.setOrderId(orderId);
			long userId=HttpServletRequestUtil.getLong(request, "userId");
			if(userId>0)
			    shopCommentCondition.setUserId(userId);
			String commentReply=HttpServletRequestUtil.getString(request, "commentReply");
			if(commentReply!=null)
		    {
				try {
					// 若传入评论内容，则将评论内容解码后添加到查询条件里，进行模糊查询
					shopCommentCondition.setCommentReply(URLDecoder.decode(commentReply, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.toString());
				}
			}
			String shopCommentContent=HttpServletRequestUtil.getString(request, "commentContent");
			if(shopCommentContent!=null)
		    {
				try {
					// 若传入评论内容，则将评论内容解码后添加到查询条件里，进行模糊查询
					shopCommentCondition.setCommentContent(URLDecoder.decode(shopCommentContent, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.toString());
				}
			}
			*/
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
	//根据shopID获取其所有服务评论信息（分页）
	@RequestMapping(value = "/listShopCommentByShopId", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> listShopCommentByShopId(HttpServletRequest request) {
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
	//根据orderId获取其所有服务评论信息（分页）
	@RequestMapping(value = "/listShopCommentByOrderId", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> listShopCommentByOrderId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ShopCommentExecution se = null;
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_NO);
		int pageSize = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_SIZE);
		// 空值判断
		if (pageIndex > 0 && pageSize > 0) {
			ShopComment shopCommentCondition = new ShopComment();
			long orderId=HttpServletRequestUtil.getLong(request, "orderId");
			if(orderId>0)
			    shopCommentCondition.setOrderId(orderId);
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
	//根据userId获取其所有服务评论信息（分页）
	@RequestMapping(value = "/listShopCommentByUserId", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> listShopCommentByUserId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ShopCommentExecution se = null;
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_NO);
		int pageSize = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_SIZE);
		// 空值判断
		if (pageIndex > 0 && pageSize > 0) {
			ShopComment shopCommentCondition = new ShopComment();
			long userId=HttpServletRequestUtil.getLong(request, "userId");
			if(userId>0)
			    shopCommentCondition.setUserId(userId);
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
	//根据commentContent获取其所有服务评论信息（分页）
		@RequestMapping(value = "/listShopCommentByCommentContent", method = RequestMethod.POST)
		@ResponseBody
		private Map<String, Object> listShopCommentByCommentContent(HttpServletRequest request) {
			Map<String, Object> modelMap = new HashMap<String, Object>();
			ShopCommentExecution se = null;
			// 获取分页信息
			int pageIndex = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_NO);
			int pageSize = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_SIZE);
			// 空值判断
			if (pageIndex > 0 && pageSize > 0) {
				ShopComment shopCommentCondition = new ShopComment();
				String shopCommentContent=HttpServletRequestUtil.getString(request, "commentContent");
				if(shopCommentContent!=null)
			    {
					try {
						// 若传入评论内容，则将评论内容解码后添加到查询条件里，进行模糊查询
						shopCommentCondition.setCommentContent(URLDecoder.decode(shopCommentContent, "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						modelMap.put("success", false);
						modelMap.put("errMsg", e.toString());
					}
				}
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

		//根据commentReply获取其所有服务评论信息（分页）
		@RequestMapping(value = "/listShopCommentByCommentReply", method = RequestMethod.POST)
		@ResponseBody
		private Map<String, Object> listShopCommentByCommentReply(HttpServletRequest request) {
			Map<String, Object> modelMap = new HashMap<String, Object>();
			ShopCommentExecution se = null;
			// 获取分页信息
			int pageIndex = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_NO);
			int pageSize = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_SIZE);
			// 空值判断
			if (pageIndex > 0 && pageSize > 0) {
				ShopComment shopCommentCondition = new ShopComment();
				String commentReply=HttpServletRequestUtil.getString(request, "commentReply");
				if(commentReply!=null)
			    {
					try {
						// 若传入评论内容，则将评论内容解码后添加到查询条件里，进行模糊查询
						shopCommentCondition.setCommentReply(URLDecoder.decode(commentReply, "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						modelMap.put("success", false);
						modelMap.put("errMsg", e.toString());
					}
				}
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
	 * 添加评论信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addShopComment", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addShopComment(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long shopId=HttpServletRequestUtil.getLong(request, "shopId");
		long orderId=HttpServletRequestUtil.getLong(request, "orderId");
		long userId=HttpServletRequestUtil.getLong(request, "userId");
		Integer serviceRating=HttpServletRequestUtil.getInt(request,"serviceRating");
		Integer starRating=HttpServletRequestUtil.getInt(request,"starRating");
		String commentContent=HttpServletRequestUtil.getString(request, "commentContent");
		String commentReply=HttpServletRequestUtil.getString(request, "commentReply");
		ShopComment shopComment = new ShopComment();
		shopComment.setShopId(shopId);
		shopComment.setOrderId(orderId);
		shopComment.setUserId(userId);
		shopComment.setServiceRating(serviceRating);
		shopComment.setStarRating(starRating);
		shopComment.setCommentContent(commentContent);
		shopComment.setCommentReply(commentReply);
		
		// 空值判断
		try {
			OrderInfo order=orderService.getByOrderId(shopComment.getOrderId());
			if(order.getOrderStatus()==1)
			{
				//添加评论		
				ShopCommentExecution ae = shopCommentService.addShopComment(shopComment);
				order.setOrderStatus(2);
				//更新订单
				OrderExecution a = orderService.modifyOrder(order);
				if (a.getState() == OrderStateEnum.SUCCESS.getState()) {
					
				} 
				else {
					modelMap.put("success", false);
					modelMap.put("errMsg", a.getStateInfo());
				}
				if (ae.getState() == ShopCommentStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ae.getStateInfo());
				}
			}
			else
			{
				modelMap.put("success", false);
				modelMap.put("errMsg","添加评论失败，订单状态不为待评价");
			}
	
			
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		return modelMap;
	}
	  //更新评论
	@RequestMapping(value = "/modifyShopComment", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "修改服务评价信息")
	private Map<String, Object> modifyShopComment(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long shopCommentId=HttpServletRequestUtil.getLong(request, "shopCommentId");
		long shopId=HttpServletRequestUtil.getLong(request, "shopId");
		long orderId=HttpServletRequestUtil.getLong(request, "orderId");
		long userId=HttpServletRequestUtil.getLong(request, "userId");
		Integer serviceRating=HttpServletRequestUtil.getInt(request,"serviceRating");
		Integer starRating=HttpServletRequestUtil.getInt(request,"starRating");
		String commentContent=HttpServletRequestUtil.getString(request, "commentContent");
		String commentReply=HttpServletRequestUtil.getString(request, "commentReply");
		System.out.println(shopCommentId+" "+commentContent);
		ShopComment shopComment = new ShopComment();
		shopComment.setShopCommentId(shopCommentId);
		shopComment.setShopId(shopId);
		shopComment.setOrderId(orderId);
		shopComment.setUserId(userId);
		shopComment.setServiceRating(serviceRating);
		shopComment.setStarRating(starRating);
		shopComment.setCommentContent(commentContent);
		shopComment.setCommentReply(commentReply);
		System.out.println(shopComment.toString());
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

	//删除评论
	@RequestMapping(value = "/deleteshopcomment", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "删除服务评价信息")
	@ApiImplicitParam(paramType = "query", name = "shopCommentId", value = "服务评价ID", required = true, dataType = "Long", example = "3")
	private Map<String, Object> deleteShopComment(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 从请求中获取shopCommentId
		long shopCommentId = HttpServletRequestUtil.getLong(request, "shopCommentId");
		if (shopCommentId > 0) {
			try {
				ShopComment shopComment=shopCommentService.getByShopCommentId(shopCommentId);
				OrderInfo order=orderService.getByOrderId(shopComment.getOrderId());
				order.setOrderStatus(1);
				orderService.modifyOrder(order);
				//删除评论
				ShopCommentExecution ae = shopCommentService.deleteShopComment(shopCommentId);
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

			
		}
		else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "空的查询信息");
			return modelMap;
		}
		return modelMap;
	}



}
