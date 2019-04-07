package com.graduation.ss.web.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.graduation.ss.dto.ConstantForSuperAdmin;
import com.graduation.ss.dto.OrderExecution;
import com.graduation.ss.dto.ShopCommentExecution;
import com.graduation.ss.entity.OrderInfo;
import com.graduation.ss.entity.ServiceInfo;
import com.graduation.ss.entity.Shop;
import com.graduation.ss.entity.ShopComment;
import com.graduation.ss.enums.OrderStateEnum;
import com.graduation.ss.enums.ShopCommentStateEnum;
import com.graduation.ss.service.OrderService;
import com.graduation.ss.service.SService;
import com.graduation.ss.service.ShopCommentService;
import com.graduation.ss.util.HttpServletRequestUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/shopComment")
@Api(value = "ShopCommentController|对服务评论操作的控制器")
public class ShopCommentController {
	@Autowired
	private ShopCommentService shopCommentService;

	@Autowired
	private SService sService;
	@Autowired
	private OrderService OrderService;
	private static final Logger log = LogManager.getLogger(ShopCommentController.class);

	//通过店铺id获取评论列表 分页 再获得订单，最后获得服务列表
	@RequestMapping(value = "/getshopCommentlistbyshopid", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据shopID获取其所有服务评论信息（分页）")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "shopId", value = "店铺ID", required = true, dataType = "Long", example = "3"),
			@ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true, dataType = "int"),
			@ApiImplicitParam(paramType = "query", name = "pageSize", value = "一页的服务评价数目", required = true, dataType = "int") })
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
			List<ShopComment> shopCommentList=se.getShopCommentList();
			List<ServiceInfo> servicelist=new ArrayList<ServiceInfo>();
			for(int i=0;i<shopCommentList.size();i++)
			{
				//通过shopComment获得订单，再获得服务
				 OrderInfo order=OrderService.getByOrderId(shopCommentList.get(i).getOrderId());
				 //服务可能已被删除，所以获取的serviceInfo为null
				 ServiceInfo serviceInfo=sService.getByServiceId(order.getServiceId());
				 if(serviceInfo==null)
				 {
					 serviceInfo=new ServiceInfo();
					 serviceInfo.setServiceName(order.getServiceName());
				 }
				servicelist.add(serviceInfo);
			}
			modelMap.put("serviceList", servicelist);
			modelMap.put("shopCommentList", se.getShopCommentList());
			modelMap.put("pageNum", pageNum);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
	//通过店铺id获取店铺信息（包括 店铺评分平均分 搜街成功率 ）
		@RequestMapping(value = "/getAvgScorebyshopid", method = RequestMethod.GET)
		@ResponseBody
		@ApiOperation(value = "根据shopID获取店铺信息（包括 店铺评分平均分 搜街成功率 ）")
		@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "shopId", value = "店铺ID", required = true, dataType = "Long", example = "3") })
		private Map<String, Object> getAvgScoreByShopId(HttpServletRequest request) {
			Map<String, Object> modelMap = new HashMap<String, Object>();
			Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
			try {
				Shop shop= shopCommentService.getAvgByShopId(shopId);
				modelMap.put("shop", shop);
				modelMap.put("successRate", shop.getSuccessRate());
				modelMap.put("serviceAvg", shop.getServiceAvg());
				modelMap.put("starAvg", shop.getStarAvg());
				modelMap.put("success", true);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			return modelMap;
		}

	//通过店铺id获取过去三个月评论列表 和服务列表 
	@RequestMapping(value = "/getshopCommentlistbysid", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据shopID获取过去三个月评论列表 和服务列表")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "shopId", value = "店铺ID", required = true, dataType = "Long", example = "3")})
	private Map<String, Object> getShopCommentListBySId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		try {
			ShopCommentExecution se = shopCommentService.getByShopId2(shopId);
			List<ShopComment> shopCommentList=se.getShopCommentList();
			List<ServiceInfo> servicelist=new ArrayList<ServiceInfo>();
			int n=shopCommentList.size();
			for(int i=0;i<n;)
			{
				 OrderInfo order=OrderService.getByOrderId(shopCommentList.get(i).getOrderId());
				 LocalDateTime t1=LocalDateTime.now().minusDays(90);
				 LocalDateTime t2=order.getCreateTime();
				 if(t1.isBefore(t2))
				 {
					 //服务可能已被删除，所以获取的serviceInfo为null
					 ServiceInfo serviceInfo=sService.getByServiceId(order.getServiceId());
					 if(serviceInfo==null)
					 {
						 serviceInfo=new ServiceInfo();
						 serviceInfo.setServiceName(order.getServiceName());
					 }
					 servicelist.add(serviceInfo);		
					 i++;
			     }		
				 else
				 {
					 shopCommentList.remove(i);
					 n=n-1;
				 }
			}
			modelMap.put("serviceList", servicelist);
			modelMap.put("shopCommentList", se.getShopCommentList());
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
		@ApiOperation(value = "根据userID获取其所有服务评论信息（分页）")
		@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "userId", value = "用户ID", required = true, dataType = "Long", example = "1"),
				@ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true, dataType = "int"),
				@ApiImplicitParam(paramType = "query", name = "pageSize", value = "一页的服务评价数目", required = true, dataType = "int") })
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
		//通过userId获取过去三个月评论列表  
		@RequestMapping(value = "/getshopCommentlistbyuid", method = RequestMethod.GET)
		@ResponseBody
		@ApiOperation(value = "根据userID获取过去三个月其所有服务评论信息")
		@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "userId", value = "用户ID", required = true, dataType = "Long", example = "1")})
		private Map<String, Object> getShopCommentListByUId(HttpServletRequest request) {
			Map<String, Object> modelMap = new HashMap<String, Object>();
			Long userId = HttpServletRequestUtil.getLong(request, "userId");
			try {
				ShopCommentExecution se = shopCommentService.getByUserId2(userId);
				List<ShopComment> shopCommentList=se.getShopCommentList();
				List<ServiceInfo> servicelist=new ArrayList<ServiceInfo>();
				int n=shopCommentList.size();
				for(int i=0;i<n;)
				{
					 OrderInfo order=OrderService.getByOrderId(shopCommentList.get(i).getOrderId());
					 LocalDateTime t1=LocalDateTime.now().minusDays(90);
					 LocalDateTime t2=order.getCreateTime();
					 if(t1.isBefore(t2))
					 {
						 //服务可能已被删除，所以获取的serviceInfo为null
						 ServiceInfo serviceInfo=sService.getByServiceId(order.getServiceId());
						 if(serviceInfo==null)
						 {
							 serviceInfo=new ServiceInfo();
							 serviceInfo.setServiceName(order.getServiceName());
						 }
						 servicelist.add(serviceInfo);		
						 i++;
				     }		
					 else
					 {
						 shopCommentList.remove(i);
						 n=n-1;
					 }
				}
				modelMap.put("serviceList", servicelist);
				modelMap.put("shopCommentList", se.getShopCommentList());
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
	@ApiOperation(value = "根据shopID、orderId、userId、commentContent获取其所有服务评论信息（分页）")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "shopId", value = "店铺ID", required = true, dataType = "Long", example = "3"),
		@ApiImplicitParam(paramType = "query", name = "orderId", value = "订单ID", required = true, dataType = "Long", example = "1"),
		@ApiImplicitParam(paramType = "query", name = "userId", value = "用户ID", required = true, dataType = "Long", example = "1"),
		@ApiImplicitParam(paramType = "query", name = "commentContent", value = "评论内容", required = true, dataType = "String", example = "测试shopComment内容"),
		@ApiImplicitParam(paramType = "query", name = "commentReply", value = "商家回复", required = true, dataType = "String", example = "回复"),
			@ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true, dataType = "int"),
			@ApiImplicitParam(paramType = "query", name = "pageSize", value = "一页的服务数目", required = true, dataType = "int") })
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
			long orderId=HttpServletRequestUtil.getLong(request, "orderId");
			if(orderId>0)
			    shopCommentCondition.setOrderId(orderId);
			long userId=HttpServletRequestUtil.getLong(request, "userId");
			if(userId>0)
			    shopCommentCondition.setUserId(userId);
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
	/**
	 * 根据id返回评论信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/searchshopCommentbyid", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据shopCommentId获取服务评论信息")
		@ApiImplicitParam(paramType = "query", name = "shopCommentId", value = "服务评价ID", required = true, dataType = "Long", example = "3")
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
	@ApiOperation(value = "添加服务评价信息")
	private Map<String, Object> addShopComment(
			@RequestBody @ApiParam(name = "ShopComment", value = "传入json格式,要传orderId", required = true)ShopComment shopComment, HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 空值判断
		if (shopComment != null) {
			try {
				//添加评论		
				ShopCommentExecution ae = shopCommentService.addShopComment(shopComment);
				OrderInfo order=OrderService.getByOrderId(shopComment.getOrderId());
				order.setOrderStatus(2);
				try {
					//更新订单
					OrderExecution a = OrderService.modifyOrder(order);
					if (a.getState() == OrderStateEnum.SUCCESS.getState()) {
						
					} 
					else {
						modelMap.put("success", false);
						modelMap.put("errMsg", a.getStateInfo());
					}
				} catch (Exception e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.toString());
					return modelMap;
				}
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
	    log.info("shopCommentInfo/n"+shopComment.toString());
		return modelMap;
	}
	//更新评论
		@RequestMapping(value = "/modifyshopComment", method = RequestMethod.POST)
		@ResponseBody
		@ApiOperation(value = "修改服务评价信息")
		private Map<String, Object> modifyShopComment(
				@RequestBody @ApiParam(name = "ShopComment", value = "传入json格式,要传shopCommentId", required = true)ShopComment shopComment, HttpServletRequest request) {
			Map<String, Object> modelMap = new HashMap<String, Object>();
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
		//更新评论
				@RequestMapping(value = "/modifyshopCommentReply", method = RequestMethod.POST)
				@ResponseBody
				@ApiOperation(value = "修改服务评价信息")
				@ApiImplicitParams({	
					@ApiImplicitParam(paramType = "query", name = "orderId", value = "订单ID", required = true, dataType = "Long", example = "1"),
					@ApiImplicitParam(paramType = "query", name = "commentReply", value = "商家回复", required = true, dataType = "String")})
				private Map<String, Object> modifyShopCommentReply(HttpServletRequest request) {
					Map<String, Object> modelMap = new HashMap<String, Object>();
					String commentReply=HttpServletRequestUtil.getString(request, "commentReply");
					long orderId=HttpServletRequestUtil.getLong(request, "orderId");
					if(orderId>0) {
						try {
							//更新评论回复信息			
							ShopComment shopComment=shopCommentService.getByOrderId(orderId);
							shopComment.setCommentReply(commentReply);
							ShopCommentExecution ae = shopCommentService.modifyShopComment(shopComment);
							OrderInfo order=OrderService.getByOrderId(orderId);
							order.setOrderStatus(4);
							try {
								//更新订单
								OrderExecution a = OrderService.modifyOrder(order);
								if (a.getState() == OrderStateEnum.SUCCESS.getState()) {
									
								} 
								else {
									modelMap.put("success", false);
									modelMap.put("errMsg", a.getStateInfo());
								}
							} catch (Exception e) {
								modelMap.put("success", false);
								modelMap.put("errMsg", e.toString());
								return modelMap;
							}
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
		@RequestMapping(value = "/deleteshopComment", method = RequestMethod.POST)
		@ResponseBody
		@ApiOperation(value = "删除服务评价信息")
		@ApiImplicitParam(paramType = "query", name = "shopCommentId", value = "服务评价ID", required = true, dataType = "Long", example = "3")
		private Map<String, Object> deleteShopComment(HttpServletRequest request) {
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
				// 空值判断
				if (shopComment != null) {
					try {
						//删除评论
						ShopCommentExecution ae = shopCommentService.deleteShopComment(shopComment);
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
			}
			else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "空的查询信息");
				return modelMap;
			}
			return modelMap;
		}

		//删除商家评论
		@RequestMapping(value = "/deleteshopCommentReply", method = RequestMethod.POST)
		@ResponseBody
		@ApiOperation(value = "删除商家回复评论信息")
		@ApiImplicitParam(paramType = "query", name = "shopCommentId", value = "服务评价ID", required = true, dataType = "Long", example = "3")
		private Map<String, Object> deleteShopCommentReply(HttpServletRequest request) {
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
				// 空值判断
				if (shopComment != null) {
					try {
						shopComment.setCommentReply(null);
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
					modelMap.put("errMsg", "删除商家回复失败");
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
