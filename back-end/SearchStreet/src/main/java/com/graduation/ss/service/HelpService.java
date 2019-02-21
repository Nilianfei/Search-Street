package com.graduation.ss.service;

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
	public HelpExecution getHelpList(Help helpCondition, int pageIndex, int pageSize);

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
	
	void selectHelp(Long helpId,Long appealId) throws HelpOperationException;
}
