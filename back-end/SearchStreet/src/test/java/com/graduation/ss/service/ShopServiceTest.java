package com.graduation.ss.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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

import com.graduation.ss.dto.ImageHolder;
import com.graduation.ss.dto.ShopExecution;
import com.graduation.ss.entity.Shop;
import com.graduation.ss.enums.ShopStateEnum;
import com.graduation.ss.exceptions.ShopOperationException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopServiceTest {
	@Autowired
	private ShopService shopService;
	
	@Test
	@Ignore
	public void testGetShopList() {
		Shop shopCondition = new Shop();
		shopCondition.setProvince("测试省份1");
		ShopExecution se = shopService.getShopList(shopCondition, 3, 2);
		System.out.println("店铺列表数为：" + se.getShopList().size());
		System.out.println("店铺总数为：" + se.getCount());
	}
	
	@Test
	@Ignore
	public void testModifyShop() throws ShopOperationException, FileNotFoundException {
		Shop shop = new Shop();
		shop.setShopId(2L);
		shop.setShopName("修改后的店铺名称");
		File shopImg = new File("C:/Users/Al/Pictures/test.png");
		InputStream is = new FileInputStream(shopImg);
		List<ImageHolder> shopImageHolderList = new ArrayList<ImageHolder>();
		shopImageHolderList.add(new ImageHolder(shopImg.getName(), is));
		InputStream is1 = new FileInputStream(shopImg);
		ImageHolder profileImageHolder1 = new ImageHolder(shopImg.getName(), is1);
		/*InputStream is2 = new FileInputStream(shopImg);
		ImageHolder shopImageHolder2 = new ImageHolder(shopImg.getName(), is2);*/
		ShopExecution shopExecution = shopService.modifyShop(shop, shopImageHolderList, null, profileImageHolder1);
		System.out.println("新的头像地址为：" + shopExecution.getShop().getProfileImg());
	}
	
	@Test
	public void testAddShop() throws ShopOperationException, FileNotFoundException {
		Shop shop = new Shop();
		shop.setBusinessLicenseCode("test");
		shop.setBusinessScope("test");
		shop.setCity("test");
		shop.setCloseTime(new Time(new Date().getTime()));
		shop.setOpenTime(new Time(new Date().getTime()));
		shop.setCoordinateX(1f);
		shop.setCoordinateY(1f);
		shop.setCreateTime(new Date());
		shop.setDistrict("test");
		shop.setFullAddress("test");
		shop.setIsMobile(1);
		shop.setLastEditTime(new Date());
		shop.setPhone("test");
		shop.setProvince("test");
		shop.setShopMoreInfo("test");
		shop.setShopName("测试service店铺");
		shop.setUserId(1L);
		shop.setEnableStatus(0);
		File shopImg = new File("C:/Users/Al/Pictures/test.png");
		InputStream is = new FileInputStream(shopImg);
		File shopImg1 = new File("C:/Users/Al/Pictures/timg.jpg");
		InputStream is1 = new FileInputStream(shopImg1);
		List<ImageHolder> shopImgList = new ArrayList<ImageHolder>();
		shopImgList.add(new ImageHolder(shopImg.getName(), is));
		shopImgList.add(new ImageHolder(shopImg1.getName(), is1));
		InputStream is2 = new FileInputStream(shopImg);
		ImageHolder businessImgHolder = new ImageHolder(shopImg.getName(), is2);
		ShopExecution se = shopService.addShop(shop, shopImgList, businessImgHolder, null );
		assertEquals(ShopStateEnum.CHECK.getState(), se.getState());
	}
}
