package com.graduation.ss.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.graduation.ss.dao.ShopDao;
import com.graduation.ss.dao.ShopImgDao;
import com.graduation.ss.dto.ImageHolder;
import com.graduation.ss.dto.ShopExecution;
import com.graduation.ss.entity.Shop;
import com.graduation.ss.entity.ShopImg;
import com.graduation.ss.enums.ShopStateEnum;
import com.graduation.ss.exceptions.ShopOperationException;
import com.graduation.ss.service.ShopService;
import com.graduation.ss.util.ImageUtil;
import com.graduation.ss.util.PageCalculator;
import com.graduation.ss.util.PathUtil;

@Service
public class ShopServiceImpl implements ShopService {
	@Autowired
	private ShopDao shopDao;
	@Autowired
	private ShopImgDao shopImgDao;

	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
		//将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		//依据查询条件，调用dao层返回相关的店铺列表
		List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		//依据相同的查询条件，返回店铺总数
		int count = shopDao.queryShopCount(shopCondition);
		ShopExecution se = new ShopExecution();
		if (shopList != null) {
			se.setShopList(shopList);
			se.setCount(count);
		} else {
			se.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		return se;
	}

	@Override
	public Shop getByShopId(long shopId) {
		return shopDao.queryByShopId(shopId);
	}

	@Override
	@Transactional
	public ShopExecution modifyShop(Shop shop, ImageHolder shopImg, ImageHolder businessLicenseImg,
			ImageHolder profileImg) throws ShopOperationException {
		if (shop == null || shop.getShopId() == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		} else {
			// 1.判断是否需要处理图片
			try {
				Shop tempShop = shopDao.queryByShopId(shop.getShopId());
				if (shopImg != null && shopImg.getImage() != null && shopImg.getImageName() != null
						&& !"".equals(shopImg.getImageName())) {
					addShopImg(shop, shopImg);
				}
				if (businessLicenseImg != null && businessLicenseImg.getImage() != null && businessLicenseImg.getImageName() != null
						&& !"".equals(businessLicenseImg.getImageName())) {
					if(tempShop.getBusinessLicenseImg() != null) {
						ImageUtil.deleteFileOrPath(tempShop.getBusinessLicenseImg());
					}
					addBusinessLicenseImg(shop, businessLicenseImg);
				}
				if (profileImg != null && profileImg.getImage() != null && profileImg.getImageName() != null
						&& !"".equals(profileImg.getImageName())) {
					if(tempShop.getProfileImg() != null) {
						ImageUtil.deleteFileOrPath(tempShop.getProfileImg());
					}
					addProfileImg(shop, profileImg);
				}
				// 2.更新店铺信息
				shop.setLastEditTime(new Date());
				int effectedNum = shopDao.updateShop(shop);
				if (effectedNum <= 0) {
					return new ShopExecution(ShopStateEnum.INNER_ERROR);
				} else {
					shop = shopDao.queryByShopId(shop.getShopId());
					return new ShopExecution(ShopStateEnum.SUCCESS, shop);
				}
			} catch (Exception e) {
				throw new ShopOperationException("modifyShop error:" + e.getMessage());
			}
		}
	}

	@Override
	@Transactional
	public ShopExecution addShop(Shop shop) throws ShopOperationException {
		// 空值判断
		if (shop == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}
		try {
			// 给店铺信息赋初始值
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			// 添加店铺信息
			int effectedNum = shopDao.insertShop(shop);
			if (effectedNum <= 0) {
				throw new ShopOperationException("店铺创建失败");
			} 
		} catch (Exception e) {
			throw new ShopOperationException("addShop error:" + e.getMessage());
		}
		return new ShopExecution(ShopStateEnum.CHECK, shop);
	}

	private void addShopImg(Shop shop, ImageHolder shopImgHolder) {
		// 获取图片存储路径，这里直接存放到相应店铺的文件夹底下
		String dest = PathUtil.getShopImgPath(shop.getShopId());
		String imgAddr = ImageUtil.generateNormalImg(shopImgHolder, dest);
		ShopImg shopImg = new ShopImg();
		shopImg.setImgAddr(imgAddr);
		shopImg.setShopId(shop.getShopId());
		shopImg.setCreateTime(new Date());
		try {
			int effectedNum = shopImgDao.insertShopImg(shopImg);
			if (effectedNum <= 0) {
				throw new ShopOperationException("创建店铺详情图片失败");
			}
		} catch (Exception e) {
			throw new ShopOperationException("创建店铺详情图片失败:" + e.toString());
		}
	}
	
	/**
	 * 删除某个店铺下的所有详情图
	 * 
	 * @param shopId
	 */
	@SuppressWarnings("unused")
	private void deleteShopImgList(long shopId) {
		// 根据shopId获取原来的图片
		List<ShopImg> shopImgList = shopImgDao.getShopImgList(shopId);
		// 干掉原来的图片
		for (ShopImg shopImg : shopImgList) {
			ImageUtil.deleteFileOrPath(shopImg.getImgAddr());
		}
		// 删除数据库里原有图片的信息
		shopImgDao.deleteShopImgByShopId(shopId);
	}
	
	private void addBusinessLicenseImg(Shop shop, ImageHolder businessLicenseImg){
		//获取businessLicenseImg图片目录的相对值路径
		String dest = PathUtil.getShopBusinessLicenseImgPath(shop.getShopId());
		String businessLicenseImgAddr = ImageUtil.generateNormalImg(businessLicenseImg, dest);
		shop.setBusinessLicenseImg(businessLicenseImgAddr);
	}
	
	private void addProfileImg(Shop shop, ImageHolder profileImg){
		//获取profileImg图片目录的相对值路径
		String dest = PathUtil.getShopProfileImgPath(shop.getShopId());
		String profileImgAddr = ImageUtil.generateThumbnail(profileImg, dest);
		shop.setProfileImg(profileImgAddr);
	}
}
