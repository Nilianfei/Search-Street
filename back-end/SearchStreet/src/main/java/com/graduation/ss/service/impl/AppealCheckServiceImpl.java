package com.graduation.ss.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.graduation.ss.dao.AppealDao;
import com.graduation.ss.dao.HelpDao;
import com.graduation.ss.entity.Appeal;
import com.graduation.ss.entity.Help;
import com.graduation.ss.exceptions.AppealOperationException;
import com.graduation.ss.exceptions.HelpOperationException;
import com.graduation.ss.service.AppealCheckService;

@Service
public class AppealCheckServiceImpl implements AppealCheckService {
	@Autowired
	private AppealDao appealDao;
	@Autowired
	private HelpDao helpDao;

	@Override
	@Transactional
	public void checkAppeal() throws AppealOperationException {
		Appeal appealCondition = new Appeal();
		appealCondition.setAppealStatus(0);
		List<Appeal> appealList = appealDao.queryAppealList(appealCondition);
		Date now = new Date();
		for (Appeal appeal : appealList) {
			if (appeal.getEndTime().before(now)) {
				appeal.setAppealStatus(3);
				try {
					int effectedNum = appealDao.updateAppeal(appeal);
					if (effectedNum <= 0) {
						throw new AppealOperationException("求助修改失败");
					}
				} catch (Exception e) {
					throw new AppealOperationException("modifyAppeal error:" + e.toString());
				}
			}
		}
	}

	@Override
	@Transactional
	public void checkHelp() throws HelpOperationException {
		Help helpCondition = new Help();
		helpCondition.setHelpStatus(0);
		List<Help> helpList=helpDao.queryHelpList(helpCondition);
		Date now=new Date();
		for(Help help:helpList){
			if(help.getEndTime().before(now)){
				help.setHelpStatus(3);
				try {
					int effectedNum = helpDao.updateHelp(help);
					if (effectedNum <= 0) {
						throw new HelpOperationException("帮助修改失败");
					}
				} catch (Exception e) {
					throw new HelpOperationException("modifyHelp error:" + e.toString());
				}
			}
		}
	}

}
