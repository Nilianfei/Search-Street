package com.graduation.ss.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.graduation.ss.entity.WechatAuth;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatAuthDaoTest {
	@Autowired
	private WechatAuthDao wechatAuthDao;
	private static final String openid="testopenid";

	@Test
	public void testInsertWechatAuth() {
		WechatAuth wechatAuth = new WechatAuth();
		wechatAuth.setOpenId(openid);
		wechatAuth.setCreateTime(new Date());
		wechatAuth.setUserId(1L);
		int effectedNum = wechatAuthDao.insertWechatAuth(wechatAuth);
		assertEquals(1, effectedNum);
	}
	
	@Test
	public void testQueryWechatByOpenId() {
		WechatAuth wechatAuth = wechatAuthDao.queryWechatByOpenId(openid);
		assertEquals("1", wechatAuth.getUserId().toString());
	}
}
