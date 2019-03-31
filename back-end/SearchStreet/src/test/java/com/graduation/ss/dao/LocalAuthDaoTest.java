package com.graduation.ss.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.graduation.ss.entity.LocalAuth;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LocalAuthDaoTest {
	@Autowired
	private LocalAuthDao localAuthDao;
	private static final String username = "testusername";
	private static final String password = "testpassword";

	@Test
	@Ignore
	public void testInsertLocalAuth() {
		LocalAuth localAuth = new LocalAuth();
		localAuth.setUserName(username);
		localAuth.setPassword(password);
		localAuth.setCreateTime(new Date());
		localAuth.setUserId(1L);
		int effectedNum = localAuthDao.insertLocalAuth(localAuth);
		assertEquals(1, effectedNum);
	}

	@Test
	@Ignore
	public void testQueryLocalByUserNameAndPwd() {
		LocalAuth localAuth = localAuthDao.queryLocalByUserNameAndPwd(username, password);
		assertEquals("1", localAuth.getUserId().toString());
	}

	@Test
	@Ignore
	public void testQueryLocalByUserId() {
		LocalAuth localAuth = localAuthDao.queryLocalByUserId(1L);
		assertEquals("1", localAuth.getUserId().toString());
	}

	@Test
	@Ignore
	public void testUpdateLocalAuth() {
		Date now = new Date();
		int effectedNum = localAuthDao.updateLocalAuth(1L, username, password, password + "new", now);
		assertEquals(1, effectedNum);
		LocalAuth localAuth = localAuthDao.queryLocalByUserId(1L);
		System.out.println(localAuth.getPassword());
	}
	
	@Test
	@Ignore
	public void testQueryUserName() {
		List<String> userNameList=localAuthDao.queryUserName();
		for(String userName:userNameList) {
			System.out.println(userName);
		}
	}
}
