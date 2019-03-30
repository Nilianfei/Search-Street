package com.graduation.ss.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.graduation.ss.dao.AppealDao;
import com.graduation.ss.dao.AppealImgDao;
import com.graduation.ss.dao.HelpDao;
import com.graduation.ss.dao.PersonInfoDao;
import com.graduation.ss.dto.AppealExecution;
import com.graduation.ss.dto.AppealImgExecution;
import com.graduation.ss.dto.ImageHolder;
import com.graduation.ss.entity.Appeal;
import com.graduation.ss.entity.AppealImg;
import com.graduation.ss.entity.Help;
import com.graduation.ss.entity.PersonInfo;
import com.graduation.ss.enums.AppealStateEnum;
import com.graduation.ss.exceptions.AppealOperationException;
import com.graduation.ss.exceptions.ShopOperationException;
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
	@Autowired
	private PersonInfoDao personInfoDao;
	@Autowired
	private HelpDao helpDao;

	@Override
	@Transactional
	public AppealExecution getAppealListFY(Appeal appealCondition, int pageIndex, int pageSize)
			throws AppealOperationException {
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Appeal> appealList = appealDao.queryAppealListFY(appealCondition, rowIndex, pageSize);
		// 依据相同的查询条件，返回帮助总数
		int count = appealDao.queryAppealCount(appealCondition);
		AppealExecution ae = new AppealExecution();
		if (appealList != null) {
			ae.setAppealList(appealList);
			ae.setCount(count);
			ae.setState(AppealStateEnum.SUCCESS.getState());
		} else {
			ae.setState(AppealStateEnum.INNER_ERROR.getState());
		}
		return ae;
	}

	@Override
	public AppealExecution getNearbyAppealList(float maxlat, float minlat, float maxlng, float minlng,
			String appealTitle) {
		List<Appeal> appealList = appealDao.queryNearbyAppealList(maxlat, minlat, maxlng, minlng, appealTitle);
		AppealExecution ae = new AppealExecution();
		if (appealList != null) {
			Date today = new Date();
			Iterator<Appeal> iter = appealList.iterator();
			while (iter.hasNext()) {
				Appeal value = iter.next();
				if (value.getEndTime().getTime() < today.getTime()) {// 去除已过时失效的求助
					iter.remove();
				}
			}
			ae.setAppealList(appealList);
			ae.setCount(appealList.size());
		} else {
			ae.setState(AppealStateEnum.INNER_ERROR.getState());
		}
		return ae;
	}

	@Override
	public Appeal getByAppealId(Long appealId) throws AppealOperationException {
		Appeal appeal = appealDao.queryByAppealId(appealId);
		if (appeal == null) {
			throw new AppealOperationException("getByAppealId error:" + "appealId无效");
		}
		Date today = new Date();
		if (appeal.getEndTime().getTime() < today.getTime()) {// 当求助失效时，将求助改为已过时失效状态
			appeal.setAppealStatus(3);
			try {
				int effectedNum = appealDao.updateAppeal(appeal);
				if (effectedNum <= 0) {
					throw new AppealOperationException("求助修改失败");
				}
			} catch (Exception e) {
				throw new AppealOperationException("modifyAppeal error:" + e.getMessage());
			}
		}
		return appeal;
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
		Long userId = appeal.getUserId();
		PersonInfo personInfo = personInfoDao.queryPersonInfoByUserId(userId);
		Long souCoin = personInfo.getSouCoin();
		if (appeal.getSouCoin() > souCoin) {
			return new AppealExecution(AppealStateEnum.SOUCOIN_LACK);
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

	@Override
	@Transactional
	public AppealExecution completeAppeal(Long appealId, Long helpId, Long appealUserId)
			throws AppealOperationException {
		if (appealId == null) {
			return new AppealExecution(AppealStateEnum.NULL_APPEALID);
		}
		if (appealUserId == null) {
			return new AppealExecution(AppealStateEnum.NULL_USERID);
		}
		if (helpId == null) {
			return new AppealExecution(AppealStateEnum.NULL_HELPID);
		}
		Long souCoin = 0l;
		Appeal appeal = appealDao.queryByAppealId(appealId);
		if (appeal == null) {
			throw new AppealOperationException("completeAppeal error:" + "appealId无效");
		}
		souCoin = appeal.getSouCoin();
		try {
			PersonInfo appealPersonInfo = personInfoDao.queryPersonInfoByUserId(appealUserId);
			Long appealerSouCoin = appealPersonInfo.getSouCoin();
			Help help = helpDao.queryByHelpId(helpId);
			if (help == null) {
				throw new AppealOperationException("helpId无效");
			}
			Long helpUserId = help.getUserId();
			PersonInfo helpPersonInfo = personInfoDao.queryPersonInfoByUserId(helpUserId);
			if (helpPersonInfo == null) {
				throw new AppealOperationException("帮助者不存在");
			}

			if (appealerSouCoin < souCoin) {
				return new AppealExecution(AppealStateEnum.SOUCOIN_LACK);
			}
			if (appeal.getAppealStatus() != 1) {
				return new AppealExecution(AppealStateEnum.COMPLETE_ERR);
			}
			if (help.getHelpStatus() != 1) {
				return new AppealExecution(AppealStateEnum.COMPLETE_ERR);
			}

			appealPersonInfo.setSouCoin(appealerSouCoin - souCoin);
			int effectedNum = personInfoDao.updatePersonInfo(appealPersonInfo);
			if (effectedNum <= 0) {
				throw new AppealOperationException("扣除求助者搜币失败");
			}

			Long helperSouCoin = helpPersonInfo.getSouCoin();
			helpPersonInfo.setSouCoin(helperSouCoin + souCoin);
			effectedNum = personInfoDao.updatePersonInfo(helpPersonInfo);
			if (effectedNum <= 0) {
				throw new AppealOperationException("增加帮助者搜币失败");
			}

			appeal.setAppealStatus(2);
			effectedNum = appealDao.updateAppeal(appeal);
			if (effectedNum <= 0) {
				throw new AppealOperationException("修改求助状态失败");
			}

			help.setHelpStatus(2);
			help.setAllCoin(souCoin);
			effectedNum = helpDao.updateHelp(help);
			if (effectedNum <= 0) {
				throw new AppealOperationException("修改帮助状态失败");
			}
		} catch (Exception e) {
			throw new AppealOperationException("completeAppeal error:" + e.getMessage());
		}
		AppealExecution ae = new AppealExecution(AppealStateEnum.SUCCESS, appeal);
		return ae;
	}

	@Override
	public AppealExecution cancelAppeal(Long userId, Long appealId) throws AppealOperationException {
		if (userId == null) {
			return new AppealExecution(AppealStateEnum.NULL_USERID);
		}
		if (appealId == null) {
			return new AppealExecution(AppealStateEnum.NULL_APPEALID);
		}
		Appeal appeal = appealDao.queryByAppealId(appealId);
		if (appeal.getUserId() != userId) {
			return new AppealExecution(AppealStateEnum.NOT_USER_APPEAL);
		}
		if (appeal.getAppealStatus() != 0) {
			return new AppealExecution(AppealStateEnum.NOT_CANCEL);
		}

		Help help = new Help();
		try {
			help.setAppealId(appealId);
			int count = helpDao.queryHelpList(help).size();
			if (count != 0) {
				return new AppealExecution(AppealStateEnum.NOT_CANCEL);
			} else {
				appeal.setAppealStatus(3);
				int effectedNum = appealDao.updateAppeal(appeal);
				if (effectedNum < 0) {
					throw new AppealOperationException("修改求助状态失败");
				} else {
					return new AppealExecution(AppealStateEnum.SUCCESS, appeal);
				}
			}
		} catch (Exception e) {
			throw new AppealOperationException("cancelAppeal error:" + e.getMessage());
		}
	}

	@Override
	@Transactional
	public AppealExecution disableAppeal(Long userId, Long appealId) throws AppealOperationException {
		if (userId == null) {
			return new AppealExecution(AppealStateEnum.NULL_USERID);
		}
		if (appealId == null) {
			return new AppealExecution(AppealStateEnum.NULL_APPEALID);
		}
		Appeal appeal = appealDao.queryByAppealId(appealId);
		if (appeal.getUserId() != userId) {
			return new AppealExecution(AppealStateEnum.NOT_USER_APPEAL);
		}
		if (appeal.getAppealStatus() != 1) {
			return new AppealExecution(AppealStateEnum.NOT_DISABLE);
		}

		Help help = new Help();
		try {
			help.setAppealId(appealId);
			List<Help> helps = helpDao.queryHelpList(help);
			for (Help help2 : helps) {
				if (help2.getHelpStatus() == 1) {
					help2.setHelpStatus(3);
					int effectedNum = helpDao.updateHelp(help2);
					if (effectedNum <= 0) {
						throw new AppealOperationException("修改帮助状态失败");
					}
					break;
				}
			}
			appeal.setAppealStatus(2);
			int effectedNum = appealDao.updateAppeal(appeal);
			if (effectedNum < 0) {
				throw new AppealOperationException("修改求助状态失败");
			} else {
				return new AppealExecution(AppealStateEnum.SUCCESS, appeal);
			}
		} catch (Exception e) {
			throw new AppealOperationException("disableAppeal error:" + e.getMessage());
		}
	}

	@Override
	public AppealImgExecution getAppealImgList(AppealImg appealImg, int pageIndex, int pageSize) {
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的商铺图片列表
		List<AppealImg> appealImgList = appealImgDao.queryAppealImgList(appealImg, rowIndex, pageSize);
		// 依据相同的查询条件，返回商铺图片总数
		int count = appealImgDao.queryAppealImgCount(appealImg);
		AppealImgExecution aie = new AppealImgExecution();
		if (appealImgList != null) {
			aie.setAppealImgList(appealImgList);
			aie.setCount(count);
		} else {
			aie.setState(AppealStateEnum.INNER_ERROR.getState());
		}
		return aie;
	}

	@Override
	public void delAppealImg(long appealImgId) throws AppealOperationException {
		if (appealImgId >= 0) {
			AppealImg appealImg = appealImgDao.getAppealImg(appealImgId);
			if (appealImg != null) {
				ImageUtil.deleteFileOrPath(appealImg.getImgAddr());
				int effectedNum = appealImgDao.deleteAppealImgById(appealImgId);
				if (effectedNum <= 0) {
					throw new ShopOperationException("删除图片失败");
				}
			}
		}
	}

	@Override
	public void createAppealImg(long appealId, ImageHolder appealImgHolder) throws AppealOperationException {
		addAppealImg(appealId, appealImgHolder);
	}

}
