package com.graduation.ss.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.graduation.ss.entity.ServiceImg;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServiceImgDaoTest {
	@Autowired
	private ServiceImgDao ServiceImgDao;
	
	@Test
	@Ignore
	public void testInsertServiceImg() throws Exception {
		Date date=new Date();
		// ServiceId为1的商品里添加图片记录
		ServiceImg ServiceImg1 = new ServiceImg();
		ServiceImg1.setImgAddr("E:/SearchStreet/1.png");
		ServiceImg1.setCreateTime(date);
		ServiceImg1.setServiceId(1L);
	
		int effectedNum = ServiceImgDao.insertServiceImg(ServiceImg1);
//		    ServiceImg ServiceImg2 = new ServiceImg();
//			ServiceImg2.setImgAddr("E:/SearchStreet/2.png");
//			ServiceImg2.setCreateTime(date);
//			ServiceImg2.setServiceId(2L);
//		
//			int effectedNum = ServiceImgDao.insertServiceImg(ServiceImg2);
	/*
			ServiceImg ServiceImg3 = new ServiceImg();
			ServiceImg3.setImgAddr("E:/SearchStreet/3.png");
			ServiceImg3.setCreateTime(date);
			ServiceImg3.setServiceId(3L);
		
			int effectedNum = ServiceImgDao.insertServiceImg(ServiceImg3);
		 */
		assertEquals(1, effectedNum);

	}

	@Test
	@Ignore
	public void testGetServiceImg() {
		// 检查ServiceId为1的商品是否有服务图片
		ServiceImg serviceImg = ServiceImgDao.getServiceImg(1L);
		int effectedNum =0;
		if(serviceImg!=null)
			effectedNum=1;
		assertEquals(1, effectedNum);
	}

	@Test
	@Ignore
	public void testUpdateServiceImg() {
		// 更新
		ServiceImg serviceImg=ServiceImgDao.getServiceImg(1L);
		serviceImg.setImgAddr("E:/SearchStreet/2.png");
		int effectedNum = ServiceImgDao.updateServiceImg(serviceImg);
		assertEquals(1, effectedNum);
	}
	@Test
	@Ignore
	public void testDeleteServiceImg() throws Exception {
		Date date=new Date();
		// 删除服务详情图片记录
		long ServiceId = 1;
		int effectedNum = ServiceImgDao.deleteServiceImg(ServiceId);
		System.out.println(date);
		assertEquals(1, effectedNum);
	}
}
