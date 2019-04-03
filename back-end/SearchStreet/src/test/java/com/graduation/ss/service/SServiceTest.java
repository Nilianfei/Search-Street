package com.graduation.ss.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.graduation.ss.dao.ServiceImgDao;
import com.graduation.ss.dto.ImageHolder;
import com.graduation.ss.dto.ServiceExecution;
import com.graduation.ss.entity.ServiceImg;
import com.graduation.ss.entity.ServiceInfo;
import com.graduation.ss.enums.ServiceStateEnum;
import com.graduation.ss.exceptions.ServiceOperationException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SServiceTest {
	@Autowired
	private SService sService;
	@Autowired
	private ServiceImgDao serviceImgDao;

	@Test
	@Ignore
	public void testGetServiceList() {
		ServiceInfo serviceCondition = new ServiceInfo();
		serviceCondition.setServiceName("测试service店铺");
		ServiceExecution se = sService.getServiceList(serviceCondition, 3, 2);
		System.out.println("服务列表数为：" + se.getServiceList().size());
		System.out.println("服务总数为：" + se.getCount());
	}
	@Test
	@Ignore
	public void testGetByShopId() {
		ServiceExecution se = sService.getByShopId(2L, 3, 2);
		System.out.println("服务列表数为：" + se.getServiceList().size());
		System.out.println("服务总数为：" + se.getCount());
		/*
		ServiceInfo serviceCondition = new ServiceInfo();
		serviceCondition.setShopId(2L);
		ServiceExecution se = sService.getServiceList(serviceCondition, 3, 2);
		System.out.println("服务列表数为：" + se.getServiceList().size());
		System.out.println("服务总数为：" + se.getCount());
		*/
	}
	@Test
	@Ignore
	public void testGetByServiceId() {
		long serviceId=1L;
		ServiceInfo service= sService.getByServiceId(serviceId);
		System.out.println("服务名称为：" + service.getServiceName());
	}

	@Test
	@Ignore
	public void testUploadImg() throws ServiceOperationException, FileNotFoundException {
		File serviceImg = new File("E:/SearchStreet/1.png");
		InputStream is = new FileInputStream(serviceImg);
		ImageHolder serviceImageHolder = new ImageHolder(serviceImg.getName(), is);
		@SuppressWarnings("unused")
		ServiceExecution serviceExecution = sService.uploadImg(1L, serviceImageHolder,new Date());
		
		ServiceImg sImg=serviceImgDao.getServiceImg(1L);
		System.out.println("新的服务地址为：" + sImg.getImgAddr());
		//并没有更新到service数据库中
		//System.out.println("新的服务地址为：" + serviceExecution.getService().getServiceImgAddr());
	}

	@Test
	@Ignore
	public void testModifyService() throws ServiceOperationException {
		ServiceInfo service = new ServiceInfo();
		service.setServiceId(2L);
		service.setServiceName("修改后的服务名称");
		ServiceExecution se = sService.modifyService(service);
		System.out.println("新的服务名称为：" + se.getServiceInfo().getServiceName());
	}

	@Test
	@Ignore
	public void testAddService() throws ServiceOperationException {
		ServiceInfo service = new ServiceInfo();
		service.setServiceName("测试service店铺4");
        service.setShopId(3L);
        service.setServicePrice(new Double(12));
		ServiceExecution se = sService.addService(service);
		assertEquals(ServiceStateEnum.SUCCESS.getState(), se.getState());
	}
	@Test
	@Ignore
	public void testDeleteService() throws ServiceOperationException {
		ServiceExecution se = sService.deleteService(2L);
		assertEquals(ServiceStateEnum.SUCCESS.getState(), se.getState());
	}
}
