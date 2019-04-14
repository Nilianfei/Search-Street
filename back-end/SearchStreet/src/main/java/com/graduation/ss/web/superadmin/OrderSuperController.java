package com.graduation.ss.web.superadmin;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import com.graduation.ss.dto.ConstantForSuperAdmin;
import com.graduation.ss.dto.OrderExecution;
import com.graduation.ss.entity.OrderInfo;
import com.graduation.ss.enums.OrderStateEnum;
import com.graduation.ss.service.OrderService;
import com.graduation.ss.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/superadmin")
public class OrderSuperController {
	@Autowired
	private OrderService orderService;

	/**
	 * 获取订单列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listorders", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> listOrders(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		OrderExecution ae = null;
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_NO);
		int pageSize = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_SIZE);
		// 空值判断
		if (pageIndex > 0 && pageSize > 0) {
			OrderInfo orderCondition = new OrderInfo();
			int orderStatus = HttpServletRequestUtil.getInt(request, "orderStatus");
			if (orderStatus >= 0) {
				// 若传入可用状态，则将可用状态添加到查询条件里
				orderCondition.setOrderStatus(orderStatus);
			}
			String serviceName = HttpServletRequestUtil.getString(request, "serviceName");
			if (serviceName != null) {
				try {
					// 若传入服务名称，则将服务名称解码后添加到查询条件里，进行模糊查询
					orderCondition.setServiceName(URLDecoder.decode(serviceName, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.toString());
				}
			}
			try {
				// 根据查询条件分页返回订单列表
				ae = orderService.getOrderList(orderCondition, pageIndex, pageSize);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (ae.getOrderList() != null) {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, ae.getOrderList());
				modelMap.put(ConstantForSuperAdmin.TOTAL, ae.getCount());
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
	 * 根据id返回订单信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/searchorderbyid", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> searchOrderById(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		OrderInfo order = null;
		// 从请求中获取orderId
		Long orderId = HttpServletRequestUtil.getLong(request, "orderId");
		if (orderId != null && orderId > 0) {
			try {
				// 根据Id获取订单实例
				order = orderService.getByOrderId(orderId);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (order != null) {
				List<OrderInfo> orderList = new ArrayList<OrderInfo>();
				orderList.add(order);
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, orderList);
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
	 * 根据serviceId返回订单信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/searchorderbyserviceid", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> searchOrderByServiceId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		OrderExecution ae = null;
		// 从请求中获取serviceId
		Long serviceId=HttpServletRequestUtil.getLong(request, "serviceId");
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_NO);
	    int pageSize = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_SIZE);
		if (serviceId != null && serviceId > 0) {
			try {
				OrderInfo orderCondition = new OrderInfo();
				// 根据serviceId获取订单实例
				int orderStatus = HttpServletRequestUtil.getInt(request, "orderStatus");
				if (orderStatus >= 0) {
					// 若传入可用状态，则将可用状态添加到查询条件里				
					orderCondition.setOrderStatus(orderStatus);
				}
				orderCondition.setServiceId(serviceId);
				ae = orderService.getOrderList(orderCondition, pageIndex, pageSize);
			    //ae = orderService.getByServiceId(serviceId,pageIndex,pageSize);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (ae.getOrderList() != null) {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, ae.getOrderList());
				modelMap.put(ConstantForSuperAdmin.TOTAL, ae.getCount());
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
	 * 根据userId返回订单信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/searchorderbyuserid", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> searchOrderByUserId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		OrderExecution ae = null;
		// 从请求中获取userId
		Long userId=HttpServletRequestUtil.getLong(request, "userId");
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_NO);
	    int pageSize = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_SIZE);
		if (userId != null && userId > 0) {
			try {
				OrderInfo orderCondition = new OrderInfo();
				// 根据userId获取订单实例
				int orderStatus = HttpServletRequestUtil.getInt(request, "orderStatus");
				if (orderStatus >= 0) {
					// 若传入可用状态，则将可用状态添加到查询条件里				
					orderCondition.setOrderStatus(orderStatus);
				}
				orderCondition.setUserId(userId);
				ae = orderService.getOrderList(orderCondition, pageIndex, pageSize);
				//ae = orderService.getByUserId(userId,pageIndex,pageSize);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (ae.getOrderList() != null) {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, ae.getOrderList());
				modelMap.put(ConstantForSuperAdmin.TOTAL,ae.getCount());
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
	 * 添加订单信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addorder", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addOrder(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long serviceId=HttpServletRequestUtil.getLong(request, "serviceId");
		Long userId=HttpServletRequestUtil.getLong(request, "userId");
		Long serviceCount=HttpServletRequestUtil.getLong(request, "serviceCount");
		int orderStatus=HttpServletRequestUtil.getInt(request, "orderStatus");
		String s1=HttpServletRequestUtil.getString(request, "createTime");
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime createTime = LocalDateTime.parse(s1,df);
		//LocalDateTime createTime=HttpServletRequestUtil.getDate(request, "createTime").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		String serviceName = HttpServletRequestUtil.getString(request, "serviceName");
		double orderPrice=HttpServletRequestUtil.getDouble(request, "orderPrice");
		OrderInfo order = new OrderInfo();
		order.setServiceId(serviceId);
		order.setUserId(userId);
		order.setServiceCount(serviceCount);
		order.setOrderStatus(orderStatus);
		order.setCreateTime(createTime);
		order.setServiceName(serviceName);
		order.setOrderPrice(orderPrice);
		
		// 空值判断
		try {
			OrderExecution ae = orderService.addOrder(order);
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
		return modelMap;
	}

	/**
	 * 修改订单信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifyorder", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyOrder(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long orderId=HttpServletRequestUtil.getLong(request, "orderId");
		Long serviceId=HttpServletRequestUtil.getLong(request, "serviceId");
		Long userId=HttpServletRequestUtil.getLong(request, "userId");
		Long serviceCount=HttpServletRequestUtil.getLong(request, "serviceCount");
		int orderStatus=HttpServletRequestUtil.getInt(request, "orderStatus");
		String s1=HttpServletRequestUtil.getString(request, "createTime");
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime createTime = LocalDateTime.parse(s1,df);
		System.out.println(createTime);
		//Date t1=new Date();
		//LocalDateTime createTime=t1.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		String s2=HttpServletRequestUtil.getString(request, "overTime");
		LocalDateTime overTime=LocalDateTime.parse(s2,df);
		String serviceName = HttpServletRequestUtil.getString(request, "serviceName");
		double orderPrice=HttpServletRequestUtil.getDouble(request, "orderPrice");
		OrderInfo order = new OrderInfo();
		order.setOrderId(orderId);
		order.setServiceId(serviceId);
		order.setUserId(userId);
		order.setServiceCount(serviceCount);
		order.setOrderStatus(orderStatus);
		order.setCreateTime(createTime);
		order.setOverTime(overTime);
		order.setServiceName(serviceName);
		order.setOrderPrice(orderPrice);
		// 空值判断
		if (order != null && order.getOrderId() != null) {
			try {
				OrderExecution ae = orderService.modifyOrder(order);
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
	@RequestMapping(value = "/deleteorder", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> deleteOrder(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 从请求中获取OrderId
		long OrderId = HttpServletRequestUtil.getLong(request, "orderId");
			if (OrderId>0) {
				try {
					//删除订单
					OrderExecution ae = orderService.deleteOrder(OrderId);
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
				modelMap.put("errMsg", "空的查询信息");
			}
		return modelMap;
	}
}
