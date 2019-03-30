package com.graduation.ss.service;


import java.util.List;

import com.graduation.ss.dto.OrderExecution;
import com.graduation.ss.exceptions.OrderOperationException;
import com.graduation.ss.entity.OrderInfo;

public interface OrderService {
	/**
	 * 根据orderCondition分页返回相应订单列表
	 * 
	 * @param orderCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public OrderExecution getOrderList(OrderInfo orderCondition, int pageIndex, int pageSize);

	/**
	 * 根据orderCondition分页返回相应订单列表
	 * 
	 * @param orderCondition
	 * @return
	 */
	public OrderExecution getOrderList2(long userId,int orderStatus);
	/**
	 * 通过serviceId获取订单信息
	 * 
	 * @param shopId
	 * @return
	 */
	public OrderExecution getByServiceId(long serviceId, int pageIndex, int pageSize);
	public OrderExecution getByServiceId2(long serviceId,int orderStatus);
	
	public List<OrderExecution>  getByServiceIdList(List<Long> serviceIdList, int pageIndex, int pageSize);
	/**
	 * 通过userId获取订单信息
	 * 
	 * @param userId
	 * @return
	 */
	public OrderExecution getByUserId(long userId, int pageIndex, int pageSize);
	/**
	 * 通过userId serviceId获取订单信息
	 * 
	 * @param userId serviceId
	 * @return
	 */
	public OrderExecution getByUserIdAndServiceId(long userId, long serviceId,int pageIndex, int pageSize);
	/**
	 * 通过订单Id获取订单信息
	 * 
	 * @param orderId
	 * @return
	 */
	public OrderInfo getByOrderId(long orderId);
	/**
	 * 更新订单信息
	 * 
	 * @param order
	 * 
	 * @return
	 * @throws OrderOperationException
	 */
	public OrderExecution modifyOrder(OrderInfo order) throws OrderOperationException;

	/**
	 * 添加订单信息
	 * 
	 * @param order
	 * @param orderImgInputStream
	 * @return
	 * @throws OrderOperationException
	 */
	public OrderExecution addOrder(OrderInfo order) throws OrderOperationException;
	/**
	 * 删除订单信息
	 * 
	 * @param order
	 * @return
	 * @throws OrderOperationException
	 */
	public OrderExecution deleteOrder(long orderId) throws OrderOperationException;
}
