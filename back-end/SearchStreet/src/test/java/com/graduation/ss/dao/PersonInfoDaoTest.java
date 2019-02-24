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

import com.graduation.ss.entity.PersonInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonInfoDaoTest {
	@Autowired
	private PersonInfoDao personInfoDao;
	
	@Test
	@Ignore
	public void testInsertPersonInfo() throws Exception {
		// 设置新增的用户信息
		PersonInfo personInfo = new PersonInfo();
		personInfo.setUserName("测试用户名");
		personInfo.setBirth(new Date());
		personInfo.setCreateTime(new Date());
		personInfo.setEmail("testemailaddress");
		personInfo.setEnableStatus(1);
		personInfo.setLastEditTime(new Date());
		personInfo.setPhone("testphone");
		personInfo.setProfileImg("test");
		personInfo.setSex("男");
		personInfo.setSouCoin(0L);
		personInfo.setUserType(0);
		int effectedNum = personInfoDao.insertPersonInfo(personInfo);
		assertEquals(1, effectedNum);
	}
	
	@Test
	@Ignore
	public void testQueryPersonInfoByUserId() {
		long userId = 1;
		// 查询Id为1的用户信息
		PersonInfo personInfo = personInfoDao.queryPersonInfoByUserId(userId);
		System.out.println(personInfo.getUserName());
	}
	
	@Test
	@Ignore
	public void testUpdatePersonInfo() {
		PersonInfo personInfo = new PersonInfo();
		personInfo.setUserId(1L);
		personInfo.setBirth(new Date());
		personInfo.setSouCoin(10000L);
		personInfo.setLastEditTime(new Date());
		int effectedNum = personInfoDao.updatePersonInfo(personInfo);
		assertEquals(1, effectedNum);
	}
	
	@Test
	public void testQueryPersonInfoList() {
		PersonInfo personInfo = new PersonInfo();
		personInfo.setEnableStatus(1);
		List<PersonInfo> personInfos=personInfoDao.queryPersonInfoList(personInfo, 0, 1);
		int count = personInfoDao.queryPersonInfoCount(personInfo);
		System.out.println(personInfos.size());
		System.out.println(count);
	}
}
