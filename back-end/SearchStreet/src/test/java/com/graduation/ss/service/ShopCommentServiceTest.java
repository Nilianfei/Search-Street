package com.graduation.ss.service;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.graduation.ss.dto.ShopCommentExecution;
import com.graduation.ss.entity.ShopComment;
import com.graduation.ss.enums.ShopCommentStateEnum;
import com.graduation.ss.exceptions.ShopCommentOperationException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopCommentServiceTest {
	@Autowired
	private ShopCommentService shopCommentService;
	@Test
	@Ignore
	public void testGetShopCommentList() {
		ShopComment shopCommentCondition = new ShopComment();
		shopCommentCondition.setCommentContent("测试shopComment内容");
		ShopCommentExecution se =  shopCommentService.getShopCommentList(shopCommentCondition, 3, 2);
		System.out.println("评论列表数为：" + se.getShopCommentList().size());
		System.out.println("评论总数为：" + se.getCount());
	}
	@Test
	@Ignore
	public void testGetByShopId() {
		ShopCommentExecution se = shopCommentService.getByShopId(2L, 3, 2);
		System.out.println("评论列表数为：" + se.getShopCommentList().size());
		System.out.println("评论总数为：" + se.getCount());
		/*
		ShopComment shopCommentCondition = new ShopComment();
		shopCommentCondition.setShopId(2L);
		ShopCommentExecution se = shopCommentService.getShopCommentList(shopCommentCondition, 3, 2);
		System.out.println("评论列表数为：" + se.getShopCommentList().size());
		System.out.println("评论总数为：" + se.getCount());
		*/
	}
	@Test
	@Ignore
	public void testGetByUserId() {
		ShopCommentExecution se = shopCommentService.getByUserId(2L, 3, 2);
		System.out.println("评论列表数为：" + se.getShopCommentList().size());
		System.out.println("评论总数为：" + se.getCount());
		/*
		ShopComment shopCommentCondition = new ShopComment();
		shopCommentCondition.setUserId(2L);
		ShopCommentExecution se = shopCommentService.getShopCommentList(shopCommentCondition, 3, 2);
		System.out.println("评论列表数为：" + se.getShopCommentList().size());
		System.out.println("评论总数为：" + se.getCount());
		*/
	}
	@Test
	@Ignore
	public void testGetByShopCommentId() {
		long shopCommentId=1L;
		ShopComment shopComment=  shopCommentService.getByShopCommentId(shopCommentId);
		System.out.println("评论内容为：" + shopComment.getCommentContent());
	}

	

	@Test
	@Ignore
	public void testModifyShopComment() throws ShopCommentOperationException {
		ShopComment shopComment = new ShopComment();
		shopComment.setShopCommentId(2L);
		shopComment.setCommentContent("修改后的评论内容");
		ShopCommentExecution se = shopCommentService.modifyShopComment(shopComment);
		System.out.println("新的评论内容为：" + se.getShopComment().getCommentContent());
	}

	@Test
	@Ignore
	public void testAddShopComment() throws ShopCommentOperationException {
		ShopComment shopComment = new ShopComment();
//		shopComment.setCommentContent("测试shopComment内容");
//        shopComment.setShopId(1L);
//        shopComment.setUserId(1L);
//        shopComment.setStarRating(5);
//        shopComment.setServiceRating(100);
//        shopComment.setCommentContent("测试shopComment内容2");
//        shopComment.setShopId(1L);
//        shopComment.setUserId(2L);
//        shopComment.setStarRating(4);
//        shopComment.setServiceRating(90);
        shopComment.setCommentContent("测试shopComment内容3");
        shopComment.setShopId(2L);
        shopComment.setUserId(1L);
        shopComment.setStarRating(4);
        shopComment.setServiceRating(100);
		ShopCommentExecution se =  shopCommentService.addShopComment(shopComment);
		assertEquals(ShopCommentStateEnum.SUCCESS.getState(), se.getState());
	}
	@Test
	@Ignore
	public void testDeleteShopComment() throws ShopCommentOperationException {
		ShopCommentExecution se =  shopCommentService.deleteShopComment(2L);
		assertEquals(ShopCommentStateEnum.SUCCESS.getState(), se.getState());
	}
}
