package com.graduation.ss.service;

import com.graduation.ss.exceptions.OrderOperationException;

public interface OrderCheckService {

	/**
	 * 检测进行中的order是否过期，如果过期，则修改order的状态，自动确认订单
	 * @throws AppealOperationException
	 */
	public void checkOrder()throws OrderOperationException;
	
}
