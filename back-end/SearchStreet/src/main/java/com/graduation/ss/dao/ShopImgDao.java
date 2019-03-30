package com.graduation.ss.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.graduation.ss.entity.ShopImg;

public interface ShopImgDao {
	/**
	 * 列出某个商铺的详情图列表
	 * 
	 * @param shopId
	 * @return
	 */
	List<ShopImg> getShopImgList(long shopId);

	/**
	 * 获取商铺图片
	 * 
	 * @param shopImgId
	 * @return
	 */
	ShopImg getShopImg(long shopImgId);

	/**
	 * 添加商铺详情图片
	 * 
	 * @param shopImg
	 * @return
	 */
	int insertShopImg(ShopImg shopImg);

	/**
	 * 通过商铺图片ID删除商铺图片
	 * 
	 * @param shopImgId
	 * @return
	 */
	int delShopImgByShopImgId(long shopImgId);

	/**
	 * 分页获取商铺图片
	 * 
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	List<ShopImg> queryShopImgList(@Param("shopImgCondition") ShopImg shopImgCondition, @Param("rowIndex") int rowIndex,
			@Param("pageSize") int pageSize);

	/**
	 * 获取商铺图片总数
	 * 
	 * @return
	 */
	int queryShopImgCount(@Param("shopImgCondition") ShopImg shopImgCondition);

	/**
	 * 删除指定商铺下的所有详情图
	 * 
	 * @param shopId
	 * @return
	 */
	int deleteShopImgByShopIdAndCreateTime(@Param("shopId") long shopId, @Param("createTime") Date createTime);
}
