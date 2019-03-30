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

import com.graduation.ss.entity.ShopComment;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopCommentDaoTest {
	@Autowired
	private ShopCommentDao shopCommentDao;
	
	@Test
	@Ignore
	public void testQueryServiceListAndCount() {
		ShopComment shopCommentCondition2 = new ShopComment();
		
		shopCommentCondition2.setShopId(2L);
		List<ShopComment> shopCommentList = shopCommentDao.queryShopCommentList(shopCommentCondition2, 0, 4);
		int count = shopCommentDao.queryShopCommentCount(shopCommentCondition2);
		System.out.println("评论列表-shopid的大小：" + shopCommentList.size());
		System.out.println("评论总数-shopid：" + count);	
		shopCommentList.clear();

		
	
		ShopComment shopCommentCondition = new ShopComment();
		shopCommentCondition.setUserId(1L);;
		shopCommentList = shopCommentDao.queryShopCommentList(shopCommentCondition, 0, 8);
		count = shopCommentDao.queryShopCommentCount(shopCommentCondition);
		System.out.println("评论列表-UserId的大小：" + shopCommentList.size());
		System.out.println("评论总数-UserId：" + count);
		for(int i=0;i<shopCommentList.size();i++)
			System.out.println("shopComment Content: "+shopCommentList.get(i).getCommentContent());
	}
	@Test
	@Ignore
	public void testInsertShopComment() {
		ShopComment shopComment = new ShopComment();
		shopComment.setShopId(1L);
		shopComment.setOrderId(1L);
		shopComment.setUserId(1L);
		shopComment.setCommentContent("test");
		shopComment.setServiceRating(5);
		shopComment.setStarRating(5);
		int effectedNum = shopCommentDao.insertShopComment(shopComment);
		assertEquals(1, effectedNum);
	}
	@Test
	@Ignore
	public void testInsertShopCommentInfo() {
		List<ShopComment>shopCommentList=new ArrayList<ShopComment>();
		ShopComment shopComment2 = new ShopComment();
		shopComment2.setShopId(2L);
		shopComment2.setOrderId(2L);
		shopComment2.setUserId(1L);
		shopComment2.setCommentContent("test2");    //DIFF SHOP
		shopComment2.setServiceRating(4);
		shopComment2.setStarRating(3);
		shopCommentList.add(shopComment2);
		ShopComment shopComment3 = new ShopComment();  //DIFF USER
		shopComment3.setShopId(1L);
		shopComment3.setOrderId(3L);
		shopComment3.setUserId(2L);
		shopComment3.setCommentContent("test_no_bad");
		shopComment3.setServiceRating(3);
		shopComment3.setStarRating(3);
		shopCommentList.add(shopComment3);
		ShopComment shopComment4 = new ShopComment();   //SECOND TIME ORDER
		shopComment4.setShopId(2L);
		shopComment4.setOrderId(4L);
		shopComment4.setUserId(1L);
		shopComment4.setCommentContent("test2_secondtime");
		shopComment4.setServiceRating(5);
		shopComment4.setStarRating(4);
		shopCommentList.add(shopComment4);
		int effectedNum = shopCommentDao.insertShopCommentInfo(shopCommentList);
		assertEquals(3, effectedNum);
	}
	@Test
	@Ignore
	public void testUpdateService() {
		ShopComment shopComment = new ShopComment();
		shopComment.setShopCommentId(1L);
		shopComment.setCommentContent("好评");
		int effectedNum = shopCommentDao.updateShopComment(shopComment);
		assertEquals(1, effectedNum);
	}
	
	@Test
	@Ignore
	public void testQueryByShopCommentId() {
		long shopCommentId = 1L;
		ShopComment shopComment = shopCommentDao.queryByShopCommentId(shopCommentId);
		System.out.println("shopComment Content: "+shopComment.getCommentContent());
	}
	@Test
	@Ignore
	public void testQueryByOrderId() {
		long orderId = 1L;
		ShopComment shopComment = shopCommentDao.queryByOrderId(orderId);
		System.out.println("shopComment Content: "+shopComment.getCommentContent());
	}
	@Test
	@Ignore
	public void testDeleteShopComment() {
		ShopComment shopComment = new ShopComment();
		shopComment.setShopCommentId(2L);
		int effectedNum = shopCommentDao.deleteShopComment(shopComment);
		assertEquals(1, effectedNum);
	}

}
