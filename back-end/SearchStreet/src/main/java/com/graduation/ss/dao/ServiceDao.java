package com.graduation.ss.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.graduation.ss.entity.ServiceInfo;;

public interface ServiceDao{
	/**
	 * 分页查询服务，可输入的条件有：服务名（模糊），店铺id，服务价格
	 * 
	 * @param serviceCondition
	 * @param rowIndex
	 *            从第几行开始取数据
	 * @param pageSize
	 *            返回的条数
	 * @return
	 */
	public List<ServiceInfo> queryServiceList(@Param("serviceCondition")ServiceInfo serviceCondition, @Param("rowIndex") int rowIndex,
			@Param("pageSize") int pageSize);

	/**
	 * 返回queryServiceList总数
	 * 
	 * @param serviceCondition
	 * @return
	 */
	public int queryServiceCount(@Param("serviceCondition")ServiceInfo serviceCondition);
	/**
	 * 通过service id查询服务
	 * @param serviceId
	 * @return
	 */
	public ServiceInfo queryByServiceId(long serviceId);

	/**
	 * 添加服务
	 * @param serviceInfo
	 * @return
	 */
	public int  insertService(ServiceInfo serviceInfo);
	/**
	 * 批量添加服务
	 * @param serviceInfo
	 * @return
	 */
	public int  insertServiceInfo(List<ServiceInfo> serviceList);
	
	/**
	 * 更新服务信息
	 * @param serviceInfo
	 * @return
	 */
	public int updateService(ServiceInfo serviceInfo);

	/**
	 * 删除服务信息
	 * @param serviceInfo
	 * @return
	 */
	public int deleteService(long serviceId);
}
