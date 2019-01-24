package com.graduation.ss.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.graduation.ss.dao.HelpDao;
import com.graduation.ss.dto.HelpExecution;
import com.graduation.ss.entity.Help;
import com.graduation.ss.enums.HelpStateEnum;
import com.graduation.ss.exceptions.HelpOperationException;
import com.graduation.ss.service.HelpService;
import com.graduation.ss.util.PageCalculator;

@Service
public class HelpServiceImpl implements HelpService {
	@Autowired
	private HelpDao helpDao;

	@Override
	public HelpExecution getHelpList(Help helpCondition, int pageIndex, int pageSize) {
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的帮助列表
		List<Help> helpList = helpDao.queryHelpList(helpCondition, rowIndex, pageSize);
		// 依据相同的查询条件，返回帮助总数
		int count = helpDao.queryHelpCount(helpCondition);
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
		try {
			// 给帮助信息赋初始值
			help.setHelpStatus(0);
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

}
