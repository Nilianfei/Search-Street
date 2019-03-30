package com.graduation.ss.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
	public void testGetNearbyShopList() {
		ShopExecution se = shopService.getNearbyShopList(30, 20, 120, 100, null);
		List<Shop> shopList = se.getShopList();
		if (shopList.size() != 0) {
			for (Shop shop : shopList) {
				System.out.println(shop);
			}
		} else
			System.out.println("没有符合条件的shop");
	}

	@Test
	@Ignore
	public void testGetShopList() {
		Shop shopCondition = new Shop();
		shopCondition.setUserId(13L);
		ShopExecution se = shopService.getShopList(shopCondition, 2, 15);
		System.out.println("店铺列表数为：" + se.getShopList().size());
		System.out.println("店铺总数为：" + se.getCount());
	}

	@Test
	@Ignore
	public void testUploadImg() throws ShopOperationException, FileNotFoundException {
		File shopImg = new File("C:/Users/Al/Pictures/test.png");
		InputStream is = new FileInputStream(shopImg);
		ImageHolder shopImageHolder = new ImageHolder(shopImg.getName(), is);
		InputStream is1 = new FileInputStream(shopImg);
		ImageHolder profileImageHolder1 = new ImageHolder(shopImg.getName(), is1);
		/*
		 * InputStream is2 = new FileInputStream(shopImg); ImageHolder shopImageHolder2
		 * = new ImageHolder(shopImg.getName(), is2);
		 */
		ShopExecution shopExecution = shopService.uploadImg(2L, shopImageHolder, null, profileImageHolder1,new Date());
		System.out.println("新的头像地址为：" + shopExecution.getShop().getProfileImg());
	}

	@Test
	@Ignore
	public void testModifyShop() throws ShopOperationException {
		Shop shop = new Shop();
		shop.setShopId(2L);
		shop.setShopName("修改后的店铺名称");
		ShopExecution se = shopService.modifyShop(shop);
		System.out.println("新的店铺名称为：" + se.getShop().getShopName());
	}

	@Test
	@Ignore
	public void testAddShop() throws ShopOperationException {
		Shop shop = new Shop();
		shop.setBusinessLicenseCode("test");
		shop.setBusinessScope("test");
		shop.setCity("test");
		shop.setLatitude(1f);
		shop.setLongitude(1f);
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
		ShopExecution se = shopService.addShop(shop);
		assertEquals(ShopStateEnum.CHECK.getState(), se.getState());
	}
}
