package com.graduation.ss.task;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.graduation.ss.service.OrderCheckService;

@DisallowConcurrentExecution
public class OrderCheckTask extends QuartzJobBean {
	@Autowired
	private OrderCheckService orderCheckService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			orderCheckService.checkOrder();
		} catch (Exception e) {
			throw new JobExecutionException(e.getMessage());
		}
	}

}
