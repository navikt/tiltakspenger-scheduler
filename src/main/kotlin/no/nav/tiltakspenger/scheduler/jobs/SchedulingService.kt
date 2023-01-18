package no.nav.tiltakspenger.scheduler.jobs

import no.nav.helse.rapids_rivers.RapidsConnection
import no.nav.tiltakspenger.scheduler.Configuration
import org.quartz.CronScheduleBuilder.dailyAtHourAndMinute
import org.quartz.JobBuilder.newJob
import org.quartz.JobDetail
import org.quartz.SimpleScheduleBuilder
import org.quartz.TriggerBuilder.newTrigger

class SchedulingService(
    databaseConfig: Configuration.DatabaseConfig,
    rapidsConnection: RapidsConnection
) {
    private val publisherJobFactory = PublishingJobFactory(rapidsConnection)
    private val scheduler =
        JobSchedulerManager(databaseConfig = databaseConfig, jobFactory = publisherJobFactory).scheduler()

    private val trigger = newTrigger()
        .withIdentity("dailyTrigger", "onlyGroup")
        .withSchedule(dailyAtHourAndMinute(0, 0))
        .build()

    private val testTrigger = newTrigger()
        .withIdentity("testTrigger", "onlyGroup")
        .withSchedule(
            SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(1)
                .withRepeatCount(2)
        )
        .build()

    private var job: JobDetail = newJob(PassageOfTimeEventsPublishingJob::class.java)
        .withIdentity("dailyJob", "onlyGroup")
        .build()

    fun scheduleJob() {
        scheduler.scheduleJob(job, trigger)
    }

    fun scheduleTestJob() {
        scheduler.scheduleJob(job, testTrigger)
    }
}
