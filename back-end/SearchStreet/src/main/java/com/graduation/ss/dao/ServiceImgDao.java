package com.graduation.ss.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.graduation.ss.entity.ServiceImg;

public interface ServiceImgDao {
	public List<ServiceImg> queryServiceImg(@Param("serviceImgCondition")ServiceImg serviceImgCondition, @Param("rowIndex") int rowIndex,
			@Param("pageSize") int pageSize);
	/**
	 * 列出某个店铺的某服务图
	 * 
	 * @param serviceId
	 * @return
	 */
	public ServiceImg getServiceImg(long serviceId);
	/**
	 * 添加服务图片
	 * 
	 * @param serviceImg
	 * @return
	 */
	public int insertServiceImg(ServiceImg serviceImg);

	/**
	 * 更改服务图片
	 * 
	 * @param serviceImg
	 * @return
	 */
	public int updateServiceImg(ServiceImg serviceImg);

	/**
	 * 删除服务图片
	 * 
	 * @param shopId
	 * @return
	 */
	public int deleteServiceImg(long serviceId);
}
