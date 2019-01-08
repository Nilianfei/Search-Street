package com.graduation.ss.service;

import com.graduation.ss.entity.PersonInfo;

public interface PersonInfoService {

	/**
	 * 根据用户Id获取personInfo信息
	 * 
	 * @param userId
	 * @return
	 */
	PersonInfo getPersonInfoByUserId(Long userId);
}
