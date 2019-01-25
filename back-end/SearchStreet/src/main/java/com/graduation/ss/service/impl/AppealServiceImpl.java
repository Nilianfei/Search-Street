package com.graduation.ss.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.graduation.ss.dao.AppealDao;
import com.graduation.ss.dao.AppealImgDao;
import com.graduation.ss.dto.AppealExecution;
import com.graduation.ss.dto.ImageHolder;
import com.graduation.ss.entity.Appeal;
import com.graduation.ss.entity.AppealImg;
import com.graduation.ss.enums.AppealStateEnum;
import com.graduation.ss.exceptions.AppealOperationException;
import com.graduation.ss.service.AppealService;
import com.graduation.ss.util.ImageUtil;
import com.graduation.ss.util.PageCalculator;
import com.graduation.ss.util.PathUtil;

@Service
public class AppealServiceImpl implements AppealService {
	@Autowired
	private AppealDao appealDao;
	@Autowired
	private AppealImgDao appealImgDao;

	@Override
	public AppealExecution getAppealList(Appeal appealCondition, int pageIndex, int pageSize) {
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的求助列表
		List<Appeal> appealList = appealDao.queryAppealList(appealCondition, rowIndex, pageSize);
		// 依据相同的查询条件，返回求助总数
		int count = appealDao.queryAppealCount(appealCondition);
		AppealExecution ae = new AppealExecution();
		if (appealList != null) {
			ae.setAppealList(appealList);
			ae.setCount(count);
		} else {
			ae.setState(AppealStateEnum.INNER_ERROR.getState());
		}
		return ae;
	}

	@Override
	public AppealExecution getNearbyAppealList(float maxlat, float minlat, float maxlng, float minlng) {
		List<Appeal> appealList = appealDao.queryNearbyAppealList(maxlat, minlat, maxlng, minlng);
		AppealExecution ae = new AppealExecution();
		if (appealList != null) {
			ae.setAppealList(appealList);
			ae.setCount(appealList.size());
		} else {
			ae.setState(AppealStateEnum.INNER_ERROR.getState());
		}
		return ae;
	}

	@Override
	public Appeal getByAppealId(long appealId) {
		return appealDao.queryByAppealId(appealId);
	}

	@Override
	public void uploadImg(long appealId, ImageHolder appealImg) throws AppealOperationException {
		try {
			if (appealImg != null && appealImg.getImage() != null && appealImg.getImageName() != null
					&& !"".equals(appealImg.getImageName())) {
				addAppealImg(appealId, appealImg);
			}
		} catch (Exception e) {
			throw new AppealOperationException("uploadAppealImg error:" + e.getMessage());
		}
	}

	private void addAppealImg(long appealId, ImageHolder appealImgHolder) {
		// 获取图片存储路径，这里直接存放到相应求助的文件夹底下
		String dest = PathUtil.getAppealImgPath(appealId);
		String imgAddr = ImageUtil.generateNormalImg(appealImgHolder, dest);
		AppealImg appealImg = new AppealImg();
		appealImg.setImgAddr(imgAddr);
		appealImg.setAppealId(appealId);
		appealImg.setCreateTime(new Date());
		try {
			int effectedNum = appealImgDao.insertAppealImg(appealImg);
			if (effectedNum <= 0) {
				throw new AppealOperationException("创建求助详情图片失败");
			}
		} catch (Exception e) {
			throw new AppealOperationException("创建求助详情图片失败:" + e.toString());
		}
	}

	@Override
	public AppealExecution addAppeal(Appeal appeal) throws AppealOperationException {
		// 空值判断
		if (appeal == null) {
			return new AppealExecution(AppealStateEnum.NULL_APPEAL);
		}
		try {
			// 给求助信息赋初始值
			appeal.setAppealStatus(0);
			// 添加求助信息
			int effectedNum = appealDao.insertAppeal(appeal);
			if (effectedNum <= 0) {
				throw new AppealOperationException("求助创建失败");
			}
		} catch (Exception e) {
			throw new AppealOperationException("addAppeal error:" + e.getMessage());
		}
		return new AppealExecution(AppealStateEnum.SUCCESS, appeal);
	}

	@Override
	public AppealExecution modifyAppeal(Appeal appeal) throws AppealOperationException {
		// 空值判断
		if (appeal == null) {
			return new AppealExecution(AppealStateEnum.NULL_APPEAL);
		}
		try {
			int effectedNum = appealDao.updateAppeal(appeal);
			if (effectedNum <= 0) {
				throw new AppealOperationException("求助修改失败");
			}
		} catch (Exception e) {
			throw new AppealOperationException("modifyAppeal error:" + e.getMessage());
		}
		return new AppealExecution(AppealStateEnum.SUCCESS, appeal);
	}

}
