package com.graduation.ss.service;

import com.graduation.ss.dto.AppealExecution;
import com.graduation.ss.dto.ImageHolder;
import com.graduation.ss.entity.Appeal;
import com.graduation.ss.exceptions.AppealOperationException;

public interface AppealService {
	/**
	 * 根据appealCondition返回相应求助列表
	 * 
	 * @param appealCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public AppealExecution getAppealListFY(Appeal appealCondition, int pageIndex, int pageSize)
			throws AppealOperationException;

	/**
	 * 根据经纬范围返回附近求助列表
	 * 
	 * @param maxlat
	 * @param minlat
	 * @param maxlng
	 * @param minlng
	 * @return
	 */
	public AppealExecution getNearbyAppealList(float maxlat, float minlat, float maxlng, float minlng,
			String appealTitle);

	/**
	 * 通过求助Id获取求助信息
	 * 
	 * @param appealId
	 * @return
	 */
	Appeal getByAppealId(Long appealId) throws AppealOperationException;

	/**
	 * 更新求助信息，不包括对图片的处理
	 * 
	 * @param appeal
	 * @param appealImg
	 * @return
	 * @throws AppealOperationException
	 */
	AppealExecution modifyAppeal(Appeal appeal) throws AppealOperationException;

	/**
	 * 上传求助图片
	 * 
	 * @param appealId
	 * @param appealImg
	 * @return
	 * @throws AppealOperationException
	 */
	void uploadImg(long appealId, ImageHolder appealImg) throws AppealOperationException;

	/**
	 * 添加求助信息，不包括对图片的处理
	 * 
	 * @param appeal
	 * @param appealImgInputStream
	 * @param fileName
	 * @return
	 * @throws AppealOperationException
	 */
	AppealExecution addAppeal(Appeal appeal) throws AppealOperationException;

	/**
	 * 求助完成之后的相关操作
	 * 
	 * @param appealId
	 * @param helpId
	 * @param appealUserId
	 * @return
	 */
	AppealExecution completeAppeal(Long appealId, Long helpId, Long appealUserId) throws AppealOperationException;

	/**
	 * 撤销求助
	 * 
	 * @param userId
	 * @param appealId
	 * @throws AppealOperationException
	 */
	AppealExecution cancelAppeal(Long userId, Long appealId) throws AppealOperationException;

	/**
	 * 使求助失效
	 * 
	 * @param userId
	 * @param appealId
	 * @throws AppealOperationException
	 */
	AppealExecution disableAppeal(Long userId, Long appealId) throws AppealOperationException;
}
