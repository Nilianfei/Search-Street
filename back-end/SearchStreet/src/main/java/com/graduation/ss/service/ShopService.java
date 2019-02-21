package com.graduation.ss.service;

import java.util.Date;

import com.graduation.ss.dto.ImageHolder;
import com.graduation.ss.dto.ShopExecution;
import com.graduation.ss.entity.Shop;
import com.graduation.ss.exceptions.ShopOperationException;

public interface ShopService {
	/**
	 * 根据shopCondition分页返回相应店铺列表
	 * 
	 * @param shopCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);

	/**
	 * 根据经纬范围返回附近店铺列表
	 * 
	 * @param maxlat
	 * @param minlat
	 * @param maxlng
	 * @param minlng
	 * @return
	 */
	public ShopExecution getNearbyShopList(float maxlat, float minlat, float maxlng, float minlng);

	/**
	 * 通过店铺Id获取店铺信息
	 * 
	 * @param shopId
	 * @return
	 */
	Shop getByShopId(long shopId);

	/**
	 * 更新店铺信息，不包括对图片的处理
	 * 
	 * @param shop
	 * @param shopImg
	 * @return
	 * @throws ShopOperationException
	 */
	ShopExecution modifyShop(Shop shop) throws ShopOperationException;

	/**
	 * 上传店铺图片
	 * 
	 * @param shopId
	 * @param shopImg
	 * @param businessLicenseImg
	 * @param profileImg
	 * @return
	 * @throws ShopOperationException
	 */
	ShopExecution uploadImg(long shopId, ImageHolder shopImg, ImageHolder businessLicenseImg, ImageHolder profileImg, Date createTime)
			throws ShopOperationException;

	/**
	 * 注册店铺信息，不包括对图片的处理
	 * 
	 * @param shop
	 * @param shopImgInputStream
	 * @param fileName
	 * @return
	 * @throws ShopOperationException
	 */
	ShopExecution addShop(Shop shop) throws ShopOperationException;
}
