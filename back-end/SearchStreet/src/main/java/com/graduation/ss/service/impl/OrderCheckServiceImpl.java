package com.graduation.ss.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.graduation.ss.dao.OrderDao;
import com.graduation.ss.entity.OrderInfo;
import com.graduation.ss.exceptions.OrderOperationException;
import com.graduation.ss.service.OrderCheckService;

@Service
public class OrderCheckServiceImpl implements OrderCheckService {
	@Autowired
	private OrderDao orderDao;
	@Override
	@Transactional
	public void checkOrder()throws OrderOperationException{
		OrderInfo orderCondition = new OrderInfo();
		orderCondition.setOrderStatus(0);
		List<OrderInfo> orderList = orderDao.queryOrderList2(orderCondition);


		LocalDateTime t=LocalDateTime.now().minusDays(7);
		for (OrderInfo order : orderList) {
			if (order.getCreateTime().isBefore(t)) {
				order.setOrderStatus(1);
				order.setOverTime(LocalDateTime.now());
				try {
					// 修改订单信息 超时自动确认订单
					int effectedNum = orderDao.updateOrder(order);
					System.out.println(effectedNum);
					if (effectedNum <= 0) {
						throw new OrderOperationException("订单修改失败");
					}
				} catch (Exception e) {
					throw new OrderOperationException("modifyOrder error:" + e.getMessage());
			}
		}
	}
  }

}