package com.graduation.ss.service;

import com.graduation.ss.dto.ImageHolder;
import com.graduation.ss.dto.PersonInfoExecution;
import com.graduation.ss.entity.PersonInfo;
import com.graduation.ss.exceptions.PersonInfoOperationException;

public interface PersonInfoService {

	/**
	 * 根据用户Id获取personInfo信息
	 * 
	 * @param userId
	 * @return
	 */
	PersonInfo getPersonInfoByUserId(long userId);
	
	/**
	 * 根据查询条件分页返回用户信息列表
	 * 
	 * @param personInfoCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	PersonInfoExecution getPersonInfoList(PersonInfo personInfoCondition, int pageIndex, int pageSize);
	
	/**
	 * 修改用户信息
	 * 
	 * @param personInfo
	 * @return
	 */
	PersonInfoExecution modifyPersonInfo(PersonInfo personInfo, ImageHolder thumbnail) throws PersonInfoOperationException;
	
	/**
	 * 添加用户信息
	 * 
	 * @param personInfo
	 * @param thumbnail
	 * @return
	 * @throws PersonInfoOperationException
	 */
	PersonInfoExecution addPersonInfo(PersonInfo personInfo, ImageHolder thumbnail) throws PersonInfoOperationException;
}
