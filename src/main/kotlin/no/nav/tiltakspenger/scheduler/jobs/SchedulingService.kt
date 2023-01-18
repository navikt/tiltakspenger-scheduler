package no.nav.tiltakspenger.scheduler.jobs

import no.nav.helse.rapids_rivers.RapidsConnection
import no.nav.tiltakspenger.scheduler.Configuration
import org.quartz.CronScheduleBuilder.dailyAtHourAndMinute
import org.quartz.JobBuilder.newJob
import org.quartz.JobDetail
import org.quartz.ScheduleBuilder
import org.quartz.TriggerBuilder.newTrigger

class SchedulingService(
    databaseConfig: Configuration.DatabaseConfig,
    rapidsConnection: RapidsConnection,
    private val scheduleBuilder: ScheduleBuilder<*> = dailyAtHourAndMinute(0, 0)
) {
    private val publisherJobFactory = PublishingJobFactory(rapidsConnection)
    private val scheduler =
        JobSchedulerManager(databaseConfig = databaseConfig, jobFactory = publisherJobFactory).scheduler()

    private val trigger = newTrigger()
        .withIdentity("dailyTrigger", "onlyGroup")
        .withSchedule(scheduleBuilder)
        .build()

    private var job: JobDetail = newJob(PassageOfTimeEventsPublishingJob::class.java)
        .withIdentity("dailyJob", "onlyGroup")
        .build()

    fun scheduleJob() {
        scheduler.deleteJob(job.key)
        scheduler.scheduleJob(job, trigger)
    }

    fun start() {
        scheduler.start()
    }

    fun stop() {
        scheduler.standby()
    }
}
