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
import com.graduation.ss.dto.ShopImgExecution;
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
	public ShopExecution getNearbyShopList(float maxlat, float minlat, float maxlng, float minlng, String shopName) {
		List<Shop> shopList = shopDao.queryNearbyShopList(maxlat, minlat, maxlng, minlng, shopName);
		ShopExecution se = new ShopExecution();
		if (shopList != null) {
			se.setShopList(shopList);
			se.setCount(shopList.size());
		} else {
			se.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		return se;
	}

	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的店铺列表
		List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		// 依据相同的查询条件，返回店铺总数
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
	public ShopExecution uploadImg(Long shopId, ImageHolder shopImg, ImageHolder businessLicenseImg,
			ImageHolder profileImg, Date createTime) throws ShopOperationException {
		if(shopId==null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOPID);
		}
		Shop shop = new Shop();
		shop.setShopId(shopId);
		try {
			Shop tempShop = shopDao.queryByShopId(shopId);
			if (shopImg != null && shopImg.getImage() != null && shopImg.getImageName() != null
					&& !"".equals(shopImg.getImageName())) {
				if (createTime != null) {
					deleteShopImgList(shopId, createTime);
				} else {
					return new ShopExecution(ShopStateEnum.NULL_SHOPIMG_CREATETIME);
				}
				addShopImg(shopId, shopImg, createTime);
			}
			if (businessLicenseImg != null && businessLicenseImg.getImage() != null
					&& businessLicenseImg.getImageName() != null && !"".equals(businessLicenseImg.getImageName())) {
				if (tempShop.getBusinessLicenseImg() != null) {
					ImageUtil.deleteFileOrPath(tempShop.getBusinessLicenseImg());
				}
				addBusinessLicenseImg(shop, businessLicenseImg);
			}
			if (profileImg != null && profileImg.getImage() != null && profileImg.getImageName() != null
					&& !"".equals(profileImg.getImageName())) {
				if (tempShop.getProfileImg() != null) {
					ImageUtil.deleteFileOrPath(tempShop.getProfileImg());
				}
				addProfileImg(shop, profileImg);
			}
			shop.setLastEditTime(new Date());
			int effectedNum = shopDao.updateShop(shop);
			if (effectedNum <= 0) {
				return new ShopExecution(ShopStateEnum.INNER_ERROR);
			} else {
				shop = shopDao.queryByShopId(shop.getShopId());
				return new ShopExecution(ShopStateEnum.SUCCESS, shop);
			}
		} catch (Exception e) {
			throw new ShopOperationException("modifyShop error:" + e.toString());
		}
	}

	@Override
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
			if (shop.getPerCost() == null || shop.getPerCost() <0) {
				shop.setPerCost(0);
			}
			// 添加店铺信息
			int effectedNum = shopDao.insertShop(shop);
			if (effectedNum <= 0) {
				throw new ShopOperationException("店铺创建失败");
			}
		} catch (Exception e) {
			throw new ShopOperationException("addShop error:" + e.toString());
		}
		return new ShopExecution(ShopStateEnum.CHECK, shop);
	}

	@Override
	public ShopExecution modifyShop(Shop shop) throws ShopOperationException {
		// 空值判断
		if (shop == null || shop.getShopId() == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}
		try {
			shop.setLastEditTime(new Date());
			if (shop.getPerCost() == null || shop.getPerCost() <0) {
				shop.setPerCost(0);
			}
			// 修改店铺信息
			int effectedNum = shopDao.updateShop(shop);
			if (effectedNum <= 0) {
				throw new ShopOperationException("店铺修改失败");
			}
		} catch (Exception e) {
			throw new ShopOperationException("modifyShop error:" + e.toString());
		}
		return new ShopExecution(ShopStateEnum.SUCCESS, shop);
	}

	private void addShopImg(long shopId, ImageHolder shopImgHolder, Date createTime) {
		// 获取图片存储路径，这里直接存放到相应店铺的文件夹底下
		String dest = PathUtil.getShopImgPath(shopId);
		String imgAddr = ImageUtil.generateNormalImg(shopImgHolder, dest);
		ShopImg shopImg = new ShopImg();
		shopImg.setImgAddr(imgAddr);
		shopImg.setShopId(shopId);
		shopImg.setCreateTime(createTime);
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
	 * 删除某个店铺下的所有旧的详情图
	 * 
	 * @param shopId
	 * @param createTime 最新上传详情图的时间
	 */
	private void deleteShopImgList(long shopId, Date createTime) {
		// 根据shopId获取原来的图片
		List<ShopImg> shopImgList = shopImgDao.getShopImgList(shopId);
		if (shopImgList != null && shopImgList.size() > 0) {
			// 干掉原来的图片
			for (ShopImg shopImg : shopImgList) {
				if (shopImg.getCreateTime().getTime() < createTime.getTime())
					ImageUtil.deleteFileOrPath(shopImg.getImgAddr());
			}
			// 删除数据库里原有图片的信息
			shopImgDao.deleteShopImgByShopIdAndCreateTime(shopId, createTime);
		}
	}

	private void addBusinessLicenseImg(Shop shop, ImageHolder businessLicenseImg) {
		// 获取businessLicenseImg图片目录的相对值路径
		String dest = PathUtil.getShopBusinessLicenseImgPath(shop.getShopId());
		String businessLicenseImgAddr = ImageUtil.generateNormalImg(businessLicenseImg, dest);
		shop.setBusinessLicenseImg(businessLicenseImgAddr);
	}

	private void addProfileImg(Shop shop, ImageHolder profileImg) {
		// 获取profileImg图片目录的相对值路径
		String dest = PathUtil.getShopProfileImgPath(shop.getShopId());
		String profileImgAddr = ImageUtil.generateThumbnail(profileImg, dest);
		shop.setProfileImg(profileImgAddr);
	}

	@Override
	@Transactional
	public ShopExecution addShop(Shop shop, ImageHolder businessLicenseImg, ImageHolder profileImg)
			throws ShopOperationException {
		// 空值判断
		if (shop == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}
		try {
			// 给店铺信息赋初始值
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			if (shop.getPerCost() == null || shop.getPerCost() <0) {
				shop.setPerCost(0);
			}
			// 添加店铺信息
			int effectedNum = shopDao.insertShop(shop);
			if (effectedNum <= 0) {
				throw new ShopOperationException("商铺创建失败");
			} else {
				if (businessLicenseImg != null && businessLicenseImg.getImage() != null
						&& businessLicenseImg.getImageName() != null && !"".equals(businessLicenseImg.getImageName())) {
					// 存储图片
					try {
						addBusinessLicenseImg(shop, businessLicenseImg);
					} catch (Exception e) {
						throw new ShopOperationException("addBusinessLicenseImg error:" + e.toString());
					}
				}
				if (profileImg != null && profileImg.getImage() != null && profileImg.getImageName() != null
						&& !"".equals(profileImg.getImageName())) {
					// 存储图片
					try {
						addProfileImg(shop, profileImg);
					} catch (Exception e) {
						throw new ShopOperationException("addProfileImg error:" + e.toString());
					}
				}
				// 更新店铺的图片地址
				effectedNum = shopDao.updateShop(shop);
				if (effectedNum <= 0) {
					throw new ShopOperationException("更新图片地址失败");
				}
			}
		} catch (Exception e) {
			throw new ShopOperationException("addShop error:" + e.toString());
		}
		return new ShopExecution(ShopStateEnum.CHECK, shop);
	}

	@Override
	public ShopExecution modifyShop(Shop shop, ImageHolder businessLicenseImg, ImageHolder profileImg)
			throws ShopOperationException {
		// 空值判断
		if (shop == null || shop.getShopId() == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}
		try {
			int effectedNum;
			Shop tempShop = shopDao.queryByShopId(shop.getShopId());
			if (businessLicenseImg != null && businessLicenseImg.getImage() != null
					&& businessLicenseImg.getImageName() != null && !"".equals(businessLicenseImg.getImageName())) {
				// 存储图片
				try {

					if (tempShop.getBusinessLicenseImg() != null) {
						ImageUtil.deleteFileOrPath(tempShop.getBusinessLicenseImg());
					}
					addBusinessLicenseImg(shop, businessLicenseImg);
				} catch (Exception e) {
					throw new ShopOperationException("addBusinessLicenseImg error:" + e.toString());
				}
			}
			if (profileImg != null && profileImg.getImage() != null && profileImg.getImageName() != null
					&& !"".equals(profileImg.getImageName())) {
				// 存储图片
				try {
					if (tempShop.getProfileImg() != null) {
						ImageUtil.deleteFileOrPath(tempShop.getProfileImg());
					}
					addProfileImg(shop, profileImg);
				} catch (Exception e) {
					throw new ShopOperationException("addProfileImg error:" + e.toString());
				}
			}
			// 更新店铺的图片地址
			shop.setLastEditTime(new Date());
			if (shop.getPerCost() == null || shop.getPerCost() <0) {
				shop.setPerCost(0);
			}

			effectedNum = shopDao.updateShop(shop);
			if (effectedNum <= 0) {
				throw new ShopOperationException("更新图片地址失败");
			}
		} catch (Exception e) {
			throw new ShopOperationException("modifyShop error:" + e.toString());
		}
		return new ShopExecution(ShopStateEnum.SUCCESS, shop);
	}

	@Override
	public ShopImgExecution delShopImg(Long shopImgId) throws ShopOperationException {
		if (shopImgId != null) {
			ShopImg shopImg = shopImgDao.getShopImg(shopImgId);
			if (shopImg != null) {
				ImageUtil.deleteFileOrPath(shopImg.getImgAddr());
				int effectedNum = shopImgDao.delShopImgByShopImgId(shopImgId);
				if (effectedNum <= 0) {
					throw new ShopOperationException("删除图片失败");
				}
			}
		} else {
			return new ShopImgExecution(ShopStateEnum.NULL_SHOPIMGID);
		}
		return new ShopImgExecution(ShopStateEnum.SUCCESS);
	}

	@Override
	public ShopImgExecution addShopImg(Long shopId, ImageHolder shopImgHolder) throws ShopOperationException {
		if (shopId == null) {
			return new ShopImgExecution(ShopStateEnum.NULL_SHOPID);
		}
		try {
			addShopImg(shopId, shopImgHolder, new Date());
		} catch (Exception e) {
			throw new ShopOperationException("addShopImg error:"+e.toString());
		}
		return new ShopImgExecution(ShopStateEnum.SUCCESS);
	}

	@Override
	public ShopImgExecution getShopImgList(ShopImg shopImg, int pageIndex, int pageSize) {
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的商铺图片列表
		List<ShopImg> shopImgList = shopImgDao.queryShopImgList(shopImg, rowIndex, pageSize);
		// 依据相同的查询条件，返回商铺图片总数
		int count = shopImgDao.queryShopImgCount(shopImg);
		ShopImgExecution sie = new ShopImgExecution();
		if (shopImgList != null) {
			sie.setShopImgList(shopImgList);
			sie.setCount(count);
		} else {
			sie.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		return sie;
	}

}
