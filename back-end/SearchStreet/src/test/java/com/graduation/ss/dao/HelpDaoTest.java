package com.graduation.ss.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.graduation.ss.entity.Help;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelpDaoTest {
	@Autowired
	private HelpDao helpDao;

	@Test
	@Ignore
	public void testInsertHelp() {
		Help help = new Help();
		help.setAppealId(1L);
		help.setUserId(1L);
		help.setHelpStatus(0);
		help.setAvgAttitude(5.0f);
		help.setAvgCompletion(4.5f);
		help.setAvgEfficiency(3.3f);
		int effectedNum = helpDao.insertHelp(help);
		assertEquals(1, effectedNum);
	}

	@Test
	//@Ignore
	public void testQueryHelpListFYAndCount() {
		Help helpCondition = new Help();

		helpCondition.setAppealId(1L);
		List<Help> helpList = helpDao.queryHelpListFY(helpCondition, 3, 2);
		int count = helpDao.queryHelpCount(helpCondition);
		System.out.println("帮助列表-appealId的大小:" + helpList.size());
		System.out.println("帮助总数-appealID：" + count);
	}

	@Test
	@Ignore
	public void testQueryHelpList() {
		Help helpCondition = new Help();
		helpCondition.setUserId(1L);
		List<Help> helpList = helpDao.queryHelpList(helpCondition);
		System.out.println("帮助列表-userId的大小:" + helpList.size());
	}

	@Test
	@Ignore
	public void testUpdateHelp() {
		Help help = new Help();
		help.setHelpId(1L);
		help.setAdditionalCoin(1l);
		help.setAttitude(5);
		help.setCompletion(5);
		help.setEfficiency(5);
		help.setHelpStatus(3);
		int effectedNum = helpDao.updateHelp(help);
		assertEquals(1, effectedNum);
	}

	@Test
	@Ignore
	public void testQueryByHelpId() {
		long helpId = 1;
		Help help = helpDao.queryByHelpId(helpId);
		System.out.println(help.toString());
	}
}
