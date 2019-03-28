package com.graduation.ss.config.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.graduation.ss.task.AppealCheckTask;
import com.graduation.ss.task.OrderCheckTask;

@Configuration
public class QuartzConfig {
	// 配置定时任务1
    @Bean(name = "appealCheckTaskTrigger")
    public JobDetail appealCheckTaskDetail() {
        return JobBuilder.newJob(AppealCheckTask.class).withIdentity("AppealCheckTask").storeDurably().build();
    }
   

    // 配置触发器1
    @Bean(name = "appealCheckTaskTrigger")
    public Trigger appealCheckTaskTrigger() {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 */1 * * * ?");
        return TriggerBuilder.newTrigger().forJob(appealCheckTaskDetail())
                .withIdentity("AppealCheckTask")
                .withSchedule(scheduleBuilder)
                .build();
    }
    @Bean(name = "orderCheckTaskDetail")
    public JobDetail orderCheckTaskDetail() {
        return JobBuilder.newJob(OrderCheckTask.class).withIdentity("OrderCheckTask").storeDurably().build();
    }

    @Bean(name = "orderCheckTaskTrigger")
    public Trigger orderCheckTaskTrigger() {
        //5秒执行一次
      //  SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
      //          .withIntervalInSeconds(5)
       //         .repeatForever();
    	 CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 */1 * * * ?");
        return TriggerBuilder.newTrigger().forJob(orderCheckTaskDetail())
                .withIdentity("OrderCheckTask")
                .withSchedule(scheduleBuilder)
                .build();
    }
}
