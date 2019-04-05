package com.graduation.ss.web.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

import com.graduation.ss.dto.ConstantForSuperAdmin;
import com.graduation.ss.dto.OrderExecution;
import com.graduation.ss.dto.ServiceExecution;
import com.graduation.ss.entity.OrderInfo;
import com.graduation.ss.entity.ServiceInfo;
import com.graduation.ss.enums.OrderStateEnum;
import com.graduation.ss.service.OrderService;
import com.graduation.ss.service.SService;
import com.graduation.ss.util.HttpServletRequestUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/order")
@Api(value = "OrderController|对服务订单操作的控制器")
public class OrderController {
	@Autowired
	private OrderService OrderService;
	@Autowired
	private SService sService;
	    
	
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
		//通过userId获取订单列表 分页 
				@RequestMapping(value = "/getOrderlistbyus", method = RequestMethod.GET)
				@ResponseBody
				@ApiOperation(value = "根据userID serviceId获取正在进行的订单")
				@ApiImplicitParams({
					@ApiImplicitParam(paramType = "query", name = "userId", value = "用户ID", required = true, dataType = "Long", example = "1"),
					@ApiImplicitParam(paramType = "query", name = "serviceId", value = "服务ID", required = true, dataType = "Long", example = "3"),
						@ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = false, dataType = "int"),
						@ApiImplicitParam(paramType = "query", name = "pageSize", value = "一页的订单数目", required = false, dataType = "int") })
				private Map<String, Object> getOrderListByUserIdAndServiceId(HttpServletRequest request) {
					Map<String, Object> modelMap = new HashMap<String, Object>();
					Long userId = HttpServletRequestUtil.getLong(request, "userId");
					Long serviceId= HttpServletRequestUtil.getLong(request, "serviceId");
					int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
					int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
					if(pageSize==-1)
					{
						pageIndex=0;
						pageSize=6;
					}
					try {
						OrderExecution se = OrderService.getByUserIdAndServiceId(userId, serviceId,pageIndex, pageSize);
						int pageNum = (int) (se.getCount() / pageSize);
						if (pageNum * pageSize < se.getCount())
							pageNum++;
						modelMap.put("OrderList", se.getOrderList());
						modelMap.put("order", se.getOrder());
						if(se.getCount()!=0)
						modelMap.put("isbook", true);
						modelMap.put("success", true);
					} catch (Exception e) {
						modelMap.put("success", false);
						modelMap.put("errMsg", e.getMessage());
					}
					return modelMap;
				}
				//通过userId和订单状态获取其近三个月订单信息
				@RequestMapping(value = "/getOrderlistbyuo", method = RequestMethod.GET)
				@ResponseBody
				@ApiOperation(value = "根据userID和订单状态获取其近三个月订单信息")
				@ApiImplicitParams({
					@ApiImplicitParam(paramType = "query", name = "userId", value = "用户ID", required = true, dataType = "Long", example = "1"),
					@ApiImplicitParam(paramType = "query", name = "orderStatus", value = "订单状态", required = false, dataType = "int", example = "0"),})
				private Map<String, Object> getOrderListByUO(HttpServletRequest request) {
					Map<String, Object> modelMap = new HashMap<String, Object>();
					Long userId = HttpServletRequestUtil.getLong(request, "userId");
					int orderStatus= HttpServletRequestUtil.getInt(request, "orderStatus");	
					try {
						OrderExecution se = OrderService.getOrderList2(userId,orderStatus);
						modelMap.put("OrderList", se.getOrderList());
						modelMap.put("orderStatus", orderStatus);
						modelMap.put("success", true);
					} catch (Exception e) {
						modelMap.put("success", false);
						modelMap.put("errMsg", e.getMessage());
					}
					return modelMap;
				}
				//通过userId和订单状态获取其近三个月订单信息和服务信息
				@RequestMapping(value = "/getOrderlistAndServicebyuo", method = RequestMethod.GET)
				@ResponseBody
				@ApiOperation(value = "根据userID和订单状态获取其近三个月订单信息和服务信息")
				@ApiImplicitParams({
					@ApiImplicitParam(paramType = "query", name = "userId", value = "用户ID", required = true, dataType = "Long", example = "1"),
					@ApiImplicitParam(paramType = "query", name = "orderStatus", value = "订单状态", required = false, dataType = "int", example = "0"),})
				private Map<String, Object> getOrderListAndServiceByUO(HttpServletRequest request) {
					Map<String, Object> modelMap = new HashMap<String, Object>();
					Long userId = HttpServletRequestUtil.getLong(request, "userId");
					int orderStatus= HttpServletRequestUtil.getInt(request, "orderStatus");	
					try {
						OrderExecution se = OrderService.getOrderList2(userId,orderStatus);
						List<OrderInfo> orderlist=se.getOrderList();
						List<ServiceInfo> servicelist=new ArrayList<ServiceInfo>();
						for(int i=0;i<orderlist.size();i++)
						{
							ServiceInfo service=sService.getByServiceId(orderlist.get(i).getServiceId());
							servicelist.add(service);
						}
						modelMap.put("OrderList", se.getOrderList());
						modelMap.put("serviceList", servicelist);
						modelMap.put("orderStatus", orderStatus);
						modelMap.put("success", true);
					} catch (Exception e) {
						modelMap.put("success", false);
						modelMap.put("errMsg", e.getMessage());
					}
					return modelMap;
				}
	/**
	 * 根据Service_id返回订单信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/searchOrderbyServiceid", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据ServiceId获取服务订单信息")
		@ApiImplicitParam(paramType = "query", name = "serviceId", value = "服务ID", required = true, dataType = "Long", example = "3")
	private Map<String, Object> searchOrderByServiceId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		OrderInfo Order = null;
		// 从请求中获取OrderId
		long ServiceId = HttpServletRequestUtil.getLong(request, "serviceId");
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
	@RequestMapping(value = "/searchOrderbyid", method = RequestMethod.GET)
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
	//通过shopId orderStatus获取其近三个月订单信息
	@RequestMapping(value = "/getOrderlistbyshopId", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据shopId orderStatus获取其近三个月订单信息")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "shopId", value = "店铺ID", required = true, dataType = "Long", example = "1"),
		@ApiImplicitParam(paramType = "query", name = "orderStatus", value = "订单状态", required = false, dataType = "int", example = "0"),})
	private Map<String, Object> getOrderListByShopId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		int orderStatus= HttpServletRequestUtil.getInt(request, "orderStatus");	
		try {
			ServiceExecution se = sService.getByShopId2(shopId);
			List<OrderInfo> orderlist=new ArrayList<OrderInfo>();
			List<ServiceInfo> servicelist=se.getServiceList();
			for(int i=0;i<se.getCount();i++)
			{
				OrderExecution ore = OrderService.getByServiceId2(servicelist.get(i).getServiceId(),orderStatus);
				orderlist.addAll(ore.getOrderList());
			}
			
			modelMap.put("OrderList",orderlist );
			modelMap.put("orderNum",orderlist.size() );
			modelMap.put("serviceList",servicelist);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
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
				Order.setCreateTime(LocalDateTime.now());
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
				@RequestBody @ApiParam(name = "Order", value = "传入json格式,要传orderId", required = true)OrderInfo Order, HttpServletRequest request) {
			Map<String, Object> modelMap = new HashMap<String, Object>();
			System.out.println(Order.toString());
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
		//取消或确认订单
				@RequestMapping(value = "/changeorderstatus", method = RequestMethod.POST)
				@ResponseBody
				@ApiOperation(value = "修改订单信息")
				private Map<String, Object> changeorderstatus(
						@RequestBody @ApiParam(name = "Order", value = "传入json格式,要传orderId", required = true)OrderInfo Order, HttpServletRequest request) {
					Map<String, Object> modelMap = new HashMap<String, Object>();
					System.out.println(Order.toString());
					// 空值判断
					if (Order != null && Order.getOrderId() != null) {
						try {
							Order.setOverTime(LocalDateTime.now());
							//更新订单
							OrderExecution ae = OrderService.modifyOrder(Order);
							if (ae.getState() == OrderStateEnum.SUCCESS.getState()) {
								modelMap.put("success", true);
								modelMap.put("order", Order);
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
			// 从请求中获取OrderId
			long OrderId = HttpServletRequestUtil.getLong(request, "OrderId");
				if (OrderId>0) {
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
					modelMap.put("errMsg", "空的查询信息");
				}
			return modelMap;
		}
}

