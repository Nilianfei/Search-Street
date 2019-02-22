package com.graduation.ss.service;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.graduation.ss.dto.HelpExecution;
import com.graduation.ss.entity.Help;
import com.graduation.ss.enums.HelpStateEnum;
import com.graduation.ss.exceptions.HelpOperationException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelpServiceTest {
	@Autowired
	private HelpService helpService;

	@Test
	@Ignore
	public void testGetHelpList() {
		Help helpCondition = new Help();
		helpCondition.setUserId(1l);
		//helpCondition.setHelpStatus(0);
		HelpExecution helpExecution = helpService.getHelpList(helpCondition);
		for(Help help:helpExecution.getHelpList()) {
			System.out.println(help.getAppealTitle());
		}
		System.out.println("帮助列表数为：" + helpExecution.getHelpList().size());
		System.out.println("帮助总数为：" + helpExecution.getCount());
	}

	@Test
	@Ignore
	public void testAddHelp() throws HelpOperationException {
		Help help = new Help();
		help.setAppealId(3l);
		help.setAppealTitle("appealTitle");
		help.setUserId(2l);
		HelpExecution helpExecution = helpService.addHelp(help);
		assertEquals(HelpStateEnum.SUCCESS.getState(), helpExecution.getState());
	}

	@Test
	@Ignore
	public void testModifyHelp() throws HelpOperationException {
		Help help = new Help();
		help.setHelpId(13l);
		help.setAdditionalCoin(2l);
		help.setAttitude(4);
		help.setCompletion(3);
		help.setEfficiency(5);
		help.setHelpStatus(3);
		HelpExecution helpExecution = helpService.modifyHelp(help);
		assertEquals(HelpStateEnum.SUCCESS.getState(), helpExecution.getState());
	}
	
	@Test
	@Ignore
	public void testSelectHelp()throws HelpOperationException{
		helpService.selectHelp(13l, 3l);
	}
}
