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
import com.graduation.ss.dto.ImageHolder;
import com.graduation.ss.entity.Appeal;
import com.graduation.ss.entity.AppealImg;
import com.graduation.ss.entity.Help;
import com.graduation.ss.entity.PersonInfo;
import com.graduation.ss.enums.AppealStateEnum;
import com.graduation.ss.exceptions.AppealOperationException;
import com.graduation.ss.service.AppealService;
import com.graduation.ss.util.ImageUtil;
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
	public AppealExecution getAppealList(Appeal appealCondition) throws AppealOperationException{
		List<Appeal> appealList = appealDao.queryAppealList(appealCondition);
		AppealExecution ae = new AppealExecution();
		if (appealCondition.getAppealStatus() != null
				&& (appealCondition.getAppealStatus() == 1 || appealCondition.getAppealStatus()==0)) {
			if (appealList != null) {
				Date today = new Date();
				Iterator<Appeal> iter = appealList.iterator();
				while (iter.hasNext()) {
					Appeal value = iter.next();
					if (value.getEndTime().getTime() < today.getTime()) {// 修改已过时失效的求助
						value.setAppealStatus(3);
						try {
							int effectedNum=appealDao.updateAppeal(value);
							if (effectedNum <= 0) {
								throw new AppealOperationException("求助修改失败");
							}
						} catch (Exception e) {
							throw new AppealOperationException("modifyAppeal error:" + e.getMessage());
						}
						iter.remove();
					}
				}
			} else {
				ae.setState(AppealStateEnum.INNER_ERROR.getState());
				return ae;
			}
		}
		if (appealList != null) {
			ae.setAppealList(appealList);
			ae.setCount(appealList.size());
			ae.setState(AppealStateEnum.SUCCESS.getState());
		} else {
			ae.setState(AppealStateEnum.INNER_ERROR.getState());
		}
		return ae;
	}

	@Override
	public AppealExecution getNearbyAppealList(float maxlat, float minlat, float maxlng, float minlng, String appealTitle) {
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
	public Appeal getByAppealId(Long appealId) throws AppealOperationException{
		Appeal appeal = appealDao.queryByAppealId(appealId);
		if(appeal==null) {
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
		if(appeal==null) {
			throw new AppealOperationException("completeAppeal error:" + "appealId无效");
		}
		souCoin = appeal.getSouCoin();
		try {
			PersonInfo personInfo = personInfoDao.queryPersonInfoByUserId(appealUserId);
			Long appealerSouCoin = personInfo.getSouCoin();
			personInfo.setSouCoin(appealerSouCoin - souCoin);
			int effectedNum = personInfoDao.updatePersonInfo(personInfo);
			if (effectedNum <= 0) {
				throw new AppealOperationException("扣除求助者搜币失败");
			}
			Help help = helpDao.queryByHelpId(helpId);
			if(help==null) {
				throw new AppealOperationException("completeAppeal error:" + "helpId无效");
			}
			Long helpUserId = help.getUserId();
			personInfo = personInfoDao.queryPersonInfoByUserId(helpUserId);
			if(personInfo==null) {
				throw new AppealOperationException("completeAppeal error:" + "帮助者不存在");
			}
			appealerSouCoin = personInfo.getSouCoin();
			personInfo.setSouCoin(appealerSouCoin + souCoin);
			effectedNum = personInfoDao.updatePersonInfo(personInfo);
			if (effectedNum <= 0) {
				throw new AppealOperationException("增加帮助者搜币失败");
			}
			appeal.setAppealStatus(2);
			effectedNum = appealDao.updateAppeal(appeal);
			if (effectedNum <= 0) {
				throw new AppealOperationException("修改求助状态失败");
			}
			help.setHelpStatus(2);
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
	@Transactional
	public void additionSouCoin(Long helpId, Long appealUserId, Long additionSouCoin) throws AppealOperationException {
		if (appealUserId == null) {
			throw new AppealOperationException("additinoSouCoin error:" + "缺少userId");
		}
		if (helpId == null) {
			throw new AppealOperationException("additinoSouCoin error:" + "缺少helpId");
		}

		try {
			PersonInfo personInfo = personInfoDao.queryPersonInfoByUserId(appealUserId);
			if(personInfo==null) {
				throw new AppealOperationException("additionSouCoin error:" + "appealUserId无效");
			}
			Long appealerSouCoin = personInfo.getSouCoin();
			personInfo.setSouCoin(appealerSouCoin - additionSouCoin);
			int effectedNum = personInfoDao.updatePersonInfo(personInfo);
			if (effectedNum <= 0) {
				throw new AppealOperationException("扣除求助者搜币失败");
			}
			Help help = helpDao.queryByHelpId(helpId);
			if(help==null) {
				throw new AppealOperationException("additionSouCoin error:" + "helpId无效");
			}
			Long helpUserId = help.getUserId();
			personInfo = personInfoDao.queryPersonInfoByUserId(helpUserId);
			if(personInfo==null) {
				throw new AppealOperationException("additionSouCoin error:" + "帮助者不存在");
			}
			appealerSouCoin = personInfo.getSouCoin();
			personInfo.setSouCoin(appealerSouCoin + additionSouCoin);
			effectedNum = personInfoDao.updatePersonInfo(personInfo);
			if (effectedNum <= 0) {
				throw new AppealOperationException("增加帮助者搜币失败");
			}
			help.setAdditionalCoin(additionSouCoin);
			effectedNum = helpDao.updateHelp(help);
			if(effectedNum<=0) {
				throw new AppealOperationException("修改帮助追赏金失败");
			}
		} catch (Exception e) {
			throw new AppealOperationException("additinoSouCoin error:" + e.getMessage());
		}
	}

}
