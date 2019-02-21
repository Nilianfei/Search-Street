package com.graduation.ss.service;

import com.graduation.ss.dto.PersonInfoExecution;
import com.graduation.ss.entity.PersonInfo;

public interface PersonInfoService {

	/**
	 * 根据用户Id获取personInfo信息
	 * 
	 * @param userId
	 * @return
	 */
	PersonInfo getPersonInfoByUserId(Long userId);
	
	/**
	 * 修改用户信息
	 * 
	 * @param personInfo
	 * @return
	 */
	PersonInfoExecution modifyPersonInfo(PersonInfo personInfo);
}
