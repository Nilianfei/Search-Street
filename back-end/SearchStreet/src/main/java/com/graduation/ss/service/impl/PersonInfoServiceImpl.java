package com.graduation.ss.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.graduation.ss.dao.PersonInfoDao;
import com.graduation.ss.dto.PersonInfoExecution;
import com.graduation.ss.entity.PersonInfo;
import com.graduation.ss.enums.PersonInfoStateEnum;
import com.graduation.ss.exceptions.PersonInfoOperationException;
import com.graduation.ss.service.PersonInfoService;

@Service
public class PersonInfoServiceImpl implements PersonInfoService {
	@Autowired
	private PersonInfoDao personInfoDao;

	@Override
	public PersonInfo getPersonInfoByUserId(Long userId) {
		return personInfoDao.queryPersonInfoByUserId(userId);
	}

	@Override
	public PersonInfoExecution modifyPersonInfo(PersonInfo personInfo) {
		// 空值判断
		if (personInfo == null) {
			return new PersonInfoExecution(PersonInfoStateEnum.NULL_PERSONINFO);
		}
		try {
			personInfo.setLastEditTime(new Date());
			// 修改个人信息
			int effectedNum = personInfoDao.updatePersonInfo(personInfo);
			if (effectedNum <= 0) {
				throw new PersonInfoOperationException("个人信息修改失败");
			}
		} catch (Exception e) {
			throw new PersonInfoOperationException("modifyPersonInfo error:" + e.getMessage());
		}
		return new PersonInfoExecution(PersonInfoStateEnum.SUCCESS, personInfo);
	}

}
