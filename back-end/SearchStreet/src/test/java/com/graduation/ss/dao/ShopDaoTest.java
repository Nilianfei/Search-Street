package com.graduation.ss.dao;

import static org.junit.Assert.assertEquals;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.graduation.ss.entity.Shop;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopDaoTest {
	@Autowired
	private ShopDao shopDao;
	
	@Test
	//@Ignore
	public void testQueryNearbyShopList() {
		List<Shop> shopList = shopDao.queryNearbyShopList(30, 20, 130, 100);
		if(shopList.size() != 0) {
			for (Shop shop : shopList) {
				System.out.println(shop);
			}
		}
		else System.out.println("没有符合条件的shop");
	}
	@Test
	@Ignore
	public void testQueryShopListAndCount() {
		Shop shopCondition = new Shop();
		
		shopCondition.setUserId(1L);
		List<Shop> shopList = shopDao.queryShopList(shopCondition, 0, 4);
		int count = shopDao.queryShopCount(shopCondition);
		System.out.println("店铺列表-user的大小：" + shopList.size());
		System.out.println("店铺总数-user：" + count);	
		
		shopCondition.setShopName("测试店铺");
		shopList = shopDao.queryShopList(shopCondition, 0, 3);
		count = shopDao.queryShopCount(shopCondition);
		System.out.println("店铺列表-name的大小：" + shopList.size());
		System.out.println("店铺总数-name：" + count);
		
		shopCondition.setProvince("测试省份1");
		shopList = shopDao.queryShopList(shopCondition, 0, 4);
		count = shopDao.queryShopCount(shopCondition);
		System.out.println("店铺列表-province的大小：" + shopList.size());
		System.out.println("店铺总数-province：" + count);	
		
		shopCondition.setEnableStatus(1);
		shopList = shopDao.queryShopList(shopCondition, 0, 3);
		count = shopDao.queryShopCount(shopCondition);
		System.out.println("店铺列表-enable的大小：" + shopList.size());
		System.out.println("店铺总数-enable：" + count);
	}
	
	@Test
	@Ignore
	public void testInsertShop() {
		Shop shop = new Shop();
		shop.setBusinessLicenseCode("test");
		shop.setBusinessLicenseImg("test");
		shop.setBusinessScope("test");
		shop.setCity("test");
		shop.setCloseTime(new Time(new Date().getTime()));
		shop.setOpenTime(new Time(new Date().getTime()));
		shop.setLatitude(1f);
		shop.setLongitude(1f);
		shop.setCreateTime(new Date());
		shop.setDistrict("test");
		shop.setFullAddress("test");
		shop.setIsMobile(1);
		shop.setLastEditTime(new Date());
		shop.setPhone("test");
		shop.setProfileImg("test");
		shop.setProvince("test");
		shop.setShopMoreInfo("test");
		shop.setShopName("test");
		shop.setUserId(1L);
		shop.setEnableStatus(0);
		int effectedNum = shopDao.insertShop(shop);
		assertEquals(1, effectedNum);
	}
	
	@Test
	@Ignore
	public void testUpdateShop() {
		Shop shop = new Shop();
		shop.setShopId(1L);
		shop.setFullAddress("测试地址");
		shop.setLastEditTime(new Date());
		shop.setShopMoreInfo("测试描述");
		int effectedNum = shopDao.updateShop(shop);
		assertEquals(1, effectedNum);
	}
	
	@Test
	@Ignore
	public void testQueryByShopId() {
		long shopId = 1;
		Shop shop = shopDao.queryByShopId(shopId);
		System.out.println("fullAddress: "+shop.getFullAddress());
	}
}
