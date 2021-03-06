package com.graduation.ss.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.graduation.ss.entity.ServiceInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceDaoTest {
	@Autowired
	private ServiceDao serviceDao;
	
	@Test
	//@Ignore
	public void testQueryServiceListAndCount() {
		ServiceInfo serviceCondition2 = new ServiceInfo();
		
		serviceCondition2.setShopId(1L);
		List<ServiceInfo> serviceInfoList = serviceDao.queryServiceList(serviceCondition2, 0, 4);
		int count = serviceDao.queryServiceCount(serviceCondition2);
		System.out.println("服务列表-shopid的大小：" + serviceInfoList.size());
		System.out.println("服务总数-shopid：" + count);	
		serviceInfoList.clear();
		serviceCondition2.setServicePrice(new Double(22));
		serviceInfoList = serviceDao.queryServiceList(serviceCondition2, 0, 3);
		count = serviceDao.queryServiceCount(serviceCondition2);
		System.out.println("服务列表-price的大小：" + serviceInfoList.size());
		System.out.println("服务总数-price：" + count);
		serviceInfoList.clear();
		
	
		ServiceInfo serviceCondition = new ServiceInfo();
		serviceCondition.setServiceName("测试服务名");
		serviceInfoList = serviceDao.queryServiceList(serviceCondition, 0, 8);
		count = serviceDao.queryServiceCount(serviceCondition);
		System.out.println("服务列表-name的大小：" + serviceInfoList.size());
		System.out.println("服务总数-name：" + count);
		for(int i=0;i<serviceInfoList.size();i++)
			System.out.println("servicename: "+serviceInfoList.get(i).getServiceName());
	}
	@Test
	@Ignore
	public void testInsertService() {
		ServiceInfo serviceInfo = new ServiceInfo();
		serviceInfo.setShopId(6L);
		serviceInfo.setServiceName("test");
		serviceInfo.setServicePrice(new Double(22));
		serviceInfo.setServicePriority(4L);
		serviceInfo.setServiceDesc("test");
		serviceInfo.setServiceContent("test");
		serviceInfo.setServiceImgAddr("test.png");
		int effectedNum = serviceDao.insertService(serviceInfo);
		assertEquals(1, effectedNum);
	}
	@Test
	@Ignore
	public void testInsertServiceInfo() {
		List<ServiceInfo>serviceInfoList=new ArrayList<ServiceInfo>();
		ServiceInfo serviceInfo2 = new ServiceInfo();
		serviceInfo2.setShopId(5L);
		serviceInfo2.setServiceName("2测试服务名称");
		serviceInfo2.setServicePrice(new Double(11));
		serviceInfo2.setServicePriority(2L);
		serviceInfo2.setServiceDesc("2测试服务名称");
		serviceInfo2.setServiceContent("2测试服务名称");
		serviceInfo2.setServiceImgAddr("test.png");
		serviceInfoList.add(serviceInfo2);
		ServiceInfo serviceInfo3 = new ServiceInfo();
		serviceInfo3.setShopId(6L);
		serviceInfo3.setServiceName("A测试服务名称");
		serviceInfo3.setServicePrice(new Double(23));
		serviceInfo3.setServicePriority(2L);
		serviceInfo3.setServiceDesc("A测试服务名称");
		serviceInfo3.setServiceContent("A测试服务名称");
		serviceInfo3.setServiceImgAddr("test.png");
		serviceInfoList.add(serviceInfo3);
		ServiceInfo serviceInfo4 = new ServiceInfo();
		serviceInfo4.setShopId(7L);
		serviceInfo4.setServiceName("aa测试服务名称");
		serviceInfo4.setServicePrice(new Double(24));
		serviceInfo4.setServicePriority(1L);
		serviceInfo4.setServiceDesc("aa测试服务名称");
		serviceInfo4.setServiceContent("aa测试服务名称");
		serviceInfo4.setServiceImgAddr("test.png");
		serviceInfoList.add(serviceInfo4);
		int effectedNum = serviceDao.insertServiceInfo(serviceInfoList);
		assertEquals(3, effectedNum);
	}
	@Test
	@Ignore
	public void testUpdateService() {
		ServiceInfo serviceInfo = new ServiceInfo();
		serviceInfo.setServiceId(1L);
		serviceInfo.setServiceContent("测试内容");
		serviceInfo.setServiceDesc("测试描述");
		int effectedNum = serviceDao.updateService(serviceInfo);
		assertEquals(1, effectedNum);
	}
	
	@Test
	@Ignore
	public void testQueryByServiceId() {
		long serviceId = 1L;
		ServiceInfo serviceInfo = serviceDao.queryByServiceId(serviceId);
		System.out.println("servicecontent: "+serviceInfo.getServiceContent());
	}

	@Test
	@Ignore
	public void testDeleteService() {
		int effectedNum = serviceDao.deleteService(2L);
		assertEquals(1, effectedNum);
	}
}
