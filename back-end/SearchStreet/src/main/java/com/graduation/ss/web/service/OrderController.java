package com.graduation.ss.web.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
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
import com.graduation.ss.dao.PersonInfoDao;
import com.graduation.ss.dao.ShopDao;
import com.graduation.ss.dto.ConstantForSuperAdmin;
import com.graduation.ss.dto.ImageHolder;
import com.graduation.ss.dto.OrderExecution;
import com.graduation.ss.dto.ServiceExecution;
import com.graduation.ss.dto.UserCode2Session;
import com.graduation.ss.entity.OrderInfo;
import com.graduation.ss.entity.ServiceInfo;
import com.graduation.ss.entity.WechatAuth;
import com.graduation.ss.enums.OrderStateEnum;
import com.graduation.ss.enums.ServiceStateEnum;
import com.graduation.ss.exceptions.OrderOperationException;
import com.graduation.ss.service.OrderService;
import com.graduation.ss.service.PersonInfoService;
import com.graduation.ss.service.SService;
import com.graduation.ss.service.ShopService;
import com.graduation.ss.service.WechatAuthService;
import com.graduation.ss.service.impl.ShopServiceImpl;
import com.graduation.ss.util.HttpServletRequestUtil;
import com.graduation.ss.util.JWT;

@RestController
@RequestMapping("/Order")
@Api(value = "OrderController|对服务订单操作的控制器")
public class OrderController {
	@Autowired
	private OrderService OrderService;
	@Autowired
	private WechatAuthService wechatAuthService;
	@Autowired
	private PersonInfoService personInfoService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private SService sService;

	    
	/*
	 * //通过店铺id获取订单列表 分页
	 * 
	 * @RequestMapping(value = "/getOrderlistbyshopid", method = RequestMethod.GET)
	 * 
	 * @ResponseBody
	 * 
	 * @ApiOperation(value = "根据shopID获取其所有订单信息（分页）")
	 * 
	 * @ApiImplicitParams({
	 * 
	 * @ApiImplicitParam(paramType = "query", name = "shopId", value = "店铺ID",
	 * required = true, dataType = "Long", example = "3"),
	 * 
	 * @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码",
	 * required = true, dataType = "int"),
	 * 
	 * @ApiImplicitParam(paramType = "query", name = "pageSize", value =
	 * "一页的服务评价数目", required = true, dataType = "int") }) private Map<String,
	 * Object> getOrderListByShopId(HttpServletRequest request) { Map<String,
	 * Object> modelMap = new HashMap<String, Object>(); Long shopId =
	 * HttpServletRequestUtil.getLong(request, "shopId"); int pageIndex =
	 * HttpServletRequestUtil.getInt(request, "pageIndex"); int pageSize =
	 * HttpServletRequestUtil.getInt(request, "pageSize"); try { OrderExecution se =
	 * OrderService.getByShopId(shopId, pageIndex, pageSize); int pageNum = (int)
	 * (se.getCount() / pageSize); if (pageNum * pageSize < se.getCount())
	 * pageNum++; modelMap.put("OrderList", se.getOrderList());
	 * modelMap.put("pageNum", pageNum); modelMap.put("success", true); } catch
	 * (Exception e) { modelMap.put("success", false); modelMap.put("errMsg",
	 * e.getMessage()); } return modelMap; }
	 */
	//通过userId获取订单列表 分页 
		@RequestMapping(value = "/getOrderlistbyuserid", method = RequestMethod.GET)
		@ResponseBody
		@ApiOperation(value = "根据userID获取其所有订单信息（分页）")
		@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "userId", value = "用户ID", required = true, dataType = "Long", example = "1"),
				@ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true, dataType = "int"),
				@ApiImplicitParam(paramType = "query", name = "pageSize", value = "一页的订单数目", required = true, dataType = "int") })
		private Map<String, Object> getOrderListByUserId(HttpServletRequest request) {
			Map<String, Object> modelMap = new HashMap<String, Object>();
			Long userId = HttpServletRequestUtil.getLong(request, "userId");
			int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
			int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
			try {
				OrderExecution se = OrderService.getByUserId(userId, pageIndex, pageSize);
				int pageNum = (int) (se.getCount() / pageSize);
				if (pageNum * pageSize < se.getCount())
					pageNum++;
				modelMap.put("OrderList", se.getOrderList());
				modelMap.put("pageNum", pageNum);
				modelMap.put("success", true);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			return modelMap;
		}
	//根据查询条件获取订单列表 分页
	@RequestMapping(value = "/listOrder", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据orderStatus、serviceName、shopName、userName、createTime、overTime获取其所有订单信息（分页）")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "orderStatus", value = "订单状态", required = true, dataType = "int", example = "3"),
		@ApiImplicitParam(paramType = "query", name = "serviceName", value = "服务名字", required = true, dataType = "String", example = "测试服务名字"),
		@ApiImplicitParam(paramType = "query", name = "shopName", value = "店铺名字", required = true, dataType = "String", example = "测试店铺名字"),
		@ApiImplicitParam(paramType = "query", name = "userName", value = "用户名字", required = true, dataType = "String", example = "测试用户名字"),
		@ApiImplicitParam(paramType = "query", name = "createTime", value = "下单时间", required = true, dataType = "DateTime", example = "20180809"),
		@ApiImplicitParam(paramType = "query", name = "overTime", value = "完成时间", required = true, dataType = "DateTime", example = "20180809"),
			@ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true, dataType = "int"),
			@ApiImplicitParam(paramType = "query", name = "pageSize", value = "一页的订单数目", required = true, dataType = "int") })
	private Map<String, Object> listOrder(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		OrderExecution se = null;
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_NO);
		int pageSize = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_SIZE);
		// 空值判断
		if (pageIndex > 0 && pageSize > 0) {
			OrderInfo OrderCondition = new OrderInfo();
			int orderStatus=HttpServletRequestUtil.getInt(request, "orderStatus");
			if(orderStatus>=0)
			   OrderCondition.setOrderStatus(orderStatus);
			String serviceName=HttpServletRequestUtil.getString(request, "serviceName");
			if(serviceName!=null&&!"".equals(serviceName))
			{
				/*
				 * ServiceInfo service=new ServiceInfo(); service.setServiceName(serviceName);
				 * // 根据查询条件分页返回订单列表 ServiceExecution servicee =
				 * sService.getServiceList(service,pageIndex,pageSize);
				 * if(servicee.getState()==-1001) { modelMap.put("success", false);
				 * modelMap.put("errMsg", servicee.getStateInfo()); return modelMap; } else {
				 * List<Long> serviceIdList=new ArrayList<Long>(); for(int
				 * i=0;i<servicee.getCount();i++) {
				 * serviceIdList.add(servicee.getServiceList().get(i).getServiceId()); };
				 * List<OrderExecution> oe=OrderService.getByServiceIdList(serviceIdList,
				 * pageIndex, pageSize);
				 * 
				 * }
				 */
				
			}
			String shopName=HttpServletRequestUtil.getString(request, "shopName");
			if(shopName!=null&&!"".equals(shopName))
			{
				long shopId=0;
				long serviceId = 0;
				OrderCondition.setServiceId(serviceId);
			}
			String userName=HttpServletRequestUtil.getString(request, "userName");
			if(userName!=null&&!"".equals(userName))
			{
				long userId = 0;
				OrderCondition.setUserId(userId);
			}
			Date createTime=HttpServletRequestUtil.getDate(request, "createTime");
			
			Date overTime=HttpServletRequestUtil.getDate(request, "overTime");
			try {
				// 根据查询条件分页返回订单列表
				se = OrderService.getOrderList(OrderCondition, pageIndex, pageSize);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (se.getOrderList() != null) {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, se.getOrderList());
				modelMap.put(ConstantForSuperAdmin.TOTAL, se.getCount());
				modelMap.put("success", true);
			} else {
				// 取出数据为空，也返回new list以使得前端不出错
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, new ArrayList<OrderInfo>());
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
	 * 根据Service_id返回订单信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/searchOrderbyServiceid", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "根据ServiceId获取服务订单信息")
		@ApiImplicitParam(paramType = "query", name = "ServiceId", value = "服务ID", required = true, dataType = "Long", example = "3")
	private Map<String, Object> searchOrderByServiceId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		OrderInfo Order = null;
		// 从请求中获取OrderId
		long ServiceId = HttpServletRequestUtil.getLong(request, "ServiceId");
		if (ServiceId > 0) {
			try {
				// 根据Id获取订单实例
				Order = OrderService.getByOrderId(ServiceId);
				
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (Order != null) {
				List<OrderInfo> OrderList = new ArrayList<OrderInfo>();
				OrderList.add(Order);
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, OrderList);
				modelMap.put(ConstantForSuperAdmin.TOTAL, 1);
				modelMap.put("success", true);
			} else {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, new ArrayList<OrderInfo>());
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
	 * 根据id返回订单信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/searchOrderbyid", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "根据OrderId获取服务订单信息")
		@ApiImplicitParam(paramType = "query", name = "OrderId", value = "订单ID", required = true, dataType = "Long", example = "3")
	private Map<String, Object> searchOrderById(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		OrderInfo Order = null;
		// 从请求中获取OrderId
		long OrderId = HttpServletRequestUtil.getLong(request, "OrderId");
		if (OrderId > 0) {
			try {
				// 根据Id获取订单实例
				Order = OrderService.getByOrderId(OrderId);
				
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (Order != null) {
				List<OrderInfo> OrderList = new ArrayList<OrderInfo>();
				OrderList.add(Order);
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, OrderList);
				modelMap.put(ConstantForSuperAdmin.TOTAL, 1);
				modelMap.put("success", true);
			} else {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, new ArrayList<OrderInfo>());
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
	//添加订单
	@RequestMapping(value = "/addOrder", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "添加订单信息")
	private Map<String, Object> addOrder(
			@RequestBody @ApiParam(name = "Order", value = "传入json格式,要传orderId", required = true)OrderInfo Order, HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();

	
		// 空值判断
		if (Order != null && Order.getOrderId() != null) {
			try {
				//添加订单
				OrderExecution ae = OrderService.addOrder(Order);
				if (ae.getState() == OrderStateEnum.SUCCESS.getState()) {
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
			modelMap.put("errMsg", "请输入订单信息");
		}
		return modelMap;
	}
	//更新订单
		@RequestMapping(value = "/modifyOrder", method = RequestMethod.POST)
		@ResponseBody
		@ApiOperation(value = "修改订单信息")
		private Map<String, Object> modifyOrder(
				@RequestBody @ApiParam(name = "Order", value = "传入json格式,要传orderd", required = true)OrderInfo Order, HttpServletRequest request) {
			Map<String, Object> modelMap = new HashMap<String, Object>();
			
			// 空值判断
			if (Order != null && Order.getOrderId() != null) {
				try {
					//更新订单
					OrderExecution ae = OrderService.modifyOrder(Order);
					if (ae.getState() == OrderStateEnum.SUCCESS.getState()) {
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
				modelMap.put("errMsg", "请输入订单信息");
			}
			return modelMap;
		}

		//删除订单
		@RequestMapping(value = "/deleteOrder", method = RequestMethod.POST)
		@ResponseBody
		@ApiOperation(value = "删除订单信息")
		@ApiImplicitParam(paramType = "query", name = "OrderId", value = "订单ID", required = true, dataType = "Long", example = "3")
		private Map<String, Object> deleteOrder(HttpServletRequest request) {
			Map<String, Object> modelMap = new HashMap<String, Object>();
			OrderInfo Order = null;
			// 从请求中获取OrderId
			long OrderId = HttpServletRequestUtil.getLong(request, "OrderId");
			if (OrderId > 0) {
				try {
					// 根据Id获取订单实例
					Order = OrderService.getByOrderId(OrderId);
					
				} catch (Exception e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.toString());
					return modelMap;
				}
				// 空值判断
				if (Order != null) {
					try {
						//删除订单
						OrderExecution ae = OrderService.deleteOrder(OrderId);
						if (ae.getState() == OrderStateEnum.SUCCESS.getState()) {
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
					modelMap.put("errMsg", "请输入订单信息");
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

