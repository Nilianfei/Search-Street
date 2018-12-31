/**
 * @Description:      Dao测试类
 * @author:           summer_unreal
 * @version            V1.0
 * Createdate:        2018年12月31日  
 */
package com.graduation.ss.dao;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.graduation.ss.entity.OrderInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDaoTest {
	@Autowired
	private OrderDao orderDao;
	
	@Test
	//@Ignore
	public void testQueryOrderListAndCount() {
		OrderInfo orderCondition2 = new OrderInfo();

		orderCondition2.setServiceId(1L);
		List<OrderInfo> orderInfoList = orderDao.queryOrderList(orderCondition2, 0, 4);
		int count = orderDao.queryOrderCount(orderCondition2);
		System.out.println("订单列表-serviceid的大小：" + orderInfoList.size());
		System.out.println("订单总数-serviceid：" + count);	
		orderInfoList.clear();
		
		orderCondition2.setOrderStatus(0);
		orderInfoList = orderDao.queryOrderList(orderCondition2, 0, 3);
		count = orderDao.queryOrderCount(orderCondition2);
		System.out.println("订单列表-OrderStatus的大小：" + orderInfoList.size());
		System.out.println("订单总数-OrderStatus：" + count);
		for(int i=0;i<orderInfoList.size();i++)
			System.out.println("orderid: "+orderInfoList.get(i).getOrderId());
		orderInfoList.clear();
		
	
		OrderInfo orderCondition = new OrderInfo();
		orderCondition.setUserId(1L);
		orderInfoList = orderDao.queryOrderList(orderCondition, 0, 8);
		count = orderDao.queryOrderCount(orderCondition);
		System.out.println("订单列表-UserId的大小：" + orderInfoList.size());
		System.out.println("订单总数-UserId：" + count);
		for(int i=0;i<orderInfoList.size();i++)
			System.out.println("orderid: "+orderInfoList.get(i).getOrderId());
		orderInfoList.clear();
		
		OrderInfo orderCondition3 = new OrderInfo();
		orderCondition3.setCreateTime(LocalDateTime.now());
		orderInfoList = orderDao.queryOrderList(orderCondition3, 0, 8);
		count = orderDao.queryOrderCount(orderCondition3);
		System.out.println("订单列表-CreateTime的大小：" + orderInfoList.size());
		System.out.println("订单总数-CreateTime：" + count);
		for(int i=0;i<orderInfoList.size();i++)
		{
			System.out.println("orderid: "+orderInfoList.get(i).getOrderId());
			System.out.println("createtime: "+orderInfoList.get(i).getCreateTime()+">="+LocalDateTime.now());
		}
		orderInfoList.clear();
		
		OrderInfo orderCondition4 = new OrderInfo();
		orderCondition3.setOverTime(LocalDateTime.now());
		orderInfoList = orderDao.queryOrderList(orderCondition4, 0, 8);
		count = orderDao.queryOrderCount(orderCondition4);
		System.out.println("LOCALDATETIME: "+LocalDateTime.now());
		System.out.println("订单列表-OverTime的大小：" + orderInfoList.size());
		System.out.println("订单总数-OverTime：" + count);
		for(int i=0;i<orderInfoList.size();i++)
		{
			
			System.out.println("orderid: "+orderInfoList.get(i).getOrderId());
			System.out.println("overtime: "+orderInfoList.get(i).getOverTime()+">="+LocalDateTime.now());
		}
	}
	@Test
	@Ignore
	public void testInsertOrder() throws Exception {
		OrderInfo orderInfo = new OrderInfo();
		//SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		//Long l = System.currentTimeMillis();
		
		orderInfo.setServiceId(1L);
		orderInfo.setUserId(1L);
		orderInfo.setServiceCount(1L);
		orderInfo.setOrderStatus(0);
		orderInfo.setCreateTime(LocalDateTime.now());
		
		LocalDateTime date2=LocalDateTime.of(9999, 12, 31, 00, 00, 00);
		orderInfo.setOverTime(date2);
		System.out.println("OverTime: "+orderInfo.getOverTime());
		System.out.println("CreateTime: "+orderInfo.getCreateTime());
		int effectedNum = orderDao.insertOrder(orderInfo);
		assertEquals(1, effectedNum);
	}
	@Test
	@Ignore
	public void testInsertOrderInfo() throws Exception {
		List<OrderInfo>orderInfoList=new ArrayList<OrderInfo>();
		OrderInfo orderInfo2 = new OrderInfo();
		orderInfo2.setServiceId(2L);  //DIFF SERVICEID
		orderInfo2.setUserId(1L);
		orderInfo2.setServiceCount(2L);
		orderInfo2.setOrderStatus(0);
		orderInfo2.setCreateTime(LocalDateTime.now());
		LocalDateTime date2=LocalDateTime.of(9999, 12, 31, 00, 00, 00);
		orderInfo2.setOverTime(date2);
		orderInfoList.add(orderInfo2);
		OrderInfo orderInfo3 = new OrderInfo();
		orderInfo3.setServiceId(1L);
		orderInfo3.setUserId(1L);
		orderInfo3.setServiceCount(2L);
		orderInfo3.setOrderStatus(1);   //DIFF ORDERSTATUS
		orderInfo3.setCreateTime(LocalDateTime.now());
		orderInfo3.setOverTime(LocalDateTime.now().plusDays(1));
		orderInfoList.add(orderInfo3);
		OrderInfo orderInfo4 = new OrderInfo();
		orderInfo4.setServiceId(1L);     //DIFF USERID
		orderInfo4.setUserId(2L);
		orderInfo4.setServiceCount(2L);
		orderInfo4.setOrderStatus(1);
		orderInfo4.setCreateTime(LocalDateTime.now());
		orderInfo4.setOverTime(LocalDateTime.now().plusDays(1));
		orderInfoList.add(orderInfo4);
		OrderInfo orderInfo5 = new OrderInfo();
		orderInfo5.setServiceId(1L);
		orderInfo5.setUserId(1L);
		orderInfo5.setServiceCount(1L);
		orderInfo5.setOrderStatus(1);
		orderInfo5.setCreateTime(LocalDateTime.of(2018, 7, 12, 00, 00, 00));    //DIFF CREATETIME/OVERTIME
		orderInfo5.setOverTime(LocalDateTime.of(2018, 7, 13, 00, 00, 00));
		orderInfoList.add(orderInfo5);
		int effectedNum = orderDao.insertOrderInfo(orderInfoList);
		assertEquals(4, effectedNum);
	}
	@Test
	@Ignore
	public void testUpdateOrder() {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setOrderId(1L);
		orderInfo.setOrderStatus(1);
		orderInfo.setOverTime(LocalDateTime.now());
		int effectedNum = orderDao.updateOrder(orderInfo);
		assertEquals(1, effectedNum);
	}
	
	@Test
	@Ignore
	public void testQueryByOrderId() {
		long orderId = 1L;
		OrderInfo orderInfo = orderDao.queryByOrderId(orderId);
		System.out.println("orderstatus: "+orderInfo.getOrderStatus());
	}
	
	@Test
	@Ignore
	public void testDeleteOrder() {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setOrderId(2L);
		int effectedNum = orderDao.deleteOrder(orderInfo);
		assertEquals(1, effectedNum);
	}
}
