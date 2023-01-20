package no.nav.tiltakspenger.scheduler.jobs

import mu.KotlinLogging
import no.nav.helse.rapids_rivers.RapidsConnection
import org.quartz.Job
import org.quartz.Scheduler
import org.quartz.spi.JobFactory
import org.quartz.spi.TriggerFiredBundle
import kotlin.reflect.jvm.jvmName

private val LOG = KotlinLogging.logger {}

@Suppress("ReturnCount")
class PublishingJobFactory(
    private val rapidsConnection: RapidsConnection
) : JobFactory {

    override fun newJob(bundle: TriggerFiredBundle, scheduler: Scheduler): Job {
        LOG.info("Creating new job")
        val jobClass = bundle.jobDetail.jobClass
        if (jobClass.name == DailyPassageOfTimeEventsPublishingJob::class.jvmName) {
            LOG.info("Creating DailyPassageOfTimeEventsPublishingJob")
            return DailyPassageOfTimeEventsPublishingJob(rapidsConnection)
        }
        if (jobClass.name == HourlyPassageOfTimeEventsPublishingJob::class.jvmName) {
            LOG.info("Creating HourlyPassageOfTimeEventsPublishingJob")
            return HourlyPassageOfTimeEventsPublishingJob(rapidsConnection)
        }
        throw NotImplementedError("Job Factory error, job not found")
    }
}
