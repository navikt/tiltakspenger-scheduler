package no.nav.tiltakspenger.scheduler.jobs

import no.nav.helse.rapids_rivers.RapidsConnection
import no.nav.tiltakspenger.scheduler.Configuration
import org.quartz.CronScheduleBuilder.cronSchedule
import org.quartz.CronScheduleBuilder.dailyAtHourAndMinute
import org.quartz.JobBuilder.newJob
import org.quartz.JobDetail
import org.quartz.ScheduleBuilder
import org.quartz.TriggerBuilder.newTrigger

class SchedulingService(
    databaseConfig: Configuration.DatabaseConfig,
    rapidsConnection: RapidsConnection,
    scheduleBuilder: ScheduleBuilder<*> =
        dailyAtHourAndMinute(0, 0) // .withMisfireHandlingInstructionFireAndProceed()
) {

    companion object {
        private const val ON_THE_HOUR_EVERY_HOUR = "0 0 * * * ?"
    }

    private val publisherJobFactory = PublishingJobFactory(rapidsConnection)
    private val scheduler =
        JobSchedulerManager(databaseConfig = databaseConfig, jobFactory = publisherJobFactory).scheduler()

    private val dailyTrigger = newTrigger()
        .withIdentity("dailyTrigger", "onlyGroup")
        .withSchedule(scheduleBuilder)
        .build()

    private val hourlySchedule = cronSchedule(ON_THE_HOUR_EVERY_HOUR) // .withMisfireHandlingInstructionFireAndProceed())
    private val hourlyTrigger = newTrigger()
        .withIdentity("hourlyTrigger", "onlyGroup")
        .withSchedule(hourlySchedule)
        .build()

    private var dailyJob: JobDetail = newJob(DailyPassageOfTimeEventsPublishingJob::class.java)
        .withIdentity("dailyJob", "onlyGroup")
        .build()

    private var hourlyJob: JobDetail = newJob(HourlyPassageOfTimeEventsPublishingJob::class.java)
        .withIdentity("hourlyJob", "onlyGroup")
        .build()

    fun scheduleJob() {
        scheduler.deleteJob(dailyJob.key)
        scheduler.deleteJob(hourlyJob.key)
        scheduler.scheduleJob(dailyJob, dailyTrigger)
        scheduler.scheduleJob(hourlyJob, hourlyTrigger)
    }

    fun start() {
        scheduler.start()
    }

    fun stop() {
        scheduler.standby()
    }
}
