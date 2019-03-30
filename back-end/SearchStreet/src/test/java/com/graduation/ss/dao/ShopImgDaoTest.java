package com.graduation.ss.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.graduation.ss.entity.ShopImg;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShopImgDaoTest {
	@Autowired
	private ShopImgDao ShopImgDao;
	
	@Test
	public void testAInsertShopImg() throws Exception {
		Date date=new Date();
		// ShopId为1的商品里添加两个详情图片记录
		ShopImg ShopImg1 = new ShopImg();
		ShopImg1.setImgAddr("图片1");
		ShopImg1.setCreateTime(date);
		ShopImg1.setShopId(1L);
		ShopImg ShopImg2 = new ShopImg();
		ShopImg2.setImgAddr("图片2");
		ShopImg2.setCreateTime(date);
		ShopImg2.setShopId(1L);
		int effectedNum = ShopImgDao.insertShopImg(ShopImg1);
		assertEquals(1, effectedNum);
		effectedNum = ShopImgDao.insertShopImg(ShopImg2);
	}

	@Test
	@Ignore
	public void testBGetShopImgList() {
		// 检查ShopId为1的商品是否有且仅有两张商品详情图片
		List<ShopImg> ShopImgList = ShopImgDao.getShopImgList(1L);
		assertEquals(2, ShopImgList.size());
	}

	@Test
	@Ignore
	public void testCDeleteShopImgByShopId() throws Exception {
		Date date=new Date();
		// 删除新增的两条商品详情图片记录
		long ShopId = 1;
		int effectedNum = ShopImgDao.deleteShopImgByShopIdAndCreateTime(ShopId, date);
		System.out.println(date);
		assertEquals(2, effectedNum);
	}
}
