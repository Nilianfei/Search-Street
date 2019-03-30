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

import com.graduation.ss.entity.Appeal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppealDaoTest {
	@Autowired
	private AppealDao appealDao;

	@Test
	@Ignore
	public void testInsertAppeal() {
		Appeal appeal = new Appeal();
		appeal.setDistrict("testdistrict");
		appeal.setAppealContent("testContent");
		appeal.setCity("test");
		appeal.setLatitude(1f);
		appeal.setLongitude(1f);
		appeal.setDistrict("test");
		appeal.setFullAddress("test");
		appeal.setPhone("test");
		appeal.setProvince("测试省份");
		appeal.setAppealMoreInfo("test");
		appeal.setAppealTitle("测试求助");
		appeal.setUserId(1L);
		appeal.setAppealStatus(1);
		appeal.setSouCoin(100L);
		appeal.setStartTime(new Date());
		appeal.setEndTime(new Date());
		int effectedNum = appealDao.insertAppeal(appeal);
		assertEquals(1, effectedNum);
	}

	@Test
	//@Ignore
	public void testQueryAppealListAndCount() {
		Appeal appealCondition = new Appeal();

		appealCondition.setUserId(1L);
		List<Appeal> appealList = appealDao.queryAppealListFY(appealCondition, 0, 5);
		int count = appealDao.queryAppealCount(appealCondition);
		System.out.println("求助列表-user的大小：" + appealList.size());
		System.out.println("求助总数-user：" + count);
	}

	@Test
	@Ignore
	public void testUpdateAppeal() {
		Appeal appeal = new Appeal();
		appeal.setAppealId(1L);
		appeal.setFullAddress("测试地址");
		appeal.setAppealMoreInfo("测试补充地址");
		int effectedNum = appealDao.updateAppeal(appeal);
		assertEquals(1, effectedNum);
	}

	@Test
	@Ignore
	public void testQueryByAppealId() {
		long appealId = 1;
		Appeal appeal = appealDao.queryByAppealId(appealId);
		System.out.println("fullAddress: " + appeal.getFullAddress());
	}

	@Test
	@Ignore
	public void testQueryNearbyAppealList() {
		List<Appeal> appealList = appealDao.queryNearbyAppealList(2, 0, 2, 0, "测试");
		if (appealList.size() != 0) {
			for (Appeal appeal : appealList) {
				System.out.println(appeal);
			}
		} else
			System.out.println("没有符合条件的appeal");
	}
	
	@Test
	@Ignore
	public void testQueryAppealList() {
		Appeal appeal = new Appeal();
		appeal.setUserId(2L);
		List<Appeal> appealList = appealDao.queryAppealList(appeal);
		System.out.println(appealList==null);
	}
}
