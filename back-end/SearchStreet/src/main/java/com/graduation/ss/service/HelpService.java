package com.graduation.ss.service;

import java.util.Date;

import com.graduation.ss.dto.HelpExecution;
import com.graduation.ss.entity.Help;
import com.graduation.ss.exceptions.HelpOperationException;

public interface HelpService {
	/**
	 * 根据helpCondition分页返回相应帮助列表
	 * 
	 * @param helpCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public HelpExecution getHelpListFY(Help helpCondition, Date startTime, Date endTime, int pageIndex, int pageSize);

	/**
	 * 根据helpCondition获取相应HelpList
	 * 
	 * @param appealId
	 * @return
	 */
	public HelpExecution getHelpList(Help helpCondition);

	/**
	 * 通过帮助ID获取帮助信息
	 * 
	 * @param helpId
	 * @return
	 */
	Help getByHelpId(long helpId);

	/**
	 * 更新帮助信息，主要是更新帮助状态以及添加求助者评价
	 * 
	 * @param help
	 * @return
	 * @throws HelpOperationException
	 */
	HelpExecution modifyHelp(Help help) throws HelpOperationException;

	/**
	 * 添加帮助
	 * 
	 * @param help
	 * @return
	 * @throws HelpOperationException
	 */
	HelpExecution addHelp(Help help) throws HelpOperationException;

	/**
	 * 选择帮助者
	 * 
	 * @param helpId
	 * @param appealId
	 * @throws HelpOperationException
	 */
	HelpExecution selectHelp(Long helpId, Long appealId, Long appealUserId) throws HelpOperationException;

	/**
	 * 追赏金
	 * 
	 * @param helpId
	 * @param appealUserId
	 * @param additionSouCoin
	 */
	HelpExecution additionSouCoin(Long helpId, Long appealUserId, Long additionSouCoin) throws HelpOperationException;
}
