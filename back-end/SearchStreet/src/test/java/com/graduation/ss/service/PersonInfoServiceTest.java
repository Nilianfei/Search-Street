package com.graduation.ss.service;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.graduation.ss.dto.PersonInfoExecution;
import com.graduation.ss.entity.PersonInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonInfoServiceTest {
	@Autowired
	private PersonInfoService personInfoService;
	
	@Test
	//@Ignore
	public void testModifyPersonInfo() {
		PersonInfo personInfo = new PersonInfo();
		personInfo.setUserId(1L);
		personInfo.setEnableStatus(0);
		PersonInfoExecution personInfoExecution = personInfoService.modifyPersonInfo(personInfo,null);
		System.out.println(personInfoExecution.getPersonInfo().getUserId());
	}
	
	@Test
	@Ignore
	public void testGetPersonInfoList() {
		PersonInfo personInfo = new PersonInfo();
		personInfo.setEnableStatus(1);
		List<PersonInfo> personInfos=personInfoService.getPersonInfoList(personInfo, 0, 1).getPersonInfoList();
		System.out.println(personInfos.size());
	}
}
