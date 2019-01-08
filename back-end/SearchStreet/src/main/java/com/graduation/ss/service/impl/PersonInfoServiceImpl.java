package com.graduation.ss.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.graduation.ss.dao.PersonInfoDao;
import com.graduation.ss.entity.PersonInfo;
import com.graduation.ss.service.PersonInfoService;

@Service
public class PersonInfoServiceImpl implements PersonInfoService{
	@Autowired
	private PersonInfoDao personInfoDao;

	@Override
	public PersonInfo getPersonInfoByUserId(Long userId) {
		return personInfoDao.queryPersonInfoByUserId(userId);
	}

}
