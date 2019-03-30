package com.graduation.ss.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.graduation.ss.entity.AppealImg;

public interface AppealImgDao {

	/**
	 * 添加求助详情图片
	 * 
	 * @param appealImg
	 * @return
	 */
	int insertAppealImg(AppealImg appealImg);

	/**
	 * 删除指定求助下的所有详情图
	 * 
	 * @param appealId
	 * @return
	 */
	int deleteAppealImgByAppealId(long appealId);

	/**
	 * 获取求助图片
	 * 
	 * @param appealImgId
	 * @return
	 */
	AppealImg getAppealImg(long appealImgId);

	/**
	 * 删除指定求助图片
	 * 
	 * @param appealImgId
	 * @return
	 */
	int deleteAppealImgById(long appealImgId);

	/**
	 * 获取求助图片总数
	 * 
	 * @param appealImgCondition
	 * @return
	 */
	int queryAppealImgCount(@Param("appealImgCondition") AppealImg appealImgCondition);

	/**
	 * 分页查询求助图片
	 * 
	 * @param appealImgCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	List<AppealImg> queryAppealImgList(@Param("appealImgCondition") AppealImg appealImgCondition,
			@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);
}
