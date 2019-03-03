package com.graduation.ss.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.graduation.ss.dao.AppealDao;
import com.graduation.ss.dao.HelpDao;
import com.graduation.ss.dto.HelpExecution;
import com.graduation.ss.entity.Appeal;
import com.graduation.ss.entity.Help;
import com.graduation.ss.enums.HelpStateEnum;
import com.graduation.ss.exceptions.HelpOperationException;
import com.graduation.ss.service.HelpService;
import com.graduation.ss.util.PageCalculator;

@Service
public class HelpServiceImpl implements HelpService {
	@Autowired
	private HelpDao helpDao;
	@Autowired
	private AppealDao appealDao;

	@Override
	public HelpExecution getHelpListFY(Help helpCondition, Date startTime, Date endTime, int pageIndex, int pageSize) {
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的帮助列表
		List<Help> helpList = helpDao.queryHelpListFY(helpCondition, startTime, endTime, rowIndex, pageSize);
		// 依据相同的查询条件，返回帮助总数
		int count = helpDao.queryHelpCount(helpCondition, startTime, endTime);
		HelpExecution he = new HelpExecution();
		if (helpList != null) {
			he.setHelpList(helpList);
			he.setCount(count);
		} else {
			he.setState(HelpStateEnum.INNER_ERROR.getState());
		}
		return he;
	}

	@Override
	public Help getByHelpId(long helpId) {
		return helpDao.queryByHelpId(helpId);
	}

	@Override
	public HelpExecution modifyHelp(Help help) throws HelpOperationException {
		// 空值判断
		if (help == null) {
			return new HelpExecution(HelpStateEnum.NULL_HELP);
		}
		try {
			int effectedNum = helpDao.updateHelp(help);
			if (effectedNum <= 0) {
				throw new HelpOperationException("帮助修改失败");
			}
		} catch (Exception e) {
			throw new HelpOperationException("modifyHelp error:" + e.getMessage());
		}
		return new HelpExecution(HelpStateEnum.SUCCESS, help);
	}

	@Override
	public HelpExecution addHelp(Help help) throws HelpOperationException {
		// 空值判断
		if (help == null) {
			return new HelpExecution(HelpStateEnum.NULL_HELP);
		}
		Long userId = help.getUserId();
		if (userId == null) {
			return new HelpExecution(HelpStateEnum.NULL_USERID);
		}
		Long appealId = help.getAppealId();
		if (appealId == null) {
			return new HelpExecution(HelpStateEnum.NULL_APPEALID);
		}
		try {
			Help helpCondition = new Help();
			Float avgCompletion = 0f;
			Float avgEfficiency = 0f;
			Float avgAttitude = 0f;
			helpCondition.setUserId(userId);
			helpCondition.setHelpStatus(2);// 查找已完成的帮助
			List<Help> helpList = helpDao.queryHelpList(helpCondition);
			for (Help help1 : helpList) {
				avgCompletion += help1.getCompletion();
				avgEfficiency += help1.getEfficiency();
				avgAttitude += help1.getAttitude();
			}
			int helpSize = helpList.size();
			if (helpSize > 0) {
				avgCompletion = avgCompletion / helpSize;
				avgAttitude = avgAttitude / helpSize;
				avgEfficiency = avgEfficiency / helpSize;
			}
			// 给帮助信息赋上以前的评价
			help.setAvgAttitude(avgAttitude);
			help.setAvgCompletion(avgCompletion);
			help.setAvgEfficiency(avgEfficiency);
			Appeal appeal = appealDao.queryByAppealId(appealId);
			if (appeal == null) {
				throw new HelpOperationException("addHelp error:" + "appealId无效");
			}
			help.setEndTime(appeal.getEndTime());
			// 给帮助信息赋初始值
			help.setHelpStatus(0);
			help.setAdditionalCoin(0l);
			help.setAttitude(0);
			help.setEfficiency(0);
			help.setCompletion(0);
			// 添加帮助信息
			int effectedNum = helpDao.insertHelp(help);
			if (effectedNum <= 0) {
				throw new HelpOperationException("帮助创建失败");
			}
		} catch (Exception e) {
			throw new HelpOperationException("addHelp error:" + e.getMessage());
		}
		return new HelpExecution(HelpStateEnum.SUCCESS, help);
	}

	/*@Override
	public HelpExecution getHelpList(Help helpCondition) {
		List<Help> helpList = helpDao.queryHelpList(helpCondition);
		HelpExecution he = new HelpExecution();
		if (helpCondition.getHelpStatus() != null
				&& (helpCondition.getHelpStatus() == 1 || helpCondition.getHelpStatus() == 0)) {
			if (helpList != null) {
				Date today = new Date();
				Iterator<Help> iter = helpList.iterator();
				while (iter.hasNext()) {
					Help value = iter.next();
					if (value.getEndTime().getTime() < today.getTime()) {// 修改已过时失效的求助
						value.setHelpStatus(3);
						try {
							int effectedNum = helpDao.updateHelp(value);
							if (effectedNum <= 0) {
								throw new AppealOperationException("帮助修改失败");
							}
						} catch (Exception e) {
							throw new AppealOperationException("modifyAppeal error:" + e.getMessage());
						}
						iter.remove();
					}
				}
			} else {
				he.setState(AppealStateEnum.INNER_ERROR.getState());
				return he;
			}
		}
		if (helpList != null) {
			he.setHelpList(helpList);
			he.setCount(helpList.size());
		} else {
			he.setState(HelpStateEnum.INNER_ERROR.getState());
		}
		return he;
	}*/

	@Override
	@Transactional
	public void selectHelp(Long helpId, Long appealId) throws HelpOperationException {
		Help helpCondition = new Help();
		helpCondition.setAppealId(appealId);
		try {
			List<Help> helpList = helpDao.queryHelpList(helpCondition);
			for (Help help : helpList) {
				help.setHelpStatus(3);// 将所有帮助设为无效
				try {
					int effectedNum = helpDao.updateHelp(help);
					if (effectedNum <= 0) {
						throw new HelpOperationException("修改帮助状态失败");
					}
				} catch (Exception e) {
					throw new HelpOperationException("selectHelp error:" + e.getMessage());
				}
			}
			Help help = new Help();
			help.setHelpId(helpId);
			help.setHelpStatus(1);// 将选择的帮助设为已确定
			try {
				int effectedNum = helpDao.updateHelp(help);
				if (effectedNum <= 0) {
					throw new HelpOperationException("修改帮助状态失败");
				}
			} catch (Exception e) {
				throw new HelpOperationException("selectHelp error:" + e.getMessage());
			}
			Appeal appeal = new Appeal();
			appeal.setAppealId(appealId);
			appeal.setAppealStatus(1);
			try {
				int effectedNum = appealDao.updateAppeal(appeal);
				if (effectedNum <= 0) {
					throw new HelpOperationException("修改求助状态失败");
				}
			} catch (Exception e) {
				throw new HelpOperationException("selectHelp error:" + e.getMessage());
			}
		} catch (Exception e) {
			throw new HelpOperationException(e.getMessage());
		}
	}

}
