package com.graduation.ss.service.impl;

import java.util.ArrayList;
//import java.lang.annotation.Annotation;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.graduation.ss.dao.OrderDao;
import com.graduation.ss.dto.ConstantForSuperAdmin;
import com.graduation.ss.dto.OrderExecution;

import com.graduation.ss.entity.OrderInfo;

import com.graduation.ss.exceptions.OrderOperationException;
import com.graduation.ss.service.OrderService;
import com.graduation.ss.util.PageCalculator;
import com.graduation.ss.util.PathUtil;
import com.graduation.ss.enums.OrderStateEnum;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderDao orderDao;
	
	@Override
	public OrderExecution getOrderList(OrderInfo orderCondition, int pageIndex, int pageSize) {
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的订单列表
		List<OrderInfo > orderList = orderDao.queryOrderList(orderCondition, rowIndex, pageSize);
		// 依据相同的查询条件，返回订单总数
		int count = orderDao.queryOrderCount(orderCondition);
		OrderExecution se = new OrderExecution();
		if (orderList != null) {
			se.setOrderList(orderList);
			se.setCount(count);
		} else {
			se.setState(OrderStateEnum.INNER_ERROR.getState());
		}
		return se;
	}
	
	@Override
	public OrderExecution getByServiceId(long serviceId, int pageIndex, int pageSize){
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的订单列表
		
		OrderInfo  orderCondition = new OrderInfo ();
		orderCondition.setServiceId(serviceId);
		List<OrderInfo > orderList = orderDao.queryOrderList(orderCondition, rowIndex, pageSize);
		OrderExecution se = new OrderExecution();
		// 依据相同的查询条件，返回订单总数
		int count = orderDao.queryOrderCount(orderCondition);
		if (orderList != null) {
			se.setOrderList(orderList);
			se.setCount(count);
		} else {
			se.setState(OrderStateEnum.INNER_ERROR.getState());
		}
		return se;
	}
	@Override
	public List<OrderExecution> getByServiceIdList(List<Long> serviceIdList, int pageIndex, int pageSize){
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的订单列表
		List<OrderInfo > torderList=new ArrayList<OrderInfo>();
		OrderInfo  orderCondition = new OrderInfo ();
		List<OrderExecution> tse = new ArrayList<OrderExecution>();
		for(int i=0;i<serviceIdList.size();i++)
		{
			orderCondition.setServiceId(serviceIdList.get(i));
			List<OrderInfo > orderList = orderDao.queryOrderList(orderCondition, rowIndex, pageSize);
			// 依据相同的查询条件，返回订单总数
			int count = orderDao.queryOrderCount(orderCondition);
			OrderExecution se = new OrderExecution(i);
			if (orderList != null) {
				
				se.setCount(torderList.size()+count);
				//se.setCount(torderList.size()+orderList.size());
				torderList.addAll(orderList);
				se.setOrderList(torderList);
				
				//se.setCount(torderList.size());
			} else {
				se.setState(OrderStateEnum.INNER_ERROR.getState());
			}
		}
		return tse;
	}
	@Override
	public OrderExecution getByUserId(long userId, int pageIndex, int pageSize){
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的订单列表
		
		OrderInfo orderCondition = new OrderInfo();
		orderCondition.setUserId(userId);
		List<OrderInfo> orderList = orderDao.queryOrderList(orderCondition, rowIndex, pageSize);
		OrderExecution se = new OrderExecution();
		// 依据相同的查询条件，返回订单总数
		int count = orderDao.queryOrderCount(orderCondition);
		if (orderList != null) {
			se.setOrderList(orderList);
			se.setCount(count);
		} else {
			se.setState(OrderStateEnum.INNER_ERROR.getState());
		}
		return se;
	}
	@Override
	public OrderInfo getByOrderId(long orderId) {
		return orderDao.queryByOrderId(orderId);
	}
   
	@Override
	public OrderExecution addOrder(OrderInfo order) throws OrderOperationException {
		// 空值判断
		if (order == null) {
			return new OrderExecution(OrderStateEnum.NULL_ORDER);
		}
		try {
			// 添加订单信息（从前端读取数据）
			int effectedNum = orderDao.insertOrder(order);
			if (effectedNum <= 0) {
				throw new OrderOperationException("订单创建失败");
			}
		} catch (Exception e) {
			throw new OrderOperationException("addOrder error:" + e.getMessage());
		}
		return new OrderExecution(OrderStateEnum.SUCCESS, order);
	}

	@Transactional
	@Override
	public OrderExecution modifyOrder(OrderInfo order) throws OrderOperationException{
		// 空值判断
		if (order == null) {
			return new OrderExecution(OrderStateEnum.NULL_ORDER);
		}
		try {
			// 修改订单信息
			int effectedNum = orderDao.updateOrder(order);
			if (effectedNum <= 0) {
				throw new OrderOperationException("订单修改失败");
			}
		} catch (Exception e) {
			throw new OrderOperationException("modifyOrder error:" + e.getMessage());
		}
		return new OrderExecution(OrderStateEnum.SUCCESS, order);
	}

	
	@Override
	public OrderExecution deleteOrder(long orderId) throws OrderOperationException
	{
		// 空值判断
				if (orderId<=0) {
					return new OrderExecution(OrderStateEnum.NULL_ORDERID);
				}
				try {
					// 删除订单信息
					int effectedNum = orderDao.deleteOrder(orderId);
					if (effectedNum <= 0) {
						throw new OrderOperationException("订单删除失败");
					}
				} catch (Exception e) {
					throw new OrderOperationException("deleteOrder error:" + e.getMessage());
				}
				return new OrderExecution(OrderStateEnum.SUCCESS, orderDao.queryByOrderId(orderId));
	}




}
