package com.graduation.ss.dao;

import java.util.List;

import com.graduation.ss.entity.ShopImg;


public interface ShopImgDao {
	/**
	 * 列出某个店铺的详情图列表
	 * 
	 * @param shopId
	 * @return
	 */
	List<ShopImg> getShopImgList(long shopId);

	/**
	 * 批量添加店铺详情图片
	 * 
	 * @param productImgList
	 * @return
	 */
	int batchInsertShopImg(List<ShopImg> shopImgList);

	/**
	 * 删除指定店铺下的所有详情图
	 * 
	 * @param shopId
	 * @return
	 */
	int deleteShopImgByShopId(long shopId);
}
