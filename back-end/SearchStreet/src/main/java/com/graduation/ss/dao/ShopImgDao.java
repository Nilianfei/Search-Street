package com.graduation.ss.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

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
	 * 添加店铺详情图片
	 * 
	 * @param shopImg
	 * @return
	 */
	int insertShopImg(ShopImg shopImg);

	/**
	 * 删除指定店铺下的所有详情图
	 * 
	 * @param shopId
	 * @return
	 */
	int deleteShopImgByShopIdAndCreateTime(@Param("shopId")long shopId, @Param("createTime")Date createTime);
}
