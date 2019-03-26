package com.graduation.ss.dao;

import com.graduation.ss.entity.ServiceImg;

public interface ServiceImgDao {
	/**
	 * 列出某个店铺的某服务图
	 * 
	 * @param serviceId
	 * @return
	 */
	ServiceImg getServiceImg(long serviceId);
	/**
	 * 添加服务图片
	 * 
	 * @param serviceImg
	 * @return
	 */
	int insertServiceImg(ServiceImg serviceImg);

	/**
	 * 更改服务图片
	 * 
	 * @param serviceImg
	 * @return
	 */
	int updateServiceImg(ServiceImg serviceImg);

	/**
	 * 删除服务图片
	 * 
	 * @param shopId
	 * @return
	 */
	int deleteServiceImg(long serviceId);
}
