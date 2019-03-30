package com.graduation.ss.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.graduation.ss.entity.WechatAuth;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatAuthServiceTest {
	@Autowired
	private WechatAuthService wechatAuthService;
	
	@Test
	public void testGetWechatAuthByOpenId() {
		WechatAuth wechatAuth = wechatAuthService.getWechatAuthByOpenId("testopenid");
		System.out.println(wechatAuth.getUserId());
	}
}
