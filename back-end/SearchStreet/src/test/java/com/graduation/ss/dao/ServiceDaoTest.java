package com.graduation.ss.dao;

import static org.junit.Assert.*;
import java.lang.Long;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.graduation.ss.entity.Service;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceDaoTest {
	@Autowired
	private ServiceDao serviceDao;
	
	@Test
	@Ignore
	public void testQueryServiceListAndCount() {
		Service serviceCondition = new Service();
		
		serviceCondition.setShopId(1L);
		List<Service> serviceList = serviceDao.queryServiceList(serviceCondition, 0, 4);
		int count = serviceDao.queryServiceCount(serviceCondition);
		System.out.println("服务列表-shopid的大小：" + serviceList.size());
		System.out.println("服务总数-shopid：" + count);	
		
		serviceCondition.setServiceName("测试服务名称");
		serviceList = serviceDao.queryServiceList(serviceCondition, 0, 3);
		count = serviceDao.queryServiceCount(serviceCondition);
		System.out.println("服务列表-name的大小：" + serviceList.size());
		System.out.println("服务总数-name：" + count);
		
	}
	@Test
	@Ignore
	public void testInsertService() {
		Service service = new Service();
		service.setShopId(1L);
		service.setServiceName("测试服务");
		service.setServicePrice(1L);
		service.setServiceDesc("测试服务");
		service.setServiceContent("测试服务");
		service.setServiceImg("测试服务");
		int effectedNum = serviceDao.insertService(service);
		assertEquals(1, effectedNum);
	}
	@Test
	@Ignore
	public void testInsertServiceInfo() {
		List<Service>serviceList=new ArrayList<Service>();
		Service service2 = new Service();
		service2.setShopId(2L);
		service2.setServiceName("test2");
		service2.setServicePrice(1L);
		service2.setServiceDesc("test2");
		service2.setServiceContent("test2");
		service2.setServiceImg("test2");
		serviceList.add(service2);
		Service service3 = new Service();
		service3.setShopId(3L);
		service3.setServiceName("test3");
		service3.setServicePrice(1L);
		service3.setServiceDesc("test3");
		service3.setServiceContent("test3");
		service3.setServiceImg("test3");
		serviceList.add(service3);
		Service service4 = new Service();
		service4.setShopId(1L);
		service4.setServiceName("测试服务内容");
		service4.setServicePrice(1L);
		service4.setServiceDesc("测试服务内容");
		service4.setServiceContent("测试服务内容");
		service4.setServiceImg("测试服务内容");
		serviceList.add(service4);
		int effectedNum = serviceDao.insertServiceInfo(serviceList);
		assertEquals(3, effectedNum);
	}
	@Test
	@Ignore
	public void testUpdateService() {
		Service service = new Service();
		service.setServiceId(1L);
		service.setServiceContent("测试内容");
		service.setServiceDesc("测试描述");
		int effectedNum = serviceDao.updateService(service);
		assertEquals(1, effectedNum);
	}
	
	@Test
	@Ignore
	public void testQueryByServiceId() {
		long serviceId = 1;
		Service service = serviceDao.queryByServiceId(serviceId);
		System.out.println("servicecontent: "+service.getServiceContent());
	}
	@Test
	@Ignore
	public void testQueryByShopId() {
		long shopId = 1;
		List<Service>serviceList=serviceDao.queryByShopId(shopId);
		for(int i=0;i<serviceList.size();i++)
		System.out.println("servicecontent: "+serviceList.get(i).getServiceContent());
	}
	@Test
	@Ignore
	public void testDeleteService() {
		Service service = new Service();
		service.setServiceId(2L);
		int effectedNum = serviceDao.deleteService(service);
		assertEquals(1, effectedNum);
	}
}
