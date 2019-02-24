package com.graduation.ss.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.graduation.ss.dto.LocalAuthExecution;
import com.graduation.ss.entity.LocalAuth;
import com.graduation.ss.enums.LocalAuthStateEnum;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LocalAuthServiceTest {
	@Autowired
	private LocalAuthService localAuthService;

	@Test
	public void testBindLocalAuth() {
		LocalAuth localAuth = new LocalAuth();
		localAuth.setUserId(13l);
		localAuth.setUserName("test");
		localAuth.setPassword("123456");
		LocalAuthExecution localAuthExecution = localAuthService.bindLocalAuth(localAuth);
		assertEquals(LocalAuthStateEnum.SUCCESS.getState(), localAuthExecution.getState());
	}
}
