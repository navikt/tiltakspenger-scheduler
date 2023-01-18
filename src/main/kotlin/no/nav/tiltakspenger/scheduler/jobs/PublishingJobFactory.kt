package no.nav.tiltakspenger.scheduler.jobs

import no.nav.helse.rapids_rivers.RapidsConnection
import org.quartz.Job
import org.quartz.Scheduler
import org.quartz.spi.JobFactory
import org.quartz.spi.TriggerFiredBundle
import kotlin.reflect.jvm.jvmName

class PublishingJobFactory(
    private val rapidsConnection: RapidsConnection
) : JobFactory {

    override fun newJob(bundle: TriggerFiredBundle, scheduler: Scheduler): Job {
        val jobClass = bundle.jobDetail.jobClass
        if (jobClass.name == PassageOfTimeEventsPublishingJob::class.jvmName) {
            return PassageOfTimeEventsPublishingJob(rapidsConnection)
        }
        throw NotImplementedError("Job Factory error")
    }
}
