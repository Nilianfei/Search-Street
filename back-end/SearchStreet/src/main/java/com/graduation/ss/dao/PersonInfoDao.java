package com.graduation.ss.dao;

import com.graduation.ss.entity.PersonInfo;

public interface PersonInfoDao {
	/**
	 * 通过用户Id查询用户
	 * 
	 * @param userId
	 * @return
	 */
	PersonInfo queryPersonInfoByUserId(long userId);

	/**
	 * 添加用户信息
	 * 
	 * @param personInfo
	 * @return
	 */
	int insertPersonInfo(PersonInfo personInfo);
	
	/**
	 * 修改用户信息
	 * 
	 * @param personInfo
	 * @return
	 */
	int updatePersonInfo(PersonInfo personInfo);
}
