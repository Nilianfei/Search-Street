package com.graduation.ss.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.graduation.ss.entity.Appeal;

public interface AppealDao {
	/**
	 * 根据经纬度范围查找求助
	 * 
	 * @param maxlat
	 * @param minlat
	 * @param maxlng
	 * @param minlng
	 * @return
	 */
	List<Appeal> queryNearbyAppealList(@Param("maxlat") float maxlat, @Param("minlat") float minlat,
			@Param("maxlng") float maxlng, @Param("minlng") float minlng);

	/**
	 * 分页查询求助，可输入的条件有：求助名（模糊），求助状态，省份，城市，地区，求助者的用户ID
	 * 
	 * @param appealCondition
	 * @param rowIndex        从第几行开始取数据
	 * @param pageSize        返回的条数
	 * @return
	 */
	List<Appeal> queryAppealList(@Param("appealCondition") Appeal appealCondition, @Param("rowIndex") int rowIndex,
			@Param("pageSize") int pageSize);

	/**
	 * 返回queryAppealList总数
	 * 
	 * @param appealCondition
	 * @return
	 */
	int queryAppealCount(@Param("appealCondition") Appeal appealCondition);

	/**
	 * 通过appeal id查询求助
	 * 
	 * @param appealId
	 * @return
	 */
	Appeal queryByAppealId(long appealId);

	/**
	 * 添加求助
	 * 
	 * @param appeal
	 * @return
	 */
	int insertAppeal(Appeal appeal);

	/**
	 * 更新求助信息
	 * 
	 * @param appeal
	 * @return
	 */
	int updateAppeal(Appeal appeal);
}
