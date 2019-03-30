package com.graduation.ss.service;

import java.util.Date;

import com.graduation.ss.dto.ImageHolder;
import com.graduation.ss.dto.ServiceExecution;
import com.graduation.ss.exceptions.ServiceOperationException;
import com.graduation.ss.entity.ServiceInfo;

public interface SService {
	/**
	 * 根据serviceCondition分页返回相应服务列表
	 * 
	 * @param serviceCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public ServiceExecution getServiceList(ServiceInfo serviceCondition, int pageIndex, int pageSize);

	/**
	 * 通过店铺Id获取服务信息
	 * 
	 * @param shopId
	 * @return
	 */
	public ServiceExecution getByShopId(long shopId, int pageIndex, int pageSize);
	public ServiceExecution getByShopId2(long shopId);
	/**
	 * 通过服务Id获取服务信息
	 * 
	 * @param serviceId
	 * @return
	 */
	public ServiceInfo getByServiceId(long serviceId);
	/**
	 * 更新服务信息，不包括对图片的处理
	 * 
	 * @param service
	 * 
	 * @return
	 * @throws ServiceOperationException
	 */
	public ServiceExecution modifyService(ServiceInfo service) throws ServiceOperationException;

	/**
	 * 上传服务图片
	 * 
	 * @param serviceId
	 * @param serviceImg
	 * 
	 * 
	 * @return
	 * @throws ServiceOperationException
	 */
	public ServiceExecution uploadImg(long serviceId, ImageHolder serviceImg,Date createTime)
			throws ServiceOperationException;

	/**
	 * 添加服务信息，不包括对图片的处理
	 * 
	 * @param service
	 * @param serviceImgInputStream
	 * @return
	 * @throws ServiceOperationException
	 */
	public ServiceExecution addService(ServiceInfo service) throws ServiceOperationException;
	/**
	 * 删除服务信息
	 * 
	 * @param service
	 * @return
	 * @throws ServiceOperationException
	 */
	public ServiceExecution deleteService(long serviceId) throws ServiceOperationException;
}
