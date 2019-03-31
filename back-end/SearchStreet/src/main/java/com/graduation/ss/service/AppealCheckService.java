package com.graduation.ss.service;

import com.graduation.ss.exceptions.AppealOperationException;
import com.graduation.ss.exceptions.HelpOperationException;

public interface AppealCheckService {

	/**
	 * 检测进行中的appeal是否过期，如果过期，则修改appeal的状态
	 * @throws AppealOperationException
	 */
	public void checkAppeal()throws AppealOperationException;
	
	/**
	 * 检测进行中的help是否过期，如果过期，则修改help的状态
	 * @throws HelpOperationException
	 */
	public void checkHelp()throws HelpOperationException;
}
