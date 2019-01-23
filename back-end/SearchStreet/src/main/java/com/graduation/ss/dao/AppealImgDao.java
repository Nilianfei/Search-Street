package com.graduation.ss.dao;

import java.util.List;

import com.graduation.ss.entity.AppealImg;

public interface AppealImgDao {
	/**
	 * 列出某个求助的详情图列表
	 * 
	 * @param appealId
	 * @return
	 */
	List<AppealImg> getAppealImgList(long appealId);

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
}
