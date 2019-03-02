package com.graduation.ss.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.graduation.ss.dto.AppealExecution;
import com.graduation.ss.dto.ImageHolder;
import com.graduation.ss.entity.Appeal;
import com.graduation.ss.enums.AppealStateEnum;
import com.graduation.ss.exceptions.AppealOperationException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppealServiceTest {
	@Autowired
	private AppealService appealService;

	@Test
	@Ignore
	public void testGetNearbyAppealList() {
		AppealExecution ae = appealService.getNearbyAppealList(50, 0, 50, 0, null);
		List<Appeal> appealList  = ae.getAppealList();
		if (appealList.size() != 0) {
			for (Appeal appeal : appealList) {
				System.out.println(appeal);
			}
		} else
			System.out.println("没有符合条件的appeal");
	}

	@Test
	@Ignore
	public void testGetAppealList() {
		Appeal appealCondition = new Appeal();
		appealCondition.setAppealStatus(0);
		AppealExecution ae = appealService.getAppealList(appealCondition);
		System.out.println("求助列表数为:" + ae.getAppealList().size());
		System.out.println("求助总数为:" + ae.getCount());
	}

	@Test
	@Ignore
	public void testUploadImg() throws AppealOperationException, FileNotFoundException {
		File appealImg = new File("D:/test.jpg");
		InputStream is = new FileInputStream(appealImg);
		ImageHolder appealImageHolder = new ImageHolder(appealImg.getName(), is);
		appealService.uploadImg(3l, appealImageHolder);
	}

	@Test
	@Ignore
	public void testModifyAppeal() throws AppealOperationException {
		Appeal appeal = new Appeal();
		appeal.setAppealId(3l);
		appeal.setAppealTitle("modifyappealtitle");
		AppealExecution ae = appealService.modifyAppeal(appeal);
		System.out.println("新的求助标题为：" + ae.getAppeal().getAppealTitle());
	}

	@Test
	@Ignore
	public void testGetAppealById() throws AppealOperationException {
		Appeal appeal = appealService.getByAppealId(2L);
		System.out.println(appeal.getAppealStatus());
	}

	@Test
	@Ignore
	public void testAddAppeal() throws AppealOperationException {
		Appeal appeal = new Appeal();
		appeal.setAppealContent("testappealContent");
		appeal.setAppealMoreInfo("testappealmoreinfo");
		appeal.setAppealStatus(0);
		appeal.setAppealTitle("testappealtitle");
		appeal.setCity("testcity");
		appeal.setDistrict("testdistrict");
		appeal.setEndTime(new Date());
		appeal.setFullAddress("testfulladdress");
		appeal.setLatitude(11f);
		appeal.setLongitude(34f);
		appeal.setPhone("testphone");
		appeal.setProvince("testprovince");
		appeal.setSouCoin(1l);
		appeal.setStartTime(new Date());
		appeal.setEndTime(new Date());
		appeal.setUserId(1l);
		AppealExecution appealExecution = appealService.addAppeal(appeal);
		assertEquals(AppealStateEnum.SUCCESS.getState(), appealExecution.getState());
	}
	
	@Test
	@Ignore
	public void testCompleteAppeal() throws AppealOperationException{
		AppealExecution ae=appealService.completeAppeal(4l, 9l, 1l);
		System.out.println(ae.getStateInfo());
	}
	
	@Test
	@Ignore
	public void testAdditionSouCoin() throws AppealOperationException{
		appealService.additionSouCoin(9l, 1l, 1l);
	}
}
