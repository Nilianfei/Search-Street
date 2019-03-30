package com.graduation.ss.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.graduation.ss.entity.Shop;

public interface ShopDao {
	/**
	 * 分页查询店铺，可输入的条件有：店铺名(模糊),店铺状态，省份，城市，地区，是否移动，user
	 * 
	 * @param shopCondition
	 * @param rowIndex      从第几行开始取数据
	 * @param pageSize      返回的条数
	 * @return
	 */
	List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition, @Param("rowIndex") int rowIndex,
			@Param("pageSize") int pageSize);

	/**
	 * 根据经纬度范围查找店铺
	 * @param maxlat
	 * @param minlat
	 * @param maxlng
	 * @param minlng
	 * @return
	 */
	List<Shop> queryNearbyShopList(@Param("maxlat") float maxlat, @Param("minlat") float minlat,
			@Param("maxlng") float maxlng, @Param("minlng") float minlng, @Param("shopName") String shopName);

	/**
	 * 返回queryShopList总数
	 * 
	 * @param shopCondition
	 * @return
	 */
	int queryShopCount(@Param("shopCondition") Shop shopCondition);

	/**
	 * 通过shop id查询店铺
	 * 
	 * @param shopId
	 * @return
	 */
	Shop queryByShopId(long shopId);

	/**
	 * 添加店铺
	 * 
	 * @param shop
	 * @return
	 */
	int insertShop(Shop shop);

	/**
	 * 更新店铺信息
	 * 
	 * @param shop
	 * @return
	 */
	int updateShop(Shop shop);
}
