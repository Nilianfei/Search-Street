package com.graduation.ss.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.graduation.ss.entity.Service;;

public interface ServiceDao{
	/**
	 * 分页查询服务，可输入的条件有：服务名（模糊），店铺id
	 * 
	 * @param serviceCondition
	 * @param rowIndex
	 *            从第几行开始取数据
	 * @param pageSize
	 *            返回的条数
	 * @return
	 */
	public List<Service> queryServiceList(@Param("serviceCondition")Service serviceCondition, @Param("rowIndex") int rowIndex,
			@Param("pageSize") int pageSize);

	/**
	 * 返回queryServiceList总数
	 * 
	 * @param serviceCondition
	 * @return
	 */
	public int queryServiceCount(@Param("serviceCondition")Service serviceCondition);
	/**
	 * 通过service id查询店铺
	 * @param serviceId
	 * @return
	 */
	public Service queryByServiceId(long serviceId);
	/**
	 * 通过shop id查询店铺
	 * @param shopId
	 * @return
	 */
	public List<Service> queryByShopId(long shopId);
	/**
	 * 添加服务
	 * @param service
	 * @return
	 */
	public int  insertService(Service service);
	/**
	 * 批量添加服务
	 * @param service
	 * @return
	 */
	public int  insertServiceInfo(List<Service> serviceList);
	
	/**
	 * 更新店铺信息
	 * @param service
	 * @return
	 */
	public int updateService(Service service);

	/**
	 * 删除店铺信息
	 * @param service
	 * @return
	 */
	public int deleteService(Service service);
}
