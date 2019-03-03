package com.graduation.ss.task;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.graduation.ss.service.AppealCheckService;

@DisallowConcurrentExecution
public class AppealCheckTask extends QuartzJobBean {
	@Autowired
	private AppealCheckService appealCheckService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			appealCheckService.checkAppeal();
			appealCheckService.checkHelp();
		} catch (Exception e) {
			throw new JobExecutionException(e.getMessage());
		}
	}

}
