package no.nav.tiltakspenger.scheduler.jobs

import mu.KotlinLogging
import no.nav.helse.rapids_rivers.RapidsConnection
import org.quartz.Job
import org.quartz.Scheduler
import org.quartz.spi.JobFactory
import org.quartz.spi.TriggerFiredBundle
import kotlin.reflect.jvm.jvmName

private val log = KotlinLogging.logger {}

@Suppress("ReturnCount")
class PublishingJobFactory(
    private val rapidsConnection: RapidsConnection
) : JobFactory {

    override fun newJob(bundle: TriggerFiredBundle, scheduler: Scheduler): Job {
        log.info("Creating new job")
        val jobClass = bundle.jobDetail.jobClass
        if (jobClass.name == PassageOfTimeEventsPublishingJob::class.jvmName) {
            log.info("Creating PassageOfTimeEventsPublishingJob")
            return PassageOfTimeEventsPublishingJob()
        }
        if (jobClass.name == DailyPassageOfTimeEventsPublishingJob::class.jvmName) {
            log.info("Creating DailyPassageOfTimeEventsPublishingJob")
            return DailyPassageOfTimeEventsPublishingJob(rapidsConnection)
        }
        if (jobClass.name == HourlyPassageOfTimeEventsPublishingJob::class.jvmName) {
            log.info("Creating HourlyPassageOfTimeEventsPublishingJob")
            return HourlyPassageOfTimeEventsPublishingJob(rapidsConnection)
        }
        throw NotImplementedError("Job Factory error")
    }
}
