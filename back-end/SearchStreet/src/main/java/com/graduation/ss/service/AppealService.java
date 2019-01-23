package com.graduation.ss.service;

import com.graduation.ss.dto.AppealExecution;
import com.graduation.ss.dto.ImageHolder;
import com.graduation.ss.entity.Appeal;
import com.graduation.ss.exceptions.AppealOperationException;

public interface AppealService {
	/**
	 * 根据appealCondition分页返回相应求助列表
	 * 
	 * @param appealCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public AppealExecution getAppealList(Appeal appealCondition, int pageIndex, int pageSize);

	/**
	 * 根据经纬范围返回附近求助列表
	 * 
	 * @param maxlat
	 * @param minlat
	 * @param maxlng
	 * @param minlng
	 * @return
	 */
	public AppealExecution getNearbyAppealList(float maxlat, float minlat, float maxlng, float minlng);

	/**
	 * 通过求助Id获取求助信息
	 * 
	 * @param appealId
	 * @return
	 */
	Appeal getByAppealId(long appealId);

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
}
