package com.graduation.ss.config.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.graduation.ss.task.AppealCheckTask;

@Configuration
public class QuartzConfig {
    @Bean
    public JobDetail appealCheckTaskDetail() {
        return JobBuilder.newJob(AppealCheckTask.class).withIdentity("AppealCheckTask").storeDurably().build();
    }

    @Bean
    public Trigger appealCheckTaskTrigger() {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 */1 * * * ?");
        return TriggerBuilder.newTrigger().forJob(appealCheckTaskDetail())
                .withIdentity("AppealCheckTask")
                .withSchedule(scheduleBuilder)
                .build();
    }
}
