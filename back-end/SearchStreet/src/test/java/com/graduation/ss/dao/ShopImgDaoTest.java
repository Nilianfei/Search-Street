package com.graduation.ss.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
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
	public void testABatchInsertShopImg() throws Exception {
		// ShopId为1的商品里添加两个详情图片记录
		ShopImg ShopImg1 = new ShopImg();
		ShopImg1.setImgAddr("图片1");
		ShopImg1.setCreateTime(new Date());
		ShopImg1.setShopId(1L);
		ShopImg ShopImg2 = new ShopImg();
		ShopImg2.setImgAddr("图片2");
		ShopImg2.setCreateTime(new Date());
		ShopImg2.setShopId(1L);
		List<ShopImg> ShopImgList = new ArrayList<ShopImg>();
		ShopImgList.add(ShopImg1);
		ShopImgList.add(ShopImg2);
		int effectedNum = ShopImgDao.batchInsertShopImg(ShopImgList);
		assertEquals(2, effectedNum);
	}

	@Test
	public void testBGetShopImgList() {
		// 检查ShopId为1的商品是否有且仅有两张商品详情图片
		List<ShopImg> ShopImgList = ShopImgDao.getShopImgList(1L);
		assertEquals(2, ShopImgList.size());
	}

	@Test
	@Ignore
	public void testCDeleteShopImgByShopId() throws Exception {
		// 删除新增的两条商品详情图片记录
		long ShopId = 1;
		int effectedNum = ShopImgDao.deleteShopImgByShopId(ShopId);
		assertEquals(2, effectedNum);
	}
}
